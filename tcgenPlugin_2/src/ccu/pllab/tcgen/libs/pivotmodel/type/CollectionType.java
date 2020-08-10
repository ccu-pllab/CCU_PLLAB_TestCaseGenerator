package ccu.pllab.tcgen.libs.pivotmodel.type;

 
import java.util.ArrayList;
import java.util.List;

import ccu.pllab.tcgen.libs.pivotmodel.Operation;

public class CollectionType extends DataType {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementType == null) ? 0 : elementType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CollectionType other = (CollectionType) obj;
		if (elementType == null) {
			if (other.elementType != null)
				return false;
		} else if (!elementType.equals(other.elementType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	private String name;
	private Classifier elementType;
	private List<Operation> ops;

	protected CollectionType(String name, Classifier elementType, List<Operation> operations) {
		this.name = name;
		this.elementType = elementType;
		this.ops = operations;
	}

	@Override
	public String getName() {
		return this.name + "(" + this.elementType.getName() + ")";
	}

	public Classifier getElementType() {
		return this.elementType;
	}

	@Override
	public List<Operation> getOwnedOperations() {
		return new ArrayList<Operation>(this.ops);
	}

}
