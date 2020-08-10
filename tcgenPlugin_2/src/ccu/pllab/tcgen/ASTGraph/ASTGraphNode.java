package ccu.pllab.tcgen.ASTGraph;
 

import java.util.ArrayList;

import ccu.pllab.tcgen.AbstractSyntaxTree.AbstractSyntaxTreeNode;

public abstract class ASTGraphNode {
	private ArrayList<AbstractSyntaxTreeNode> operand=new ArrayList<AbstractSyntaxTreeNode>();
	private ASTGraphNode left;
	private ASTGraphNode right;
	private String id;
	private static int count=0;
	private boolean visited=false;
	public ASTGraphNode()
	{
		id=""+count++;
	}
	public void setId(String id)
	{
		this.id=id;;
	}
	public String getId()
	{
		return this.id;
	}
	
	public void setVisited()
	{
		this.visited=true;
	}
	public boolean getVisited()
	{
		return this.visited;
	}
	public void addOperand(AbstractSyntaxTreeNode node)
	{
		operand.add(node);
	}
	public ArrayList<AbstractSyntaxTreeNode> getOperand()
	{
		return this.operand;
	}
	public void setLeft(ASTGraphNode node)
	{
		left=node;
	}
	public ASTGraphNode getLeft()
	{
		return this.left;
	}
	public void setRight(ASTGraphNode node)
	{
		right=node;
	}
	public ASTGraphNode getRight()
	{
		return this.right;
	}
	public abstract String getCLP(String attribute,String argument);
	//public abstract boolean checkLinkNodeIsVisited();
	
}
