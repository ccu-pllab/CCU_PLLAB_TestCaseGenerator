package ccu.pllab.tcgen.AbstractConstraint;


import java.util.ArrayList;

public abstract class CLGConstraint {
	private static int constraint_count=0;
	private String constraint_id;
	private String clone_id;
	public CLGConstraint(){
		this.constraint_id = "c"+constraint_count++;
	}
	public String getConstraintId(){
		return this.constraint_id;
	}
	public void setCloneId(String conId)
	{
		this.clone_id=conId;
	}
	public String getCloneId()
	{
		return this.clone_id;
	}
	public abstract String getImgInfo();
	public abstract String getCLPInfo();
	public abstract ArrayList<String> getInvCLPInfo();
	public abstract CLGConstraint clone();
	public abstract String getCLPValue();
	public abstract String getLocalVariable();
	public abstract void setCLPValue(String data);
	public abstract void negConstraint();
	public String getConstratinImg(){
		String result ="";
		result += (this.getConstraintId() + " " + String.format("[shape=\"ecllipse\", label=\"%s\",style = \"filled\",fillcolor = \"white\",xlabel=\"[%s]\"]"+ "\n",this.getImgInfo(),this.getConstraintId()));
		return result;
	}
	
	
	public abstract void preconditionAddPre();
	public abstract void postconditionAddPre();
	
}