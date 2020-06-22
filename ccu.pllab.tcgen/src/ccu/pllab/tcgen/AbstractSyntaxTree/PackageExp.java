package ccu.pllab.tcgen.AbstractSyntaxTree;


import java.util.ArrayList;
import java.util.HashMap;

import ccu.pllab.tcgen.AbstractConstraint.*;
import ccu.pllab.tcgen.ASTGraph.ASTGraphNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;

public class PackageExp extends AbstractSyntaxTreeNode{
	String packageName;
	ArrayList<AbstractSyntaxTreeNode> context=new ArrayList<AbstractSyntaxTreeNode>();
	
	public PackageExp(String packageName)
	{
		super();//呼叫AbstractSyntaxTreeNode()
		this.packageName=packageName;
	}
	
	public ArrayList<AbstractSyntaxTreeNode> getTreeNode()
	{
		return this.context;
	}
	
	public void addTreeChildNode(AbstractSyntaxTreeNode ...  childNode)
	{
		for(AbstractSyntaxTreeNode node:childNode)
		this.context.add(node);
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
	public void addVariableType(SymbolTable symbolTable,String methodName)
	{}
	
	@Override
	public void changeAssignToEqual(){}
	
	@Override
	public String childNodeInfo(){return "";}
	
	@Override
	public String ASTInformation()
	{
		return "\""+"("+this.getID()+")"+this.packageName+"\"";
	}
	
	@Override
	public void conditionChangeAssignToEqual() {}
	
	@Override
	public  CLGConstraint AST2CLG(){return null;}
	
	@Override
	public  CLGConstraint AST2CLG(boolean boundaryAnalysis){return null;}
	
	@Override
	public  String NodeToString(){return "";}
	
	@Override
	public final void toGraphViz() 
	{//一個遞迴函式，來畫AST圖
		String astInformation=this.ASTInformation();
		for(AbstractSyntaxTreeNode oneChild:this.context)
		{
			System.out.println(astInformation+"->"+oneChild.ASTInformation());
			oneChild.toGraphViz();//遞迴
		}
	}
	@Override
	public String AST2CLP(String attribute,String method) {return "";}
	@Override
	public  void preconditionAddPre() {
		for(AbstractSyntaxTreeNode node:this.context)
			node.preconditionAddPre();
	}
	@Override
	public  void postconditionAddPre() {
		for(AbstractSyntaxTreeNode node:this.context)
			node.postconditionAddPre();	
	}
	@Override
	public void demoganOperator(){}
	@Override
	public String demonganAST2CLP(String attribute,String argument) {return "";}
	@Override
	public ASTGraphNode AST2ASTGraph() {return null;}
	@Override
	public AbstractSyntaxTreeNode ASTclone()
	{
		return null;
	}
}
