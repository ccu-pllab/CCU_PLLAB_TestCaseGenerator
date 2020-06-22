package ccu.pllab.tcgen.ast;

 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGMethodInvocationNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
//import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.ConnectionNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.facade.FacadeConfig;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.node.GraphVisitor;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.node.NodeVisitHandler;
import ccu.pllab.tcgen.libs.node.QueueFrontier;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.CollectionType;
import ccu.pllab.tcgen.libs.pivotmodel.type.PrimitiveType;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;

public class OperationCallExp extends PropertyCallExp {
	private List<ASTNode> parameters;
	private boolean isMethod;

	public OperationCallExp(Constraint obj, ASTNode source, String name, Classifier type, boolean isMethod, Collection<ASTNode> parameters) {
		super(obj, source, name, type);
		this.parameters = new ArrayList<ASTNode>(parameters);
		this.isMethod = isMethod;
		for (ASTNode p : this.parameters) {
			p.addPreviousNode(this);
		}
		if (isMethod) {
			NodeVisitHandler<ASTNode> nodeAnnotator = new NodeVisitHandler<ASTNode>() {
			 
			
				@Override
				public void visit(ASTNode current_node) {
					if (current_node instanceof VariableExp) {
						((VariableExp) current_node).setState("both"); 
					}
				}
			};  
			GraphVisitor<ASTNode> bfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.PREORDER, new QueueFrontier<ASTNode>());
			for (ASTNode node : this.parameters) {
				bfs.traverse(node, nodeAnnotator);
			}
			bfs.traverse(source, nodeAnnotator);
		} 
	}

	public OperationCallExp(Constraint obj, ASTNode source, String name, Classifier type, boolean isMethod, ASTNode... parameters) {
		this(obj, source, name, type, isMethod, Arrays.asList(parameters)); 
	}

	public List<ASTNode> getParameterExps() {
		return parameters;
	}

	public void setParameterExpAtPosition(int position, ASTNode parameter) {
		this.parameters.set(position, parameter);
	}

	public boolean isMethod() {
		return isMethod;
	}

	
	@Override
	public ccu.pllab.tcgen.clg.CLGNode toCLG(Criterion criterion) {
		/*
		if (this.isMethod()) {
			return new ConstraintNode(this.getConstraint(), this);
		} else if (this.isRelationOperation() && this.getParameterExps().get(0) instanceof IterateExp) {
			VariableExp result = new VariableExp(this.getConstraint(), String.format("#ResultAcc%d", ((IterateExp) this.getParameterExps().get(0)).getIterate_id()), ((ASTNode) this.getNextNodes()
					.get(1)).getType(), this.getConstraint().getKind().getName());
			OperationCallExp var_equal_result_node = new OperationCallExp(this.getConstraint(), this.getSourceExp(), this.getPropertyName(), TypeFactory.getInstance().getClassifier("Boolean"), false,
					result);
			ConstraintNode clgVariableEqualResultNode = new ConstraintNode(this.getConstraint(), var_equal_result_node);
			ccu.pllab.tcgen.clg.CLGNode clgIterateNode = this.getParameterExps().get(0).toCLG(criterion);
			clgIterateNode.getEndNode().addNextNode(clgVariableEqualResultNode);
			clgIterateNode.setEndNode(clgVariableEqualResultNode);
			return clgIterateNode;
		} else if (this.getPropertyName().equals("not")) {
			final ASTNode root_node = this.getSourceExp().clone();
			if (canRetainNotOperation(root_node)) {
				return new ConstraintNode(this.getConstraint(), this);
			} else if (root_node instanceof OperationCallExp && ((OperationCallExp) root_node).getPropertyName().equals("xor")) {
				OperationCallExp _exp = (OperationCallExp) root_node;
				OperationCallExp not_left_node = new OperationCallExp(_exp.getConstraint(), _exp.getSourceExp(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
				OperationCallExp not_right_node = new OperationCallExp(_exp.getConstraint(), _exp.getParameterExps().get(0), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
				ccu.pllab.tcgen.clg.CLGNode clg_left_node = _exp.getSourceExp().toCLG(criterion);
				ccu.pllab.tcgen.clg.CLGNode clg_right_node = _exp.getParameterExps().get(0).toCLG(criterion);
				ccu.pllab.tcgen.clg.CLGNode clg_not_left_node = not_left_node.toCLG(criterion);
				ccu.pllab.tcgen.clg.CLGNode clg_not_right_node = not_right_node.toCLG(criterion);
				ConnectionNode begin_node = new ConnectionNode(_exp.getConstraint());
				ConnectionNode end_node = new ConnectionNode(_exp.getConstraint());
				begin_node.addNextNode(clg_left_node);
				begin_node.addNextNode(clg_not_left_node);
				clg_left_node.getEndNode().addNextNode(clg_right_node);
				clg_not_left_node.getEndNode().addNextNode(clg_not_right_node);
				clg_right_node.getEndNode().addNextNode(end_node);
				clg_not_right_node.getEndNode().addNextNode(end_node);
				begin_node.setEndNode(end_node);
				return begin_node;
			} else {
				ASTNode inverse_node = root_node.toDeMorgan();
				return inverse_node.toCLG(criterion);
			}
		} else if (criterion.equals(CriterionFactory.Criterion.dc)) {
			return new ConstraintNode(this.getConstraint(), this);
		} else if (this.getPropertyName().equals("and")) {
			ccu.pllab.tcgen.clg.CLGNode clgNodeLeft = this.getSourceExp().toCLG(criterion);
			CLGNode clgNodeRight = this.parameters.get(0).toCLG(criterion);

			clgNodeLeft.getEndNode().addNextNode(clgNodeRight);
			clgNodeLeft.setEndNode(clgNodeRight.getEndNode());
			return clgNodeLeft;
		} else if ((this.getPropertyName().equals("or")) && criterion.equals(CriterionFactory.Criterion.mcc)) {
			CLGNode clgBeginConnecting = new ConnectionNode(this.getConstraint());
			CLGNode clgEndConnecting = new ConnectionNode(this.getConstraint());
			CLGNode clgLeft_1 = this.getSourceExp().toCLG(criterion);
			CLGNode clgRight_1 = this.parameters.get(0).toCLG(criterion);
			OperationCallExp astNotLeft = new OperationCallExp(this.getConstraint(), this.getSourceExp(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			OperationCallExp astNotRight = new OperationCallExp(this.getConstraint(), this.parameters.get(0), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			CLGNode clgNotLeft_2 = astNotLeft.toCLG(criterion);
			CLGNode clgRight_2 = this.parameters.get(0).toCLG(criterion);
			CLGNode clgLeft_3 = this.getSourceExp().toCLG(criterion);
			CLGNode clgNotRight_3 = astNotRight.toCLG(criterion);

			clgBeginConnecting.addNextNode(clgLeft_1);
			clgBeginConnecting.addNextNode(clgNotLeft_2);
			clgBeginConnecting.addNextNode(clgLeft_3);

			clgLeft_1.getEndNode().addNextNode(clgRight_1);
			clgNotLeft_2.getEndNode().addNextNode(clgRight_2);
			clgLeft_3.getEndNode().addNextNode(clgNotRight_3);
			clgRight_1.getEndNode().addNextNode(clgEndConnecting);
			clgRight_2.getEndNode().addNextNode(clgEndConnecting);
			clgNotRight_3.getEndNode().addNextNode(clgEndConnecting);
			clgBeginConnecting.setEndNode(clgEndConnecting);

			return clgBeginConnecting;
		} else if (this.getPropertyName().equals("or")) {
			CLGNode clgBeginConnecting = new ConnectionNode(this.getConstraint());
			CLGNode clgEndConnecting = new ConnectionNode(this.getConstraint());
			CLGNode clgLeft = this.getSourceExp().toCLG(criterion);
			CLGNode clgRight = this.parameters.get(0).toCLG(criterion);
			ASTNode astNotL = new OperationCallExp(this.getConstraint(), this.getSourceExp().clone(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			ASTNode astNotR = new OperationCallExp(this.getConstraint(), this.getParameterExps().get(0).clone(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			CLGNode clgNotLeft = astNotL.toCLG(criterion);
			CLGNode clgNotRight = astNotR.toCLG(criterion);
			
			clgBeginConnecting.addNextNode(clgLeft);
			clgBeginConnecting.addNextNode(clgNotLeft);
			clgLeft.getEndNode().addNextNode(clgNotRight);
			clgNotLeft.getEndNode().addNextNode(clgRight);
			clgRight.getEndNode().addNextNode(clgEndConnecting);
			clgNotRight.getEndNode().addNextNode(clgEndConnecting);
			clgBeginConnecting.setEndNode(clgEndConnecting);
			return clgBeginConnecting;
		} else if (this.getPropertyName().equals("xor")) {
			OperationCallExp not_left_node = new OperationCallExp(this.getConstraint(), this.getSourceExp(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			OperationCallExp not_right_node = new OperationCallExp(this.getConstraint(), this.getParameterExps().get(0), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			CLGNode clg_left_node = this.getSourceExp().toCLG(criterion);
			CLGNode clg_right_node = this.getParameterExps().get(0).toCLG(criterion);
			CLGNode clg_not_left_node = not_left_node.toCLG(criterion);
			CLGNode clg_not_right_node = not_right_node.toCLG(criterion);
			ConnectionNode begin_node = new ConnectionNode(this.getConstraint());
			ConnectionNode end_node = new ConnectionNode(this.getConstraint());
			begin_node.addNextNode(clg_not_left_node);
			begin_node.addNextNode(clg_left_node);
			clg_not_left_node.getEndNode().addNextNode(clg_right_node);
			clg_left_node.getEndNode().addNextNode(clg_not_right_node);
			clg_right_node.getEndNode().addNextNode(end_node);
			clg_not_right_node.getEndNode().addNextNode(end_node);
			begin_node.setEndNode(end_node);
			return begin_node;
		} else if (this.getPropertyName().equals("implies")) {
			OperationCallExp not_left_node = new OperationCallExp(this.getConstraint(), this.getSourceExp(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			OperationCallExp not_left_or_right_node = new OperationCallExp(this.getConstraint(), not_left_node, "or", TypeFactory.getInstance().getClassifier("Boolean"), false,
					this.getParameterExps());
			return not_left_or_right_node.toCLG(criterion);
		}*/
		return new ConstraintNode(this.getConstraint(), this);
	}

	@Override
	public OperationCallExp clone() {
		List<ASTNode> new_params = new ArrayList<ASTNode>();
		for (ASTNode p : parameters) {
			new_params.add(p.clone());
		}
		OperationCallExp n = new OperationCallExp(this.getConstraint(), this.getSourceExp().clone(), this.getPropertyName(), this.getType(), this.isMethod(), new_params);
		n.setAttributes(this.getAttributes());
		return n;
	}

	@Override
	public String toOCL() {
		
		String result = "";
		if (this.isMethod()) {
		
			result += this.getSourceExp();
			result += "." + this.getPropertyName();
			result += "(";
			result += StringUtils.join(this.getParameterExps(), ", ");
			result += ")";
		} else {
			if (this.getPropertyName().equals("not")) {
				result += "(" + this.getPropertyName() + " " + this.getSourceExp() + " ";
				result += ")";
			
			} else {
				result += "(" + this.getSourceExp() + " " + this.getPropertyName() + " ";
				result += StringUtils.join(this.getParameterExps(), ", ");
				result += ")";
				
			}
		}
		
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
		if (!this.isMethod()) {
			//System.out.println("*************"+handle_operation("call", template_args));
			return handle_operation("call", template_args);
		} else {
			return handle_method_invoke("call", template_args);
		}
	}

	@Override
	public String getEntirePredicate(Map<String, String> template_args) {
		if (!this.isMethod()) {
			return handle_operation("body", template_args);
		} else {
			return handle_method_invoke("body", template_args);
		}
	}

	private String handle_method_invoke(String type, final Map<String, String> template_args) {
		if (this.getSourceExp().getType().getName().equals(this.getPropertyName()) && type.equals("body")) {
			return handle_constructor_invoke(type, template_args);
		}
		ST tpl = TemplateFactory.getTemplate("method_call_" + type);
		tpl.add("node_identifier", this.getId());
		tpl.add("method_name", this.getPropertyName());
		tpl.add("class_name", this.getSourceExp().getType().getName());
		List<ASTNode> var_exps = new ArrayList<ASTNode>();
		var_exps.add(this.getSourceExp());
		var_exps.addAll(this.getParameterExps());
		List<String> vars_predicates = new ArrayList<String>();
		for (ASTNode args : var_exps) {
			HashMap<String, String> arg_tpl_args = new HashMap<String, String>();
			vars_predicates.add(args.getPredicateName(arg_tpl_args).replaceAll("\\(.*\\)", ""));
		}
		tpl.add("vars_predicate", vars_predicates);
		tpl.add("state", this.getState());
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	private String handle_constructor_invoke(String type, Map<String, String> template_args) {
		ST tpl = TemplateFactory.getTemplate("constructor_call_" + type);
		tpl.add("node_identifier", this.getId());
		tpl.add("method_name", this.getPropertyName());
		tpl.add("class_name", this.getSourceExp().getType().getName());
		List<ASTNode> var_exps = new ArrayList<ASTNode>();
		var_exps.addAll(this.getParameterExps());
		List<String> vars_predicates = new ArrayList<String>();

		for (ASTNode args : var_exps) {
			HashMap<String, String> arg_tpl_args = new HashMap<String, String>();
			vars_predicates.add(args.getPredicateName(arg_tpl_args).replaceAll("\\(.*\\)", ""));
		}
		tpl.add("vars_predicate", vars_predicates);
		tpl.add("state", this.getState());
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	private String handle_operation(final String type, final Map<String, String> template_args) {
		if (this.getSourceExp().getType() instanceof PrimitiveType) {
			if (this.getParameterExps().size() == 0) {
				return handle_primitive_unary(type, template_args);
			} else if (this.getParameterExps().size() == 1) {
				return handle_primitive_binary(type, template_args);
			} else if (this.getParameterExps().size() == 2) {
				return handle_primitive_triary(type, template_args);
			}
		}
		if (this.getSourceExp().getType() instanceof UML2Class && this.getType().getName().equals("Boolean")) {
			return handle_object_relation(type, template_args);
		}
		if (this.getSourceExp().getType() instanceof CollectionType && translate_operator_to_operation_name(this.getPropertyName()).equals("equals")) {
			return handle_collection_equals(type, template_args);
		}
		String operation_name = translate_operator_to_operation_name(this.getPropertyName());
		throw new IllegalStateException("unable to handle operation: " + operation_name);
	}

	private String handle_collection_equals(String type, Map<String, String> template_args) {
		ST tpl;
		if (type.equals("call")) {
			tpl = TemplateFactory.getTemplate("operation_ocl_collection_equals_call");
		} else {
			if (this.getAttribute("dummy_assign").equals(Boolean.toString(true))) {
				tpl = TemplateFactory.getTemplate("dummy_collection_assign");
			} else {
				tpl = TemplateFactory.getTemplate("operation_ocl_collection_equals_body");
			}
		}
		tpl.add("node_identifier", this.getId());
		if (type.equals("body")) {
			tpl.add("type", this.getSourceExp().getType().getName().toLowerCase().split("\\(")[0]);
			tpl.add("predicate", this.getSourceExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
			tpl.add("predicate2", this.getParameterExps().get(0).getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		}
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	private String handle_object_relation(String type, Map<String, String> template_args) {
		ST tpl;
		if (type.equals("call")) {
			tpl = TemplateFactory.getTemplate("operation_ocl_object_relation_call");
		} else {
			if (this.getAttribute("dummy_assign").equals(Boolean.toString(true))) {
				tpl = TemplateFactory.getTemplate("dummy_object_assign");
			} else {
				tpl = TemplateFactory.getTemplate("operation_ocl_object_relation_body");
			}
		}
		tpl.add("node_identifier", this.getId());
		tpl.remove("name");
		tpl.add("name", translate_operator_to_operation_name(this.getPropertyName()));
		if (type.equals("body")) {
			tpl.add("type1", this.getSourceExp().getType().getName());
			tpl.add("type2", this.getParameterExps().get(0).getType().getName());
			tpl.add("predicate", this.getSourceExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
			tpl.add("predicate2", this.getParameterExps().get(0).getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		}
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	private String handle_primitive_triary(String type, Map<String, String> template_args) {
		ST tpl;
		if (type.equals("call")) {
			tpl = TemplateFactory.getTemplate("operation_ocl_primitive_operation_call");
		} else {
			if (this.getAttribute("dummy_assign").equals(Boolean.toString(true))) {
				tpl = TemplateFactory.getTemplate("dummy_primitive_assign");
			} else {
				tpl = TemplateFactory.getTemplate("operation_ocl_primitive_triary_operation_body");
			}
		}
		tpl.add("node_identifier", this.getId());
		tpl.add("type", type_mapping(this.getSourceExp().getType()));
		if (translate_operator_to_operation_name(this.getPropertyName()).equals("minus")) {
			tpl.add("name", "binary_" + translate_operator_to_operation_name(this.getPropertyName()));
		} else {
			tpl.add("name", translate_operator_to_operation_name(this.getPropertyName()));
		}
		if (type.equals("body")) {
			tpl.add("predicate", this.getSourceExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
			tpl.add("predicate2", this.getParameterExps().get(0).getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
			tpl.add("predicate3", this.getParameterExps().get(1).getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		}
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	private String handle_primitive_binary(String type, Map<String, String> template_args) {
		ST tpl;
		if (type.equals("call")) {
			tpl = TemplateFactory.getTemplate("operation_ocl_primitive_operation_call");
		} else {
			if (this.getAttribute("dummy_assign").equals(Boolean.toString(true))) {
				tpl = TemplateFactory.getTemplate("dummy_primitive_assign");
			} else {
				tpl = TemplateFactory.getTemplate("operation_ocl_primitive_binary_operation_body");
			}
		}
		tpl.add("node_identifier", this.getId());
		tpl.add("type", type_mapping(this.getSourceExp().getType()));
		if (translate_operator_to_operation_name(this.getPropertyName()).equals("minus")) {
			tpl.add("name", "binary_" + translate_operator_to_operation_name(this.getPropertyName()));
		} else {
			tpl.add("name", translate_operator_to_operation_name(this.getPropertyName()));
		}
		if (type.equals("body")) {
			tpl.add("predicate", this.getSourceExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
			tpl.add("predicate2", this.getParameterExps().get(0).getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		}
		if (type_mapping(this.getSourceExp().getType()).equals("string")) {// String
																			// replace
																			// some
			switch (tpl.getAttribute("name").toString()) {
			case "plus":
				tpl.remove("name");
				tpl.add("name", "concat");
				break;
			}
		}
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	private String handle_primitive_unary(String type, Map<String, String> template_args) {
		ST tpl;
		if (type.equals("call")) {
			tpl = TemplateFactory.getTemplate("operation_ocl_primitive_operation_call");
		} else {
			tpl = TemplateFactory.getTemplate("operation_ocl_primitive_unary_operation_body");
		}
		tpl.add("node_identifier", this.getId());
		tpl.add("type", type_mapping(this.getType()));
		if (translate_operator_to_operation_name(this.getPropertyName()).equals("minus")) {
			tpl.add("name", "unary_" + translate_operator_to_operation_name(this.getPropertyName()));
		} else {
			tpl.add("name", translate_operator_to_operation_name(this.getPropertyName()));
		}
		if (type.equals("body")) {
			tpl.add("predicate", this.getSourceExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		}
		if (type_mapping(this.getSourceExp().getType()).equals("string")) {// 'a'.size()
																			// integer=>string
			tpl.remove("type");
			tpl.add("type", "string");
		}
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	private String translate_operator_to_operation_name(String operation_name) {
		if (operation_name.equals("=")) {
			operation_name = "equals";
		} else if (operation_name.equals("<")) {
			operation_name = "less_than";
		} else if (operation_name.equals(">")) {
			operation_name = "greater_than";
		} else if (operation_name.equals("+")) {
			operation_name = "plus";
		} else if (operation_name.equals("-")) {
			operation_name = "minus";
		} else if (operation_name.equals("*")) {
			operation_name = "times";
		} else if (operation_name.equals("/")) {
			operation_name = "div";
		} else if (operation_name.equals("<=")) {
			operation_name = "less_equal";
		} else if (operation_name.equals(">=")) {
			operation_name = "greater_equal";
		} else if (operation_name.equals("<>")) {
			operation_name = "not_equals";
		}
		else if (operation_name.equals("%")) {
			operation_name = "mod";
		}
		else if (operation_name.equals("&&")) {
			operation_name = "and";
		}
		else if (operation_name.equals("||")) {
			operation_name = "or";
		}
		return operation_name;
	}

	private String type_mapping(Classifier type) {
		if (type instanceof PrimitiveType) {
			return type.getName().toLowerCase();
		} else if (type instanceof CollectionType) {
			return type.getName().toLowerCase().split("\\(")[0];
		}
		return "object";
	}

	@Override
	public ASTNode toDeMorgan() {
		if (this.getPropertyName().equals("not")) {
			return this.getSourceExp();
		} else if (this.isMethod()) {
			return this;
		} else if (this.getPropertyName().equals("and") || this.getPropertyName().equals("&&")) {
			ASTNode sourceNode;
			ASTNode parameterNode;
			if (canRetainNotOperation(this.getSourceExp())) {
				sourceNode = new OperationCallExp(this.getConstraint(), this.getSourceExp(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			} else {
				sourceNode = this.getSourceExp().toDeMorgan();
			}
			if (canRetainNotOperation(this.getParameterExps().get(0))) {
				parameterNode = new OperationCallExp(this.getConstraint(), this.getParameterExps().get(0), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			} else {
				parameterNode = this.getParameterExps().get(0).toDeMorgan();
			}
			return new OperationCallExp(this.getConstraint(), sourceNode, "or", TypeFactory.getInstance().getClassifier("Boolean"), false, parameterNode);
		} else if (this.getPropertyName().equals("or")|| this.getPropertyName().equals("||")) {
			ASTNode sourceNode;
			ASTNode parameterNode;
			if (canRetainNotOperation(this.getSourceExp())) {
				sourceNode = new OperationCallExp(this.getConstraint(), this.getSourceExp(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			} else {
				sourceNode = this.getSourceExp().toDeMorgan();
			}
			if (canRetainNotOperation(this.getParameterExps().get(0))) {
				parameterNode = new OperationCallExp(this.getConstraint(), this.getParameterExps().get(0), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			} else {
				parameterNode = this.getParameterExps().get(0).toDeMorgan();
			}
			return new OperationCallExp(this.getConstraint(), sourceNode, "and", TypeFactory.getInstance().getClassifier("Boolean"), false, parameterNode);
		} else if (this.getPropertyName().equals("xor")) {
			return new OperationCallExp(this.getConstraint(), this, "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
		} else if (this.getPropertyName().equals("implies")) {
			OperationCallExp not_right_node = new OperationCallExp(this.getConstraint(), this.getParameterExps().get(0), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			return new OperationCallExp(this.getConstraint(), this.getSourceExp(), "and", TypeFactory.getInstance().getClassifier("Boolean"), false, not_right_node);
		} else if (this.getPropertyName().equals("=")) {
			return new OperationCallExp(this.getConstraint(), this.getSourceExp().toDeMorgan(), "<>", TypeFactory.getInstance().getClassifier("Boolean"), false, this.getParameterExps().get(0)
					.toDeMorgan());
		} else if (this.getPropertyName().equals("<>")) {
			return new OperationCallExp(this.getConstraint(), this.getSourceExp().toDeMorgan(), "==", TypeFactory.getInstance().getClassifier("Boolean"), false, this.getParameterExps().get(0)
					.toDeMorgan());
		} else if (this.getPropertyName().equals(">")) {
			return new OperationCallExp(this.getConstraint(), this.getSourceExp().toDeMorgan(), "<=", TypeFactory.getInstance().getClassifier("Boolean"), false, this.getParameterExps().get(0)
					.toDeMorgan());
		} else if (this.getPropertyName().equals("<=")) {
			return new OperationCallExp(this.getConstraint(), this.getSourceExp().toDeMorgan(), ">", TypeFactory.getInstance().getClassifier("Boolean"), false, this.getParameterExps().get(0)
					.toDeMorgan());
		} else if (this.getPropertyName().equals("<")) {
			return new OperationCallExp(this.getConstraint(), this.getSourceExp().toDeMorgan(), ">=", TypeFactory.getInstance().getClassifier("Boolean"), false, this.getParameterExps().get(0)
					.toDeMorgan());
		} else if (this.getPropertyName().equals(">=")) {
			return new OperationCallExp(this.getConstraint(), this.getSourceExp().toDeMorgan(), "<", TypeFactory.getInstance().getClassifier("Boolean"), false, this.getParameterExps().get(0)
					.toDeMorgan());
		} else {
			return new OperationCallExp(this.getConstraint(), this.getSourceExp().toDeMorgan(), this.getPropertyName(), this.getType(), false, this.getParameterExps().get(0).toDeMorgan());
		}
	}

	private boolean canRetainNotOperation(ASTNode node) {
		return (node.getType().equals(TypeFactory.getInstance().getClassifier("Boolean")) && (node instanceof VariableExp || node instanceof LiteralExp || node instanceof TypeLiteralExp
				|| node instanceof AttributeCallExp || node instanceof AssociationEndCallExp || node instanceof IteratorExp || (node instanceof OperationCallExp && ((OperationCallExp) node).isMethod)));
	}

	@Override
	public ASTNode toPreProcessing() {
		this.setSourceExp(this.getSourceExp().toPreProcessing());
		for (int i = 0; i < this.getParameterExps().size(); i++) {
			this.setParameterExpAtPosition(i, this.getParameterExps().get(i).toPreProcessing());
		}
		if (!this.isSeparableLogicalRelationOperation()) {
			if (this.getSourceExp() instanceof IfExp) {
				return transformIfExp((IfExp) this.getSourceExp(), this.getParameterExps().get(0), this.getPropertyName());
			} else if (this.getParameterExps().size() > 0 && this.getParameterExps().get(0) instanceof IfExp) {
				return transformIfExp((IfExp) this.getParameterExps().get(0), this.getSourceExp(), this.getPropertyName());
			} else if (this.getSourceExp() instanceof OperationCallExp
					&& (((OperationCallExp) this.getSourceExp()).getPropertyName().equals("&&") || ((OperationCallExp) this.getSourceExp()).getPropertyName().equals("||"))) {
				return transformAndOrExp((OperationCallExp) this.getSourceExp(), this.getParameterExps().get(0));
			} else if (this.getParameterExps().size() > 0 && this.getParameterExps().get(0) instanceof OperationCallExp
					&& (((OperationCallExp) this.getParameterExps().get(0)).getPropertyName().equals("&&") ||((OperationCallExp) this.getParameterExps().get(0)).getPropertyName().equals("||"))) {
				return transformAndOrExp(((OperationCallExp) this.getParameterExps().get(0)), this.getSourceExp());
			}
		} else if (this.getPropertyName().equals("not")) {
			if(canRetainNotOperation(this.getSourceExp())) {
				return this;
			} else {
				return this.getSourceExp().toDeMorgan();
			}
		}

		return this;
	}

	private ASTNode transformIfExp(IfExp if_node, ASTNode other_node, String operation) {
		OperationCallExp then_exp = new OperationCallExp(this.getConstraint(), other_node, operation, TypeFactory.getInstance().getClassifier("Boolean"), false, if_node.getThenExp());
		ASTNode transform_then = then_exp.toPreProcessing();
		OperationCallExp else_exp = new OperationCallExp(this.getConstraint(), other_node, operation, TypeFactory.getInstance().getClassifier("Boolean"), false, if_node.getElseExp());
		ASTNode transform_else = else_exp.toPreProcessing();
		IfExp if_exp = new IfExp(this.getConstraint(), if_node.getConditionExp(), transform_then, transform_else);
		return if_exp;
	}

	private ASTNode transformAndOrExp(OperationCallExp and_or_node, ASTNode other_node) {
		LiteralExp true_exp = new LiteralExp(this.getConstraint(), TypeFactory.getInstance().getClassifier("Boolean"), "true");
		LiteralExp false_exp = new LiteralExp(this.getConstraint(), TypeFactory.getInstance().getClassifier("Boolean"), "false");
		OperationCallExp then_exp = new OperationCallExp(this.getConstraint(), other_node, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, true_exp);
		OperationCallExp else_exp = new OperationCallExp(this.getConstraint(), other_node, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, false_exp);
		
		IfExp if_exp = new IfExp(this.getConstraint(), and_or_node, then_exp, else_exp);
		return if_exp;
	}

	public boolean isRelationOperation() {
		switch (this.getPropertyName()) {
		case "=":
			return true;
		case ">":
			return true;
		case "<":
			return true;
		case ">=":
			return true;
		case "<=":
			return true;
		case "<>":
			return true;
		default:
			return false;
		}
	}

	public boolean isSeparableLogicalRelationOperation() {
		switch (this.getPropertyName()) {
		case "and":
		case "&&":
		case "or":
		case "||":
		case "not":
		case "xor":
		case "implies":
			return true;
		default:
			return false;
		}
	}

	@Override
	public CLGGraph OCL2CLG() {
	
		//System.out.println("How Have You Been?"+this.CLGConstraint().getConstratinImg());
		
		
		
		if (this.isMethod()) {
			
		
			return new CLGGraph(this.CLGConstraint());
		} else if (this.isRelationOperation() && this.getParameterExps().get(0) instanceof IterateExp) {  //Iterate
	
			VariableExp result = new VariableExp(this.getConstraint(), String.format("#ResultAcc%d", ((IterateExp) this.getParameterExps().get(0)).getIterate_id()), ((ASTNode) this.getNextNodes()
					.get(1)).getType(), this.getConstraint().getKind().getName());
			OperationCallExp var_equal_result_node = new OperationCallExp(this.getConstraint(), this.getSourceExp(), this.getPropertyName(), TypeFactory.getInstance().getClassifier("Boolean"), false,
					result);
		
			
			CLGConstraint VariableEqualResultNode_left = new CLGVariableNode(this.getSourceExp().toOCL());
			CLGConstraint VariableEqualResultNode_Right = new CLGVariableNode(result.toOCL());
			
			CLGOperatorNode VariableEqualResultNode = new CLGOperatorNode();
			
			VariableEqualResultNode.setOperator("=");
			VariableEqualResultNode.setLeftOperand(VariableEqualResultNode_left);
			VariableEqualResultNode.setRightOperand(VariableEqualResultNode_Right);
			
			CLGGraph clgVariableEqualResultNode = new CLGGraph(VariableEqualResultNode);
			CLGGraph clgIterateNode = this.getParameterExps().get(0).OCL2CLG(); 
			clgIterateNode.graphAnd(clgVariableEqualResultNode);
			
			return clgIterateNode;
		} else if (this.getPropertyName().equals("not")) {  //inverse variable
	
			
			
			final ASTNode root_node = this.getSourceExp().clone();
			if (canRetainNotOperation(root_node)) {
		
				//System.out.println("deot");
				
				return new CLGGraph(this.CLGConstraint());
			} else if (root_node instanceof OperationCallExp && ((OperationCallExp) root_node).getPropertyName().equals("xor")) {
			
			
				OperationCallExp _exp = (OperationCallExp) root_node;
				OperationCallExp not_left_node = new OperationCallExp(_exp.getConstraint(), _exp.getSourceExp(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
				OperationCallExp not_right_node = new OperationCallExp(_exp.getConstraint(), _exp.getParameterExps().get(0), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
				CLGGraph clg_left_node = _exp.getSourceExp().OCL2CLG();
				CLGGraph clg_right_node = _exp.getParameterExps().get(0).OCL2CLG();
				CLGGraph clg_not_left_node = not_left_node.OCL2CLG();
				CLGGraph clg_not_right_node = not_right_node.OCL2CLG();
						
				clg_left_node.graphAnd(clg_right_node);
				clg_not_left_node.graphAnd(clg_not_right_node); 
				
				clg_left_node.graphOr(clg_not_left_node);
				 
				return clg_left_node;
			} else {
				
				ASTNode inverse_node = root_node.toDeMorgan();
				//System.out.println("dealwithnot"+inverse_node.toOCL());
				
				return inverse_node.OCL2CLG();
			}
		}
		else if (this.getPropertyName().equals("and") || this.getPropertyName().equals("&&") ) {
		  
			
			
			if(this.toOCL().contains("if")&& !this.toOCL().contains("not"))
			{
			;	
			CLGGraph clgNodeLeft =this.getSourceExp().OCL2CLG();
			
		
			
			CLGGraph clgNodeRight = this.parameters.get(0).OCL2CLG();
			
			
			
			clgNodeLeft.graphAnd(clgNodeRight);
			return clgNodeLeft;
			}
			
			return new CLGGraph(this.CLGConstraint());		
		} 
	
		
		
		
		else if (this.getPropertyName().equals("or")||this.getPropertyName().equals("||")) {
			
			//System.out.println("8."+this.toOCL());
			
			if(this.toOCL().contains("if") && !this.toOCL().contains("not"))
			{ 
				
			CLGGraph clgNodeLeft =this.getSourceExp().OCL2CLG();
			 
			
			CLGGraph clgNodeRight = this.parameters.get(0).OCL2CLG();
		
			
			clgNodeLeft.graphOr(clgNodeRight);
			return clgNodeLeft;
			}
			
			
			return new CLGGraph(this.CLGConstraint());		
			/*CLGGraph clgLeft = this.getSourceExp().OCL2CLG(criterion);
		
			CLGGraph clgRight = this.parameters.get(0).OCL2CLG(criterion);
	
			ASTNode astNotL = new OperationCallExp(this.getConstraint(), this.getSourceExp().clone(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			
			ASTNode astNotR = new OperationCallExp(this.getConstraint(), this.getParameterExps().get(0).clone(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			
			CLGGraph clgNotLeft = astNotL.OCL2CLG(criterion); 
		
			
			CLGGraph clgNotRight = astNotR.OCL2CLG(criterion);
			
			
			clgLeft.graphAnd(clgNotRight);
			clgNotLeft.graphAnd(clgRight);
			
			clgLeft.graphOr(clgNotLeft);
			
			return clgLeft;*/
		} else if (this.getPropertyName().equals("xor")) {
			
			//System.out.println("HereAfterUs!!");
	
			 
			OperationCallExp not_left_node = new OperationCallExp(this.getConstraint(), this.getSourceExp(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			ASTNode notA=this.getSourceExp().toDeMorgan();
			//System.out.println("notA->"+notA.toString());
			OperationCallExp not_right_node = new OperationCallExp(this.getConstraint(), this.getParameterExps().get(0), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			ASTNode notB=this.getParameterExps().get(0).toDeMorgan();
			
			OperationCallExp left_and_not_right_node = new OperationCallExp(this.getConstraint(),  this.getSourceExp(), "and", TypeFactory.getInstance().getClassifier("Boolean"), false,
					 notB); 

			OperationCallExp not_left_and_right_node = new OperationCallExp(this.getConstraint(), notA, "and", TypeFactory.getInstance().getClassifier("Boolean"), false,
					this.getParameterExps().get(0)); 

			
			OperationCallExp left_xor_right = new OperationCallExp(this.getConstraint(), left_and_not_right_node, "or", TypeFactory.getInstance().getClassifier("Boolean"), false,
					not_left_and_right_node); 
			
			//System.out.println("AAAAAAAAAAAA!!!!"+left_xor_right.toString());
			
			return left_xor_right.OCL2CLG();
			
		
			
		} else if (this.getPropertyName().equals("implies")) {   //A->B
		
			OperationCallExp not_left_node = new OperationCallExp(this.getConstraint(), this.getSourceExp(), "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
			ASTNode a =this.getSourceExp().toDeMorgan();
			OperationCallExp not_left_or_right_node = new OperationCallExp(this.getConstraint(), a, "or", TypeFactory.getInstance().getClassifier("Boolean"), false,
					this.getParameterExps()); 
			//System.out.println("WWWWWWWWWWWWWWWWWWWWw!!!!"+not_left_or_right_node.toString()+",PPP"+not_left_or_right_node.getPropertyName());
			
			return not_left_or_right_node.OCL2CLG();
		}
			
		//System.out.println("LLLLL"+this.toOCL());
		
		return new CLGGraph(this.CLGConstraint());
	}

	public CLGConstraint parseConstraint(OperationCallExp opexp){
		//System.out.println("doparse:"+opexp.toOCL());
		CLGConstraint Left= null;
		CLGConstraint Right=null;	
	
		String LConstraint=null,op=null,RConstraint=null;
		CLGOperatorNode opconstraint= null;
				//

//		if(opexp.toOCL().contains("not"))
//		{
//			opconstraint=new CLGOperatorNode(" "+" not "+" ");
//			System.out.println("QQnot=>"+opexp.toOCL());
//			if(opexp.getSourceExp() instanceof OperationCallExp)
//			{
//				System.out.println("!not"+opexp.getSourceExp().toString());
//				Left = new CLGVariableNode(" ");
//				System.out.println("!notLEFT"+Left.getImgInfo());
//                Right = parseConstraint((OperationCallExp)opexp.getSourceExp());
//				
//                opconstraint.setLeftOperand(Left);
//				opconstraint.setRightOperand(Right);
//		
//			}
//			else if(opexp.getSourceExp() instanceof PropertyCallExp){
//				Left = new CLGVariableNode(" ");
//				Right = new CLGVariableNode(opexp.getSourceExp().toString());
//				
//			
//		
//				opconstraint.setRightOperand(Left);
//				opconstraint.setRightOperand(Right);
//				
//			}
//			
//			
//			
//		}
		
	   if(opexp.getSourceExp() instanceof OperationCallExp)
		{
			 //System.out.println("V"+opexp.toOCL() );
			Left = parseConstraint((OperationCallExp)opexp.getSourceExp());
			op =  opexp.getPropertyName();
			//System.out.println("operator----------1-----------"+op);
			opconstraint=new CLGOperatorNode(op);
			
			
			if(opexp.getParameterExps().get(0) instanceof OperationCallExp)
			{
				// System.out.println("V1");
				Right = parseConstraint((OperationCallExp)opexp.getParameterExps().get(0));
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);
				
			}
			else if(opexp.getParameterExps().get(0) instanceof PropertyCallExp)
			{
				 
				RConstraint = opexp.getParameterExps().get(0).toString();
				//System.out.println("V2"+RConstraint);
				Right = new CLGVariableNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
			
			}
			else if(opexp.getParameterExps().get(0).toString().matches("[0-9]+"))
			{
				 //System.out.println("V3Literal");
				RConstraint = opexp.getParameterExps().get(0).toString();
				Right = new CLGLiteralNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);
			
			}	
			else 
			{
				RConstraint = opexp.getParameterExps().get(0).toString();
				Right = new CLGVariableNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
				LConstraint=op=RConstraint="";
			}
		}	
	else if(opexp.getSourceExp() instanceof PropertyCallExp || opexp.getSourceExp().toString().contains("@pre")) 	
		{
		// System.out.println("V4"+opexp.getSourceExp());
            LConstraint=opexp.getSourceExp().toString();
			Left = new CLGVariableNode(LConstraint);
			op =  opexp.getPropertyName();
			//System.out.println("operator----------2-----------"+op);
			opconstraint=new CLGOperatorNode(op);
			if(opexp.getParameterExps().get(0) instanceof OperationCallExp)
			{
				// System.out.println("V111"+opexp.getParameterExps().get(0));
				Right = parseConstraint((OperationCallExp)opexp.getParameterExps().get(0));
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);
				LConstraint=op=RConstraint="";
			}
			else if(opexp.getParameterExps().get(0) instanceof PropertyCallExp)
		    {
				//System.out.println("V5"+opexp.getParameterExps().get(0));
			    RConstraint = opexp.getParameterExps().get(0).toString();
				Right = new CLGVariableNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
				LConstraint=op=RConstraint="";
			}
			else if(opexp.getParameterExps().get(0).toString().matches("[0-9]+"))
			{
				 //System.out.println("V6Literal");
				RConstraint = opexp.getParameterExps().get(0).toString();
				Right = new CLGLiteralNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);
				LConstraint=op=RConstraint="";
			}
			else 
			{
				//System.out.println("V67"+opexp.getParameterExps().get(0));
				RConstraint = opexp.getParameterExps().get(0).toString();
				Right = new CLGVariableNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
				LConstraint=op=RConstraint="";
			}
			
		}
		
	else if(this.getSourceExp().toString().matches("[0-9]+"))
	  {
		 //System.out.println("V7Literal");
		      LConstraint = opexp.getSourceExp().toString();
		     Left = new CLGLiteralNode(LConstraint);
		     op =  opexp.getPropertyName().toString();
		     //System.out.println("operator---------3------------"+op);
			 opconstraint=new CLGOperatorNode(op);	
			if(opexp.getParameterExps().get(0) instanceof PropertyCallExp)
			    {
				//System.out.println("V8");
					RConstraint =opexp.getParameterExps().get(0).toString();
					Right = new CLGVariableNode(RConstraint);
					opconstraint.setLeftOperand(Left);
					opconstraint.setRightOperand(Right);	
					LConstraint=op=RConstraint="";
				}
			else if(this.getParameterExps().get(0).toString().matches("[0-9]+"))
			    {
				//System.out.println("V9Literal");
				RConstraint= opexp.getParameterExps().get(0).toString();
				Right = new CLGLiteralNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
				LConstraint=op=RConstraint="";
			    }				
	  }	
	else {
		//System.out.println("else!!!"+opexp.getParameterExps().get(0));
		 LConstraint = opexp.getSourceExp().toString();
		 Left = new CLGVariableNode(LConstraint);
	     op =  opexp.getPropertyName().toString();
	   //  System.out.println("operator-----------4----------"+op);
		 opconstraint=new CLGOperatorNode(op);	
			
		 if(opexp.getParameterExps().get(0)  instanceof OperationCallExp)
			{
			  //  System.out.println("elseV1");
				Right = parseConstraint((OperationCallExp)opexp.getParameterExps().get(0));
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);
				LConstraint=op=RConstraint="";
			}
		 
		 else if(opexp.getParameterExps().get(0) instanceof PropertyCallExp)
		    {
			   // System.out.println("elseV8");
				RConstraint =opexp.getParameterExps().get(0).toString();
				Right = new CLGVariableNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
				LConstraint=op=RConstraint="";
			}
	 if(opexp.getParameterExps().get(0).toOCL().matches("[0-9]") ||opexp.getParameterExps().get(0).toOCL().matches("[0-9]+") )
		    { 
			 //System.out.println("elseLiteral");
			RConstraint= opexp.getParameterExps().get(0).toString();
			Right = new CLGLiteralNode(RConstraint);
			opconstraint.setLeftOperand(Left);
			opconstraint.setRightOperand(Right);	
			LConstraint=op=RConstraint="";
		    }
		else{
			//System.out.println("elsea");
			RConstraint = opexp.getParameterExps().get(0).toString();
			Right = new CLGVariableNode(RConstraint);
			opconstraint.setLeftOperand(Left);
			opconstraint.setRightOperand(Right);	
			LConstraint=op=RConstraint="";
		}
		
	}	
//		System.out.println("====M_finish====>" + opconstraint.getOperator().toString());
//		System.out.println("====R_finish====>" + opconstraint.getRightOperand().getImgInfo());
//		System.out.println("====L_finish====>" + opconstraint.getLeftOperand().getImgInfo());
		return opconstraint;
	}
	
	
	
	
	@Override
	public CLGConstraint CLGConstraint() {
		
		CLGOperatorNode OperatiorConstraint = new CLGOperatorNode() ;
		String result = "";
		if (this.isMethod()) {
			
			
			CLGConstraint Left=new CLGVariableNode(this.getSourceExp().toString()+".");
			CLGConstraint Right=new CLGVariableNode("("+this.getParameterExps().toString().substring(1,this.getParameterExps().toString().length()-1 )+")");
			OperatiorConstraint.setOperator(getPropertyName().toString());
			OperatiorConstraint.setLeftOperand(Left);
			OperatiorConstraint.setRightOperand(Right);

		} else {
			if (this.getPropertyName().equals("not")) {
				
				 //System.out.println("sword! ");
				
				
				CLGConstraint Left=new CLGVariableNode("");
				
				CLGConstraint Right=new CLGVariableNode(this.getSourceExp().toString());
	           
				OperatiorConstraint.setOperator(getPropertyName().toString()+" ");
				OperatiorConstraint.setLeftOperand(Left);
				OperatiorConstraint.setRightOperand(Right); 
					
			}
			else {

				CLGConstraint Left= null;
				CLGConstraint Right=null;	
				String op,L,R = null;
				CLGOperatorNode opconstraint= null;
          /*************************LEFT***********************************************************/
				if(this.getSourceExp() instanceof OperationCallExp && !this.getSourceExp().toString().contains("()"))
				{
				 // System.out.println("LO"+getPropertyName().toString());
					Left = this.parseConstraint((OperationCallExp)this.getSourceExp());
					
					op =  getPropertyName().toString();
					OperatiorConstraint=new CLGOperatorNode(op);
				}
				else if(this.getSourceExp() instanceof PropertyCallExp)
				{
				//System.out.println("Lp");
						L = this.getSourceExp().toString();
						Left = new CLGVariableNode(L);
						op =  getPropertyName().toString();
						OperatiorConstraint=new CLGOperatorNode(op);
				}
				else if(this.getSourceExp().toString().equals("result"))
				{				
						L = this.getSourceExp().toString();
						Left = new CLGVariableNode(L);
						op =  getPropertyName().toString();
						OperatiorConstraint=new CLGOperatorNode(op);
				}
				else if(this.getSourceExp().toString().matches("[0-9]+"))
				{
					// System.out.println("LLLiteral");
					    L = this.getSourceExp().toString();
						Left = new CLGLiteralNode(L);
						op =  getPropertyName().toString();
						OperatiorConstraint=new CLGOperatorNode(op);					
				}
				else{
				
					L = this.getSourceExp().toString();
					//System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ"+L);
					Left = new CLGVariableNode(L);
					op =  getPropertyName().toString();
					OperatiorConstraint=new CLGOperatorNode(op);
				}
				
		/***************************RIGHT*********************************************************/

		        if(this.getParameterExps().get(0) instanceof OperationCallExp && !this.getParameterExps().get(0).toString().contains("()")) 	
				{
		        	//System.out.println("RO");
					Right = this.parseConstraint((OperationCallExp)this.getParameterExps().get(0));
					op =  getPropertyName().toString();
					OperatiorConstraint=new CLGOperatorNode(op);		
				}	
		        else if(this.getParameterExps().get(0) instanceof PropertyCallExp)
				{
		        	  // System.out.println("RP");
						
					
						Right = new CLGVariableNode(this.getParameterExps().get(0).toString());
						op =  this.getPropertyName().toString();
						OperatiorConstraint=new CLGOperatorNode(op);					
			    }
		    	else if(this.getParameterExps().get(0) instanceof IfExp)
				{
		    		
		    		Right = new CLGVariableNode(getParameterExps().toString().substring(1,this.getParameterExps().toString().length()-1));
		    		op =  this.getPropertyName().toString();
		    		OperatiorConstraint=new CLGOperatorNode(op);	
									
			    }
				else if(this.getParameterExps().get(0).toString().matches("[0-9]+"))
				{
					// System.out.println("RLiteral"+this.getParameterExps().get(0).toString());
						R = this.getParameterExps().get(0).toString();
						Right = new CLGLiteralNode(R);
						op =  this.getPropertyName().toString();
						OperatiorConstraint=new CLGOperatorNode(op);					
			    }
				else if(this.getParameterExps().get(0).toString().equals("true")||this.getParameterExps().get(0).toString().equals("false"))
				{ 
					// System.out.println("XXXXXXXXXXXXXXXXXX"+this.getParameterExps().get(0).toString());
						R = this.getParameterExps().get(0).toString();
						Right = new CLGVariableNode(R);
						op =  this.getPropertyName().toString();
						OperatiorConstraint=new CLGOperatorNode(op);					
			    }
				else{
					//System.out.println("Relse");
				
					Right = new CLGLiteralNode(this.getParameterExps().get(0).toString());
					op =  this.getPropertyName().toString();
					OperatiorConstraint=new CLGOperatorNode(op);
					//System.out.println("RightJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ"+this.getParameterExps().get(0).toString());
				}
		        
		        
		        
		        
		        
		        
		        OperatiorConstraint.setLeftOperand(Left);
		        OperatiorConstraint.setRightOperand(Right);
		        
				/*****/
 
		        
		        
		        
		        
					
				
//		        System.out.println("====finalM_finish====>" + OperatiorConstraint.getOperator().toString());
//		        System.out.println("====finalR_finish====>" + OperatiorConstraint.getRightOperand().getImgInfo());
//				System.out.println("====finalL_finish====>" + OperatiorConstraint.getLeftOperand().getImgInfo());	
			
			
				
				
				
				
				
				
				
				
				
				
				
				
//				CLGConstraint Left=this.getSourceExp().CLGConstraint();
//				
//				CLGConstraint Right=this.getParameterExps().get(0).CLGConstraint();
//				
//				OperatiorConstraint.setOperator(getPropertyName().toString());
//				
//				OperatiorConstraint.setLeftOperand(Left);
//				OperatiorConstraint.setRightOperand(Right);
				
				
			}
			
				}
			
		
	
	
		return OperatiorConstraint;
	}



	
}
