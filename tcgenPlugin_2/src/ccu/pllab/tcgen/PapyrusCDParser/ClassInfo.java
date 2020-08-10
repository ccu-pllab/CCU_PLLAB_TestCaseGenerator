package ccu.pllab.tcgen.PapyrusCDParser;


import java.util.ArrayList;

public class ClassInfo {
	private String name ;
	private String id;
	private ArrayList<VariableInfo> properties;
	private ArrayList<OperationInfo> operations;
	
	public ClassInfo() {
		name = "";
		id = "";
		properties = null;
		operations = null;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setID( String id ) {
		this.id = id ;
	}
	
	public void setProperties(ArrayList<VariableInfo> p) {
		properties = p ;
	}
	
	public void setOperations(ArrayList<OperationInfo> o) {
		operations = o ;
	}
	
	public String getName() {
		return name;
	}
	
	public String getID() {
		return id ;
	}
	
	public ArrayList<VariableInfo> getProperties() {
		return properties;
	}
	
	public ArrayList<OperationInfo> getOperations() {
		return operations;
	}
	
	public VariableInfo findProperty( String name ) {
		if( properties == null ) {
			return null ;
		} // if
		
		else {
			for( int i = 0 ; i < properties.size() ; i++) {
				if ( properties.get(i).getName().equals(name) )
					return properties.get(i) ;
			} // for
			
			return null;
		} // else
	} // findProperty()
	
	
	public OperationInfo findOperation( String name ) {
		if( operations == null ) {
			return null ;
		} // if
		
		else {
			for( int i = 0 ; i < operations.size() ; i++) {
				if ( operations.get(i).getName().equals(name) )
					return operations.get(i) ;
			} // for
			
			return null;
		} // else
	} // findProperty()
}
