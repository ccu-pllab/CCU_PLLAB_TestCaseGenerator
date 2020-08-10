package ccu.pllab.tcgen.srcNodeVisitor;

 
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.NumberLiteral;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.*;


public class NumberLiteralVisitor extends JAVA2CLG implements SrcNodeVisit{
	CLGNode clgNode;
	CLGConstraint constraint;
	/*************************************************/
	public boolean visit(NumberLiteral node){
		constraint = new CLGLiteralNode(node.getToken());
		return false;
	}
	
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return clgNode;
	}
	public CLGConstraint getConstraint(){
		return this.constraint;
	}

	@Override
	public CLGGraph getCLGGraph() {
		// TODO Auto-generated method stub
		return null;
	}
}
