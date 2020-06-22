package ccu.pllab.tcgen.AbstractSyntaxTree;


import java.util.ArrayList;
import java.util.HashMap;

import ccu.pllab.tcgen.ASTGraph.ASTGraphNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.*;

public class ClassifierContext extends AbstractSyntaxTreeNode{
	String className;
	private StereoType inv;//一定是inv，所以不用AbstractSyntaxTreeNode
	
	public ClassifierContext(String className) {
		super();
		this.className=className;
	}
	
	public void setStereoType(StereoType inv)
	{
		this.inv=inv;
	}
	
	public String getClassName()
	{
		return this.className;
	}
	
	public StereoType getInv()
	{
		return this.inv;
	}
	
	/*@Override
	public String addOperatorType(){return "";}
	
	/*@Override
	public void addAllNodeType(String type){}
	
	@Override
	public void putAttributeToHashMap(HashMap<String,String> table){}
	
	@Override
	public  void takeHashMapKeyValue(HashMap<String,String> table){}
	
	@Override
	public void addSelfOrResultType(String className){}*/
	
	@Override
	public void addVariableType(SymbolTable symbolTable,String methodName){}
	
	@Override
	public void changeAssignToEqual(){}
	
	@Override
	public void conditionChangeAssignToEqual() {}
	
	@Override
	public String childNodeInfo(){return "";}
	
	@Override
	public String ASTInformation()
	{
		return "\""+"("+this.getID()+")"+this.className+"\"";
	}
	
	@Override
	public  CLGConstraint AST2CLG(){return null;}
	
	@Override
	public  CLGConstraint AST2CLG(boolean boundaryAnalysis){return null;}
	
	@Override
	public  String NodeToString(){return "";}
	
	@Override
	public final void toGraphViz() 
	{//一個遞迴函式，來畫AST圖
			System.out.println(this.ASTInformation()+"->"+this.inv.ASTInformation());
			this.inv.toGraphViz();//遞迴
	}
	
	@Override
	public void demoganOperator()
	{}
	@Override
	public String AST2CLP(String attribute,String method) {return "";}
	@Override
	public String demonganAST2CLP(String attribute,String argument) {return "";}
	@Override
	public  void preconditionAddPre() {
		this.inv.preconditionAddPre();
	}
	@Override
	public  void postconditionAddPre() {}
	
	@Override
	public ASTGraphNode AST2ASTGraph() {return null;}
	
	@Override
	public AbstractSyntaxTreeNode ASTclone()
	{
		return null;
	}
}
