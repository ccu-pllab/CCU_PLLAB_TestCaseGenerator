package ccu.pllab.tcgen.transform;


import ccu.pllab.tcgen.AbstractSyntaxTree.*;
import ccu.pllab.tcgen.clg.StartNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.exe.main.Main;
import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGIfNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AST2CLG {
	private ArrayList<CLGGraph> clg_list = new ArrayList<CLGGraph>();
	private CLGGraph invCLG;
	private String criterion;
	private String clgFolder;
	
	// private String
	public AST2CLG(AbstractSyntaxTreeNode ast, String criterion, String folderPath, boolean boundaryAnalysis) throws IOException {
		this.criterion = criterion;
		clgFolder = folderPath;
		Main.boundary_analysis=boundaryAnalysis;
		ArrayList<AbstractSyntaxTreeNode> astNode = ((PackageExp) ast).getTreeNode();
		CLGCriterionTransformer clgTF = new CLGCriterionTransformer();

		for (int clg_number = 0; clg_number < astNode.size(); clg_number++) {
			if (astNode.get(clg_number) instanceof OperationContext) {// 如果是method，分開pre和post的CLG來做
				for (int stereo = 0; stereo < ((OperationContext) astNode.get(clg_number)).getStereoType().size(); stereo++) {
					CLGGraph clg = new CLGGraph(((OperationContext) astNode.get(clg_number)).getStereoType().get(stereo).getTreeNode().AST2CLG());// 直接從pre或post內文下手
					String classname = ((OperationContext) astNode.get(clg_number)).getClassName();
					String methodname = ((OperationContext) astNode.get(clg_number)).getMethodName();
					String returntype = ((OperationContext) astNode.get(clg_number)).getReturnType();
					// *******設定CLG的起始點的值
					((CLGStartNode) (clg.getStartNode())).setClassName(classname);
					((CLGStartNode) (clg.getStartNode())).setMethodName(methodname);
					ArrayList<String> argument = new ArrayList<String>();
					ArrayList<String> argumenttype = new ArrayList<String>();
					ArrayList<PropertyCallExp> parameters = ((OperationContext) astNode.get(clg_number)).getParameters();
					for (int number = 0; number < parameters.size(); number++) {
						argument.add(parameters.get(number).getVariable());
						argumenttype.add(parameters.get(number).getType());
					}
					((CLGStartNode) (clg.getStartNode())).setMethodParameters(argument);
					((CLGStartNode) (clg.getStartNode())).setMethodParameterTypes(argumenttype);
					CLGStartNode startNode = (CLGStartNode) (clg.getStartNode());
					if (returntype.equals(""))// &&
												// ((CLGStartNode)(clg.getStartNode())).getClassName().equals(((CLGStartNode)(clg.getStartNode())).getMethodName()))
						((CLGStartNode) (clg.getStartNode())).setIsConstructor(true);
					else // if(returntype!="")
					{
						((CLGStartNode) (clg.getStartNode())).setRetType(returntype);
					}

					// ******
					clg_list.add(clg);// 處理好一個pre或post
				}

				if (((OperationContext) astNode.get(clg_number)).getStereoType().size() == 2) {// 如果method有pre和post做結合，以CLGIf做結合
					String[] clgindex;
					CLGGraph conditionCLG = this.getCLGGraph().get(clg_list.size() - 2);
					clgindex = conditionCLG.getConstraintCollection().keySet().toString().substring(1, conditionCLG.getConstraintCollection().keySet().toString().length() - 1).split(", ");
					int a = Integer.parseInt(clgindex[0]);
					CLGConstraint condition = conditionCLG.getConstraintNodeById(a).getConstraint();
					condition.preconditionAddPre();// 因為OCL有些要是pre但沒標註

					CLGGraph thenCLG = this.getCLGGraph().get(clg_list.size() - 1);
					clgindex = thenCLG.getConstraintCollection().keySet().toString().substring(1, thenCLG.getConstraintCollection().keySet().toString().length() - 1).split(", ");
					a = Integer.parseInt(clgindex[0]);
					CLGConstraint then = thenCLG.getConstraintNodeById(a).getConstraint();
					then.postconditionAddPre();
					// condition->then是原本沒有連接的狀況，else是要表達exception
					CLGOperatorNode elseExp = new CLGOperatorNode("=");
					elseExp.setType("boolean");
					CLGVariableNode result = new CLGVariableNode("result", "boolean");
					CLGLiteralNode liter = new CLGLiteralNode("\"Exception\"", "boolean");
					elseExp.setLeftOperand(result);
					elseExp.setRightOperand(liter);
					CLGGraph clg = new CLGGraph(new CLGIfNode(condition, then, elseExp));// 比較好做圖的連接pre和post
					// *******做dc或dcc或mcc的轉換
					if (this.criterion.equals("dcc") || this.criterion.equals("dccdup"))
						clg = clgTF.CriterionTransformer(clg, "dcc");
					else if (this.criterion.equals("mcc") || this.criterion.equals("mccdup"))
						clg = clgTF.CriterionTransformer(clg, "mcc");
					else
						clg = clgTF.CriterionTransformer(clg, "dc");

					this.genCLGGraph(clg, clg_number, ((OperationContext) astNode.get(clg_number)).getMethodName());// 建圖

					// ~以下至121都是為了張振鴻學長CLG部分做基本設定
					String classname = ((OperationContext) astNode.get(clg_number)).getClassName();
					String methodname = ((OperationContext) astNode.get(clg_number)).getMethodName();
					String returntype = ((OperationContext) astNode.get(clg_number)).getReturnType();
					((CLGStartNode) (clg.getStartNode())).setClassName(classname);
					((CLGStartNode) (clg.getStartNode())).setMethodName(methodname);
					ArrayList<String> argument = new ArrayList<String>();
					ArrayList<String> argumenttype = new ArrayList<String>();
					ArrayList<PropertyCallExp> parameters = ((OperationContext) astNode.get(clg_number)).getParameters();
					for (int number = 0; number < parameters.size(); number++) {
						argument.add(parameters.get(number).getVariable());
						argumenttype.add(parameters.get(number).getType());
					}
					((CLGStartNode) (clg.getStartNode())).setMethodParameters(argument);
					((CLGStartNode) (clg.getStartNode())).setMethodParameterTypes(argumenttype);
					if (returntype.equals("")) {
						((CLGStartNode) (clg.getStartNode())).setRetType("null");
						((CLGStartNode) (clg.getStartNode())).setIsConstructor(true);
					} else {
						((CLGStartNode) (clg.getStartNode())).setRetType(returntype);
					}

					clg_list.remove(clg_list.size() - 1);// 刪post clg
					clg_list.remove(clg_list.size() - 1);// 刪pre clg
					clg_list.add(clg);// 加入pre+post clg
				} else {// 只有post而已
					CLGGraph clg = this.getCLGGraph().get(clg_list.size() - 1);
					String[] clgindex = clg.getConstraintCollection().keySet().toString().substring(1, clg.getConstraintCollection().keySet().toString().length() - 1).split(", ");
					int a = Integer.parseInt(clgindex[0]);
					CLGConstraint post = clg.getConstraintNodeById(a).getConstraint();
					post.postconditionAddPre();
					clg = clgTF.CriterionTransformer(clg, this.criterion);
					clg.getStartNode().setVisitFalse();
					this.genCLGGraph(clg, clg_number, ((OperationContext) astNode.get(clg_number)).getMethodName());
					clg_list.remove(clg_list.size() - 1);
					clg_list.add(clg);
				}
			} else {// 處理inv的部分
				this.invCLG = new CLGGraph(((ClassifierContext) astNode.get(clg_number)).getInv().getTreeNode().AST2CLG());
				String classname = ((ClassifierContext) astNode.get(clg_number)).getClassName();
				((CLGStartNode) (this.invCLG.getStartNode())).setClassName(classname);
				((CLGStartNode) (this.invCLG.getStartNode())).setMethodName(classname);
				((CLGStartNode) (this.invCLG.getStartNode())).setIsConstructor(false);
				this.invCLG = clgTF.CriterionTransformer(this.invCLG, this.criterion);
			}
		}
	}

	public AST2CLG(AbstractSyntaxTreeNode ast) throws IOException {
		ArrayList<AbstractSyntaxTreeNode> astNode = ((PackageExp) ast).getTreeNode();
		CLGCriterionTransformer clgTF = new CLGCriterionTransformer();

		for (int clg_number = 0; clg_number < astNode.size(); clg_number++) {
			if (astNode.get(clg_number) instanceof OperationContext) {// 如果是method，分開pre和post的CLG來做
				int pre_number=((OperationContext) astNode.get(clg_number)).getPreNum();
				int post_number=((OperationContext) astNode.get(clg_number)).getPostNum();
				for (int stereo = 0; stereo < ((OperationContext) astNode.get(clg_number)).getStereoType().size(); stereo++) {
					CLGConstraint temp=((OperationContext) astNode.get(clg_number)).getStereoType().get(stereo).getTreeNode().AST2CLG();
					if(temp==null)
					{
						Main.msort=true; 
						pre_number--;
						continue;
					}
					CLGGraph clg = new CLGGraph(temp);// 直接從pre或post內文下手
					String classname = ((OperationContext) astNode.get(clg_number)).getClassName();
					String methodname = ((OperationContext) astNode.get(clg_number)).getMethodName();
					String returntype = ((OperationContext) astNode.get(clg_number)).getReturnType();
					// *******設定CLG的起始點的值
					((CLGStartNode) (clg.getStartNode())).setClassName(classname);
					((CLGStartNode) (clg.getStartNode())).setMethodName(methodname);
					ArrayList<String> argument = new ArrayList<String>();
					ArrayList<String> argumenttype = new ArrayList<String>();
					ArrayList<PropertyCallExp> parameters = ((OperationContext) astNode.get(clg_number)).getParameters();
					for (int number = 0; number < parameters.size(); number++) {
						argument.add(parameters.get(number).getVariable());
						argumenttype.add(parameters.get(number).getType());
					}
					((CLGStartNode) (clg.getStartNode())).setMethodParameters(argument);
					((CLGStartNode) (clg.getStartNode())).setMethodParameterTypes(argumenttype);
					CLGStartNode startNode = (CLGStartNode) (clg.getStartNode());
					if (returntype.equals(""))
						((CLGStartNode) (clg.getStartNode())).setIsConstructor(true);
					else 
					{
						((CLGStartNode) (clg.getStartNode())).setRetType(returntype);
					}

					// ******
					clg_list.add(clg);// 處理好一個pre或post
				}
					
				if (pre_number==1&&post_number==1) {
					String[] clgindex;
					CLGGraph conditionCLG = this.getCLGGraph().get(clg_list.size() - 2);
					clgindex = conditionCLG.getConstraintCollection().keySet().toString().substring(1, conditionCLG.getConstraintCollection().keySet().toString().length() - 1).split(", ");
					int a = Integer.parseInt(clgindex[0]);
					CLGConstraint condition = conditionCLG.getConstraintNodeById(a).getConstraint();
					condition.preconditionAddPre();// 因為OCL有些要是pre但沒標註

					CLGGraph thenCLG = this.getCLGGraph().get(clg_list.size() - 1);
					clgindex = thenCLG.getConstraintCollection().keySet().toString().substring(1, thenCLG.getConstraintCollection().keySet().toString().length() - 1).split(", ");
					a = Integer.parseInt(clgindex[0]);
					CLGConstraint then = thenCLG.getConstraintNodeById(a).getConstraint();
					then.postconditionAddPre();
					// condition->then是原本沒有連接的狀況，else是要表達exception
					CLGOperatorNode elseExp = new CLGOperatorNode("=");
					elseExp.setType("boolean");
					CLGVariableNode result = new CLGVariableNode("exception", "boolean");
					CLGLiteralNode liter = new CLGLiteralNode("\"Exception\"", "boolean");
					elseExp.setLeftOperand(result);
					elseExp.setRightOperand(liter);
					CLGGraph clg = new CLGGraph(new CLGIfNode(condition, then, elseExp));// 比較好做圖的連接pre和post
					// *******做dc或dcc或mcc的轉換
					if (Main.criterion.equals(CriterionFactory.Criterion.dcc) || Main.criterion.equals(CriterionFactory.Criterion.dccdup))
						clg = clgTF.CriterionTransformer(clg, CriterionFactory.Criterion.dcc);
					else if (Main.criterion.equals(CriterionFactory.Criterion.mcc) || Main.criterion.equals(CriterionFactory.Criterion.mccdup))
						clg = clgTF.CriterionTransformer(clg, CriterionFactory.Criterion.mcc);
					else
						clg = clgTF.CriterionTransformer(clg, CriterionFactory.Criterion.dc);

					this.genCLGGraph(clg, clg_number);// 建圖

					// ~以下至121都是為了張振鴻學長CLG部分做基本設定
					String classname = ((OperationContext) astNode.get(clg_number)).getClassName();
					String methodname = ((OperationContext) astNode.get(clg_number)).getMethodName();
					String returntype = ((OperationContext) astNode.get(clg_number)).getReturnType();
					((CLGStartNode) (clg.getStartNode())).setClassName(classname);
					((CLGStartNode) (clg.getStartNode())).setMethodName(methodname);
					ArrayList<String> argument = new ArrayList<String>();
					ArrayList<String> argumenttype = new ArrayList<String>();
					ArrayList<PropertyCallExp> parameters = ((OperationContext) astNode.get(clg_number)).getParameters();
					for (int number = 0; number < parameters.size(); number++) {
						argument.add(parameters.get(number).getVariable());
						argumenttype.add(parameters.get(number).getType());
					}
					((CLGStartNode) (clg.getStartNode())).setMethodParameters(argument);
					((CLGStartNode) (clg.getStartNode())).setMethodParameterTypes(argumenttype);
					
					//設定attributes
					ArrayList<String> attribute = new ArrayList<String>();
					ArrayList<VariableToken> attributes = Main.symbolTable.getAttribute();
					for (int number = 0; number < attributes.size(); number++) {
						attribute.add(attributes.get(number).getVariableName());
					}
					((CLGStartNode) (clg.getStartNode())).setClassAttributes(attribute);
					
					if (returntype.equals("")) {
						((CLGStartNode) (clg.getStartNode())).setRetType("null");
						((CLGStartNode) (clg.getStartNode())).setIsConstructor(true);
					} else {
						((CLGStartNode) (clg.getStartNode())).setRetType(returntype);
					}

					clg_list.remove(clg_list.size() - 1);// 刪post clg
					clg_list.remove(clg_list.size() - 1);// 刪pre clg
					clg_list.add(clg);// 加入pre+post clg
					
					/*start  ocl2clp*/
					//String methodCLP = ((CLGStartNode) clg.getStartNode()).OCL2CLP();
					//System.out.println(methodCLP);
					/*end ocl2clp*/
					
				} else if(post_number>=2)
				{
					//System.out.println("clgsize:"+clg_list.size());
				
					CLGGraph[] post=new CLGGraph[post_number];
					for(int start=clg_list.size()-post_number-pre_number,i=0;i<pre_number+post_number;start++,i++)
					{
						CLGGraph clg = this.clg_list.get(start);// 比較好做圖的連接pre和post
						// *******做dc或dcc或mcc的轉換
						if (Main.criterion.equals(CriterionFactory.Criterion.dcc) || Main.criterion.equals(CriterionFactory.Criterion.dccdup))
							clg = clgTF.CriterionTransformer(clg, CriterionFactory.Criterion.dcc);
						else if (Main.criterion.equals(CriterionFactory.Criterion.mcc) || Main.criterion.equals(CriterionFactory.Criterion.mccdup))
							clg = clgTF.CriterionTransformer(clg, CriterionFactory.Criterion.mcc);
						else
							clg = clgTF.CriterionTransformer(clg, CriterionFactory.Criterion.dc);

						post[i]=clg;
					}
					for(int start=0;start<post_number-1;start++)
					{
						CLGGraph a=post[start];
						CLGGraph b=post[start+1];
						a.graphOr(b);
						post[start+1]=a;
					}
					
					for(int i=0;i<post_number;i++)
						this.clg_list.remove(this.clg_list.size()-1);
					//if(((CLGStartNode) post[post_number-1].getStartNode()).getClassName().equals("SortedArray"))
					//	post[post_number-1].graphAnd(this.invCLG);
					this.genCLGGraph(post[post_number-1], clg_number);
					ArrayList<CLGGraph> clg_list2=new ArrayList<CLGGraph>();
					clg_list2.add(post[post_number-1]);
				
					this.clg_list.addAll(clg_list2);
					
				}
					
					
					else {// 只有post而已
					CLGGraph clg = this.getCLGGraph().get(clg_list.size() - 1);
					String[] clgindex = clg.getConstraintCollection().keySet().toString().substring(1, clg.getConstraintCollection().keySet().toString().length() - 1).split(", ");
					int a = Integer.parseInt(clgindex[0]);
					CLGConstraint post = clg.getConstraintNodeById(a).getConstraint();
					post.postconditionAddPre();
					clg = clgTF.CriterionTransformer(clg, Main.criterion);
					clg.getStartNode().setVisitFalse();
					this.genCLGGraph(clg, clg_number);
					clg_list.remove(clg_list.size() - 1);
					clg_list.add(clg);
					
					//設定attributes
					ArrayList<String> attribute = new ArrayList<String>();
					ArrayList<VariableToken> attributes = Main.symbolTable.getAttribute();
					for (int number = 0; number < attributes.size(); number++) {
						attribute.add(attributes.get(number).getVariableName());
					}
					((CLGStartNode) (clg.getStartNode())).setClassAttributes(attribute);
					
					/*start  ocl2clp*/
					//String methodCLP = ((CLGStartNode) clg.getStartNode()).OCL2CLP();
					//System.out.println(methodCLP);
					/*end ocl2clp*/
				}
			} else {// 處理inv的部分
				this.invCLG = new CLGGraph(((ClassifierContext) astNode.get(clg_number)).getInv().getTreeNode().AST2CLG());
				String classname = ((ClassifierContext) astNode.get(clg_number)).getClassName();
				((CLGStartNode) (this.invCLG.getStartNode())).setClassName(classname);
				((CLGStartNode) (this.invCLG.getStartNode())).setMethodName(classname);
				((CLGStartNode) (this.invCLG.getStartNode())).setIsConstructor(false);
				this.invCLG = clgTF.CriterionTransformer(this.invCLG, Main.criterion);
			}
		}
		Main.clg = this.clg_list;
	}
  
	public ArrayList<CLGGraph> getCLGGraph() {
		return this.clg_list;
	}

	public CLGGraph getInvCLG() {
		return this.invCLG;
	}

	public void genCLGGraph(CLGGraph clg, int number) throws IOException {
 FileWriter dataFile;
		
		dataFile = new FileWriter("${project_loc}/../../examples/output/CLG/CLG"+number+".dot");
		BufferedWriter input = new BufferedWriter(dataFile);
		input.write(clg.graphDraw());
		input.close();
				
		new ProcessBuilder("dot", "-Tpng", "${project_loc}/../../examples/output/CLG/CLG"+number+".dot",
		"-o", "${project_loc}/../../examples/output/CLG/CLG"+number+".png").start();
	}

	public void genCLGGraph(CLGGraph clg, int number, String methodName) throws IOException {
		FileWriter dataFile;
		File f = new File(this.clgFolder + "\\CLG");
		f.mkdir();
		// dataFile = new
		// FileWriter("${project_loc}/../../examples/output/CLG/CLG"+number+".dot");
		dataFile = new FileWriter(this.clgFolder + "\\CLG\\" + methodName + "CLG" + number + ".dot");
		BufferedWriter input = new BufferedWriter(dataFile);
		input.write(clg.graphDraw());
		input.close();

		new ProcessBuilder("dot", "-Tpng", this.clgFolder + "\\CLG\\" + methodName + "CLG" + number + ".dot", "-o", this.clgFolder + "\\CLG\\" + methodName + "CLG" + number + ".png").start();

	}
}
