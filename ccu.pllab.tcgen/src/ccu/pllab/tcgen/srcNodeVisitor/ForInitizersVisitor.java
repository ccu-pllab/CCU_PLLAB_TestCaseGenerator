package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;

public class ForInitizersVisitor extends JAVA2CLG implements SrcNodeVisit {
	/*********************************************************************/
	CLGConstraint constraint;
	CLGGraph clgGraph;

	public boolean visit(VariableDeclarationFragment node) {
		VariableDeclarationFragmentVisitor visitor = new VariableDeclarationFragmentVisitor();
		node.accept(visitor);
		constraint = visitor.getConstraint();
		return false;
	}

	public boolean visit(Assignment node) {
		AssignmentVisitor visitor = new AssignmentVisitor();
		node.accept(visitor);
		constraint = visitor.getConstraint();
		clgGraph =visitor.getCLGGraph();
		return false;
	}

	/********************************************************************/
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CLGConstraint getConstraint() {
		// TODO Auto-generated method stub
		return constraint;
	}

	@Override
	public CLGGraph getCLGGraph() {
		// TODO Auto-generated method stub
		return clgGraph;
	}

}
