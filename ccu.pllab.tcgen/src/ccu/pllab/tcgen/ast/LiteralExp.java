package ccu.pllab.tcgen.ast;

 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;

public class LiteralExp extends ASTNode {

	private Classifier type;
	private String value;

	public LiteralExp(Constraint obj, Classifier type, String value) {
		super(obj);
		this.type = type;
		this.value = value;
	}

	@Override
	public Classifier getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	@Override
	public CLGNode toCLG(Criterion criterion) {
		
		assert this.getType().equals(TypeFactory.getInstance().getClassifier("Boolean"));
		
		return new ConstraintNode(this.getConstraint(), this);
	}
	

	@Override
	public CLGGraph OCL2CLG() {
		String StringType = this.type.toString();
		CLGLiteralNode literalConstraint = new CLGLiteralNode(StringType,this.value);
		assert this.getType().equals(TypeFactory.getInstance().getClassifier("Boolean"));
		CLGGraph constraintgraph = new CLGGraph(literalConstraint);
		
		return constraintgraph;
		
	}

	@Override
	public LiteralExp clone() {
		LiteralExp n = new LiteralExp(this.getConstraint(), this.getType(), this.getValue());
		return n;
	}

	@Override
	public String toOCL() {
	   
		return this.getValue();
	}

	@Override
	public String getLabelForGraphviz() {
		return this.getValue();
	}

	@Override
	public String getState() {
		return "invariant";
	}

	@Override
	public List<INode> getNextNodes() {
		return new ArrayList<INode>();
	}

	@Override
	public String getPredicateName(Map<String, String> template_args) {
		ST tpl = TemplateFactory.getTemplate("literal_call");
		tpl.add("node_identifier", this.getId());
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> template_args) {
		ST tpl = TemplateFactory.getTemplate("literal_body");
		tpl.add("node_identifier", this.getId());
		if (this.getType().getName().equals("Boolean")) {
			Boolean b = Boolean.valueOf(this.getValue());
			tpl.add("value", (b) ? "1" : "0");

		} else if (this.getType().getName().equals("String")) {
			char[] chartemp = this.getValue().toCharArray();
			String tempstring ="[ "+chartemp.length;
			for(char temp:chartemp){
				tempstring += ", "+ (int)temp;
			}
			tempstring+="]";
			tpl.add("value",tempstring);
		} else {
			tpl.add("value", this.getValue());
		}
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public ASTNode toDeMorgan() {
		if (this.type.equals(TypeFactory.getInstance().getClassifier("Boolean"))) {
			if (this.value.equals(Boolean.valueOf(true).toString())) {
				return new LiteralExp(this.getConstraint(), TypeFactory.getInstance().getClassifier("Boolean"), Boolean.valueOf(false).toString());
			} else {
				return new LiteralExp(this.getConstraint(), TypeFactory.getInstance().getClassifier("Boolean"), Boolean.valueOf(true).toString());
			}
		} else {
			return this; 
		}
	}

	@Override 
	public ASTNode toPreProcessing() {
		return this;
	} 

	@Override
	public CLGConstraint CLGConstraint() {
		CLGConstraint LiteralConstraint=new CLGLiteralNode(this.getValue());
		
		
		
		return LiteralConstraint;
	}

	




}
