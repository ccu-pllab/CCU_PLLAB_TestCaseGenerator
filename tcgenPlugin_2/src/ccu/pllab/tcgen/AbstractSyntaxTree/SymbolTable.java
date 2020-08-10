package ccu.pllab.tcgen.AbstractSyntaxTree;


import ccu.pllab.tcgen.AbstractCLG.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.parse.ANTLRParser.ruleReturns_return; 
public class SymbolTable {
	private String className;
	private ArrayList<VariableToken> attribute;
	private ArrayList<MethodToken> method;
	private ArrayList<VariableToken> argument;
	private HashMap<String,String> attributeMap=new HashMap<String,String>();//Name,type
	private HashMap<String,String> argumentMap=new HashMap<String,String>();
	
	
	public SymbolTable(String calssName)
	{
		this.className=className;
		attribute=new ArrayList<VariableToken>();
		method=new ArrayList<MethodToken>();
		argument=new ArrayList<VariableToken>();
	}
	
	public void addAttribute(VariableToken variable)
	{
		attribute.add(variable);
	}
	
	public void addMethod(MethodToken method)
	{
		this.method.add(method);
	}
	public void addArgument(ArrayList<VariableToken> argument)
	{
		this.argument.addAll(argument);
	}
	
	public void addAttributeMap(VariableToken attribute)
	{
		this.attributeMap.put(attribute.getVariableName(),attribute.getType());
	}
	
	public void addArgumentMap(VariableToken argument)
	{
		this.argumentMap.put(argument.getVariableName(),argument.getType());
	}
	
	public HashMap<String,String> getAttributeMap()
	{
		return this.attributeMap;
	}
	
	public HashMap<String,String> getArgumentMap()
	{
		return this.argumentMap;
	}
	
	public ArrayList<VariableToken> getAttribute()
	{
		return this.attribute;
	}
	public String getClassName()
	{
		return this.className;
	}
	public ArrayList<MethodToken> getMethod()
	{
		return this.method;
	}
	public ArrayList<VariableToken> getArgument()
	{
		return this.argument;
	}
	/*public void setAttribute(String attribute,String type)
	{
		this.attribute.put(attribute, type);
	}
	
	public void setMethodReturn(String methodName,String returnType)
	{
		this.methodReturn.put(methodName,returnType);
	}
	
	public void setMethodArg(String methodName,HashMap<String, String>arg)
	{
		this.methodArg.put(methodName,arg);
	}
	
	
	public HashMap<String, String> getAttribute()
	{
		return this.attribute;
	}
	public HashMap<String, String> getMethodReturn()
	{
		return this.methodReturn;
	}
	public HashMap<String, String> getMethodArg(String method)
	{
		return this.methodArg.get(method);
	}*/
}
