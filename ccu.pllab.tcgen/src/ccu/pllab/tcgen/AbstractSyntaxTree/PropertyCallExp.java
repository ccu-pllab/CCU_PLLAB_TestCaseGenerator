package ccu.pllab.tcgen.AbstractSyntaxTree;
import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;
import org.apache.commons.io.output.ThresholdingOutputStream;

import ccu.pllab.tcgen.ASTGraph.ASTGraphNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.*;
import ccu.pllab.tcgen.exe.main.Main;
import ccu.pllab.tcgen.oclRunner.OclParser.ReturnTypeContext;
import scala.reflect.generic.Trees.This;
public class PropertyCallExp extends AbstractSyntaxTreeNode{
//result.hour or self.hour
	private String type="";
	private String variable;//pathname
	private boolean timeExpression=false;
	private ArrayList<AbstractSyntaxTreeNode> qualifier;
	private ArrayList<AbstractSyntaxTreeNode> qualifier2;
	private ArrayList<AbstractSyntaxTreeNode> parameters;
	
	
	
	public PropertyCallExp(String variable)
	{
		super();
		this.variable=variable;
		if(this.variable.equals("mod")||this.variable.equals("div"))
		this.type="Integer";
	}

	
	public void setType(String type)
	{
		this.type=type;
	}
	
	public String getType()
	{
		return this.type;
	}
	public void setVariable(String variable)
	{
		this.variable=variable;
	}
	public String getVariable()
	{
		return this.variable;
	}
	
	public void setTimeExpression()
	{
		this.timeExpression=true;
	}
	
	public boolean getTimeExpression()
	{
		return this.timeExpression;
	}
	
	public void setQualifier(ArrayList<AbstractSyntaxTreeNode> qualifier)
	{
		this.qualifier=qualifier;
	}
	public void setQualifier2(ArrayList<AbstractSyntaxTreeNode> qualifier)
	{
		this.qualifier2=qualifier;
	}
	public ArrayList<AbstractSyntaxTreeNode> getQualifier()
	{
		return this.qualifier;
	}
	
	public void setParameters(ArrayList<AbstractSyntaxTreeNode> parameters)
	{
		this.parameters=new ArrayList<AbstractSyntaxTreeNode>();
		this.parameters=parameters;
	}
	
	public void addParameters(AbstractSyntaxTreeNode ...parameters)
	{
		if(this.parameters==null)
			this.parameters=new ArrayList<AbstractSyntaxTreeNode>();
		for(AbstractSyntaxTreeNode parameter:parameters)
			this.parameters.add(parameter);
	}
	
	public ArrayList<AbstractSyntaxTreeNode> getParameters()
	{
		return this.parameters;
	}
	
	@Override
	public void addVariableType(SymbolTable symbolTable,String methodName)
	{
		if(this.variable.equals("self"))
			this.type=symbolTable.getClassName();
		else
		{
			if(this.parameters!=null)
			{
				//HashMap<String, String> method=symbolTable.getMethodReturn();
				ArrayList<MethodToken> method=symbolTable.getMethod();
				for(MethodToken token:method)
				{
					if(token.getMethodName().equals(this.variable))
					{
						this.type=token.getReturnType();
						break;
					}
				}
			/*	if(method.containsKey(this.variable))
				{
					this.type=method.get(this.variable);
				}*/
			}
			else{//如果是屬性的token或是參數的token
				ArrayList<VariableToken> attribute=symbolTable.getAttribute();
				ArrayList<MethodToken> method=symbolTable.getMethod();
				if(attribute!=null && attribute.contains(this.variable))
				{
					for(VariableToken variable:attribute)
						if(variable.getVariableName().equals(this.variable))
						{
							this.type=variable.getType();
						}
				}
				else
				{
					for(MethodToken token:method)
					{
						for(VariableToken variable:token.getArgument())
						{
							if(variable.getVariableName().equals(this.variable))
								this.type=variable.getType();
						}
					}
				}
				/*HashMap<String, String> attribute=symbolTable.getAttribute();
				HashMap<String, String> arg=symbolTable.getMethodArg(methodName);
				if(attribute!=null && attribute.containsKey(this.variable))
				{
					this.type=attribute.get(this.variable);
				}
				else if(arg!=null && arg.containsKey(this.variable))
				{
					this.type=arg.get(this.variable);
				}*/
			}
		}
	}
	
	@Override
	public void changeAssignToEqual(){}
	
	@Override
	public void conditionChangeAssignToEqual() {}
	
	@Override
	public String childNodeInfo()
	{
		String info=this.variable;
		if(this.qualifier!=null)
			info+=this.qualifier.get(0).childNodeInfo();
		return info;
	}
	
	@Override
	public String ASTInformation()
	{
		String astInformation=variable;
		if(this.timeExpression)
			astInformation+="@pre";
		if(this.qualifier!=null && this.qualifier.size()>0)
		 astInformation+="["+this.qualifier.get(0).NodeToString()+"]";
		if(this.qualifier2!=null && this.qualifier2.size()>0)
			 astInformation+="["+this.qualifier2.get(0).NodeToString()+"]";
		if(this.parameters!=null&&this.parameters.size()>0)
			astInformation+="()";
		return "\""+"("+this.getID()+")"+astInformation+"\"";
	}
	
	@Override
	public  CLGConstraint AST2CLG()
	{
		String name=this.variable;
		CLGConstraint constraint=null;
		if(this.parameters!=null)
		{
			ArrayList<String> argument=new ArrayList<String>();
			for(int parameter=0;parameter<this.parameters.size();parameter++)
				argument.add(this.parameters.get(parameter).NodeToString());
			
			constraint=new CLGMethodInvocationNode(Main.className,name,argument);
		}
		else
		{
			
			if(this.timeExpression)
					name+="_pre";//
			if(this.qualifier!=null)
			{
				name+="["+this.qualifier.get(0).NodeToString()+"]";
				this.type="Set";
			}
			if(this.qualifier2!=null)
			{
				name+="["+this.qualifier2.get(0).NodeToString()+"]";
				this.type="Set";
			}
			if(name.contains("@"))
			{
			name=name.replaceAll("@", "");
			name=name.replaceAll("->size", "size_pre");
			}
			name=name.replaceAll("sizepre", "size_pre"); //2019/05/22
			constraint=new CLGVariableNode(name,this.type);
			
		}
		return constraint;
	}
	
	@Override
	public  CLGConstraint AST2CLG(boolean boundaryAnalysis)
	{
		String name=this.variable;
		CLGConstraint constraint=null;
		if(this.parameters!=null)
		{
			ArrayList<String> argument=new ArrayList<String>();
			for(int parameter=0;parameter<this.parameters.size();parameter++)
				argument.add(this.parameters.get(parameter).NodeToString());
			
			constraint=new CLGMethodInvocationNode(Main.className,name,argument);
		}
		else
		{
			
			if(this.timeExpression)
					name+="_pre";//
			if(this.qualifier!=null)
			{
				name+="["+this.qualifier.get(0).childNodeInfo()+"]";
				this.type="Set";
			}
			constraint=new CLGVariableNode(name,this.type);
			
		}
		return constraint;
	}
	
	@Override
	public  String NodeToString()
	{
		String name=this.variable;
		if(this.qualifier!=null)
			name+="["+this.qualifier.get(0).childNodeInfo()+"]";
		if(this.timeExpression)
			name+="@pre";
		
			
		return name;
	}
	
	@Override
	public void toGraphViz()
	{
		int size=0;
		if(this.parameters!=null&&this.parameters.size()>=0)
		size=this.parameters.size();
		for(int parameter=0;parameter<size;parameter++)
		{
			System.out.println(this.ASTInformation()+"->"+this.parameters.get(parameter).ASTInformation());
			this.parameters.get(parameter).toGraphViz();
		}
	}
	public PropertyCallExp clone()
	{
		PropertyCallExp property=new PropertyCallExp(this.variable);
		property.setParameters(this.parameters);
		return property;
	}
	@Override
	public String AST2CLP(String attribute,String argument) {
		String obj_pre="",arg_pre="";
		if(attribute.length()>0)
			obj_pre=attribute.replaceAll(",", "_pre,")+"_pre";
		if(argument.length()>0)
			arg_pre=argument.replaceAll(",", "_pre,")+"_pre";
		String clp="variable_"+this.getID()+"(Obj_pre,Arg_pre,Obj_post,Arg_post,Result):-\n"
				+"Result="+this.variable.toUpperCase().charAt(0);
		if(this.variable.length()>1)
			clp+=this.variable.substring(1);
		if(this.timeExpression)
			clp+="_pre";
		return clp+".\n\n";
		}
	@Override
	public String demonganAST2CLP(String attribute,String argument) {return "";}
	@Override
	public  void preconditionAddPre() {
		if(this.qualifier==null && this.parameters==null)
		this.timeExpression=true;
	}
	@Override
	public  void postconditionAddPre() {
		if(this.qualifier==null && this.parameters==null)
		this.timeExpression=true;
	}
	@Override
	public void demoganOperator(){}
	@Override
	public ASTGraphNode AST2ASTGraph() {return null;}
	@Override
	public AbstractSyntaxTreeNode ASTclone()
	{
		PropertyCallExp propertyCallExp=new PropertyCallExp(this.variable);
		propertyCallExp.setType(this.type);
		propertyCallExp.setParameters(this.parameters);
		propertyCallExp.setQualifier(this.qualifier);
		if(this.timeExpression)
		propertyCallExp.setTimeExpression();
		return propertyCallExp;
	}
}
