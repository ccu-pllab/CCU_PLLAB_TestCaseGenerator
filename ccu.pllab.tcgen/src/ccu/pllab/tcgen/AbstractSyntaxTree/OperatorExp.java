package ccu.pllab.tcgen.AbstractSyntaxTree;


import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.MessageSort;

import ccu.pllab.tcgen.ASTGraph.ASTGraphAnd;
import ccu.pllab.tcgen.ASTGraph.ASTGraphNode;
import ccu.pllab.tcgen.ASTGraph.ASTGraphOr;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.*;
import ccu.pllab.tcgen.exe.main.Main;

public class OperatorExp extends AbstractSyntaxTreeNode{
	String operator;
	String type="";
	AbstractSyntaxTreeNode leftOperand;
	AbstractSyntaxTreeNode rightOperand;
	String declareType;
	
	public OperatorExp(String operator) {
		super();
		this.operator=operator;
	}
	
	public String getOperator()
	{
		return this.operator;
	}
	
	public AbstractSyntaxTreeNode getLeftOperand()
	{
		return this.leftOperand;
	}
	
	public AbstractSyntaxTreeNode getRightOperand()
	{
		return this.rightOperand;
	}
	
	public void setOperand(AbstractSyntaxTreeNode leftOperand,AbstractSyntaxTreeNode rightOperand)
	{
		this.leftOperand=leftOperand;
		this.rightOperand=rightOperand;
	}
	
	public void setUnaryOperand(AbstractSyntaxTreeNode rightOperand)
	{
		this.leftOperand=null;
		this.rightOperand=rightOperand;
	}
	
	
	public void setDeclareType(String type)
	{
		this.declareType=type;
	}
	
	public String getDeclareType()
	{
		return this.declareType;
	}
	
	
	@Override
	public void addVariableType(SymbolTable symbolTable,String methodName)
	{
		if(this.leftOperand!=null)
			this.leftOperand.addVariableType(symbolTable, methodName);
		if(this.rightOperand!=null)
			this.rightOperand.addVariableType(symbolTable, methodName);
	}
	
	@Override
	public void changeAssignToEqual()
	{
		if(this.operator.equals("="))
			this.operator="==";
		else
		{
			if(this.leftOperand!=null)
				this.leftOperand.changeAssignToEqual();
			if(this.rightOperand!=null)
				this.rightOperand.changeAssignToEqual();
		}
	}
	
	@Override
	public void conditionChangeAssignToEqual() 
	{
		if(this.leftOperand!=null)
			this.leftOperand.conditionChangeAssignToEqual();
		if(this.rightOperand!=null)
			this.rightOperand.conditionChangeAssignToEqual();
	}
	
	@Override
	public String childNodeInfo()
	{
		String info="";
		if(this.leftOperand!=null)
			info+=this.leftOperand.childNodeInfo();
		info+=this.operator;
		if(this.rightOperand!=null)
			info+=this.rightOperand.childNodeInfo();
		return info;
	}
	
	@Override
	public String ASTInformation()
	{
		return "\""+"("+this.getID()+")"+this.operator+"\"";
	}
	
	@Override
	public  CLGConstraint AST2CLG()
	{
		CLGConstraint constraint=null;
		if(this.operator.equals("."))
		{
			constraint=this.leftOperand.AST2CLG();
			CLGConstraint right=this.rightOperand.AST2CLG();
			((CLGVariableNode)constraint).setConstraint(right);
		}
		else if(this.operator.equals("->"))
		{
			PropertyCallExp right=(PropertyCallExp)this.rightOperand;
			if(right.getVariable().contains("msort"))
			{
				return null;
			}
			/*else if(right.getVariable().contains("size"))
			{
				right.setVariable("size_pre");
			}*/
			PropertyCallExp left=(PropertyCallExp)this.leftOperand;
			String pre="";
			if(left.getTimeExpression())
				pre="pre";
			constraint=new CLGVariableNode(left.getVariable()+pre+right.getVariable()+"_pre","String");
		}
		else
		{
		
			
			constraint=new CLGOperatorNode(this.operator);
			if(Main.bodyExpBoundary)
			((CLGOperatorNode) constraint).setBoundary();
			((CLGOperatorNode)constraint).setType(this.declareType);
			if(this.leftOperand!=null)
			{
				if(this.leftOperand.AST2CLG() ==null)
					return this.rightOperand.AST2CLG();
				((CLGOperatorNode)constraint).setLeftOperand(this.leftOperand.AST2CLG());
			}
			
				
			((CLGOperatorNode)constraint).setRightOperand(this.rightOperand.AST2CLG());	
			
			
			
		}
		return constraint;
	}
	
	@Override
	public  CLGConstraint AST2CLG(boolean boundaryAnalysis)
	{
		CLGConstraint constraint=null;
		if(this.operator.equals("."))
		{
			constraint=this.leftOperand.AST2CLG(boundaryAnalysis);
			CLGConstraint right=this.rightOperand.AST2CLG(boundaryAnalysis);
			((CLGVariableNode)constraint).setConstraint(right);
		}
		else if(this.operator.equals("->"))
		{
			PropertyCallExp right=(PropertyCallExp)this.rightOperand;
			PropertyCallExp left=(PropertyCallExp)this.leftOperand;
			String pre="";
			if(left.getTimeExpression())
				pre="pre";
			constraint=new CLGVariableNode(left.getVariable()+pre+right.getVariable()+"_pre","String");
		}
		else
		{
			constraint=new CLGOperatorNode(this.operator);
			((CLGOperatorNode)constraint).setType(this.declareType);
			if(this.leftOperand!=null)
			{
				((CLGOperatorNode)constraint).setLeftOperand(this.leftOperand.AST2CLG(boundaryAnalysis));
			}
			((CLGOperatorNode)constraint).setRightOperand(this.rightOperand.AST2CLG(boundaryAnalysis));	
		}
		return constraint;
	}
	
	@Override
	public String NodeToString()
	{
		String name="";
		if(this.leftOperand!=null)
			name+=this.leftOperand.NodeToString();
		name+=this.operator;
		name+=this.rightOperand.NodeToString();
		return name;
	}
	
	@Override
	public void toGraphViz()
	{//一個遞迴函式，來畫AST圖
		String astInformation=this.ASTInformation();//減少一直呼叫函式
		if(this.leftOperand!=null)
		{
			System.out.println(astInformation+"->"+this.leftOperand.ASTInformation());
			this.leftOperand.toGraphViz();//遞迴
		}
		if(this.rightOperand!=null)
		{
			System.out.println(astInformation+"->"+this.rightOperand.ASTInformation());
			this.rightOperand.toGraphViz();
		}
	}
	
	@Override
	public String AST2CLP(String attribute,String argument) 
	{
		
		String obj_pre="",arg_pre="";
		if(attribute.length()>0)
			obj_pre=attribute.replaceAll(",", "_pre,")+"_pre";
		if(argument.length()>0)
			arg_pre=argument.replaceAll(",", "_pre,")+"_pre";
		String clp="operator_"+this.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,Result):-\n";
		switch (this.operator) 
		{
		case "and":
		case "or":
			if(this.leftOperand instanceof OperatorExp)
				clp+="operator_";
			else if(this.leftOperand instanceof PropertyCallExp)
				clp+="variable_";
			else {
				clp+="literal_";
			}
			clp+=this.leftOperand.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,[Left]),\n";
			if(this.rightOperand instanceof OperatorExp)
				clp+="operator_";
			else if(this.rightOperand instanceof PropertyCallExp)
				clp+="variable_";
			else {
				clp+="literal_";
			}
			clp+=this.rightOperand.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,[Right]).\n\n";
			break;
		case "+":
		case "-":
		case "*":
		case "/":
			if(this.leftOperand instanceof OperatorExp)
				clp+="operator_";
			else if(this.leftOperand instanceof PropertyCallExp)
				clp+="variable_";
			else {
				clp+="literal_";
			}
			clp+=this.leftOperand.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,[Left]),\n";
			if(this.rightOperand instanceof OperatorExp)
				clp+="operator_";
			else if(this.rightOperand instanceof PropertyCallExp)
				clp+="variable_";
			else {
				clp+="literal_";
			}
			clp+=this.rightOperand.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,[Right]),\n";
			clp+="Result=Left"+this.operator+"Right.\n\n";
		case "=":
			clp+="variable_"+this.leftOperand.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,[Left]),\n";
			if(this.rightOperand instanceof OperatorExp)
				clp+="operator_";
			else if(this.rightOperand instanceof PropertyCallExp)
				clp+="variable_";
			else {
				clp+="literal_";
			}
			clp+=this.rightOperand.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,[Right]),\n";
			if(((PropertyCallExp) this.leftOperand).getVariable().equals("result"))
				clp+="Left=Right,\nResult=Left.\n\n";
			else
			clp+="Left=Right.\n\n";
			break;
		case "==":
		case "<>":
		case ">":
		case ">=":
		case "<":
		case "<=":
			if(this.leftOperand instanceof OperatorExp)
				clp+="operator_";
			else if(this.leftOperand instanceof PropertyCallExp)
				clp+="variable_";
			else {
				clp+="literal_";
			}
			clp+=this.leftOperand.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,[Left]),\n";
			if(this.rightOperand instanceof OperatorExp)
				clp+="operator_";
			else if(this.rightOperand instanceof PropertyCallExp)
				clp+="variable_";
			else {
				clp+="literal_";
			}
			clp+=this.rightOperand.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,[Right]),\n";
			
			if(this.operator.equals("<="))
				clp+="Left#=<Right.\n\n";
			else if(this.operator.equals("<>"))
				clp+="Left=/=Right.\n\n";
			else
			clp+="Left#"+this.operator+"Right.\n\n";
			break;
		}
		clp+=this.leftOperand.AST2CLP(attribute, argument);
		clp+=this.rightOperand.AST2CLP(attribute, argument);
		return clp;
	}
	@Override
	public  void preconditionAddPre() {
		if(this.leftOperand!=null)
			this.leftOperand.preconditionAddPre();
		if(this.rightOperand!=null)
			this.rightOperand.preconditionAddPre();
	}
	@Override
	public  void postconditionAddPre() {
		if(this.operator.equals("||")||this.operator.equals("&&"))
		{
		//	System.out.println("testOperator:"+this.operator);
		//	if(this.leftOperand!=null)
			this.leftOperand.postconditionAddPre();
			//if(this.rightOperand!=null)
			this.rightOperand.postconditionAddPre();
		}
		else if(this.operator.equals("="))
		{
			this.addpostpre(this.rightOperand);
		}
	}
	
	public void addpostpre(AbstractSyntaxTreeNode node)
	{
		if(node instanceof OperatorExp)
		{
				addpostpre(((OperatorExp)node).getLeftOperand());
				addpostpre(((OperatorExp)node).getRightOperand());
		}
		else if(node instanceof PropertyCallExp)
		{
			((PropertyCallExp)node).postconditionAddPre();;
		}
	}
	
	
	@Override
	public void demoganOperator()
	{
		switch (this.operator) {
		case "and":
			this.operator="or";
			break;
		case "or":
			this.operator="and";
			break;
		case "==":
			this.operator="<>";
			break;
		case ">=":
			this.operator="<";
			break;
		case "<=":
			this.operator=">";
			break;
		case ">":
			this.operator="=<";
			break;
		case "<":
			this.operator=">=";
			break;
		default:
			break;
		}
		this.leftOperand.demoganOperator();
		this.rightOperand.demoganOperator();
	}
	@Override
	public String demonganAST2CLP(String attribute,String argument) 
	{
		String obj_pre="",arg_pre="",clp="";
		/*if(attribute.length()>0)
			obj_pre=attribute.replaceAll(",", "_pre,")+"_pre";
		if(argument.length()>0)
			arg_pre=argument.replaceAll(",", "_pre,")+"_pre";
		String clp="operator_"+this.getID()+"_2(["+obj_pre+"],["+arg_pre+"],["+attribute+"],["+argument+"],[Result]):-\n";
		switch (this.operator) 
		{
		case "and":
		case "or":
			break;
		case "+":
		case "-":
		case "*":
		case "/":
			
		case "=":
			
			break;
		case "==":
		case "<>":
		case ">":
		case ">=":
		case "<":
		case "<=":*/
			
		return clp;
	}
	
	
	@Override
	public ASTGraphNode AST2ASTGraph() 
	{
		ASTGraphNode connect=null;
		if(this.operator.equals("and"))
		{
			connect=new ASTGraphAnd();
			if((this.leftOperand instanceof OperatorExp) && 
				( !((OperatorExp)this.leftOperand).equals("and")&&!((OperatorExp)this.leftOperand).equals("or")))
			{
				connect.addOperand(this.leftOperand);	
			}
			if((this.rightOperand instanceof OperatorExp) && 
					( !((OperatorExp)this.rightOperand).equals("and")&&!((OperatorExp)this.rightOperand).equals("or")))
				{
					connect.addOperand(this.rightOperand);	
				}
				connect.setLeft(this.leftOperand.AST2ASTGraph());
				connect.setRight(this.rightOperand.AST2ASTGraph());
		}
		else if(this.operator.equals("and"))
		{
			connect=new ASTGraphOr();
			connect.setLeft(this.leftOperand.AST2ASTGraph());
			connect.setRight(this.rightOperand.AST2ASTGraph());
		}
		return connect;
	}
	@Override
	public AbstractSyntaxTreeNode ASTclone()
	{
		OperatorExp oper= new OperatorExp(this.operator);
		oper.setOperand(this.leftOperand.ASTclone(), this.rightOperand.ASTclone());
		return oper;
	}
}
