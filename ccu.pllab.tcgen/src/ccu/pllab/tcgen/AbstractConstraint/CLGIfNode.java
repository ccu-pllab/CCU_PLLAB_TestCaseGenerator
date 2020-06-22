package ccu.pllab.tcgen.AbstractConstraint;


import java.util.ArrayList;

import ccu.pllab.tcgen.exe.main.Main;

public class CLGIfNode  extends CLGConstraint{
	private CLGConstraint condition;
	private CLGConstraint then;
	private CLGConstraint elseExp;
	private CLGConstraint noncondition;
	//private CLGOperatorNode and;
	
	public CLGIfNode(CLGConstraint condition,CLGConstraint then,CLGConstraint elseExp) {
		// TODO Auto-generated constructor stub
		super();
		this.condition=condition;
		this.then=then;
		this.elseExp=elseExp;
		this.noncondition=condition;
	}
	public CLGIfNode(CLGConstraint condition,CLGConstraint then) {
		// TODO Auto-generated constructor stub
		super();
		this.condition=condition;
		this.then=then;
		this.noncondition=condition;
	}
	public CLGConstraint getCondition()
	{
		return this.condition;
	}
	public CLGConstraint getThen()
	{
		return this.then;
	}
	public CLGConstraint getElse()
	{
		return this.elseExp;
	}
	public CLGConstraint getNoncondition() {
		return this.noncondition;
	}
	@Override
	public  String getImgInfo() {return this.getCLPInfo();}
	
	@Override
	public  String getCLPInfo() 
	{
		return "";
	}
	@Override
	public  ArrayList<String> getInvCLPInfo()
	{
		ArrayList<String> invCLP=new ArrayList<String>();
		invCLP.addAll(this.condition.getInvCLPInfo());
		invCLP.addAll(this.then.getInvCLPInfo());
		invCLP.addAll(this.elseExp.getInvCLPInfo());
		return invCLP;
	}
	public  CLGConstraint clone() 
	{
		if(this.elseExp!=null)
		return new CLGIfNode(this.condition,this.then,this.elseExp);
		else
			return new CLGIfNode(this.condition,this.then);
	}
	public  String getCLPValue() {return "";}
	public  String getLocalVariable() {return "";}
	public  void setCLPValue(String data) {}
	@Override
	public void negConstraint() {
		this.condition.negConstraint();
		this.then.negConstraint();
		if(this.elseExp!=null)
		this.elseExp.negConstraint();
	}//??
	
	@Override
	public void preconditionAddPre()
	{
		this.condition.preconditionAddPre();
		this.then.preconditionAddPre();
		if(this.elseExp!=null)
			this.elseExp.preconditionAddPre();
	}
	
	@Override
	public void postconditionAddPre()
	{
		this.condition.preconditionAddPre();
		this.then.postconditionAddPre();
		if(this.elseExp!=null)
			this.elseExp.postconditionAddPre();
	}
public CLGConstraint Demongan(CLGConstraint Demonganconstraint){

		
		CLGOperatorNode finaltree = null;
		
		if(((CLGOperatorNode)Demonganconstraint).getOperator().equals("&&") ||((CLGOperatorNode)Demonganconstraint).getOperator().equals("and"))
		{
            finaltree=new CLGOperatorNode("||");
		}
		else if(((CLGOperatorNode)Demonganconstraint).getOperator().equals("||")||((CLGOperatorNode)Demonganconstraint).getOperator().equals("or"))
		{
			finaltree=new CLGOperatorNode("&&");	
		}
		else if(((CLGOperatorNode)Demonganconstraint).getOperator().equals("==")||((CLGOperatorNode)Demonganconstraint).getOperator().equals("="))
		{
			finaltree=new CLGOperatorNode("<>");
		}
		else if(((CLGOperatorNode)Demonganconstraint).getOperator().equals("<>"))
		{
			finaltree=new CLGOperatorNode("==");
		}
		else if(((CLGOperatorNode)Demonganconstraint).getOperator().equals("<="))
		{
			finaltree=new CLGOperatorNode(">");
		}
		else if(((CLGOperatorNode)Demonganconstraint).getOperator().equals(">"))
		{
			finaltree=new CLGOperatorNode("<=");
		}
		else if(((CLGOperatorNode)Demonganconstraint).getOperator().equals(">="))
		{
			finaltree=new CLGOperatorNode("<");
		}
		else if(((CLGOperatorNode)Demonganconstraint).getOperator().equals("<"))
		{
			finaltree=new CLGOperatorNode(">=");
		}
		if(((CLGOperatorNode)Demonganconstraint).getLeftOperand() instanceof ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode && !((CLGOperatorNode) ((CLGOperatorNode)Demonganconstraint).getLeftOperand()).getOperator().contains("*") && !((CLGOperatorNode) ((CLGOperatorNode)Demonganconstraint).getLeftOperand()).getOperator().contains("%")&& !((CLGOperatorNode) ((CLGOperatorNode)Demonganconstraint).getLeftOperand()).getOperator().contains("+")&& !((CLGOperatorNode) ((CLGOperatorNode)Demonganconstraint).getLeftOperand()).getOperator().contains("-"))
		{
			
			finaltree.setLeftOperand(Demongan(((CLGOperatorNode)Demonganconstraint).getLeftOperand()));
			
		}
		else{
			finaltree.setLeftOperand(((CLGOperatorNode)Demonganconstraint).getLeftOperand());
		}
		
		
		
		if(((CLGOperatorNode)Demonganconstraint).getRightOperand() instanceof ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode  && !((CLGOperatorNode) ((CLGOperatorNode)Demonganconstraint).getRightOperand()).getOperator().contains("*")&& !((CLGOperatorNode) ((CLGOperatorNode)Demonganconstraint).getRightOperand()).getOperator().contains("+")&& !((CLGOperatorNode) ((CLGOperatorNode)Demonganconstraint).getRightOperand()).getOperator().contains("-")&& !((CLGOperatorNode) ((CLGOperatorNode)Demonganconstraint).getRightOperand()).getOperator().contains("%"))
		{
			
			finaltree.setRightOperand(Demongan(((CLGOperatorNode)Demonganconstraint).getRightOperand()));
		}
		else{
			finaltree.setRightOperand(((CLGOperatorNode)Demonganconstraint).getRightOperand());
		}
	
		return finaltree;
	
	}
}
