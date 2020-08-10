package ccu.pllab.tcgen.clg;
 

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.libs.node.INode;

public class ConnectionNode extends CLGNode {

	public ConnectionNode(Constraint constraint) {
		super(constraint);
	}

	@Override
	public CLGNode clone() {
		return new ConnectionNode(this.getConstraint());
	}

	@Override
	public String toGraphViz() {
		this.setAttribute("graphviz_dfs_state", "explored");
		String result = "";
		if (this instanceof EndNode) {
			result += (this.getId() + " " + String.format("[style=filled, fillcolor=black, shape=\"%s\", label=\"\", fixedsize=true, width=.2, height=.2]", getShape()) + "\n");
		} else {
			result += (this.getId() + " " + String.format("[shape=\"%s\", label=\"\", fixedsize=true, width=.2, height=.2]", getShape()) + "\n");
		}
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
	public String getShape() {
		return "diamond";
	}

}
