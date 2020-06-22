package ccu.pllab.tcgen.clgGraph2Path;

  
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.AbstractConstraint.*;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.ast.ASTUtil;
import ccu.pllab.tcgen.libs.DresdenOCLASTtoInternelAST;
import ccu.pllab.tcgen.libs.pivotmodel.ClassDiagInfo;
import ccu.pllab.tcgen.libs.pivotmodel.ClassDiagToJson;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;
import ccu.pllab.tcgen.clgGraph2Path.CLGPath;

public class CLGReachingDefinitionAnalyzer extends CLGGraph{
	private ArrayList<Hashtable> hashtable=new ArrayList<Hashtable>();
	private ArrayList<Hashtable> hashtable1=new ArrayList<Hashtable>();
	private ArrayList<String> def = new ArrayList<String>();
	private ArrayList<String> use = new ArrayList<String>();
	public CLGReachingDefinitionAnalyzer(){
		this.hashtable.clear(); 
		this.hashtable1.clear();
	}

	//614
	public ArrayList<DUP> parsePathDUP(CLGPath clgpath, ArrayList<DUP>dup){
		ArrayList<DUP> pathDUP = new ArrayList<DUP>();
		List<CLGNode> pnode = clgpath.getPathNodes();
		
		for(int i=0;i<pnode.size();i++){ 
			for(int j=0;j<dup.size();j++){ 
				if(((CLGNode)dup.get(j).getDefineNode()).equals(pnode.get(i))){
					ArrayList<CLGNode> arrclg = new ArrayList<CLGNode>();
					for(int i11=0;i11<pnode.size();i11++){ 
						arrclg.add(pnode.get(i11));
					}
					for(int arrco=0;arrco<=i;arrco++){
						arrclg.remove(0);
					}
					if(arrclg.contains(((CLGNode)dup.get(j).getUseNode())))
					if(((CLGNode)dup.get(j).getUseNode()).equals(pnode.get(i+1))){
						if(!pathDUP.contains(dup.get(j))) pathDUP.add(dup.get(j));
					}//if is dup , add
					else{ 
						for(int i1=i+1;i1<pnode.size();i1++){ 
							ArrayList<CLGNode> regno = new ArrayList<CLGNode>();
							for(int j11=0;j11<dup.size();j11++){ 
								regno.add(dup.get(j11).getDefineNode());
							}
							boolean boolflag =false;
							for(int j1=0;j1<dup.size();j1++){ 
								if(((CLGNode)dup.get(j).getUseNode()).equals(pnode.get(i1))){
									if(!pathDUP.contains(dup.get(j))) pathDUP.add(dup.get(j));
									//System.out.println(" !! find~");
								}
								else{
									if(regno.contains(pnode.get(i1))){
										int indexnum=0;
										for(int regnum=0;regnum<regno.size();regnum++){
											if(regno.get(regnum).equals(pnode.get(i1))){
												indexnum=regnum;
												String var1 = dup.get(indexnum).getVariable();
												String var2 = dup.get(j).getVariable();
												var1=var1.substring(0, var1.indexOf("_"));
												var2=var2.substring(0, var2.indexOf("_"));
												if(var1.equals(var2)){
													j1=dup.size(); i1=pnode.size();
													boolflag=true; break;
												}
												else{
													if(j1==dup.size()-1 && boolflag==false)
														if(!pathDUP.contains(dup.get(j))) pathDUP.add(dup.get(j));
												}
											}
										}
									}//end reg con	
								}
							}//end for j1
						}
					}//end else
				}
			}//end for j
		}
		return pathDUP;
	}
	
	public void parseDefUse(CLGOperatorNode clgnode){
		if(clgnode.getRightOperand() instanceof CLGOperatorNode){
			parseDefUse((CLGOperatorNode)clgnode.getRightOperand());
		}
		else if(clgnode.getRightOperand() instanceof CLGVariableNode){
			if(!use.contains(((CLGVariableNode)clgnode.getRightOperand()).getName())){
				String useste = ((CLGVariableNode)clgnode.getRightOperand()).getName();
				//useste=useste.replaceAll("result", "self");
				//useste=useste.replaceAll("Result", "self");
				
				String usestr1 = useste;
				if(usestr1.contains("@pre")){
					usestr1 =  useste.replaceAll("@pre", "");
					usestr1 += "@pre";
				}
				if(usestr1.contains("#LetVar_")){
					usestr1=usestr1.replaceAll("#LetVar_", "");
				}
				if(usestr1.contains("#Iterate")){
					usestr1=usestr1.replaceAll("#Iterate", "");
				}
				if(usestr1.contains("#")){
					usestr1=usestr1.replaceAll("#", "");
				}
				//----
				if(usestr1.contains("[")){
					String instr = usestr1.substring(usestr1.indexOf("[")+1, usestr1.length()-1);
					if(instr.contains("+"))
						instr = instr.substring(0, instr.indexOf("+"));
					if(instr.contains("-"))
						instr = instr.substring(0, instr.indexOf("-"));
					if(instr.contains("*"))
						instr = instr.substring(0, instr.indexOf("*"));
					
					if(!use.contains(instr)){
						if(!(((CLGVariableNode)clgnode.getRightOperand()).getName()).contains("!"))
							use.add(instr);
					}
					
					usestr1=usestr1.substring(0, usestr1.indexOf("["));
				}
				
				if(!use.contains(usestr1)){
//					if(!clgnode.getRightOperand().getImgInfo().contains("!"))
					if(!(((CLGVariableNode)clgnode.getRightOperand()).getName()).contains("!"))
						use.add(usestr1);
				}
			}
		}
		
		if(clgnode.getLeftOperand() instanceof CLGOperatorNode){
			parseDefUse((CLGOperatorNode)clgnode.getLeftOperand());
		}
		else if(clgnode.getLeftOperand() instanceof CLGVariableNode){
			String strop = clgnode.getOperator();
			strop=strop.replaceAll(" ", ""); 
			if(strop.equals("=")) {  
				if(!def.contains(((CLGVariableNode)clgnode.getLeftOperand()).getName())){
					String defstr = ((CLGVariableNode)clgnode.getLeftOperand()).getName();
					//defstr=defstr.replaceAll("result", "self");
					//defstr=defstr.replaceAll("Result", "self");
					if(def.indexOf("result")!=-1){
						def.remove("result");
					}
					if(def.indexOf("Result")!=-1){
						def.remove("Result");
					}
					if(defstr.contains("#LetVar_")){
						defstr=defstr.replaceAll("#LetVar_", "");
					}
					if(defstr.contains("#Iterate")){
						defstr=defstr.replaceAll("#Iterate", "");
					}
					if(defstr.contains("#")){
						defstr=defstr.replaceAll("#", "");
					}
					
					if(defstr.contains("[")){
						String useStr = defstr.substring(
								defstr.indexOf("[")+1, defstr.indexOf("]"));
						if(useStr.contains("+"))
							useStr = useStr.substring(0, useStr.indexOf("+"));
						if(useStr.contains("-"))
							useStr = useStr.substring(0, useStr.indexOf("-"));
						if(useStr.contains("*"))
							useStr = useStr.substring(0, useStr.indexOf("*"));
						
						if(!use.contains(useStr))
							use.add(useStr);
						
						defstr=defstr.substring(0, defstr.indexOf("["));
					}
					
					if(!def.contains(defstr))
						def.add(defstr);
				}
			}
			else {
				if(!use.contains(((CLGVariableNode)clgnode.getLeftOperand()).getName())){
					String useste = ((CLGVariableNode)clgnode.getLeftOperand()).getName();
					//useste=useste.replaceAll("result", "self");
					//useste=useste.replaceAll("Result", "self");
					
					String usestr1 = useste;
					if(usestr1.contains("@pre")){
						usestr1 =  useste.replaceAll("@pre", "");
						usestr1 += "@pre";
					}
					if(usestr1.contains("#LetVar_")){
						usestr1=usestr1.replaceAll("#LetVar_", "");
					}
					if(usestr1.contains("#Iterate")){
						usestr1=usestr1.replaceAll("#Iterate", "");
					}
					if(usestr1.contains("#")){
						usestr1=usestr1.replaceAll("#", "");
					}
					//----
					if(usestr1.contains("[")){
						String instr = usestr1.substring(usestr1.indexOf("[")+1, usestr1.length()-1);
						if(instr.contains("+"))
							instr = instr.substring(0, instr.indexOf("+"));
						if(instr.contains("-"))
							instr = instr.substring(0, instr.indexOf("-"));
						if(instr.contains("*"))
							instr = instr.substring(0, instr.indexOf("*"));
						
						if(!use.contains(instr)){
								use.add(instr);
						}
						usestr1=usestr1.substring(0, usestr1.indexOf("["));
					}
					if(!use.contains(usestr1))
						use.add(usestr1);
				}
			}
		}
	}
	public ArrayList<DUP> dupGenerate(CLGGraph clg, String classname, String methodname){
		ArrayList<Integer> consnum = new ArrayList<Integer>();
		Set<Integer> numset = clg.getConstraintCollection().keySet();/*for(obj o: collection<obj>)  -> enhanceFor*/
		Iterator iterator = numset.iterator();
	    while(iterator.hasNext()){
	    	consnum.add((Integer)iterator.next());
	    }
	    //define
	    ArrayList<String> alldef = new ArrayList<String>(); // final all def~
	    ArrayList<String> alldef415;// = new ArrayList<String>();
	    ArrayList<ArrayList<String>> def415 = new ArrayList<ArrayList<String>>();//every node def
	    //--------------------
	    ArrayList<Integer> alldefindex = new ArrayList<Integer>();
	    //--
	    ArrayList<String> checkdefin = new ArrayList<String>();
	    //use
	    ArrayList<String> alluse330 = new ArrayList<String>();
	    ArrayList<String> totaluse415 = new ArrayList<String>();
	    ArrayList<ArrayList<String>> use415 = new ArrayList<ArrayList<String>>();
	    
	    // gen and kill
	    ArrayList<ArrayList<Integer>> gen315 = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> kill315 = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> finalgen315 = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> finalkill315 = new ArrayList<ArrayList<Integer>>();
	    
	    
	    //---------------------
	    //fine define
	    for(int i=0;i<consnum.size();i++){ 
			CLGConstraint clgcon = clg.getConstraintNodeById(consnum.get(i)).getConstraint();
			 alldef415 = new ArrayList<String>();
			//-- 此constraint 是 method, sereach hashtable(classname, methodname,
			//	 CLG and define use).
			if(clgcon instanceof CLGMethodInvocationNode){ 
				CLGMethodInvocationNode clgmethod = (CLGMethodInvocationNode)clgcon;
				for(int j=0;j<this.hashtable.size();j++){
					if(clgmethod.getMethodName().equals(this.hashtable.get(j).get("method_name"))){
						alldef415 = (ArrayList<String>)this.hashtable.get(j).get("define");
						def415.add(alldef415);
						use415.add((ArrayList<String>)this.hashtable.get(j).get("use"));
						break;
					}
					if(this.hashtable.size()==0){
						System.out.println("please set hashtable!");
					}
				}
			}//end if CLGMethodInvocationNode
			//此constraint 是 guard can direct analysis.
			else if(clgcon instanceof CLGOperatorNode){
				CLGOperatorNode clgguard = (CLGOperatorNode)clgcon;
				def = new ArrayList<String>();
				use = new ArrayList<String>();
				this.parseDefUse(clgguard); //79
				def415.add(def);  use415.add(use);
				Hashtable ht=new Hashtable();
				ht.put("define", def); ht.put("use", use); ht.put("nodenum", consnum.get(i));
				ht.put("class_name", classname); ht.put("method_name", methodname); ht.put("clg", clg);
				this.hashtable.add(ht);
				
			}//end if CLGOperatorNode
		}//------------end for i------------
	    
	    //-----------------------------------------------
	    for(int defsize=0;defsize<def415.size();defsize++){
			for(int defsize1=0;defsize1<def415.get(defsize).size();defsize1++){
				if(!alldef.contains(def415.get(defsize).get(defsize1))){
					alldef.add(def415.get(defsize).get(defsize1));
					alldefindex.add(1);
				}
				String strdef = def415.get(defsize).get(defsize1);
				strdef= strdef.replaceAll("self.", "");
				checkdefin.add(strdef); //703
			}
		}
	    //-----------end for defsize------------------
	    System.out.println(""+alldefindex);
	   // System.out.println("703 "+alldefindex);
	    
		//------------find num of the define------------
		int defsizecount=0;
		for(int i=0;i<def415.size();i++){
			defsizecount+=def415.get(i).size();
		}
		//-----------------end find clg define-----------------
		System.out.println("size = "+defsizecount);
		//System.out.println(" 79 size = "+defsizecount);
		//---------------------------build gen---------------------------
		int defbit=0;
		ArrayList<Integer> saveposition = new ArrayList<Integer>();
		for(int i=0;i<def415.size();i++){
			ArrayList<Integer> reggen315 = new ArrayList<Integer>();
			for(int k=0;k<defsizecount;k++){ //initial reggen
				reggen315.add(0);
			}//end for k
			if(def415.get(i).size()!=0){//int i1=i;
				for(int j=0;j<def415.get(i).size();j++){
					reggen315.set(defbit, 1);
					defbit++;
					gen315.add(reggen315);
					saveposition.add(i);
				}
			}
			else{
				gen315.add(reggen315); 
				saveposition.add(i);
			}
		}//end for i
		System.out.println("all gen= "+gen315+" \n position = "+saveposition+"\n gen size= "+gen315.size());
		//System.out.println("416.620 all gen= "+gen315+" \n position = "+saveposition+"\n gen size= "+gen315.size());
		//---------------------------end bulid gen---------------------------
		
		//----------------------bulid kill-----------------------------
		for(int i=0;i<def415.size();i++){
			ArrayList<Integer> regkill315 = new ArrayList<Integer>();
			for(int l=0;l<def415.size();l++){ //initial regkill315
				for(int m=0;m<def415.get(l).size();m++){
					regkill315.add(0);
				}
			}//end for j
			if(def415.get(i).size()==0)  kill315.add(regkill315);
			for(int j=0;j<def415.get(i).size();j++){
				for(int k=0;k<def415.size();k++){
					if(k!=i && def415.get(k).contains(def415.get(i).get(j))){
						int numm=0;
						for(int l=0;l<k;l++){
							numm += def415.get(l).size();
						}
						numm+=def415.get(k).indexOf(def415.get(i).get(j));
						regkill315.set(numm, 1);
					}
				}
				kill315.add(regkill315);
			}//end for j
		}
		//----------------------end build kill------------
		// --------------merge gen and kill-----------------------
		ArrayList<Integer> inte = new ArrayList<Integer>();
		for(int j=0;j<def415.size();j++){
			for(int k=0;k<def415.get(j).size();k++){
				inte.add(0);
			}
		}
		for(int j=0;j<def415.size();j++){
			finalgen315.add(inte);
			finalkill315.add(inte);
		}
		for(int i=0;i<def415.size();i++){
			if(def415.get(i).size()==0 || def415.get(i).size()==1){
				int counum=0;
				for(int j=0;j<i;j++){
					if(def415.get(j).size()==0 || def415.get(j).size()==1){
						counum+=1;
					}else counum+=def415.get(j).size();
				}
				finalgen315.set(i, gen315.get(counum));
						
				finalkill315.set(i, kill315.get(counum));
			}
			else{
				int counum=0;
				for(int j=0;j<i;j++){
					if(def415.get(j).size()==0 || def415.get(j).size()==1){
						counum+=1;
					}else counum+=def415.get(j).size();
				}
				ArrayList<Integer> bitgen = new ArrayList<Integer>();
				ArrayList<Integer> bitgen1 = new ArrayList<Integer>();
						
				ArrayList<Integer> bitkill = new ArrayList<Integer>();
				ArrayList<Integer> bitkill1 = new ArrayList<Integer>();
				for(int j1=0;j1<def415.size();j1++){
					for(int k1=0;k1<def415.get(j1).size();k1++){
						bitgen.add(0); bitgen1.add(0);
						bitkill.add(0); bitkill1.add(0);
					}
				}
				for(int k=0;k<def415.get(i).size();k++){
					if(k==0){
						bitgen = gen315.get(counum+k);
						bitkill = kill315.get(counum+k);
					}
					else{
						for(int z=0;z<bitgen.size();z++){
							bitgen1.set(z, ( bitgen.get(z) | gen315.get(counum+k).get(z)) );
							bitgen=bitgen1;
						}
						for(int z=0;z<bitkill.size();z++){
							bitkill1.set(z, ( bitkill.get(z) | kill315.get(counum+k).get(z)) );
							bitkill=bitkill1;
						}
					}
							
				}
				finalgen315.set(i, bitgen1); 
				finalkill315.set(i, bitkill1);
			}//end else
		}//end for i
		//-------------------------------------------------
		
		//------------------build pre node---------------------------------
		ArrayList<ArrayList<Integer>> prenode315 = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> prenode418 = new ArrayList<ArrayList<Integer>>();
		
		for(int i=0;i<consnum.size();i++){ //這個狀態圖有size個constraintnode
			CLGNode clgnode = clg.getConstraintNodeById(consnum.get(i));
			ArrayList<Integer> regprenode = new ArrayList<Integer>();
			
			ArrayList<Integer> regprenode418 = new ArrayList<Integer>();
			ArrayList<CLGNode> savepre = new ArrayList<CLGNode>();
			
					
			for(int j=0;j<clgnode.getPredecessor().size();j++){ //此constraint的pre
				//--418
				savepre.add(clgnode.getPredecessor().get(j));
				
				if(clgnode.getPredecessor().get(j) instanceof CLGConnectionNode){
					CLGConnectionNode connode1 = (CLGConnectionNode)clgnode.getPredecessor().get(j) ;
							
					for(int k=0;k<connode1.getPredecessor().size();k++){
						if(connode1.getPredecessor().get(k) instanceof CLGStartNode){
							regprenode.add(0);
						}
						else if(connode1.getPredecessor().get(k) instanceof CLGConnectionNode){
							//connode1 = (CLGConnectionNode)connode1.getPredecessor().get(k) ;
						}
						else if(connode1.getPredecessor().get(k) instanceof CLGConstraintNode){
							CLGConstraintNode inpre = (CLGConstraintNode)connode1.getPredecessor().get(k);
							regprenode.add(inpre.getXLabelId());
						}
					}
				}
				else if(clgnode.getPredecessor().get(j) instanceof CLGConstraintNode){
					CLGConstraintNode inpre = (CLGConstraintNode)clgnode.getPredecessor().get(j);
					regprenode.add(inpre.getXLabelId());
				}
			}//end for j
			for(int k=0;k<savepre.size();k++){
				if(savepre.get(k) instanceof CLGConnectionNode){
					for(int k1=0;k1<savepre.get(k).getPredecessor().size();k1++){
						savepre.add(savepre.get(k).getPredecessor().get(k1));
						if(savepre.get(k).getPredecessor().get(k1) instanceof CLGStartNode){
							if(savepre.get(k).getPredecessor().size()==1)
								regprenode418.add(0);
						}
					}
				}
				else if(savepre.get(k) instanceof CLGConstraintNode){
					CLGConstraintNode inpre = (CLGConstraintNode)savepre.get(k);
					regprenode418.add(inpre.getXLabelId());
				}
			}
			prenode315.add(regprenode);
			prenode418.add(regprenode418);
		}//end for i
		//------------------end build pre node 3/10---------------------------------
		
		ArrayList<Integer> saveregin315 = new ArrayList<Integer>();
		ArrayList<Integer> initialin315 = new ArrayList<Integer>();
		ArrayList<Integer> saveregout315 = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> regout315 = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> regin315 = new ArrayList<ArrayList<Integer>>();
		//------------------------initial in out------------------------
		for(int j=0;j<finalgen315.get(0).size();j++){
			saveregin315.add(0);  saveregout315.add(0); initialin315.add(0);
		}
		for(int i=0;i<finalgen315.size();i++){
			regout315.add(saveregout315);  regin315.add(saveregin315);
		}
		//----------------end initial in out------------------------
				
		//------------------------bulid in---------------------------
		Boolean checkout315=true; 
		ArrayList<Integer> regprelist315 = new ArrayList<Integer>();
		while(checkout315){
			ArrayList<ArrayList<Integer>> copyout = new ArrayList<ArrayList<Integer>>(regout315);
			for(int i=0;i< prenode418.size();i++){
				regprelist315=prenode418.get(i);
				//record pre_in
				int arrayin[] = new int[regout315.get(0).size()];
				int incount=0;
				for(int j=0;j<regprelist315.size();j++){
					if(regprelist315.size()==1 && regprelist315.get(j).equals(0)){
						saveregin315=initialin315;
					}
					else{
						incount++;
						if(incount==1) {
							for(int k=0;k<regout315.get(consnum.indexOf(regprelist315.get(j))).size();k++){
								arrayin[k]=regout315.get(consnum.indexOf(regprelist315.get(j))).get(k);
							}
						}
						else{
							for(int k=0;k<arrayin.length;k++){//regprelist315
								int tem = (arrayin[k] | (regout315.get(consnum.indexOf(regprelist315.get(j))).get(k)));
								arrayin[k]=tem;
							}
						}//end else
					}//end else
				}//end for j
				if(incount==0) {  regin315.set(i, saveregin315);}
				else {
					ArrayList<Integer> r = new ArrayList<Integer>();
					for(int j=0;j<arrayin.length;j++){
						r.add(arrayin[j]);
					}
					regin315.set(i, r);
				}
				//--------------------------build out------------
				int arrayout[] = new int[arrayin.length];
				for(int j=0;j<arrayin.length;j++){
					if(arrayin[j]==1  && finalkill315.get(i).get(j).equals(1)){
						arrayout[j]=0;
					}
					else if(arrayin[j]==1 && finalkill315.get(i).get(j).equals(0)){
						arrayout[j]=1;
					}
					else if(arrayin[j]==0){
								arrayout[j]=0;
					}
				}
				for(int k=0;k<arrayout.length;k++){
					int tem = (arrayout[k] | finalgen315.get(i).get(k));
					arrayout[k]=tem;
				}
				//----end build out--------------------------------
				ArrayList<Integer> r1 = new ArrayList<Integer>();
				for(int j=0;j<arrayout.length;j++){
					r1.add(arrayout[j]);
				}
				regout315.set(i, r1);
			}//end for i
			if(copyout.equals(regout315)) { checkout315=false; 	}
		}//end while
		//------------------end build in out----------------------
		
		//--------------build DUP 
		ArrayList<Integer> recordposi = new ArrayList<Integer>();
				
		for(int r1=0;r1<def415.size();r1++){
			if(def415.get(r1).size()!=0){
				for(int r2=0;r2<def415.get(r1).size();r2++){
					recordposi.add(consnum.get(r1));
				}
			}
		}//end for r1
		
		ArrayList<DUP> listdup316 = new ArrayList<DUP>();
		
		
		ArrayList<String> usarr = new ArrayList<String>();
		ArrayList<Integer> usint = new ArrayList<Integer>();
		
		for(int i=0;i<regin315.size();i++){ 
			ArrayList<Integer> inregin = new ArrayList<Integer>();
			inregin=regin315.get(i);
			for(int j=0;j<inregin.size();j++){// j= def
				if(inregin.get(j)==1){ //yes
					if(use415.get(i).size()!=0){// has use
						for(int usecc=0;usecc<use415.get(i).size();usecc++){
							String us33 = use415.get(i).get(usecc);
							us33=us33.replaceAll("self@pre.", "");
							us33=us33.replaceAll("@pre", "");
							us33=us33.replaceAll("self.", "");
							
							if(checkdefin.get(j).equals(us33)){//703
								String strdupvar="";
								if(!usarr.contains(us33)){
									usarr.add(us33); usint.add(1);
									strdupvar = us33+"_1";
								}
								else{
									int c = (usint.get(usarr.indexOf(us33))+1);
									usint.set(usarr.indexOf(us33),c);
									strdupvar = us33+"_"+c;
								}
								DUP dup = new DUP(strdupvar, clg.getConstraintNodeById(recordposi.get(j)),
										clg.getConstraintNodeById(consnum.get(i)));
								listdup316.add(dup);
							}
						}
					}
				}//end if(inregin.get(j)==1)
			}//end for k
		}//end for i
		
	
		
		String aa = "";
		for(int i=0;i<this.hashtable.size();i++){
			aa+=this.hashtable.get(i)+"\n";
		}
		DataWriter.writeInfo(aa, "hashtable", "java",DataWriter.output_folder_path,"hashtable");
		return listdup316;
	}
	
}
