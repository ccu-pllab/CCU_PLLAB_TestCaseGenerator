package ccu.pllab.tcgen.srcNodeVisitor;

 
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodInvocation;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode;
import ccu.pllab.tcgen.AbstractConstraint.*;


public class MethodInvocationVisitor extends JAVA2CLG implements SrcNodeVisit{
	CLGNode clgNode;
	CLGConstraint constraint;
	CLGGraph clgGraph;
	

	/*************************************************/
	public boolean visit(MethodInvocation node){	
		String obj=node.getExpression().toString();
		String name =node.getName().toString();
		ArrayList<String> arg = new ArrayList<String>();
		for(int i=0;i<node.arguments().size();i++){
			arg.add(node.arguments().get(i).toString());
		}
//		constraint = new CLGMethodInvocationConstraint(obj,name,sa);
		
		constraint = new CLGMethodInvocationNode(obj,name,arg);
		clgNode = new CLGConstraintNode(constraint);
		return false;
	}
	
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return clgNode;
	}
	public CLGConstraint getConstraint(){
		return constraint;
	}
	public void setClassName(String className){
	}

	@Override
	public CLGGraph getCLGGraph() {
		// TODO Auto-generated method stub
		return this.clgGraph;
	}
}
