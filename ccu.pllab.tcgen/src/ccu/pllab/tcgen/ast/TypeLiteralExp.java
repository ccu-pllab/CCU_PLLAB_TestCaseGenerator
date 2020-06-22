package ccu.pllab.tcgen.ast;

 
import java.util.Map;

import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;

public class TypeLiteralExp extends LiteralExp {

	public TypeLiteralExp(Constraint obj, Classifier type) {
		super(obj, type, type.getName());
	}

	@Override
	public String getPredicateName(Map<String, String> template_args) {
		ST tpl = TemplateFactory.getTemplate("type_literal_node_call");

		tpl.add("node_identifier", this.getId());
		tpl.add("type_name", this.getType().getName());

		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> template_args) {
		ST tpl = TemplateFactory.getTemplate("type_literal_node_body");

		tpl.add("node_identifier", this.getId());
		tpl.add("type_name", this.getType().getName());

		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

}
