package ccu.pllab.tcgen.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGMethodInvocationNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
 

public class IteratorExp extends LoopExp {

	private ArrayList<ASTNode> parameters;

	public IteratorExp(Constraint obj, ASTNode source, String name, Classifier type, List<ASTNode> parameters) {
		super(obj, source, name, type);
		this.parameters = new ArrayList<ASTNode>(parameters);
		for (ASTNode p : this.parameters) {
			p.addPreviousNode(this);
		}
	}

	public IteratorExp(Constraint obj, ASTNode source, String name, Classifier type, ASTNode... parameters) {
		this(obj, source, name, type, new ArrayList<ASTNode>(Arrays.asList(parameters)));
	}

	public void setParameterExpAtPosition(int position, ASTNode parameter) {
		this.parameters.set(position, parameter);
	}

	public List<ASTNode> getParameterExps() {
		return parameters;
	}

	@Override
	public CLGNode toCLG(Criterion criterion) {
	
		assert this.getType().equals(TypeFactory.getInstance().getClassifier("Boolean"));
		return new ConstraintNode(this.getConstraint(), this);
	}

	@Override
	public IteratorExp clone() {
		List<ASTNode> new_params = new ArrayList<ASTNode>();
		for (ASTNode p : parameters) {
			new_params.add(p.clone());
		}
		IteratorExp n = new IteratorExp(this.getConstraint(), this.getSourceExp().clone(), this.getPropertyName(), this.getType(), new_params);
		return n;
	}

	@Override
	public String toOCL() {
		String result = "";
		result += this.getSourceExp();
		result += "->" + this.getPropertyName();
		result += "(";
		result += StringUtils.join(this.getParameterExps(), ", ");
		result += ")";
		
		return result;
	
	}

	@Override
	public List<INode> getNextNodes() {
		List<INode> nodes = super.getNextNodes();
		for (INode n : this.getParameterExps()) {
			nodes.add(n);
		}
		return nodes;
	}

	@Override
	public String getPredicateName(Map<String, String> template_args) {
		if (this.getPropertyName().equals("one") || this.getPropertyName().equals("any")) {
			ST tpl = TemplateFactory.getTemplate("ocl_collection_operation_" + this.getPropertyName() + "_call");
			tpl.add("node_identifier", this.getId());
			tpl.add("isStatic", ASTUtil.isStatic(this.getConstraint()) && !ASTUtil.isConstructor(this.getConstraint()));
			for (Map.Entry<String, String> entry : template_args.entrySet()) {
				tpl.add(entry.getKey(), entry.getValue());
			}
			return tpl.render();
		}
		ST tpl = TemplateFactory.getTemplate("ocl_collection_operation_call");
		tpl.add("node_identifier", this.getId());

		tpl.add("operation_name", this.getPropertyName());
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> template_args) {
		if (this.getPropertyName().equals("one") || this.getPropertyName().equals("any")) {
			ST tpl = TemplateFactory.getTemplate("ocl_collection_operation_" + this.getPropertyName() + "_body");
			tpl.add("node_identifier", this.getId());
			tpl.add("collection_predicate", this.getSourceExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
			tpl.add("isStatic", ASTUtil.isStatic(this.getConstraint()) && !ASTUtil.isConstructor(this.getConstraint()));
			for (Map.Entry<String, String> entry : template_args.entrySet()) {
				tpl.add(entry.getKey(), entry.getValue());
			}
			return tpl.render();
		}
		ST tpl = TemplateFactory.getTemplate("ocl_collection_operation_body");
		tpl.add("collection_type", this.getSourceExp().getType().getName().toLowerCase().split("\\(")[0]);
		tpl.add("node_identifier", this.getId());
		tpl.add("collection_predicate", this.getSourceExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		if (this.getParameterExps().size() > 0) {
			tpl.add("object_predicate", this.getParameterExps().get(0).getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		}

		tpl.add("operation_name", this.getPropertyName());
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public ASTNode toDeMorgan() {
		return this;
	}

	@Override
	public ASTNode toPreProcessing() {
		return this;
	}

	public void setParameterExps(ASTNode... ps) {
		this.parameters = new ArrayList<ASTNode>(Arrays.asList(ps));

	}

	@Override
	public CLGGraph OCL2CLG() {
		String type = this.getType().toString();
		CLGVariableNode variableConstraint =new CLGVariableNode(type);
		assert this.getType().equals(TypeFactory.getInstance().getClassifier("Boolean")); 
		CLGGraph constraintgraph = new CLGGraph(variableConstraint);
		return constraintgraph;
	}

	@Override
	public CLGConstraint CLGConstraint() {
		
		CLGMethodInvocationNode IteratorConstraint=new CLGMethodInvocationNode(this.getSourceExp().toString(),this.getPropertyName(),this.parameters.toString());
        
	
		return IteratorConstraint;
	}



}
