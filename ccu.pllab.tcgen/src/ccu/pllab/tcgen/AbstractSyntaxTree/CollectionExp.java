package ccu.pllab.tcgen.AbstractSyntaxTree;


import java.util.ArrayList;

import ccu.pllab.tcgen.ASTGraph.ASTGraphNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGCollectionNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;

public class CollectionExp extends AbstractSyntaxTreeNode{
	private String type;
	private AbstractSyntaxTreeNode start;
	private AbstractSyntaxTreeNode end;
	private int counting=1;
	private String acc;
	private String acc_type;
	private AbstractSyntaxTreeNode body;
	private String collectionName;
	private String methodName;
	
	public CollectionExp(String type)
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
	public void setMethodName(String methodName) {
		this.methodName=methodName;
	}
	
	public String getType()
	{
		return this.type;
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
		String method="";
		if(this.methodName.length()>0)
			method="."+this.methodName;
		return "\""+this.getID()+")"+this.type+method+"\"";
	}
	
	@Override
	public CLGConstraint AST2CLG()
	{
		CLGConstraint constraint=null;
		if(this.methodName.length()>0)
		{
			if(this.methodName.equals("size()"))
			{
				constraint=new CLGVariableNode("size_pre","String");
			}
		}
		
		return constraint;
	}
	
	
	@Override
	public CLGConstraint AST2CLG(boolean boundaryAnalysis)
	{
		CLGConstraint constraint=null;
		if(this.methodName.length()>0)
		{
			if(this.methodName.equals("size()"))
			{
				constraint=new CLGVariableNode("size_pre","String");
			}
		}
		return constraint;
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
	public String AST2CLP(String attribute,String method) {return "";}
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
