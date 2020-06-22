package ccu.pllab.tcgen.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGConnectionNode;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.ConnectionNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.node.GraphVisitor;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.node.NodeVisitHandler;
import ccu.pllab.tcgen.libs.node.StackFrontier;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;;

 
public class IterateExp extends LoopExp {

	private ASTNode accInitExp;
	private ASTNode bodyExp;
	static private int global_iterate_id = 0;
	private int iterate_id;

	public IterateExp(Constraint obj, ASTNode source, String name, ASTNode accInitExp, ASTNode bodyExp) {
		super(obj, source, name, accInitExp.getType());
		this.accInitExp = accInitExp;
		this.bodyExp = bodyExp;
		accInitExp.addPreviousNode(this);
		bodyExp.addPreviousNode(this);
		this.iterate_id = global_iterate_id++;
	}

	public ASTNode getAccInitExp() {
		return accInitExp;
	}

	public ASTNode getBodyExp() {
		return bodyExp;
	}

	public void setBodyExp(ASTNode bodyExp) {
		this.bodyExp = bodyExp;
	}

	public void setAccInitExp(ASTNode accInitExp) {
		this.accInitExp = accInitExp;
	}

	@Override
	public CLGNode toCLG(Criterion criterion) {
		IteratorExp size_value = new IteratorExp(this.getConstraint(), this.getSourceExp(), "size", TypeFactory.getInstance().getClassifier("Integer"));
		VariableExp incresment_var = new VariableExp(this.getConstraint(), String.format("#IterateIndex%d", this.iterate_id), TypeFactory.getInstance().getClassifier("Integer"), this.getConstraint()
				.getKind().getName());
		// IterateIndex = 0
		CLGNode clgIndexInit = new ConstraintNode(this.getConstraint(), new OperationCallExp(this.getConstraint(), incresment_var.clone(), "=", TypeFactory.getInstance().getClassifier("Boolean"), false, new LiteralExp(this.getConstraint(), TypeFactory.getInstance().getClassifier("Integer"), "1")));
		// IterateAccumulator = accumulator initial value
		VariableExp acc_var = new VariableExp(this.getConstraint(), String.format("#IterateAcc%d", this.iterate_id), this.getType(), this.getConstraint().getKind().getName());
		OperationCallExp equal_acc = new OperationCallExp(this.getConstraint(), acc_var, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, this.accInitExp);
		equal_acc.setAttribute("dummy_assign", Boolean.toString(true));
		CLGNode clgAccInitNode = new ConstraintNode(this.getConstraint(), equal_acc);
		ConnectionNode clgIterateConjunctionNode = new ConnectionNode(this.getConstraint());
		// IterateIndex <= IterateColSize
		OperationCallExp less_equal_collection_size_node = new OperationCallExp(this.getConstraint(), incresment_var.clone(), "<=", TypeFactory.getInstance().getClassifier("Boolean"), false,
				size_value);
		ConstraintNode clgLessCollectionSizeNode = new ConstraintNode(this.getConstraint(), less_equal_collection_size_node);

		// IterateElement = collection->at(IterateIndex)
		VariableExp element = new VariableExp(this.getConstraint(), String.format("#IterateElement%d", this.iterate_id), TypeFactory.getInstance().getClassifier(this.getAttribute("iterator_type")),
				this.getConstraint().getKind().getName());
		IteratorExp collection_postition = new IteratorExp(this.getConstraint(), this.getSourceExp(), "at", TypeFactory.getInstance().getClassifier(this.getAttribute("iterator_type")),
				incresment_var.clone());
		OperationCallExp element_equal = new OperationCallExp(this.getConstraint(), element.clone(), "=", TypeFactory.getInstance().getClassifier("Boolean"), false, collection_postition);
		element_equal.setAttribute("dummy_assign", Boolean.toString(true));
		ConstraintNode clgElementPick = new ConstraintNode(this.getConstraint(), element_equal);

		// IterateAccumulator = iterate body
		OperationCallExp acc_equal_body = new OperationCallExp(this.getConstraint(), acc_var.clone(), "=", TypeFactory.getInstance().getClassifier("Boolean"), false,
				renameAccAndElement(this.getBodyExp()));
		acc_equal_body.setAttribute("dummy_assign", Boolean.toString(true));
		ASTNode transform_equal_body = acc_equal_body.clone().toPreProcessing();
		CLGNode clgAccEqualBodyNode = transform_equal_body.toCLG(criterion);
		
		// IterateIndex = IterateIndex + 1
		CLGNode clgIndexInc = new ConstraintNode(this.getConstraint(), new OperationCallExp(this.getConstraint(), incresment_var.clone(), "=", TypeFactory.getInstance().getClassifier("Boolean"), false,
				new OperationCallExp(this.getConstraint(), incresment_var.clone(), "+", TypeFactory.getInstance().getClassifier("Integer"), false, new LiteralExp(this.getConstraint(), TypeFactory.getInstance().getClassifier("Integer"), "1"))));

		// IterateIndex > IterateColSize
		OperationCallExp greater_collection_size_node = new OperationCallExp(this.getConstraint(), incresment_var.clone(), ">", TypeFactory.getInstance().getClassifier("Boolean"), false, size_value);
		ConstraintNode clgGreaterCollectionSizeNode = new ConstraintNode(this.getConstraint(), greater_collection_size_node);

		// ResultAcc = IterateAccumulator
		VariableExp result = new VariableExp(this.getConstraint(), String.format("#ResultAcc%d", this.iterate_id), ((ASTNode) this.getNextNodes().get(1)).getType(), this.getConstraint().getKind()
				.getName());
		OperationCallExp acc_equal_result_node = new OperationCallExp(this.getConstraint(), result, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, acc_var.clone());
		acc_equal_result_node.setAttribute("dummy_assign", Boolean.toString(true));
		ConstraintNode clgResultNode = new ConstraintNode(this.getConstraint(), acc_equal_result_node);

		clgAccInitNode.addNextNode(clgIndexInit);
		clgIndexInit.addNextNode(clgIterateConjunctionNode);
		clgIterateConjunctionNode.addNextNode(clgLessCollectionSizeNode);
		clgLessCollectionSizeNode.addNextNode(clgElementPick);
		clgElementPick.addNextNode(clgAccEqualBodyNode);
		clgAccEqualBodyNode.getEndNode().addNextNode(clgIndexInc);
		clgIndexInc.addNextNode(clgIterateConjunctionNode);
		clgIterateConjunctionNode.addNextNode(clgGreaterCollectionSizeNode);
		clgGreaterCollectionSizeNode.addNextNode(clgResultNode);
		clgAccInitNode.setEndNode(clgResultNode);
		return clgAccInitNode;
	}

	public int getIterate_id() {
		return this.iterate_id;
	}

	@Override
	public IterateExp clone() {
		IterateExp n = new IterateExp(this.getConstraint(), this.getSourceExp().clone(), this.getPropertyName(), this.getAccInitExp(), this.getBodyExp().clone());
		n.setAttributes(this.getAttributes());
		return n;
	}

	@Override
	public String toOCL() {
		String result = this.getSourceExp() + "->iterate(" + this.getAttribute("iterator_name") + ":" + this.getAttribute("iterator_type") + ";" + this.getAttribute("result_name") + ":"
				+ this.getAttribute("result_name") + "=" + this.getAccInitExp() + "|" + this.getBodyExp() + ")";
    
		return result;
	}

	@Override
	public List<INode> getNextNodes() {
		List<INode> nodes = super.getNextNodes();
		nodes.add(this.getAccInitExp());
		nodes.add(this.getBodyExp());
		return nodes;
	}

	@Override
	public String getPredicateName(Map<String, String> template_args) {
		ST tpl = TemplateFactory.getTemplate("ocl_iterate_operation_call");
		tpl.add("node_identifier", this.getId());
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> template_args) {
		ST tpl = TemplateFactory.getTemplate("ocl_iterate_operation_body");
		tpl.add("node_identifier", this.getId());
		tpl.add("collection_predicate", this.getSourceExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		tpl.add("acc_init_predicate", this.getAccInitExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		tpl.add("acc_iteration_predicate", this.getBodyExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		for (Map.Entry<String, String> entry : template_args.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public ASTNode toDeMorgan() {
		return this;
	}

	@Override
	public ASTNode toPreProcessing() {
		this.bodyExp = this.bodyExp.toPreProcessing();
		return this;
	}

	private ASTNode renameAccAndElement(ASTNode bodyExp) {
		GraphVisitor<ASTNode> dfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.POSTORDER, new StackFrontier<ASTNode>());
		dfs.traverse(bodyExp, new NodeVisitHandler<ASTNode>() {

			@Override
			public void visit(ASTNode current_node) {
				if (current_node instanceof VariableExp && ((VariableExp) current_node).getVariableName().equals(IterateExp.this.getAttribute("iterator_name"))) {
					((VariableExp) current_node).setVariableName(String.format("IterateElement%d", IterateExp.this.iterate_id));
				} else if (current_node instanceof VariableExp && ((VariableExp) current_node).getVariableName().equals(IterateExp.this.getAttribute("result_name"))) {
					((VariableExp) current_node).setVariableName(String.format("IterateAcc%d", IterateExp.this.iterate_id));
				}
			}
		});
		return bodyExp;
	}

	@Override
	public CLGGraph OCL2CLG() {
		IteratorExp size_value = new IteratorExp(this.getConstraint(), this.getSourceExp(), "size", TypeFactory.getInstance().getClassifier("Integer"));
		VariableExp incresment_var = new VariableExp(this.getConstraint(), String.format("IterateIndex%d", this.iterate_id), TypeFactory.getInstance().getClassifier("Integer"), this.getConstraint()
				.getKind().getName());
		// IterateIndex = 0
		CLGConstraint Left = new CLGVariableNode(incresment_var.toOCL());
		CLGConstraint Right = new CLGLiteralNode("1");
		CLGOperatorNode IterateIndex = new CLGOperatorNode();
		IterateIndex.setLeftOperand(Left);
		IterateIndex.setOperator("=");
		IterateIndex.setRightOperand(Right);
		CLGGraph clgIndexInit =new CLGGraph(IterateIndex);

		// IterateAccumulator = accumulator initial value
		VariableExp acc_var = new VariableExp(this.getConstraint(), String.format("IterateAcc%d", this.iterate_id), this.getType(), this.getConstraint().getKind().getName());
		OperationCallExp equal_acc = new OperationCallExp(this.getConstraint(), acc_var, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, this.accInitExp);
		
		equal_acc.setAttribute("dummy_assign", Boolean.toString(true));
		CLGConstraint Leftinit = new CLGVariableNode(acc_var.toOCL());
		CLGConstraint accInitExp = new CLGLiteralNode(this.accInitExp.toOCL());
		CLGOperatorNode IterateAccumulator = new CLGOperatorNode();
		IterateAccumulator.setLeftOperand(Leftinit);
		IterateAccumulator.setOperator("=");
		IterateAccumulator.setRightOperand(accInitExp);
		
		CLGGraph clgAccInitNode =new CLGGraph(IterateAccumulator);

	
		// IterateIndex <= IterateColSize
		OperationCallExp less_equal_collection_size_node = new OperationCallExp(this.getConstraint(), incresment_var.clone(), "<=", TypeFactory.getInstance().getClassifier("Boolean"), false,
				size_value);
		
		CLGConstraint size_value_constraint = new CLGVariableNode(size_value.toOCL());
		CLGOperatorNode LessCollectionSize = new CLGOperatorNode();
		LessCollectionSize.setLeftOperand(Left);
		LessCollectionSize.setOperator("<=");
		LessCollectionSize.setRightOperand(size_value_constraint);
		CLGGraph clgLessCollectionSizeNode =new CLGGraph(LessCollectionSize);
		
		
		// IterateElement = collection->at(IterateIndex) 1
		VariableExp element = new VariableExp(this.getConstraint(), String.format("IterateElement%d", this.iterate_id), TypeFactory.getInstance().getClassifier(this.getAttribute("iterator_type")),
				this.getConstraint().getKind().getName());
		IteratorExp collection_postition = new IteratorExp(this.getConstraint(), this.getSourceExp(), "at", TypeFactory.getInstance().getClassifier(this.getAttribute("iterator_type")),
				incresment_var.clone());
		OperationCallExp element_equal = new OperationCallExp(this.getConstraint(), element.clone(), "=", TypeFactory.getInstance().getClassifier("Boolean"), false, collection_postition);
		element_equal.setAttribute("dummy_assign", Boolean.toString(true));
		
	
	
		CLGOperatorNode ElementPick = new CLGOperatorNode();
		
		CLGConstraint ElementPick_left = new CLGVariableNode(element.clone().toOCL());
		CLGConstraint ElementPick_right = new CLGVariableNode(collection_postition.toOCL());
		
		ElementPick.setOperator("=");
		ElementPick.setLeftOperand(ElementPick_left);
		ElementPick.setRightOperand(ElementPick_right);
		
		CLGGraph clgElementPick =new CLGGraph(ElementPick);
		
	

		// IterateAccumulator = iterate body
		OperationCallExp acc_equal_body = new OperationCallExp(this.getConstraint(), acc_var.clone(), "=", TypeFactory.getInstance().getClassifier("Boolean"), false,
				renameAccAndElement(this.getBodyExp()));
		acc_equal_body.setAttribute("dummy_assign", Boolean.toString(true));
		
	
		ASTNode transform_equal_body = acc_equal_body.clone().toPreProcessing();	
		
	
		
		CLGGraph clgAccEqualBodyNode = transform_equal_body.OCL2CLG();
		// IterateIndex = IterateIndex + 1
		CLGOperatorNode IndexInc = new CLGOperatorNode();
		
		IndexInc.setLeftOperand(Left);
		IndexInc.setOperator("+");
		IndexInc.setRightOperand(Right);
		
		CLGOperatorNode ALLIndexInc = new CLGOperatorNode();
		ALLIndexInc.setLeftOperand(Left);
		ALLIndexInc.setOperator("=");
		ALLIndexInc.setRightOperand(IndexInc);
		
		CLGGraph clgIndexInc =new CLGGraph(ALLIndexInc);

		// IterateIndex > IterateColSize 1
		OperationCallExp greater_collection_size_node = new OperationCallExp(this.getConstraint(), incresment_var.clone(), ">", TypeFactory.getInstance().getClassifier("Boolean"), false, size_value);
		

		CLGOperatorNode GreaterCollectionSizeNode = new CLGOperatorNode();
		
		CLGConstraint GreaterCollectionSizeNode_left = new CLGVariableNode(incresment_var.clone().toOCL());
		CLGConstraint GreaterCollectionSizeNode_Right= new CLGVariableNode(size_value.toOCL());
		
		GreaterCollectionSizeNode.setOperator(">");
		GreaterCollectionSizeNode.setLeftOperand(GreaterCollectionSizeNode_left);
		GreaterCollectionSizeNode.setRightOperand(GreaterCollectionSizeNode_Right);
		
		CLGGraph clgGreaterCollectionSizeNode =new CLGGraph(GreaterCollectionSizeNode);

		// ResultAcc = IterateAccumulator 1
		VariableExp result = new VariableExp(this.getConstraint(), String.format("ResultAcc%d", this.iterate_id), ((ASTNode) this.getNextNodes().get(1)).getType(), this.getConstraint().getKind()
				.getName());
		OperationCallExp acc_equal_result_node = new OperationCallExp(this.getConstraint(), result, "=", TypeFactory.getInstance().getClassifier("Boolean"), false, acc_var.clone());
		acc_equal_result_node.setAttribute("dummy_assign", Boolean.toString(true));
			
		CLGOperatorNode acc_equal_result_node_constraint = new CLGOperatorNode();
		
		CLGConstraint ResultNode_left = new CLGVariableNode(result.toOCL());
		CLGConstraint acc_var_Right = new CLGVariableNode(acc_var.clone().toOCL());
		acc_equal_result_node_constraint.setOperator("=");
		acc_equal_result_node_constraint.setLeftOperand(ResultNode_left);
		acc_equal_result_node_constraint.setRightOperand(acc_var_Right);
		
		CLGGraph clgResultNode =new CLGGraph(acc_equal_result_node_constraint);
		
		//////////////////////////////////////////////////
		clgAccInitNode.graphAnd(clgIndexInit);
		
		clgLessCollectionSizeNode.graphAnd(clgElementPick);
		clgLessCollectionSizeNode.graphAnd(clgAccEqualBodyNode);
		clgLessCollectionSizeNode.graphAnd(clgIndexInc);
		clgLessCollectionSizeNode.graphClosure();
		
		clgAccInitNode.graphAnd(clgLessCollectionSizeNode);
		
		clgGreaterCollectionSizeNode.graphAnd(clgResultNode);
		
		clgAccInitNode.graphAnd(clgGreaterCollectionSizeNode);
		
	
		
		return clgAccInitNode;
	}

	@Override
	public CLGConstraint CLGConstraint() {
		// TODO Auto-generated method stub
		return null;
	}


}
