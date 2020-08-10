package ccu.pllab.tcgen.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import scala.actors.threadpool.Arrays;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;

 
public class CollectionRange extends ASTNode {

	private Classifier type;
	private ASTNode first;
	private ASTNode last;

	public CollectionRange(Constraint constraint, Classifier classifier, ASTNode first, ASTNode last) {
		super(constraint);
		this.type = classifier;
		this.first = first;
		this.last = last;
	}

	@Override
	public String getPredicateName(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("collection_range_call");
		tpl.add("node_identifier", this.getId());
		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		HashMap<String, String> arg_tpl_args = new HashMap<String, String>();

		ST tpl = TemplateFactory.getTemplate("collection_range_body");
		tpl.add("node_identifier", this.getId());
		tpl.add("first_predicate", this.first.getPredicateName(arg_tpl_args).replaceAll("\\(.*\\)", ""));
		tpl.add("last_predicate", this.last.getPredicateName(arg_tpl_args).replaceAll("\\(.*\\)", ""));
		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<INode> getNextNodes() {
		return new ArrayList<INode>(Arrays.asList(new INode[] { this.first, this.last }));
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
		return this.first.toOCL() + ".." + this.last.toOCL();
	}

	@Override
	public String getLabelForGraphviz() {
		return this.toOCL();
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
