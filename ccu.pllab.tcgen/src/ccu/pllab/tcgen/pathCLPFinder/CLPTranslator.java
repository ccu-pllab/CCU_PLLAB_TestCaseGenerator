package ccu.pllab.tcgen.pathCLPFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.internal.core.SelectionRequestor;
import org.stringtemplate.v4.compiler.STParser.template_return;

import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.AbstractConstraint.*;
import ccu.pllab.tcgen.AbstractSyntaxTree.SymbolTable;
import ccu.pllab.tcgen.AbstractSyntaxTree.VariableToken;
import ccu.pllab.tcgen.clgGraph2Path.CLGPath;
import ccu.pllab.tcgen.clgGraph2Path.CLGPathEnumerator;
import ccu.pllab.tcgen.exe.main.Main;


public class CLPTranslator {

	private Map<String, Integer> variableSet;
	private HashMap<String, String> arraySet;
	private HashMap<String, Integer> arrayVariableSet;
	private int body_count = 1;
	private CLGNode startNode;
	private ArrayList<String> object_pre;
	private ArrayList<String> arg_pre;
	private ArrayList<String> object_post;
	private ArrayList<String> arg_post;
	private ArrayList<String> result;
	private String bodyCLP;
	private String arraycontent = "";
	private int arraycount = 0;
	private boolean usedMod;
	private int iterateTimes = -1;
	private String invCLP;
	private SymbolTable symbolTable;
	private ArrayList<String> attribute = new ArrayList<String>();
	private int uselength;
	private boolean twoD=false;
	private int row;
	private int col;
	private boolean bounded=false;
	private int bound;
	private boolean containException=false;
	
	public void setInvCLP(String invCLP) {
		this.invCLP = invCLP;
	}

	public void setSymbolTable(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	public void setAttribute(ArrayList<String> attribute) {
		this.attribute = attribute;
	}
	
	public void setBounded(int bound)
	{
		if(bound>=0)
		{
		this.bounded=true;
		this.bound=bound;
		}
		else
		{
			this.bounded=false;
		}
	}

	private final String delayMod = "delay o_mod(M,N,_) if nonground([M,N]).\n" + "o_mod(M,N,R):-\n" + "mod(M,N,R).";

	private final String findElement = "findelement(Sequence,X,Index):-\n" + "Index#>1,\n" + "Index1#=Index-1,\n" + "subsequence(Sequence,Sequence_post),\n" + "findelement(Sequence_post,X,Index1).\n"
			+ "findelement(Sequence,X,1):-\n" + "sequenceFirst(Sequence, X).\n" + "sequenceFirst([H|_], H).\n" + "subsequence([_|H], H).\n";

	private final String randomArray = "integerGen(N) :-\n" + "  random(R),\n" + "  mod(R, 32767, N).\n" + "intSequenceInstances(0, []).\n" + "intSequenceInstances(Size, [N|Seq]) :-\n"
			+ "  Size > 0,\n" + "  Size1 is Size -1,\n" + "  intSequenceInstances(Size1, Seq),\n" + "  integerGen(N).\n";

	private final String permutation = "del(A,[A|B],B).\n" + "del(A,[B|C],[B|D]):-\n" + "del(A,C,D).\n" +

			"list_permutation([],[]).\n" + "list_permutation(A,[B|C]):-\n" + "del(B,A,D),\n" + "list_permutation(D,C).\n";
	private final String randset = "randset(0, L,[]).\r\n" + "randset(Size,L,[N|Seq]) :-\r\n" + " Size > 0,\r\n" + "  Size1 is Size -1,\r\n" + "  randset(Size1,L,Seq),\r\n" + "  random(R),\r\n"
			+ "  mod(R, L, N).\n";
	private final String random_member = "random_member(D, A,E) :-\r\n" + "length(A, B),\r\n" + "random(N),\r\n" + "mod(N, B, C),\r\n" + "C1#=C+1,\r\n" + "findelement(A,D,C1),\r\n"
			+ "remove(D,A,E).\r\n" + "  \r\n" + "remove(H, [H], []).\r\n" + "remove(X, [H], [H]).\r\n" + "remove(X, [X|T],T ).\r\n" + "remove(X, [H|T], [H|S]):-\r\n" + "remove(X,T,S).\n";

	private final String findnsols = "findnsols(N, Term, Goal, Solutions) :-\r\n" + "    ( N < 1 ->\r\n" + "        Solutions = []\r\n" + "    ;\r\n" + "        record_create(Bag),\r\n"
			+ "		N1#=N+1,\r\n" + "        shelf_create(count(N1), Counter),\r\n" + "        ( \r\n" + "            once((\r\n" + "                call(Goal),\r\n"
			+ "                recordz(Bag, Term),\r\n" + "                \\+shelf_dec(Counter, 1)   % succeed if enough\r\n" + "            )),\r\n" + "            fail\r\n" + "        ;\r\n"
			+ "            recorded_list(Bag, Solutions)\r\n" + "        )\r\n" + "    ).\n";

	private final String findindex="findIndex([A|B],Value,I,C):-\r\n" + 
			"I#=C,A#=Value;C1#=C+1,findIndex(B,Value,I,C1).\r\n" + 
			"findIndex([],Value,-1).";
	private final String sortedArray="intSequenceInstances2(Size, Array) :-\r\n" + 
			"intSequenceInstances(Size, Seq),\r\n" + 
			"msort(Seq,Array).\n";
	
	private final String declareArray2="delay dcl_1dInt_array2(_, Size) if nonground([Size]).\ndcl_1dInt_array2(Array,Size) :-\r\n" + 
			"dcl_1dInt_array(Seq, Size),\r\n" + 
			"msort(Seq,Array).\n";
	private final String intarray="intColArray(Col,Array):-\r\n" + 
			"Col#>1,\r\n" + 
			"random(R),\r\n" + 
			"mod(R, 32767, N),\r\n" + 
			"arg(Col,Array,N),\r\n" + 
			"Col2#=Col-1,\r\n" + 
			"intColArray(Col2,Array).\r\n" + 
			"\r\n" + 
			"intColArray(1,Array):-\r\n" + 
			"random(R),\r\n" + 
			"mod(R, 32767, N),\r\n" + 
			"arg(1,Array,N).\r\n" + 
			"\r\n" + 
			"intArrayInstances(Row,Col,Array):-\r\n" + 
			"Row#>1,\r\n" + 
			"arg(Row,Array,Array2),\r\n" + 
			"intColArray(Col,Array2),\r\n" + 
			"Row2#=Row-1,\r\n" + 
			"intArrayInstances(Row2,Col,Array).\r\n" + 
			"\r\n" + 
			"intArrayInstances(1,Col,Array):-\r\n" + 
			"arg(1,Array,Array2),\r\n" + 
			"intColArray(Col,Array2).\n";
	
	private final String declareArray="labeling_1dInt_array([]).\r\n" + 
			"labeling_1dInt_array([X|L]) :-\r\n" + 
			"    indomain(X, random),\r\n" + 
			"    labeling_1dInt_array(L).\n";
	
	private final String labelingArray="delay dcl_1dInt_array(_, Size) if nonground([Size]).\ndcl_1dInt_array([], 0).\r\n" + 
			"dcl_1dInt_array([X|L], Size) :-\r\n" + 
			"    Size > 0,\r\n" + 
			"    X :: 0..32767,\r\n" + 
			"    Size1 is Size - 1,\r\n" + 
			"    dcl_1dInt_array(L, Size1).\n";
	
	private final String mysort="mysort(Seq,0).\nmysort(Seq,1).\r\n" + 
			"	mysort(Seq,Size):-\r\n" + 
			"	Size#>1,\r\n" + 
			"	Size2#=Size-1,\r\n" + 
			"	findelement(Seq,A,Size),\r\n" + 
			"	findelement(Seq,B,Size2),\r\n" + 
			"	B#=<A,\r\n" + 
			"	mysort(Seq,Size2).\n";
	private final String declare2DArray="dcl_2dInt_array(Array,0,0).\ndcl_2dInt_array(Array,Row,Col):-\r\n" + 
			"Row#>1,\r\n" + 
			"array_list(Array,TempArray),\r\n" + 
			"findelement(TempArray,Array2,Row),\r\n" + 
			"intColArray(Array2,Col),\r\n" + 
			"Row2#=Row-1,\r\n" + 
			"dcl_2dInt_array(Array,Row2,Col).\r\n" + 
			"\r\n" + 
			"dcl_2dInt_array(Array,1,Col):-\r\n" + 
			"array_list(Array,TempArray),\r\n" + 
			"findelement(TempArray,Array2,1),\r\n" + 
			"intColArray(Array2,Col).\r\n" + 
			"\r\n" + 
			"intColArray(Array,Col):-\r\n" + 
			"Col#>1,\r\n" + 
			"X::0..32767,\r\n" + 
			"array_list(Array,Array2),\r\n" + 
			"findelement(Array2,X,Col),\r\n" + 
			"Col2#=Col-1,\r\n" + 
			"intColArray(Array,Col2).\r\n" + 
			"\r\n" + 
			"intColArray(Array,1):-\r\n" + 
			"X::0..32767,\r\n" + 
			"array_list(Array,Array2),\r\n" + 
			"findelement(Array2,X,1).\n";
	
	private final String labeling2DArray="labeling_2dInt_array(Array,0,0).\nlabeling_2dInt_array(Array,Row,Col):-\r\n" + 
			"Row#>1,\r\n" + 
			"array_list(Array,TempArray),\r\n" + 
			"findelement(TempArray,Array2,Row),\r\n" + 
			"labelingColArray(Array2,Col),\r\n" + 
			"Row2#=Row-1,\r\n" + 
			"labeling_2dInt_array(Array,Row2,Col).\r\n" + 
			"\r\n" + 
			"labeling_2dInt_array(Array,1,Col):-\r\n" + 
			"array_list(Array,TempArray),\r\n" + 
			"findelement(TempArray,Array2,1),\r\n" + 
			"labelingColArray(Array2, Col).\r\n" + 
			"\r\n" + 
			"labelingColArray(Array,Col):-\r\n" + 
			"Col#>1,\r\n" + 
			"array_list(Array,TempArray),\r\n" + 
			"findelement(TempArray,X,Col),\r\n" + 
			"indomain(X,random),\r\n" + 
			"Col2#=Col-1,\r\n" + 
			"labelingColArray(Array,Col2).\r\n" + 
			"\r\n" + 
			"labelingColArray(Array,1):-\r\n" + 
			"array_list(Array,TempArray),\r\n" + 
			"findelement(TempArray,X,1),\r\n" + 
			"indomain(X,random).\n";
	
	private final String randomIndex="randomIndexArray(N, Array) :-\r\n" + 
			"    randomIndexArray(N, N, [], Array).\r\n" + 
			"\r\n" + 
			"randomIndexArray(_, 0, Array, Array) :-\r\n" + 
			"    labeling_1dInt_array(Array).\r\n" + 
			"randomIndexArray(N, I, Curr, Array) :-\r\n" + 
			"    I > 0,\r\n" + 
			"    I1 is I - 1,\r\n" + 
			"    X :: 1..N,\r\n" + 
			"    notEqualAll(X, Curr),\r\n" + 
			"    randomIndexArray(N, I1, [X|Curr], Array).\r\n" + 
			"\r\n" + 
			"notEqualAll(_, []).\r\n" + 
			"notEqualAll(X, [Y|L]) :-\r\n" + 
			"    X #\\= Y,\r\n" + 
			"    notEqualAll(X, L).\n";
	
	public CLPTranslator() {
		this.initial();

	}

	public void initial() {
		variableSet = new HashMap<String, Integer>();
		arraySet=new HashMap<String, String>();
		arrayVariableSet=new HashMap<String,Integer>();
		body_count = 1;
	}

	public int getPathNumber() {
		return this.body_count - 1;
	}

	public void setIterateTimes(int times) {
		this.iterateTimes = times;
	}

	public int getIterateTimes() {
		return this.iterateTimes ;
	}
	
	public void setArrayContent(String content) {
		this.arraycontent = content;
	}

	public String getArrayContent() {
		return this.arraycontent;
	}

	public void setArrayCount(int count) {
		this.arraycount = count;
	}

	public int getArrayCount() {
		return this.arraycount;
	}
public int getBodycount()
{
	return body_count;
}
	public String genPathCLP(CLGPath path) {
		this.usedMod = false;
		Main.issort=false;
		this.object_pre = new ArrayList<String>();
		this.arg_pre = new ArrayList<String>();
		this.object_post = new ArrayList<String>();
		this.arg_post = new ArrayList<String>();
		this.result = new ArrayList<String>();
		this.bodyCLP = "";
		containException=false;
		String completeCLP = "";
		completeCLP += this.importLibraryCLP();
		this.startNode = path.getPathNodes().get(0);
		Main.ifCLP = "";
		Main.head = "";
uselength=0;
		this.genBodyName((CLGStartNode) this.startNode, this.body_count, path.getPathNodes());// List<CLGNode> path加的
		String head = this.getHeadInfo();
		Main.head = head.substring(head.indexOf('('), head.indexOf(')') + 1);

		this.bodyCLP = this.genBodyCLP(path.getPathNodes(), body_count);
		if(bodyCLP.contains("Size#=(Size_pre+1)"))
		{
			uselength=1;
			bodyCLP=bodyCLP.replaceAll(",Size=Size_pre", "");
		}
		else if(bodyCLP.contains("Size#=(Size_pre-1)"))
		{
			uselength=2;
			bodyCLP=bodyCLP.replaceAll(",Size=Size_pre", "");
		}
		
		if(bodyCLP.contains("Exception"))
			containException=true;
		else
			head=head.replace("[Exception]", "[]");
		
		if(!bodyCLP.contains("Result"))
			head=head.replace("[Result]", "[]");
		completeCLP += head;
		completeCLP += this.bodyCLP;
		completeCLP += this.genWrapperCLP((CLGStartNode) path.getPathNodes().get(0), body_count);
		
		completeCLP += this.invCLP();
		completeCLP = completeCLP.replaceAll("\nAcc_pre,", "\nAcc=true,");
		completeCLP = completeCLP.replaceAll("\nAcc2_pre,", "\nAcc2=true,");
		if (Main.ifCLP.length() > 0)
			completeCLP += Main.ifCLP;
		if (completeCLP.contains("mod"))
			completeCLP += delayMod;
		
		if (completeCLP.contains("findnsols"))
			completeCLP += findnsols;
		//if (completeCLP.contains("intSequenceInstances2"))
		//	completeCLP+=sortedArray;
		if (completeCLP.contains("mysort"))
				completeCLP+=mysort;
		//if (completeCLP.contains("intSequence"))
		//	completeCLP += randomArray;
	//	if(completeCLP.contains("dcl_1dInt_array2"))
	//		completeCLP+=declareArray2;
		if(completeCLP.contains("dcl_1dInt_array"))
			completeCLP+=declareArray;
		if(completeCLP.contains("labeling_1dInt_array"))
			completeCLP+=labelingArray;
		if(completeCLP.contains("findIndex"))
			completeCLP+=findindex;
		if (completeCLP.contains("list_permutation"))
			completeCLP += permutation;
		if (completeCLP.contains("random_member"))
			completeCLP += random_member;
		//if(completeCLP.contains("Row"))
			//completeCLP+=intarray;
		if(completeCLP.contains("dcl_2dInt_array"))
			completeCLP+=declare2DArray;
		if(completeCLP.contains("labeling_2dInt_array"))
			completeCLP+=labeling2DArray;
		if(completeCLP.contains("randomIndexArray"))
			completeCLP+=randomIndex;
		if (completeCLP.contains("findelement"))
			completeCLP += findElement;
		this.body_count++;
		completeCLP=completeCLP.replaceAll("=-1", "=(-1)");
		if (this.object_post.size() > 0) {

			for (String object : this.object_post) {
				String temp = object.toLowerCase().charAt(0) + object.substring(1);
				for (VariableToken variableToken : Main.symbolTable.getAttribute()) {
					if (variableToken.getVariableName().equals(temp)) {
						if (variableToken.getType().contains("["))
							completeCLP=completeCLP.replaceAll(object.toUpperCase().charAt(0) + object.substring(1)+"#=", object.toUpperCase().charAt(0) + object.substring(1)+"=");
					}
				}
				}
		}
		if(completeCLP.contains("Sizepre"))
			completeCLP=completeCLP.replaceAll("Sizepre", "Size_pre");
		if(completeCLP.contains("Result#=ArrayData"))
			completeCLP=completeCLP.replaceAll("Result#=ArrayData", "Result=ArrayData");
		if(completeCLP.contains("Result#=")&&Main.issort)
			completeCLP=completeCLP.replaceAll("Result#=", "Result=");
		if(completeCLP.contains("Exception="))
			completeCLP=completeCLP.replaceAll("dcl_1dInt_array(Array_pre, ArraySize_pre),\n", "");
	//	if(completeCLP.contains("Index)"))
		//	completeCLP=completeCLP.replaceAll("Index)", "ArrayIndex)");
		System.out.println("complete:\n" + completeCLP);
		return completeCLP;
	}


	public String getHeadInfo() {
		String completeCLP = ((CLGStartNode) this.startNode).getClassName();
		completeCLP = completeCLP.toLowerCase().charAt(0) + completeCLP.substring(1);
		String methodname = ((CLGStartNode) this.startNode).getMethodName();
		completeCLP += methodname.toUpperCase().charAt(0) + methodname.substring(1);
		completeCLP += "_" + this.body_count;
		completeCLP += "(";
		int object_pre_size = this.object_pre.size();
		completeCLP += addHeadInfo(this.object_pre);
		
		completeCLP += "],";
		completeCLP += addHeadInfo(this.arg_pre);
		completeCLP += "],";
		if (!this.bodyCLP.contains("Exception=\"Exception\"")) {
			completeCLP += addHeadInfo(this.object_post);
			completeCLP += "],";
		} else
			completeCLP += "[],";

		completeCLP += addHeadInfo(this.arg_post);
		completeCLP += "],";
		completeCLP += "[Result],";
	
			completeCLP += "[Exception],";
			
		completeCLP = completeCLP.substring(0, completeCLP.length() - 1);
		completeCLP += "):-\n";
		for (String object : this.object_pre) {
			String temp = object.replaceAll("_pre", "");
			temp = temp.toLowerCase().charAt(0) + temp.substring(1);
			for (VariableToken variableToken : Main.symbolTable.getAttribute()) {
				if (variableToken.getVariableName().equals(temp)) {
					if (variableToken.getType().contains("]["))
					{
						String temptype=variableToken.getType();
						if (iterateTimes == -1)
						{
						completeCLP+="Row_pre#="+temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]"))+",\n";
						if(!temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]")).contains("x"))
						row=Integer.parseInt(temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]")));
						completeCLP+="Col_pre#="+temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]"))+",\n";
						if(!temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]")).contains("x"))
						col=Integer.parseInt(temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]")));
						}
						else
						{
							completeCLP +="Row_pre#=" + iterateTimes + ",\n";
							completeCLP+="Col_pre#="+iterateTimes+",\n";
							row=iterateTimes;
							col=iterateTimes;
						}
					}
				else if (variableToken.getType().contains("[")) {
						String arraySize = variableToken.getType();
						arraySize = arraySize.substring(arraySize.indexOf("[") + 1, arraySize.indexOf("]"));
						if (iterateTimes == -1)
							completeCLP += "Size_pre#=" + arraySize + ",\n";
						else
							completeCLP += "Size_pre#=" + iterateTimes + ",\n";
						if(bounded)
						{
							if(bound>-1)
							completeCLP+="Bound_pre#="+bound+",\n";
							else
								completeCLP+="Bound_pre#=0...bound,\n";
						}
					}
				}
			}

		}
		return completeCLP;
	}



	public String addHeadInfo(ArrayList<String> list) {
		String completeCLP = "[";
		if (list.size() > 0) {
			for (String index : list) {
				completeCLP += index + ",";
			}
			completeCLP = completeCLP.substring(0, completeCLP.length() - 1);
		}

		return completeCLP;
	}

	// *********
	private String importLibraryCLP() {
		String importLibrary = "";
		importLibrary += ":- lib(ic).\n";
		importLibrary += ":- lib(timeout).\n";
		return importLibrary;
	}

	private String invCLP() {
		String invPred = "";
		return invPred;
	}

	private String genWrapperCLP(CLGStartNode startNode, int pathNumber) {
		String wrapperCLP = "";
		/********/

		/********/
		wrapperCLP += this.genWrapperName(startNode);
		if(wrapperCLP.contains("Unsort")&&!wrapperCLP.contains("Search"))
			Main.issort=true;
		/********/
		wrapperCLP += this.genDomain(startNode);
		/********/
		boolean isarray = false;
	//	System.out.println("arraycount:"+arraycount);
		if ((arraycount == 0||!Main.issort)) {
			wrapperCLP += this.genStateAligned(startNode);
			/********/
			wrapperCLP += this.genInvariant();
			/********/

			wrapperCLP += this.genBodyCall(startNode, pathNumber);
		} else {
			if(this.object_pre.size()>0 &&!Main.msort)
			{
			String object_pre_content = "";
			String object_post_content = "";
		
			String new_className = startNode.getClassName();
			String new_methodName = startNode.getMethodName();
			new_className = new_className.toLowerCase().charAt(0) + new_className.substring(1);
			new_methodName = new_methodName.toUpperCase().charAt(0) + new_methodName.substring(1);
			if(Main.symbolTable.getAttributeMap().containsKey("size"))
			{
		if((iterateTimes==-1 &&!bounded)||(iterateTimes>1&&!bounded))
		{
			wrapperCLP += "Obj_pre1=[List1_pre" + ",Size_pre],\n";
			wrapperCLP += "Obj_post1=[List1,Size],\n";
			wrapperCLP += new_className + new_methodName + "_" + pathNumber + "(Obj_pre1,Arg_pre1,Obj_post1,Arg_post1,Result1,Exception),\n";
			wrapperCLP += "length(List1,Size),\n";
			wrapperCLP+="labeling_1dInt_array(Data_pre),\n";
			wrapperCLP+="msort(Data_pre,List2_pre),\n";
			wrapperCLP += "Obj_pre2=[List2_pre" + ",Size_pre],\n";
			wrapperCLP += "Obj_post2=[List2,Size],\n";
			wrapperCLP += new_className + new_methodName + "_" + pathNumber + "(Obj_pre2,Arg_pre2,Obj_post2,Arg_post2,Result2,Exception),\n";
			wrapperCLP += "length(List2,Size),\n";
			wrapperCLP+="reverse(List2_pre,List3_pre),\n";
			wrapperCLP += "Obj_pre3=[List3_pre" + ",Size_pre],\n";
			wrapperCLP += "Obj_post3=[List3,Size],\n";
			wrapperCLP += new_className + new_methodName + "_" + pathNumber + "(Obj_pre3,Arg_pre3,Obj_post3,Arg_post3,Result3,Exception),\n";
			wrapperCLP += "length(List3,Size),\n";
		/*	for (int lim = 1; lim <= 3 && lim <= 24; lim++) {
			wrapperCLP += "Obj_pre" + lim + "=[List" + lim + "_pre" + ",Size_pre],\n";
				wrapperCLP += "Obj_post" + lim + "=[List" + lim + ",Size],\n";
				wrapperCLP += new_className + new_methodName + "_" + pathNumber + "(Obj_pre" + lim + ",Arg_pre" + lim + ",Obj_post" + lim + ",Arg_post" + lim + ",Result" + lim + "),\n";
				wrapperCLP += "length(List" + lim + ",Size),\n";
			}*/
		}
			else
			{
				wrapperCLP += "Obj_pre1=[List1_pre,Size_pre],\n";
				wrapperCLP += "Obj_post1=[List1,Size],\n";
				wrapperCLP += new_className + new_methodName + "_" + pathNumber + "(Obj_pre1,Arg_pre1,Obj_post1,Arg_post1,Result1,Exception),\n";
				wrapperCLP += "length(List1,Size),\n";
				wrapperCLP+="labeling_1dInt_array(Data_pre),\n";
			}
			String wrapperObj = "";
		
				wrapperCLP += "Obj_pre=[List1_pre";
			
				if((iterateTimes==-1 &&!bounded)||(iterateTimes>1&&!bounded))
				for (int lim = 2; lim <= 3; lim++)
					wrapperCLP += ",List" + lim + "_pre";
				wrapperCLP += ",Size_pre],\n";
			
				if((iterateTimes==-1 &&!bounded)||(iterateTimes>1&&!bounded)) {
				for (int lim = 1; lim <= 3 && lim <= 24 ; lim++)
				wrapperObj += ",List" + lim;
			wrapperCLP += "List=[" + wrapperObj.substring(1) + "],\n";
				}
				else
				{
					wrapperCLP += "List=List1,\n";
				}
				
			/*	if((iterateTimes==-1 &&!bounded)||(iterateTimes>1&&!bounded)) {
					for (int lim = 1; lim <= 3 && lim <= 24 ; lim++)
					wrapperObj += ",Result" + lim;
				wrapperCLP += "Result=[" + wrapperObj.substring(1) + "],\n";
					}
					else
					{
						wrapperCLP += "Result=Result1,\n";
					}*/
				
			wrapperCLP += "Obj=[List,Size],\n";
			if((iterateTimes==-1 &&!bounded)||(iterateTimes>1&&!bounded)) 
				wrapperCLP +="Result=[Result1,Result2,Result3,Size]";
				else 
					wrapperCLP +="Result=[Result1,Size]";
			}
			else
			{

				if((iterateTimes==-1 &&!bounded)||(iterateTimes>1&&!bounded))
				{
					wrapperCLP += "Obj_pre1=[List1_pre],\n";
					wrapperCLP += "Obj_post1=[List1],\n";
					wrapperCLP += new_className + new_methodName + "_" + pathNumber + "(Obj_pre1,Arg_pre1,Obj_post1,Arg_post1,Result1,Exception),\n";
					wrapperCLP += "length(List1,Size_pre),\n";
					wrapperCLP+="labeling_1dInt_array(Data_pre),\n";
					wrapperCLP+="msort(Data_pre,List2_pre),\n";
					wrapperCLP += "Obj_pre2=[List2_pre],\n";
					wrapperCLP += "Obj_post2=[List2],\n";
					wrapperCLP += new_className + new_methodName + "_" + pathNumber + "(Obj_pre2,Arg_pre2,Obj_post2,Arg_post2,Result2,Exception),\n";
					wrapperCLP += "length(List2,Size_pre),\n";
					wrapperCLP+="reverse(List2_pre,List3_pre),\n";
					wrapperCLP += "Obj_pre3=[List3_pre],\n";
					wrapperCLP += "Obj_post3=[List3],\n";
					wrapperCLP += new_className + new_methodName + "_" + pathNumber + "(Obj_pre3,Arg_pre3,Obj_post3,Arg_post3,Result3,Exception),\n";
					wrapperCLP += "length(List3,Size_pre),\n";
				}
					else
					{
						wrapperCLP += "Obj_pre1=[List1_pre],\n";
						wrapperCLP += "Obj_post1=[List1],\n";
						wrapperCLP += new_className + new_methodName + "_" + pathNumber + "(Obj_pre1,Arg_pre1,Obj_post1,Arg_post1,Result1,Exception),\n";
						wrapperCLP += "length(List1,Size_pre),\n";
						wrapperCLP+="labeling_1dInt_array(Data_pre),\n";
					}
					String wrapperObj = "";
				
						wrapperCLP += "Obj_pre=[List1_pre";
					
						if((iterateTimes==-1 &&!bounded)||(iterateTimes>1&&!bounded))
						for (int lim = 2; lim <= 3; lim++)
							wrapperCLP += ",List" + lim + "_pre";
						wrapperCLP += ",Size_pre],\n";
					
						if((iterateTimes==-1 &&!bounded)||(iterateTimes>1&&!bounded)) {
						for (int lim = 1; lim <= 3 && lim <= 24 ; lim++)
						wrapperObj += ",List" + lim;
					wrapperCLP += "List=[" + wrapperObj.substring(1) + "],\n";
						}
						else
						{
							wrapperCLP += "List=List1,\n";
						}
						
					wrapperCLP += "Obj=[List,Size],\n";
					if((iterateTimes==-1 &&!bounded)||(iterateTimes>1&&!bounded)) 
						wrapperCLP +="Result=[Result1,Result2,Result3,Size]";
						else 
							wrapperCLP +="Result=[Result1,Size]";
					
			}
		//	wrapperCLP+="Result=[Result1,Result2,Result3]";
			isarray = true;
			if (startNode.getMethodParameters().size() > 0)
				wrapperCLP += ",\n";
		}
			else if(this.arg_pre.size()>0 &&!Main.msort)
			{
				wrapperCLP += this.genStateAligned(startNode);
				/********/
				wrapperCLP += this.genInvariant();
				/********/

				wrapperCLP += this.genBodyCall(startNode, pathNumber);
			}
			
		}
		/********/
		String labelword = this.analysisLabelingCLP(startNode);
		System.out.println("labelword:"+labelword);
		if (isarray )
			wrapperCLP += ".\n";
	//	else if(wrapperCLP.contains("List2"))
	//		wrapperCLP += ",\n"+"labeling_1dInt_array(Data_pre).\n";
		else
		{
			if(labelword.indexOf(".")==0)
				wrapperCLP=wrapperCLP.substring(0, wrapperCLP.lastIndexOf(","));
			wrapperCLP += labelword;
		}
		/********/

		return wrapperCLP;
	}


	private String genWrapperName(CLGStartNode startNode) {
		String clpPredicateName = "test";
		String new_className = startNode.getClassName();
		String new_methodName = startNode.getMethodName();
		new_className = new_className.toUpperCase().charAt(0) + new_className.substring(1);
		new_methodName = new_methodName.toUpperCase().charAt(0) + new_methodName.substring(1);
		clpPredicateName += new_className;
		clpPredicateName += new_methodName;
		clpPredicateName += "(Obj_pre,Arg_pre,Obj,Arg,Result,Exception):-\n";
		return clpPredicateName;
	}

	private String genDomain(CLGStartNode startNode) {
		String domainPredicate = "";
		String object_pre_content = "";
		String arg_pre_content = "";
		String object_post_content = "";
		String arg_post_content = "";
		String result_content = "";

		if (this.object_pre.size() > 0) {
			for (String object : this.object_pre) {
				String temp = object.replaceAll("_pre", "");
				temp = temp.toLowerCase().charAt(0) + temp.substring(1);
				for (VariableToken variableToken : Main.symbolTable.getAttribute()) {
					if (variableToken.getVariableName().equals(temp)) {
						if (!variableToken.getType().contains("["))
							object_pre_content += "," + object;
					}
				}
			}
			if (object_pre_content != "") {
				domainPredicate += "[" + object_pre_content.substring(1) + "]:: -32768..32767,\n";
			}
		}
		if (this.arg_pre.size() > 0) {
			for (String object : this.arg_pre) {
				String temp = object.replaceAll("_pre", "");
				temp = temp.toLowerCase().charAt(0) + temp.substring(1);
				for (VariableToken variableToken : Main.symbolTable.getArgument()) {
					if (variableToken.getVariableName().equals(temp)) {
						if (!variableToken.getType().contains("["))
							arg_pre_content += "," + object;
					}
				}
				
			}
			if (arg_pre_content != "") {
				domainPredicate += "[" + arg_pre_content.substring(1) + "]:: -32768..32767,\n";
			}

		}
		if (this.object_post.size() > 0) {

			for (String object : this.object_post) {
				String temp = object.toLowerCase().charAt(0) + object.substring(1);
				for (VariableToken variableToken : Main.symbolTable.getAttribute()) {
					if (variableToken.getVariableName().equals(temp)) {
						if (!variableToken.getType().contains("["))
							object_post_content += "," + object;
					}
				}
				
			}
			if (object_post_content != "")
				domainPredicate += "[" + object_post_content.substring(1) + "]:: -32768..32767,\n";
		}
		if (this.arg_post.size() > 0) {
			for (String object : this.arg_post) {
				String temp = object.toLowerCase().charAt(0) + object.substring(1);
				for (VariableToken variableToken : Main.symbolTable.getArgument()) {
					if (variableToken.getVariableName().equals(temp)) {
						if (!variableToken.getType().contains("["))
							arg_post_content += "," + object;
					}
				}
				
			}
			if (arg_post_content != "")
				domainPredicate += "[" + arg_post_content.substring(1) + "]:: -32768..32767,\n";
		}
		/*for (String object : this.result) {
			if (object.contains("Result_"))
				result_content += "," + object;
		}
		if (result_content.length() > 0)
			domainPredicate += "[" + result_content.substring(1) + "]:: -32768..32767,\n";*/
		boolean useObjPreArrayState=false,useArgPreArrayState=false;
		for (String object : this.object_pre) {
			String temp = object.replaceAll("_pre", "");
			temp = temp.toLowerCase().charAt(0) + temp.substring(1);
			for (VariableToken variableToken : Main.symbolTable.getAttribute()) {
				if (variableToken.getVariableName().equals(temp)) {
					if (variableToken.getType().contains("[")) {
						String type = variableToken.getType();
						String size = type.substring(type.indexOf("[") + 1, type.length() - 1);
						
						if (size.contains("...")||size.contains("bound")||size.contains("x")) {
							if (iterateTimes == -1)
							{
								if (variableToken.getType().contains("]["))
								{
									String temptype=variableToken.getType();
									domainPredicate+="Row_pre#="+temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]"))+",\n";
									domainPredicate+="Col_pre#="+temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]"))+",\n";
									twoD=true;
									Main.twoD=true;
								}
								else
									domainPredicate += "Size_pre" + "#=" + size + ",\n";
							}
								else
								{
									if (variableToken.getType().contains("]["))
									{
										String temptype=variableToken.getType();
										domainPredicate+="Row_pre#="+this.iterateTimes+",\n";
										domainPredicate+="Col_pre#="+this.iterateTimes+",\n";
										twoD=true;
										Main.twoD=true;
									}
									else
										domainPredicate += "Size_pre" + "#=" + this.iterateTimes + ",\n";
									
								}
						} else
						{
							if (variableToken.getType().contains("]["))
							{
								String temptype=variableToken.getType();
								domainPredicate+="Row_pre#="+temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]"))+",\n";
								domainPredicate+="Col_pre#="+temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]"))+",\n";
								twoD=true;
								Main.twoD=true;
							}
							else
								domainPredicate += "Size_pre#=" + size + ",\n";
						}
					
						temp = temp.toUpperCase().charAt(0) + temp.substring(1);
						if(twoD)
						{//二維
							domainPredicate += "dim(" + temp + "_pre,[Row_pre,Col_pre]),\n";
							if(row>0)
								domainPredicate +="dcl_2dInt_array("+temp+"_pre,Row_pre,Col_pre),\n";
							//domainPredicate += "intArrayInstances(Row_pre,Col_pre,"+temp+"_pre),\n";
							domainPredicate	+= "dim(" + temp + ",[Col_pre,Row_pre]),\n";
						}
						else
						{//一維
							if(!Main.msort)
							{//當物件前不是已排序
							//	domainPredicate += "intSequenceInstances(Size_pre," + temp + "_pre),\n";
								domainPredicate += "dcl_1dInt_array("+temp+"_pre, Size_pre),\n";
								if(uselength==0)
									domainPredicate += "length(" + temp + ",Size_pre),\n";
								else if(uselength==1)
									domainPredicate += "Size#=Size_pre+1,\nlength(" + temp + ",Size),\n";
								else {
									domainPredicate += "Size#=Size_pre-1,\nlength(" + temp + ",Size),\n";
								}
								domainPredicate+="List1_pre=Data_pre,\n";
							/*	if((iterateTimes == -1 || iterateTimes>1)&&Main.issort&&!bounded)
								{
									domainPredicate+="msort(Data_pre,List2_pre),\n";
									domainPredicate+="reverse(List2_pre,List3_pre),\n";
								}*/
							}
							else
							{
								//domainPredicate += "intSequenceInstances2(Size_pre," + temp + "_pre),\nlength(" + temp + ",Size_pre),\n";
								domainPredicate += "dcl_1dInt_array(" + temp + "_pre,Size_pre),\n";
								domainPredicate += "mysort(" + temp + "_pre,Size_pre),\nlength(" + temp + ",Size_pre),\n";
							}
						}
						useObjPreArrayState=true;
					}
				}
			}

		}
		
		int arrayrow=0,arraycol=0;
		for (String object : this.arg_pre) {
			String temp = object.replaceAll("_pre", "");
			temp = temp.toLowerCase().charAt(0) + temp.substring(1);
			for (VariableToken variableToken : Main.symbolTable.getArgument()) {
				if (variableToken.getVariableName().equals(temp)) {
					if (variableToken.getType().contains("[")) {
						String type = variableToken.getType();
						String size = type.substring(type.indexOf("[") + 1, type.length() - 1);
						if (size.contains("...")||size.contains("x")) {
							if (iterateTimes == -1)
							{
								if (variableToken.getType().contains("]["))
								{
									String temptype=variableToken.getType();
									domainPredicate+="ArrayRow_pre#="+temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]"))+",\n";
									if(!temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]")).contains("x"))
										arrayrow=Integer.parseInt(temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]")));
									domainPredicate+="ArrayCol_pre#="+temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]"))+",\n";
									if(!temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]")).contains("x"))
										arraycol=Integer.parseInt(temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]")));
									twoD=true;
									Main.twoD=true;
								}
								else
								{
								domainPredicate += temp.toUpperCase().charAt(0) + temp.substring(1) + "Size_pre" + "#=" + size + ",\n";
								}
							}
								else
								{
									if (variableToken.getType().contains("]["))
									{
										String temptype=variableToken.getType();
										domainPredicate+="ArrayRow_pre#="+this.iterateTimes+",\n";
										arrayrow=this.iterateTimes;
										domainPredicate+="ArrayCol_pre#="+this.iterateTimes+",\n";
										arraycol=this.iterateTimes;
										twoD=true;
										Main.twoD=true;
									}
									else
								domainPredicate += temp.toUpperCase().charAt(0) + temp.substring(1) + "Size_pre" + "#=" + this.iterateTimes + ",\n";
								}
						} else
						{
							//System.out.println("test"+variableToken.getType());
							if (variableToken.getType().contains("]["))
							{
								
								String temptype=variableToken.getType();
								System.out.println("type:"+temptype);
								domainPredicate+="ArrayRow_pre#="+temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]"))+",\n";
								if(!temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]")).contains("x"))
									arrayrow=Integer.parseInt(temptype.substring(temptype.indexOf("[")+1, temptype.indexOf("]")));
								domainPredicate+="ArrayCol_pre#="+temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]"))+",\n";
								if(temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]")).contains("x"))
									arraycol=Integer.parseInt(temptype.substring(temptype.lastIndexOf("[")+1, temptype.lastIndexOf("]")));
								twoD=true;
								Main.twoD=true;
							}
							else
							domainPredicate += temp.toUpperCase().charAt(0) + temp.substring(1) + "Size_pre#=" + size + ",\n";
						}
						if(twoD)
						{//二維
							temp = temp.toUpperCase().charAt(0) + temp.substring(1);
							domainPredicate += "dim(" + temp + "_pre,[ArrayRow_pre,ArrayCol_pre]),\n";
							if(arrayrow>0)
								domainPredicate += "dcl_2dInt_array("+temp+"_pre, ArrayRow_pre,ArrayCol_pre),\n";
						//	domainPredicate += "intArrayInstances(ArrayRow_pre,ArrayCol_pre,"+temp+"_pre),\n";
						}
						else
						{
							if(!Main.msort)
							{
								temp = temp.toUpperCase().charAt(0) + temp.substring(1);
							//	domainPredicate += "intSequenceInstances(ArraySize_pre," + temp + "_pre),\nlength(" + temp + ",ArraySize_pre),\n";
							//	if(!this.bodyCLP.contains("Exception")&&!this.bodyCLP.contains("SortedArray"))
								domainPredicate += "dcl_1dInt_array("+temp+"_pre, ArraySize_pre),\nlength("+ temp + ",ArraySize_pre),\n";
								domainPredicate+="List1_pre=Data_pre,\n";
							}
							else
							{
							//	domainPredicate += "intSequenceInstances2(" + temp.toUpperCase().charAt(0) + temp.substring(1) + "Size_pre," + temp.toUpperCase().charAt(0) + temp.substring(1)+ "_pre),\nlength(" + temp.toUpperCase().charAt(0)+ temp.substring(1) + ","
							//						+ temp.toUpperCase().charAt(0) + temp.substring(1) + "Size_pre),\n";
								domainPredicate += "dcl_1dInt_array("  + temp.toUpperCase().charAt(0) + temp.substring(1)+ "_pre,"+ temp.toUpperCase().charAt(0) + temp.substring(1) + "Size_pre),\nlength(" + temp.toUpperCase().charAt(0)+ temp.substring(1) + ","
										+ temp.toUpperCase().charAt(0) + temp.substring(1) + "Size_pre),\n";
								domainPredicate += "mysort(" + temp.toUpperCase().charAt(0) + temp.substring(1) + "_pre,"+ temp.toUpperCase().charAt(0) + temp.substring(1) +"Size_pre),\n";
										
							}
						}
						useArgPreArrayState=true;
					}
				}
			}

		}
//if(!useObjPreArrayState && !useObjPreArrayState &&arg_pre.size()>0)
//{
//	domainPredicate +="Size#="+this.iterateTimes+",\n";
//}
		return domainPredicate;
	}


	private String genStateAligned(CLGStartNode startNode) {
		String alignedPredicate = "";
		if (this.object_pre.size() > 0) {
			alignedPredicate += "Obj_pre=" + addHeadInfo(this.object_pre);
			alignedPredicate += "],\n";
		}
		if (this.arg_pre.size() > 0)
			alignedPredicate += "Arg_pre=" + addHeadInfo(this.arg_pre) + "],\n";
		if (this.object_post.size() > 0)
			alignedPredicate += "Obj=" + addHeadInfo(this.object_post) + "],\n";
		if (this.arg_post.size() > 0)
			alignedPredicate += "Arg=" + addHeadInfo(this.arg_post) + "],\n";
		if(this.twoD && !startNode.isConstructor())
			alignedPredicate += "Arg_pre=[],\nArg=[],\n";
		return alignedPredicate;
	}


	private String genInvariant() {
		String invariant = "";
		return invariant;
	}

	private String genBodyCall(CLGStartNode startNode, int num) {
		String bodyCall = "";
		String new_className = startNode.getClassName();
		String new_methodName = startNode.getMethodName();
		new_className = new_className.toLowerCase().charAt(0) + new_className.substring(1);
		new_methodName = new_methodName.toUpperCase().charAt(0) + new_methodName.substring(1);
		bodyCall += new_className + new_methodName + "_" + num + "(Obj_pre,Arg_pre,Obj,Arg,Result,Exception),\n";
		return bodyCall;
	}


	private String analysisLabelingCLP(CLGStartNode startNode) {
		String labelingPredicate = "";
		if(containException)
			labelingPredicate+="Obj=Obj_pre,\n";
		Boolean isSet = false;
		Boolean isSet2 = false;
		for (String object : this.object_post) {
			String temp = object.toLowerCase().charAt(0) + object.substring(1);

			for (VariableToken variableToken : Main.symbolTable.getAttribute()) {
				if (variableToken.getVariableName().equals(temp))
					if (variableToken.getType().contains("["))
						isSet = true;
			}
		}
		String arg="";
		for (String object : this.arg_post) {
			String temp = object.toLowerCase().charAt(0) + object.substring(1);

			for (VariableToken variableToken : Main.symbolTable.getArgument()) {
				if (variableToken.getVariableName().equals(temp))
					if (variableToken.getType().contains("["))
						isSet2 = true;
					else {
						arg+=","+object;
					}
			}
		}
	//	if(!this.bodyCLP.contains("Exception")&&!this.bodyCLP.contains("SortedArray"))
	//	{
		if (this.object_pre.size()>0 && !isSet) {
			labelingPredicate += "labeling(Obj_pre),\n";
			labelingPredicate += "labeling(Obj),";
		}
		
		else if(isSet && !twoD)
		{
			labelingPredicate +="labeling_1dInt_array(Data_pre),\n";
		}
		else if(isSet && twoD)
		{
			labelingPredicate +="labeling_2dInt_array(Data_pre,Row_pre,Col_pre),\n";
		}
		else if(Main.doArray){
			labelingPredicate +="labeling_1dInt_array(Data_pre),\n";
			
		}
	//	}
		if (startNode.getMethodParameters() != null) {
			if(!isSet2)
			{
			labelingPredicate += "labeling(Arg_pre),\n";
			labelingPredicate += "labeling(Arg)";
			}
			else
			{
				if(!twoD)
				{
		//			if(!this.bodyCLP.contains("Exception")&&!this.bodyCLP.contains("SortedArray"))
		//			{
					if(Main.symbolTable.getAttributeMap().containsKey("size"))
				labelingPredicate +="labeling_1dInt_array(Array_pre),\n";
					else
						labelingPredicate +="labeling_1dInt_array(Array_pre)";
			//		}
				}
				else
				{
					if(Main.symbolTable.getAttributeMap().containsKey("row"))
					labelingPredicate +="labeling_2dInt_array(Array_pre,ArrayRow_pre,ArrayCol_pre),\n";
					else
						labelingPredicate +="labeling_2dInt_array(Array_pre,ArrayRow_pre,ArrayCol_pre)";
				}
					if(arg.length()>0)
				{
				String arg_pre="Arg_pre2=["+arg.replaceAll(",", "_pre,").substring(5)+"],\n";
				arg="Arg2=["+arg.substring(1)+"],\n";
				labelingPredicate += arg_pre+arg+"labeling(Arg_pre2),\n";
				labelingPredicate += "labeling(Arg2)";
				}
			}
		}
		if(containException && labelingPredicate.length()<14)
			labelingPredicate=labelingPredicate.substring(0, labelingPredicate.indexOf(","));
		if (startNode.getClassAttributes() != null || startNode.getMethodParameters() != null) {
			labelingPredicate += ".\n";
		}

		return labelingPredicate;
	}


	private String genBodyName(CLGStartNode startNode, int num, List<CLGNode> path) {
		String bodyPredicateName = "";
		String new_className = startNode.getClassName();
		String new_methodName = startNode.getMethodName();
		new_className = new_className.toLowerCase().charAt(0) + new_className.substring(1);
		new_methodName = new_methodName.toUpperCase().charAt(0) + new_methodName.substring(1);
		String AttributeList = "";
		if (startNode.getClassAttributes() != null || startNode.getMethodParameters() != null) {

			AttributeList += "([";
			for (int i = 0; i < Main.attribute.size(); i++) {
				String new_preVar = Main.attribute.get(i).toUpperCase().charAt(0) + Main.attribute.get(i).substring(1);
				// AttributeList += new_preVar + "_0";
				AttributeList += new_preVar + "_pre";
				if (!startNode.isConstructor())
					this.object_pre.add(new_preVar + "_pre");
				// this.object_pre.add(new_preVar + "_0");
				this.object_post.add(new_preVar);
				if (i != (Main.attribute.size() - 1)) {
					AttributeList += ",";
				}
			}
  
			AttributeList += "],[";
			for (int i = 0; i < startNode.getMethodParameters().size(); i++) {
				String new_preArg = startNode.getMethodParameters().get(i).toUpperCase().charAt(0) + startNode.getMethodParameters().get(i).substring(1);
				AttributeList += new_preArg + "_pre";
				String className = ((CLGStartNode) this.startNode).getClassName();
				if (startNode.getMethodParameterTypes().get(i).equals(className)) {
					for (String attr : this.object_post) {
						this.arg_pre.add(new_preArg + "_" + attr + "_pre");
						this.arg_post.add(new_preArg + "_" + attr);
					}
				} else {
					this.arg_pre.add(new_preArg + "_pre");
					this.arg_post.add(new_preArg);
				}
				if (i != (startNode.getMethodParameters().size() - 1)) {
					AttributeList += ",";
				}
			}
			AttributeList += "],[";

			for (int i = 0; i < startNode.getClassAttributes().size(); i++) {
				String new_preVar = startNode.getClassAttributes().get(i).toUpperCase().charAt(0) + startNode.getClassAttributes().get(i).substring(1);
				AttributeList += new_preVar;
				if (i != (startNode.getClassAttributes().size() - 1)) {
					AttributeList += ",";
				}
			}
			AttributeList += "],[";

			for (int i = 0; i < startNode.getMethodParameters().size(); i++) {
				String new_preArg = startNode.getMethodParameters().get(i).toUpperCase().charAt(0) + startNode.getMethodParameters().get(i).substring(1);
				AttributeList += new_preArg;// +"_post";
				if (i != (startNode.getMethodParameters().size() - 1)) {
					AttributeList += ",";
				}
			}

			AttributeList += "],[";

			ArrayList<CLGConstraint> constraintList = new ArrayList<CLGConstraint>();

			for (int i = 1; i < path.size() - 1; i++) {
				if (path.get(i) instanceof CLGConstraintNode) {
					((CLGConstraintNode) path.get(i)).getId();
					constraintList.add(((CLGConstraintNode) path.get(i)).getConstraint().clone());
				}
			}

			if (startNode.isConstructor()) {
				for (CLGConstraint c : constraintList) {
					if (c.getCLPInfo().contains("Exception") && !this.result.contains("Result")) {
						this.result.add("Result");
						this.object_post = new ArrayList<String>();
					}
				}
			} else {
				switch (startNode.getReturnType()) {
				case "Integer":
				case "Boolean":
				case "String":
				case "Real":
					if (!this.result.contains("Result"))
						this.result.add("Result");
					break;
				case "OclVoid":
					break;
				default:
					for (String attr : this.object_post) {
						if (!this.result.contains(attr))
							this.result.add("Result_" + attr);
					}
				}
			}

			if (!startNode.isConstructor())
				AttributeList += "Result";
			AttributeList += "]):-\n";
		}

		
		bodyPredicateName += new_className;
		bodyPredicateName += new_methodName;
		bodyPredicateName += "_" + num + AttributeList;

		return bodyPredicateName;
	}


	private String genBodyCLP(List<CLGNode> path, int num) {
		String bodyCLP = "";
		ArrayList<CLGConstraint> constraintList = new ArrayList<CLGConstraint>();
		CLGNode endNode = path.get(path.size() - 1);
		CLGStartNode startNode = (CLGStartNode) path.get(0);
		this.variableSet.clear();

		if(((CLGStartNode) this.startNode).isConstructor())
		{
			if(bounded)
				bodyCLP +="Bound#="+this.bound+",\n";
		}
		else if(!((CLGStartNode) this.startNode).isConstructor()&& twoD)
			bodyCLP+="dim(ArrayData,[Row_pre,Col_pre]),\n";
		
		HashMap<CLGConstraint, CLGConstraintNode> cons2ConNode=new HashMap<CLGConstraint, CLGConstraintNode>();
		for (int i = 1; i < path.size() - 1; i++) {
			if (path.get(i) instanceof CLGConstraintNode) {
				((CLGConstraintNode) path.get(i)).getId();
				CLGConstraint cons=((CLGConstraintNode) path.get(i)).getConstraint().clone();
				constraintList.add(cons);
				cons2ConNode.put(cons,(CLGConstraintNode) path.get(i) );
				//constraintList.add(((CLGConstraintNode) path.get(i)).getConstraint());
			}
		}
HashMap<String, Integer> iterateTimes=new HashMap<String, Integer>();
HashMap<CLGConstraintNode, Integer> conNodeiterateTimes=new HashMap<CLGConstraintNode, Integer>();
		for (CLGConstraint c : constraintList) {
			if (c instanceof CLGOperatorNode) {
				if (((CLGOperatorNode) c).getOperator().equals("=")) {
					this.renameUseVar(((CLGOperatorNode) c).getRightOperand());
					this.renameDefVar(((CLGOperatorNode) c).getLeftOperand());
					String temp = c.getCLPInfo();
					if(temp=="")
						continue;
					if (c.getCLPInfo().contains("mod"))
						usedMod = true;
					if (c.getCLPInfo().contains("method")) {
						temp = temp.substring(temp.indexOf("method") + 6);
						if (temp.contains("Remainder")) {
							if (variableSet.containsKey("Remainder")) {
								temp = temp.replaceAll("Remainder", "Remainder_" + variableSet.get("Remainder"));
								variableSet.put("Remainder", variableSet.get("Remainder") + 1);
							} else
								variableSet.put("Remainder", 1);
						}
						bodyCLP += temp;
					} else {
						if(temp=="")
							continue;
						if (temp.contains("Remainder")) {
							if (variableSet.containsKey("Remainder")) {
								temp = temp.replaceAll("Remainder", "Remainder_" + variableSet.get("Remainder"));
								variableSet.put("Remainder", variableSet.get("Remainder") + 1);
							} else
								variableSet.put("Remainder", 1);
						}
						bodyCLP += temp;
					}
				} else {
					this.renameUseVar(((CLGOperatorNode) c).getLeftOperand());
					this.renameUseVar(((CLGOperatorNode) c).getRightOperand());
					String temp ="";
				
					if(!Main.boundary_analysis||!((CLGOperatorNode) c).getBoundary()||(!Main.iterateBoundary.containsKey(((CLGOperatorNode) c).getCloneCID())&&!Main.conNodeiterateBoundary.containsKey(cons2ConNode.get(c))))
					{
					temp= c.getCLPInfo();
					}
					else
					{
						String oldOp=((CLGOperatorNode) c).getOperator();
						if(!iterateTimes.containsKey(((CLGOperatorNode) c).getCloneCID()))
						{
							iterateTimes.put(((CLGOperatorNode) c).getCloneCID(), 1);
							if(!conNodeiterateTimes.containsKey(cons2ConNode.get(c)))
							conNodeiterateTimes.put(cons2ConNode.get(c), 1);
						}
						if(iterateTimes.get(((CLGOperatorNode) c).getCloneCID())==Main.iterateBoundary.get(((CLGOperatorNode) c).getCloneCID())
								||conNodeiterateTimes.get(cons2ConNode.get(c))==Main.conNodeiterateBoundary.get(cons2ConNode.get(c)))
						{
							Main.changeBoundary=true;
							iterateTimes.put(((CLGOperatorNode) c).getCloneCID(), iterateTimes.get(((CLGOperatorNode) c).getCloneCID())+1);
							conNodeiterateTimes.put(cons2ConNode.get(c),conNodeiterateTimes.get(cons2ConNode.get(c))+1);
						}
						else
						{
							iterateTimes.put(((CLGOperatorNode) c).getCloneCID(), iterateTimes.get(((CLGOperatorNode) c).getCloneCID())+1);
							conNodeiterateTimes.put(cons2ConNode.get(c),conNodeiterateTimes.get(cons2ConNode.get(c))+1);
						}
						temp=c.getCLPInfo();
						Main.changeBoundary=false;
					}
					
					
					if(temp=="")
						continue;
					if (temp.contains("Remainder")) {
						if (variableSet.containsKey("Remainder")) {
							temp = temp.replaceAll("Remainder", "Remainder_" + variableSet.get("Remainder"));
							variableSet.put("Remainder", variableSet.get("Remainder") + 1);
						} else
							variableSet.put("Remainder", 1);
					}
					bodyCLP += temp;

				}
			} else {
				String temp = c.getCLPInfo();
				bodyCLP += temp;
			}

			if (!c.equals(constraintList.get(constraintList.size() - 1))) {
				bodyCLP += ",\n";
			}

		}
		if(Main.boundary_analysis&&bodyCLP.contains("#=<Element"))
			bodyCLP=bodyCLP.replaceAll("#=<Element", "#<Element");
		if(twoD)
		{	
			if(!bodyCLP.contains("Exception"))
			{
			for( int i=2;bodyCLP.contains("It#=(It_pre+1),");i++)
			{
				String tempIt=bodyCLP.substring(bodyCLP.indexOf("It#=(It_pre+1),")+11);
				tempIt=tempIt.substring(0,tempIt.indexOf("It_pre")+3)+i+tempIt.substring(tempIt.indexOf("It_pre")+6);
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("It#=(It_pre+1),")+11)+tempIt;
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("It#=(It_pre+1),"))+"It_"+i+"#=(It_"+(i-1)+bodyCLP.substring(bodyCLP.indexOf("It#=(It_pre+1),")+11);		
			}
			bodyCLP=bodyCLP.replaceAll("It_pre", "It_1");
			
			String changeIt=bodyCLP;
			ArrayList<String> changeCombine=new ArrayList<String>();
				for(int i=1;changeIt.contains("it");i++ )
				{
					String checkNext=changeIt;
					if(changeIt.contains("It_"+(i+1)))
					{
					checkNext=changeIt.substring(0,changeIt.indexOf("It_"+(i+1)));
					changeIt=changeIt.substring(changeIt.indexOf("It_"+(i+1)));
					}
					checkNext=checkNext.replaceAll("it", "It_"+i);
					changeCombine.add(checkNext);
				}
				changeCombine.add(changeIt);
			String allChange="";
			for(String change:changeCombine)
				allChange+=change;
			bodyCLP=allChange;
			
				
			String checkAcc2=bodyCLP;
			ArrayList<String> allCut2=new ArrayList<String>();
			
			for(int j=1;checkAcc2.contains("Acc2");j++)
			{
				String cut1=checkAcc2.substring(0,checkAcc2.indexOf("Acc2")+4);
				cut1=cut1.substring(0,cut1.lastIndexOf("It2_pre")+3)+"_"+j+cut1.substring(cut1.lastIndexOf("It2_pre")+7);
				allCut2.add(cut1);
				String cut2=checkAcc2.substring(checkAcc2.indexOf("Acc2")+4);
				cut2=cut2.substring(0,cut2.indexOf("It2_pre")+3)+"_"+j+cut2.substring(cut2.indexOf("It2_pre")+7);
				if(cut2.contains("Acc2"))
				{
					checkAcc2=cut2;
					String checkround2=checkAcc2.substring(0,checkAcc2.indexOf("Acc2"));
					int round2=0;
					while(checkround2.contains("It2#=(It2_pre+1)"))
					{
						round2++;
						checkround2=checkround2.substring(checkround2.indexOf("It2#=(It2_pre+1)")+1);
					}
					j+=round2;
				}
				else
				{
					allCut2.add(cut2);
					break;
				}
			}
			if(allCut2.size()>0)
			{
			String combine2="";
			for(String cut:allCut2)
				combine2+=cut;
			bodyCLP=combine2;
			}
			
			for( int i=2;bodyCLP.contains("It2#=(It2_pre+1),");i++)
			{
				if(bodyCLP.contains("It2_"+i))
				continue;
				String tempIt2=bodyCLP.substring(bodyCLP.indexOf("It2#=(It2_pre+1),")+13);
				tempIt2=tempIt2.substring(0,tempIt2.indexOf("It2_pre")+4)+i+tempIt2.substring(tempIt2.indexOf("It2_pre")+7);
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("It2#=(It2_pre+1),")+13)+tempIt2;
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("It2#=(It2_pre+1),"))+"It2_"+i+"#=(It2_"+(i-1)+bodyCLP.substring(bodyCLP.indexOf("It2#=(It2_pre+1),")+13);		
			}
			if(bodyCLP.contains("i2t"))
			{
			String changeIt2=bodyCLP;
			ArrayList<String> changeCombine2=new ArrayList<String>();
			for(int i=1;changeIt2.contains("i2t");i++ )
			{
				String checkNext2=changeIt2;
				if(changeIt2.contains("It2_"+(i+1)))
				{
				checkNext2=changeIt2.substring(0,changeIt2.indexOf("It2_"+(i+1)));
				changeIt2=changeIt2.substring(changeIt2.indexOf("It2_"+(i+1)));
				}
				checkNext2=checkNext2.replaceAll("i2t", "It2_"+i);
				changeCombine2.add(checkNext2);
			}
			changeCombine2.add(changeIt2);
				
			String allChange2="";
			for(String change:changeCombine2)
				allChange2+=change;
			bodyCLP=allChange2;
			}
		//	bodyCLP=bodyCLP.replaceAll("It_1", "It");
		//	bodyCLP=bodyCLP.replaceAll("_1", "");
			/*for(int i=1;i<=row*col*2 && bodyCLP.contains("ArrayRowi)");i++)
			bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("ArrayRowi)")+8)+i+bodyCLP.substring(bodyCLP.indexOf("ArrayRowi)")+8);
			
			for(int i=1;i<=row*col*2 && bodyCLP.contains(",ArrayRowi");i++)
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf(",ArrayRowi")+9)+i+bodyCLP.substring(bodyCLP.indexOf(",ArrayRowi")+9);
			
			for(int i=1;i<=row*col*2 && bodyCLP.contains(",Arrayi)");i++)
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf(",Arrayi)")+6)+i+bodyCLP.substring(bodyCLP.indexOf(",Arrayi)")+6);
			
			for(int i=1;i<=row*col*2 && bodyCLP.contains("IndexRow#");i++)
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("IndexRow#")+8)+i+bodyCLP.substring(bodyCLP.indexOf("IndexRow#")+8);
			
			for(int i=1;i<=row*col*2 && bodyCLP.contains("(IndexRow,");i++)
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("(IndexRow,")+9)+i+bodyCLP.substring(bodyCLP.indexOf("(IndexRow,")+9);
			
			for(int i=1;i<=row*col*2 && bodyCLP.contains("Index#");i++)
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("Index#")+5)+i+bodyCLP.substring(bodyCLP.indexOf("Index#")+5);
			
			for(int i=1;i<=row*col*2 && bodyCLP.contains("(Index,");i++)
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("(Index,")+6)+i+bodyCLP.substring(bodyCLP.indexOf("(Index,")+6);
			
			for(int i=1;i<=row*col*2 && bodyCLP.contains("Arrayi");i++)
				bodyCLP=bodyCLP.substring(0,bodyCLP.indexOf("Arrayi")+5)+i+bodyCLP.substring(bodyCLP.indexOf("Arrayi")+5);*/
			}
			
		}
		else {
		
		if (bodyCLP.contains("it")) {
			String cutCLP = bodyCLP.substring(0, bodyCLP.indexOf("It_"));
			bodyCLP = cutCLP.replaceAll("it", "It") + bodyCLP.substring(bodyCLP.indexOf("It_"));
			ArrayList<String> cut = new ArrayList<String>();
			cutCLP = bodyCLP;
			for (int i = 2; cutCLP.indexOf("It_") > 0; i++) {
				if(cutCLP.contains("It_"+(i+1)+"#=1"))
						i=i+1;
				if (cutCLP.contains("it")) {
					cut.add(cutCLP.substring(0, cutCLP.indexOf("it")) + "It_" + i);
					cutCLP = cutCLP.substring(cutCLP.indexOf("it") + 2);
					while (cutCLP.indexOf("it") < cutCLP.indexOf("It_") && cutCLP.indexOf("it") >= 0) {
						cut.add(cutCLP.substring(0, cutCLP.indexOf("it")) + "It_" + i);
						cutCLP = cutCLP.substring(cutCLP.indexOf("it") + 2);
					}
				} else {
					cut.add(cutCLP);
					break;
				}
			}
			bodyCLP = "";
			for (String clp : cut) {
				bodyCLP += clp;
			}
		}
		if(bodyCLP.contains("Acc_2"))
			bodyCLP=bodyCLP.replaceAll("Acc_2", "Acc");
	/*	String tempbodyCLP=bodyCLP;
		for(int changeIndex=1;tempbodyCLP.contains("Index#=");changeIndex++)
		{
			String cut=tempbodyCLP.substring(0, tempbodyCLP.indexOf("Index#=")+5)+changeIndex+tempbodyCLP.substring(tempbodyCLP.indexOf("Index#=")+5);
			tempbodyCLP=cut;
		}
		for(int changeIndex=1;tempbodyCLP.contains("Index#=");changeIndex++)
		{
			String cut=tempbodyCLP.substring(0, tempbodyCLP.indexOf("Index#=")+5)+changeIndex+tempbodyCLP.substring(tempbodyCLP.indexOf("Index#=")+5);
			tempbodyCLP=cut;
		}
		for(int changeIndex=1;tempbodyCLP.contains("Index)");changeIndex++)
		{
			String cut=tempbodyCLP.substring(0, tempbodyCLP.indexOf("Index)")+5)+changeIndex+tempbodyCLP.substring(tempbodyCLP.indexOf("Index)")+5);
			tempbodyCLP=cut;
		}
		for(int changeIndex=1;tempbodyCLP.contains(",Arrayi,");changeIndex++)
		{
			String cut=tempbodyCLP.substring(0, tempbodyCLP.indexOf(",Arrayi,")+6)+changeIndex+tempbodyCLP.substring(tempbodyCLP.indexOf(",Arrayi,")+6);
			tempbodyCLP=cut;
		}
		
		
		for(int changeIndex=3;tempbodyCLP.contains("member(Arrayi");changeIndex=changeIndex+4)
		{
			String cut=tempbodyCLP.substring(0, tempbodyCLP.indexOf("member(Arrayi")+12)+changeIndex+tempbodyCLP.substring(tempbodyCLP.indexOf("member(Arrayi")+12);
			tempbodyCLP=cut;
			cut=tempbodyCLP.substring(0, tempbodyCLP.indexOf("member(Arrayi")+12)+(changeIndex+1)+tempbodyCLP.substring(tempbodyCLP.indexOf("member(Arrayi")+12);
			tempbodyCLP=cut;
		}
		for(int changeIndex=1;Main.msort&&tempbodyCLP.contains("Arrayi");changeIndex++)
		{
			String cut=tempbodyCLP.substring(0, tempbodyCLP.indexOf("Arrayi")+5)+changeIndex+tempbodyCLP.substring(tempbodyCLP.indexOf("Arrayi")+5);
			tempbodyCLP=cut;
		}
		for(int changeIndex=1;tempbodyCLP.contains("Arrayi#=<Arrayi");changeIndex=changeIndex+4)
		{
			String cut=tempbodyCLP.substring(0, tempbodyCLP.indexOf("Arrayi#=<Arrayi")+5)+changeIndex+tempbodyCLP.substring(tempbodyCLP.indexOf("Arrayi#=<Arrayi")+5,tempbodyCLP.indexOf("Arrayi#=<Arrayi")+14)+
					+(changeIndex+1)+tempbodyCLP.substring(tempbodyCLP.indexOf("Arrayi#=<Arrayi")+14);
			tempbodyCLP=cut;
		}
		for(int changeIndex=1;tempbodyCLP.contains("Arrayi#=Arrayi");changeIndex=changeIndex+2)
		{
			String cut=tempbodyCLP.substring(0, tempbodyCLP.indexOf("Arrayi#=Arrayi")+5)+changeIndex+tempbodyCLP.substring(tempbodyCLP.indexOf("Arrayi#=Arrayi")+5,tempbodyCLP.indexOf("Arrayi#=Arrayi")+13)+
					+(changeIndex+1)+tempbodyCLP.substring(tempbodyCLP.indexOf("Arrayi#=Arrayi")+13);
			tempbodyCLP=cut;
		}
		while(tempbodyCLP.contains("Arrayi") &&!Main.issort)
		{
			String cut1=tempbodyCLP.substring(0,tempbodyCLP.lastIndexOf("Arrayi"));
			String cut2= cut1.substring(cut1.lastIndexOf("Array")+5,cut1.lastIndexOf(",Index"));
					tempbodyCLP=tempbodyCLP.substring(0,tempbodyCLP.lastIndexOf("Arrayi")+5)+cut2+tempbodyCLP.substring(tempbodyCLP.lastIndexOf("Arrayi")+6);
		}
		bodyCLP=tempbodyCLP;
		String connect="";
		String cutCLP=bodyCLP;
		for(int count=0;cutCLP.indexOf("<Array")>0;count++)
		{
			connect+=cutCLP.substring(0,cutCLP.indexOf("<Array")+6);
			cutCLP=cutCLP.substring(cutCLP.indexOf("<Array")+6);
			connect+=cutCLP.substring(0,cutCLP.indexOf(",")+1);
			connect+="\nfindIndex(Data_pre,Array"+(count*4+1)+"i,I"+(count*2+1)+",0),\n";
			connect+="findIndex(Data_pre,Array"+(count*4+2)+"i,I"+(count*2+2)+",0),\n";
			connect+="I"+(count*2+1)+"=\\=I"+(count*2+2)+",";
			cutCLP=cutCLP.substring(cutCLP.indexOf(",")+1);
		}
		connect+=cutCLP;
		bodyCLP=connect;*/
		}
		bodyCLP += this.stateAssignEquals(startNode);
		System.out.println("fuck:"+startNode.getClassName());
		if (!bodyCLP.contains("Exception") && Main.invCLP != null && Main.invCLP != ""&&!startNode.getClassName().equals("SortedArray")) {
			bodyCLP += "," + Main.invCLP;
		}
		bodyCLP += endNode.toCLPInfo() + "\n";
		return bodyCLP;
	}



	public String stateAssignEquals(CLGStartNode startNode) {
		/*****************/
		String stateAssignEquals = "";

		if (this.object_post.size() > 0 && this.object_pre.size() > 0)
			for (String object : this.object_post) {
				String temp = object.toLowerCase().charAt(0) + object.substring(1);
				for (VariableToken variableToken : Main.symbolTable.getAttribute()) {
					if (variableToken.getVariableName().equals(temp)) {
					//	if (!variableToken.getType().contains("[") ||Main.msort||twoD||)
						if (!(startNode.getMethodName().contains("push")||startNode.getMethodName().contains("pop")||startNode.getMethodName().contains("enqueue")||startNode.getMethodName().contains("dequeue")))
							stateAssignEquals += "," + object + "=" + object + "_pre";
					}
				}
				}
		
		for (String arg : this.arg_post)
			stateAssignEquals += "," + arg + "=" + arg + "_pre";

		return stateAssignEquals;
	}

	private void renameDefVar(CLGConstraint constraint) {
		if (constraint instanceof CLGVariableNode) {
			
			String variable = constraint.getCLPValue();
			variable = variable.replaceAll("_pre", "");

			if (!variableSet.containsKey(variable) && !variable.contains("result") && !variable.equals("") && !variable.contains("_")&&((!twoD)||variable.contains("acc"))) {
				
				
				variableSet.put(variable, 1);
				constraint.setCLPValue(variable);
				
			} else if (variableSet.containsKey(variable) && !variable.contains("result") && !variable.equals("") && !variable.contains("_")&&((!twoD)||variable.contains("acc"))) {
				constraint.setCLPValue(variable + "_" + (variableSet.get(variable) + 1));
				variableSet.put(variable, variableSet.get(variable) + 1);// + 1);

			
			}
		}
	}

	private void renameUseVar(CLGConstraint constraint) {
		if (constraint instanceof CLGOperatorNode) {
			this.renameUseVar(((CLGOperatorNode) constraint).getLeftOperand());
			this.renameUseVar(((CLGOperatorNode) constraint).getRightOperand());
		} else if (constraint instanceof CLGVariableNode) {
			
			String variable = constraint.getCLPValue();
			if(!variable.contains("["))
			{
			variable = variable.replaceAll("_pre", "");
			}
			
			
			if (variableSet.containsKey(variable) && !variable.contains("result") && !variable.equals("") && !variable.contains("_")&&((!twoD)||variable.contains("acc"))) {
				if (variableSet.get(variable) == 1)
					constraint.setCLPValue(variable);
				else
					constraint.setCLPValue(variable + "_" + variableSet.get(variable));// + "_" + variableSet.get(constraint.getCLPValue()));
			}
			
		}

	}

	// ---
	public String genMethodPathCLP(CLGPath path, int pathN) {
		int clpcount = 1;
		String clpstr1 = "", classn = "";
		List<CLGNode> nodeli = path.getPathNodes();
		clpstr1 += importLibraryCLP();
		clpstr1 += "testpath" + (pathN) + "(Obj_pre, Arg_pre, Obj_post, Arg_post, Ret_val):-\n\n";

		CLGPathEnumerator clgPathEnumerator = new CLGPathEnumerator();
		nodeli = clgPathEnumerator.filterConstraintNode(nodeli);

		for (int i = 0; i < nodeli.size(); i++) {
			if (nodeli.get(i) instanceof CLGConstraintNode) {
				CLGConstraintNode c = (CLGConstraintNode) nodeli.get(i);

				if (c.getConstraint() instanceof CLGOperatorNode) {
					CLGOperatorNode clgop = (CLGOperatorNode) c.getConstraint();
					if (clgop.getOperator().equals("=")) {
						this.renameUseVar(clgop.getRightOperand());
						this.renameDefVar(clgop.getLeftOperand());
						clpstr1 += "Poststate" + (clpcount - 1) + " = [P" + (clpcount - 1) + "],\n";
						clpstr1 += "P" + (clpcount - 1) + " #=" + clgop.getRightOperand().getImgInfo() + ",\n";
					} else {
						this.renameUseVar(clgop.getLeftOperand());
						this.renameUseVar(clgop.getRightOperand());
						if (clgop.getOperator().equals("==")) {
							clpstr1 += "Poststate" + (clpcount - 1) + " = [P" + (clpcount - 1) + "],\n";
							clpstr1 += "P" + (clpcount - 1) + " #= " + clgop.getRightOperand().getImgInfo() + ",\n\n";
						} else {
							clpstr1 += "Poststate" + (clpcount - 1) + " = [P" + (clpcount - 1) + "],\n";
							clpstr1 += "P" + (clpcount - 1) + " #" + clgop.getOperator() + clgop.getRightOperand().getImgInfo() + ",\n";
						}
					}
				} else if (c.getConstraint() instanceof CLGMethodInvocationNode) {
					CLGMethodInvocationNode clgme = (CLGMethodInvocationNode) c.getConstraint();

					if (clpcount == 1) {
						classn = clgme.getMethodName();
						if (nodeli.size() == 3) {
							clpstr1 += clgme.getMethodName().toLowerCase() + clgme.getMethodName().substring(0, 1).toUpperCase() + clgme.getMethodName().substring(1, clgme.getMethodName().length())
									+ "(Prearg" + clpcount + ", Poststate" + clpcount + ", Postarg" + clpcount + "),\n";
							clpstr1 += "Obj_pre = [[]],\n";
							clpstr1 += "Arg_pre = [Prearg" + clpcount + "],\n";
							clpstr1 += "Obj_post = [Poststate" + clpcount + "],\n";
							clpstr1 += "Arg_post = [Postarg" + clpcount + "],\n";
							clpstr1 += "Ret_val = [[]].\n\n";
						} else {
							clpstr1 += clgme.getMethodName().toLowerCase() + clgme.getMethodName().substring(0, 1).toUpperCase() + clgme.getMethodName().substring(1, clgme.getMethodName().length())
									+ "(Prearg" + clpcount + ", Poststate" + clpcount + ", Postarg" + clpcount + "),\n";
							clpstr1 += "Obj_pre = [[]|Pres" + clpcount + "],\n";
							clpstr1 += "Arg_pre = [Prearg" + clpcount + "|Prea" + clpcount + "],\n";
							clpstr1 += "Obj_post = [Poststate" + clpcount + "|Posts" + clpcount + "],\n";
							clpstr1 += "Arg_post = [Postarg" + clpcount + "|Posta" + clpcount + "],\n";
							clpstr1 += "Ret_val = [[]|Re" + clpcount + "],\n\n";
						}
						clpcount++;
					} else {
						// check last op
						if (i != nodeli.size() - 2) {
							clpstr1 += classn.toLowerCase() + clgme.getMethodName().substring(0, 1).toUpperCase() + clgme.getMethodName().substring(1, clgme.getMethodName().length()) + "(Poststate"
									+ (clpcount - 1) + ", Prearg" + clpcount + ", Poststate" + clpcount + ", Postarg" + clpcount + ", Return" + clpcount + "),\n";
							clpstr1 += "Pres" + (clpcount - 1) + " = [Poststate" + (clpcount - 1) + "|Pres" + clpcount + "],\n";
							clpstr1 += "Prea" + (clpcount - 1) + " = [Prearg" + clpcount + "|Prea" + clpcount + "],\n";
							clpstr1 += "Posts" + (clpcount - 1) + " = [Poststate" + clpcount + "|Posts" + clpcount + "],\n";
							clpstr1 += "Posta" + (clpcount - 1) + " = [Postarg" + clpcount + "|Posta" + clpcount + "],\n";
							clpstr1 += "Re" + (clpcount - 1) + " = " + "[Return" + clpcount + "|Re" + clpcount + "],\n\n";
							clpcount++;
						} else {
							clpstr1 += classn.toLowerCase() + clgme.getMethodName().substring(0, 1).toUpperCase() + clgme.getMethodName().substring(1, clgme.getMethodName().length()) + "(Poststate"
									+ (clpcount - 1) + ", Prearg" + clpcount + ", Poststate" + clpcount + ", Postarg" + clpcount + ", Return" + clpcount + "),\n";
							clpstr1 += "Pres" + (clpcount - 1) + " = [Poststate" + (clpcount - 1) + "],\n";
							clpstr1 += "Prea" + (clpcount - 1) + " = [Prearg" + clpcount + "],\n";
							clpstr1 += "Posts" + (clpcount - 1) + " = [Poststate" + clpcount + "],\n";
							clpstr1 += "Posta" + (clpcount - 1) + " = [Postarg" + clpcount + "],\n";
							clpstr1 += "Re" + (clpcount - 1) + " = " + "[Return" + clpcount + "].\n\n";
							clpcount++;
						}
					} // end else
				}
			}
		}
		this.body_count++;
		return clpstr1;
	}

}