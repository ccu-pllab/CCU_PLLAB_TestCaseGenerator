package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;

public class VariableDeclarationStatementVisitor extends JAVA2CLG implements SrcNodeVisit {
	CLGNode clgNode;
	CLGGraph clgGraph;
	CLGConstraint constraint;

	public boolean visit(VariableDeclarationFragment node) {

		CLGConstraint cons = new CLGOperatorNode("=");
		CLGConstraint leftOperand = new CLGVariableNode(node.getName().toString());		
		CLGConstraint rightOperand = null;
		if (node.getInitializer() != null) {
			if (node.getInitializer().getClass().toString().equals("class org.eclipse.jdt.core.dom.SimpleName")) {
				rightOperand = new CLGVariableNode(node.getInitializer().toString());
			} else if (node.getInitializer().getClass().toString()
					.equals("class org.eclipse.jdt.core.dom.NumberLiteral")) {
				rightOperand = new CLGVariableNode(node.getInitializer().toString());
			}
		}
		((CLGOperatorNode) cons).setLeftOperand(leftOperand);
		((CLGOperatorNode) cons).setRightOperand(rightOperand);
		
		constraint = cons;
		if(rightOperand!=null){
			if (clgGraph == null) {
				clgGraph = new CLGGraph(constraint);
			} else {
				clgGraph.graphAnd(new CLGGraph(constraint));
			}
		}
		
		
		return false;
	}

	/*************************************************/
	@Override
	public CLGNode getNode() {
		return clgNode;
	}

	@Override
	public CLGConstraint getConstraint() {
		return constraint;
	}

	@Override
	public CLGGraph getCLGGraph() {
		return this.clgGraph;
	}

}
