package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ReturnStatement;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.*;
import ccu.pllab.tcgen.srcASTVisitor.SrcVisitorUnit;

public class ReturnStatementVisitor extends JAVA2CLG implements SrcNodeVisit {
	private CLGGraph clgGraph;
	private CLGConstraint constraint;
	private CLGNode clgNode;
	
	
	/******************************************************************/
	public boolean visit(ReturnStatement node){
		
		CLGConstraint resultConstriant=new CLGVariableNode("");;
			
		CLGConstraint returnLeftConstraint = new CLGVariableNode("Result");
		CLGConstraint returnOpConstraint = new CLGOperatorNode("=");

		String resultType = node.getExpression().getClass().toString();
		switch (resultType) {
		case "class org.eclipse.jdt.core.dom.InfixExpression":
			InfixExpressionVisitor inFixVisitor =new InfixExpressionVisitor();
			node.getExpression().accept(inFixVisitor);
			resultConstriant=inFixVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.NumberLiteral":
			NumberLiteralVisitor numberVisitor =new NumberLiteralVisitor();	
			node.getExpression().accept(numberVisitor);
			resultConstriant=numberVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.StringLiteral":
			StringLiteralVisitor stringVisitor =new StringLiteralVisitor();
			node.getExpression().accept(stringVisitor);
			resultConstriant=stringVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.MethodInvocation":
			MethodInvocationVisitor methodVisitor = new MethodInvocationVisitor();
			node.getExpression().accept(methodVisitor);
			resultConstriant=methodVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.PrefixExpression":
			PrefixExpressionVisitor prefixVisitor = new PrefixExpressionVisitor();
			node.getExpression().accept(prefixVisitor);
			resultConstriant=prefixVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.PostfixExpression":
			PostfixExpressionVisitor postfixVisitor = new PostfixExpressionVisitor();
			node.getExpression().accept(postfixVisitor);
			resultConstriant=postfixVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.SimpleName":
			SimpleNameVisitor simpleNameVisitor = new SimpleNameVisitor();
			node.getExpression().accept(simpleNameVisitor);
			resultConstriant=simpleNameVisitor.getConstraint();
			break;
		}
		( (CLGOperatorNode)returnOpConstraint) .setLeftOperand(returnLeftConstraint);
		( (CLGOperatorNode)returnOpConstraint) .setRightOperand(resultConstriant);
		clgGraph= new CLGGraph(returnOpConstraint);
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
		return null;
	}

	@Override
	public CLGGraph getCLGGraph() {
		// TODO Auto-generated method stub
		return this.clgGraph;
	}

}
