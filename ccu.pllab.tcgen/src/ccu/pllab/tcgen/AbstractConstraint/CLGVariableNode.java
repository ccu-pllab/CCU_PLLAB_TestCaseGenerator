package ccu.pllab.tcgen.AbstractConstraint;


import java.util.ArrayList;

import ccu.pllab.tcgen.exe.main.Main;

public class CLGVariableNode extends CLGConstraint{
	private String type;
	private String name;
	private CLGConstraint constraint;
	private String clp="";
	public CLGVariableNode(){
		super();
		this.type = "";
		this.name = "";
		this.constraint = null;
	}
	public CLGVariableNode(String name){
		super();
		this.type = "";
		this.name = name;
		this.constraint = null;
	}

	public CLGVariableNode(String name ,String type){
		super();
		this.type = type;
		this.name = name;
		this.constraint = null;
	}
	public CLGVariableNode(String name, String type, CLGConstraint constraint) {
		super();
		this.type = type;
		this.name = name;
		this.constraint = constraint;
	}
	public void setConstraint(CLGConstraint constraint) {
		this.constraint = constraint;
	}
	public CLGConstraint getConstraint(){
		return this.constraint;
	}
	public void setType(String type){
		this.type=type;
	}
	public String getType() {
		return this.type;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}

	@Override
	public String getImgInfo() {
		if (this.constraint == null) {
			return this.getName();
		} else {
		//	return this.name + "[" + this.constraint.getImgInfo() + "]";
			return this.name + "." + this.constraint.getImgInfo() ;
		}
	}
	
//	@Override
//	public String getCLPInfo() {
//		String new_name="";
//		new_name+=this.name.toUpperCase().charAt(0);
//		if(this.name.length()>1){
//			new_name+=this.name.substring(1);
//		}
//		if (this.constraint == null) {
//			return new_name;
//		} else {
//			return new_name + "[" + this.constraint.getCLPInfo() + "]";
//		}
//		
//	}
	
	@Override
	public String getCLPInfo() {
		String new_name="";
		if(clp.length()>0)
			return clp;
		/*need  modify*/
		if(this.name.length()>1){
			/*method call*/
			if(this.getName().startsWith("Sequence")) {
				String name1 = this.getName().split("->")[0];
				String name2 = this.getName().split("->")[1];
				String arg1 = name1.substring(9, name1.length()-1).split("\\.\\.")[0];
				String arg2 = name1.substring(9, name1.length()-1).split("\\.\\.")[1];
				
				if (arg1.contains("self@pre.")) {
					arg1= arg1.replaceAll("self@pre.", "");
				//	arg1 = arg1.substring(0, 1).toUpperCase() + arg1.substring(1)+"_0";
					arg1 = arg1.substring(0, 1).toUpperCase() + arg1.substring(1)+"_pre";
				}
				if (arg2.contains("self@pre.")) {
					arg2= arg2.replaceAll("self@pre.", "");
				//	arg2 = arg2.substring(0, 1).toUpperCase() + arg2.substring(1)+"_0";
					arg2 = arg2.substring(0, 1).toUpperCase() + arg2.substring(1)+"_pre";
				}
				
				String method = name2.split("\\(")[0];
				method = method.substring(0, 1).toUpperCase() + method.substring(1);
				String arg = name2.split("\\(")[1];
				if (arg.equals(")")) {
					arg = "(Temp,"+method+")";
				}
				else {
					arg = arg.split("\\)")[0];
					arg = "(Temp,"+arg+","+ method+")";
				}
				
				new_name = method +",\n	newSequence("+ arg1 +","+ arg2 +",Temp),\n	sequence"+method+arg;
			}
			
			/*ocl modify*/
			else {
				//System.out.println("CLGVariable.java Name:"+this.getName());
				if(this.getName().contains("self@pre."))
				{
					new_name= this.getName().replaceAll("self@pre.", "");
					//new_name = new_name.substring(0, 1).toUpperCase() + new_name.substring(1)+"_0";
					new_name = new_name.substring(0, 1).toUpperCase() + new_name.substring(1)+"_pre";
				}
				else if(this.getName().contains("@pre")){
					new_name= this.getName().replaceAll("@pre", "_pre");
					//new_name = new_name.substring(0, 1).toUpperCase() + new_name.substring(1)+"_0";
					new_name = new_name.substring(0, 1).toUpperCase() + new_name.substring(1)+"_pre";
				}
				else if(this.getName().contains("self") && this.constraint!=null) {
					new_name= this.getName().replaceAll("self", "");
					//new_name = new_name.substring(0, 1).toUpperCase() + new_name.substring(1);
				}
						
				else {
					new_name+=this.name.toUpperCase().charAt(0);
					new_name+=this.name.substring(1);
				}
			}
		}
		
		/*do not modify*/
		else {
			if(this.name.length()>0)
			new_name+=this.name.toUpperCase().charAt(0);
		}
		if(new_name.contains("presize") && !new_name.contains("presize_pre"))
		new_name=new_name.replaceAll("presize", "presize_pre");
		if (this.constraint == null) {
			clp=new_name;
			return new_name;
		} 
		else if(new_name.equals(""))
		{
			clp=this.constraint.getCLPInfo() ;
			return clp;
			//return this.constraint.getCLPInfo() ;
		}
		else if(!(this.constraint instanceof CLGOperatorNode)){
			//return new_name + "[" + this.constraint.getCLPInfo() + "]";
			clp=new_name + "_" + this.constraint.getCLPInfo() ;
			return clp;
			//return new_name + "_" + this.constraint.getCLPInfo() ;
		}
		else {
			clp= new_name;
			return new_name;
		}
	}
	
	@Override
	public CLGConstraint clone() {
		if(this.constraint==null){
			CLGConstraint cons=new CLGVariableNode(this.name,this.type,this.constraint);
			cons.setCloneId(this.getConstraintId());
		//return new CLGVariableNode(this.name,this.type,this.constraint);
			return cons;
		}else{
			CLGConstraint newConstrain=this.constraint.clone();
			CLGConstraint cons=new CLGVariableNode(this.name,this.type,newConstrain);
			cons.setCloneId(this.getConstraintId());
			return cons;
			//return new CLGVariableNode(this.name,this.type,newConstrain);
		}
	
	}
	@Override
	public String getCLPValue() {

	/*	if(this.getName().contains("@pre"))
		{
			return  this.getName().replaceAll("@pre", "");
		}*/
		if(this.getName().contains("@"))
		{
			return  this.getName().replaceAll("@", "_");
		}
		/*if(this.getName().contains("self."))
		{
			return  this.getName().replaceAll("self.", "");
		}*/
		if(this.getName().contains("self") &&this.constraint!=null)
		{
			return  this.getName().replaceAll("self", "");
		}
		return this.getName();
	}
	public void setCLPValue(String data){
		this.setName(data);
	}
	
	@Override
	public  ArrayList<String> getInvCLPInfo()
	{
		ArrayList<String> variable=new ArrayList<String>();
		variable.add(this.getCLPInfo());
		return variable;
	}
	
	@Override
	public String getLocalVariable() {
		// TODO Auto-generated method stub
//		if(this.getName().startsWith("IterateAcc") || this.getName().startsWith("IterateIndex")) {
//			return this.getName();
//		}
		if(this.getName().toLowerCase().equals("it")) {
			return "It";
		}
		else if(this.getName().toLowerCase().equals("acc")) {
			return "Acc";
		}
		else {
			return "";
		}
	}
	@Override
	public void negConstraint() {
	}
	
	@Override
	public void preconditionAddPre()
	{
		if(this.constraint==null)
		{
			if(!this.name.contains("_pre") && (Main.symbolTable.getArgumentMap().get(this.name)!=null ||Main.symbolTable.getAttributeMap().get(this.name)!=null))
				this.name+="_pre";
		}
		else
			this.constraint.preconditionAddPre();
	}
	@Override
	public void postconditionAddPre()
	{
		if(this.constraint!=null)
			this.constraint.postconditionAddPre();
		else
		{
			if(!this.name.contains("_pre")&& (Main.symbolTable.getArgumentMap().get(this.name)!=null ||Main.symbolTable.getAttributeMap().get(this.name)!=null))
				this.name+="_pre";
		}
	}
}
