package ccu.pllab.tcgen.tc;

 
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import ccu.pllab.tcgen.clg2path.Path;
import ccu.pllab.tcgen.libs.CLPArg;
import ccu.pllab.tcgen.libs.CLPResult;
import ccu.pllab.tcgen.libs.CLPState;
import ccu.pllab.tcgen.libs.TestData;
import ccu.pllab.tcgen.libs.clpresultparse.EvalArgArg;
import ccu.pllab.tcgen.libs.clpresultparse.EvalArgRet;
import ccu.pllab.tcgen.libs.clpresultparse.EvalArgSelf;
import ccu.pllab.tcgen.libs.clpresultparse.EvalAscElm;
import ccu.pllab.tcgen.libs.clpresultparse.EvalCLPResult;
import ccu.pllab.tcgen.libs.clpresultparse.EvalObjElm;
import ccu.pllab.tcgen.libs.clpresultparse.ResultLexer;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ResultContext;
import ccu.pllab.tcgen.libs.pivotmodel.AscInstance;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.ObjectInstance;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Operation;

public class TestDataFactory {
	String mClsName = "";
	String mMethodName = "";
	int mPathGenStatus;
	private Model model;

	public TestDataFactory(Model model, String clsName, String methodName) {
		this.model = model;
		mClsName = clsName;
		mMethodName = methodName;
	}

	public TestData ConvResult2Data(Path path, String sCLPResult, boolean isInvalidated) {
		TestData data = new TestData(mClsName, mMethodName, path.getId(), model.findClassInfoByName(mClsName).findMethod(mMethodName).getName().equals(mClsName));
		data.setInvalidate(isInvalidated);
		if (path.getASTNodes().get(0).getConstraint().getKind().getName().equals("precondition")) {
			data.setPreconditionName(path.getASTNodes().get(0).getConstraint().getName());
		}

		// construct a single TestData from a CLPResult and ClassDiaginfo
		// 1. parse CLPResult
		// 2. associate CLPResult with ClassDiagInfo for TestData
		CLPResult clpRet = ParseCLPResult(sCLPResult);
		// 1. decompose prestate
		CLPState clpState = clpRet.getPreState();
		ArrayList<String> stateList = clpState.getObjList();
		for (String str : stateList) {
			ObjectInstance obj = ParseObj(str);
			ArrayList<Triple<String, String, String>> Attrlist =obj.getAttrValueList();
			ArrayList<Triple<String, String, String>> templist =new ArrayList<Triple<String, String, String>>();
			for(Triple<String, String, String> Attr:Attrlist){
				if(Attr.getMiddle().startsWith("collection")){
					if(Attr.getMiddle().endsWith("Integer")){
						String[] tempString=Attr.getRight().substring(1, Attr.getRight().length()-1).split(",",2);
						templist.add(new ImmutableTriple<String, String, String>(Attr.getLeft(),Attr.getMiddle(),"new ArrayList<Integer>(Arrays.asList("+tempString[1]+"))"));
					}
					else if(Attr.getMiddle().endsWith("String")){
						String tempStringList=Attr.getRight();
						tempStringList=tempStringList.substring(tempStringList.indexOf(",")+1, Attr.getRight().length()-1);
						String[] splitvalueList=tempStringList.split("]");
						tempStringList="";
						for(int i=0;i<splitvalueList.length;i++){
							if(i>0)
							{
								tempStringList+=",";
							}
							String[] splitvalue = splitvalueList[i].substring(1).split(",");
							String tempString ="\"";
							for(int count=1;count<splitvalue.length;count++){
								tempString += ((char) Integer.parseInt(splitvalue[count]));
							}
							tempString+="\"";
							tempStringList+=tempString;
						}
						templist.add(new ImmutableTriple<String, String, String>(Attr.getLeft(),Attr.getMiddle(),"new ArrayList<Integer>(Arrays.asList("+tempStringList+"))"));
					}
				}
				else if(Attr.getMiddle().equals("String")){
					String[] splitvalue = Attr.getRight().substring(1, Attr.getRight().length()-1).split(",");
					String tempString ="\"";
					for(int count=1;count<splitvalue.length;count++){
						tempString += ((char) Integer.parseInt(splitvalue[count]));
					}
					tempString+="\"";
					templist.add(new ImmutableTriple<String, String, String>(Attr.getLeft(),Attr.getMiddle(),tempString));
				}
				else{
					templist.add(Attr);
				}
				
			}
			obj.setAttrValueList(templist);
			data.getPreObjList().add(obj);
		}

		ArrayList<String> ascList = clpState.getAscList();
		for (String str : ascList) {
			AscInstance asc = ParseAsc(str);
			data.getPreAscList().add(asc);
		}
  
		// 2. decompose poststate
		clpState = clpRet.getPostState();
		stateList = clpState.getObjList();
		for (String str : stateList) {
			ObjectInstance obj = ParseObj(str);
			ArrayList<Triple<String, String, String>> Attrlist =obj.getAttrValueList();
			ArrayList<Triple<String, String, String>> templist =new ArrayList<Triple<String, String, String>>();
			for(Triple<String, String, String> Attr:Attrlist){
				if(Attr.getMiddle().startsWith("collection")){
					if(Attr.getMiddle().endsWith("Integer")){
						String[] tempString=Attr.getRight().substring(1, Attr.getRight().length()-1).split(",",2);
						templist.add(new ImmutableTriple<String, String, String>(Attr.getLeft(),Attr.getMiddle(),"new ArrayList<Integer>(Arrays.asList("+tempString[1]+"))"));
					}
					else if(Attr.getMiddle().endsWith("String")){
						String tempStringList=Attr.getRight();
						tempStringList=tempStringList.substring(tempStringList.indexOf(",")+1, Attr.getRight().length()-1);
						String[] splitvalueList=tempStringList.split("]");
						tempStringList="";
						for(int i=0;i<splitvalueList.length;i++){
							if(i>0)
							{
								tempStringList+=",";
							}
							String[] splitvalue = splitvalueList[i].substring(1).split(",");
							String tempString ="\"";
							for(int count=1;count<splitvalue.length;count++){
								tempString += ((char) Integer.parseInt(splitvalue[count]));
							}
							tempString+="\"";
							tempStringList+=tempString;
						}
						templist.add(new ImmutableTriple<String, String, String>(Attr.getLeft(),Attr.getMiddle(),"new ArrayList<Integer>(Arrays.asList("+tempStringList+"))"));
					}
				}
				else if(Attr.getMiddle().equals("String")){
					String[] splitvalue = Attr.getRight().substring(1, Attr.getRight().length()-1).split(",");
					String tempString ="\"";
					for(int count=1;count<splitvalue.length;count++){
						tempString += ((char) Integer.parseInt(splitvalue[count]));
					}
					tempString+="\"";
					templist.add(new ImmutableTriple<String, String, String>(Attr.getLeft(),Attr.getMiddle(),tempString));
				}
				else{
					templist.add(Attr);
				}
			}
			obj.setAttrValueList(templist);
			data.getPostObjList().add(obj);
		}

		ascList = clpState.getAscList();
		for (String str : ascList) {
			AscInstance asc = ParseAsc(str);
			data.getPostAscList().add(asc);
		}

		// 3. decompose arg
		CLPArg arg = clpRet.getArg();
		// 3.1. self id
		data.setSelfID(ParseArgSelf(arg.getSelf()));
		// 3.2. ret
		ImmutablePair<String, String> argRet = ParseArgRet(arg.getRet());
		if(argRet.getLeft().startsWith("collection")){
			if(argRet.getLeft().endsWith("Integer")){
				String[] tempString=argRet.getRight().substring(1, argRet.getRight().length()-1).split(",",2);
				argRet=new ImmutablePair<String, String>(argRet.getLeft(),"new ArrayList<Integer>(Arrays.asList("+tempString[1]+"))");
			}
			else if(argRet.getLeft().endsWith("String")){
				String tempStringList=argRet.getRight();
				tempStringList=tempStringList.substring(tempStringList.indexOf(",")+1, argRet.getRight().length()-1);
				String[] splitvalueList=tempStringList.split("]");
				tempStringList="";
				for(int i=0;i<splitvalueList.length;i++){
					if(i>0)
					{
						tempStringList+=",";
					}
					String[] splitvalue = splitvalueList[i].substring(1).split(",");
					String tempString ="\"";
					for(int count=1;count<splitvalue.length;count++){
						tempString += ((char) Integer.parseInt(splitvalue[count]));
					}
					tempString+="\"";
					tempStringList+=tempString;
				}
				argRet=new ImmutablePair<String, String>(argRet.getLeft(),"new ArrayList<Integer>(Arrays.asList("+tempStringList+"))");
			}
		}
		else if(argRet.getLeft().equals("String")){
			String[] splitvalue = argRet.getRight().substring(1, argRet.getRight().length()-1).split(",");
			String tempString ="\"";
			for(int count=1;count<splitvalue.length;count++){
				tempString += ((char) Integer.parseInt(splitvalue[count]));
			}
			tempString+="\"";
			argRet=new ImmutablePair<String, String>(argRet.getLeft(),tempString);
		}
		data.setRet(argRet);
		// 3.3. arglist
		ArrayList<String> argValueList = arg.getArgList();
		UML2Operation info = model.findClassInfoByName(mClsName).findMethod(mMethodName);
		ArrayList<ImmutablePair<String, String>> argInfoList = info.getArgList();

		for (int i = 0; i < argValueList.size(); i++) {
			ImmutablePair<String, String> argInfo = argInfoList.get(i);
			String argValue = ParseArgArg(argValueList.get(i));
			if(argInfo.getRight().startsWith("collection")){
				if(argInfo.getRight().endsWith("Integer")){
					String[] tempString=argValue.substring(1, argValue.length()/2).split(",",2);
					argValue="new ArrayList<Integer>(Arrays.asList("+tempString[1]+"))";
				}
				else if(argInfo.getRight().endsWith("String")){
					String tempStringList=argValue;
					tempStringList=tempStringList.substring(tempStringList.indexOf(",")+1, argValue.length()/2);
					String[] splitvalueList=tempStringList.split("]");
					tempStringList="";
					for(int j=0;j<splitvalueList.length;j++){
						if(j>0)
						{
							tempStringList+=",";
						}
						String[] splitvalue = splitvalueList[j].substring(1).split(",");
						String tempString ="\"";
						for(int count=1;count<splitvalue.length;count++){
							tempString += ((char) Integer.parseInt(splitvalue[count]));
						}
						tempString+="\"";
						tempStringList+=tempString;
					}
					argValue="new ArrayList<Integer>(Arrays.asList("+tempStringList+"))";
				}
			}
			else if(argInfo.getRight().equals("String")){
				String[] splitvalue = argValue.replaceAll("\\[|\\]", "").split(",");
				argValue ="\"";
				for(int count=1;count<splitvalue.length/2;count++){
					argValue += ((char) Integer.parseInt(splitvalue[count]));
				}
				argValue +="\"";
			}
			else{
				argValue = argValue.replaceAll("\\[|\\]", "").split(",")[0];
			}
			ImmutableTriple<String, String, String> elm = new ImmutableTriple<String, String, String>(argInfo.getLeft(), argInfo.getRight(), argValue);
			data.getArgList().add(elm);
		}

		return data;
	}

	private String ParseArgArg(String sArg) {
		ANTLRInputStream input = new ANTLRInputStream(sArg);
		ResultLexer lexer = new ResultLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ResultParser parser = new ResultParser(tokens);
		ParseTree tree = parser.argArg(); // parse

		EvalArgArg eval = new EvalArgArg(model);

		return eval.visit(tree);
	}

	private ImmutablePair<String, String> ParseArgRet(String sArg) {
		ANTLRInputStream input = new ANTLRInputStream(sArg);
		ResultLexer lexer = new ResultLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ResultParser parser = new ResultParser(tokens);
		ParseTree tree = parser.argRet(); // parse

		UML2Operation info = model.findClassInfoByName(mClsName).findMethod(mMethodName);
		EvalArgRet eval = new EvalArgRet(info);
		return eval.visit(tree);
	}

	private Integer ParseArgSelf(String sArg) {
		ANTLRInputStream input = new ANTLRInputStream(sArg);
		ResultLexer lexer = new ResultLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ResultParser parser = new ResultParser(tokens);
		ParseTree tree = parser.argSelf(); // parse

		EvalArgSelf eval = new EvalArgSelf();
		return eval.visit(tree);
	}

	private AscInstance ParseAsc(String sCLPAsc) {
		ANTLRInputStream input = new ANTLRInputStream(sCLPAsc);
		ResultLexer lexer = new ResultLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ResultParser parser = new ResultParser(tokens);
		ParseTree tree = parser.ascElm();// parse

		EvalAscElm eval = new EvalAscElm(model);
		return eval.visit(tree);
	}

	private CLPResult ParseCLPResult(String sCLPResult) {
		ANTLRInputStream input = new ANTLRInputStream(sCLPResult);
		ResultLexer lexer = new ResultLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ResultParser parser = new ResultParser(tokens);
		ParseTree tree = parser.result();// parse

		EvalCLPResult eval = new EvalCLPResult();
		eval.visitResult((ResultContext) tree);

		return eval.getResult();
	}

	private ObjectInstance ParseObj(String sCLPCls) {
		ANTLRInputStream input = new ANTLRInputStream(sCLPCls);
		ResultLexer lexer = new ResultLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ResultParser parser = new ResultParser(tokens);
		ParseTree tree = parser.objElm();// parse

		EvalObjElm eval = new EvalObjElm(model);
		return eval.visit(tree);
	}
}
