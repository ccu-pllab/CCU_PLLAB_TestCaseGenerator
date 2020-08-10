package ccu.pllab.tcgen.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.ConnectionNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.libs.TemplateFactory;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
 

public class IfExp extends ASTNode {

	private ASTNode conditionExp;
	private ASTNode thenExp;
	private ASTNode elseExp;

	public IfExp(Constraint obj, ASTNode conditionExp, ASTNode thenExp, ASTNode elseExp) {
		super(obj);
		this.conditionExp = conditionExp;
		this.thenExp = thenExp;
		this.elseExp = elseExp;
		conditionExp.addPreviousNode(this);
		thenExp.addPreviousNode(this);
		elseExp.addPreviousNode(this);
	}

	public ASTNode getConditionExp() {
		
		return conditionExp;
	}

	public ASTNode getThenExp() {
		return thenExp;
	}

	public ASTNode getElseExp() {
		return elseExp;
	}

	public void setConditionExp(ASTNode conditionExp) {
		this.conditionExp = conditionExp;
	}

	public void setThenExp(ASTNode thenExp) {
		this.thenExp = thenExp;
	}

	public void setElseExp(ASTNode elseExp) {
		this.elseExp = elseExp;
	}

	@Override
	public CLGNode toCLG(Criterion criterion) {
		 
		CLGNode clgCondNode = this.getConditionExp().toCLG(criterion);
		ASTNode astnotCondNode = this.getConditionExp().clone();
		ASTNode astNotNode = new OperationCallExp(this.getConstraint(), astnotCondNode, "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
		CLGNode clgNotCondNode = astNotNode.toCLG(criterion);
		
		ConnectionNode clgNodeBeginConnecting = new ConnectionNode(this.getConstraint());
		ConnectionNode clgNodeEndConnecting = new ConnectionNode(this.getConstraint());
		CLGNode clgThenNode = this.getThenExp().toCLG(criterion);
		CLGNode clgElseNode = this.getElseExp().toCLG(criterion);

		clgNodeBeginConnecting.addNextNode(clgCondNode);
		clgNodeBeginConnecting.addNextNode(clgNotCondNode);
		clgCondNode.getEndNode().addNextNode(clgThenNode);
		clgNotCondNode.getEndNode().addNextNode(clgElseNode);
		clgThenNode.getEndNode().addNextNode(clgNodeEndConnecting);
		clgElseNode.getEndNode().addNextNode(clgNodeEndConnecting);
		clgNodeBeginConnecting.setEndNode(clgNodeEndConnecting);
		return clgNodeBeginConnecting;
	}

	@Override
	public Classifier getType() {
		return this.getThenExp().getType();
	}

	@Override
	public IfExp clone() {
		IfExp n = new IfExp(this.getConstraint(), this.getConditionExp().clone(), this.getThenExp().clone(), this.getElseExp().clone());
		return n;
	}

	@Override
	public String toOCL() { 
		
	
		return "if " + this.getConditionExp() + " then " + this.getThenExp() + " else " + this.getElseExp();
	}

	@Override
	public String getLabelForGraphviz() {
		return "if";
	} 

	@Override
	public String getState() {
		throw new IllegalStateException("there is no way to determine the state of if expr");
	}
 
	@Override
	public List<INode> getNextNodes() {
		INode[] nodes = new INode[] { this.getConditionExp(), this.getThenExp(), this.getElseExp() };
		return new ArrayList<INode>(Arrays.asList(nodes));
	}

	@Override
	public String getPredicateName(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("if_node_call");
		tpl.add("node_identifier", this.getId());
		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}

	@Override
	public String getEntirePredicate(Map<String, String> templateArgs) {
		ST tpl = TemplateFactory.getTemplate("if_node_body");
		tpl.add("node_identifier", this.getId());
		tpl.add("condition", this.getConditionExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		tpl.add("then_predicate", this.getThenExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		tpl.add("else_predicate", this.getElseExp().getPredicateName(new HashMap<String, String>()).replaceAll("\\(.*\\)", ""));
		for (Map.Entry<String, String> entry : templateArgs.entrySet()) {
			tpl.add(entry.getKey(), entry.getValue());
		}
		return tpl.render();
	}
 
	@Override
	public ASTNode toDeMorgan() {
		this.thenExp = this.thenExp.toDeMorgan();
		this.elseExp = this.elseExp.toDeMorgan();
		return this;
	}

	@Override
	public ASTNode toPreProcessing() {
		this.conditionExp = this.conditionExp.toPreProcessing();
		this.thenExp = this.thenExp.toPreProcessing();
		this.elseExp = this.elseExp.toPreProcessing();
		return this;
	}

	@Override
	public CLGGraph OCL2CLG() {

		OperationCallExp ifconstraint= (OperationCallExp)this.getConditionExp();
		CLGOperatorNode OperatiorConstraint = new CLGOperatorNode() ;
		CLGConstraint Left= null;
		CLGConstraint Right=null;	
		String op,L,R = null;
		CLGOperatorNode opconstraint= null;
		
		if(ifconstraint.getSourceExp() instanceof OperationCallExp && !ifconstraint.getSourceExp().toString().contains("()"))
		{
			Left = this.parseConstraint((OperationCallExp)ifconstraint.getSourceExp());
			
			op =  ifconstraint.getPropertyName().toString();
			if(op.equals("="))
			{
				OperatiorConstraint=new CLGOperatorNode("==");
			}
			else
			{
			OperatiorConstraint=new CLGOperatorNode(op);
			}
		
		}
		else if(ifconstraint.getSourceExp() instanceof PropertyCallExp)
		{
				 
				L = ifconstraint.getSourceExp().toString();
				Left = new CLGVariableNode(L);
				op =  ifconstraint.getPropertyName().toString();
				if(op.equals("="))
				{
					OperatiorConstraint=new CLGOperatorNode("==");
				}
				else
				{
				OperatiorConstraint=new CLGOperatorNode(op);
				}
		}
		else if(ifconstraint.getSourceExp().toString().equals("result"))
		{				
				L = ifconstraint.getSourceExp().toString();
				Left = new CLGVariableNode(L);
				op =  ifconstraint.getPropertyName().toString();
				if(op.equals("="))
				{
					OperatiorConstraint=new CLGOperatorNode("==");
				}
				else
				{
				OperatiorConstraint=new CLGOperatorNode(op);
				}
		}
		else if(ifconstraint.getSourceExp().toString().matches("[0-9]+"))
		{
			    L = ifconstraint.getSourceExp().toString();
				Left = new CLGLiteralNode(L);
				op =  ifconstraint.getPropertyName().toString();
				if(op.equals("="))
				{
					OperatiorConstraint=new CLGOperatorNode("==");
				}
				else
				{
				OperatiorConstraint=new CLGOperatorNode(op);
				}					
		}
		else{
			L = ifconstraint.getSourceExp().toString();
			Left = new CLGVariableNode(L);
			op =  ifconstraint.getPropertyName().toString();
			if(op.equals("="))
			{
				OperatiorConstraint=new CLGOperatorNode("==");
			}
			else
			{
			OperatiorConstraint=new CLGOperatorNode(op);
			}
		}
		
		/************/	 
        if(ifconstraint.getParameterExps().get(0) instanceof OperationCallExp && !ifconstraint.getParameterExps().get(0).toString().contains("()")) 	
		{
			Right = this.parseConstraint((OperationCallExp)ifconstraint.getParameterExps().get(0));
			op =  ifconstraint.getPropertyName().toString();
			if(op.equals("="))
			{
				OperatiorConstraint=new CLGOperatorNode("==");
			}
			else
			{
			OperatiorConstraint=new CLGOperatorNode(op);
			}	
		}	
        else if(ifconstraint.getParameterExps().get(0) instanceof PropertyCallExp)
		{
				Right = new CLGVariableNode(ifconstraint.getParameterExps().get(0).toString());
				op = ifconstraint.getPropertyName().toString();
				if(op.equals("="))
				{
					OperatiorConstraint=new CLGOperatorNode("==");
				}
				else
				{
				OperatiorConstraint=new CLGOperatorNode(op);
				}					
	    }
    	else if(ifconstraint.getParameterExps().get(0) instanceof IfExp)
		{
    		
    		Right = new CLGVariableNode(ifconstraint.getParameterExps().toString().substring(1,ifconstraint.getParameterExps().toString().length()-1));
    		op =  ifconstraint.getPropertyName().toString();
    		if(op.equals("="))
			{
				OperatiorConstraint=new CLGOperatorNode("==");
			}
			else
			{
			OperatiorConstraint=new CLGOperatorNode(op);
			}
							
	    }
		else if(ifconstraint.getParameterExps().get(0).toString().matches("[0-9]+"))
		{
				R = ifconstraint.getParameterExps().get(0).toString();
				Right = new CLGLiteralNode(R);
				op =  ifconstraint.getPropertyName().toString();
				if(op.equals("="))
				{
					OperatiorConstraint=new CLGOperatorNode("==");
				}
				else
				{
				OperatiorConstraint=new CLGOperatorNode(op);
				}					
	    }					
		else{
			Right = new CLGVariableNode(ifconstraint.getParameterExps().get(0).toString());
			op =  ifconstraint.getPropertyName().toString();
			if(op.equals("="))
			{
				OperatiorConstraint=new CLGOperatorNode("==");
			}
			else
			{
			OperatiorConstraint=new CLGOperatorNode(op);
			}	
		}
        OperatiorConstraint.setLeftOperand(Left);
        OperatiorConstraint.setRightOperand(Right);
        
        
        
        CLGGraph newclgConNode = new CLGGraph(OperatiorConstraint);
		ASTNode astnotCondNode = this.getConditionExp().clone();
		ASTNode astNotNode = new OperationCallExp(this.getConstraint(), astnotCondNode, "not", TypeFactory.getInstance().getClassifier("Boolean"), false);
		CLGGraph clgNotConNode = astNotNode.OCL2CLG();
		CLGGraph clgThenNode = this.getThenExp().OCL2CLG();
		CLGGraph clgElseNode = this.getElseExp().OCL2CLG();
		
	 
		
		newclgConNode.graphAnd(clgThenNode);
		clgNotConNode.graphAnd(clgElseNode);
		
		newclgConNode.graphOr(clgNotConNode);
		
		return newclgConNode;

		
	}
	
	
	
	
	
	
	public CLGConstraint parseConstraint(OperationCallExp opexp){
		
		CLGConstraint Left= null;
		CLGConstraint Right=null;	
	
		String LConstraint=null,op=null,RConstraint=null;
		CLGOperatorNode opconstraint= null;

		
		if(opexp.getSourceExp() instanceof OperationCallExp)
		{
			Left = parseConstraint((OperationCallExp)opexp.getSourceExp());
			op =  opexp.getPropertyName();
			if(op.equals("="))
			{
				opconstraint=new CLGOperatorNode("==");
			}
			else
			{
				opconstraint=new CLGOperatorNode(op);
			}
			
			
			if(opexp.getParameterExps().get(0) instanceof OperationCallExp)
			{

				Right = parseConstraint((OperationCallExp)opexp.getParameterExps().get(0));
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);
				
			}
			else if(opexp.getParameterExps().get(0) instanceof PropertyCallExp)
			{
				RConstraint = opexp.getParameterExps().get(0).toString();
				Right = new CLGVariableNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
			
			}
			else if(opexp.getParameterExps().get(0).toString().matches("[0-9]+"))
			{
				
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
	
            LConstraint=opexp.getSourceExp().toString();
			Left = new CLGVariableNode(LConstraint);
			op =  opexp.getPropertyName();
			if(op.equals("="))
			{
				opconstraint=new CLGOperatorNode("==");
			}
			else
			{
				opconstraint=new CLGOperatorNode(op);
			}		
			if(opexp.getParameterExps().get(0) instanceof PropertyCallExp)
		    {
				
			    RConstraint = opexp.getParameterExps().get(0).toString();
				Right = new CLGVariableNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
				LConstraint=op=RConstraint="";
			}
			else if(opexp.getParameterExps().get(0).toString().matches("[0-9]+"))
			{
				
				RConstraint = opexp.getParameterExps().get(0).toString();
				Right = new CLGLiteralNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);
				LConstraint=op=RConstraint="";
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
		
	else if(opexp.getSourceExp().toString().matches("[0-9]+"))
	  {
		
		      LConstraint = opexp.getSourceExp().toString();
		     Left = new CLGLiteralNode(LConstraint);
		     op =  opexp.getPropertyName().toString();
			 opconstraint=new CLGOperatorNode(" "+op+" ");	
			if(opexp.getParameterExps().get(0) instanceof PropertyCallExp)
			    {
				
					RConstraint =opexp.getParameterExps().get(0).toString();
					Right = new CLGVariableNode(RConstraint);
					opconstraint.setLeftOperand(Left);
					opconstraint.setRightOperand(Right);	
					LConstraint=op=RConstraint="";
				}
			else if(opexp.getParameterExps().get(0).toString().matches("[0-9]+"))
			    {
			
				RConstraint= opexp.getParameterExps().get(0).toString();
				Right = new CLGLiteralNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
				LConstraint=op=RConstraint="";
			    }				
	  }	
	else {
	
		 LConstraint = opexp.getSourceExp().toString();
		 Left = new CLGVariableNode(LConstraint);
	     op =  opexp.getPropertyName().toString();
	     if(op.equals("="))
			{
	    	 opconstraint=new CLGOperatorNode("==");
			}
			else
			{
				opconstraint=new CLGOperatorNode(op);
			}	
			
		 if(opexp.getParameterExps().get(0) instanceof OperationCallExp)
			{
			
				Right = parseConstraint((OperationCallExp)opexp.getParameterExps().get(0));
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);
				
			}
		 
		 else if(opexp.getParameterExps().get(0) instanceof PropertyCallExp)
		    {
			
				RConstraint =opexp.getParameterExps().get(0).toString();
				Right = new CLGVariableNode(RConstraint);
				opconstraint.setLeftOperand(Left);
				opconstraint.setRightOperand(Right);	
				LConstraint=op=RConstraint="";
			}
		else if(opexp.getParameterExps().get(0).toString().matches("[0-9]+"))
		    {
			
			RConstraint= opexp.getParameterExps().get(0).toString();
			Right = new CLGLiteralNode(RConstraint);
			opconstraint.setLeftOperand(Left);
			opconstraint.setRightOperand(Right);	
			LConstraint=op=RConstraint="";
		    }
		else{
	
			RConstraint = opexp.getParameterExps().get(0).toString();
			Right = new CLGVariableNode(RConstraint);
			opconstraint.setLeftOperand(Left);
			opconstraint.setRightOperand(Right);	
			LConstraint=op=RConstraint="";
		}
		
	}	
		
		return opconstraint;
	}

	
	@Override
	public CLGConstraint CLGConstraint() {
	
		CLGConstraint getConditionExp=this.getConditionExp().CLGConstraint();
		CLGConstraint getThenExp=this.getThenExp().CLGConstraint();
		CLGConstraint getElseExp=this.getElseExp().CLGConstraint();
	
	return null;
	}



}
