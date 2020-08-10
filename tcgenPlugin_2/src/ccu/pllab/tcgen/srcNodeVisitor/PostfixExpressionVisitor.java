package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.PostfixExpression;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;

public class PostfixExpressionVisitor extends JAVA2CLG implements SrcNodeVisit {
	private CLGNode clgNode;
	private CLGConstraint constraint;
	private CLGGraph clgGraph;

	/*************************************************/
	public boolean visit(PostfixExpression node) {
		CLGConstraint cons = null ;
		// which op + or ++
		if (node.getOperator().toString().equals("++")) {
			cons = new CLGOperatorNode("+");
		} else if (node.getOperator().toString().equals("--")) {
			cons = new CLGOperatorNode("-");
		}
		if (node.getOperand().getClass().toString().equals("class org.eclipse.jdt.core.dom.ArrayAccess")) {
			ArrayAccessVisitor visitor = new ArrayAccessVisitor();
			node.getOperand().accept(visitor);
			((CLGOperatorNode) cons).setLeftOperand(visitor.getConstraint());
		} else {
			((CLGOperatorNode) cons).setLeftOperand(new CLGVariableNode(node.getOperand().toString()));
		}
		((CLGOperatorNode) cons).setRightOperand(new CLGLiteralNode("1"));
		constraint = cons;
		if(!node.getParent().getClass().toString().equals("class org.eclipse.jdt.core.dom.Assignment")){
			CLGOperatorNode extra_cons = new CLGOperatorNode("=");
			CLGConstraint extra_left_cons = new CLGVariableNode(node.getOperand().toString());
			extra_cons.setLeftOperand(extra_left_cons);
			extra_cons.setRightOperand(cons);
			constraint=cons;
		}
		
		
		clgGraph = new CLGGraph(constraint);
		return false;
	}

	/*****************************************************************/
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return clgNode;
	}

	@Override
	public CLGConstraint getConstraint() {
		return this.constraint;
	}

	public CLGGraph getCLGGraph() {
		return this.clgGraph;
	}
}
