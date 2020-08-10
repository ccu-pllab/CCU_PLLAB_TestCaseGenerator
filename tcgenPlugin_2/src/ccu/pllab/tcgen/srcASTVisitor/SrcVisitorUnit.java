package ccu.pllab.tcgen.srcASTVisitor;

 
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.srcNodeVisitor.*;

public class SrcVisitorUnit extends ASTVisitor {
	private CLGNode clgNode;
	private String className;
	private CLGGraph clgGraph;
	private ArrayList<String> classAttrubutes = new ArrayList<String>();
	private List<CLGNode> clgNodeList = new ArrayList<CLGNode>();
	private List<CLGGraph> clgGraphList = new ArrayList<CLGGraph>();

	/********************************************************************************/

	public boolean visit(CompilationUnit node) {
		return true;
	}

	public boolean visit(TypeDeclaration node) {
		className = node.getName().toString();
		return true;
	}

	public boolean visit(FieldDeclaration node) {
		FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();
		node.accept(visitor);
		System.out.println("field: "+node.fragments());
		classAttrubutes.add(node.fragments().get(0).toString());
		return true;
	}

	public boolean visit(MethodDeclaration node) {
		if (clgNode != null)
			clgNode.clearSuccessors();
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		node.accept(visitor);
		visitor.setClassName(className);
		clgNode = visitor.getNode();
		clgGraph = null;
		return true;
	}

	public void endVisit(MethodDeclaration node) {

		if (clgGraph == null){
			clgGraph = new CLGGraph();
		}
		((CLGStartNode) clgGraph.getStartNode()).setClassName(className);
		((CLGStartNode) clgGraph.getStartNode()).setMethodName(node.getName().toString());
		if(node.getReturnType2()!= null){
			((CLGStartNode) clgGraph.getStartNode()).setRetType(node.getReturnType2().toString());
		}else{
			((CLGStartNode) clgGraph.getStartNode()).setRetType("null");
		}
		if (classAttrubutes != null){
			((CLGStartNode) clgGraph.getStartNode()).setClassAttributes(classAttrubutes);
		}
		if (node.parameters().size() > 0) {
			ArrayList<String> parameters = new ArrayList<String>();
			for (Object o : node.parameters()) {
				if (o instanceof SingleVariableDeclaration)
					parameters.add(((SingleVariableDeclaration) o).getName().toString());
			}
			((CLGStartNode) clgGraph.getStartNode()).setMethodParameters(parameters);
		}
		
		clgGraphList.add(clgGraph);
	}

	public boolean visit(Assignment node) {
		AssignmentVisitor visitor = new AssignmentVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;
	}

	public boolean visit(MethodInvocation node) {
		MethodInvocationVisitor visitor = new MethodInvocationVisitor();
		node.accept(visitor);
		return false;
	}

	public boolean visit(IfStatement node) {
		IfStatementVisitor visitor = new IfStatementVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;
	}

	public boolean visit(PrefixExpression node) {
		PrefixExpressionVisitor visitor = new PrefixExpressionVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;

	}

	public boolean visit(DoStatement node) {
		DoStatementVisitor visitor = new DoStatementVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;
	}
	public boolean visit(ThrowStatement node){
		ThrowStatementVisitor visitor = new ThrowStatementVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();			
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;
		
	}
	public boolean visit(PostfixExpression node) {
		PostfixExpressionVisitor visitor = new PostfixExpressionVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;

	}

	public boolean visit(WhileStatement node) {
		WhileStatementVisitor visitor = new WhileStatementVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;
	}

	public boolean visit(ForStatement node) {
		ForStatementVisitor visitor = new ForStatementVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;
	}

	public boolean visit(ReturnStatement node) {
		ReturnStatementVisitor visitor = new ReturnStatementVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;
	}

	public boolean visit(ArrayAccess node) {
		ArrayAccessVisitor visitor = new ArrayAccessVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}
		return false;
	}

	public boolean visit(VariableDeclarationStatement node) {
		VariableDeclarationStatementVisitor visitor = new VariableDeclarationStatementVisitor();
		node.accept(visitor);
		if (clgGraph == null) {
			clgGraph = visitor.getCLGGraph();
		} else {
			clgGraph.graphAnd(visitor.getCLGGraph());
		}

		return false;
	}

//	public boolean visit(EnhancedForStatement node) {
//		EnhancedForStatementVisitor visitor = new EnhancedForStatementVisitor();
//		node.accept(visitor);
//		return false;
//	}
//
//	public boolean visit(SwitchStatement node) {
//		SwitchStatementVisitor visitor = new SwitchStatementVisitor();
//		node.accept(visitor);
//		return false;
//	}

	/**********************************************************************/
	public CLGNode getNode() {
		return this.clgNode;
	}

	public CLGGraph getGraph() {
		return this.clgGraph;
	}

	public List<CLGNode> getCLGNode() {
		return clgNodeList;
	}

	public List<CLGGraph> getCLGGraph() {
		return clgGraphList;
	}

}
