package ccu.pllab.tcgen.ASTGraph;

 

import ccu.pllab.tcgen.AbstractSyntaxTree.AbstractSyntaxTreeNode;
import ccu.pllab.tcgen.AbstractSyntaxTree.OperatorExp;

public class ASTGraphAnd extends ASTGraphNode{
	
	public ASTGraphAnd()
	{
		super();
	}

	@Override
	public String getCLP(String attribute,String argument) {
		// TODO Auto-generated method stub
		String objpre=attribute.replaceAll(",", "_pre,");
		objpre+="_pre";
		String argpre=argument.replaceAll(",", "_pre,");
		argpre+="_pre";
		String clp="operator_and_"+this.getId()+"(["+objpre+"],["+argpre+"],["+attribute+"],["+argument+"],[Result]):-\n";
		for(AbstractSyntaxTreeNode node:this.getOperand())
			clp+=node.AST2CLP(attribute,argument);
		if(this.getLeft()!=null)
			clp+=this.getLeft().getCLP(attribute,argument);
		if(this.getRight()!=null)
			clp+=this.getRight().getCLP(attribute,argument);

		if(this.getLeft()==null &&this.getRight()==null)
			this.setVisited();
		else if(this.getLeft().getVisited() &&this.getRight()==null)
			this.setVisited();
		else if(this.getLeft()==null &&this.getRight().getVisited())
			this.setVisited();
		else if(this.getLeft().getVisited() &&this.getRight().getVisited())
			this.setVisited();
		return clp;
	}
}
