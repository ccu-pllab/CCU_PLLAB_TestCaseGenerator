package ccu.pllab.tcgen.srcNodeVisitor;


 
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractCLG.CLGStartNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;

public class MethodDeclarationVisitor extends JAVA2CLG implements SrcNodeVisit{
	CLGNode clgNode;

	/*************************************************/
	public boolean visit(MethodDeclaration node){
		clgNode = new CLGStartNode("",node.getName().toString());
		ArrayList<String> parameters=new ArrayList<String>();
		for(Object o:node.parameters()){
			if(o instanceof SingleVariableDeclaration)
			parameters.add(((SingleVariableDeclaration) o).getName().toString());
		}
		System.out.println(parameters);
		( (CLGStartNode)clgNode).setMethodParameters(parameters);
		return false;
	}
	
	@Override
	public CLGNode getNode() {
		// TODO Auto-generated method stub
		return clgNode;
	}
	public void setClassName(String className){
		((CLGStartNode)clgNode).setClassName(className);
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
