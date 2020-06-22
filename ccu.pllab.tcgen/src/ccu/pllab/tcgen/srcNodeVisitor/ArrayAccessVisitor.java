package ccu.pllab.tcgen.srcNodeVisitor;
 

import org.eclipse.jdt.core.dom.ArrayAccess;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;

public class ArrayAccessVisitor extends JAVA2CLG implements SrcNodeVisit {
	CLGConstraint constraint;
	
	/***********************************************************/
	
	public boolean visit(ArrayAccess node){

		CLGVariableNode arrayConstraint = new CLGVariableNode(node.getArray().toString());
		String arrayIndex=node.getIndex().getClass().toString();
		switch(arrayIndex){
		case "class org.eclipse.jdt.core.dom.InfixExpression":
			InfixExpressionVisitor inFixVisitor =new InfixExpressionVisitor();
			node.getIndex().accept(inFixVisitor);
			arrayConstraint.setConstraint(inFixVisitor.getConstraint());
			break;
		case "class org.eclipse.jdt.core.dom.NumberLiteral":
			NumberLiteralVisitor numberVisitor =new NumberLiteralVisitor();
			node.getIndex().accept(numberVisitor);	
			arrayConstraint.setConstraint(numberVisitor.getConstraint());
			break;
		case "class org.eclipse.jdt.core.dom.StringLiteral":
			StringLiteralVisitor stringVisitor =new StringLiteralVisitor();
			node.getIndex().accept(stringVisitor);
			arrayConstraint.setConstraint(stringVisitor.getConstraint());
			break;
		case "class org.eclipse.jdt.core.dom.MethodInvocation":
			MethodInvocationVisitor methodVisitor = new MethodInvocationVisitor();
			node.getIndex().accept(methodVisitor);
			arrayConstraint.setConstraint(methodVisitor.getConstraint());
			break;
		case "class org.eclipse.jdt.core.dom.PrefixExpression":
			PrefixExpressionVisitor prefixVisitor = new PrefixExpressionVisitor();
			node.getIndex().accept(prefixVisitor);
			arrayConstraint.setConstraint(prefixVisitor.getConstraint());
			break;
		case "class org.eclipse.jdt.core.dom.PostfixExpression":
			PostfixExpressionVisitor postfixVisitor = new PostfixExpressionVisitor();
			node.getIndex().accept(postfixVisitor);
			arrayConstraint.setConstraint(postfixVisitor.getConstraint());
			break;
		default :
			arrayConstraint.setConstraint(new CLGVariableNode(node.getIndex().toString()));
			break;
		}
		
		constraint=arrayConstraint;
		return false;
	}
	
	
	
	
	
	/**********************************************************/
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CLGConstraint getConstraint() {
		// TODO Auto-generated method stub
		return constraint;
	}

	@Override
	public CLGGraph getCLGGraph() {
		// TODO Auto-generated method stub
		return null;
	}

}
