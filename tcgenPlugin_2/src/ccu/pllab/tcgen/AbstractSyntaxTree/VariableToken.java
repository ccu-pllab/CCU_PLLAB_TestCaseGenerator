package ccu.pllab.tcgen.AbstractSyntaxTree;


public class VariableToken {
	String variableName;
	String type;
	String lowerValue;
	String highValue;
	
	public VariableToken(String name,String type) {
		// TODO Auto-generated constructor stub
		this.variableName=name;
		this.type=type;
		this.lowerValue="1";
		this.highValue="1";
	}
	
	public String getVariableName()
	{
		return this.variableName;
	}
	
	public String getType()
	{
		return this.type;
	}
}
