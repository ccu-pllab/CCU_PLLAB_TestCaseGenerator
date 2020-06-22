package ccu.pllab.tcgen.AbstractSyntaxTree;


import java.util.ArrayList;
import java.util.HashMap;

import ccu.pllab.tcgen.ASTGraph.ASTGraphNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.*;
public class StereoType extends AbstractSyntaxTreeNode{
	String stereoType;
	String exception;
	AbstractSyntaxTreeNode expression;
	
	public StereoType(String stereoType) {
		super();
		this.stereoType=stereoType;
	}

	public AbstractSyntaxTreeNode getTreeNode()
	{
		return this.expression;
	}
	
	public void addTreeChildNode(AbstractSyntaxTreeNode expression)
	{
		this.expression=expression;
	}
	
	public void setException(String exception)
	{
		this.exception=exception;
	}
	
	public String getException()
	{
		return this.exception;
	}
	public AbstractSyntaxTreeNode getExpression()
	{
		return this.expression;
	}
	public String getStereoType() {
		return stereoType;
	}
	//@Override
	//public String addOperatorType() {return "";}
	
	/*@Override
	public void addAllNodeType(String type){}
	
	@Override
	public void putAttributeToHashMap(HashMap<String,String> table){}
	
	@Override
	public  void takeHashMapKeyValue(HashMap<String,String> table){}
	
	@Override
	public void addSelfOrResultType(String className){}*/
	
	@Override
	public void addVariableType(SymbolTable symbolTable,String methodName)
	{}
	
	@Override
	public void changeAssignToEqual(){}
	
	@Override
	public void conditionChangeAssignToEqual() {}
	
	@Override
	public String childNodeInfo(){return "";}
	
	@Override
	public String ASTInformation()
	{
		return "\""+"("+this.getID()+")"+this.stereoType+"\"";
	}
	
	@Override
	public  CLGConstraint AST2CLG()
	{
		return expression.AST2CLG();
	}
	
	@Override
	public  CLGConstraint AST2CLG(boolean boundaryAnalysis)
	{
		return expression.AST2CLG();
	}
	
	@Override
	public  String NodeToString(){return "";}
	
	@Override
	public void toGraphViz()
	{//一個遞迴函式，來畫AST圖
		if(this.expression!=null)
		{
			System.out.println(this.ASTInformation()+"->"+this.expression.ASTInformation());
			this.expression.toGraphViz();//遞迴
		}
	}
	@Override
	public String AST2CLP(String attribute,String argument) 
	{
		return this.expression.AST2CLP(attribute, argument);
	}
	@Override
	public String demonganAST2CLP(String attribute,String argument) {return "";}
	@Override
	public  void preconditionAddPre() {
		this.expression.preconditionAddPre();
	}
	@Override
	public  void postconditionAddPre() {
		this.expression.postconditionAddPre();
	}
	@Override
	public void demoganOperator(){}
	@Override
	public ASTGraphNode AST2ASTGraph() {return null;}
	@Override
	public AbstractSyntaxTreeNode ASTclone()
	{
		return null;
	}
}
