package ccu.pllab.tcgen.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import tudresden.ocl20.pivot.metamodels.uml2.internal.model.UML2Class;
import tudresden.ocl20.pivot.metamodels.uml2.internal.model.UML2Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGStartNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.EndNode;
import ccu.pllab.tcgen.clg.StartNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.node.GraphVisitor;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.node.NodeVisitHandler;
import ccu.pllab.tcgen.libs.node.QueueFrontier;
import ccu.pllab.tcgen.libs.pivotmodel.Attribute;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.PrimitiveType;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;

 
public class Constraint extends ASTNode {
	private tudresden.ocl20.pivot.pivotmodel.Constraint dresden_constraint;
	private ASTNode spec;
	private Model model;

	public Constraint(Model model, tudresden.ocl20.pivot.pivotmodel.Constraint obj, ASTNode spec) {
		super(obj);
		this.dresden_constraint = obj;
		this.model = model;
		this.spec = spec;
		
		this.spec.addPreviousNode(this);
	}

	public ASTNode getSpecification() {
		
		return this.spec;
	}

	public void setSpecification(ASTNode spec) {
		this.spec = spec;
	}

	public String getConstraintKind() {
		return this.dresden_constraint.getKind().toString();
	}

	public Model getModel() {
		return model;
	}

	public String getConstraintedClassName() {
		if (this.dresden_constraint.getConstrainedElement().get(0) instanceof UML2Class) {
			return ((UML2Class) this.dresden_constraint.getConstrainedElement().get(0)).getName();
		} else {
			return ((UML2Operation) this.dresden_constraint.getConstrainedElement().get(0)).getOwner().getName();
		}
	}

	public String getConstraintedMethodName() {
		if (this.dresden_constraint.getConstrainedElement().get(0) instanceof UML2Class) {
			return "";
		} else {
			return ((UML2Operation) this.dresden_constraint.getConstrainedElement().get(0)).getName();
		}
	}

	public String getConstrainedElement() {
		if (this.dresden_constraint.getConstrainedElement().get(0) instanceof UML2Class) {
			UML2Class uml2class = (UML2Class) this.dresden_constraint.getConstrainedElement().get(0);
			return uml2class.getName() + "::" + this.getConstraintKind();
		} else {
			UML2Operation op = (UML2Operation) this.dresden_constraint.getConstrainedElement().get(0);
			return op.getOwner().getName() + "::" + op.getName() + "::" + this.getConstraintKind();
		}
	}

	@Override
	public CLGNode toCLG(Criterion criterion) {
		ASTNode _copy = this.clone();
		ASTNode copy_tree = _copy.toPreProcessing();
		final CLGNode startNode = new StartNode(((Constraint) copy_tree).spec.getConstraint());
		final CLGNode endNode = new EndNode(((Constraint) copy_tree).spec.getConstraint());
		startNode.setEndNode(endNode);
		CLGNode clgNode = ((Constraint) copy_tree).spec.toCLG(criterion);
		startNode.addNextNode(clgNode);
		clgNode.getEndNode().addNextNode(endNode);
		return startNode;
	}

	@Override
	public Classifier getType() {
		return TypeFactory.getInstance().getClassifier("Boolean");
	}

	@Override
	public ASTNode clone() {
		return new Constraint(this.model, this.dresden_constraint, this.spec.clone());
	}

	@Override
	public String toOCL() {
		
		return this.getSpecification().toOCL();
	}

	@Override
	public String getLabelForGraphviz() {
		return this.getConstrainedElement();
	}

	@Override
	public String getState() {
		return this.getConstraintKind();
	}

	@Override
	public List<INode> getNextNodes() {
		INode[] nodes = new INode[] { this.getSpecification() };
		return new ArrayList<INode>(Arrays.asList(nodes));
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
		String result = ("n_" + this.getId() + "_" + this.getConstrainedElement()).replace("::", "_") + String.format("(%s, %s, %s)", instances_name, vars_name, result_name);
		return result;
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		return this.getPredicateName(templateArgs) + " :-\n" + "\t" + this.getSpecification().getPredicateName(templateArgs) + ".";
	}

	@Override
	public ASTNode toDeMorgan() {
		return this;
	}

	@Override
	public ASTNode toPreProcessing() {
		this.spec = this.spec.toPreProcessing();
		final Queue<ASTNode> new_variable_queue = new LinkedList<ASTNode>();
		final Queue<ASTNode> need_rename_queue = new LinkedList<ASTNode>();
		GraphVisitor<ASTNode> dfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.PREORDER, new QueueFrontier<ASTNode>());
		dfs.traverse(this.spec, new NodeVisitHandler<ASTNode>() {

			@Override
			public void visit(ASTNode current_node) {
				current_node = current_node.toPreProcessing();
				if (current_node instanceof OperationCallExp) {
					OperationCallExp _exp = (OperationCallExp) current_node;
					if (!(_exp.getPropertyName().equals("="))) {

						if (_exp.getSourceExp() instanceof IterateExp) {
							VariableExp newVar = new VariableExp(Constraint.this.getConstraint(), "#ASTnewVar_" + current_node.getId(), _exp.getSourceExp().getType(), _exp.getSourceExp().getState());
							need_rename_queue.add(_exp.getSourceExp());
							_exp.setSourceExp(newVar);
							new_variable_queue.add(newVar);
						}
						for (int i = 0; i < _exp.getParameterExps().size(); i++) {
							if (_exp.getParameterExps().get(i) instanceof IterateExp) {
								VariableExp newVar = new VariableExp(Constraint.this.getConstraint(), "#ASTnewVar_" + current_node.getId(), _exp.getParameterExps().get(i).getType(), _exp
										.getParameterExps().get(i).getState());
								need_rename_queue.add(_exp.getParameterExps().get(i));
								_exp.setParameterExpAtPosition(0, newVar);
								new_variable_queue.add(newVar);
							}
						}
					}
				} else if (current_node instanceof IfExp) {
					IfExp _exp = (IfExp) current_node;
					if (_exp.getConditionExp() instanceof IterateExp) {
						VariableExp newVar = new VariableExp(Constraint.this.getConstraint(), "#ASTnewVar_" + current_node.getId(), _exp.getConditionExp().getType(), _exp.getConditionExp().getState());
						need_rename_queue.add(_exp.getConditionExp());
						_exp.setConditionExp(newVar);
						new_variable_queue.add(newVar);
						need_rename_queue.add(_exp.getConditionExp());
					}
					if (_exp.getThenExp() instanceof IterateExp) {
						VariableExp newVar = new VariableExp(Constraint.this.getConstraint(), "#ASTnewVar_" + current_node.getId(), _exp.getThenExp().getType(), _exp.getThenExp().getState());
						need_rename_queue.add(_exp.getThenExp());
						_exp.setThenExp(newVar);
						new_variable_queue.add(newVar);
						need_rename_queue.add(_exp.getThenExp());
					}
					if (_exp.getElseExp() instanceof IterateExp) {
						VariableExp newVar = new VariableExp(Constraint.this.getConstraint(), "#ASTnewVar_" + current_node.getId(), _exp.getElseExp().getType(), _exp.getElseExp().getState());
						need_rename_queue.add(_exp.getElseExp());
						_exp.setElseExp(newVar);
						new_variable_queue.add(newVar);
						need_rename_queue.add(_exp.getElseExp());
					}

				} else if (current_node instanceof IterateExp) {
					IterateExp _exp = (IterateExp) current_node;
					if (_exp.getAccInitExp() instanceof IterateExp) {
						VariableExp newVar = new VariableExp(Constraint.this.getConstraint(), "#ASTnewVar_" + current_node.getId(), _exp.getAccInitExp().getType(), _exp.getAccInitExp().getState());
						need_rename_queue.add(_exp.getAccInitExp());
						_exp.setAccInitExp(newVar);
						new_variable_queue.add(newVar);
					}
					if (_exp.getSourceExp() instanceof IterateExp) {
						VariableExp newVar = new VariableExp(Constraint.this.getConstraint(), "#ASTnewVar_" + current_node.getId(), _exp.getSourceExp().getType(), _exp.getSourceExp().getState());
						need_rename_queue.add(_exp.getSourceExp());
						_exp.setSourceExp(newVar);
						new_variable_queue.add(newVar);
					}

				} else if (current_node instanceof IteratorExp) {
					IteratorExp _exp = (IteratorExp) current_node;
					if (_exp.getSourceExp() instanceof IterateExp) {
						VariableExp newVar = new VariableExp(Constraint.this.getConstraint(), "#ASTnewVar_" + current_node.getId(), _exp.getSourceExp().getType(), _exp.getSourceExp().getState());
						need_rename_queue.add(_exp.getSourceExp());
						_exp.setSourceExp(newVar);
						new_variable_queue.add(newVar);
					}
					for (int i = 0; i < _exp.getParameterExps().size(); i++) {
						if (_exp.getParameterExps().get(i) instanceof IterateExp) {
							VariableExp newVar = new VariableExp(Constraint.this.getConstraint(), "#ASTnewVar_" + current_node.getId(), _exp.getParameterExps().get(i).getType(), _exp
									.getParameterExps().get(i).getState());
							need_rename_queue.add(_exp.getParameterExps().get(i));
							_exp.setParameterExpAtPosition(0, newVar);
							new_variable_queue.add(newVar);
						}
					}
				}
			}
		});

		while (need_rename_queue.size() > 0 && new_variable_queue.size() > 0) {
			final ASTNode need_rename = need_rename_queue.remove();
			final VariableExp new_variable = (VariableExp) new_variable_queue.remove();
			ASTNode root_node = this.spec;
			final VariableExp new_equal_variable = new_variable.clone();
			OperationCallExp _exp = new OperationCallExp(need_rename.getConstraint(), new_equal_variable, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, need_rename);
			_exp.setAttribute("dummy_assign", Boolean.toString(true));
			OperationCallExp _and = new OperationCallExp(root_node.getConstraint(), _exp, "and", TypeFactory.getInstance().getClassifier("Boolean"), false, root_node);
			this.spec = _and;
		}

		return this;
	}

	public List<OperationCallExp> getEqualsExpForUnchangedProperties() {
		List<OperationCallExp> equal_exps = new ArrayList<OperationCallExp>();
		if (!this.getConstraintKind().equals("postcondition")) {
			return equal_exps;
		}
		final List<Parameter> parameters = ASTUtil.createNewParameterListForCLP(dresden_constraint);
		final HashMap<String, Set<PropertyCallExp>> changedPropertyCallExprs = getChangedProperties();

		for (Parameter parameter : parameters) {
			if (model.findClassInfoByName(parameter.getType().getName()) == null) {
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

	public HashMap<String, Set<PropertyCallExp>> getChangedProperties() {
		return ASTUtil.detectChangedProperties(model, dresden_constraint, Arrays.asList(this.spec));
	}

	private Set<Attribute> findUnchangedPropertyForParameter(final HashMap<String, Set<PropertyCallExp>> changedPropertyCallExprs, Parameter parameter) {
		Set<Attribute> unchagnedProperties = new HashSet<Attribute>(model.findClassInfoByName(parameter.getType().getName()).getAttrAndAscList());
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

	@Override
	public CLGGraph OCL2CLG() {
		ASTNode _copy = this.clone();
		ASTNode copy_tree = _copy.toPreProcessing();       
		CLGGraph clggraph = ((Constraint) copy_tree).spec.OCL2CLG();
		ArrayList attribute = new ArrayList();
		ArrayList arg = new ArrayList();
	
		((CLGStartNode) clggraph.getStartNode()).setClassName(this.getConstraintedClassName());
		((CLGStartNode) clggraph.getStartNode()).setMethodName(this.getConstraintedMethodName());
		/*設定attribute*/
		for(int i = 0 ; i< model.getClasses().size() ;i++) {
			for(int j = 0; j< model.getClasses().get(i).getAttrList().size(); j++) {
				attribute.add(model.getClasses().get(i).getAttrList().get(j).getName());
			}
		}
		((CLGStartNode) clggraph.getStartNode()).setClassAttributes(attribute);
		/*設定method arg*/
		for(int k = 0 ; k< model.getClasses().size() ;k++) {
			if(model.getClasses().get(k).findMethod(this.getConstraintedMethodName()) != null) {
				for(int l =0; l<model.getClasses().get(k).findMethod(this.getConstraintedMethodName()).getArgList().size();l++) {
					arg.add(model.getClasses().get(k).findMethod(this.getConstraintedMethodName()).getArgList().get(l).getKey());
				}
			}
		}
		((CLGStartNode) clggraph.getStartNode()).setMethodParameters(arg);
		
		if(   ((CLGStartNode) clggraph.getStartNode()).getClassName().equals(((CLGStartNode) clggraph.getStartNode()).getMethodName())         )
			((CLGStartNode) clggraph.getStartNode()).setIsConstructor(true);    
		CLGConstraint test = ((Constraint) copy_tree).spec.CLGConstraint();

		return clggraph;
	} 

	@Override
	public CLGConstraint CLGConstraint() {
		// TODO Auto-generated method stub
		return null;
	}


}
