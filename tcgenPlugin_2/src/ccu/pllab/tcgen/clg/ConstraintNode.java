package ccu.pllab.tcgen.clg;
 

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.ast.ASTNode;
import ccu.pllab.tcgen.libs.node.INode;

public class ConstraintNode extends CLGNode {
	static private int xlabel_count = 0;
	private ASTNode ast;
	private int xlable_id;

	public ConstraintNode(Constraint constraint, ASTNode obj) {
		super(constraint);
		this.ast = obj;
		this.xlable_id = xlabel_count++;
		assert obj != null;
	}

	public ASTNode getASTNode() {
		return this.ast;
	}

	public void setASTNode(ASTNode node) {
		this.ast = node;
	}

	@Override
	public CLGNode clone() {
		ConstraintNode n = new ConstraintNode(this.getConstraint(), this.getASTNode().clone());
		return n;
	}

	@Override
	public String toGraphViz() {
		this.setAttribute("graphviz_dfs_state", "explored");
		String result = "";
		result += (this.getId() + " " + String.format("[shape=box, label=\"%s\", xlabel=\"%d\"]", this.getASTNode().toOCL(), this.xlable_id) + "\n");
		for (INode it_child : this.getNextNodes()) {
			CLGNode child = (CLGNode) it_child;
			if (child.getAttribute("graphviz_dfs_state").equals("")) {
				result += child.toGraphViz();

			}
			result += (this.getId() + " -> " + child.getId() + "\n");
		}
		this.setAttribute("graphviz_dfs_state", "visited");
		return result;
	}

	@Override
	public void addNextNode(INode node) {
		// assert this.getNextNodes().size() == 0;
		super.addNextNode(node);
	}

	@Override
	public String getShape() {
		return "box";
	}
}
