package ccu.pllab.tcgen.ast;

import java.util.Map;

import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
 

public class AttributeCallExp extends PropertyCallExp {

	public AttributeCallExp(Constraint obj, ASTNode source, String name, Classifier type) {
		super(obj, source, name, type);
	}

	@Override
	public CLGNode toCLG(Criterion criterion) {
		assert this.getType().equals(TypeFactory.getInstance().getClassifier("Boolean"));
		return new ConstraintNode(this.getConstraint(), this);
	}

	@Override
	public AttributeCallExp clone() {
		AttributeCallExp n = new AttributeCallExp(this.getConstraint(), this.getSourceExp().clone(), this.getPropertyName(), this.getType());
		return n;
	}

	@Override
	public String toOCL() {
		return  this.getSourceExp()+ "." + this.getPropertyName();
	}
 
	@Override
	public String getPredicateName(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("property_call");
		tpl.remove("result_name");
		tpl.add("node_identifier", this.getId());
		tpl.add("class_name", this.getSourceExp().getType().getName());
		tpl.add("property_name", this.getPropertyName());

		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("property_body");
		tpl.remove("result_name");
		tpl.add("node_identifier", this.getId());
		tpl.add("class_name", this.getSourceExp().getType().getName());
		tpl.add("property_name", this.getPropertyName());
		String tempType =this.getType().getName();
		if(tempType.startsWith("Set")||tempType.startsWith("Sequence")||tempType.startsWith("OrderedSet")||tempType.startsWith("Bag")){
			tpl.add("result_call", "[_|Result]");
		}
		else
		{
			tpl.add("result_call", "Result");
		}
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
	public CLGGraph OCL2CLG( ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CLGConstraint CLGConstraint() {
	
		// TODO Auto-generated method stub
		return null;
	}



}
