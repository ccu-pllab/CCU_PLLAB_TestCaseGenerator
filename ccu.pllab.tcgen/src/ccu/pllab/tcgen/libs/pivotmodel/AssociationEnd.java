package ccu.pllab.tcgen.libs.pivotmodel;

 
public class AssociationEnd {
	private int lower;

	private String name;

	private String type;

	private String unique;
	
	private int upper;

	public int getLower() {
		return lower;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getUpper() {
		return upper;
	}
	
	public void setUnique(String unique){
		this.unique=unique;
	}
	
	public String getUnique(){
		return this.unique;
	}
	
	public void setLower(int lower) {
		this.lower = lower;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpper(int upper) {
		this.upper = upper;
	}

	@Override
	public String toString() {
		return "AssociationEnd [name=" + name + ", type=" + type + ", lower=" + lower + ", upper=" + upper + "]";
	}

}
