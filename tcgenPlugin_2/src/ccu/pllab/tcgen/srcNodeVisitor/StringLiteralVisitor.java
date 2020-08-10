package ccu.pllab.tcgen.srcNodeVisitor;


 
import org.eclipse.jdt.core.dom.StringLiteral;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;



public class StringLiteralVisitor extends JAVA2CLG implements SrcNodeVisit{
	CLGNode clgNode;
	CLGConstraint constraint;
	/*************************************************/
	public boolean visit(StringLiteral node){
		constraint= new CLGLiteralNode(node.getEscapedValue(),"String");
		return false;
	}
	public CLGConstraint getConstraint(){
		return this.constraint;
	}
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return clgNode;
	}
	@Override
	public CLGGraph getCLGGraph() {
		// TODO Auto-generated method stub
		return null;
	}

}
