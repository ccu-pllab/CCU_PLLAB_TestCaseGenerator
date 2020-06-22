package ccu.pllab.tcgen.srcNodeVisitor;
import org.eclipse.jdt.core.dom.WhileStatement;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.srcASTVisitor.SrcVisitorUnit;
 

public class WhileStatementVisitor extends JAVA2CLG implements SrcNodeVisit{
	private CLGNode clgNode;
	private CLGConstraint constraint;
	private CLGGraph clgGraph;
	/*************************************************/
	public boolean visit(WhileStatement node){		
		InfixExpressionVisitor visitor = new InfixExpressionVisitor();
		node.getExpression().accept(visitor);		
		
		SrcVisitorUnit bodyVisitor = new SrcVisitorUnit();
		/***/
		node.getBody().accept(bodyVisitor);
		CLGGraph inbodyGraph = new CLGGraph(visitor.getConstraint());
		CLGGraph outbodyGraph = new CLGGraph(visitor.negationConstraint());
		
	
		/*  empty body exception*/
		if(bodyVisitor.getGraph()!=null)
		inbodyGraph.graphAnd(bodyVisitor.getGraph());
		inbodyGraph.graphClosure();
		inbodyGraph.graphAnd(outbodyGraph);		
		clgGraph=inbodyGraph;
		return false;
	}
	
	/******************************************************************/
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return clgNode;
	}
	@Override
	public CLGConstraint getConstraint() {
		return this.constraint;
	}
	@Override
	public CLGGraph getCLGGraph() {
		return this.clgGraph;
	}

}
