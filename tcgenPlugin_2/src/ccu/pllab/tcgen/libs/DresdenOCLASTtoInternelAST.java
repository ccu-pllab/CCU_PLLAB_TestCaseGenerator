package ccu.pllab.tcgen.libs;
 

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import tudresden.ocl20.pivot.essentialocl.expressions.BooleanLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionItem;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionLiteralPart;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionRange;
import tudresden.ocl20.pivot.essentialocl.expressions.ExpressionInOcl;
import tudresden.ocl20.pivot.essentialocl.expressions.IfExp;
import tudresden.ocl20.pivot.essentialocl.expressions.IntegerLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.IterateExp;
import tudresden.ocl20.pivot.essentialocl.expressions.LetExp;
import tudresden.ocl20.pivot.essentialocl.expressions.OclExpression;
import tudresden.ocl20.pivot.essentialocl.expressions.OperationCallExp;
import tudresden.ocl20.pivot.essentialocl.expressions.PropertyCallExp;
import tudresden.ocl20.pivot.essentialocl.expressions.RealLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.StringLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.TypeLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.VariableExp;
import tudresden.ocl20.pivot.essentialocl.expressions.util.ExpressionsSwitch;
import tudresden.ocl20.pivot.essentialocl.types.CollectionType;
import tudresden.ocl20.pivot.metamodels.uml2.internal.model.UML2Class;
import tudresden.ocl20.pivot.metamodels.uml2.internal.model.UML2Operation;
import tudresden.ocl20.pivot.pivotmodel.ConstrainableElement;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import ccu.pllab.tcgen.ast.ASTNode;
import ccu.pllab.tcgen.ast.ASTUtil;
import ccu.pllab.tcgen.ast.AssociationEndCallExp;
import ccu.pllab.tcgen.ast.AttributeCallExp;
import ccu.pllab.tcgen.ast.LiteralExp;
import ccu.pllab.tcgen.libs.node.GraphVisitor;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.node.NodeVisitHandler;
import ccu.pllab.tcgen.libs.node.StackFrontier;
import ccu.pllab.tcgen.libs.pivotmodel.Association;
import ccu.pllab.tcgen.libs.pivotmodel.AssociationEnd;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;

public class DresdenOCLASTtoInternelAST extends ExpressionsSwitch<ASTNode> {
	private Constraint constraint;
	private UML2Class constraintedClass;
	private ConstrainableElement constraintedElement;

	private Stack<List<Parameter>> vars_stack;
	private ASTUtil util;
	private Model context;

	public DresdenOCLASTtoInternelAST(ASTUtil util, Model context) {
		this.vars_stack = new Stack<List<Parameter>>();
		this.context = context;
		this.util = util;
	}

	@Override
	public ASTNode caseCollectionLiteralExp(CollectionLiteralExp object) {
		List<ASTNode> partList = new ArrayList<ASTNode>();
		for (CollectionLiteralPart part : object.getPart()) {
			partList.add(this.doSwitch(part));
		}
		return new ccu.pllab.tcgen.ast.CollectionLiteralExp(this.constraint, TypeFactory.getInstance().getClassifier(object.getType().getName()), partList);
	}

	@Override
	public ASTNode caseCollectionRange(CollectionRange object) {
		return new ccu.pllab.tcgen.ast.CollectionRange(constraint, TypeFactory.getInstance().getClassifier(object.getType().getName()), this.doSwitch(object.getFirst()), this.doSwitch(object
				.getLast()));
	}

	@Override
	public ASTNode caseCollectionLiteralPart(CollectionLiteralPart object) {
		return super.caseCollectionLiteralPart(object);
	}

	@Override
	public ASTNode caseCollectionItem(CollectionItem object) {
		return this.doSwitch(object.getItem());
	}

	@Override
	public ASTNode caseBooleanLiteralExp(BooleanLiteralExp object) {
		return new LiteralExp(this.constraint, TypeFactory.getInstance().getClassifier(object.getType().getName()), Boolean.toString(object.isBooleanSymbol()));
	}

	@Override
	public ASTNode caseExpressionInOcl(ExpressionInOcl object) {
		return this.doSwitch(object.getBodyExpression());
	}

	@Override
	public ccu.pllab.tcgen.ast.IfExp caseIfExp(IfExp object) {
		ccu.pllab.tcgen.ast.IfExp node = new ccu.pllab.tcgen.ast.IfExp(this.constraint, this.doSwitch(object.getCondition()), this.doSwitch(object.getThenExpression()), this.doSwitch(object
				.getElseExpression()));
		return node;
	}

	@Override
	public ASTNode caseIntegerLiteralExp(IntegerLiteralExp object) {
		return new LiteralExp(this.constraint, TypeFactory.getInstance().getClassifier(object.getType().getName()), Integer.toString(object.getIntegerSymbol()));
	}

	@Override
	public ccu.pllab.tcgen.ast.IterateExp caseIterateExp(final IterateExp object) {
		Parameter it_param = PivotModelFactory.eINSTANCE.createParameter();
		it_param.setName(object.getIterator().get(0).getName());
		Parameter acc_param = PivotModelFactory.eINSTANCE.createParameter();
		acc_param.setName(object.getResult().getName());

		vars_stack.push(ASTUtil.createNewParameterListForIterate(this.constraint, acc_param, it_param));
		ccu.pllab.tcgen.ast.IterateExp node = new ccu.pllab.tcgen.ast.IterateExp(this.constraint, this.doSwitch(object.getSource()), "iterate", this.doSwitch(object.getResult().getInitExpression()),
				this.doSwitch(object.getBody()));
		node.setAttribute("iterator_name", object.getIterator().get(0).getName());
		node.setAttribute("iterator_type", object.getIterator().get(0).getType().getName());
		node.setAttribute("result_name", object.getResult().getName());
		node.setAttribute("result_type", object.getResult().getType().getName());

		vars_stack.pop();
		return node;
	}

	public ccu.pllab.tcgen.ast.IteratorExp caseIteratorExp(OperationCallExp object) {
		ASTNode source = this.doSwitch(object.getSource());
		List<OclExpression> arguments = object.getArgument();
		List<ASTNode> parameters = new ArrayList<ASTNode>();
		for (int i = 0; i < arguments.size(); i++) {
			parameters.add(this.doSwitch(arguments.get(i)));
		}
		return new ccu.pllab.tcgen.ast.IteratorExp(this.constraint, source, object.getReferredOperation().getName(), TypeFactory.getInstance().getClassifier(object.getType().getName()), parameters);
	}

	@Override
	public ASTNode caseLetExp(final LetExp object) {
		final ccu.pllab.tcgen.ast.VariableExp newVariable = util.createNewVaraible(this.constraint, object.getVariable());
		newVariable.setVariableName(String.format("#LetVar_%s_%s", newVariable.getVariableName(), newVariable.getCLPIndex()));
		final ASTNode init_exp = this.doSwitch(object.getVariable().getInitExpression());
		final ASTNode var_assign = new ccu.pllab.tcgen.ast.OperationCallExp(this.constraint, newVariable, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, init_exp);
		var_assign.setAttribute("dummy_assign", Boolean.toString(true));
		final ASTNode in_statements = this.doSwitch(object.getIn());
		final ASTNode root = new ccu.pllab.tcgen.ast.OperationCallExp(this.constraint, var_assign, "and", TypeFactory.getInstance().getClassifier("Boolean"), false, in_statements);
		GraphVisitor<ASTNode> dfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.POSTORDER, new StackFrontier<ASTNode>());
		dfs.traverse(in_statements, new NodeVisitHandler<ASTNode>() {

			@Override
			public void visit(ASTNode current_node) {
				if (!(current_node instanceof ccu.pllab.tcgen.ast.VariableExp)) {
					return;
				}
				ccu.pllab.tcgen.ast.VariableExp variableExp = (ccu.pllab.tcgen.ast.VariableExp) current_node;
				if (variableExp.getVariableName().equals(object.getVariable().getName())) {
					if (variableExp.getPreviousNodes().get(0).getNextNodes().indexOf(variableExp) > -1) {
						variableExp.setVariableName(String.format("#LetVar_%s_%s", variableExp.getVariableName(), newVariable.getCLPIndex()));
						variableExp.setCLPIndex(newVariable.getCLPIndex());
					}
				}
			}
		});
		return root;
	}

	@Override
	public ASTNode caseOperationCallExp(OperationCallExp object) {

		if (object.getName().equals("atPre")) {
			ASTNode node = this.doSwitch(object.getSource());
			node.setAttribute("annotate", "@pre");

			Queue<ASTNode> queue = new LinkedList<ASTNode>();
			queue.add(node);

			while (queue.size() > 0) {
				final ASTNode target = queue.poll();
				if (target instanceof ccu.pllab.tcgen.ast.VariableExp) {
					((ccu.pllab.tcgen.ast.VariableExp) target).setState("precondition");
				}

				for (INode child : target.getNextNodes()) {
					queue.add((ASTNode) child);
				}
			}
			return node;

		}
		if (object.getSource().getType() instanceof CollectionType && object.getReferredOperation().getName().matches("\\w+")) {
			return this.caseIteratorExp(object);
		}

		boolean isMethod = false;
		for (Operation op : constraintedClass.getOwnedOperation()) {
			if (op.equals(object.getReferredOperation())) {
				isMethod = true;
				break;
			}
		}
		Classifier type;
		if (ASTUtil.isConstructor(constraint)) {
			type = TypeFactory.getInstance().getClassifier(object.getType().getName());
		} else {
			type = TypeFactory.getInstance().getClassifier(object.getReferredOperation().getType().getName());
		}

		List<OclExpression> arguments = object.getArgument();
		ASTNode source = this.doSwitch(object.getSource());
		List<ASTNode> parameters = new ArrayList<ASTNode>();
		for (int i = 0; i < arguments.size(); i++) {
			parameters.add(this.doSwitch(arguments.get(i)));
		}
		return new ccu.pllab.tcgen.ast.OperationCallExp(this.constraint, source, object.getReferredOperation().getName(), type, isMethod, parameters);
	}

	@Override
	public ASTNode casePropertyCallExp(PropertyCallExp object) {
		ASTNode node;
		Association info = context.findAssociation(object.getSource().getType().getName(), object.getReferredProperty().getName());
		if (info != null) {

			AssociationEnd memberEnd = context.getAnotherMemberEnd(info, object.getSource().getType().getName(), object.getReferredProperty().getName());
			AssociationEndCallExp aecexp = new AssociationEndCallExp(this.constraint, doSwitch(object.getSource()), object.getReferredProperty().getName(), TypeFactory.getInstance().getClassifier(
					object.getType().getName()), object.getReferredProperty().getName(), memberEnd.getName(), info.getName());
			node = aecexp;
		} else {
			node = new AttributeCallExp(this.constraint, doSwitch(object.getSource()), object.getReferredProperty().getName(), TypeFactory.getInstance().getClassifier(object.getType().getName()));
		}
		return node;
	}

	@Override
	public ASTNode caseRealLiteralExp(RealLiteralExp object) {
		return new LiteralExp(this.constraint, TypeFactory.getInstance().getClassifier(object.getType().getName()), Double.toString(object.getRealSymbol()));
	}

	@Override
	public ASTNode caseStringLiteralExp(StringLiteralExp object) {
		return new LiteralExp(this.constraint, TypeFactory.getInstance().getClassifier(object.getType().getName()), object.getStringSymbol());
	}

	@Override
	public ASTNode caseTypeLiteralExp(TypeLiteralExp object) {
		return new ccu.pllab.tcgen.ast.TypeLiteralExp(this.constraint, TypeFactory.getInstance().getClassifier(object.getReferredType().getName()));
	}

	@Override
	public ccu.pllab.tcgen.ast.VariableExp caseVariableExp(VariableExp object) {
		ccu.pllab.tcgen.ast.VariableExp node = new ccu.pllab.tcgen.ast.VariableExp(this.constraint, object.getReferredVariable().getName(), TypeFactory.getInstance().getClassifier(
				object.getType().getName()), getVariableStateFromConstraintKind());
		node.setAttribute("label", object.getReferredVariable().getName());
		if (object.getReferredVariable().getName().contains("implicit")) {
			node.setAttribute("name", "self");
		}
		if (node.getVariableName().equals("self")) {
			node.setCLPIndex(Integer.toString(1));
		} else if (node.getVariableName().equals("result")) {
			if (ASTUtil.hasReturnValue(constraint)) {
				node.setCLPIndex(Integer.toString(ASTUtil.getVarsLengthForCLP(constraint)));
			} else {
				throw new IllegalStateException("there is no return value for method:" + node.getConstraint().getConstrainedElement().get(0));
			}
		} else {
			for (int i = 0; i < vars_stack.peek().size(); i++) {
				Parameter p = vars_stack.peek().get(i);
				if (object.getReferredVariable().getName().equals(p.getName())) {
					node.setCLPIndex(Integer.toString(i + 1));
					break;
				}
			}
		}
		if (object.getType() instanceof CollectionType) {
			node.setAttribute("element_type", ((CollectionType) object.getType()).getElementType().getName());
		}
		return node;
	}

	private String getVariableStateFromConstraintKind() {
		if (constraint.getKind().getName().equals("invariant")) {
			return "both";
		} else {
			return constraint.getKind().getName();
		}
	}

	private void getInformationFromConstraintedElement(Constraint constraint) {
		this.constraint = constraint;
		constraintedElement = constraint.getConstrainedElement().get(0);
		if (constraintedElement instanceof UML2Operation) {
			UML2Operation op = (UML2Operation) constraintedElement;
			constraintedClass = (UML2Class) op.getOwningType();
		} else if (constraintedElement instanceof UML2Class) {
			constraintedClass = (UML2Class) constraintedElement;
		}
	}

	public List<ccu.pllab.tcgen.ast.Constraint> parseOclTreeNodeFromPivotModel(List<Constraint> pivot_model_instances) {
		List<ccu.pllab.tcgen.ast.Constraint> tree_nodes = new ArrayList<ccu.pllab.tcgen.ast.Constraint>();
		for (Constraint constraint : pivot_model_instances) {
			getInformationFromConstraintedElement(constraint);
			util.resetNewVariableIndex(constraint);
			ccu.pllab.tcgen.ast.Constraint node;
			if (constraintedElement instanceof UML2Operation) {
				vars_stack.push(ASTUtil.createNewParameterListForCLP(constraint));
				if (vars_stack.peek().size() > 0 && vars_stack.peek().get(0).getName() == null) {
					vars_stack.peek().get(0).setName("self");
				}
				node = new ccu.pllab.tcgen.ast.Constraint(this.context, constraint, this.doSwitch(constraint.getSpecification()));
			} else {
				vars_stack.push(new ArrayList<Parameter>());
				node = new ccu.pllab.tcgen.ast.Constraint(this.context, constraint, this.doSwitch(constraint.getSpecification()));
			}
			tree_nodes.add(node);
			vars_stack.pop();
		}

		return tree_nodes;
	}

}
