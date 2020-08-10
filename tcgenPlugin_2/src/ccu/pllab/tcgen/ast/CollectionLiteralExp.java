package ccu.pllab.tcgen.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
 

public class CollectionLiteralExp extends ASTNode {

	private List<ASTNode> collectionParts;
	private Classifier type;

	public CollectionLiteralExp(Constraint constraint, Classifier classifier, List<ASTNode> literalParts) {
		super(constraint);
		this.collectionParts = literalParts;
		this.type = classifier;
	}

	@Override
	public String getPredicateName(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("collection_literal_call");
		tpl.add("node_identifier", this.getId());
		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("collection_literal_body");
		tpl.add("node_identifier", this.getId());

		List<String> part_predicates = new ArrayList<String>();
		for (ASTNode part : this.collectionParts) {
			HashMap<String, String> arg_tpl_args = new HashMap<String, String>();
			part_predicates.add(part.getPredicateName(arg_tpl_args).replaceAll("\\(.*\\)", ""));
		}

		tpl.add("collection_parts", part_predicates);
		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public List<INode> getNextNodes() {
		return new ArrayList<INode>(collectionParts);
	}

	@Override
	public CLGNode toCLG(Criterion criterion) {
		throw new IllegalStateException();
	}

	@Override
	public Classifier getType() {
		return this.type;
	}

	@Override
	public String getState() {
		return "both";
	}

	@Override
	public ASTNode clone() {
		return this;
	}

	@Override
	public String toOCL() {
		List<String> parts_ocl = new ArrayList<String>();
		for (ASTNode part : this.collectionParts) {
			parts_ocl.add(part.toOCL());
		}
		return this.getType().getName().split("\\(")[0] + "{" + StringUtils.join(parts_ocl, ",") + "}";
	}

	@Override
	public String getLabelForGraphviz() {
		return this.collectionParts.toString();
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
	public CLGGraph OCL2CLG() {
		throw new IllegalStateException();
	}

	@Override
	public CLGConstraint CLGConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

}
