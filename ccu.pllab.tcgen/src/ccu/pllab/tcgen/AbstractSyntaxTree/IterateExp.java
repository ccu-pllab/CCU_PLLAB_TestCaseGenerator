package ccu.pllab.tcgen.AbstractSyntaxTree;

import java.util.ArrayList;

import ccu.pllab.tcgen.ASTGraph.ASTGraphNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGCollectionNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGIterateNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;
import ccu.pllab.tcgen.exe.main.Main;


public class IterateExp extends AbstractSyntaxTreeNode{
	private String type;
	private AbstractSyntaxTreeNode start;
	private AbstractSyntaxTreeNode end;
	private int counting=1;
	private String acc;
	private String acc_type;
	private AbstractSyntaxTreeNode body;
	private String methodName;
	private String it;
	private String acc_variable;
	
	public IterateExp(String type)
	{
		super();
		this.type=type;
	}
	public void setStart(AbstractSyntaxTreeNode start)
	{
		this.start=start;
	}
	public void setEnd(AbstractSyntaxTreeNode end)
	{
		this.end=end;
	}
	public void setBody(AbstractSyntaxTreeNode body)
	{
		this.body=body;
	}
	public AbstractSyntaxTreeNode getStart()
	{
		return this.start;
	}
	public AbstractSyntaxTreeNode getEnd()
	{
		return this.end;
	}
	public AbstractSyntaxTreeNode getBody()
	{
		return this.body;
	}
	
	public void setAcc(String acc)
	{
		this.acc=acc;
	}
	public void setIt(String it)
	{
		this.it=it;
	}
	public void setAccVariable(String acc)
	{
		this.acc_variable=acc;
	}
	public void setAccType(String type)
	{
		this.acc_type=type;
	}
	
	public String getAccType()
	{
		return this.acc_type;
	}
	
	@Override
	public String childNodeInfo(){return "";}
	
	@Override
	public String ASTInformation()
	{
		return "\""+this.getID()+")"+this.type+"\"";
	}
	
	@Override
	public CLGConstraint AST2CLG()
	{
		CLGIterateNode iterateNode=new CLGIterateNode();
		CLGOperatorNode initial=new CLGOperatorNode("=");
	//	initial.setLeftOperand(new CLGVariableNode("it_pre","Integer"));
		initial.setLeftOperand(new CLGVariableNode(this.it,"Integer"));
		initial.setRightOperand(this.start.AST2CLG());
		iterateNode.setStart(this.start.AST2CLG());
		
		CLGOperatorNode acc=new CLGOperatorNode("=");
		if(this.acc_type.equals("Integer"))
		{
		acc.setLeftOperand(new CLGVariableNode(this.acc_variable,"Integer"));
		acc.setRightOperand(new CLGLiteralNode(this.acc, "Integer"));
		iterateNode.setAccType("Integer");
		}
		else if(this.acc_type.equals("Boolean"))
		{
			acc.setLeftOperand(new CLGVariableNode(this.acc_variable,"Boolean"));
			acc.setRightOperand(new CLGLiteralNode(this.acc, "Boolean"));
			iterateNode.setAccType("Boolean");
		}
		CLGOperatorNode allInit=new CLGOperatorNode("&&");
		allInit.setLeftOperand(initial);
		allInit.setRightOperand(acc);
		iterateNode.setInitial(allInit);
		CLGOperatorNode condition=null;
	//	if(Main.boundary_analysis)
	/*	if(Main.msort && Main.boundary_analysis)
		{
			condition=new CLGOperatorNode("<");
			condition.setLeftOperand(new CLGVariableNode(this.it+"_pre","Integer"));
			OperatorExp add=new OperatorExp("+");
			add.setOperand(this.end, new LiteralExp("Integer", "1"));
			condition.setRightOperand(add.AST2CLG());
		}
		else
		{*/
		condition=new CLGOperatorNode("<=");
		condition.setLeftOperand(new CLGVariableNode(this.it,"Integer"));
		condition.setRightOperand(this.end.AST2CLG());
		condition.setFromIterateExp();
		//}
		iterateNode.setCondition(condition);
		Main.bodyExpBoundary=true;
		CLGConstraint body=this.body.AST2CLG();
		Main.bodyExpBoundary=false;
		body.postconditionAddPre();
		iterateNode.setBody(body);
		CLGOperatorNode increment=new CLGOperatorNode("=");
		increment.setLeftOperand(new CLGVariableNode(this.it,"Integer"));
		CLGOperatorNode addone=new CLGOperatorNode("+");
		addone.setLeftOperand(new CLGVariableNode(this.it,"Integer"));
		addone.setRightOperand(new CLGLiteralNode("1","Integer"));
		increment.setRightOperand(addone);
		iterateNode.setIncrement(increment);
		return iterateNode;
	}
	
	@Override
	public CLGConstraint AST2CLG(boolean boundaryAnalysis)
	{
		CLGIterateNode iterateNode=new CLGIterateNode();
		CLGOperatorNode initial=new CLGOperatorNode("=");
		initial.setLeftOperand(new CLGVariableNode("it","Integer"));
		initial.setRightOperand(this.start.AST2CLG(boundaryAnalysis));
		iterateNode.setStart(this.start.AST2CLG(boundaryAnalysis));
		
		CLGOperatorNode acc=new CLGOperatorNode("=");
		if(this.acc_type.equals("Integer"))
		{
		acc.setLeftOperand(new CLGVariableNode("acc","Integer"));
		acc.setRightOperand(new CLGLiteralNode(this.acc, "Integer"));
		iterateNode.setAccType("Integer");
		}
		else if(this.acc_type.equals("Boolean"))
		{
			acc.setLeftOperand(new CLGVariableNode("acc","Boolean"));
			acc.setRightOperand(new CLGLiteralNode(this.acc, "Boolean"));
			iterateNode.setAccType("Boolean");
		}
		CLGOperatorNode allInit=new CLGOperatorNode("&&");
		allInit.setLeftOperand(initial);
		allInit.setRightOperand(acc);
		iterateNode.setInitial(allInit);
		CLGOperatorNode condition=null;
		if(boundaryAnalysis)
		{
			condition=new CLGOperatorNode("<");
			condition.setLeftOperand(new CLGVariableNode("it","Integer"));
			OperatorExp add=new OperatorExp("+");
			add.setOperand(this.end, new LiteralExp("Integer", "1"));
			condition.setRightOperand(add.AST2CLG(boundaryAnalysis));
		}
		else
		{
		condition=new CLGOperatorNode("<=");
		condition.setLeftOperand(new CLGVariableNode("it","Integer"));
		condition.setRightOperand(this.end.AST2CLG(boundaryAnalysis));
		}
		iterateNode.setCondition(condition);
		CLGConstraint body=this.body.AST2CLG(boundaryAnalysis);
		body.postconditionAddPre();
		iterateNode.setBody(body);
		CLGOperatorNode increment=new CLGOperatorNode("=");
		increment.setLeftOperand(new CLGVariableNode("it","Integer"));
		CLGOperatorNode addone=new CLGOperatorNode("+");
		addone.setLeftOperand(new CLGVariableNode("it","Integer"));
		addone.setRightOperand(new CLGLiteralNode("1","Integer"));
		increment.setRightOperand(addone);
		iterateNode.setIncrement(increment);
		return iterateNode;
	}
	
	@Override
	public String NodeToString()
	{
		return "";
	}
	
	@Override
	public void toGraphViz() //因為每個繼承的物件有不同寫法
	{
		String astInformation=this.ASTInformation();//減少一直呼叫函式
		if(this.start!=null)
		{
			System.out.println(astInformation+"->"+this.start.ASTInformation());
			this.start.toGraphViz();//遞迴
		}
		if(this.end!=null)
		{
			System.out.println(astInformation+"->"+this.end.ASTInformation());
			this.end.toGraphViz();
		}
		if(this.body!=null)
		{
			System.out.println(astInformation+"->"+this.body.ASTInformation());
			this.body.toGraphViz();
		}
	}
	
	@Override
	public void addVariableType(SymbolTable symbolTable,String methodName)
	{
		this.start.addVariableType(symbolTable, methodName);
		this.end.addVariableType(symbolTable, methodName);
		this.body.addVariableType(symbolTable, methodName);
	}
	
	@Override
	public void changeAssignToEqual(){}
	
	@Override
	public void conditionChangeAssignToEqual() {}
	@Override
	public String AST2CLP(String attribute,String method) 
	{
		String clp="iterate"+this.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,Result):-\n";
		clp+="apply(Source,[Obj_pre,Obj_post,Abj_pre,Abj_post,Collection]),\n";
		clp+="apply(AccInitExp,[Obj_pre,Obj_post,Abj_pre,Abj_post,AccInitValue]),\n";
		clp+="iterate(Obj_pre,Arg_pre,Obj_post,Arg_post,Result,Collection,AccInitValue,BodyExp).\n";
		clp+="delay iterate(_,_,_,_,_,Collection,_,_) if var(Collection).\n";
		clp+="iterate(Obj_pre,Arg_pre,Obj_post,Arg_post,AccInitValue,Collection,AccInitValue,_):-\n";
		clp+="length(Collecion,0).\n";
		clp+="iterate(Obj_pre,Arg_pre,Obj_post,Arg_post,Result,Collection,AccInitValue,AccIterPredicate):-\n";
		clp+="(\nforeach(Elem,Collection),\n";
		clp+="fromto(AccInitValuePre,AccPre,AccPost,Result),\n";
		clp+="param(Obj_pre,Arg_pre,Obj_post,Arg_post,AccIterPredicate)\n";
		clp+="do\nappend(Arg_pre,Arg_post,[[AccPre,AccPre],[Elem,Elem]],NewArg_pre,NewArg_post),\n";
		clp+="apply(accIterPredicate,[Obj_pre,Obj_post,Arg_pre,Arg_post,AccPost])).\n";
		return clp;
	}
	
	@Override
	public  void preconditionAddPre() {}
	@Override
	public  void postconditionAddPre() {}
	@Override
	public void demoganOperator()
	{
		
	}
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
