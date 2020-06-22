package ccu.pllab.tcgen.srcNodeVisitor;

 

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;

public class FieldDeclarationVisitor extends JAVA2CLG implements SrcNodeVisit {

	public boolean visit(FieldDeclaration node) {

		for (Object frame : node.fragments()) {
			VariableDeclarationFragmentVisitor visitor = new VariableDeclarationFragmentVisitor();
			if (frame instanceof VariableDeclarationFragment) {
				((VariableDeclarationFragment) frame).accept(visitor);
			}
		}

		return false;
	}

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
		return null;
	}

}
