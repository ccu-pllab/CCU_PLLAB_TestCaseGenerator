package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.Assignment;
import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.AbstractConstraint.*;

public class AssignmentVisitor extends JAVA2CLG implements SrcNodeVisit {

	CLGConstraint constraint;
	CLGNode clgNode;
	CLGGraph clgGraph;

	/*************************************************/
	public boolean visit(Assignment node) {
		CLGConstraint leftVar = new CLGVariableNode();
		CLGConstraint operator = new CLGOperatorNode(node.getOperator().toString());
		CLGConstraint rightVar ;
		
		/********************************************************************/
		String leftType = node.getLeftHandSide().getClass().toString();

		switch (leftType) {
		case "class org.eclipse.jdt.core.dom.InfixExpression":
			InfixExpressionVisitor inFixVisitor = new InfixExpressionVisitor();
			node.getLeftHandSide().accept(inFixVisitor);
			leftVar = inFixVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.NumberLiteral":
			NumberLiteralVisitor numberVisitor = new NumberLiteralVisitor();
			node.getLeftHandSide().accept(numberVisitor);
			leftVar = numberVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.StringLiteral":
			StringLiteralVisitor stringVisitor = new StringLiteralVisitor();
			node.getLeftHandSide().accept(stringVisitor);
			leftVar = stringVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.MethodInvocation":
			MethodInvocationVisitor methodVisitor = new MethodInvocationVisitor();
			node.getLeftHandSide().accept(methodVisitor);
			leftVar = methodVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.PrefixExpression":
			PrefixExpressionVisitor prefixVisitor = new PrefixExpressionVisitor();
			node.getLeftHandSide().accept(prefixVisitor);
			leftVar = prefixVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.PostfixExpression":
			PostfixExpressionVisitor postfixVisitor = new PostfixExpressionVisitor();
			node.getLeftHandSide().accept(postfixVisitor);
			leftVar = postfixVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.ArrayAccess":
			ArrayAccessVisitor arrayVisitor = new ArrayAccessVisitor();
			node.getLeftHandSide().accept(arrayVisitor);
			leftVar = arrayVisitor.getConstraint();
			break;
		default:
			leftVar = new CLGVariableNode(node.getLeftHandSide().toString());
			break;
		}

		/********************************************************************/
		String rightType = node.getRightHandSide().getClass().toString();
		switch (rightType) {
		case "class org.eclipse.jdt.core.dom.InfixExpression":
			InfixExpressionVisitor inFixVisitor = new InfixExpressionVisitor();
			node.getRightHandSide().accept(inFixVisitor);
			rightVar = inFixVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.NumberLiteral":
			NumberLiteralVisitor numberVisitor = new NumberLiteralVisitor();
			node.getRightHandSide().accept(numberVisitor);
			rightVar = numberVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.StringLiteral":
			StringLiteralVisitor stringVisitor = new StringLiteralVisitor();
			node.getRightHandSide().accept(stringVisitor);
			rightVar = stringVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.MethodInvocation":
			MethodInvocationVisitor methodVisitor = new MethodInvocationVisitor();
			node.getRightHandSide().accept(methodVisitor);
			rightVar = methodVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.PrefixExpression":
			PrefixExpressionVisitor prefixVisitor = new PrefixExpressionVisitor();
			node.getRightHandSide().accept(prefixVisitor);
			rightVar = prefixVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.PostfixExpression":
			PostfixExpressionVisitor postfixVisitor = new PostfixExpressionVisitor();
			node.getRightHandSide().accept(postfixVisitor);
			rightVar = postfixVisitor.getConstraint();
			break;
		case "class org.eclipse.jdt.core.dom.ArrayAccess":
			ArrayAccessVisitor arrayVisitor = new ArrayAccessVisitor();
			node.getRightHandSide().accept(arrayVisitor);
			rightVar = arrayVisitor.getConstraint();
			break;
		default:
			rightVar = new CLGVariableNode(node.getRightHandSide().toString());
			break;
		}
		((CLGOperatorNode) operator).setLeftOperand(leftVar);
		((CLGOperatorNode) operator).setRightOperand(rightVar);
		this.constraint = operator;
		clgGraph = new CLGGraph(this.constraint);

		return false;
	}

	/********************************************************************************/
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

	public CLGGraph getCLGGraph() {
		return this.clgGraph;
	}

}
