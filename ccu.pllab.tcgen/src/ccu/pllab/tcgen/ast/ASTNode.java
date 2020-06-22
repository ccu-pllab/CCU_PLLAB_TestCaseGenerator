package ccu.pllab.tcgen.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.Predicate;
import ccu.pllab.tcgen.libs.node.AnnotatedNode;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGMethodInvocationNode;
 

public abstract class ASTNode extends AnnotatedNode implements Predicate {
	private static long node_count = 0;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {//等於
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ASTNode other = (ASTNode) obj;
		if (id != other.id)
			return false;
		return true;
	}

	private long id;

	private List<INode> parents;

	private Constraint constraint;
	private CLGConstraint constraint1;
	private String nodeWord;
	

	public ASTNode(Constraint obj) {
		super();
		this.id = node_count++;
		this.constraint = obj;
		parents = new ArrayList<INode>();
	}
	public ASTNode(String word) {
		super();
		this.id = node_count++;
		this.nodeWord = word;
		parents = new ArrayList<INode>();
	}
	
	@Override
	public void addPreviousNode(INode node) {
		this.clearPreviousNodes();
		this.parents.add(node);
	}

	@Override
	public void addNextNode(INode node) {
		throw new IllegalStateException();
	}

	@Override
	public abstract List<INode> getNextNodes();

	@Override
	public List<INode> getPreviousNodes() {
		return new ArrayList<INode>(this.parents);
	}

	@Override
	public void removeNextNode(INode node) {
		throw new IllegalStateException();
	}

	@Override
	public void removePreviousNode(INode node) {
		this.parents.remove(node);
	}

	@Override
	public void replaceNextNode(INode target, INode new_node) {
		throw new IllegalStateException();
	}

	@Override
	public void replacePreviousNode(INode target, INode new_node) {
		this.parents.set(this.parents.indexOf(target), new_node);
	}

	@Override
	public void clearPreviousNodes() {
		this.parents.clear();
	}

	@Override
	public void clearNextNodes() {
		throw new IllegalStateException();
	}

	public Constraint getConstraint() {
		return this.constraint;
	}
	
	public static boolean isInteger(String str){
		Pattern pattern =  Pattern.compile("^[-//+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	
	@Override
	public String toString() {
		return this.toOCL();
	}

	@Override
	public final long getId() {
		return this.id;
	}

	public abstract CLGNode toCLG(Criterion criterion);
	
	public abstract CLGGraph OCL2CLG();
	
	public abstract CLGConstraint CLGConstraint();

	public abstract Classifier getType();

	public abstract String getState();

	@Override
	public abstract ASTNode clone();

	public abstract String toOCL();

	public abstract String getLabelForGraphviz();

	public abstract ASTNode toDeMorgan();

	public abstract ASTNode toPreProcessing();

	@Override
	public final String toGraphViz() {
		String result = "";
		result += (this.getId() + " " + String.format("[label=\"(%d) %s\"]", this.getId(), this.getLabelForGraphviz()) + "\n"); 
		for (INode child : this.getNextNodes()) {
			result += child.toGraphViz();
			result += (this.getId() + " -> " + child.getId() + "\n");
		}
		return result;
	}
}
