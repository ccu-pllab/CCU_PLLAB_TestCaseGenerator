package ccu.pllab.tcgen.srcNodeVisitor;

 
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;

public interface SrcNodeVisit {
	public CLGNode getNode();
	public CLGConstraint getConstraint();
	public CLGGraph getCLGGraph();
}
