package ccu.pllab.tcgen.ast;

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
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;

public  class AssociationEndCallExp extends PropertyCallExp {

	private String dstRole;
	private String srcRole;
	private String ascName;

	public AssociationEndCallExp(Constraint obj, ASTNode source, String name, Classifier type, String dstRole, String srcRole, String ascName) {
		super(obj, source, name, type);
		this.dstRole = dstRole;
		this.srcRole = srcRole;
		this.ascName = ascName;
		this.setAttribute("dsrRole", dstRole);
		this.setAttribute("srcRole", srcRole);
		this.setAttribute("assoication_name", ascName);
	}

	public String getDstRole() {
		return dstRole;
	}

	public String getSrcRole() {
		return srcRole;
	}

	public String getAscName() {
		return ascName;
	}

	@Override
	public CLGNode toCLG(Criterion criterion) {
		assert this.getType().equals(TypeFactory.getInstance().getClassifier("Boolean"));
		return new ConstraintNode(this.getConstraint(), this);
	}
	@Override
	public CLGGraph OCL2CLG() {
		String type = this.getType().toString();
		CLGVariableNode variableConstraint =new CLGVariableNode(this.getPropertyName(),type);
		assert this.getType().equals(TypeFactory.getInstance().getClassifier("Boolean")); 
		CLGGraph constraintgraph = new CLGGraph(variableConstraint);
		constraintgraph.getStartNode().getSuccessor().get(0).removePredecessor(constraintgraph.getStartNode());
		constraintgraph.getEndNode().getPredecessor().get(0).removeSuccessor(constraintgraph.getEndNode());
		return constraintgraph;
	}

	@Override
	public AssociationEndCallExp clone() {
		AssociationEndCallExp n = new AssociationEndCallExp(this.getConstraint(), this.getSourceExp().clone(), this.getPropertyName(), this.getType(), dstRole, srcRole, ascName);
		return n;
	}

	@Override
	public String toOCL() {
		return this.getSourceExp() + "." + this.getPropertyName();
	}

	@Override
	public String getPredicateName(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("navigate_call");
		tpl.add("node_identifier", this.getId());
		tpl.add("class_name", this.getSourceExp().getType().getName());
		tpl.add("association_name", this.getAscName());
		tpl.add("dstRole", this.getDstRole());
		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("navigate_body");
		tpl.add("node_identifier", this.getId());
		tpl.add("class_name", this.getSourceExp().getType().getName());
		tpl.add("dstRole", this.getDstRole());
		tpl.add("srcRole", this.getSrcRole());
		tpl.add("state", this.getState());
		tpl.add("association_name", this.getAscName());

		tpl.add("object_getter", this.getSourceExp().getPredicateName(templateArgs).replaceAll("\\(.*\\)", ""));
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssociationEndCallExp other = (AssociationEndCallExp) obj;
		if (ascName == null) {
			if (other.ascName != null)
				return false;
		} else if (!ascName.equals(other.ascName))
			return false;
		if (dstRole == null) {
			if (other.dstRole != null)
				return false;
		} else if (!dstRole.equals(other.dstRole))
			return false;
		if (srcRole == null) {
			if (other.srcRole != null)
				return false;
		} else if (!srcRole.equals(other.srcRole))
			return false;
		return true;
	}

	@Override
	public CLGConstraint CLGConstraint() {

		// TODO Auto-generated method stub
		return null;
	}



	

}
