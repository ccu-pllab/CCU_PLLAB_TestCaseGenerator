package ccu.pllab.tcgen.ASTGraph;

 
import ccu.pllab.tcgen.AbstractSyntaxTree.AbstractSyntaxTreeNode;

public class ASTGraphIf extends ASTGraphNode{
	boolean isElse=false;
	public ASTGraphIf()
	{
		super();
	}
	public void setIsElse()
	{
		this.isElse=true;
	}
	@Override
	public String getCLP(String attribute,String argument) {
		// TODO Auto-generated method stub
		String clp="";
		for(AbstractSyntaxTreeNode node:this.getOperand())
			clp+=node.AST2CLP(attribute,argument);
		if(this.getRight()!=null)
			clp+=this.getRight().getCLP(attribute,argument);
		if(this.getRight().getVisited())
			this.setVisited();
		return clp;
	}
}
