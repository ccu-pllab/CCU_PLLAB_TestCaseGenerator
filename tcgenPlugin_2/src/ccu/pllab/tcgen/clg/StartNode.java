package ccu.pllab.tcgen.clg;

 
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.libs.node.INode;

public class StartNode extends ConnectionNode {

	public StartNode(Constraint constraint) {
		super(constraint);
	}

	@Override
	public String getShape() {
		return "box";
	}

	@Override
	public String toGraphViz() {
		this.setAttribute("graphviz_dfs_state", "explored");
		String result = "";
		result += (String.format("n_%d [shape=\"ellipse\", label=\"%s\"]", this.getId(), this.getLabel()) + "\n");
		result += (this.getId() + " " + "[style=filled, fillcolor=black, shape=\"circle\", label=\"\", fixedsize=true, width=.2, height=.2]\n");
		for (INode it_child : this.getNextNodes()) {
			CLGNode child = (CLGNode) it_child;
			if (child.getAttribute("graphviz_dfs_state").equals("")) {
				result += child.toGraphViz();

			}
			result += (this.getId() + " -> " + child.getId() + "\n");
			result += ("n_" + this.getId() + " -> " + this.getId() + "\n");
		}
		this.setAttribute("graphviz_dfs_state", "visited");
		return result;
	}

	public String getLabel() {
		String result = "";
		result += this.getConstraintedClass();
		if (!this.getConstraintedMethod().equals("")) {
			result += "::" + this.getConstraintedMethod();
		}
		result += " " + this.getConstraintedKind();
		return result;
	}
	
	@Override
	public CLGNode clone() {
		return new StartNode(this.getConstraint());
	}

}
