package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Expression;

import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.srcASTVisitor.SrcVisitorUnit;

public class ForStatementVisitor extends JAVA2CLG implements SrcNodeVisit {
	CLGNode clgNode;
	CLGGraph clgGraph = null;

	/***************************************************/

	public boolean visit(ForStatement node) {
		
		
		
		ForInitizersVisitor initVisitor = new ForInitizersVisitor();
		InfixExpressionVisitor branchVisitor = new InfixExpressionVisitor();
		SrcVisitorUnit bodyVisitor = new SrcVisitorUnit();
		SrcVisitorUnit updaterVisitor = new SrcVisitorUnit();
		int i=0;
		CLGGraph initGraph = null;
		for (Object nd : node.initializers()) {
			((Expression) nd).accept(initVisitor);
			CLGGraph tempGraph = null;
			if(initVisitor.getCLGGraph()!=null){
				tempGraph=initVisitor.getCLGGraph();
			}else{
				tempGraph= new CLGGraph(initVisitor.getConstraint());
			}
			if (initGraph == null) {
				initGraph = tempGraph;
			} else {
				initGraph.graphAnd(tempGraph);
			}
		}
		node.getExpression().accept(branchVisitor);
		CLGGraph inbodyGraph = new CLGGraph(branchVisitor.getConstraint());
		CLGGraph outbodyGraph = new CLGGraph(branchVisitor.negationConstraint());

		node.getBody().accept(bodyVisitor);
		
		inbodyGraph.graphAnd(bodyVisitor.getGraph());
		
		CLGGraph updaterGraph = null;
		for (Object nd : node.updaters()) {
			((Expression) nd).accept(updaterVisitor);
			CLGGraph tempGraph = updaterVisitor.getGraph();
			if (updaterGraph == null) {
				updaterGraph = tempGraph;
			} else {
				updaterGraph.graphAnd(tempGraph);
			}
		}
		inbodyGraph.graphAnd(updaterGraph);
		
		inbodyGraph.graphClosure();
		
		inbodyGraph.graphAnd(outbodyGraph);
		if(initGraph!=null){
			initGraph.graphAnd(inbodyGraph);
		}else{
			initGraph=inbodyGraph;
		}
		
		clgGraph = initGraph;
		
		return false;
	}

	/*
	 * 
	 * 
	 * the following visitor
	 */

	/*************************************************/

	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return clgNode;
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
