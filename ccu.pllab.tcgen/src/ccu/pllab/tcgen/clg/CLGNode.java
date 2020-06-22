package ccu.pllab.tcgen.clg;
 

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tudresden.ocl20.pivot.metamodels.uml2.internal.model.UML2Class;
import tudresden.ocl20.pivot.metamodels.uml2.internal.model.UML2Operation;
import tudresden.ocl20.pivot.pivotmodel.ConstrainableElement;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.libs.node.AnnotatedNode;
import ccu.pllab.tcgen.libs.node.INode;

public abstract class CLGNode extends AnnotatedNode {
	private static long node_count = 0;

	private CLGNode endNode;
	private Set<CLGNode> childs;
	private Set<CLGNode> parents;
	private long id;

	private Constraint constraint;

	public Constraint getConstraint() {
		return constraint;
	}

	public String getConstraintedClass() {
		ConstrainableElement elem = constraint.getConstrainedElement().get(0);
		if (elem instanceof UML2Operation) {
			return ((UML2Operation) elem).getOwner().getName();
		} else {
			return ((UML2Class) elem).getName();
		}
	}

	public String getConstraintedMethod() {
		ConstrainableElement elem = constraint.getConstrainedElement().get(0);
		if (elem instanceof UML2Operation) {
			return ((UML2Operation) elem).getName();
		} else {
			return "";
		}
	}

	public String getConstraintedKind() {
		return constraint.getKind().getName();
	}

	public CLGNode(Constraint constraint) {
		super();
		childs = new HashSet<CLGNode>();
		parents = new HashSet<CLGNode>();
		this.id = node_count++;
		this.constraint = constraint;
	}

	public CLGNode getEndNode() {
		if (endNode == null) {
			return this;
		} else {
			return endNode;
		}
	}

	public abstract String getShape();

	public void setEndNode(CLGNode endNode) {
		this.endNode = endNode;
	}

	@Override
	public final long getId() {
		return this.id;
	}

	@Override
	public abstract String toGraphViz();

	@Override
	public void addNextNode(INode node) {
		this.childs.add((CLGNode) node);
		if (!node.getPreviousNodes().contains(this)) {
			node.addPreviousNode(this);
		}
	}

	@Override
	abstract public CLGNode clone();

	@Override
	public void addPreviousNode(INode node) {
		this.parents.add((CLGNode) node);
		if (!node.getNextNodes().contains(this)) {
			node.addNextNode(this);
		}
	}

	@Override
	public List<INode> getNextNodes() {
		return new ArrayList<INode>(this.childs);
	}

	@Override
	public List<INode> getPreviousNodes() {
		return new ArrayList<INode>(this.parents);
	}

	@Override
	public void removeNextNode(INode node) {
		this.childs.remove(node);
	}

	@Override
	public void removePreviousNode(INode node) {
		this.parents.remove(node);
	}

	@Override
	public void replaceNextNode(INode target, INode new_node) {
		this.childs.remove(target);
		this.childs.add((CLGNode) new_node);
	}

	@Override
	public void replacePreviousNode(INode target, INode new_node) {
		this.parents.remove(target);
		this.parents.add((CLGNode) new_node);
	}

	@Override
	public void clearPreviousNodes() {
		this.parents.clear();
	}

	@Override
	public void clearNextNodes() {
		this.childs.clear();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		CLGNode other = (CLGNode) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getConstraintedMethodReturnType() {
		ConstrainableElement elem = constraint.getConstrainedElement().get(0);
		if (elem instanceof UML2Operation) {
			return ((UML2Operation) elem).getType().getName();
		} else {
			return "";
		}
	}
}
