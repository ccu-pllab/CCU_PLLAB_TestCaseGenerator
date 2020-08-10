package ccu.pllab.tcgen.libs.pivotmodel;

 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONArray;
import org.json.JSONObject;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGStartNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.ast.Constraint;
import ccu.pllab.tcgen.ast.OperationCallExp;
import ccu.pllab.tcgen.ast.PropertyCallExp;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.JsonUtils;
import ccu.pllab.tcgen.libs.Predicate;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;

public class UML2Operation extends Operation implements Predicate {
	ArrayList<ImmutablePair<String, String>> mArgList;
	String mRetType;
	UML2Class owner;
	private List<Constraint> preconstraints;
	private List<Constraint> postconstraints;

	public UML2Operation(UML2Class owner, JSONObject method_obj) {
		super(method_obj.optString("name"));
		this.owner = owner;
		this.preconstraints = new ArrayList<Constraint>();
		this.postconstraints = new ArrayList<Constraint>();
		mArgList = new ArrayList<ImmutablePair<String, String>>();
		JSONArray arg_list_array = method_obj.optJSONArray("argList");
		arg_list_array = JsonUtils.SortIDFile(arg_list_array);

		for (int j = 0; j < arg_list_array.length(); j++) {
			JSONObject arg_obj = arg_list_array.optJSONObject(j);
			ImmutablePair<String, String> arg = JsonUtils.ParseNameTypePair(arg_obj);
			mArgList.add(arg);
		}
		// return type
		mRetType = method_obj.optString("ret_type");
	}

	public ArrayList<ImmutablePair<String, String>> getArgList() {
		return mArgList;
	}

	public String getRetType() {
		return mRetType;
	}

	public UML2Class getOwner() {
		return this.owner;
	}

	public void setArgList(ArrayList<ImmutablePair<String, String>> mArgList) {
		this.mArgList = mArgList;
	}

	public void setRetType(String mRetType) {
		this.mRetType = mRetType;
	}

	public List<Constraint> getPreConstraints() {
		return this.preconstraints;
	}

	public List<Constraint> getPostConstraints() {
		return this.postconstraints;
	}

	public void addPrecondition(Constraint constr) {
		this.preconstraints.add(constr);
	}

	public void addPostcondition(Constraint constr) {
		this.postconstraints.add(constr);
	}

public CLGGraph getCLG(Criterion criterion) { 
		
		CLGGraph preconstraints = new CLGGraph();
		CLGGraph postconstraints = new CLGGraph();
		for (Constraint constraint : this.getPreConstraints()) {

			
			preconstraints=constraint.OCL2CLG();
			
		} 
		for (Constraint constraint : this.getPostConstraints()) {
			  
			postconstraints=constraint.OCL2CLG();
			
		}
		if(preconstraints.getConstraintCollection().size() ==0)
		{
			return postconstraints;
		}
		
		preconstraints.graphAnd(postconstraints);

	
		
		return preconstraints;
	}


public CLGGraph getInvalidCLG(Criterion criterion) {
	
	CLGGraph nodes = new CLGGraph();
 
	
	for (Constraint constraint : this.getPreConstraints()) {
		Constraint invalid_constraint = (Constraint) constraint.clone();
		OperationCallExp not_spec = new OperationCallExp(constraint.getConstraint(), constraint.getSpecification().clone(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
		invalid_constraint.setSpecification(not_spec);
	
		nodes=invalid_constraint.OCL2CLG();
		
		
		
		for(Constraint sub_constraint : this.getPreConstraints()) {
			if(constraint.equals(sub_constraint)) {
				continue; 
			}
	
			
			nodes.graphAnd(sub_constraint.OCL2CLG());
		}
				
	
	}



	return nodes;
}

	@Override
	public String getPredicateName(Map<String, String> templateArgs) {
		String instances_name = "Instances";
		if (templateArgs.get("instances_name") != null) {
			instances_name = templateArgs.get("instances_name");
		}
		String vars_name = "Vars";
		if (templateArgs.get("vars_name") != null) {
			vars_name = templateArgs.get("vars_name");
		}
		String result_name = "Result";
		if (templateArgs.get("result_name") != null) {
			result_name = templateArgs.get("result_name");
		}
		return String.format("method_body_%s_%s(%s, %s, %s)", this.getOwner().getName(), this.getName(), instances_name, vars_name, result_name);
	}

	@Override
	public String getEntirePredicate(final Map<String, String> templateArgs) {
		if (this.getOwner().getName().equals(this.getName()) && this.getRetType().equals(this.getName())) {
			return handle_constructor(templateArgs);
		} else {
			return handle_normal_method(templateArgs);
		}
	}

	private String handle_constructor(Map<String, String> templateArgs) {
		String vars_name = "Vars";
		if (templateArgs.get("vars_name") != null) {
			vars_name = templateArgs.get("vars_name");
		}
		String result_name = "Result";
		if (templateArgs.get("result_name") != null) {
			result_name = templateArgs.get("result_name");
		}
		if (this.getPreConstraints().size() + this.getPostConstraints().size() == 0) {
			HashMap<String, String> tpl_args = new HashMap<String, String>();
			tpl_args.put("instances_name", "_");
			tpl_args.put("vars_name", "_");
			tpl_args.put("result_name", "_");
			return this.getPredicateName(tpl_args) + ".";
		}
		String result = this.getPredicateName(templateArgs) + " :-";
		result += String.format("\n\tappend(%s, [%s, %s|_], NewVars),", vars_name, result_name, result_name);
		List<String> constraint_predicates = new ArrayList<String>();

		HashMap<String, String> templateArgs2 = new HashMap<String, String>();
		templateArgs2.putAll(templateArgs);
		templateArgs2.put("vars_name", "NewVars");
		templateArgs2.put("result_name", "1");

		for (Constraint constr : this.getPreConstraints()) {
			constraint_predicates.add("\n\t" + constr.getPredicateName(templateArgs2));
		}
		if (this.getPostConstraints().size() > 0) {
			for (Constraint constr : this.getPostConstraints()) {
				constraint_predicates.add("\n\t" + constr.getPredicateName(templateArgs2));
			}
		} else {
			constraint_predicates.add(String.format("%s = %s", result_name, result_name));
		}
		result += StringUtils.join(constraint_predicates, ",") + ",";
		result += "\n\tnth1(1, NewVars, Self),";
		result += "\n\tnth1(2, Self, SelfPost),";
		result += "\n\tocl_attributeCall(_, \"" + this.getRetType() + "\", \"type\", SelfPost, uml_obj),";
		result += "\n\tocl_attributeCall(_, \"" + this.getRetType() + "\", \"name\", SelfPost, \"" + this.getRetType() + "\").";
		return result;
	}

	private String handle_normal_method(final Map<String, String> templateArgs) {
		String vars_name = "Vars";
		if (templateArgs.get("vars_name") != null) {
			vars_name = templateArgs.get("vars_name");
		}
		String result_name = "Result";
		if (templateArgs.get("result_name") != null) {
			result_name = templateArgs.get("result_name");
		}
		if (this.getPreConstraints().size() + this.getPostConstraints().size() == 0) {
			HashMap<String, String> tpl_args = new HashMap<String, String>();
			tpl_args.put("instances_name", "_");
			tpl_args.put("vars_name", "_");
			tpl_args.put("result_name", "_");
			return this.getPredicateName(tpl_args) + ".";
		}
		String result = this.getPredicateName(templateArgs) + " :-";
		result += String.format("\n\tappend(%s, [[%s, %s]|_], NewVars),", vars_name, result_name, result_name);
		List<String> constraint_predicates = new ArrayList<String>();

		HashMap<String, String> templateArgs2 = new HashMap<String, String>();
		templateArgs2.putAll(templateArgs);
		templateArgs2.put("vars_name", "NewVars");
		templateArgs2.put("result_name", "1");

		for (Constraint constr : this.getPreConstraints()) {

			constraint_predicates.add("\n\t" + constr.getPredicateName(templateArgs2));
		}
		if (this.getPostConstraints().size() > 0) {
			for (Constraint constr : this.getPostConstraints()) {
				constraint_predicates.add("\n\t" + constr.getPredicateName(templateArgs2));
			}
		} else {
			constraint_predicates.add(String.format("%s = %s", result_name, result_name));
		}
		result += StringUtils.join(constraint_predicates, ",") + ".";
		return result;
	}

	public HashMap<String, Set<PropertyCallExp>> getChangedProperties() {
		final HashMap<String, Set<PropertyCallExp>> changedPropertyCallExprs = new HashMap<String, Set<PropertyCallExp>>();
		for (Constraint constraint : this.postconstraints) {
			for (Entry<String, Set<PropertyCallExp>> e : constraint.getChangedProperties().entrySet()) {
				if (changedPropertyCallExprs.get(e.getKey()) == null) {
					changedPropertyCallExprs.put(e.getKey(), new HashSet<PropertyCallExp>());
				}
				changedPropertyCallExprs.get(e.getKey()).addAll(e.getValue());
			}
		}
		return changedPropertyCallExprs;
	}

	public ArrayList<String>  roger() {
		final ArrayList<String> changedPropertyCallExprs = new ArrayList<String>();

		for (Constraint constraint : this.postconstraints) {
			for (Entry<String, Set<PropertyCallExp>> e : constraint.getChangedProperties().entrySet()) {
				for(PropertyCallExp a : e.getValue())
					
					
					
					changedPropertyCallExprs.add(a.toString());
			
				
				
			}
		}
		
		
		
		
		return changedPropertyCallExprs;
	}
	
	
	public List<Set<PropertyCallExp>> getChangedPropertiesOrderByParameters() {
		final HashMap<String, Set<PropertyCallExp>> changedPropertyCallExprs = this.getChangedProperties();
		List<Set<PropertyCallExp>> list = new ArrayList<Set<PropertyCallExp>>();
		list.add(changedPropertyCallExprs.get("self"));
		for (ImmutablePair<String, String> arg : this.getArgList()) {
			list.add(changedPropertyCallExprs.get(arg.left));
		}
		return list;
	}
}
