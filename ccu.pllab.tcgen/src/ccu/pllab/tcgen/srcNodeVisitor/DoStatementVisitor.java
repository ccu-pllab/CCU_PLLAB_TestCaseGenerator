package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.DoStatement;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.srcASTVisitor.SrcVisitorUnit;

public class DoStatementVisitor extends JAVA2CLG implements SrcNodeVisit{
	CLGNode clgNode;
	CLGGraph clgGraph;
	CLGConstraint constraint;
	
	
/*******************************************************/	
	public boolean visit(DoStatement node){
		System.out.println("Do node.getExpression(): "+node.getExpression());
		System.out.println("Do node.getBody(): "+node.getBody());
		
		SrcVisitorUnit visitor_1 = new SrcVisitorUnit();
		node.getBody().accept(visitor_1);
		CLGGraph doFirstBody = visitor_1.getGraph();
		
		InfixExpressionVisitor InfixVisitor = new InfixExpressionVisitor();
		node.getExpression().accept(InfixVisitor);
		CLGGraph doInLoop = new CLGGraph(InfixVisitor.getConstraint());
		CLGGraph doOutLoop = new CLGGraph(InfixVisitor.negationConstraint());
		
		SrcVisitorUnit visitor_2 = new SrcVisitorUnit();
		node.getBody().accept(visitor_2);
		
		doInLoop.graphAnd(visitor_2.getGraph());		
		doInLoop.graphClosure();
		
		doInLoop.graphAnd(doOutLoop);
		doFirstBody.graphAnd(doInLoop);
		
		clgGraph=doFirstBody;
		return false;
	}
	
	
/******************************************************/
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return this.clgNode;
	}

	@Override
	public CLGConstraint getConstraint() {
		// TODO Auto-generated method stub
		return this.constraint;
	}

	@Override
	public CLGGraph getCLGGraph() {
		// TODO Auto-generated method stub
		return this.clgGraph;
	}

}
