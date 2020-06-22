package ccu.pllab.tcgen.sd2clg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import com.parctechnologies.eclipse.EclipseException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ccu.pllab.tcgen.AbstractCLG.CLGConnectionNode;
//import ccu.pllab.tcgen.home_sd2clg.TestData;//////////////
import ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode;
import ccu.pllab.tcgen.AbstractConstraint.*;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestDataClassLevel;
import ccu.pllab.tcgen.TestCase.TestScriptGeneratorClassLevel;
import ccu.pllab.tcgen.ast.ASTNode;
import ccu.pllab.tcgen.ast.ASTUtil;
import ccu.pllab.tcgen.ast.OperationCallExp;
import ccu.pllab.tcgen.ast.PropertyCallExp;
import ccu.pllab.tcgen.libs.DresdenOCLASTtoInternelAST;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.ClassDiagInfo;
import ccu.pllab.tcgen.libs.pivotmodel.ClassDiagToJson;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.clgGraph2Path.CoverageCriterionManager;
import ccu.pllab.tcgen.exe.main.Main;

import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import java.util.HashMap;

 

public class SDXML2SD{
	
	
	public SDXML2SD(){
	}
	
	public CLGGraph startToConvert(Element root,ArrayList<String> attributestr,String packageName,IModel model,DresdenOCLASTtoInternelAST ast_constructor)throws ParserConfigurationException, SAXException, IOException, TemplateException, ModelAccessException, JSONException, org.apache.commons.cli.ParseException, ParseException, EclipseException
	//public CLGGraph startToConvert(Element root,ArrayList<String> attributestr,String packageName,File uml)throws ParserConfigurationException, SAXException, IOException, TemplateException, ModelAccessException, JSONException, org.apache.commons.cli.ParseException, ParseException, EclipseException	
	{
		CLGGraph clg =new CLGGraph();
		NodeList firstRegionChildList = root.getChildNodes();
		//declare some of  basic data structure //maybe  state
		ArrayList<Element> subvertexArray = new ArrayList<>();
		ArrayList<Element> transitionArray = new ArrayList<>();
		ArrayList<Element> guardArray = new ArrayList<>();
		ArrayList<Element> methodArray = new ArrayList<>();
		ArrayList<Element> triggerArray = new ArrayList<>();
		
		ArrayList<String> recordconnectionnode = new ArrayList<>();//To record connect node
		StateDigram StateD = null ;
		String SDname=((Element)(root.getParentNode())).getAttribute("name");

		StateD = new StateDigram(SDname,attributestr);
		HashMap<String,StateDigram> saveAllSubSD=new HashMap<String,StateDigram>();
		HashMap<String,State> saveStateId=new HashMap<String,State>();
		HashMap<State,CLGGraph> saveAllSubCLG=new HashMap<State,CLGGraph>();
		ArrayList<String> sdguard = new ArrayList<>();//sd guard :arraylist  like a>1
		FinalState finalstate = null;
		InitialState initialstate = null ;	
		
		//set basic data structure
		int subvertexN=0, transN=0;
		for(int i=0;i<firstRegionChildList.getLength();i++)
		{//distinguish state and transition from child Node
			Node childNode =firstRegionChildList.item(i);
			if(childNode.getNodeName().equals("subvertex"))
			{
				Element subvertex= (Element) childNode;
				subvertexArray.add(subvertexN++,subvertex);
			}//It is OK!
			else if(childNode.getNodeName().equals("transition"))
			{
				Element transition= (Element) childNode;
				transitionArray.add(transN++,transition);
			}
		}
		for(int i=0;i<transitionArray.size();i++)
		{//distinguish state and transition from child Node
			Element transitionChildElement =transitionArray.get(i);
			NodeList attributeChildList = transitionChildElement.getChildNodes();
			for(int j=0;j<attributeChildList.getLength();j++)
			{
				Node attributeChild=attributeChildList.item(j);
			if( attributeChild.getNodeName().equals("ownedRule"))
			{
				guardArray.add((Element)attributeChild);
			}
			else if(attributeChild.getNodeName().equals("trigger"))
			{	
				triggerArray.add((Element)attributeChild);
			}
			else if(attributeChild.getNodeName().equals("effect"))
			{
				methodArray.add((Element)attributeChild);
			}
			}
		}//It may be OK!
		String guard_cons = null;
		ArrayList<String> guard_conslist = new ArrayList<String>();
		CLGConstraint method = null, guard = null;
		State source = null, target = null;
		int connect=0;
		HashMap<String,Element> generalSubvertex=new HashMap<String,Element>();
		for (int temp_subvertex = 0; temp_subvertex <  subvertexArray.size(); temp_subvertex++) {
			Element eElement_subvertex=  subvertexArray.get(temp_subvertex);
			if(eElement_subvertex.getAttribute("xmi:type").equals("uml:Pseudostate"))
			{
				initialstate = new InitialState(1, "InitialState");
				//initialstate = new InitialState(1, "InitialState",eElement_subvertex.getAttribute("xmi:id"));
			}
			else if(eElement_subvertex.getAttribute("xmi:type").equals("uml:FinalState"))
			{
				finalstate = new FinalState(1, "InitialState");
	//			finalstate = new FinalState(2, "FinalState",eElement_subvertex.getAttribute("xmi:id")); 
			}
			else
			{
				//State st = new State(2+connectionnodecount,eElement_subvertex.getAttribute("name"));
				//StateD.addstate(new State(connect+3,eElement_subvertex.getAttribute("name"),eElement_subvertex.getAttribute("xmi:id")));
				State st=new State(connect+3,eElement_subvertex.getAttribute("name"));
				StateD.addstate(st);
				if(eElement_subvertex.hasChildNodes())
				{
					int j=0;
					for(;!eElement_subvertex.getChildNodes().item(j).getNodeName().equals("region");j++);
					clg=	startToConvert((Element)(eElement_subvertex.getChildNodes().item(j)),attributestr,packageName,model,ast_constructor);
				//	clg=	startToConvert((Element)(eElement_subvertex.getChildNodes().item(j)),attributestr,packageName,uml);
					saveAllSubCLG.put(st, clg);
					saveStateId.put(""+(connect+3), st);
				}
				connect++;
			}
			generalSubvertex.put(eElement_subvertex.getAttribute("xmi:id"),eElement_subvertex);
		}

		ArrayList<INode> ocl_guard324 = new ArrayList<INode>();//find dom ana
		for (int temp_transition = 0; temp_transition < transitionArray.size(); temp_transition++)
		{//處理transition
			Node nNode_transition = transitionArray.get(temp_transition);
			Element eElement_transition = (Element) nNode_transition;
			if(!eElement_transition.getAttribute("guard").isEmpty())
			{//如果有護衛條件
				for (int temp_guard = 0; temp_guard < guardArray.size(); temp_guard++) 
				{//將所有transition做處理
					Element eElement_guard = guardArray.get(temp_guard);
					
					if(eElement_transition.getAttribute("guard").equals(eElement_guard.getAttribute("xmi:id")))
					{//如果transition上的護衛條件的ID與ownedRule的ID同樣
						guard_conslist.add(eElement_guard.getAttribute("name"));
						//---------parse guard -------
						String inocl, endocl, guardocl = null;
						inocl="package tcgen\n\tcontext "+SDname+"\n\tinv:\n\t\t";
						endocl="\nendpackage";
						guardocl=eElement_guard.getAttribute("name");
						File dir1 = new File("../Examples/"+packageName+"guard"); 
						dir1.mkdir();   
						FileWriter dataFile = new FileWriter("../Examples/"+packageName+"guard/"+SDname+temp_transition+".ocl");
						BufferedWriter input = new BufferedWriter(dataFile);
						input.write(inocl+guardocl+endocl);
						input.close();
						
					/*	Model context;
						IModel model;
						ClassDiagInfo class_diag_info;
						ClassDiagToJson class_diag_to_json;
					//	List<ccu.pllab.tcgen.ast.Constraint>OCLCons;
						
						class_diag_to_json = new ClassDiagToJson(uml);
						class_diag_info = new ClassDiagInfo(class_diag_to_json);
						context = new Model(class_diag_info);
						TypeFactory.getInstance().setModel(context);
						ASTUtil ast_util = new ASTUtil();
						DresdenOCLASTtoInternelAST ast_constructor = new DresdenOCLASTtoInternelAST(ast_util, context);*/
						
						//StandaloneFacade.INSTANCE.initialize(new URL("file:./log4j.properties"));
						//model = StandaloneFacade.INSTANCE.loadUMLModel(uml, new File("./resources/org.eclipse.uml2.uml.resources.jar"));
						List<tudresden.ocl20.pivot.pivotmodel.Constraint> loaduml= StandaloneFacade.
								INSTANCE.parseOclConstraints(model, new File("../Examples/"+packageName+"guard/"+packageName+temp_transition+".ocl"));
						List<ccu.pllab.tcgen.ast.Constraint> OCLCons = ast_constructor.parseOclTreeNodeFromPivotModel(loaduml);
						List<CLGConstraint> testguard=new ArrayList<CLGConstraint>();
						for(int i=0;i<OCLCons.size();i++)
						{
							ASTNode testast = OCLCons.get(i).toPreProcessing();//ASTNode:抽象語法樹
							List<INode> testast_1 = null;
							int savesize=0;
							testast_1 = testast.getNextNodes();
							savesize=testast_1.size();
							for(int j=0;j<savesize;j++)
							{
								ocl_guard324.add(testast_1.get(j));//ocl guard 324是什麼   一個arraylist
								if(j==savesize-1)
								{
									if(testast_1.get(0).getNextNodes().size()!=0)
									{
										testast_1=testast_1.get(0).getNextNodes();//新的抽象語法樹
										savesize=testast_1.size();
										j=-1;//重新一次
									}
								}
							}
						}
					}
				}
			}
		}//end guard*/
		
				
		
		for (int temp_transition = 0; temp_transition < transitionArray.size(); temp_transition++) {
			Node nNode_transition = transitionArray.get(temp_transition);
			Element eElement_transition = (Element) nNode_transition;
			
guard_conslist = new ArrayList<String>();
			
			if(!eElement_transition.getAttribute("guard").isEmpty())
			{	
				for (int temp_guard = 0; temp_guard < guardArray.size(); temp_guard++) 
				{
					Element eElement_guard =guardArray.get(temp_guard);
					if(eElement_transition.getAttribute("guard").equals(eElement_guard.getAttribute("xmi:id")))//transition上的guard的ID和guard的ID同
					{//在護衛條件的list加上護衛條件的名字
						guard_cons=eElement_guard.getAttribute("name");
						guard_conslist.add(eElement_guard.getAttribute("name"));//有一個arraylist存guard的name   //似乎可以加break
					}
				}
				List<CLGOperatorNode> listguard=new ArrayList<CLGOperatorNode>();
				for(int abc=0;abc<ocl_guard324.size();abc++)
				{
					String a = ocl_guard324.get(abc).toString();
					String regexp = a.replaceAll("[\\(\\)]", "");
					regexp=regexp.replaceAll(" ","");
					regexp=regexp.replaceAll("self.","");// [ \ ( ) ] " " self. 都不行用
					
					for(int i=0;i<guard_conslist.size();i++)
					{
						String gustr = guard_conslist.get(i);//guard_conlist是arraylist
						gustr=gustr.replaceAll(" ","");
						if(regexp.equals(gustr))
						{//如果regexp = gustr
							OperationCallExp _exp = (OperationCallExp) ocl_guard324.get(abc);//operation
							final List<CLGConstraint> intopreguard = new ArrayList<CLGConstraint>();//一個CLGConstraint列表
						
							CLGOperatorNode guardna=null;
							ccu.pllab.tcgen.AbstractConstraint.CLGConstraint clgliteral=null, clgvariable=null;//限定只有AbstractConstraint.CLGConstraint才行
							String TotalL=null, TotalR=null, Total=null;
							OperationCallExp Ori_exp= (OperationCallExp)_exp;
							if(Ori_exp.getSourceExp() instanceof OperationCallExp)//需要看getSourceExp
							{//如果是operation
								Ori_exp=(OperationCallExp)Ori_exp.getSourceExp();//有Ori_exp是因為for迴圈
								clgvariable=parseSD(Ori_exp);//clgvariable是CLGConstraint  用在第296行  parseSD是用在還能分解ori_exp
								Total=_exp.getPropertyName();//Total是放屬性名字
								guardna = new CLGOperatorNode(Total);//將屬性設定成operator node
							}
							else if(Ori_exp.getSourceExp() instanceof PropertyCallExp)
							{//如果是property
								PropertyCallExp p =(PropertyCallExp)Ori_exp.getSourceExp();
								TotalL=p.getPropertyName();
								clgvariable=new CLGVariableNode(TotalL);//CLGVariableNode查看看  PropertyCallExp設定成variable node
								Total=_exp.getPropertyName();
								guardna = new CLGOperatorNode(Total);//CLGOperatorNode查看看
							}
							else if(Ori_exp.getSourceExp().toString().matches("[0-9]+"))
							{//如果是數字
								TotalL=Ori_exp.getSourceExp().toString();
								clgvariable=new CLGLiteralNode(TotalL);//CLLiteralNode查看看
								Total=_exp.getPropertyName(); 
								guardna = new CLGOperatorNode(Total);
							}
							if(_exp.getParameterExps().get(0) instanceof OperationCallExp)
							{//如果是operation
								Ori_exp=(OperationCallExp)_exp.getParameterExps().get(0);
								clgliteral=parseSD(Ori_exp);//如果還能分解ori_exp
								Total=_exp.getPropertyName();
								guardna = new CLGOperatorNode(Total);
							}
							else if(_exp.getParameterExps().get(0) instanceof PropertyCallExp)
							{//如果是property
								PropertyCallExp p =(PropertyCallExp)_exp.getParameterExps().get(0);
								TotalR=p.getPropertyName();
								clgliteral=new CLGVariableNode(TotalR);
								Total=_exp.getPropertyName();
								guardna = new CLGOperatorNode(Total);
							}
							else if(_exp.getParameterExps().get(0).toString().matches("[0-9]+"))
							{//如果是數字
								TotalR=_exp.getParameterExps().get(0).toString();
								clgliteral=new CLGLiteralNode(TotalR);
								Total=_exp.getPropertyName();
								guardna = new CLGOperatorNode(Total);
							}
							guardna.setLeftOperand(clgvariable);
							guardna.setRightOperand(clgliteral);//Boolean:var 不等式  數字
							if(!guardna.equals(null))
							{//看不懂
								listguard.add(guardna);//Listguard加入guardna  Listguard存放CLGoperator，存guardna
								intopreguard.add(guardna);//intopreguard存放CLGConstraint，存guardna
							}
						}//end if(regexp.equals(guard_cons))
					}
				}//end for
				for(int i=0;i<listguard.size();i++)//listguard是List
				{
					for(int i1=0;i1<guard_conslist.size();i1++)
					{
						String guard_stri1=guard_conslist.get(i1);//guard_stri1是護衛條件的名字
						guard_stri1=guard_stri1.replaceAll(" ", "");
						if(listguard.get(i).getImgInfo().equals(guard_stri1))//如果listguard中的護衛條件某一項等於某一個護衛條件
						{
							if(listguard.get(i).getOperator().equals("=")) //boolean is equal
								listguard.get(i).setOperator("==");//等於改成等於等於
							ArrayList<CLGConstraint> node_now = new ArrayList<CLGConstraint>();
							ArrayList<CLGConstraint> node_left = new ArrayList<CLGConstraint>();
							ArrayList<CLGConstraint> node_right = new ArrayList<CLGConstraint>();
							ArrayList<String> node_now_opetator = new ArrayList<String>();
							ArrayList<Integer> node_index = new ArrayList<Integer>();
							ArrayList<String> node_position = new ArrayList<String>();//這些是node的資料
							
							node_now.add(listguard.get(i));
							node_now_opetator.add(listguard.get(i).getOperator());
							node_position.add("n");
							node_index.add(-1);
							
							if(listguard.get(i).getLeftOperand().getImgInfo().toString().matches("[0-9]+"))//eg 9>1
								node_left.add(listguard.get(i).getLeftOperand());
							
							else if(listguard.get(i).getLeftOperand() instanceof CLGOperatorNode)
								node_left.add(listguard.get(i).getLeftOperand());
							
							else if(listguard.get(i).getLeftOperand() instanceof CLGVariableNode)//eg a>1
								node_left.add(listguard.get(i).getLeftOperand());
							
							if(listguard.get(i).getRightOperand().getImgInfo().toString().matches("[0-9]+"))
								node_right.add(listguard.get(i).getRightOperand());

							else if(listguard.get(i).getRightOperand() instanceof CLGOperatorNode)
								node_right.add(listguard.get(i).getRightOperand());
							
							else if(listguard.get(i).getRightOperand() instanceof CLGVariableNode)
								node_right.add(listguard.get(i).getRightOperand());
							
							for(int i28=0;i28<node_now.size();i28++)
							{
								if(node_left.get(i28)!=null)
								{
									if(node_left.get(i28).getImgInfo().toString().matches("[0-9]+"))
									{
										node_now.add(node_left.get(i28));
										node_left.add(null); 
										node_right.add(null);
										node_position.add("L");
										node_index.add(i28);
										node_now_opetator.add(null);
									}
									else if(node_left.get(i28) instanceof CLGVariableNode)
									{
										node_now.add(node_left.get(i28));
										node_left.add(null);
										node_right.add(null);
										node_position.add("L");
										node_index.add(i28);
										node_now_opetator.add(null);
									}
									else if(node_left.get(i28)  instanceof CLGOperatorNode)
									{
										node_now.add(node_left.get(i28));
										CLGOperatorNode a = (CLGOperatorNode)node_left.get(i28);//不一樣
										node_left.add(a.getLeftOperand());
										node_right.add(a.getRightOperand());
										node_position.add("L");
										node_index.add(i28);
										if(a.getOperator().equals("=")) 
											a.setOperator("==");
										node_now_opetator.add(a.getOperator());
									}
								}
								if(node_right.get(i28)!=null)
								{
									if(node_right.get(i28).getImgInfo().toString().matches("[0-9]+"))
									{
										node_now.add(node_right.get(i28));
										node_left.add(null); 
										node_right.add(null);
										node_position.add("R");
										node_index.add(i28);
										node_now_opetator.add(null);
									}
									else if(node_right.get(i28) instanceof CLGVariableNode)
									{
										node_now.add(node_right.get(i28));
										node_right.add(null);
										node_right.add(null);
										node_position.add("R");
										node_index.add(i28);
										node_now_opetator.add(null);
									}
									else if(node_right.get(i28)  instanceof CLGOperatorNode)
									{
										node_now.add(node_right.get(i28));
										CLGOperatorNode a = (CLGOperatorNode)node_right.get(i28);
										node_left.add(a.getLeftOperand());
										node_right.add(a.getRightOperand());
										node_position.add("R");
										node_index.add(i28);
										if(a.getOperator().equals("=")) 
											a.setOperator("==");
										node_now_opetator.add(a.getOperator());
									}
								}
							}
							guard=node_now.get(0);//Transition會加入此guard
						}
					}
				}
			}
			else guard=null;
			
			
			if(generalSubvertex.containsKey(eElement_transition.getAttribute("source")))
			{
				switch (generalSubvertex.get(eElement_transition.getAttribute("source")).getAttribute("xmi:type"))
				{
					case "uml:Pseudostate":
						source = initialstate;
						break;
					case "uml:FinalState":
						source = finalstate;
						break;
					case "uml:State":
						List<State> tempState =StateD.getStates();
						for(int k=0;k<tempState.size();k++)
						{
							String tempName=tempState.get(k).getName();
							if(generalSubvertex.get(eElement_transition.getAttribute("source")).getAttribute("name").equals(tempName))
							{
								source = tempState.get(k);
								break;
							}
						}
				}
			}
			if(generalSubvertex.containsKey(eElement_transition.getAttribute("target")))
			{
				switch (generalSubvertex.get(eElement_transition.getAttribute("target")).getAttribute("xmi:type"))
				{
					case "uml:Pseudostate":
						target = initialstate;
						break;
					case "uml:FinalState":
						target = finalstate;
						break;
					case "uml:State":
						List<State> tempState =StateD.getStates();
						for(int k=0;k<tempState.size();k++)
						{
							String tempName=tempState.get(k).getName();
							if(generalSubvertex.get(eElement_transition.getAttribute("target")).getAttribute("name").equals(tempName))
							{
								target = tempState.get(k);
								System.out.println("State:"+generalSubvertex.get(eElement_transition.getAttribute("target")).getAttribute("name"));
								break;
							}
						}
				}
			}
			
			
			CLGConstraint temptrigger;
			ArrayList<CLGConstraint> trigger=new ArrayList<CLGConstraint>();
			String parsetriggername, triggername = null, triggerobj;
			ArrayList<String> triggerarg=new ArrayList<String>();
			NodeList transChildList = eElement_transition.getChildNodes();
			for(int i=0;i<transChildList.getLength();i++)
			{
				if(transChildList.item(i).equals("trigger"))
				{
					parsetriggername=((Element)transChildList.item(i)).getAttribute("name");
					if(parsetriggername.isEmpty()) 
					{//check if empty
						method=null;
					}
					else
					{
						int CheckTrigger=parsetriggername.indexOf("(");//get how many '('
						int CheckTrigger1=parsetriggername.indexOf(")");//get how many ')'
						int CheckTriggerOBJ=parsetriggername.indexOf("."); //if no =-1   get how many '.'
						if(CheckTriggerOBJ==-1) 
						{//no obj
							triggerobj="";
							if(CheckTrigger!=-1) 
							{ 
								parsetriggername=parsetriggername.substring(0,CheckTrigger);
								//no many arg
								if(parsetriggername.substring(CheckTrigger+1,CheckTrigger1).indexOf(",")==-1){//no arg
									String flag = parsetriggername.substring(CheckTrigger+1,CheckTrigger1);
									
									if(!flag.equals("")) triggerarg.add(flag); 
									else triggerarg.add(" "); //no arg , give " " 
								}
								else{//to get part of parme name , add it to method arg
									String[] a = parsetriggername.substring(CheckTrigger+1,CheckTrigger1).split(",");
									for(int j=0;j<a.length;j++){
										triggerarg.add(a[j]);
									}
								}
								temptrigger = new CLGMethodInvocationNode(triggername,triggerobj);
								trigger.add(temptrigger);
							}
							else 
							{
								if(guard!=null&&guard.equals(parsetriggername))
								{//ask!!!!!!!
									temptrigger=guard;
									trigger.add(temptrigger);
								}
								else
							    {
									triggername=parsetriggername;
									temptrigger = new CLGVariableNode(triggername);
									trigger.add(temptrigger);
								}

							}
						}
						else 
						{
							triggerobj=parsetriggername.substring(0, CheckTriggerOBJ);
							if(CheckTrigger!=-1) {
								triggername=parsetriggername.substring(CheckTriggerOBJ+1,CheckTrigger);
								if(parsetriggername.substring(CheckTrigger+1,CheckTrigger1).indexOf(",")==-1){
									//methodarg.add(parmename.substring(CheckMethod+1,CheckMethod1));
									String flag = parsetriggername.substring(CheckTrigger+1,CheckTrigger1);
									if(!flag.equals("")) triggerarg.add(flag); 
									else triggerarg.add(" "); //no arg , give " " 
								}
								else{
									String[] a = parsetriggername.substring(CheckTrigger+1,CheckTrigger1).split(",");
									for(int j=0;j<a.length;j++){
										triggerarg.add(a[j]);
									}
								}
								temptrigger = new CLGMethodInvocationNode(triggerobj,triggername,triggerarg);
								trigger.add(temptrigger);
							}
							else{
								triggername=parsetriggername.substring(CheckTriggerOBJ+1);
								triggerarg.add(" ");
								temptrigger = new CLGMethodInvocationNode(triggerobj,triggername,triggerarg);
								trigger.add(temptrigger);
							}
						}
					}
				}
			}
			
			
		String parmename, methodname = null, methodobj;
		ArrayList<String> methodarg=new ArrayList<String>();
		parmename=eElement_transition.getAttribute("name");//to get parme name
		if(parmename.isEmpty()) {//check if empty
			method=null;
		}
		else{
			int CheckMethod=parmename.indexOf("(");//get how many '('
			int CheckMethod1=parmename.indexOf(")");//get how many ')'
			int CheckMethodOBJ=parmename.indexOf("."); //if no =-1   get how many '.'
			if(CheckMethodOBJ==-1) {//no obj
				methodobj="";
				if(CheckMethod!=-1) { //is method, no obj
					methodname=parmename.substring(0,CheckMethod);
					//no many arg
					if(parmename.substring(CheckMethod+1,CheckMethod1).indexOf(",")==-1){//no arg
						String flag = parmename.substring(CheckMethod+1,CheckMethod1);
						
						if(!flag.equals("")) methodarg.add(flag); 
						else methodarg.add(" "); //no arg , give " " 
					}
					else{//to get part of parme name , add it to method arg
						String[] a = parmename.substring(CheckMethod+1,CheckMethod1).split(",");
						for(int i=0;i<a.length;i++){
							methodarg.add(a[i]);
						}
					}
					method = new CLGMethodInvocationNode(methodname,methodarg);//?
				}
				else {
						if(guard!=null&&guard.equals(parmename))
						{//ask!!!!!!!
							method=guard;
						}
						else
					    {
							methodname=parmename;
							method = new CLGVariableNode(methodname);
						}

				}
			}//end no method obj
			else {
				methodobj=parmename.substring(0, CheckMethodOBJ);
				if(CheckMethod!=-1) {
					methodname=parmename.substring(CheckMethodOBJ+1,CheckMethod);
					if(parmename.substring(CheckMethod+1,CheckMethod1).indexOf(",")==-1){
						//methodarg.add(parmename.substring(CheckMethod+1,CheckMethod1));
						String flag = parmename.substring(CheckMethod+1,CheckMethod1);
						if(!flag.equals("")) methodarg.add(flag); 
						else methodarg.add(" "); //no arg , give " " 
					}
					else{
						String[] a = parmename.substring(CheckMethod+1,CheckMethod1).split(",");
						for(int i=0;i<a.length;i++){
							methodarg.add(a[i]);
						}
					}
					method = new CLGMethodInvocationNode(methodobj,methodname,methodarg);
				}
				else{
					methodname=parmename.substring(CheckMethodOBJ+1);
					methodarg.add(" ");
					method = new CLGMethodInvocationNode(methodobj,methodname,methodarg);
				}
			}
		}
	//---------------------------------------------------------------------
		if(guard instanceof CLGOperatorNode){
			CLGOperatorNode clop = (CLGOperatorNode)guard;
			if(clop.getOperator().equals("and"))
				clop.setOperator(" and ");
			if(clop.getOperator().equals("or"))
				clop.setOperator(" or ");
			
			ArrayList<CLGConstraint> node_now = new ArrayList<CLGConstraint>();
			ArrayList<CLGConstraint> node_left = new ArrayList<CLGConstraint>();
			ArrayList<CLGConstraint> node_right = new ArrayList<CLGConstraint>();
			ArrayList<String> node_now_opetator = new ArrayList<String>();
			ArrayList<Integer> node_index = new ArrayList<Integer>();
			ArrayList<String> node_position = new ArrayList<String>();
			
			node_now.add(clop);
			node_now_opetator.add(clop.getOperator());
			node_position.add("n");
			node_index.add(-1);
			//match string about left operend
			if(clop.getLeftOperand().getImgInfo().toString().matches("[0-9]+")){
				node_left.add(clop.getLeftOperand());
			}
			else if(clop.getLeftOperand() instanceof CLGOperatorNode){
				node_left.add(clop.getLeftOperand());
			}
			else if(clop.getLeftOperand() instanceof CLGVariableNode){
				node_left.add(clop.getLeftOperand());
			}
			//the same about right
			if(clop.getRightOperand().getImgInfo().toString().matches("[0-9]+")){
				node_right.add(clop.getRightOperand());
			}
			else if(clop.getRightOperand() instanceof CLGOperatorNode){
				node_right.add(clop.getRightOperand());
			}
			else if(clop.getRightOperand() instanceof CLGVariableNode){
				node_right.add(clop.getRightOperand());
			}
			//*********
			for(int i28=0;i28<node_now.size();i28++){
				if(node_left.get(i28)!=null){//about left node ???????
					if(node_left.get(i28).getImgInfo().toString().matches("[0-9]+")){
						node_now.add(node_left.get(i28));
						node_left.add(null); 
						node_right.add(null);
						node_position.add("L");
						node_index.add(i28);
						node_now_opetator.add(null);
					}
					else if(node_left.get(i28) instanceof CLGVariableNode){
						node_now.add(node_left.get(i28));
						node_left.add(null);
						node_right.add(null);
						node_position.add("L");
						node_index.add(i28);
						node_now_opetator.add(null);
					}
					else if(node_left.get(i28)  instanceof CLGOperatorNode){
						node_now.add(node_left.get(i28));
						CLGOperatorNode a = (CLGOperatorNode)node_left.get(i28);
						node_left.add(a.getLeftOperand());
						node_right.add(a.getRightOperand());
						node_position.add("L");
						node_index.add(i28);
						if(a.getOperator().equals("or")) a.setOperator(" or ");
						if(a.getOperator().equals("and")) a.setOperator(" and ");
						node_now_opetator.add(a.getOperator());
					}
				}
				if(node_right.get(i28)!=null){//about right node
					if(node_right.get(i28).getImgInfo().toString().matches("[0-9]+")){
						node_now.add(node_right.get(i28));
						node_left.add(null); 
						node_right.add(null);
						node_position.add("R");
						node_index.add(i28);
						node_now_opetator.add(null);
					}
					else if(node_right.get(i28) instanceof CLGVariableNode){
						node_now.add(node_right.get(i28));
						node_right.add(null);
						node_right.add(null);
						node_position.add("R");
						node_index.add(i28);
						node_now_opetator.add(null);
					}
					else if(node_right.get(i28)  instanceof CLGOperatorNode){
						node_now.add(node_right.get(i28));
						CLGOperatorNode a = (CLGOperatorNode)node_right.get(i28);
						node_left.add(a.getLeftOperand());
						node_right.add(a.getRightOperand());
						node_position.add("R");
						node_index.add(i28);
						if(a.getOperator().equals("or")) a.setOperator(" or ");
						if(a.getOperator().equals("and")) a.setOperator(" and ");
						node_now_opetator.add(a.getOperator());
					}
				}
			}
		}
        Transition TaranS = new Transition(temp_transition+1, method, source, target, guard); //about transition
		source.addtransition(TaranS);	
		StateD.addtransition(TaranS);			
		}
		

		SD2CLG cpsd = new SD2CLG();
		CLGGraph gtclggraph=new CLGGraph();	
		gtclggraph=cpsd.convert(StateD);
	//	System.out.println("before:"+);
		if(saveStateId.isEmpty()==false)
		{
			for(String key:saveStateId.keySet())
			{//start big graph insert subgraph
				gtclggraph.InsertCompoundStateCLG(Integer.parseInt(key),saveAllSubCLG.get(saveStateId.get(key)));
			
			}
		}
		
		if(gtclggraph.getConnectionNode(7)!=null)
			System.out.println("yes!!");
		System.out.println("Ok");
	//	CLGNode tempnode=(CLGNode)gtclggraph.getStartNode().getSuccessor().get(0);
		//System.out.println(gtclggraph.graphDraw());
	//	System.out.println(gtclggraph.getConnectionNode(3).toGenImg2());
		return gtclggraph;
				
	}
	
	
	
	
	public CLGGraph converts(File sd, File uml) throws ParserConfigurationException, SAXException, IOException, TemplateException, ModelAccessException, JSONException, org.apache.commons.cli.ParseException, ParseException, EclipseException{
		//*********Open file,make file readable***********
		Document doc ;
		System.out.println(sd.getAbsolutePath());//print absolute Path
		File fXmlFile = sd ;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(fXmlFile);	
		doc.getDocumentElement().normalize();//dbFactory = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(sd).getDocumentElement().normalize()
		
	
		Document documl;
		File fXmlFileuml = uml;
		DocumentBuilderFactory dbFactoryuml = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilderuml = dbFactoryuml.newDocumentBuilder();
		documl = dBuilderuml.parse(fXmlFileuml);					
		documl.getDocumentElement().normalize();
		
		
		//Get SD document stateMachine root WCL
		ArrayList<String> recordconnectionnode = new ArrayList<>();//To record connect node
		StateDigram StateD = null ;
		ArrayList<String> sdguard = new ArrayList<>();//sd guard :arraylist  like a>1
		FinalState finalstate = null;
		InitialState initialstate = null ;	
		ArrayList<String> attributestr = new ArrayList<String>();
		NodeList nList_ownedAttribute = documl.getElementsByTagName("ownedAttribute");
		//*********Start: trace nodelist=>to get attribute name && convert node to attributestr********
		for (int temp_ownedAttribute = 0; temp_ownedAttribute < nList_ownedAttribute.getLength(); temp_ownedAttribute++) {
			Node nNode_ownedAttribute = nList_ownedAttribute.item(temp_ownedAttribute);//same:ownedAttribute
			Element eElement_ownedAttribute = (Element) nNode_ownedAttribute;//same		
			attributestr.add(eElement_ownedAttribute.getAttribute("name"));//different
		}
		//************read sd packagename*************
		String packagename=null;
		NodeList nList_packname = doc.getElementsByTagName("packagedElement");
		for (int temp_packname = 0; temp_packname < nList_packname.getLength(); temp_packname++) {
			Node nNode_packname = nList_packname.item(temp_packname);
			Element eElement_packname = (Element) nNode_packname;
			packagename=eElement_packname.getAttribute("name");
			StateD = new StateDigram(packagename,attributestr);//different=>create state diagramm, and put package name && attributestr in stateD
		}	
		
		Node firstRegion =doc.getElementsByTagName("region").item(0);
		Element firstRegionElement =(Element)firstRegion;
		
		Model context;
		IModel model;
		ClassDiagInfo class_diag_info;
		ClassDiagToJson class_diag_to_json;
		List<ccu.pllab.tcgen.ast.Constraint>OCLCons;
		
		class_diag_to_json = new ClassDiagToJson(uml);
		class_diag_info = new ClassDiagInfo(class_diag_to_json);
		context = new Model(class_diag_info);
		TypeFactory.getInstance().setModel(context);
		ASTUtil ast_util = new ASTUtil();
		DresdenOCLASTtoInternelAST ast_constructor = new DresdenOCLASTtoInternelAST(ast_util, context);
		
		StandaloneFacade.INSTANCE.initialize(new URL("file:./log4j.properties"));
		model = StandaloneFacade.INSTANCE.loadUMLModel(uml, new File("./resources/org.eclipse.uml2.uml.resources.jar"));
		return startToConvert(firstRegionElement,attributestr,packagename,model,ast_constructor);
	}
	
	
	
	
	
	
	
	
	public StateDigram convert(File sd, File uml) throws ParserConfigurationException, SAXException, IOException, TemplateException, ModelAccessException, ParseException{
		Document doc ;
		File fXmlFile = sd ; //new File(filename)
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(fXmlFile);					
		doc.getDocumentElement().normalize();
		
		NodeList nList_subvertex = doc.getElementsByTagName("subvertex");
		NodeList nList_transition = doc.getElementsByTagName("transition");	
		ArrayList<String> recordconnectionnode = new ArrayList<>();
		StateDigram StateD = null ;
		FinalState finalstate = null;
		InitialState initialstate = null ;
		//------------SD Attribute-------------------
		ArrayList<String> attributestr = new ArrayList<String>();
		Document documl ;
		File fXmlFileuml = uml ; //new File(filename)
		DocumentBuilderFactory dbFactoryuml = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilderuml = dbFactoryuml.newDocumentBuilder();
		documl = dBuilderuml.parse(fXmlFileuml);					
		documl.getDocumentElement().normalize();
		NodeList nList_ownedAttribute = documl.getElementsByTagName("ownedAttribute");
		
		for (int temp_ownedAttribute = 0; temp_ownedAttribute < nList_ownedAttribute.getLength(); temp_ownedAttribute++) {
			Node nNode_ownedAttribute = nList_ownedAttribute.item(temp_ownedAttribute);
			Element eElement_ownedAttribute = (Element) nNode_ownedAttribute;
			
			attributestr.add(eElement_ownedAttribute.getAttribute("name"));
		}
		//-------------------------------------------
		//-------read sd packagename---
			String packagename=null;
			NodeList nList_packname = doc.getElementsByTagName("packagedElement");
			for (int temp_packname = 0; temp_packname < nList_packname.getLength(); temp_packname++) {
				Node nNode_packname = nList_packname.item(temp_packname);
				Element eElement_packname = (Element) nNode_packname;
				packagename=eElement_packname.getAttribute("name");
				StateD = new StateDigram(packagename,attributestr);
			}
		//-----------------------------
		int connectionnodecount=0;
		int stflag=0, endflag=0;
		//-----------------------state--------------------------------------------
		for (int temp_subvertex = 0; temp_subvertex < nList_subvertex.getLength(); temp_subvertex++) {
			Node nNode_subvertex = nList_subvertex.item(temp_subvertex);
			Element eElement_subvertex = (Element) nNode_subvertex;
			if(stflag==0){
				if(eElement_subvertex.getAttribute("xmi:type").equals("uml:Pseudostate")){
					recordconnectionnode.add(0, eElement_subvertex.getAttribute("xmi:id"));
					stflag=1; 
					temp_subvertex=0; 
					initialstate = new InitialState(1, "InitialState");
					StateD.addstate(initialstate);
				}
			}
			else if(endflag==0){
				if(eElement_subvertex.getAttribute("xmi:type").equals("uml:FinalState")){
					recordconnectionnode.add(1, eElement_subvertex.getAttribute("xmi:id"));
					endflag=1; 
					temp_subvertex=0;
					finalstate = new FinalState(2, "FinalState"); 
					StateD.addstate(finalstate);
				}
			}
			else if(stflag==1 && endflag==1){
				if(!eElement_subvertex.getAttribute("xmi:type").equals("uml:FinalState") &&
						!eElement_subvertex.getAttribute("xmi:type").equals("uml:Pseudostate")){
					recordconnectionnode.add(2+connectionnodecount,eElement_subvertex.getAttribute("xmi:id"));
					connectionnodecount++;
					State st = new State(2+connectionnodecount,eElement_subvertex.getAttribute("name"));
					StateD.addstate(st); 
				}
			}
		}//end for
		//------------------transition-----------------------------------------
		ArrayList<String> guard_conslist = new ArrayList<String>();
		CLGConstraint method = null, guard = null;
		State source = null, target = null;
		ArrayList<INode> ocl_guard324 = new ArrayList<INode>();
		
		for (int temp_transition = 0; temp_transition < nList_transition.getLength(); temp_transition++) {
			Node nNode_transition = nList_transition.item(temp_transition);
			Element eElement_transition = (Element) nNode_transition;
			guard_conslist = new ArrayList<String>();
			
			//----------------------------guard------------------------------------
			if(!eElement_transition.getAttribute("guard").isEmpty()){
				NodeList nList_guard = doc.getElementsByTagName("ownedRule");	
				for (int temp_guard = 0; temp_guard < nList_guard.getLength(); temp_guard++) {
					Node nNode_guard = nList_guard.item(temp_guard);
					Element eElement_guard = (Element) nNode_guard;
					
					if(eElement_transition.getAttribute("guard").equals(eElement_guard.getAttribute("xmi:id"))){
						guard_conslist.add(eElement_guard.getAttribute("name"));
						//---------parse guard -------
						String inocl, endocl, guardocl = null;
						inocl="package tcgen\n\tcontext "+packagename+"\n\tinv:\n\t\t";
						endocl="\nendpackage";
						guardocl=eElement_guard.getAttribute("name");
						File dir1 = new File("../Examples/"+packagename+"guard"); 
						dir1.mkdir();   
						FileWriter dataFile = new FileWriter("../Examples/"+packagename+"guard/"+packagename+temp_transition+".ocl");
						BufferedWriter input = new BufferedWriter(dataFile);
						input.write(inocl+guardocl+endocl);
						input.close();
						
						Model context;
						IModel model;
						ClassDiagInfo class_diag_info;
						ClassDiagToJson class_diag_to_json;
						List<ccu.pllab.tcgen.ast.Constraint>OCLCons;
						
						class_diag_to_json = new ClassDiagToJson(uml);
						class_diag_info = new ClassDiagInfo(class_diag_to_json);
						context = new Model(class_diag_info);
						TypeFactory.getInstance().setModel(context);
						ASTUtil ast_util = new ASTUtil();
						DresdenOCLASTtoInternelAST ast_constructor = new DresdenOCLASTtoInternelAST(ast_util, context);
						
						StandaloneFacade.INSTANCE.initialize(new URL("file:./log4j.properties"));
						model = StandaloneFacade.INSTANCE.loadUMLModel(uml, new File("./resources/org.eclipse.uml2.uml.resources.jar"));
						List<tudresden.ocl20.pivot.pivotmodel.Constraint> loaduml= StandaloneFacade.
								INSTANCE.parseOclConstraints(model, new File("../Examples/"+packagename+"guard/"+packagename+temp_transition+".ocl"));
//						List<tudresden.ocl20.pivot.pivotmodel.Constraint> loaduml= StandaloneFacade.
//								INSTANCE.parseOclConstraints(model, new File
//								(DataWriter.output_folder_path+"guardOCL/"+packagename+temp_transition+".ocl"));
						OCLCons = ast_constructor.parseOclTreeNodeFromPivotModel(loaduml);
//						List<CLGConstraint> testguard=new ArrayList<CLGConstraint>();
						for(int i=0;i<OCLCons.size();i++){
							ASTNode testast = OCLCons.get(i).toPreProcessing();
							List<INode> testast_1 = null;
							int savesize=0;
							testast_1 = testast.getNextNodes();
							savesize=testast_1.size();
							for(int j=0;j<testast_1.size();j++){
								ocl_guard324.add(testast_1.get(j));
								if(j==savesize-1){
									if(testast_1.get(0).getNextNodes().size()!=0){
										testast_1=testast_1.get(0).getNextNodes();
										savesize=testast_1.size();
										j=-1;
									}
								}
							}
						}
						//-------
					}
				}
				//---20170216-----
				List<CLGOperatorNode> listguard=new ArrayList<CLGOperatorNode>();
				for(int abc=0;abc<ocl_guard324.size();abc++){
					String a = ocl_guard324.get(abc).toString();
					String regexp = a.replaceAll("[\\(\\)]", "");
					regexp=regexp.replaceAll(" ","");
					regexp=regexp.replaceAll("self.","");
					//System.out.println("test~ "+regexp);
					for(int i=0;i<guard_conslist.size();i++){
						String gustr = guard_conslist.get(i);
						gustr=gustr.replaceAll(" ","");
						//System.out.println("test1~ "+gustr);
					if(regexp.equals(gustr)){
						OperationCallExp _exp = (OperationCallExp) ocl_guard324.get(abc);
						//-----
						final List<CLGConstraint> intopreguard = new ArrayList<CLGConstraint>();
						
						CLGOperatorNode guardna=null;
						ccu.pllab.tcgen.AbstractConstraint.CLGConstraint clgliteral=null, clgvariable=null;
						String TotalL=null, TotalR=null, Total=null;
						OperationCallExp Ori_exp= (OperationCallExp)_exp;
						if(Ori_exp.getSourceExp() instanceof OperationCallExp){
							Ori_exp=(OperationCallExp)Ori_exp.getSourceExp();
							clgvariable=parseSD(Ori_exp);
							Total=_exp.getPropertyName();
							guardna = new CLGOperatorNode(Total);
							//System.out.println(" part11 "+Total);
						}
						else if(Ori_exp.getSourceExp() instanceof PropertyCallExp){
							PropertyCallExp p =(PropertyCallExp)Ori_exp.getSourceExp();
							TotalL=p.getPropertyName();
							clgvariable=new CLGVariableNode(TotalL);
							Total=_exp.getPropertyName();
							guardna = new CLGOperatorNode(Total);
							//System.out.println(" part12 "+Total);
						}
						else if(Ori_exp.getSourceExp().toString().matches("[0-9]+")){
							TotalL=Ori_exp.getSourceExp().toString();
							clgvariable=new CLGLiteralNode(TotalL);
							Total=_exp.getPropertyName(); 
							guardna = new CLGOperatorNode(Total);
							//System.out.println(" part13 "+Total);
						}
						//----
						if(_exp.getParameterExps().get(0) instanceof OperationCallExp){
							Ori_exp=(OperationCallExp)_exp.getParameterExps().get(0);
							clgliteral=parseSD(Ori_exp);
							Total=_exp.getPropertyName();
							guardna = new CLGOperatorNode(Total);
							//System.out.println(" part111 "+Total);
						}
						else if(_exp.getParameterExps().get(0) instanceof PropertyCallExp){
							PropertyCallExp p =(PropertyCallExp)_exp.getParameterExps().get(0);
							TotalR=p.getPropertyName();
							clgliteral=new CLGVariableNode(TotalR);
							Total=_exp.getPropertyName();
							guardna = new CLGOperatorNode(Total);
							//System.out.println(" part112 "+Total);
						}
						else if(_exp.getParameterExps().get(0).toString().matches("[0-9]+")){
							TotalR=_exp.getParameterExps().get(0).toString();
							clgliteral=new CLGLiteralNode(TotalR);
							Total=_exp.getPropertyName();
							guardna = new CLGOperatorNode(Total);
							//System.out.println(" part113 "+Total);
						}
						guardna.setLeftOperand(clgvariable);
						guardna.setRightOperand(clgliteral);
						//System.out.println(" part3 "+guardna.getImgInfo());
						if(!guardna.equals(null)){
							listguard.add(guardna);
							intopreguard.add(guardna);
						}
					}//end if(regexp.equals(guard_cons))
				}
				}//end for
				//System.out.println(" 324324. "+listguard+	" 324324. "+guard_conslist);
				for(int i=0;i<listguard.size();i++){
					for(int i1=0;i1<guard_conslist.size();i1++){
						String guard_stri1=guard_conslist.get(i1);
						guard_stri1=guard_stri1.replaceAll(" ", "");
						if(listguard.get(i).getImgInfo().equals(guard_stri1)){
							//System.out.println("  check op~ 328 "+listguard.get(i).getOperator());
							if(listguard.get(i).getOperator().equals("=")) listguard.get(i).setOperator("==");
							//----328
							ArrayList<CLGConstraint> node_now = new ArrayList<CLGConstraint>();
							ArrayList<CLGConstraint> node_left = new ArrayList<CLGConstraint>();
							ArrayList<CLGConstraint> node_right = new ArrayList<CLGConstraint>();
							ArrayList<String> node_now_opetator = new ArrayList<String>();
							ArrayList<Integer> node_index = new ArrayList<Integer>();
							ArrayList<String> node_position = new ArrayList<String>();
							
							node_now.add(listguard.get(i));
							node_now_opetator.add(listguard.get(i).getOperator());
							node_position.add("n");
							node_index.add(-1);
							
							if(listguard.get(i).getLeftOperand().getImgInfo().toString().matches("[0-9]+")){
								node_left.add(listguard.get(i).getLeftOperand());
							}
							else if(listguard.get(i).getLeftOperand() instanceof CLGOperatorNode){
								node_left.add(listguard.get(i).getLeftOperand());
							}
							else if(listguard.get(i).getLeftOperand() instanceof CLGVariableNode){
								node_left.add(listguard.get(i).getLeftOperand());
							}
							
							if(listguard.get(i).getRightOperand().getImgInfo().toString().matches("[0-9]+")){
								node_right.add(listguard.get(i).getRightOperand());
							}
							else if(listguard.get(i).getRightOperand() instanceof CLGOperatorNode){
								node_right.add(listguard.get(i).getRightOperand());
							}
							else if(listguard.get(i).getRightOperand() instanceof CLGVariableNode){
								node_right.add(listguard.get(i).getRightOperand());
							}
							
							for(int i28=0;i28<node_now.size();i28++){
								if(node_left.get(i28)!=null){
									if(node_left.get(i28).getImgInfo().toString().matches("[0-9]+")){
										node_now.add(node_left.get(i28));
										node_left.add(null); 
										node_right.add(null);
										node_position.add("L");
										node_index.add(i28);
										node_now_opetator.add(null);
									}
									else if(node_left.get(i28) instanceof CLGVariableNode){
										node_now.add(node_left.get(i28));
										node_left.add(null);
										node_right.add(null);
										node_position.add("L");
										node_index.add(i28);
										node_now_opetator.add(null);
									}
									else if(node_left.get(i28)  instanceof CLGOperatorNode){
										node_now.add(node_left.get(i28));
										CLGOperatorNode a = (CLGOperatorNode)node_left.get(i28);
										node_left.add(a.getLeftOperand());
										node_right.add(a.getRightOperand());
										node_position.add("L");
										node_index.add(i28);
										if(a.getOperator().equals("=")) a.setOperator("==");
										node_now_opetator.add(a.getOperator());
									}
								}
								if(node_right.get(i28)!=null){
									if(node_right.get(i28).getImgInfo().toString().matches("[0-9]+")){
										node_now.add(node_right.get(i28));
										node_left.add(null); 
										node_right.add(null);
										node_position.add("R");
										node_index.add(i28);
										node_now_opetator.add(null);
									}
									else if(node_right.get(i28) instanceof CLGVariableNode){
										node_now.add(node_right.get(i28));
										node_right.add(null);
										node_right.add(null);
										node_position.add("R");
										node_index.add(i28);
										node_now_opetator.add(null);
									}
									else if(node_right.get(i28)  instanceof CLGOperatorNode){
										node_now.add(node_right.get(i28));
										CLGOperatorNode a = (CLGOperatorNode)node_right.get(i28);
										node_left.add(a.getLeftOperand());
										node_right.add(a.getRightOperand());
										node_position.add("R");
										node_index.add(i28);
										if(a.getOperator().equals("=")) a.setOperator("==");
										node_now_opetator.add(a.getOperator());
									}
								}
							}//end for i28
							//guard=listguard.get(i);
							guard=node_now.get(0);
						}
					}
				}//end for
				//----------------
			}//end has guard
			else guard=null;
			//--------------------------------------------------------------------
			
			//------------find source and target-------------------------
			for(int find=0; find<recordconnectionnode.size(); find++){
				if(recordconnectionnode.get(find).equals(eElement_transition.getAttribute("source"))){
					if(find==0)
						source = initialstate;
					else if(find==1)
						source = finalstate;
					else
						source=StateD.getStates().get(find);
				}
				if(recordconnectionnode.get(find).equals(eElement_transition.getAttribute("target"))){
					if(find==0)
						target = initialstate;
					else if(find==1)
						target = finalstate;
					else
						target=StateD.getStates().get(find);
				}
			}
			//-----------------------method-------------------------------------
			String parmename, methodname = null, methodobj;
			ArrayList<String> methodarg=new ArrayList<String>();
			parmename=eElement_transition.getAttribute("name");
			if(parmename.isEmpty()) {
				method=null;
			}
			else{
				int CheckMethod=parmename.indexOf("(");
				int CheckMethod1=parmename.indexOf(")");
				int CheckMethodOBJ=parmename.indexOf("."); //if no =-1
				if(CheckMethodOBJ==-1) {
					methodobj="";
					if(CheckMethod!=-1) { //is method, no obj
						methodname=parmename.substring(0,CheckMethod);
						//no many arg
						if(parmename.substring(CheckMethod+1,CheckMethod1).indexOf(",")==-1){
							//methodarg.add(parmename.substring(CheckMethod+1,CheckMethod1));
							String flag = parmename.substring(CheckMethod+1,CheckMethod1);
							
							if(!flag.equals("")) methodarg.add(flag); 
							else methodarg.add(" "); //no arg , give " " 
						}
						else{
							String[] a = parmename.substring(CheckMethod+1,CheckMethod1).split(",");
							for(int i=0;i<a.length;i++){
								methodarg.add(a[i]);
							}
						}
						method = new CLGMethodInvocationNode(methodname,methodarg);
					}
					else {
						//for(int i=0;i<guard;i++){
							if(guard.equals(parmename)){
								//System.out.println("!!! yes get method guard~ "+testguard.get(i).getImgInfo());
								method=guard;
							}
							else{
								methodname=parmename;
								method = new CLGVariableNode(methodname);
							}
						//}//end for
					}
				}//end no method obj
				else {
					methodobj=parmename.substring(0, CheckMethodOBJ);
					if(CheckMethod!=-1) {
						methodname=parmename.substring(CheckMethodOBJ+1,CheckMethod);
						if(parmename.substring(CheckMethod+1,CheckMethod1).indexOf(",")==-1){
							//methodarg.add(parmename.substring(CheckMethod+1,CheckMethod1));
							String flag = parmename.substring(CheckMethod+1,CheckMethod1);
							if(!flag.equals("")) methodarg.add(flag); 
							else methodarg.add(" "); //no arg , give " " 
						}
						else{
							String[] a = parmename.substring(CheckMethod+1,CheckMethod1).split(",");
							for(int i=0;i<a.length;i++){
								methodarg.add(a[i]);
							}
						}
						method = new CLGMethodInvocationNode(methodobj,methodname,methodarg);
					}
					else{
						methodname=parmename.substring(CheckMethodOBJ+1);
						methodarg.add(" ");
						method = new CLGMethodInvocationNode(methodobj,methodname,methodarg);
					}
				}
			}
			Transition TaranS = new Transition(temp_transition+1, method, source, target, guard);
			source.addtransition(TaranS);
			StateD.addtransition(TaranS);
		}//end for transition 
		return StateD;
	}//end public void convert()
	
	private CLGConstraint parseSD(OperationCallExp as){
		String LC=null,MC=null,RC=null, Total=null;
		CLGOperatorNode guardna=null;
		ccu.pllab.tcgen.AbstractConstraint.CLGConstraint CH_clgvariable=null,CH_clgliteral=null;
			
		if(as.getSourceExp() instanceof OperationCallExp){
			CH_clgvariable=parseSD((OperationCallExp) as.getSourceExp());
			MC=as.getPropertyName(); 
			guardna=new CLGOperatorNode(MC);
			if(as.getParameterExps().get(0) instanceof OperationCallExp){
				CH_clgliteral=parseSD((OperationCallExp) as.getParameterExps().get(0));
				Total=LC+MC+RC;
				guardna.setLeftOperand(CH_clgvariable);
				guardna.setRightOperand(CH_clgliteral);
			}
			else if(as.getParameterExps().get(0) instanceof PropertyCallExp){
				PropertyCallExp p = (PropertyCallExp)as.getParameterExps().get(0);
				RC=p.getPropertyName(); 
				CH_clgliteral=new CLGVariableNode(RC);
				Total=LC+MC+RC;
				guardna.setLeftOperand(CH_clgvariable);
				guardna.setRightOperand(CH_clgliteral);
			}
			else if(as.getParameterExps().get(0).toString().matches("[0-9]+")){
				RC=as.getParameterExps().get(0).toString();
				CH_clgliteral=new CLGLiteralNode(RC);
				Total=LC+MC+RC;
				guardna.setLeftOperand(CH_clgvariable);
				guardna.setRightOperand(CH_clgliteral);
			}
		}
		else if(as.getSourceExp() instanceof PropertyCallExp){
			PropertyCallExp p = (PropertyCallExp) as.getSourceExp();
			LC=p.getPropertyName();
			CH_clgvariable=new CLGVariableNode(LC);
			MC=as.getPropertyName(); 
			guardna=new CLGOperatorNode(MC);
			if(as.getParameterExps().get(0) instanceof PropertyCallExp){
				PropertyCallExp p11 =(PropertyCallExp)as.getParameterExps().get(0);
				RC=p11.getPropertyName();
				CH_clgliteral=new CLGVariableNode(RC);
				Total=LC+MC+RC;
				guardna.setLeftOperand(CH_clgvariable);
				guardna.setRightOperand(CH_clgliteral);
				LC=MC=RC="";
			}else if(as.getParameterExps().get(0).toString().matches("[0-9]+")){
				RC=as.getParameterExps().get(0).toString();
				CH_clgliteral=new CLGLiteralNode(RC);
				Total=LC+MC+RC;
				guardna.setLeftOperand(CH_clgvariable);
				guardna.setRightOperand(CH_clgliteral);
				LC=MC=RC="";
			}
		}
		else if(as.getSourceExp().toString().matches("[0-9]+")){
			LC=as.getSourceExp().toString();
			CH_clgvariable=new CLGLiteralNode(LC);
			MC=as.getPropertyName(); //System.out.println("  check op "+MC);
			guardna=new CLGOperatorNode(MC);
			if(as.getParameterExps().get(0) instanceof PropertyCallExp){
				PropertyCallExp p11 =(PropertyCallExp)as.getParameterExps().get(0);
				RC=p11.getPropertyName();
				CH_clgliteral=new CLGVariableNode(RC);
				Total=LC+MC+RC;
				guardna.setLeftOperand(CH_clgvariable);
				guardna.setRightOperand(CH_clgliteral);
				LC=MC=RC="";
			}else if(as.getParameterExps().get(0).toString().matches("[0-9]+")){
				RC=as.getParameterExps().get(0).toString();
					CH_clgliteral=new CLGLiteralNode(RC);
					Total=LC+MC+RC;
					guardna.setLeftOperand(CH_clgvariable);
					guardna.setRightOperand(CH_clgliteral);
					LC=MC=RC="";
				}
		}
		return guardna;
	}
	
	
	
	public static List<ccu.pllab.tcgen.ast.Constraint> parseOCL(File ocl, File uml) throws TemplateException, ModelAccessException, IOException, ParseException{
		Model context;
		IModel model;
		ClassDiagInfo class_diag_info;
		ClassDiagToJson class_diag_to_json;
		List<ccu.pllab.tcgen.ast.Constraint>OCLCons;
		//File uml = new File("../Examples/test20170304/coffeemachine.uml");
		class_diag_to_json = new ClassDiagToJson(uml);
		class_diag_info = new ClassDiagInfo(class_diag_to_json);
		context = new Model(class_diag_info);
		TypeFactory.getInstance().setModel(context);
		ASTUtil ast_util = new ASTUtil();
		DresdenOCLASTtoInternelAST ast_constructor = new DresdenOCLASTtoInternelAST(ast_util, context);
		
		StandaloneFacade.INSTANCE.initialize(new URL("file:./log4j.properties"));
		model = StandaloneFacade.INSTANCE.loadUMLModel(uml, new File("./resources/org.eclipse.uml2.uml.resources.jar"));
		//List<tudresden.ocl20.pivot.pivotmodel.Constraint> loaduml= StandaloneFacade.  //test20170304/date/date.ocl"
		//		INSTANCE.parseOclConstraints(model, new File("../Examples/test20170304/coffeemachine.ocl"));
		List<tudresden.ocl20.pivot.pivotmodel.Constraint> loaduml= StandaloneFacade.  //test20170304/date/date.ocl"
				INSTANCE.parseOclConstraints(model, ocl);
		OCLCons = ast_constructor.parseOclTreeNodeFromPivotModel(loaduml); 
		
		return OCLCons;
	}
}
