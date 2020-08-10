package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;

import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.srcASTVisitor.SrcVisitorUnit;

public class IfStatementVisitor extends JAVA2CLG implements SrcNodeVisit {
	CLGNode clgNode = null;
	int i = 0;
	private CLGGraph clgGraph;

	/*************************************************/

	public boolean visit(IfStatement node) {

		
		
		InfixExpressionVisitor visitor = new InfixExpressionVisitor();
		node.getExpression().accept(visitor);

		CLGGraph thenGraph = new CLGGraph(visitor.getConstraint());
		CLGGraph elseGraph = new CLGGraph(visitor.negationConstraint());
		SrcVisitorUnit visitorThen = new SrcVisitorUnit();
		node.getThenStatement().accept(visitorThen);
		
		
		if(visitorThen.getGraph()!=null){
		thenGraph.graphAnd(visitorThen.getGraph());
		}
	
		SrcVisitorUnit visitorElse = new SrcVisitorUnit();
		if (node.getElseStatement() != null) {
			node.getElseStatement().accept(visitorElse);
	
			elseGraph.graphAnd(visitorElse.getGraph());

		}
		
		thenGraph.graphOr(elseGraph);

		clgGraph=thenGraph;

		return false;
	}

	

	/***************************************************************/
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return this.clgNode;
	}

	@Override
	public CLGConstraint getConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

	public CLGGraph getCLGGraph() {
		
		return this.clgGraph;
	}

}
