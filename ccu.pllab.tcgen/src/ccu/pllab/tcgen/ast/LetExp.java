package ccu.pllab.tcgen.ast;

 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGMethodInvocationNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.clg.ConnectionNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.facade.FacadeConfig;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.node.GraphVisitor;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.node.NodeVisitHandler;
import ccu.pllab.tcgen.libs.node.QueueFrontier;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.CollectionType;
import ccu.pllab.tcgen.libs.pivotmodel.type.PrimitiveType;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
public class LetExp extends ASTNode {
	private ASTNode var_assign;
	private ASTNode init_exp;
	
	public LetExp(Constraint obj,ASTNode var_assign, ASTNode init_exp) {
		super(obj);
		this.var_assign = var_assign;
		this.init_exp = init_exp;
	}

	@Override
	public String getPredicateName(Map<String, String> templateArgs) {
		return "";
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		return "";
	}

	@Override
	public List<INode> getNextNodes() {
		List<INode> nodes = new ArrayList();
		nodes.add(this.var_assign);
		nodes.add(this.init_exp);
		return nodes;
	}

	@Override
	public ccu.pllab.tcgen.clg.CLGNode toCLG(Criterion criterion) {
		return new ConstraintNode(this.getConstraint(), this);
	}

	@Override
	public CLGGraph OCL2CLG() {
		CLGGraph clg_left_node = var_assign.OCL2CLG();
		CLGGraph clg_right_node = init_exp.OCL2CLG();
		clg_left_node.graphAnd(clg_right_node);
		return clg_left_node;
	}

	@Override
	public ccu.pllab.tcgen.AbstractConstraint.CLGConstraint CLGConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Classifier getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ASTNode clone() {
		return this;
	}

	@Override
	public String toOCL() {
		return "";
	}

	@Override
	public String getLabelForGraphviz() {
		return "let";
	}

	@Override
	public ASTNode toDeMorgan() {
		return this;
	}

	@Override
	public ASTNode toPreProcessing() {
		return this;
	}
	

}
