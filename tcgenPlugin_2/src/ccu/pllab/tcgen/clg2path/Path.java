package ccu.pllab.tcgen.clg2path;

 
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import tudresden.ocl20.pivot.metamodels.uml2.internal.model.UML2Operation;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import ccu.pllab.tcgen.ast.ASTNode;
import ccu.pllab.tcgen.ast.ASTUtil;
import ccu.pllab.tcgen.ast.IteratorExp;
import ccu.pllab.tcgen.ast.OperationCallExp;
import ccu.pllab.tcgen.ast.PropertyCallExp;
import ccu.pllab.tcgen.ast.VariableExp;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg.EndNode;
import ccu.pllab.tcgen.graphviz.GraphVizable;
import ccu.pllab.tcgen.libs.Predicate;
import ccu.pllab.tcgen.libs.node.GraphVisitor;
import ccu.pllab.tcgen.libs.node.NodeVisitHandler;
import ccu.pllab.tcgen.libs.node.StackFrontier;
import ccu.pllab.tcgen.libs.pivotmodel.Attribute;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.type.PrimitiveType;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;

public class Path implements GraphVizable, Predicate {
	private static int path_count = 0;
	final private List<CLGNode> nodes;
	final private int id;
	final private String edgeColor;
	final private List<ASTNode> actual_asts;
	final private Model model;
	final private Constraint dresden_constraint;

	private Path(List<CLGNode> nodes, Model model, boolean processing) {
		this.nodes = new ArrayList<CLGNode>(nodes);
		this.id = path_count++;
		this.edgeColor = genRandColor();
		this.model = model;
		this.dresden_constraint = nodes.get(0).getConstraint();
		this.actual_asts = new ArrayList<ASTNode>();
		if (processing) {
			this.analysisASTNodes();
		} else {
			for (CLGNode node : this.nodes) {
				if (node instanceof ConstraintNode) {
					this.actual_asts.add(((ConstraintNode) node).getASTNode());
				}
			}
		}
	}

	public Path(List<CLGNode> nodes, Model model) {
		this(nodes, model, true);
	}

	public int getId() {
		return this.id;
	}

	public List<CLGNode> getCLGNodes() {
		return this.nodes;
	}

	public Constraint getConstraint() {
		return this.nodes.get(0).getConstraint();
	}

	@Override
	public String toGraphViz() {
		String result = "";
		for (int i = 0; i < this.nodes.size(); i++) {
			if (this.nodes.get(i) instanceof ConstraintNode) {
				result += this.nodes.get(i).getId() + String.format(" [shape=box, label=\"%s\"]", ((ConstraintNode) this.nodes.get(i)).getASTNode().toOCL()) + "\n";
			} else {
				result += this.nodes.get(i).getId();
				if (i == 0) {
					result += "[style=filled, fillcolor=black, shape=\"circle\", label=\"\", fixedsize=true, width=.2, height=.2]\n";
					result += "n_" + this.nodes.get(i).getId() + " [label=\"" + this.nodes.get(i).getConstraintedClass() + "::" + this.nodes.get(i).getConstraintedMethod() + " " + this.id + "\"]\n";
					result += "n_" + this.nodes.get(i).getId() + " -> " + this.nodes.get(i).getId() + "[color=\"" + this.getEdgeColor() + "\"]";
				} else if (this.nodes.get(i) instanceof EndNode) {
					result += String.format(" [style=filled, fillcolor=black, label=\"\", shape=\"%s\", fixedsize=true, width=.2, height=.2]", this.nodes.get(i).getShape());
				} else {
					result += String.format(" [label=\"\", shape=\"%s\", fixedsize=true, width=.2, height=.2]", this.nodes.get(i).getShape());
				}
				result += "\n";
			}
			if (i != this.nodes.size() - 1) {
				result += this.nodes.get(i).getId() + " -> " + this.nodes.get(i + 1).getId() + " [color=\"" + this.getEdgeColor() + "\", label=\"" + i + "\"]\n";
			}
		}
		return result;
	}

	public String getPredicateName() {
		return String.format("tcgen_%d_%s", this.id, this.nodes.get(0).getConstraintedClass() + this.nodes.get(0).getConstraintedMethod());
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
		return String.format("%s(%s, %s, %s)", this.getPredicateName(), instances_name, vars_name, result_name);
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		Operation op = (Operation) this.getCLGNodes().get(0).getConstraint().getConstrainedElement().get(0);

		String instances_name = "InstancesPre";
		if (templateArgs.get("instances_name") != null) {
			instances_name = templateArgs.get("instances_name");
		}
		String vars_name = "OutputVars";
		if (templateArgs.get("vars_name") != null) {
			vars_name = templateArgs.get("vars_name");
		}
		String result_name = "InstancesPost";
		if (templateArgs.get("result_name") != null) {
			result_name = templateArgs.get("result_name");
		}

		writer.println(String.format("tcgen_%d_%s(%s, %s, %s)", this.id, this.nodes.get(0).getConstraintedClass() + this.nodes.get(0).getConstraintedMethod(), instances_name, vars_name, result_name)
				+ " :-");

		List<String> class_names = generate_var_class_name(op);
		List<String> o_prefix_class_names = generate_var_object_names(class_names);
		write_paramenter_initialization(writer, class_names);
		if (ASTUtil.hasReturnValue(this.getConstraint())) {
			writer.println(String.format("\tVars = [%s, Result],", StringUtils.join(o_prefix_class_names, ", ")));
			writer.println(String.format("\tOutputVars = [Result, %s],", StringUtils.join(o_prefix_class_names, ", ")));
		} else {
			writer.println(String.format("\tVars = [%s],", StringUtils.join(o_prefix_class_names, ", ")));
			writer.println(String.format("\tOutputVars = [[_, _], %s],", StringUtils.join(o_prefix_class_names, ", ")));
		}

		writer.println("\tInstances = [InstancesPre, InstancesPost],");
		writer.println(String.format("\tcreateInstances%d(InstancesPre),", this.getId()));
		List<String> created_vars_list = write_each_predicate(writer);
		writer.println(String.format("\tcreateInstances%d(InstancesPost),", this.getId()));
		writer.println(String.format("\tallIntegerIndomain([Instances, Vars, [%s]]),", StringUtils.join(created_vars_list, ", ")));
		writer.println(String.format("\teclipse_language:'delayed_goals'(Goals), (Goals = [] -> true; write(\"%s\"), writeln(Goals)).", this.getPredicateName()));
		writer.flush();
		StringWriter string_writer = new StringWriter();
		writer.println(string_writer.toString());

		return sw.toString();
	}

	public List<ASTNode> getASTNodes() {
		return new ArrayList<ASTNode>(this.actual_asts);
	}

	private static String genRandColor() {
		String result = "#";
		Random r = new Random();
		for (int i = 0; i < 6; i++) {
			result += Integer.toHexString(r.nextInt(16));
		}
		return result;
	}

	private String getEdgeColor() {
		return this.edgeColor;
	}

	private void write_clp_of_ast_node(final PrintWriter writer, final ASTNode current_node, final String varsName) {
		HashMap<String, String> tpl_args = new HashMap<String, String>();
		tpl_args.put("result_name", "1");
		tpl_args.put("vars_name", varsName);
		writer.println(String.format("\t%s,", current_node.getPredicateName(tpl_args)) + "%\t" + current_node.toOCL());
	}

	private List<String> write_each_predicate(PrintWriter writer) {
		List<String> created_vars = new ArrayList<String>();
		Constraint current_constraint = null;
		int constraint_count = -1;
		for (ASTNode current_node : this.actual_asts) {
			if (current_constraint != current_node.getConstraint()) {
				current_constraint = current_node.getConstraint();
				constraint_count++;
				writer.println(String.format("\tappend(Vars, [_|_],  Vars%d),", constraint_count));
				created_vars.add("Vars" + constraint_count);
			}
			write_clp_of_ast_node(writer, current_node, String.format("Vars%d", constraint_count));
		}
		return created_vars;
	}

	private HashMap<String, Integer> prepareNewSymbolTable() {
		HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();
		UML2Operation op = (UML2Operation) this.getConstraint().getConstrainedElement().get(0);
		if (!op.isStatic()) {
			symbolTable.put("self", 1);
		} else if (op.isStatic() && op.getName().equals(op.getOwningType().getName())) {
			symbolTable.put("self", 1);
		}
		for (Parameter p : op.getSignatureParameter()) {
			symbolTable.put(p.getName(), symbolTable.size() + 1);
		}
		if (ASTUtil.hasReturnValue(this.getConstraint())) {
			symbolTable.put("result", symbolTable.size() + 1);
		}

		return symbolTable;
	}

	private void write_paramenter_initialization(PrintWriter writer, List<String> class_names) {
		int parameter_index = 0;
		for (String class_name : class_names) {
			writer.println(String.format("\tparameterOf%s%d(Instances, O%s%d),", class_name, this.getId(), class_name, parameter_index));
			parameter_index++;
		}
		if (!nodes.get(0).getConstraintedMethodReturnType().equals("OclVoid")) {
			writer.println(String.format("\tparameterOf%s%d(Instances, Result),", nodes.get(0).getConstraintedMethodReturnType(), this.getId()));
		}
	}

	private List<String> generate_var_class_name(Operation op) {
		List<String> class_names = new ArrayList<String>();
		if (!ASTUtil.isStatic(this.getCLGNodes().get(0).getConstraint()) || ASTUtil.isConstructor(this.getCLGNodes().get(0).getConstraint())) {
			class_names.add(op.getOwningType().getName());
		}

		for (Parameter p : op.getInputParameter()) {
			class_names.add(p.getType().getName());
		}
		return class_names;
	}

	private List<String> generate_var_object_names(List<String> class_names) {
		List<String> o_prefix_class_names = new ArrayList<String>();
		{
			int parameter_index = 0;
			for (String class_name : class_names) {
				o_prefix_class_names.add("O" + class_name + parameter_index);
				parameter_index++;
			}
		}
		return o_prefix_class_names;
	}

	private void analysisASTNodes() {
		final HashMap<String, Integer> symbolTable = prepareNewSymbolTable();
		final NodeVisitHandler<ASTNode> assign_index_handler = new NodeVisitHandler<ASTNode>() {
			@Override
			public void visit(ASTNode current_node) {
				if (!(current_node instanceof VariableExp)) {
					return;
				}
				VariableExp variableExp = (VariableExp) current_node;
				if (symbolTable.get(variableExp.getVariableName()) == null) {
					symbolTable.put(variableExp.getVariableName(), symbolTable.size() + 1);
				}
				variableExp.setCLPIndex(symbolTable.get(variableExp.getVariableName()).toString());
			}
		};
		final GraphVisitor<ASTNode> assign_index_dfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.POSTORDER, new StackFrontier<ASTNode>());

		final HashMap<Integer, Integer> iterate_count_table = new HashMap<Integer, Integer>();
		ListIterator<CLGNode> it = this.nodes.listIterator();
		while (it.hasNext()) {
			final CLGNode current_node = it.next();
			if (!(current_node instanceof ConstraintNode)) {
				continue;
			}
			final ConstraintNode constraintNode = (ConstraintNode) current_node.clone();
			actual_asts.add(constraintNode.getASTNode());
			ssaProcessingForIterateVariable(iterate_count_table, constraintNode);
			assign_index_dfs.traverse(constraintNode.getASTNode(), assign_index_handler);
		}
		List<OperationCallExp> equals_exps = getEqualExpsForAttributeConsistency();
		for (OperationCallExp equal_exp : equals_exps) {
			actual_asts.add(equal_exp);
		}
	}

	private void ssaProcessingForIterateVariable(final HashMap<Integer, Integer> iterate_count_table, final ConstraintNode constraintNode) {
		if (constraintNode.getASTNode().toOCL().matches("\\(#IterateAcc\\d+ = .+\\)")) {
			final OperationCallExp opcall = (OperationCallExp) constraintNode.getASTNode();
			final VariableExp lhs_of_op = (VariableExp) opcall.getSourceExp();
			final ASTNode rhs_of_op = opcall.getParameterExps().get(0);
			final Integer iterate_id = Integer.parseInt(lhs_of_op.getVariableName().replace("#IterateAcc", ""));
			if (iterate_count_table.get(iterate_id) != null) {
				final Integer old_count = iterate_count_table.get(iterate_id);
				final Integer new_count = iterate_count_table.get(iterate_id) + 1;
				iterate_count_table.put(iterate_id, new_count);

				lhs_of_op.setVariableName(lhs_of_op.getVariableName() + "[" + new_count + "]");

				GraphVisitor<ASTNode> ssa_dfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.POSTORDER, new StackFrontier<ASTNode>());
				ssa_dfs.traverse(rhs_of_op, new NodeVisitHandler<ASTNode>() {
					@Override
					public void visit(ASTNode current_node) {
						if (!(current_node instanceof VariableExp)) {
							return;
						}
						VariableExp variableExp = (VariableExp) current_node;
						if (variableExp.getVariableName().matches("#Iterate\\w+" + iterate_id)) {
							variableExp.setVariableName(variableExp.getVariableName() + "[" + old_count + "]");
						}
					}
				});
			} else {
				iterate_count_table.put(iterate_id, 1);
				lhs_of_op.setVariableName(lhs_of_op.getVariableName() + "[" + 1 + "]");
			}
		} else if (constraintNode.getASTNode().toOCL().matches("\\(#IterateElement\\d+ = .+->at\\(#IterateIndex\\d+\\)\\)")) {
			final OperationCallExp opcall = (OperationCallExp) constraintNode.getASTNode();
			final VariableExp lhs_of_op = (VariableExp) opcall.getSourceExp();
			Integer iterate_id = Integer.parseInt(lhs_of_op.getVariableName().replace("#IterateElement", ""));
			lhs_of_op.setVariableName(lhs_of_op.getVariableName() + "[" + iterate_count_table.get(iterate_id) + "]");
			final VariableExp iterateIndex = (VariableExp) ((IteratorExp) opcall.getParameterExps().get(0)).getParameterExps().get(0);
			iterateIndex.setVariableName(iterateIndex.getVariableName() + "[" + iterate_count_table.get(iterate_id) + "]");
		} else if (constraintNode.getASTNode().toOCL().matches("\\(#IterateIndex\\d+ (\\<\\=|\\>) .+->size\\(\\)\\)")) {
			final OperationCallExp opcall = (OperationCallExp) constraintNode.getASTNode();
			final VariableExp lhs_of_op = (VariableExp) opcall.getSourceExp();
			Integer iterate_id = Integer.parseInt(lhs_of_op.getVariableName().replace("#IterateIndex", ""));
			lhs_of_op.setVariableName(lhs_of_op.getVariableName() + "[" + iterate_count_table.get(iterate_id) + "]");
		} else if (constraintNode.getASTNode().toOCL().matches("\\(#ResultAcc\\d+ = #IterateAcc\\d+\\)")) {
			final OperationCallExp opcall = (OperationCallExp) constraintNode.getASTNode();
			final VariableExp rhs_of_op = (VariableExp) opcall.getParameterExps().get(0);
			Integer iterate_id = Integer.parseInt(rhs_of_op.getVariableName().replace("#IterateAcc", ""));
			rhs_of_op.setVariableName(rhs_of_op.getVariableName() + "[" + iterate_count_table.get(iterate_id) + "]");
		} else if (constraintNode.getASTNode().toOCL().matches("\\(#IterateIndex\\d+ = \\(#IterateIndex\\d+ \\+ 1\\)\\)")) {
			final OperationCallExp opcall = (OperationCallExp) constraintNode.getASTNode();
			final VariableExp lhs_of_op = (VariableExp) opcall.getSourceExp();
			Integer iterate_id = Integer.parseInt(lhs_of_op.getVariableName().replace("#IterateIndex", ""));
			lhs_of_op.setVariableName(lhs_of_op.getVariableName() + "[" + iterate_count_table.get(iterate_id) + "]");
			final VariableExp lhs_of_plus = (VariableExp) ((OperationCallExp) opcall.getParameterExps().get(0)).getSourceExp();
			lhs_of_plus.setVariableName(lhs_of_plus.getVariableName() + "[" + (iterate_count_table.get(iterate_id) - 1) + "]");
		} else {
			GraphVisitor<ASTNode> ssa_dfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.POSTORDER, new StackFrontier<ASTNode>());
			ssa_dfs.traverse(constraintNode.getASTNode(), new NodeVisitHandler<ASTNode>() {
				@Override
				public void visit(ASTNode current_node) {
					if (!(current_node instanceof VariableExp)) {
						return;
					}
					VariableExp variableExp = (VariableExp) current_node;
					if (!variableExp.getVariableName().startsWith("#Iterate") && !variableExp.getVariableName().startsWith("#Result")) {
						return;
					}
					final Integer iterate_id = Integer.parseInt(variableExp.getVariableName().replaceAll("#\\D+", ""));
					if (variableExp.getVariableName().matches("#Iterate\\w+" + iterate_id)) {
						variableExp.setVariableName(variableExp.getVariableName() + "[" + iterate_count_table.get(iterate_id) + "]");
					}
				}
			});
		}
	}

	private List<OperationCallExp> getEqualExpsForAttributeConsistency() {
		final List<Parameter> parameters = ASTUtil.createNewParameterListForCLP(dresden_constraint);
		final HashMap<String, Set<PropertyCallExp>> changedPropertyCallExprs = ASTUtil.detectChangedProperties(model, dresden_constraint, this.getASTNodes());
		List<OperationCallExp> equal_exps = new ArrayList<OperationCallExp>();
		for (Parameter parameter : parameters) {
			if (this.model.findClassInfoByName(parameter.getType().getName()) == null) {
				continue;
			}
			Set<Attribute> unchagedPropertiesForParameter = findUnchangedPropertyForParameter(changedPropertyCallExprs, parameter);
			Iterator<Attribute> pit = unchagedPropertiesForParameter.iterator();
			while (pit.hasNext()) {
				Attribute a = pit.next();
				if (TypeFactory.getInstance().getClassifier(a.getType()) instanceof PrimitiveType) {
					equal_exps.add(ASTUtil.getEqualExpForUnchangedAttribute(this.dresden_constraint, parameter.getName(), parameter.getType().getName(), parameters.indexOf(parameter) + 1, a));
				} else {
					equal_exps.add(ASTUtil.getEqualExpForAsc(this.dresden_constraint, model.findClassInfoByName(parameter.getType().getName()), parameter.getName(), parameters.indexOf(parameter) + 1,
							a));
				}

			}

		}
		return equal_exps;
	}

	private Set<Attribute> findUnchangedPropertyForParameter(final HashMap<String, Set<PropertyCallExp>> changedPropertyCallExprs, Parameter parameter) {
		Set<Attribute> unchagnedProperties = new HashSet<Attribute>(this.model.findClassInfoByName(parameter.getType().getName()).getAttrAndAscList());
		for (Entry<String, Set<PropertyCallExp>> e : changedPropertyCallExprs.entrySet()) {
			if (!e.getKey().equals(parameter.getName())) {
				continue;
			}

			for (PropertyCallExp p : e.getValue()) {
				Iterator<Attribute> i = unchagnedProperties.iterator();
				while (i.hasNext()) {
					Attribute a = i.next();
					if (a.getName().equals(p.getPropertyName())) {
						i.remove();
					}
				}
			}
		}
		return unchagnedProperties;
	}

	public List<Path> getBoundaryCombinationVariants() {
		List<Path> result = new ArrayList<Path>();
		result.add(new Path(this.nodes, this.model));
		List<List<Pair<Integer, String>>> variants = this.calculateVariants();
		for (final List<Pair<Integer, String>> elem : variants) {
			final GraphVisitor<ASTNode> dfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.PREORDER, new StackFrontier<ASTNode>());
			final AtomicInteger counter = new AtomicInteger(0);
			List<CLGNode> new_nodes = new ArrayList<CLGNode>();
			for (CLGNode node : this.nodes) {
				if (node instanceof ConstraintNode) {
					ConstraintNode constraintNode = (ConstraintNode) node.clone();
					new_nodes.add(constraintNode);
					dfs.traverse(constraintNode.getASTNode(), new NodeVisitHandler<ASTNode>() {
						@Override
						public void visit(ASTNode current_node) {
							final int i = counter.getAndIncrement();
							if (current_node instanceof OperationCallExp) {
								final OperationCallExp target = (OperationCallExp) current_node;
								String opname = target.getPropertyName();
								if (opname.equals("<=") || opname.equals(">=")) {
									for (Pair<Integer, String> p : elem) {
										if (p.getKey().equals(i)) {
											target.setPropertyName(p.getValue());
										}
									}
								}
							}
						}
					});
				} else {
					new_nodes.add(node.clone());
				}
			}
			result.add(new Path(new_nodes, this.model, false));
		}
		
		return result;
	}

	private List<List<Pair<Integer, String>>> calculateVariants() {
		final AtomicInteger counter = new AtomicInteger(0);
		List<Pair<Integer, List<String>>> relops = new ArrayList<Pair<Integer, List<String>>>();
		for (CLGNode node : this.nodes) {
			if (node instanceof ConstraintNode) {
				ConstraintNode constraintNode = (ConstraintNode) node;
				if (constraintNode.getASTNode() instanceof OperationCallExp) {
					OperationCallExp opcall = (OperationCallExp) constraintNode.getASTNode();
					if (opcall.getSourceExp() instanceof VariableExp && ((VariableExp) opcall.getSourceExp()).getVariableName().startsWith("#IterateIndex")) {
						continue;
					}
				}
				relops.addAll(calculateRelOpPositions(constraintNode.getASTNode(), counter));
			}
		}
		return boundary_combination(relops);
	}

	private List<Pair<Integer, List<String>>> calculateRelOpPositions(ASTNode node, final AtomicInteger counter) {
		final List<Pair<Integer, List<String>>> result = new ArrayList<Pair<Integer, List<String>>>();

		GraphVisitor<ASTNode> dfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.PREORDER, new StackFrontier<ASTNode>());
		dfs.traverse(node, new NodeVisitHandler<ASTNode>() {
			private List<String> ops;

			@Override
			public void visit(ASTNode current_node) {
				final int i = counter.getAndIncrement();
				if (current_node instanceof OperationCallExp) {
					OperationCallExp opcall = (OperationCallExp) current_node;

					String opname = opcall.getPropertyName();
					switch (opname) {
					case "<=":
						ops = new ArrayList<String>();
						ops.add("<");
						ops.add("=");
						result.add(Pair.of(i, ops));
						break;
					case ">=":
						ops = new ArrayList<String>();
						ops.add(">");
						ops.add("=");
						result.add(Pair.of(i, ops));
						break;
					}
				}
			}
		});
		return result;
	}

	private List<List<Pair<Integer, String>>> boundary_combination(List<Pair<Integer, List<String>>> relOps) {
		List<List<Pair<Integer, String>>> result = new ArrayList<List<Pair<Integer, String>>>();
		for (Pair<Integer, List<String>> elem : relOps) {
			if (result.size() == 0) {
				List<Pair<Integer, String>> permutation;
				permutation = new ArrayList<Pair<Integer, String>>();
				permutation.add(Pair.of(elem.getKey(), elem.getValue().get(0)));
				result.add(permutation);

				permutation = new ArrayList<Pair<Integer, String>>();
				permutation.add(Pair.of(elem.getKey(), elem.getValue().get(1)));
				result.add(permutation);
			} else {
				List<List<Pair<Integer, String>>> resultCopy = new ArrayList<List<Pair<Integer, String>>>();
				for (List<Pair<Integer, String>> result_elem : result) {
					List<Pair<Integer, String>> result_elem_copy = new ArrayList<Pair<Integer, String>>(result_elem);
					resultCopy.add(result_elem_copy);

					result_elem.add(Pair.of(elem.getKey(), elem.getValue().get(0)));
					result_elem_copy.add(Pair.of(elem.getKey(), elem.getValue().get(1)));
				}
				result.addAll(resultCopy);
			}
		}

		ListIterator<List<Pair<Integer, String>>> result_it = result.listIterator();
		while (result_it.hasNext()) {
			List<Pair<Integer, String>> elem = result_it.next();
			int i = 0;
			for (Pair<Integer, String> com_elem : elem) {
				if (com_elem.getValue().equals("=")) {
					i++;
					if (i > 1)
						break;
				}
			}
			if (i > 1) {
				result_it.remove();
			}
		}
		return result;
	}

}
