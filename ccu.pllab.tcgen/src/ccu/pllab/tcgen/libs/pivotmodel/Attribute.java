package ccu.pllab.tcgen.libs.pivotmodel;

 
public class Attribute {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	private String name;
	private String type;
	private int lower;
	private int upper;
	private boolean unique;

	public Attribute(String name, String type ,int lower,int upper,String unique) {
		this.name = name;
		this.type = type;
		this.lower=lower;
		this.upper=upper;
		this.unique=Boolean.valueOf(unique);
	}
	
	public boolean getUnique(){
		return this.unique;
	}
	
	
	public int getLower(){
		return this.lower;
	}
	
	public int getUpper(){
		return this.upper;
	}
	
	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public String getDomain() {
		return "-65535..65535";
	}

}
