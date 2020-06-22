package ccu.pllab.tcgen.AbstractSyntaxTree;
import java.util.ArrayList;
import java.util.HashMap;

import ccu.pllab.tcgen.ASTGraph.ASTGraphNode;
import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.AbstractConstraint.*;
import scala.reflect.generic.Trees.This;


public class OperationContext extends AbstractSyntaxTreeNode{
	private String className;
	private String methodName;
	private ArrayList<PropertyCallExp> parameters=new ArrayList<PropertyCallExp>();
	private String returnType="";
	private int preNum=0;
	private int postNum=0;
	private ArrayList<StereoType> stereoType=new ArrayList<StereoType>();
	
	
	public ArrayList<PropertyCallExp> getParameters()
	{
		return this.parameters;
	}

	public OperationContext(String className,String methodName)  {
		super();
		this.className=className;
		this.methodName=methodName;
	}
	
	public String getClassName()
	{
		return this.className;
	}
	
	public String getMethodName()
	{
		return this.methodName;
	}
	
	public void setParameters(ArrayList<PropertyCallExp>parameters)
	{
		this.parameters=parameters;
	}
	
	public void setReturnType(String returnType)
	{
		this.returnType=returnType;
	}
	
	public String getReturnType()
	{
		return this.returnType;
	}
	
	public void setStereoType(StereoType stereoType)
	{
		this.stereoType.add(stereoType);
	}
	
	public ArrayList<StereoType> getStereoType()
	{
		return this.stereoType;
	}
	public int  getPreNum() {
		int preNum=0;
		for(StereoType s:this.stereoType)
			if(s.getStereoType().equals("precondition"))
				preNum++;
		return preNum;
	}
	public int  getPostNum() {
		int postNum=0;
		for(StereoType s:this.stereoType)
			if(s.getStereoType().equals("postcondition"))
				postNum++;
		return postNum;
	}
	/*@Override
	public String addOperatorType(){return "";}
	
	/*@Override
	public void addAllNodeType(String type){}
	
	@Override
	public void putAttributeToHashMap(HashMap<String,String> table){}
	
	@Override
	public  void takeHashMapKeyValue(HashMap<String,String> table){}
	
	@Override
	public void addSelfOrResultType(String className){}*/
	
	@Override
	public void addVariableType(SymbolTable symbolTable,String methodName)
	{}
	@Override
	public void changeAssignToEqual(){}
	
	@Override
	public void conditionChangeAssignToEqual() {}
	
	@Override
	public String childNodeInfo(){return "";}
	
	@Override
	public String ASTInformation()
	{
		String nodeName=this.methodName+"(";
		if(this.parameters.size()>0)
		{
			nodeName+=this.parameters.get(0).getVariable()+":"+this.parameters.get(0).getType();
			if(this.parameters.size()>1)
			{
				for(int size=1;size<this.parameters.size();size++)
				{
					PropertyCallExp var=this.parameters.get(size);
					nodeName+=","+var.getVariable()+":"+var.getType();
				}
			}
		}
		nodeName+=")";
		return "\""+"("+this.getID()+")"+nodeName+"\"";
	}
	
	@Override
	public  CLGConstraint AST2CLG(){return null;}
	
	@Override
	public  CLGConstraint AST2CLG(boolean boundaryAnalysis){return null;}
	
	@Override
	public  String NodeToString(){return "";}
	
	@Override
	public final void toGraphViz() 
	{//一個遞迴函式，來畫AST圖
		String astInformation=this.ASTInformation();
		for(StereoType stereo:this.stereoType)
		{
			System.out.println(astInformation+"->"+stereo.ASTInformation());
			stereo.toGraphViz();
		}
	} 
	
	@Override
	public String AST2CLP(String attribute,String argument) 
	{
		String clp=":-lib(ic).\n:-lib(timeout).\n"+this.methodName+"(Obj_pre,Arg_pre,Obj_post,Arg_post,Result):-\n";
		if(attribute.length()>0)
		{
			clp+="["+attribute.replaceAll(",", "_pre,")+"_pre,"+attribute+"]:: -32768..32767,\n";
			clp+="Obj_pre=["+attribute.replaceAll(",", "_pre,")+"_pre],\n";
			clp+="Obj_post=["+attribute+"],\n";
		}
		if(argument.length()>0)
		{
			clp+="["+argument.replaceAll(",", "_pre,")+"_pre,"+argument+"]:: -32768..32767,\n";
			clp+="Arg_pre=["+argument.replaceAll(",", "_pre,")+"_pre],\n";
			clp+="Arg_post=["+argument+"],\n";
		}
		String[] object=attribute.split(",");
		String[] arg=argument.split(",");
		if(attribute.length()>0)
		for(String obj:object)
			clp+=obj.toUpperCase().charAt(0)+obj.substring(1)+"="+obj.toUpperCase().charAt(0)+obj.substring(1)+"_pre,\n";
		if(argument.length()>0)
		for(String var:arg)
			clp+=var.toUpperCase().charAt(0)+var.substring(1)+"="+var.toUpperCase().charAt(0)+var.substring(1)+"_pre,\n";
		clp=clp.substring(0, clp.length()-2)+".\n\n";
		for(StereoType stereo:stereoType)
			clp+=stereo.AST2CLP(attribute, argument);
		/*if(attribute!=null)
		{
			clp+="[";
			String obj="",objPre="";
			for(VariableToken attr:attribute)
				obj+=attr.getVariableName()+",";
			
				objPre=obj.replaceAll(",", "_pre,");
				clp+=objPre+obj;
				clp=clp.substring(0, clp.length()-1);
				clp+="]::-32768..32767,\n";
				clp+="Obj_pre=["+objPre.substring(0, objPre.length()-1)+"],\n";
				clp+="Obj_"
						+ "post=["+obj.substring(0, obj.length()-1)+"],\n";
		}
		if(method.getArgument().size()>0)
		{
			
		String argPost="",argPre="";
		for(VariableToken arg:method.getArgument())
			argPost+=arg.getVariableName()+",";
			argPre=argPost.replace(",", "_pre,");
			clp+="["+argPre+argPost.substring(0,argPost.length()-1)+"]::-32768..32767,\n";
			clp+="Arg_pre=["+argPre.substring(0, argPre.length()-1)+"],\n";
			clp+="Arg_post=["+argPost.substring(0, argPost.length()-1)+"],\n";
		}
		clp+="method(Obj_pre,Arg_pre,Obj_post,Arg_post,Result).\n";
		for(StereoType stereo:stereoType)
			clp+=stereo.AST2CLP(attribute, method);*/
		return clp;
	}
	@Override
	public  void preconditionAddPre() {
		if(this.stereoType.size()>1)
			this.stereoType.get(0).preconditionAddPre();
	}
	@Override
	public  void postconditionAddPre() {
		if(this.stereoType.size()>1)
			this.stereoType.get(1).postconditionAddPre();
		else
			this.stereoType.get(0).postconditionAddPre();
	}
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
