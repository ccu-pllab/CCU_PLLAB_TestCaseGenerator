package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ThrowStatement;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;

public class ThrowStatementVisitor extends JAVA2CLG implements SrcNodeVisit {
	private CLGGraph clgGraph;

	public boolean visit(ThrowStatement node){
		SimpleNameVisitor visitor = new SimpleNameVisitor(){
			CLGLiteralNode constraint ;
			public boolean visit(SimpleName node){
				constraint = new CLGLiteralNode("\""+node.getIdentifier().toString()+"\"");
				constraint.setType("String");
				return false;
			}
			public CLGConstraint getConstraint(){
				return constraint;
			}
		};
		node.accept(visitor);
		CLGConstraint returnLeftConstraint = new CLGVariableNode("Result");
		CLGConstraint returnOpConstraint = new CLGOperatorNode("=");
		( (CLGOperatorNode)returnOpConstraint) .setLeftOperand(returnLeftConstraint);
		( (CLGOperatorNode)returnOpConstraint) .setRightOperand(visitor.getConstraint());
		clgGraph= new CLGGraph(returnOpConstraint);
		return false;
	}

	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CLGConstraint getConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CLGGraph getCLGGraph() {
		// TODO Auto-generated method stub
		return this.clgGraph;
	}

}
