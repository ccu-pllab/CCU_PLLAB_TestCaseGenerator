package ccu.pllab.tcgen.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tudresden.ocl20.pivot.essentialocl.expressions.Variable;
import tudresden.ocl20.pivot.metamodels.uml2.internal.model.UML2Class;
import tudresden.ocl20.pivot.metamodels.uml2.internal.model.UML2Operation;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.Type;
import ccu.pllab.tcgen.libs.node.GraphVisitor;
import ccu.pllab.tcgen.libs.node.NodeVisitHandler;
import ccu.pllab.tcgen.libs.node.StackFrontier;
import ccu.pllab.tcgen.libs.pivotmodel.Association;
import ccu.pllab.tcgen.libs.pivotmodel.AssociationEnd;
import ccu.pllab.tcgen.libs.pivotmodel.Attribute;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
 

public class ASTUtil {

	public static List<Parameter> createNewParameterListForCLP(Constraint constraint) {
		List<Parameter> parameter_list = new ArrayList<Parameter>();
		addSelfParameterIfNeed(constraint, parameter_list);
		if (constraint.getConstrainedElement().get(0) instanceof UML2Operation) {
			UML2Operation operation = (UML2Operation) constraint.getConstrainedElement().get(0);
			parameter_list.addAll(operation.getInputParameter());
		}
		addReturnParameterIfNeed(constraint, parameter_list);
		return parameter_list;
	}

	private static void addReturnParameterIfNeed(Constraint constraint, List<Parameter> parameter_list) {
		if (hasReturnValue(constraint)) {
			parameter_list.add(createNewParameter("result", ((UML2Operation) constraint.getConstrainedElement().get(0)).getReturnParameter().getType()));
		}
	}

	private static void addSelfParameterIfNeed(Constraint constraint, List<Parameter> parameter_list) {
		if (!isStatic(constraint) || isConstructor(constraint)) {
			if (constraint.getConstrainedElement().get(0) instanceof UML2Class) {
				parameter_list.add(createNewParameter("self", (UML2Class) constraint.getConstrainedElement().get(0)));
			} else {
				parameter_list.add(createNewParameter("self", ((UML2Operation) constraint.getConstrainedElement().get(0)).getOwningType()));
			}

		}
	}

	public static List<Parameter> createNewParameterListForIterate(Constraint constraint, Parameter... extracts) {
		List<Parameter> parameter_list = createNewParameterListForCLP(constraint);
		parameter_list.addAll(Arrays.asList(extracts));
		return parameter_list;
	}

	public static Parameter createNewParameter(String name, Type type) {
		Parameter p = PivotModelFactory.eINSTANCE.createParameter();
		p.setName(name);
		p.setType(type);
		return p;
	}

	public static List<Parameter> createNewParameterListForIterator(Constraint constraint, List<Parameter> inputParameter) {
		List<Parameter> parameter_list = new ArrayList<Parameter>();
		addSelfParameterIfNeed(constraint, parameter_list);
		parameter_list.addAll(inputParameter);
		addReturnParameterIfNeed(constraint, parameter_list);
		return parameter_list;
	}

	public static List<Parameter> getVarsForModel(Constraint constraint) {
		if (constraint.getConstrainedElement().get(0) instanceof UML2Class) {
			return new ArrayList<Parameter>();
		}

		return ((Operation) constraint.getConstrainedElement().get(0)).getInputParameter();
	}

	public static int getVarsLengthForCLP(Constraint constraint) {
		int l = getVarsForModel(constraint).size();
		if (!isStatic(constraint) || isConstructor(constraint)) {
			l++;
		}
		if (hasReturnValue(constraint)) {
			l++;
		}
		return l;
	}

	public static boolean hasReturnValue(Constraint constraint) {
		if (constraint.getConstrainedElement().get(0) instanceof UML2Class) {
			return false;
		} else {
			Operation op = (Operation) constraint.getConstrainedElement().get(0);
			return op.getReturnParameter() != null && !op.getReturnParameter().getType().getName().contains("Void");
		}
	}

	public static boolean isStatic(Constraint constraint) {
		if (constraint.getConstrainedElement().get(0) instanceof UML2Class) {
			return false;
		}

		Operation operation = (Operation) constraint.getConstrainedElement().get(0);
		return operation.isStatic();
	}

	public static boolean isConstructor(Constraint constraint) {
		if (constraint.getConstrainedElement().get(0) instanceof UML2Class) {
			return false;
		}
		Operation operation = (Operation) constraint.getConstrainedElement().get(0);
		return operation.getOwningType().getName().toString().equals(operation.getName().toString());
	}

	public enum AST_Operator {
		EqualTo("="), NonEqualTo("<>"), LessThan("<"), GreaterThan(">"), LessEqualTo("<="), GreaterEqualTo(">="), And("and"), Or("or");
		private String name;

		AST_Operator(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static final int NewVariableFirstIndex = 8;
	private long counterForId = 5000;

	private int newVariableIndex;

	public ASTUtil() {
	}

	// ......................Variable.......................

	public VariableExp createNewVaraible(Constraint constraint, Variable var) {
		VariableExp node = new VariableExp(constraint, var.getName(), TypeFactory.getInstance().getClassifier(var.getType().getName()), constraint.getKind().getName());
		node.setCLPIndex(Integer.toString(this.newVariableIndex++));
		return node;
	}

	public long getCounterForId() {
		counterForId++;
		return counterForId;
	}

	public void resetNewVariableIndex(Constraint constraint) {
		newVariableIndex = ASTUtil.getVarsLengthForCLP(constraint) + NewVariableFirstIndex;
	}

	static public OperationCallExp getEqualExpForAsc(Constraint constraint, ccu.pllab.tcgen.libs.pivotmodel.UML2Class clazz, String sourceName, int sourceVaraibleIndex, Attribute a) {
		VariableExp preVar = new VariableExp(constraint, sourceName, TypeFactory.getInstance().getClassifier(clazz.getName()), "precondition");
		preVar.setCLPIndex(Integer.toString(sourceVaraibleIndex));
		VariableExp postVar = new VariableExp(constraint, sourceName, TypeFactory.getInstance().getClassifier(clazz.getName()), "postcondition");
		postVar.setCLPIndex(Integer.toString(sourceVaraibleIndex));
		Association asc = clazz.findAscByAttributeName(a.getName());
		AssociationEnd ascEnd = asc.getRoleList().get(1);
		AssociationEnd anotherEnd = asc.getRoleList().get(0);
		AssociationEndCallExp preAtt = new AssociationEndCallExp(constraint, preVar.clone(), a.getName(), TypeFactory.getInstance().getClassifier(String.format("Set(%s)", a.getType())),
				anotherEnd.getName(), ascEnd.getName(), asc.getName());
		AssociationEndCallExp postAtt = new AssociationEndCallExp(constraint, postVar.clone(), a.getName(), TypeFactory.getInstance().getClassifier(String.format("Set(%s)", a.getType())),
				anotherEnd.getName(), ascEnd.getName(), asc.getName());
		return (new OperationCallExp(constraint, postAtt, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, preAtt));
	}

	static public OperationCallExp getEqualExpForUnchangedAttribute(Constraint constraint, String sourceName, String sourceType, int sourceVaraibleIndex, Attribute a) {
		VariableExp preVar = new VariableExp(constraint, sourceName, TypeFactory.getInstance().getClassifier(sourceType), "precondition");
		preVar.setCLPIndex(Integer.toString(sourceVaraibleIndex));
		VariableExp postVar = new VariableExp(constraint, sourceName, TypeFactory.getInstance().getClassifier(sourceType), "postcondition");
		postVar.setCLPIndex(Integer.toString(sourceVaraibleIndex));
		AttributeCallExp preAtt = new AttributeCallExp(constraint, preVar, a.getName(), TypeFactory.getInstance().getClassifier(a.getType()));
		AttributeCallExp postAtt = new AttributeCallExp(constraint, postVar, a.getName(), TypeFactory.getInstance().getClassifier(a.getType()));
		OperationCallExp equal_for_att = new OperationCallExp(constraint, postAtt, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, preAtt);
		return equal_for_att;
	}

	static public HashMap<String, Set<PropertyCallExp>> detectChangedProperties(final Model model, final Constraint dresden_constraint, final List<ASTNode> exps) {
		final List<Parameter> parameters = ASTUtil.createNewParameterListForCLP(dresden_constraint);
		final HashMap<String, Set<PropertyCallExp>> changedPropertyCallExprs = new HashMap<String, Set<PropertyCallExp>>();
		GraphVisitor<ASTNode> dfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.POSTORDER, new StackFrontier<ASTNode>());
		for (ASTNode node : exps) {
			dfs.traverse(node, new NodeVisitHandler<ASTNode>() {

				@Override
				public void visit(ASTNode current_node) {
					if (current_node instanceof ccu.pllab.tcgen.ast.OperationCallExp) {
						final ASTNode op_source = ((ccu.pllab.tcgen.ast.OperationCallExp) current_node).getSourceExp();
						if (op_source instanceof ccu.pllab.tcgen.ast.AttributeCallExp || op_source instanceof ccu.pllab.tcgen.ast.AssociationEndCallExp) {
							ccu.pllab.tcgen.ast.PropertyCallExp v = (ccu.pllab.tcgen.ast.PropertyCallExp) op_source;
							if (v.getSourceExp() instanceof VariableExp && v.getState().equals("postcondition")
									&& Integer.parseInt(((VariableExp) v.getSourceExp()).getCLPIndex()) <= parameters.size()) {
								VariableExp source = (VariableExp) v.getSourceExp();
								if (changedPropertyCallExprs.get(source.getVariableName()) == null) {
									changedPropertyCallExprs.put(source.getVariableName(), new HashSet<PropertyCallExp>());
								}
								changedPropertyCallExprs.get(source.getVariableName()).add(v);
							}
						}

						// else if (current_node instanceof OperationCallExp) {
						// OperationCallExp opCall = (OperationCallExp)
						// current_node;
						// if (opCall.isMethod()) {
						// ccu.pllab.tcgen.libs.pivotmodel.UML2Operation
						// operation =
						// model.findClassInfoByName(opCall.getSourceExp().getType().getName()).findMethod(opCall.getPropertyName());
						// List<Set<PropertyCallExp>> changedProperties =
						// operation.getChangedPropertiesOrderByParameters();
						// Map<ASTNode, Set<PropertyCallExp>> changedProperties2
						// = new HashMap<ASTNode, Set<PropertyCallExp>>();
						// changedProperties2.put(opCall.getSourceExp(),
						// changedProperties.get(0));
						// for (ASTNode parameter : opCall.getParameterExps()) {
						// changedProperties2.put(parameter,
						// changedProperties.get(opCall.getParameterExps().indexOf(parameter)
						// + 1));
						// }
						//
						// for (Entry<ASTNode, Set<PropertyCallExp>> e :
						// changedProperties2.entrySet()) {
						// if (e.getKey() instanceof VariableExp) {
						// VariableExp source = (VariableExp) e.getKey();
						// if
						// (changedPropertyCallExprs.get(source.getVariableName())
						// == null) {
						// changedPropertyCallExprs.put(source.getVariableName(),
						// new HashSet<PropertyCallExp>());
						// }
						// if (e.getValue() == null) {
						// continue;
						// }
						// for (PropertyCallExp pc : e.getValue()) {
						// changedPropertyCallExprs.get(source.getVariableName()).add(pc);
						// }
						//
						// }
						// }
						// }
						//
						// }
					}
				}
			});
		}
		return changedPropertyCallExprs;
	}
}
