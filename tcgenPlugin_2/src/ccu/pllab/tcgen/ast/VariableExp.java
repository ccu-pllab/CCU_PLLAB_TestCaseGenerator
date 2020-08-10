package ccu.pllab.tcgen.ast;
 

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;

public class VariableExp extends ASTNode {

	private String name;
	private Classifier type;
	private String index;
	private String state;
	private boolean stateChangable;

	public VariableExp(Constraint obj, String name, Classifier type, String state) {
		super(obj);
		assert state.equals("precondition") || state.equals("postcondition") || state.equals("both") || state.equals("invariant");
		this.name = name;
		this.type = type;
		this.state = state;
		this.index = "";
		this.stateChangable = true;
	}

	public String getVariableName() {
	
		return this.name;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public Classifier getType() {
		return this.type;
	}

	public String getCLPIndex() {
		return this.index;
	}

	public void setCLPIndex(String index) {
		this.index = index;
	}

	public void setVariableName(String name) {
		this.name = name;
	}

	@Override
	public CLGNode toCLG(Criterion criterion) {
		
		assert this.getType().equals(TypeFactory.getInstance().getClassifier("Boolean"));
		return new ConstraintNode(this.getConstraint(), this);
	}

	@Override
	public VariableExp clone() {
		VariableExp n = new VariableExp(this.getConstraint(), this.getVariableName(), this.getType(), this.getState());
		n.setCLPIndex(this.getCLPIndex());
		n.setAttributes(this.getAttributes());
		return n;
	}

	@Override
	public String toOCL() {
	
		if (this.getState().equals("precondition")) {
			return this.getVariableName() +"@pre";
		} else {
			return this.getVariableName();
		}
	}

	@Override
	public String getLabelForGraphviz() {
		return this.toOCL() + String.format("[%s]", this.index);
	}

	@Override
	public List<INode> getNextNodes() {
		return new ArrayList<INode>();
	}

	public void setState(String string) {
		if (stateChangable) {
			this.state = string;
		}

	}

	@Override
	public String getPredicateName(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("variable_node_call");
		tpl.add("node_identifier", this.getId());
		tpl.add("variable_name", this.getVariableName().replace("#", "").replace("[", "").replace("]", ""));
		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("variable_node_body");
		tpl.add("node_identifier", this.getId());
		tpl.add("variable_name", this.getVariableName().replace("#", "").replace("[", "").replace("]", ""));
		tpl.add("state", this.getState());
		tpl.add("variable_index", this.getCLPIndex());
		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VariableExp other = (VariableExp) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public CLGGraph OCL2CLG() {
		String type = this.getType().toString();
		CLGVariableNode variableConstraint =new CLGVariableNode(this.name,type);
		assert this.getType().equals(TypeFactory.getInstance().getClassifier("Boolean")); 
		CLGGraph constraintgraph = new CLGGraph(variableConstraint);
		return constraintgraph;
	}

	@Override
	public CLGConstraint CLGConstraint() {
		
		if (this.getState().equals("precondition")) {
			CLGConstraint VariableConstraint=new CLGVariableNode(this.getVariableName()+"@pre"); 
			return VariableConstraint;
		} else {
			CLGConstraint VariableConstraint=new CLGVariableNode(this.getVariableName()); 
			return VariableConstraint;
		}
	}

	



}
