package ccu.pllab.tcgen.ast;

 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;

public abstract class PropertyCallExp extends ASTNode {

	private ASTNode source;
	private Classifier type;
	private String name;

	@Override
	public Classifier getType() {
		return type;
	}

	public String getPropertyName() {
		if(name.equals("mod"))
		{
	
			return "%";
		}
		if(name.equals("and"))
		{
	
			return "&&";
		}
		if(name.equals("or"))
		{
	
			return "||";
		}
		return name;
	}
	
	public void setPropertyName(String str) {
		this.name = str;
	}

	public PropertyCallExp(Constraint obj, ASTNode source, String name, Classifier type) {
		super(obj);
		assert name != null;
		assert !name.equals("");
		this.source = source;
		this.name = name;
		this.type = type;
		source.addPreviousNode(this);

	}

	public ASTNode getSourceExp() {
		
	
		
	
		return source;
	}

	public void setSourceExp(ASTNode source) {
		this.source = source;
	}

	@Override
	public String getLabelForGraphviz() {
		return this.getPropertyName();
	}

	@Override
	public String getState() {
		return this.getSourceExp().getState();
	}

	@Override
	public List<INode> getNextNodes() {
		INode[] nodes = new INode[] { this.getSourceExp() };
		return new ArrayList<INode>(Arrays.asList(nodes));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyCallExp other = (PropertyCallExp) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
