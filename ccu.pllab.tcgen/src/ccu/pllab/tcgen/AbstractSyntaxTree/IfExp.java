package ccu.pllab.tcgen.AbstractSyntaxTree;


import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;

import ccu.pllab.tcgen.ASTGraph.ASTGraphIf;
import ccu.pllab.tcgen.ASTGraph.ASTGraphNode;
import ccu.pllab.tcgen.ASTGraph.ASTGraphOr;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.*;
import scala.cloneable;

public class IfExp  extends AbstractSyntaxTreeNode{
	String ifExp;
	AbstractSyntaxTreeNode conditionExp;
	AbstractSyntaxTreeNode thenExp;
	AbstractSyntaxTreeNode elseExp;
	
	//子節點沒有else
	public IfExp(String ifExp,AbstractSyntaxTreeNode condition,AbstractSyntaxTreeNode then) {
		super();
		this.ifExp=ifExp;
		this.conditionExp=condition;
		this.thenExp=then;
		
	}
	
	//子節點有else
	public IfExp(String ifExp,AbstractSyntaxTreeNode condition,AbstractSyntaxTreeNode then,AbstractSyntaxTreeNode elseExp) {
		super();
		this.ifExp=ifExp;
		this.conditionExp=condition;
		this.thenExp=then;
		this.elseExp=elseExp;
	}
	
	public void setIfExp(String ifExp) {
		this.ifExp = ifExp;
	}
	public void setConditionExp(AbstractSyntaxTreeNode conditionExp) {
		this.conditionExp = conditionExp;
	}
	public void setElseExp(AbstractSyntaxTreeNode elseExp) {
		this.elseExp = elseExp;
	}
	
	public AbstractSyntaxTreeNode getCondition()
	{
		return this.conditionExp;
	}
	public AbstractSyntaxTreeNode getThen()
	{
		return this.thenExp;
	}
	public AbstractSyntaxTreeNode getElse()
	{
		return this.elseExp;
	}
	
	
	@Override
	public String ASTInformation()
	{
		return "\""+"("+this.getID()+")"+this.ifExp+"\"";
	}
	
	@Override
	public void addVariableType(SymbolTable symbolTable,String methodName)
	{
		this.conditionExp.addVariableType(symbolTable, methodName);
		this.thenExp.addVariableType(symbolTable, methodName);
		if(this.elseExp!=null)
		this.elseExp.addVariableType(symbolTable, methodName);
	}
	
	@Override
	public void changeAssignToEqual()
	{
		this.conditionExp.changeAssignToEqual();
		this.thenExp.changeAssignToEqual();
		this.elseExp.changeAssignToEqual();
	}
	
	@Override
	public void conditionChangeAssignToEqual() 
	{
		this.conditionExp.changeAssignToEqual();
		if(this.thenExp instanceof IfExp)
		this.thenExp.conditionChangeAssignToEqual();
		if(this.elseExp instanceof IfExp)
		this.elseExp.conditionChangeAssignToEqual();
	}
	
	@Override
	public String childNodeInfo(){return "";}
	
	
	@Override
	public  CLGConstraint AST2CLG()
	{
		CLGConstraint condition=this.conditionExp.AST2CLG();
		CLGConstraint notcondition=this.Demongan(condition);
		CLGConstraint then=this.thenExp.AST2CLG();
		if(this.elseExp!=null)
		{
		CLGConstraint elseExp=this.elseExp.AST2CLG();
		return new CLGIfNode(condition,then,elseExp);
		}
		else
		{
			return new CLGIfNode(condition,then);
		}
	}
	
	@Override
	public  CLGConstraint AST2CLG(boolean boundaryAnalysis)
	{
		CLGConstraint condition=this.conditionExp.AST2CLG(boundaryAnalysis);
		CLGConstraint notcondition=this.Demongan(condition);
		CLGConstraint then=this.thenExp.AST2CLG(boundaryAnalysis);
		if(this.elseExp!=null)
		{
		CLGConstraint elseExp=this.elseExp.AST2CLG(boundaryAnalysis);
		return new CLGIfNode(condition,then,elseExp);
		}
		else
		{
			return new CLGIfNode(condition,then);
		}
	}
	
	@Override
	public  String NodeToString(){return "";}
	
	@Override
	public final void toGraphViz() 
	{//一個遞迴函式，來畫AST圖
			String astInformation=this.ASTInformation();
			System.out.println(astInformation+"->"+this.conditionExp.ASTInformation());
			this.conditionExp.toGraphViz();//遞迴
			System.out.println(astInformation+"->"+this.thenExp.ASTInformation());
			this.thenExp.toGraphViz();
			if(this.elseExp!=null)
			{
				System.out.println(astInformation+"->"+this.elseExp.ASTInformation());
				this.elseExp.toGraphViz();
			}
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
	@Override
	public String AST2CLP(String attribute,String argument) {
		String obj_pre="",arg_pre="";
		if(attribute.length()>0)
			obj_pre=attribute.replaceAll(",", "_pre,")+"_pre";
		if(argument.length()>0)
			arg_pre=argument.replaceAll(",", "_pre,")+"_pre";
		String clp="if_"+this.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,Result):-\n";
		clp+="condition"+this.conditionExp.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,Condition),";
		clp+="ifExpression(Obj_pre,Arg_pre,Obj_post,Arg_post,Condition,ThenPredicate,ElsePredicate,Result).\n\n";
		clp+="delay ifExpression(_,_,_,_,Condition,_,_,__) if nonground(Condition).\n";
		clp+="ifExpression(Obj_pre,Arg_pre,Obj_post,Arg_post,Condition,ThenPredicate,ElsePredicate,Result):-\n";
		clp+="(\nCondition=1-> apply(ThenPredicate,[Obj_pre,Arg_pre,Obj_post,Arg_post,Result]);\n";
		clp+="apply(ElsePredicate,[Obj_pre,Arg_pre,Obj_post,Arg_post,Result]).\n)\n\n";
		
		clp+=this.conditionExp.AST2CLP(attribute, argument);
		clp+=this.thenExp.AST2CLP(attribute, argument);
		clp+=this.elseExp.AST2CLP(attribute, argument);
		return clp;
	}
	@Override
	public String demonganAST2CLP(String attribute,String argument) {return "";}
	
	@Override
	public void demoganOperator()
	{
		this.conditionExp.demoganOperator();
	}
	
	
	
	@Override
	public  void preconditionAddPre() {
		this.conditionExp.preconditionAddPre();
		this.thenExp.preconditionAddPre();
		if(this.elseExp!=null)
			this.elseExp.preconditionAddPre();
	}
	@Override
	public  void postconditionAddPre() {
		this.conditionExp.preconditionAddPre();
		this.thenExp.postconditionAddPre();
		if(this.elseExp!=null)
			this.elseExp.postconditionAddPre();
	}
	@Override
	public ASTGraphNode AST2ASTGraph() 
	{
		ASTGraphOr connect=new ASTGraphOr();
		ASTGraphIf iftrue=new ASTGraphIf();
		ASTGraphIf iffalse=new ASTGraphIf();
		iftrue.addOperand(this.conditionExp.ASTclone());
		if((this.thenExp instanceof OperatorExp) && 
				( !((OperatorExp)this.thenExp).equals("and")&&!((OperatorExp)this.thenExp).equals("or")))
			iftrue.addOperand(this.thenExp);
		iffalse.addOperand(this.conditionExp.ASTclone());
		if((this.elseExp instanceof OperatorExp) && 
				( !((OperatorExp)this.elseExp).equals("and")&&!((OperatorExp)this.elseExp).equals("or")))
			iffalse.addOperand(this.elseExp);
		iftrue.setLeft(null);
		iffalse.setLeft(null);
		iftrue.setRight(this.thenExp.AST2ASTGraph());
		iffalse.setRight(this.elseExp.AST2ASTGraph());
		connect.setLeft(iftrue);
		connect.setRight(iffalse);
		return connect;
		}
	@Override
	public AbstractSyntaxTreeNode ASTclone()
	{
		IfExp ifExp=null;
		if(this.elseExp==null)
		ifExp=new IfExp(this.ifExp, this.conditionExp, this.thenExp);
		else
			ifExp=new IfExp(this.ifExp, this.conditionExp, this.thenExp,this.elseExp);
		return null;
	}
}
