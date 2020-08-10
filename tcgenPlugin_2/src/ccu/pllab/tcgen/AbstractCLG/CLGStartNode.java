package ccu.pllab.tcgen.AbstractCLG;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ccu.pllab.tcgen.libs.Predicate;

public class CLGStartNode extends CLGNode {

	private String className;
	private String methodName;
	private String retType;
	private boolean isConstructor;
	private ArrayList<String> classAttributes;
	private ArrayList<String> methodParameters;
	private ArrayList<String> methodParametertypes;//我加
	
	public CLGStartNode() {
		super();
		this.retType="";
		this.isConstructor=false;
		classAttributes = new ArrayList<String>();
		methodParameters = new ArrayList<String>();
		methodParametertypes = new ArrayList<String>();
	}
	
	public CLGStartNode(String className, String methodName) {
		super();
		this.retType="";
		this.isConstructor=false;
		this.className = className;
		this.methodName = methodName;
		classAttributes = new ArrayList<String>();
		methodParameters = new ArrayList<String>();
		methodParametertypes = new ArrayList<String>();
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public void setRetType(String retType){
		this.retType=retType;
	}
//	public void setClassAttributes(ArrayList<String> attributeList) {
//		classAttributes = attributeList;
//	}
	public void setClassAttributes(ArrayList<String> attributeList) {
		for(String attribute :attributeList){
			classAttributes.add(attribute);
		}
	}

//	public void setMethodParameters(ArrayList<String> parameters){
//		methodParameters=parameters;
//	}
	public void setMethodParameters(ArrayList<String> parameters){
		for(String arg :parameters){
			methodParameters.add(arg);
		}
	}
	
	public void setMethodParameterTypes(ArrayList<String> parametertypes){
		for(String arg :parametertypes){
			methodParametertypes.add(arg);
		}
	}
	
	public void setIsConstructor(boolean isConstructor){
		this.isConstructor=isConstructor;
	}
	public String getClassName() {
		return this.className;
	}
	public String getFirstUpperClassName(){
		return this.className.toUpperCase().charAt(0) + this.className.substring(1);
	}
	public String getMethodName() {
		return this.methodName;
	}
	public String getRetType() {
		return this.retType;
	}
	public String getFirstUpperMethodName(){
		return this.methodName.toUpperCase().charAt(0) + this.methodName.substring(1);
	}
	public ArrayList<String> getClassAttributes(){
		return this.classAttributes;
	}
	public ArrayList<String> getMethodParameters(){
		return this.methodParameters;
	}
	public ArrayList<String> getMethodParameterTypes(){
		return this.methodParametertypes;
	}
	public String getGraphName(){
		return this.getFirstUpperClassName()+this.getFirstUpperMethodName();
	}
	
	@Override
	public String toGetImgInfo() {
		String result = "";
		result += String.format("digraph \"%s_%s\" {\n", this.getClassName(), this.getMethodName());
		result += (this.getId() + " "
				+ "[style=filled, fillcolor=black, shape=\"circle\", label=\"\", fixedsize=true, width=.2, height=.2, xlabel=\"[0]\"]\n");
		return result;
	}

	@Override
	public String toCLPInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	public String stateAssignEqual() {
		String content = "";
		for (int i = 0; i < classAttributes.size(); i++) {
			String new_preVar = classAttributes.get(i).toUpperCase().charAt(0) + classAttributes.get(i).substring(1);
			//content += ",\n" + new_preVar + "#=" + new_preVar + "_0";
			content += ",\n" + new_preVar + "#=" + new_preVar + "_pre";
		}
		return content;
	}

	public String toString(){
		return "(CLGStartNode)";
	}
	
	public boolean isConstructor(){
		return this.isConstructor;
	}
	public String getReturnType(){
		return this.retType;
	}
	
	public String OCL2CLP() {
		String CLP="";
		ArrayList<ArrayList<String>> clp = new ArrayList();
		ArrayList<ArrayList<String>> localParameters = new ArrayList();
		clp = this.genMethodCLP("", "", classAttributes, methodParameters, localParameters,"");
		
		//Sort
		Collections.sort(clp, new Comparator<ArrayList<String>>(){
            public int compare(ArrayList a1, ArrayList a2) {
            	String num1 = (String) a1.get(0);
            	String num2 = (String) a2.get(0);
                return num1.compareTo(num2);
            }
        });
		
		//change type to String
		for(int i = 0; i < clp.size(); i++) {
			for(int j = 1; j < clp.get(i).size(); j++) {
				CLP = CLP + clp.get(i).get(j); 
			}
		}
		//File
		try {
			File dir = new File("examples/"+this.getClassName()+"CLP"); 
			dir.mkdir();
			FileWriter dataFile = new FileWriter("examples/"+this.getClassName()+"CLP/"+this.getClassName()+this.getMethodName().substring(0, 1).toUpperCase() + this.getMethodName().substring(1)+".ecl");
			BufferedWriter input = new BufferedWriter(dataFile);
			input.write(CLP);
			input.close();
		}
		catch (Exception e) {
		}	
		return CLP;
	}
	
	@Override
	public ArrayList genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters, ArrayList localParameters, String result) {
		ArrayList attributes_pre = new ArrayList();
		ArrayList attributes_post = new ArrayList();
		ArrayList arg_pre = new ArrayList();
		ArrayList arg_post = new ArrayList();
		String return_value="Result";
		ArrayList<ArrayList<String>> clp = new ArrayList();
		
		
//		System.out.println("---------------"+this.className);
//		System.out.println("---------------"+this.methodName);
		if(this.className!= null)
			className = this.className.substring(0, 1).toLowerCase() + this.className.substring(1);
		if(this.methodName != null)	
			methodName = this.methodName.substring(0, 1).toUpperCase() + this.methodName.substring(1);
		
		
		for(int i = 0; i < this.classAttributes.size(); i++) {
			attributes_pre.add(this.classAttributes.get(i).substring(0, 1).toUpperCase() + this.classAttributes.get(i).substring(1)+"_pre");
			attributes_post.add(this.classAttributes.get(i).substring(0, 1).toUpperCase() + this.classAttributes.get(i).substring(1)) ;
		}
		for(int j = 0; j < this.methodParameters.size(); j++) {
			arg_pre.add(this.methodParameters.get(j).substring(0, 1).toUpperCase() + this.methodParameters.get(j).substring(1)+"_pre");
			arg_post.add(this.methodParameters.get(j).substring(0, 1).toUpperCase() + this.methodParameters.get(j).substring(1)) ;
		}
		
		/*modify non primitive type return value*/
		if(this.getReturnType() == null || this.getReturnType().equals("") || this.getReturnType().equals("String") || this.getReturnType().equals("Boolean") || this.getReturnType().equals("Integer")) {
			return_value="Result";
		}
		else if(this.getReturnType().equals(this.className)){
			ArrayList returnValue = new ArrayList();
			for(int k = 0; k < this.classAttributes.size(); k++) {
				returnValue.add("Result_"+this.classAttributes.get(k).substring(0, 1).toUpperCase() + this.classAttributes.get(k).substring(1)) ;
			}
			return_value = returnValue.toString();
		}
		
		
		
		clp.add(new ArrayList());
		clp.get(0).add("0");
		clp.get(0).add(":- lib(ic).\r\n:- lib(timeout).\r\n\n");
		//constructor 沒有 attribute pre
		if (className.toLowerCase().equals(methodName.toLowerCase())) {
			clp.get(0).add(className + methodName +"(ArgPre, ObjPost, ArgPost, Result, Exception):- \n");
			clp.get(0).add("	"+className + methodName +"_startNode(ArgPre, ObjPost, ArgPost, Result, Exception). \n");
			clp.get(0).add(className + methodName +"_startNode("+ arg_pre +","+ attributes_post +","+ arg_post +", "+ return_value +", Exception):- \n");
		}
		else {
			clp.get(0).add(className + methodName +"(ObjPre, ArgPre, ObjPost, ArgPost, Result, Exception):- \n");
			clp.get(0).add("	"+className + methodName +"_startNode(ObjPre, ArgPre, ObjPost, ArgPost, Result, Exception). \n");
			clp.get(0).add(className + methodName +"_startNode("+ attributes_pre +","+ arg_pre +","+ attributes_post +","+ arg_post +", "+ return_value +", Exception):- \n");
		}
		clp.get(0).add(fix());
		if(this.getSuccessor().get(0).getClass().equals(CLGConnectionNode.class)) {
			clp.get(0).add("	"+ className + methodName + "_node_" +((CLGConnectionNode)this.getSuccessor().get(0)).getConnectionId()+"("+attributes_pre +","+ arg_pre +","+ attributes_post +","+ arg_post+", "+ return_value+", Exception, "+ localParameters +"). \n");
		}
		clp.addAll(this.getSuccessor().get(0).genMethodCLP(className, methodName, attributes_post, arg_post, localParameters, return_value));
		return clp;
	}
	
	public String fix() {
		String fix="";
		switch (this.methodName) {
			case "push":
				fix = "	Size#=(Size_pre+1),\r\n" + 
					  "	length(Data,Size),\n";
				break;
			case "enqueue":
				fix = "	Size#=(Size_pre+1),\r\n" + 
					  "	length(Data,Size),\n";
				break;
			case "pop":
				fix = "	Size#=(Size_pre-1),\r\n" + 
					  "	length(Data,Size),\n";
				break;
			case "dequeue":
				fix = "	Size#=(Size_pre-1),\r\n" + 
					  "	length(Data,Size),\n";
				break;
			default:
				fix = "";
		}
		return fix;
	}
}

	
//	@Override
//	public String genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters) {
//		String CLP = "";
//		ArrayList attributes_pre = new ArrayList();
//		ArrayList attributes_post = new ArrayList();
//		ArrayList arg_pre = new ArrayList();
//		ArrayList arg_post = new ArrayList();
//		if(this.className!= null)
//			className = this.className.toLowerCase();
//		if(this.methodName != null)
//			methodName = this.methodName.toLowerCase();
//
//		for(int i = 0; i < this.classAttributes.size(); i++) {		
//			attributes_pre.add(this.classAttributes.get(i).substring(0, 1).toUpperCase() + this.classAttributes.get(i).substring(1)+"_0");
//			attributes_post.add(this.classAttributes.get(i).substring(0, 1).toUpperCase() + this.classAttributes.get(i).substring(1)) ;
//		}
//		for(int j = 0; j < this.methodParameters.size(); j++) {		
//			arg_pre.add(this.methodParameters.get(j).substring(0, 1).toUpperCase() + this.methodParameters.get(j).substring(1)+"_0");
//			arg_post.add(this.methodParameters.get(j).substring(0, 1).toUpperCase() + this.methodParameters.get(j).substring(1)) ;
//		}
//		
//		CLP = CLP + className + "_" + methodName +"("+ attributes_pre +","+ arg_pre +","+ attributes_post +","+ arg_post +", Result):- \n";
//		if(this.getSuccessor().get(0).getClass().equals(CLGConnectionNode.class)) {
//			CLP = CLP + "	"+ className + "_" + methodName + "_node_" +((CLGConnectionNode)this.getSuccessor().get(0)).getConnectionId()+"("+attributes_pre +","+ arg_pre +","+ attributes_post +","+ arg_post+", Result). \n\n";
//		}
//		CLP = CLP + this.getSuccessor().get(0).genMethodCLP(className, methodName, attributes_post, arg_post);
//		return CLP;
//	}
//}
