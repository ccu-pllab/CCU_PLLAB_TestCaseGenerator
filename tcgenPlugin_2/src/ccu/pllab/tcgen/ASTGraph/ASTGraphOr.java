package ccu.pllab.tcgen.ASTGraph;

 
import ccu.pllab.tcgen.AbstractSyntaxTree.AbstractSyntaxTreeNode;

public class ASTGraphOr extends ASTGraphNode{
	
	public ASTGraphOr()
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
		String clp="operator_or_"+this.getId()+"(["+objpre+"],["+argpre+"],["+attribute+"],["+argument+"],[Result]):-\n";
		if(this.getLeft()!=null && !this.getLeft().getVisited())
		{
			if(this.getLeft() instanceof ASTGraphAnd)
			clp+="operator_and_"+this.getLeft().getId()+"(["+objpre+"],["+argpre+"],["+attribute+"],["+argument+"],[Result]).\n";
			else if(this.getLeft() instanceof ASTGraphIf)
			clp+="operator_if_"+this.getLeft().getId()+"(["+objpre+"],["+argpre+"],["+attribute+"],["+argument+"],[Result]).\n";
			clp+=this.getLeft().getCLP(attribute,argument);
		}
		else if(this.getRight()!=null && !this.getRight().getVisited())
		{
			if(this.getRight() instanceof ASTGraphAnd)
			clp+="operator_and_"+this.getRight().getId()+"(["+objpre+"],["+argpre+"],["+attribute+"],["+argument+"],[Result]).\n";
			else if(this.getLeft() instanceof ASTGraphIf)
			clp+="operator_if_"+this.getRight().getId()+"(["+objpre+"],["+argpre+"],["+attribute+"],["+argument+"],[Result]).\n";
			clp+=this.getRight().getCLP(attribute,argument);
		}
	//	if(this.getLeft()!=null && !this.getLeft().getVisited())
	//	{
			
	//	}
		//else if(this.getRight()!=null && !this.getLeft().getVisited())
			
		
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
