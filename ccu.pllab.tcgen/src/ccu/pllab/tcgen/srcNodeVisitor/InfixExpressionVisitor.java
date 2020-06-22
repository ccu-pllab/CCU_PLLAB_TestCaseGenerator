package ccu.pllab.tcgen.srcNodeVisitor;


 
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Expression;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.*;

public class InfixExpressionVisitor extends JAVA2CLG implements SrcNodeVisit {
	private CLGNode clgNode;
	private CLGConstraint constraint;
	private CLGGraph clgGraph;
	/****************************************************************/
	@Override
	public boolean visit(InfixExpression node) {
		String leftOperand = node.getLeftOperand().getClass().toString();
		String rightOperand = node.getRightOperand().getClass().toString();
		CLGConstraint leftOperandCons = new CLGVariableNode();
		CLGConstraint operationCons = new CLGOperatorNode(node.getOperator().toString());
		CLGConstraint rightOperandCons = new CLGVariableNode();
		
		
		/********
		 * 
		 * 括號要修正
		 */
		
//		System.out.println("LCons: "+node.getLeftOperand().getClass().toString());
//		System.out.println("RCons: "+node.getRightOperand().getClass().toString());
//		System.out.println("node.getOperator() "+node.getOperator().toString());
//		System.out.println("node.getLeftOperand() "+node.getLeftOperand().toString());
//		System.out.println("node.getRightOperand() "+node.getRightOperand().toString());
//		System.out.println("node.extendedOperands() "+node.extendedOperands().toString());
//		System.out.println("node.extendedOperands().size() "+node.extendedOperands().size());
//		if(node.extendedOperands().size()>0)
//		System.out.println("node.extendedOperands().class() "+node.extendedOperands().get(0).getClass());
//		InfixExpressionVisitor Nvisitor = new InfixExpressionVisitor();
//		if(node.extendedOperands().size()>0)
//		((Expression)node.extendedOperands().get(0)).accept(Nvisitor);
		/*
		 * 
		 * 
		 * LeftOp start to visit
		 */
		if (leftOperand.equals("class org.eclipse.jdt.core.dom.InfixExpression")) {
			InfixExpressionVisitor visitor = new InfixExpressionVisitor();
			node.getLeftOperand().accept(visitor);
			leftOperandCons = visitor.getConstraint();
			

		} else if (leftOperand.equals("class org.eclipse.jdt.core.dom.NumberLiteral")) {
			NumberLiteralVisitor visitor = new NumberLiteralVisitor();
			node.getLeftOperand().accept(visitor);
			leftOperandCons = visitor.getConstraint();
		} else if (leftOperand.equals("class org.eclipse.jdt.core.dom.StringLiteral")) {
			StringLiteralVisitor visitor = new StringLiteralVisitor();
			node.getLeftOperand().accept(visitor);
			leftOperandCons = visitor.getConstraint();
		} else if (leftOperand.equals("class org.eclipse.jdt.core.dom.SimpleName")) {
			SimpleNameVisitor visitor = new SimpleNameVisitor();
			node.getLeftOperand().accept(visitor);
			leftOperandCons = visitor.getConstraint();
		}else if((leftOperand.equals("class org.eclipse.jdt.core.dom.ArrayAccess"))){
			ArrayAccessVisitor visitor =new ArrayAccessVisitor();
			node.getLeftOperand().accept(visitor);
			leftOperandCons = visitor.getConstraint();
		}
		((CLGOperatorNode) operationCons).setLeftOperand(leftOperandCons);
		
		//LeftOp finish
		
		/*
		 * 
		 * 
		 * RightOp start to visit
		 */
		if (rightOperand.equals("class org.eclipse.jdt.core.dom.InfixExpression")) {
			InfixExpressionVisitor visitor = new InfixExpressionVisitor();
			node.getRightOperand().accept(visitor);
			rightOperandCons = visitor.getConstraint();		
		} else if (rightOperand.equals("class org.eclipse.jdt.core.dom.NumberLiteral")) {
			NumberLiteralVisitor visitor = new NumberLiteralVisitor();
			node.getRightOperand().accept(visitor);
			rightOperandCons = visitor.getConstraint();
		} else if (rightOperand.equals("class org.eclipse.jdt.core.dom.StringLiteral")) {
			StringLiteralVisitor visitor = new StringLiteralVisitor();
			node.getRightOperand().accept(visitor);
			rightOperandCons = visitor.getConstraint();
		} else if (rightOperand.equals("class org.eclipse.jdt.core.dom.SimpleName")) {
			SimpleNameVisitor visitor = new SimpleNameVisitor();
			node.getRightOperand().accept(visitor);
			rightOperandCons = visitor.getConstraint();
		}else if((leftOperand.equals("class org.eclipse.jdt.core.dom.ArrayAccess"))){
			ArrayAccessVisitor visitor =new ArrayAccessVisitor();
			node.getRightOperand().accept(visitor);
			rightOperandCons = visitor.getConstraint();
		}
		((CLGOperatorNode) operationCons).setRightOperand(rightOperandCons);
		//RightOp finish
		
		constraint = operationCons;
		return false;
	}

	/*********************************************************/
	public CLGNode getNode() {
		return clgNode;
	}

	public CLGConstraint getConstraint() {
		return this.constraint;
	}

	public CLGConstraint negationConstraint() {
		CLGConstraint cons = this.getConstraint().clone();
		((CLGOperatorNode) (cons)).negation();
		return cons;
	}

	public CLGGraph getCLGGraph() {
		return this.clgGraph;
	}
}
/* 加入4種 visitor */