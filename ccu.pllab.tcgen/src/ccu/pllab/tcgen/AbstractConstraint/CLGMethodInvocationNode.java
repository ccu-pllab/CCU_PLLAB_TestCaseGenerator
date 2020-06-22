package ccu.pllab.tcgen.AbstractConstraint;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ccu.pllab.tcgen.exe.main.Main;

public class CLGMethodInvocationNode extends CLGConstraint {

	private String methodObject;
	private String methodName;
	private ArrayList<String> methodArgument;

	public CLGMethodInvocationNode(String methodObject, String methodName, ArrayList<String> methodArgument) {
		super();
		this.methodArgument = new ArrayList<String>();
		this.methodObject = methodObject;
		this.methodName = methodName;
		this.methodArgument.addAll(methodArgument);
	}
	public CLGMethodInvocationNode(String methodName, ArrayList<String> methodArgument) {
		super();
		this.methodArgument = new ArrayList<String>();
		this.methodObject = "this";
		this.methodName = methodName;
		this.methodArgument.addAll(methodArgument);
	}

	public CLGMethodInvocationNode(String methodName, String methodArgument) {
		super();
		this.methodArgument = new ArrayList<String>();
		this.methodObject = "this";
		this.methodName = methodName;
		this.methodArgument.add(methodArgument);
	}
	public CLGMethodInvocationNode(String methodObject, String methodName, String methodArgument) {
		super();
		this.methodArgument = new ArrayList<String>();
		this.methodObject = methodObject;
		this.methodName = methodName;
		this.methodArgument.add(methodArgument);
	}

	public String getMethodObject() {
		return this.methodObject;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public ArrayList<String> getMethodArgument() {
		return (ArrayList<String>) this.methodArgument;
	}

	
	@Override
	public String getImgInfo() {
		ArrayList<String> methodArgList = new ArrayList<String>();
		methodArgList = this.methodArgument;
		String methodArgVars="";
		for (int i = 0; i < methodArgList.size(); i++) {
			methodArgVars +=methodArgList.get(i);
			if (i < methodArgList.size() - 1) {
				methodArgVars +=",";
			}
		}
		methodArgVars=methodArgVars.replaceAll("_pre", "");
		if(this.methodObject==null)
			return  this.getMethodName() + "(" + methodArgVars + ")";
		return this.getMethodObject() + "." + this.getMethodName() + "(" + methodArgVars + ")";
	}


	@Override
	public String getCLPInfo() {
		String content = "";
		/*if(this.methodObject==null)
			content+= this.methodName;
		else
		content += //this.methodObject+"_"+
					this.methodName;
		content += "(";
		if(!this.methodArgument.isEmpty()){			
			for(int i =0; i <this.methodArgument.size();i++){
				content +=this.methodArgument.get(i);
				if( !(i == this.methodArgument.size()-1)){
					content+=",";
				}
			}			
		}
		content+=")";*/
		//File method=new File("..\\example\\methodCLP\\"+this.methodName+".txt");
		/*FileReader fr=null;
		boolean notfile=false;
		try {
			fr = new FileReader("..\\examples\\methodCLP\\"+this.methodName+".txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			notfile=true;
		} 
		if(notfile)
		{*/
		if(this.methodName.equals("gcd"))
		{
			content+="methodo_mod(M,N,R),\r\n" + 
					"M_1=N_pre,\r\n" + 
					"N_1=R,\r\n" + 
					"N_1#=0,\r\n" + 
					"N_1#=0,\r\n" + 
					"Result=M_1";
		}
		else if(this.methodName.equals("mod"))
		{
			content+="o_mod(M,N,R)";
		}
		else {
			ArrayList<String> content2=new ArrayList<String>();
			String returnString="";
			for(String arg :this.methodArgument)
			{
				if(arg.contains("["))
				{
					content2.add("Arrayi");
					returnString+="Index#=";
					returnString+=arg.substring(arg.indexOf("[")+1, arg.indexOf("]"));//.replace("it", "It");
					returnString+=",\nfindelement("+arg.toUpperCase().charAt(0)+arg.substring(1,arg.indexOf("["))+",Arrayi,Index),\n";
				}
				else
					content2.add(arg);
			}
			for(String arg :content2)
			{
				content+=","+arg.toUpperCase().charAt(0)+arg.substring(1);
			}
			if(content!="")
				content=content.substring(1);
			content=content.replace("@pre", "_pre");
			content=returnString+this.methodName+"("+content+")";
		}
			//System.out.println("test..."+content);
		//}
	/*	else {
        BufferedReader br = new BufferedReader(fr);
        String line;
        try {
			while((line = br.readLine())!=null)
			{
				content+=line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}*/
		return content;
	}

	@Override
	public  ArrayList<String> getInvCLPInfo()
	{
		return null;
	}
	
	@Override
	public CLGConstraint clone() {
		CLGConstraint cons=new CLGMethodInvocationNode(this.methodObject, this.methodName, this.methodArgument);
		cons.setCloneId(this.getConstraintId());
		return cons;
	//	return new CLGMethodInvocationNode(this.methodObject, this.methodName, this.methodArgument);
	}

	@Override
	public String getCLPValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCLPValue(String data) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getLocalVariable() {
		// TODO Auto-generated method stub
		return "";
	}
	@Override
	public void negConstraint() {}
	@Override
	public void preconditionAddPre(){}
	@Override
	public void postconditionAddPre(){
		ArrayList<String> methodArgument2=new ArrayList<String>();
		for(String arg:this.methodArgument)
		{
			if((Main.symbolTable.getAttribute().contains(arg) ||Main.symbolTable.getArgument().contains(arg))&& !arg.contains("@pre"))
				methodArgument2.add(arg+"_pre");
				else
					methodArgument2.add(arg);
		}
		this.methodArgument=methodArgument2;
	}
}
