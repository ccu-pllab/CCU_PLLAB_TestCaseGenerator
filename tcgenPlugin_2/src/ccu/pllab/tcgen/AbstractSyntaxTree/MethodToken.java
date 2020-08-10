package ccu.pllab.tcgen.AbstractSyntaxTree;


import java.util.ArrayList;

public class MethodToken {
	String methodName;
	ArrayList<VariableToken> argument=new ArrayList<VariableToken>();
	String returnType;
	
	public MethodToken(String name) {
		// TODO Auto-generated constructor stub
		this.methodName=name;
	}
	
	public void setReturnType(String type)
	{
		this.returnType=type;
	}
	public void addArgument(VariableToken variable)
	{
		this.argument.add(variable);
	}
	
	public String getMethodName()
	{
		return this.methodName;
	}
	public String getReturnType()
	{
		return this.returnType;
	}
	public ArrayList<VariableToken> getArgument()
	{
		return this.argument;
	}
}
