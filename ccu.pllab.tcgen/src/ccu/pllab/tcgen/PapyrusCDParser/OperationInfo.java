package ccu.pllab.tcgen.PapyrusCDParser;


import java.util.ArrayList;

public class OperationInfo {
	private String name ;
	private ArrayList<VariableInfo> parameter ;
	private VariableInfo return_Type ;
	private String id ;
	private String visibility ;
	private String className ;  // 所屬類別
	
	public OperationInfo() {
		name="";
		parameter=null;
		return_Type=null;
	}
	
	public void setName(String name) {
		this.name = name ;
	}
	
	public void setParameter(ArrayList<VariableInfo> p) {
		parameter = p ;
	}
	
	public void setReturnType(VariableInfo rt) {
		return_Type = rt ;
	}
	
	public void setID( String id ) {
		this.id = id ;
	}
	
	public void setVisibility( String visibility ) {
		this.visibility = visibility ;
	}
	
	public void setClassName( String c_name ) {
		this.className = c_name ;
	}
	
	
	public String getName() {
		return name ;
	}
	
	public ArrayList<VariableInfo> getParameter(){
		return parameter ;
	}
	
	public VariableInfo getReturnType() {
		return return_Type;
	}

	public String getID() {
		return id ;
	}
	
	public String getVisibility() {
		return visibility ;
	}
	
	public String getClassName() {
		return this.className ;
	}
	
}
