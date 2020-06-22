package ccu.pllab.tcgen.srcNodeVisitor;

 

import org.eclipse.jdt.core.dom.SimpleName;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;

public class SimpleNameVisitor extends JAVA2CLG implements SrcNodeVisit{
	private CLGNode clgNode;
	private CLGConstraint constraint;
	/*************************************************/
	public boolean visit(SimpleName node){		
		constraint = new CLGVariableNode(node.getIdentifier().toString());
		return false;
	}
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
		// TODO Auto-generated method stub
		return null;
	}

}
