package ccu.pllab.tcgen.csp;
 

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import ccu.pllab.tcgen.ast.Constraint;
import ccu.pllab.tcgen.clg2path.Path;
import ccu.pllab.tcgen.libs.Predicate;
import ccu.pllab.tcgen.libs.pivotmodel.Association;
import ccu.pllab.tcgen.libs.pivotmodel.AssociationEnd;
import ccu.pllab.tcgen.libs.pivotmodel.Attribute;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;

public class CreateObject implements Predicate {

	private Model model;
	private Path path;
	private CreateObjectConfig config;

	public CreateObject(Model model, Path path, CreateObjectConfig config) {
		this.model = model;
		this.path = path;
		this.config = config;
	}

	@Override
	public String getPredicateName(Map<String, String> templateArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		write_createInstance(writer);
		writer.println();
		write_obj_creation_predicate(writer);
		writer.println();
		write_link_creation_predicate(writer);

		return sw.toString();
	}

	private void write_link_creation_predicate(PrintWriter writer) {
		for (Association asso_info : this.model.getAssociations()) {
			List<AssociationEnd> asso_role_list = asso_info.getRoleList();

			writer.println(String.format("constraints%sCard%d(CardVariables):-", asso_info.getName(), path.getId()));
			writer.println(String.format("\tconstraintsBinAssocMultiplicities(\"%s\", \"%s\", \"%s\", CardVariables).", asso_info.getName(), asso_info.getRoleList().get(0).getName(), asso_info
					.getRoleList().get(1).getName()));
			writer.println(String.format("creation%s%d(Instances, Size, S%s, S%s):-", asso_info.getName(), path.getId(), asso_info.getRoleList().get(0).getType(), asso_info.getRoleList().get(1)
					.getType()));
			writer.println(String.format("\tlength(Instances, Size),"));
			if (asso_info.isUnique()) {
				writer.println(String.format("\tlist_is_unique(Instances, 1),"));
			}
			List<String> asso_class_name_list = new ArrayList<String>();
			for (AssociationEnd role_info : asso_role_list) {
				asso_class_name_list.add("param(S" + role_info.getType() + ")");
			}
			writer.println(String.format("\t(foreach(Xi, Instances), %s do", StringUtils.join(asso_class_name_list, ", ")));

			List<String> attr_value_list = new ArrayList<String>();
			attr_value_list.add("uml_asc");
			attr_value_list.add(String.format("\"%s\"", asso_info.getName()));
			for (int count = 1; count <= asso_role_list.size(); count++) {
				attr_value_list.add("ValuePart" + count);
			}
			writer.println(String.format("\tXi = [%s],", StringUtils.join(attr_value_list, ", ")));

			writer.println(String.format("\tic:'#>'(ValuePart1, 0), ic:'#=<'(ValuePart1, S%s),", asso_role_list.get(0).getType()));
			writer.println(String.format("\tic:'#>'(ValuePart2, 0), ic:'#=<'(ValuePart2, S%s)).", asso_role_list.get(1).getType()));
			writer.println(String.format("differentLinks%s%d(X):- ", asso_info.getName(), path.getId()));
			writer.println(String.format("\tdifferentLinks(X)."));
			writer.println(String.format("orderedLinks%s%d(X):- ", asso_info.getName(), path.getId()));
			writer.println(String.format("\torderedLinks(X)."));
			writer.println(String.format("cardinalityLinks%s%d(Instances):-", asso_info.getName(), path.getId()));
			writer.println(String.format("\tlinksConstraintMultiplicities(Instances,\"%s\", \"%s\", \"%s\").", asso_info.getName(), asso_info.getRoleList().get(0).getName(), asso_info.getRoleList()
					.get(1).getName()));
			writer.println();
		}
	}

	private void write_obj_creation_predicate(PrintWriter writer) {
		for (UML2Class info : this.model.getClasses()) {
			writer.println(String.format("creation%s%d(Instances, Size):-", info.getName(), path.getId()));
			writer.println(String.format("\tlength(Instances, Size),"));
			writer.println(String.format("\t(foreach(Xi, Instances), param(Size) do"));
			List<String> attr_value_list = new ArrayList<String>();
			attr_value_list.add("uml_obj");
			attr_value_list.add(String.format("\"%s\"", info.getName()));
			attr_value_list.add("OidInteger");
			for (int count = 1; count <= info.getAttrList().size(); count++) { //getAttrAndAscList
				attr_value_list.add("Object" + count);
			}
			writer.println(String.format("\tXi = [%s],", StringUtils.join(attr_value_list, ", ")));
			if (info.getAttrList().size() == 0) {
				writer.println(String.format("\tic:'::'(OidInteger, 1..Size))."));
			} else {
				writer.println(String.format("\tic:'::'(OidInteger, 1..Size),"));
				int count = 1;
				for (; count <= info.getAttrList().size() - 1; count++) { //getAttrAndAscList
					if(info.getAttrList().get(count-1).getUpper()!=1){
						Attribute tempAttr=info.getAttrList().get(count-1);
						if(tempAttr.getType().endsWith("String")){
							writer.println(String.format("\tcollection_StringDeclDomain(%b,[65..90,97..122],0,15,%d,%d,Object%d),",
									tempAttr.getUnique(),tempAttr.getLower(),tempAttr.getUpper(),count));
						}
						else{
							writer.println(String.format("\tcollection_IntegerDeclDomain(%b,%d, %d, %d, %d, Object%d),",tempAttr.getUnique(),
							config.getIntDomain().getMinimum(), config.getIntDomain().getMaximum(),tempAttr.getLower(),tempAttr.getUpper(),count));
						}
					}
					else{
						if(info.getAttrList().get(count-1).getType().equals("String"))
						{
							writer.println(String.format("\tstringDeclDomain([65..90,97..122],0,15,Object%d),",count));
						}
						else{
							writer.println(String.format("\tic:'::'(Object%d, %d..%d),", count, config.getIntDomain().getMinimum(), config.getIntDomain().getMaximum()));
						}
					}
				}
				if(info.getAttrList().get(count-1).getUpper()!=1){
					Attribute tempAttr=info.getAttrList().get(count-1);
					if(tempAttr.getType().endsWith("String")){
						writer.println(String.format("\tcollection_StringDeclDomain(%b,[65..90,97..122],0,15,%d,%d,Object%d),",
								tempAttr.getUnique(),tempAttr.getLower(),tempAttr.getUpper(),count));
					}
					else{
						writer.println(String.format("\tcollection_IntegerDeclDomain(%b,%d, %d, %d, %d, Object%d),",tempAttr.getUnique(),
						config.getIntDomain().getMinimum(), config.getIntDomain().getMaximum(),tempAttr.getLower(),tempAttr.getUpper(),count));
					}
				}
				else{
					if(info.getAttrList().get(count-1).getType().equals("String"))
					{
						writer.println(String.format("\tstringDeclDomain([65..90,97..122],0,15,Object%d)).",count));
					}
					else{
						writer.println(String.format("\tic:'::'(Object%d, %d..%d)).", count, config.getIntDomain().getMinimum(), config.getIntDomain().getMaximum()));
					}
				}
			}
			List<String> domain_var_list = new ArrayList<String>();
			domain_var_list.add("OidInteger");
			for (int count = 1; count <= info.getAttrList().size(); count++) {
				domain_var_list.add("Integer" + count);
			}
			writer.println(String.format("differentOids%s%d(Instances) :- ", info.getName(), path.getId()));
			writer.println(String.format("\tdifferentOids(Instances)."));
			writer.println(String.format("orderedInstances%s%d(Instances) :- ", info.getName(), path.getId()));
			writer.println(String.format("\torderedInstances(Instances)."));
			writer.println();

			writer.println(String.format("parameterOf%s%d([InstancesPre, InstancesPost], [ObjectPre, ObjectPost]):-", info.getName(), path.getId()));
			writer.println(String.format("\tindex(\"%s\", %sInstanceIndex),", info.getName(), info.getName()));
			writer.println(String.format("\tnth1(%sInstanceIndex, InstancesPre, All%sInstancesPre),", info.getName(), info.getName()));
			writer.println(String.format("\tnth1(%sInstanceIndex, InstancesPost, All%sInstancesPost),", info.getName(), info.getName()));
			writer.printf("\tnth1_var(I%s, All%sInstancesPre,1),\n", info.getName(), info.getName());
			writer.println(String.format("\tnth1(I%s, All%sInstancesPre, ObjectPre),", info.getName(), info.getName()));
			writer.println(String.format("\tnth1(I%s, All%sInstancesPost, ObjectPost),", info.getName(), info.getName()));
			writer.println(String.format("\tgetOid(ObjectPre, Oid),", info.getName()));
			writer.println(String.format("\tgetOid(ObjectPost, Oid).", info.getName()));
		}

		writer.println(String.format("parameterOfInteger%d(_, [Object, Object]):-", path.getId()));
		writer.println(String.format("\tObject :: %d..%d.", config.getIntDomain().getMinimum(), config.getIntDomain().getMaximum()));
		writer.println(String.format("parameterOfBoolean%d(_, [Object, Object]):-", path.getId()));
		writer.println(String.format("\tic:make_bool(Object)."));
		writer.println(String.format("parameterOfString%d(_, [Object, Object]):-", path.getId()));
		writer.println(String.format("\tstringDeclDomain([65..90,97..122],0,15,Object)."));
	}

	private void write_createInstance(PrintWriter writer) {
		List<String> class_asso_names = new ArrayList<String>();
		List<String> obj_name_list = new ArrayList<String>();
		for (UML2Class info : this.model.getClasses()) {
			obj_name_list.add("O" + info.getName());
		}
		for (Association info : this.model.getAssociations()) {
			obj_name_list.add("L" + info.getName());
		}

		writer.println(String.format("%s(Instances):-", this.getPredicateName()));
		writer.println(String.format("\tlength(Instances, %s),", this.model.getClasses().size() + this.model.getAssociations().size()));
		writer.println(String.format("\tInstances = [%s],", StringUtils.join(obj_name_list, ", ")));
		writer.println("\t%Cardinality definitions");
		for (UML2Class info : this.model.getClasses()) {
			class_asso_names.add(info.getName());
			Range<Integer> range = config.getRangeOfInstance(info);
			writer.println(String.format("\tic:'::'(S%s, %d..%d),", info.getName(), range.getMinimum(), range.getMaximum()));
		}
		for (Association info : this.model.getAssociations()) {
			class_asso_names.add(info.getName());
			Range<Integer> range = config.getRangeOfInstance(info);
			writer.println(String.format("\tic:'::'(S%s, %d..%d),", info.getName(), range.getMinimum(), range.getMaximum()));
		}
		writer.println();

		writer.println(String.format("\tCardVariables=[S%s],", StringUtils.join(class_asso_names, ", S")));
		for (Association info : this.model.getAssociations()) {
			writer.println(String.format("\tconstraints%sCard%d(CardVariables),", info.getName(), path.getId()));
		}
		writer.println();

		writer.println("\t%Instantiation of cardinality variables");
		writer.println("\tic:'labeling'(CardVariables),");
		writer.println();

		writer.println("\t%Object creation");
		for (UML2Class info : this.model.getClasses()) {
			writer.println(String.format("\tcreation%s%d(O%s, S%s),", info.getName(), path.getId(), info.getName(), info.getName()));
			writer.println(String.format("\tdifferentOids%s%d(O%s),", info.getName(), path.getId(), info.getName()));
			writer.println(String.format("\torderedInstances%s%d(O%s),", info.getName(), path.getId(), info.getName()));
		}
		writer.println();

		writer.println("\t%Link creation");
		for (Association info : this.model.getAssociations()) {
			writer.println(String.format("\tcreation%s%d(L%s, S%s, S%s, S%s),", info.getName(), path.getId(), info.getName(), info.getName(), info.getRoleList().get(0).getType(), info.getRoleList()
					.get(1).getType()));
			writer.println(String.format("\tdifferentLinks%s%d(L%s),", info.getName(), path.getId(), info.getName()));
			writer.println(String.format("\torderedLinks%s%d(L%s),", info.getName(), path.getId(), info.getName()));
		}
		writer.println();

		for (Association info : this.model.getAssociations()) {
			writer.println(String.format("\tcardinalityLinks%s%d(Instances),", info.getName(), path.getId()));
		}

		List<String> property_name_list = new ArrayList<String>();
		List<String> asc_property_name_list = new ArrayList<String>();
		for (UML2Class info : this.model.getClasses()) {
			property_name_list.add("At" + info.getName());
		}
		for (Association info : this.model.getAssociations()) {
			asc_property_name_list.add("P" + info.getName());
		}
		Integer counting_for_invs = 0;
		Map<Integer, UML2Class> counting_mapping_class = new HashMap<Integer, UML2Class>();
		for (UML2Class info : this.model.getClasses()) {
			if (info.getInvariants().size() == 0) {
				continue;
			} else {
				counting_mapping_class.put(counting_for_invs++, info);
			}
		}
		for (int count = 0; count < counting_for_invs; count++) {
			UML2Class info = counting_mapping_class.get(count);
			writer.println(String.format("\t(foreach(I%s, O%s), param(Instances) do(", info.getName(), info.getName()));
			Map<String, String> tpl_arg = new HashMap<String, String>();
			tpl_arg.put("instances_name", "[Instances, Instances]");
			tpl_arg.put("vars_name", String.format("[[I%s, I%s]]", info.getName(), info.getName()));
			tpl_arg.put("result_name", "1");
			List<String> inv_preidcates = new ArrayList<String>();
			for (Constraint inv : info.getInvariants()) {
				inv_preidcates.add("\n\t\t" + inv.getPredicateName(tpl_arg));
			}
			writer.println(StringUtils.join(inv_preidcates, ","));
			writer.println(String.format("\n\t)),"));
		}
		writer.println("true.");
	}

	public String getPredicateName() {
		return String.format("createInstances%d", this.path.getId());
	}

}
