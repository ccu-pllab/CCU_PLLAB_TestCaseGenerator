package ccu.pllab.tcgen.AbstractCLG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.security.auth.kerberos.KerberosKey;

import org.antlr.v4.parse.ANTLRParser.parserRule_return;
import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;

import ccu.pllab.tcgen.AbstractConstraint.CLGCollectionNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGLiteralNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGMethodInvocationNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGVariableNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGIfNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGIterateNode;
import ccu.pllab.tcgen.ast.OperationCallExp;
import ccu.pllab.tcgen.clg2path.CriterionFactory;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;


public class CLGCriterionTransformer extends CLGGraph {
	public ArrayList<CLGConstraint> constraint;
	public ArrayList<CLGConstraint> constraintCollection;
	public ArrayList<CLGConstraint> tempconstraint;
	public ArrayList<CLGConstraint> tempconstraintCollection;
	public ArrayList<CLGConstraint> tempconstraintIterate;
	public ArrayList<CLGConstraint> cons;
	public ArrayList<CLGGraph> ifclg;
	private boolean tempConsize = true;
	private boolean tempConsizeCollection = true;
	public ArrayList<CLGConstraint> constraintIterate;
	private boolean tempConsizeIterate = true;
private boolean nodemon=false;
	public CLGCriterionTransformer() {
		super();
	}

	public CLGGraph CriterionTransformer(CLGGraph clg, Criterion criterion) {
		String[] clgindex = clg.getConstraintCollection().keySet().toString().substring(1, clg.getConstraintCollection().keySet().toString().length() - 1).split(", ");
		if (criterion.equals(CriterionFactory.Criterion.dcc) || criterion.equals(CriterionFactory.Criterion.dccdup)) {
			System.out.println("<=========dcc criterion=========>");
			CLGNode successor = null;
			CLGNode Predecessor = null;
			int size = clg.getConstraintCollection().size();
			for (int i = 0; i < size; i++) {
				int a = Integer.parseInt(clgindex[i]);
				if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGMethodInvocationNode) {
					// System.out.println("Constraint is CLGMethodInvocationNode!!");
				} else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIfNode) {
					CLGIfNode x = (CLGIfNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint condition = x.getCondition();
					CLGConstraint notcondition = this.Demongan(condition);
					CLGGraph notleft = null;
					this.constraint = new ArrayList<CLGConstraint>();
					if(!(condition instanceof CLGIterateNode))
					findChildNodeIsIf(notcondition);

					CLGGraph left = parseConstraint(condition, criterion);

					if (this.tempConsize) {
						this.tempconstraint = this.constraint;
						notleft = parseConstraint(notcondition, criterion);
						CLGGraph notleft2 = parseConstraint(this.Demongan(notcondition), criterion);
						for (int tempsize = 0; tempsize < tempconstraint.size(); tempsize++) {
							if (tempconstraint.get(tempsize) instanceof CLGIfNode)
								notleft2.graphAnd(parseConstraint(this.Demonganif((CLGIfNode) tempconstraint.get(tempsize)), criterion));
						}
						if (tempconstraint.size() > 0)
							notleft.graphOr(notleft2);
					} else {
						notleft = parseConstraint(notcondition, criterion);
					}

					CLGConstraint then = x.getThen();
					CLGGraph middle = parseConstraint(then, criterion);
					left.graphAnd(middle);
					if (x.getElse() != null) {
						CLGConstraint elseExp = x.getElse();
						CLGGraph right = parseConstraint(elseExp, criterion);
						notleft.graphAnd(right);
						// left.graphOr(notleft);
					}
					left.graphOr(notleft);
					left.getEndNode().getPredecessor().get(0).removeSuccessor(left.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(left.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(left.getEndNode().getPredecessor().get(0));

					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, left);
				}

				else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIterateNode) {
					CLGIterateNode x = (CLGIterateNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint conditionCon = x.getCondition();
					CLGGraph condition = new CLGGraph(conditionCon);
					CLGConstraint bodyCon = x.getBody();
					CLGGraph body = parseConstraint(bodyCon, criterion);
					CLGConstraint IncrementCon = x.getIncrement();
					CLGGraph increment = new CLGGraph(IncrementCon);
					condition.graphAnd(body);
					condition.graphAnd(increment);
					condition.graphClosure();
					condition.graphAnd(new CLGGraph(this.Demongan(conditionCon)));
					CLGConstraint initialCon = x.getInitial();
					CLGGraph initial = parseConstraint(initialCon, criterion);
					initial.graphAnd(condition);

					initial.getEndNode().getPredecessor().get(0).removeSuccessor(initial.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(initial.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(initial.getEndNode().getPredecessor().get(0));
					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, initial);
					// CLGGraph temp=parseConstraint(clg.getConstraintNodeById(a).getConstraint(),
					// criterion);
					// CLGStartNode start=(CLGStartNode) clg.getStartNode();
				} else {
					CLGOperatorNode x = (CLGOperatorNode) clg.getConstraintNodeById(a).getConstraint();

					/********************
					 * and operator 處理
					 ******************************************/
					if (x.getOperator().equals("&&") || x.getOperator().equals("and"))// and是新增的
					{

						CLGConstraint Left = x.getLeftOperand();
						CLGGraph L = parseConstraint(Left, criterion);
						CLGConstraint Right = x.getRightOperand();
						CLGGraph R = parseConstraint(Right, criterion);
						L.graphAnd(R);

						// 連接圖片
						L.getEndNode().getPredecessor().get(0).removeSuccessor(L.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(L.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(L.getEndNode().getPredecessor().get(0));

						clg.getConstraintCollection().remove(a);
						this.AddConstraintNode(clg, L);

					}
					/****************************************************************/
					/******************** or operator 處理 ******************************************/
					else if (x.getOperator().equals("||") || x.getOperator().equals("or"))// and是新增的
					{

						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						A.graphAnd(notB);

						notA.graphAnd(B);

						A.graphOr(notA);

						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

						clg.getConstraintCollection().remove(a);
						this.AddConstraintNode(clg, A);

					}
					/****************************************************************/
					/********************
					 * xor operator 處理
					 ******************************************/
					else if (x.getOperator().equals("xor")) {

						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						A.graphAnd(notB);

						B.graphAnd(notA);

						A.graphOr(B);

						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

					}
					/****************************************************************/
					/********************
					 * implies operator 處理
					 ******************************************/
					else if (x.getOperator().equals("implies")) {

						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						notA.graphAnd(notB);

						A.graphAnd(B);

						notA.graphOr(A);

						// 連接圖片
						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));
					} else if (x.getOperator().equals("=") && x.getRightOperand() instanceof CLGIterateNode) {
						CLGGraph closure = parseConstraint(x.getRightOperand(), criterion);
						x.setRightOperand(new CLGVariableNode("acc_pre", "Integer"));
						CLGGraph newClg = new CLGGraph(x);
						closure.graphAnd(newClg);
						closure.getEndNode().getPredecessor().get(0).removeSuccessor(closure.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(closure.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(closure.getEndNode().getPredecessor().get(0));
					}
					/****************************************************************/
				}
			}
		}
		// MCC//////////////////////////////////////////////////////////////////////////////////////////////////////
		else if (criterion.equals(CriterionFactory.Criterion.mcc) || criterion.equals(CriterionFactory.Criterion.mccdup)) {
			System.out.println("<=========mcc criterion=========>");
			CLGNode successor = null;
			CLGNode Predecessor = null;

			int size = clg.getConstraintCollection().size();

			for (int i = 0; i < size; i++) {

				int a = Integer.parseInt(clgindex[i]);
				if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGMethodInvocationNode) {
					// System.out.println("Constraint is CLGMethodInvocationNode!!");
				} else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIterateNode) {
					CLGIterateNode x = (CLGIterateNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint conditionCon = x.getCondition();
					CLGGraph condition = new CLGGraph(conditionCon);
					CLGConstraint bodyCon = x.getBody();
					CLGGraph body = parseConstraint(bodyCon, criterion);
					CLGConstraint IncrementCon = x.getIncrement();
					CLGGraph increment = new CLGGraph(IncrementCon);
					condition.graphAnd(body);
					condition.graphAnd(increment);
					condition.graphClosure();
					condition.graphAnd(new CLGGraph(this.Demongan(conditionCon)));
					CLGConstraint initialCon = x.getInitial();
					CLGGraph initial = parseConstraint(initialCon, criterion);
					initial.graphAnd(condition);

					initial.getEndNode().getPredecessor().get(0).removeSuccessor(initial.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(initial.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(initial.getEndNode().getPredecessor().get(0));
					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, initial);
				} else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIfNode) {
					CLGIfNode x = (CLGIfNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint condition = x.getCondition();
					CLGConstraint notcondition = this.Demongan(condition);
					this.constraint = new ArrayList<CLGConstraint>();
					if(!(condition instanceof CLGIterateNode))
					findChildNodeIsIf(notcondition);

					CLGGraph left = parseConstraint(condition, criterion);
					CLGGraph notleft = null;
					if (this.tempConsize) {
						this.tempconstraint = this.constraint;
						notleft = parseConstraint(notcondition, criterion);
						CLGGraph notleft2 = parseConstraint(this.Demongan(notcondition), criterion);
						for (int tempsize = 0; tempsize < tempconstraint.size(); tempsize++) {
							if (tempconstraint.get(tempsize) instanceof CLGIfNode)
								notleft2.graphAnd(parseConstraint(this.Demonganif((CLGIfNode) tempconstraint.get(tempsize)), criterion));
						}
						if (tempconstraint.size() > 0)
							notleft.graphOr(notleft2);
					} else
						notleft = parseConstraint(notcondition, criterion);
					// CLGGraph notleft= parseConstraint(notcondition,criterion);
					CLGConstraint then = x.getThen();
					CLGGraph middle = parseConstraint(then, criterion);
					if (x.getElse() != null) {
						CLGConstraint elseExp = x.getElse();
						CLGGraph right = parseConstraint(elseExp, criterion);
						notleft.graphAnd(right);
					}
					left.graphAnd(middle);

					left.graphOr(notleft);

					left.getEndNode().getPredecessor().get(0).removeSuccessor(left.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(left.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(left.getEndNode().getPredecessor().get(0));

					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, left);
				} else {
					CLGOperatorNode x = (CLGOperatorNode) clg.getConstraintNodeById(a).getConstraint();

					/********************
					 * and operator 處理
					 ******************************************/
					if (x.getOperator().equals("&&") || x.getOperator().equals("and"))// 改!
					{

						CLGConstraint Left = x.getLeftOperand();
						CLGGraph L = parseConstraint(Left, criterion);
						CLGConstraint Right = x.getRightOperand();
						CLGGraph R = parseConstraint(Right, criterion);
						L.graphAnd(R);

						// 連接圖片
						L.getEndNode().getPredecessor().get(0).removeSuccessor(L.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(L.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(L.getEndNode().getPredecessor().get(0));
						clg.getConstraintCollection().remove(a);
						this.AddConstraintNode(clg, L);

					}
					/****************************************************************/
					/******************** or operator 處理 ******************************************/
					else if (x.getOperator().equals("||") || x.getOperator().equals("or")) {
						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph mid = parseConstraint(left, criterion);

						CLGGraph mid2 = parseConstraint(right, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						mid.graphAnd(mid2);

						A.graphAnd(notB);

						notA.graphAnd(B);

						A.graphOr(notA);

						A.graphOr(mid);

						// 連接圖片
						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

						clg.getConstraintCollection().remove(a);
						this.AddConstraintNode(clg, A);

					}
					/****************************************************************/
					/********************
					 * xor operator 處理
					 ******************************************/
					else if (x.getOperator().equals("xor")) {
						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						A.graphAnd(notB);

						B.graphAnd(notA);

						A.graphOr(B);

						// 連接圖片
						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

					}
					/****************************************************************/
					/********************
					 * implies operator 處理
					 ******************************************/
					else if (x.getOperator().equals("implies")) {
						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						notA.graphAnd(notB);

						A.graphAnd(B);

						notA.graphOr(A);

						// 連接圖片
						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

					}
					/****************************************************************/

					else if (x.getOperator().equals("=") && x.getRightOperand() instanceof CLGIterateNode) {
						CLGGraph closure = parseConstraint(x.getRightOperand(), criterion);
						x.setRightOperand(new CLGVariableNode("acc_pre", "Integer"));
						CLGGraph newClg = new CLGGraph(x);
						closure.graphAnd(newClg);
						closure.getEndNode().getPredecessor().get(0).removeSuccessor(closure.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(closure.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(closure.getEndNode().getPredecessor().get(0));
					}
				}

			}

		} else {
			int size = clg.getConstraintCollection().size();
			System.out.println("<=========dc criterion=========>");
			CLGNode successor = null;
			CLGNode Predecessor = null;
			for (int i = 0; i < size; i++) {
				boolean noDeomongan=false;
				int a = Integer.parseInt(clgindex[i]);
				if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIfNode) {
					CLGIfNode x = (CLGIfNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint condition = x.getCondition();
					CLGConstraint notcondition = this.Demongan(condition);
					this.constraint = new ArrayList<CLGConstraint>();
					if(!(condition instanceof CLGIterateNode))
					findChildNodeIsIf(notcondition);
					CLGGraph left = null;
					CLGGraph notleft = null;

					if (this.tempConsize) {
						this.tempconstraint = this.constraint;
						notleft = parseConstraint(notcondition, criterion);
						CLGGraph notleft2 = parseConstraint(this.Demongan(notcondition), criterion);
						left = parseConstraint(this.Demongan(notcondition), criterion);
						for (int tempsize = 0; tempsize < tempconstraint.size(); tempsize++) {
							if (tempconstraint.get(tempsize) instanceof CLGIfNode)
								notleft2.graphAnd(parseConstraint(this.Demonganif((CLGIfNode) tempconstraint.get(tempsize)), criterion));
							else
								notleft2.graphAnd(parseConstraint(tempconstraint.get(tempsize), criterion));
							left.graphAnd(parseConstraint(tempconstraint.get(tempsize), criterion));
						}
						if (tempconstraint.size() > 0)
							notleft.graphOr(notleft2);
					} else {
						left = parseConstraint(condition, criterion);
						this.nodemon=true;
						notleft = parseConstraint(notcondition, criterion);
						this.nodemon=false;
					}

					if (x.getElse() != null) {
						CLGConstraint elseExp = x.getElse();
						CLGGraph right = parseConstraint(elseExp, criterion);
						notleft.graphAnd(right);		
					}
					CLGConstraint then = x.getThen();
					CLGGraph middle = parseConstraint(then, criterion);
					left.graphAnd(middle);
					left.graphOr(notleft);

					left.getEndNode().getPredecessor().get(0).removeSuccessor(left.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(left.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(left.getEndNode().getPredecessor().get(0));

					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, left);
				} else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIterateNode) {
					CLGIterateNode x = (CLGIterateNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint conditionCon = x.getCondition();
					CLGGraph condition = new CLGGraph(conditionCon);
					CLGConstraint bodyCon = x.getBody();
					CLGGraph body = parseConstraint(bodyCon, criterion);
					CLGConstraint IncrementCon = x.getIncrement();
					CLGGraph increment = new CLGGraph(IncrementCon);
					condition.graphAnd(body);
					condition.graphAnd(increment);
					condition.graphClosure();
					condition.graphAnd(new CLGGraph(this.Demongan(conditionCon)));
					CLGConstraint initialCon = x.getInitial();
					CLGGraph initial = parseConstraint(initialCon, CriterionFactory.Criterion.dcc);
					initial.graphAnd(condition);

					initial.getEndNode().getPredecessor().get(0).removeSuccessor(initial.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(initial.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(initial.getEndNode().getPredecessor().get(0));
					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, initial);
				} else if(clg.getConstraintNodeById(a).getConstraint() instanceof CLGMethodInvocationNode)
				{
					
				}
				else {
					CLGOperatorNode x = (CLGOperatorNode) clg.getConstraintNodeById(a).getConstraint();
					CLGGraph clg_pre = clg;
					this.constraint = new ArrayList<CLGConstraint>();
					ArrayList<CLGGraph> subclg = new ArrayList<CLGGraph>();
					if (x.getOperator().equals("&&") || x.getOperator().equals("||")) {
						if (x.getLeftOperand() instanceof CLGIfNode ) {
							this.constraint.add(x.getLeftOperand());
							findChildNodeIsIf(x.getRightOperand());
						}else if((x.getLeftOperand() instanceof CLGIterateNode ))
						{
							this.constraintIterate=new ArrayList<CLGConstraint>();
							this.constraintIterate.add( x.getLeftOperand());
							findChildNodeIsIterate((CLGOperatorNode) x.getRightOperand());
							this.constraint.addAll(this.constraintIterate);
						}
						else if(x.getLeftOperand() instanceof CLGMethodInvocationNode)
						{
							this.constraintIterate = new ArrayList<CLGConstraint>();
							this.constraint.add(x.getLeftOperand());
							findChildNodeIsIterate((CLGOperatorNode) x.getRightOperand());
							this.constraint.addAll(this.constraintIterate);
						}
						else {
							findChildNodeIsIf(x);
						}

						for (CLGConstraint cons : this.constraint) {
							if (cons instanceof CLGIfNode) {
								subclg.add(this.parseConstraint(cons, criterion));
							} else if (cons instanceof CLGIterateNode) {
								subclg.add(this.parseConstraint(cons, criterion));
							} else
								subclg.add(new CLGGraph(cons));
						}

						for (int clgsize = subclg.size(); clgsize >= 2; clgsize--) {
							CLGGraph right = subclg.get(clgsize - 1);
							CLGGraph left = subclg.get(clgsize - 2);
							subclg.remove(clgsize - 1);
							subclg.remove(clgsize - 2);
							left.graphAnd(right);
							subclg.add(left);
						}
						CLGNode start = clg_pre.getStartNode();
						if (subclg.size() > 0) {
							((CLGStartNode) subclg.get(0).getStartNode()).setClassName(((CLGStartNode) start).getClassName());
							((CLGStartNode) subclg.get(0).getStartNode()).setMethodName(((CLGStartNode) start).getMethodName());
							((CLGStartNode) subclg.get(0).getStartNode()).setRetType(((CLGStartNode) start).getReturnType());
							((CLGStartNode) subclg.get(0).getStartNode()).setIsConstructor(((CLGStartNode) start).isConstructor());
							((CLGStartNode) subclg.get(0).getStartNode()).setClassAttributes(((CLGStartNode) start).getClassAttributes());
							((CLGStartNode) subclg.get(0).getStartNode()).setMethodParameters(((CLGStartNode) start).getMethodParameters());
							((CLGStartNode) subclg.get(0).getStartNode()).setMethodParameterTypes(((CLGStartNode) start).getMethodParameterTypes());
							clg = subclg.get(0);
						}
					} else if (x.getOperator().equals("=") && x.getRightOperand() instanceof CLGIterateNode) {
						CLGGraph closure = parseConstraint(x.getRightOperand(), criterion);
						x.setRightOperand(new CLGVariableNode("acc_pre", "Integer"));
						CLGGraph newClg = new CLGGraph(x);
						closure.graphAnd(newClg);
						closure.getEndNode().getPredecessor().get(0).removeSuccessor(closure.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(closure.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(closure.getEndNode().getPredecessor().get(0));
					}
				}
			}
		}
		/*****************************************
		 * 限制節點與邊數量統計***** System.out.println("constraint node " +
		 * clg.getConstraintCollection().size()); System.out.println("edge
		 * "+clg.getAllBranches().size() );
		 ***********/

		return clg;
	}

	public CLGGraph CriterionTransformer(CLGGraph clg, String criterion) {/// 仿
		String[] clgindex = clg.getConstraintCollection().keySet().toString().substring(1, clg.getConstraintCollection().keySet().toString().length() - 1).split(", ");
		if (criterion.equals("dcc") || criterion.equals("dccdup")) {
			System.out.println("<=========dcc criterion=========>");
			CLGNode successor = null;
			CLGNode Predecessor = null;
			int size = clg.getConstraintCollection().size();
			for (int i = 0; i < size; i++) {
				int a = Integer.parseInt(clgindex[i]);
				if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGMethodInvocationNode) {
					// System.out.println("Constraint is CLGMethodInvocationNode!!");
					
				} else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIfNode) {
					CLGIfNode x = (CLGIfNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint condition = x.getCondition();
					CLGConstraint notcondition = this.Demongan(condition);
					CLGGraph notleft = null;
					this.constraint = new ArrayList<CLGConstraint>();
					findChildNodeIsIf(notcondition);

					CLGGraph left = parseConstraint(condition, criterion);

					if (this.tempConsize) {
						this.tempconstraint = this.constraint;
						notleft = parseConstraint(notcondition, criterion);
						CLGGraph notleft2 = parseConstraint(this.Demongan(notcondition), criterion);
						for (int tempsize = 0; tempsize < tempconstraint.size(); tempsize++) {
							if (tempconstraint.get(tempsize) instanceof CLGIfNode)
								notleft2.graphAnd(parseConstraint(this.Demonganif((CLGIfNode) tempconstraint.get(tempsize)), criterion));
						}
						if (tempconstraint.size() > 0)
							notleft.graphOr(notleft2);
					} else {
						notleft = parseConstraint(notcondition, criterion);
					}

					CLGConstraint then = x.getThen();
					CLGGraph middle = parseConstraint(then, criterion);
					left.graphAnd(middle);
					if (x.getElse() != null) {
						CLGConstraint elseExp = x.getElse();
						CLGGraph right = parseConstraint(elseExp, criterion);
						notleft.graphAnd(right);
						// left.graphOr(notleft);
					}
					left.graphOr(notleft);
					left.getEndNode().getPredecessor().get(0).removeSuccessor(left.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(left.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(left.getEndNode().getPredecessor().get(0));

					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, left);
				}

				else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIterateNode) {
					CLGIterateNode x = (CLGIterateNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint conditionCon = x.getCondition();
					CLGGraph condition = new CLGGraph(conditionCon);
					CLGConstraint bodyCon = x.getBody();
					CLGGraph body = parseConstraint(bodyCon, criterion);
					CLGConstraint IncrementCon = x.getIncrement();
					CLGGraph increment = new CLGGraph(IncrementCon);
					condition.graphAnd(body);
					condition.graphAnd(increment);
					condition.graphClosure();
					condition.graphAnd(new CLGGraph(this.Demongan(conditionCon)));
					CLGConstraint initialCon = x.getInitial();
					CLGGraph initial = parseConstraint(initialCon, criterion);
					initial.graphAnd(condition);

					initial.getEndNode().getPredecessor().get(0).removeSuccessor(initial.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(initial.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(initial.getEndNode().getPredecessor().get(0));
					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, initial);
					// CLGGraph temp=parseConstraint(clg.getConstraintNodeById(a).getConstraint(),
					// criterion);
					// CLGStartNode start=(CLGStartNode) clg.getStartNode();
				} else {
					CLGOperatorNode x = (CLGOperatorNode) clg.getConstraintNodeById(a).getConstraint();

					/********************
					 * and operator 處理
					 ******************************************/
					if (x.getOperator().equals("&&") || x.getOperator().equals("and"))// and是新增的
					{

						CLGConstraint Left = x.getLeftOperand();
						CLGGraph L = parseConstraint(Left, criterion);
						CLGConstraint Right = x.getRightOperand();
						CLGGraph R = parseConstraint(Right, criterion);
						L.graphAnd(R);

						// 連接圖片
						L.getEndNode().getPredecessor().get(0).removeSuccessor(L.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(L.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(L.getEndNode().getPredecessor().get(0));

						clg.getConstraintCollection().remove(a);
						this.AddConstraintNode(clg, L);

					}
					/****************************************************************/
					/******************** or operator 處理 ******************************************/
					else if (x.getOperator().equals("||") || x.getOperator().equals("or"))// and是新增的
					{

						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						A.graphAnd(notB);

						notA.graphAnd(B);

						A.graphOr(notA);

						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

						clg.getConstraintCollection().remove(a);
						this.AddConstraintNode(clg, A);

					}
					/****************************************************************/
					/********************
					 * xor operator 處理
					 ******************************************/
					else if (x.getOperator().equals("xor")) {

						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						A.graphAnd(notB);

						B.graphAnd(notA);

						A.graphOr(B);

						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

					}
					/****************************************************************/
					/********************
					 * implies operator 處理
					 ******************************************/
					else if (x.getOperator().equals("implies")) {

						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						notA.graphAnd(notB);

						A.graphAnd(B);

						notA.graphOr(A);

						// 連接圖片
						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));
					} else if (x.getOperator().equals("=") && x.getRightOperand() instanceof CLGIterateNode) {
						CLGGraph closure = parseConstraint(x.getRightOperand(), criterion);
						x.setRightOperand(new CLGVariableNode("acc_pre", "Integer"));
						CLGGraph newClg = new CLGGraph(x);
						closure.graphAnd(newClg);
						closure.getEndNode().getPredecessor().get(0).removeSuccessor(closure.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(closure.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(closure.getEndNode().getPredecessor().get(0));
					}
					/****************************************************************/
				}
			}
		}
		// MCC//////////////////////////////////////////////////////////////////////////////////////////////////////
		else if (criterion.equals("mcc") || criterion.equals("mccdup")) {
			System.out.println("<=========mcc criterion=========>");
			CLGNode successor = null;
			CLGNode Predecessor = null;

			int size = clg.getConstraintCollection().size();

			for (int i = 0; i < size; i++) {

				int a = Integer.parseInt(clgindex[i]);
				if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGMethodInvocationNode) {
					// System.out.println("Constraint is CLGMethodInvocationNode!!");
				} else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIterateNode) {
					CLGIterateNode x = (CLGIterateNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint conditionCon = x.getCondition();
					CLGGraph condition = new CLGGraph(conditionCon);
					CLGConstraint bodyCon = x.getBody();
					CLGGraph body = parseConstraint(bodyCon, criterion);
					CLGConstraint IncrementCon = x.getIncrement();
					CLGGraph increment = new CLGGraph(IncrementCon);
					condition.graphAnd(body);
					condition.graphAnd(increment);
					condition.graphClosure();
					condition.graphAnd(new CLGGraph(this.Demongan(conditionCon)));
					CLGConstraint initialCon = x.getInitial();
					CLGGraph initial = parseConstraint(initialCon, criterion);
					initial.graphAnd(condition);

					initial.getEndNode().getPredecessor().get(0).removeSuccessor(initial.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(initial.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(initial.getEndNode().getPredecessor().get(0));
					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, initial);
				} else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIfNode) {
					CLGIfNode x = (CLGIfNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint condition = x.getCondition();
					CLGConstraint notcondition = this.Demongan(condition);
					this.constraint = new ArrayList<CLGConstraint>();
					findChildNodeIsIf(notcondition);

					CLGGraph left = parseConstraint(condition, criterion);
					CLGGraph notleft = null;
					if (this.tempConsize) {
						this.tempconstraint = this.constraint;
						notleft = parseConstraint(notcondition, criterion);
						CLGGraph notleft2 = parseConstraint(this.Demongan(notcondition), criterion);
						for (int tempsize = 0; tempsize < tempconstraint.size(); tempsize++) {
							if (tempconstraint.get(tempsize) instanceof CLGIfNode)
								notleft2.graphAnd(parseConstraint(this.Demonganif((CLGIfNode) tempconstraint.get(tempsize)), criterion));
						}
						if (tempconstraint.size() > 0)
							notleft.graphOr(notleft2);
					} else
						notleft = parseConstraint(notcondition, criterion);
					// CLGGraph notleft= parseConstraint(notcondition,criterion);
					CLGConstraint then = x.getThen();
					CLGGraph middle = parseConstraint(then, criterion);
					if (x.getElse() != null) {
						CLGConstraint elseExp = x.getElse();
						CLGGraph right = parseConstraint(elseExp, criterion);
						notleft.graphAnd(right);
					}
					left.graphAnd(middle);

					left.graphOr(notleft);

					left.getEndNode().getPredecessor().get(0).removeSuccessor(left.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(left.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(left.getEndNode().getPredecessor().get(0));

					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, left);
				} else {
					CLGOperatorNode x = (CLGOperatorNode) clg.getConstraintNodeById(a).getConstraint();

					/********************
					 * and operator 處理
					 ******************************************/
					if (x.getOperator().equals("&&") || x.getOperator().equals("and"))// 改!
					{

						CLGConstraint Left = x.getLeftOperand();
						CLGGraph L = parseConstraint(Left, criterion);
						CLGConstraint Right = x.getRightOperand();
						CLGGraph R = parseConstraint(Right, criterion);
						L.graphAnd(R);

						// 連接圖片
						L.getEndNode().getPredecessor().get(0).removeSuccessor(L.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(L.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(L.getEndNode().getPredecessor().get(0));
						clg.getConstraintCollection().remove(a);
						this.AddConstraintNode(clg, L);

					}
					/****************************************************************/
					/******************** or operator 處理 ******************************************/
					else if (x.getOperator().equals("||") || x.getOperator().equals("or")) {
						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph mid = parseConstraint(left, criterion);

						CLGGraph mid2 = parseConstraint(right, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						mid.graphAnd(mid2);

						A.graphAnd(notB);

						notA.graphAnd(B);

						A.graphOr(notA);

						A.graphOr(mid);

						// 連接圖片
						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

						clg.getConstraintCollection().remove(a);
						this.AddConstraintNode(clg, A);

					}
					/****************************************************************/
					/********************
					 * xor operator 處理
					 ******************************************/
					else if (x.getOperator().equals("xor")) {
						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						A.graphAnd(notB);

						B.graphAnd(notA);

						A.graphOr(B);

						// 連接圖片
						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

					}
					/****************************************************************/
					/********************
					 * implies operator 處理
					 ******************************************/
					else if (x.getOperator().equals("implies")) {
						CLGConstraint left = x.getLeftOperand();

						CLGConstraint notleft = this.Demongan((CLGOperatorNode) x.getLeftOperand());
						;

						CLGConstraint right = x.getRightOperand();

						CLGConstraint notright = this.Demongan((CLGOperatorNode) x.getRightOperand());

						CLGGraph A = parseConstraint(left, criterion);

						CLGGraph notA = parseConstraint(notleft, criterion);

						CLGGraph B = parseConstraint(right, criterion);

						CLGGraph notB = parseConstraint(notright, criterion);

						notA.graphAnd(notB);

						A.graphAnd(B);

						notA.graphOr(A);

						// 連接圖片
						A.getEndNode().getPredecessor().get(0).removeSuccessor(A.getEndNode());

						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(A.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(A.getEndNode().getPredecessor().get(0));

					}
					/****************************************************************/

					else if (x.getOperator().equals("=") && x.getRightOperand() instanceof CLGIterateNode) {
						CLGGraph closure = parseConstraint(x.getRightOperand(), criterion);
						x.setRightOperand(new CLGVariableNode("acc_pre", "Integer"));
						CLGGraph newClg = new CLGGraph(x);
						closure.graphAnd(newClg);
						closure.getEndNode().getPredecessor().get(0).removeSuccessor(closure.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(closure.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(closure.getEndNode().getPredecessor().get(0));
					}
				}

			}

		} else {
			int size = clg.getConstraintCollection().size();
			System.out.println("<=========dc criterion=========>");
			CLGNode successor = null;
			CLGNode Predecessor = null;
			
			for (int i = 0; i < size; i++) {
				int a = Integer.parseInt(clgindex[i]);
				if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIfNode) {
					CLGIfNode x = (CLGIfNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint condition = x.getCondition();
					
					CLGConstraint notcondition = this.Demongan(condition);
					this.constraint = new ArrayList<CLGConstraint>();
					findChildNodeIsIf(notcondition);
					CLGGraph left = null;
					CLGGraph notleft = null;

					if (this.tempConsize) {
						this.tempconstraint = this.constraint;
						notleft = parseConstraint(notcondition, criterion);
						CLGGraph notleft2 = parseConstraint(this.Demongan(notcondition), criterion);
						if(condition instanceof CLGIterateNode)
							left = parseConstraint(condition, criterion);
						else
						left = parseConstraint(this.Demongan(notcondition), criterion);
						for (int tempsize = 0; tempsize < tempconstraint.size(); tempsize++) {
							if (tempconstraint.get(tempsize) instanceof CLGIfNode)
								notleft2.graphAnd(parseConstraint(this.Demonganif((CLGIfNode) tempconstraint.get(tempsize)), criterion));
							else
								notleft2.graphAnd(parseConstraint(tempconstraint.get(tempsize), criterion));
							left.graphAnd(parseConstraint(tempconstraint.get(tempsize), criterion));
						}
						if (tempconstraint.size() > 0)
							notleft.graphOr(notleft2);
					} else {
						left = parseConstraint(condition, criterion);
						
						notleft = parseConstraint(notcondition, criterion);
					}

					if (x.getElse() != null) {
						CLGConstraint elseExp = x.getElse();
						CLGGraph right = parseConstraint(elseExp, criterion);
						notleft.graphAnd(right);
					}
					CLGConstraint then = x.getThen();
					CLGGraph middle = parseConstraint(then, criterion);
					left.graphAnd(middle);
					left.graphOr(notleft);

					left.getEndNode().getPredecessor().get(0).removeSuccessor(left.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(left.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(left.getEndNode().getPredecessor().get(0));

					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, left);
				} else if (clg.getConstraintNodeById(a).getConstraint() instanceof CLGIterateNode) {
					CLGIterateNode x = (CLGIterateNode) clg.getConstraintNodeById(a).getConstraint();
					CLGConstraint conditionCon = x.getCondition();
					CLGGraph condition = new CLGGraph(conditionCon);
					CLGConstraint bodyCon = x.getBody();
					
					CLGGraph body = parseConstraint(bodyCon, criterion);
					CLGConstraint IncrementCon = x.getIncrement();
					CLGGraph increment = new CLGGraph(IncrementCon);
					condition.graphAnd(body);
					condition.graphAnd(increment);
					condition.graphClosure();
					condition.graphAnd(new CLGGraph(this.Demongan(conditionCon)));
					CLGConstraint initialCon = x.getInitial();
					CLGGraph initial = parseConstraint(initialCon, "dcc");
					initial.graphAnd(condition);

					initial.getEndNode().getPredecessor().get(0).removeSuccessor(initial.getEndNode());

					successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
					Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
					successor.removePredecessor(clg.getConstraintNodeById(a));
					Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
					Predecessor.addSuccessor(initial.getStartNode().getSuccessor().get(0));
					successor.addPredecessor(initial.getEndNode().getPredecessor().get(0));
					clg.getConstraintCollection().remove(a);
					this.AddConstraintNode(clg, initial);
				} else {
					
					CLGOperatorNode x = (CLGOperatorNode) clg.getConstraintNodeById(a).getConstraint();
					CLGGraph clg_pre = clg;
					this.constraint = new ArrayList<CLGConstraint>();
					ArrayList<CLGGraph> subclg = new ArrayList<CLGGraph>();
					if (x.getOperator().equals("&&") || x.getOperator().equals("||")) {

						if (x.getLeftOperand() instanceof CLGIfNode) {
							this.constraint.add(x.getLeftOperand());
							findChildNodeIsIf(x.getRightOperand());
						} 
						else if(x.getLeftOperand() instanceof CLGIterateNode) {
							this.constraintIterate = new ArrayList<CLGConstraint>();
							this.constraint.add(x.getLeftOperand());
							findChildNodeIsIterate(x.getRightOperand());
						}
						else {
							findChildNodeIsIf(x);
							findChildNodeIsIterate(x);
						}

						for (CLGConstraint cons : this.constraint) {
							if (cons instanceof CLGIfNode) {
								subclg.add(this.parseConstraint(cons, criterion));
							} else if (cons instanceof CLGIterateNode) {
								subclg.add(this.parseConstraint(cons, criterion));
							} else
								subclg.add(new CLGGraph(cons));
						}

						for (int clgsize = subclg.size(); clgsize >= 2; clgsize--) {
							CLGGraph right = subclg.get(clgsize - 1);
							CLGGraph left = subclg.get(clgsize - 2);
							subclg.remove(clgsize - 1);
							subclg.remove(clgsize - 2);
							left.graphAnd(right);
							subclg.add(left);
						}
						CLGNode start = clg_pre.getStartNode();
						if (subclg.size() > 0) {
							((CLGStartNode) subclg.get(0).getStartNode()).setClassName(((CLGStartNode) start).getClassName());
							((CLGStartNode) subclg.get(0).getStartNode()).setMethodName(((CLGStartNode) start).getMethodName());
							((CLGStartNode) subclg.get(0).getStartNode()).setRetType(((CLGStartNode) start).getReturnType());
							((CLGStartNode) subclg.get(0).getStartNode()).setIsConstructor(((CLGStartNode) start).isConstructor());
							((CLGStartNode) subclg.get(0).getStartNode()).setClassAttributes(((CLGStartNode) start).getClassAttributes());
							((CLGStartNode) subclg.get(0).getStartNode()).setMethodParameters(((CLGStartNode) start).getMethodParameters());
							((CLGStartNode) subclg.get(0).getStartNode()).setMethodParameterTypes(((CLGStartNode) start).getMethodParameterTypes());
							clg = subclg.get(0);
						}
					} else if (x.getOperator().equals("=") && x.getRightOperand() instanceof CLGIterateNode) {
						CLGGraph closure = parseConstraint(x.getRightOperand(), criterion);
						x.setRightOperand(new CLGVariableNode("acc_pre", "Integer"));
						CLGGraph newClg = new CLGGraph(x);
						closure.graphAnd(newClg);
						closure.getEndNode().getPredecessor().get(0).removeSuccessor(closure.getEndNode());
						successor = clg.getConstraintNodeById(a).getSuccessor().get(0);
						Predecessor = clg.getConstraintNodeById(a).getPredecessor().get(0);
						successor.removePredecessor(clg.getConstraintNodeById(a));
						Predecessor.removeSuccessor(clg.getConstraintNodeById(a));
						Predecessor.addSuccessor(closure.getStartNode().getSuccessor().get(0));
						successor.addPredecessor(closure.getEndNode().getPredecessor().get(0));
					}
				}
			}
		}
		/*****************************************
		 * 限制節點與邊數量統計***** System.out.println("constraint node " +
		 * clg.getConstraintCollection().size()); System.out.println("edge
		 * "+clg.getAllBranches().size() );
		 ***********/

		return clg;
	}

	public void findChildNodeIsIf(CLGConstraint node) {
		// this.constraint=new ArrayList<CLGConstraint>();
		if (node instanceof CLGIfNode)
			this.constraint.add(node);
		else if (((CLGOperatorNode) node).getOperator().equals("&&") || ((CLGOperatorNode) node).getOperator().equals("||")) {
			if (((CLGOperatorNode) node).getLeftOperand() instanceof CLGIfNode) {
				this.constraint.add(((CLGOperatorNode) node).getLeftOperand());
				findChildNodeIsIf(((CLGOperatorNode) node).getRightOperand());
			}
			CLGConstraint search = ((CLGOperatorNode) node).getRightOperand();
			CLGOperatorNode temp = (CLGOperatorNode) node;
			int isIf = 0;
			while (search != null)// 改過
			{
				if (search instanceof CLGIfNode) {
					isIf = 1;
					break;
				} else if (search != null) {
					if (search instanceof CLGOperatorNode) {
						if (((CLGOperatorNode) search).getOperator().equals("&&") || ((CLGOperatorNode) search).getOperator().equals("||") || ((CLGOperatorNode) search).getOperator().equals("and")
								|| ((CLGOperatorNode) search).getOperator().equals("or")) {
							if (((CLGOperatorNode) search).getLeftOperand() instanceof CLGIfNode) {
								isIf = 2;
								break;
							} else {
								temp = (CLGOperatorNode) search;
								search = ((CLGOperatorNode) search).getRightOperand();
							}
						} else
							break;
					} else
						break;
				}
			}
			switch (isIf) {
			case 1:
				temp.setRightOperand(null);
				this.constraint.add(search);
				this.tempConsize = true;
				break;
			case 2:
				temp.setRightOperand(null);
				this.constraint.add(((CLGOperatorNode) search).getLeftOperand());
				((CLGOperatorNode) search).setLeftOperand(null);
				this.constraint.add(((CLGOperatorNode) search).getRightOperand());
				this.tempConsize = true;
				break;
			case 0:
				this.constraint.add(node);
				this.tempConsize = false;
			}
		}
	}

	public void findChildNodeIsIterate(CLGConstraint node) {
		if (node instanceof CLGIterateNode)
			this.constraintIterate.add(node);
		else if (((CLGOperatorNode) node).getOperator().equals("&&") || ((CLGOperatorNode) node).getOperator().equals("||")) {
			if ( ((CLGOperatorNode) node).getLeftOperand() instanceof CLGIterateNode) {
				if(((CLGOperatorNode) node).getLeftOperand()!=null)
				this.constraintIterate.add(((CLGOperatorNode) node).getLeftOperand());
				findChildNodeIsIterate(((CLGOperatorNode) node).getRightOperand());
			}
			else {
			CLGConstraint search = ((CLGOperatorNode) node).getRightOperand();
			CLGOperatorNode temp = (CLGOperatorNode) node;
			int isIterate = 0;
			while (search != null)// 改過
			{
				if (search instanceof CLGIterateNode) {
					isIterate = 1;
					break;
				} else if (search != null) {
					if (search instanceof CLGOperatorNode) {
						if (((CLGOperatorNode) search).getOperator().equals("&&") || ((CLGOperatorNode) search).getOperator().equals("||") || ((CLGOperatorNode) search).getOperator().equals("and")
								|| ((CLGOperatorNode) search).getOperator().equals("or")) {
							if (((CLGOperatorNode) search).getLeftOperand() instanceof CLGIterateNode) {
								isIterate = 2;
								break;
							} else {
								temp = (CLGOperatorNode) search;
								search = ((CLGOperatorNode) search).getRightOperand();
							}
						} else
							break;
					} else
						break;
				}
			}
			switch (isIterate) {
			case 1:
				temp.setRightOperand(null);
			//	this.constraintIterate.add(temp);
				this.constraintIterate.add(search);
				this.tempConsizeIterate = true;
				break;
			case 2:
				temp.setRightOperand(null);
				this.constraintIterate.add(((CLGOperatorNode) search).getLeftOperand());
				((CLGOperatorNode) search).setLeftOperand(null);
				this.constraintIterate.add(((CLGOperatorNode) search).getRightOperand());
				this.tempConsizeIterate = true;
				break;
			case 0:
				this.constraintIterate.add(node);
				this.tempConsizeIterate = false;
			}
		}
		}
		else {
			this.constraintIterate.add(node);
		}
	}

	private CLGGraph parseConstraint(CLGConstraint constraint, Criterion criterion) {

		if (constraint instanceof CLGIfNode) {
			CLGIfNode ifconstraint = (CLGIfNode) constraint;
			CLGConstraint condition = ifconstraint.getCondition();
			CLGGraph left = parseConstraint(condition, criterion);
			CLGConstraint then = ifconstraint.getThen();
			CLGGraph middle = parseConstraint(then, criterion);

			CLGConstraint notcondition = this.Demongan(condition);
			CLGGraph notleft = parseConstraint(notcondition, criterion);
			left.graphAnd(middle);

			if (ifconstraint.getElse() != null) {
				CLGConstraint elseExp = ifconstraint.getElse();
				CLGGraph right = parseConstraint(elseExp, criterion);
				notleft.graphAnd(right);
			}
			left.graphOr(notleft);

			return left;
		}

		else if ((constraint instanceof CLGIterateNode) && criterion.equals(CriterionFactory.Criterion.dc)) {
			CLGIterateNode collconstraint = (CLGIterateNode) constraint;
			CLGGraph initial = new CLGGraph(((CLGOperatorNode) collconstraint.getInitial()).getLeftOperand());
			initial.graphAnd(new CLGGraph(((CLGOperatorNode) collconstraint.getInitial()).getRightOperand()));

			CLGGraph condition = new CLGGraph(collconstraint.getCondition());
			if(collconstraint.getBody() instanceof CLGIterateNode)
				condition.graphAnd(parseConstraint(collconstraint.getBody(), CriterionFactory.Criterion.dcc));
			else
			condition.graphAnd(new CLGGraph(collconstraint.getBody()));
			condition.graphAnd(new CLGGraph(collconstraint.getIncrement()));

			condition.graphClosure();
			condition.graphAnd(new CLGGraph(this.Demongan(collconstraint.getCondition())));
			initial.graphAnd(condition);
			return initial;

		} else if (constraint instanceof CLGIterateNode) {
			CLGIterateNode collconstraint = (CLGIterateNode) constraint;
			CLGGraph condition = parseConstraint(collconstraint.getCondition(), criterion);
			CLGGraph body = parseConstraint(collconstraint.getBody(), criterion);
			CLGGraph increment = parseConstraint(collconstraint.getIncrement(), criterion);
			condition.graphAnd(body);
			condition.graphAnd(increment);
			condition.graphClosure();
			condition.graphAnd(new CLGGraph(this.Demongan(collconstraint.getCondition())));
			// CLGConstraint initialCon=collconstraint.getInitial();
			CLGGraph initial = parseConstraint(collconstraint.getInitial(), criterion);
			initial.graphAnd(condition);
			return initial;
		}

		else if (constraint instanceof CLGMethodInvocationNode) {
			return new CLGGraph(constraint);
		} else if (constraint instanceof CLGVariableNode) {
			return new CLGGraph(constraint);
		} else {
			CLGOperatorNode opconstraint = (CLGOperatorNode) constraint;
			if ((opconstraint.getOperator().equals("&&") || opconstraint.getOperator().equals("and") || opconstraint.getOperator().equals("||") || opconstraint.getOperator().equals("or"))
					&& criterion.equals(CriterionFactory.Criterion.dc)) {

				this.constraint = new ArrayList<CLGConstraint>();// 更動
				ArrayList<CLGGraph> subclg = new ArrayList<CLGGraph>();
				if (opconstraint.getLeftOperand() instanceof CLGIfNode) {
					this.constraint.add(opconstraint.getLeftOperand());
					findChildNodeIsIf(opconstraint.getRightOperand());
				}
				else if (opconstraint.getLeftOperand() instanceof CLGMethodInvocationNode) {
					this.constraint.add(opconstraint.getLeftOperand());
					findChildNodeIsIf(opconstraint.getRightOperand());
				}
				else {
					findChildNodeIsIf(opconstraint);
				}

				ArrayList<CLGConstraint> otherConstraint = new ArrayList<CLGConstraint>();
				for (CLGConstraint cons : this.constraint) {
					if (cons instanceof CLGIfNode ||cons instanceof CLGMethodInvocationNode) {
						subclg.add(this.parseConstraint(cons, criterion));
						otherConstraint.add(cons);
					} 
					else {
						
						this.constraintIterate = new ArrayList<CLGConstraint>();
						findChildNodeIsIterate(cons);
						
						if (constraintIterate.size() > 1)
							for (CLGConstraint tempcons : this.constraintIterate) {
								if ((tempcons instanceof CLGIterateNode)) {
									subclg.add(parseConstraint(tempcons, criterion));
								} else
									subclg.add(new CLGGraph(tempcons));
							}
						else
							subclg.add(new CLGGraph(cons));
					}
				}

				if (this.tempconstraint == null) {
					this.tempconstraint = new ArrayList<CLGConstraint>();
					this.tempconstraint.add(opconstraint);
					this.tempconstraint.addAll(this.constraint);
				}
				for (int clgsize = subclg.size(); clgsize >= 2; clgsize--) {
					CLGGraph right = subclg.get(clgsize - 1);
					CLGGraph left = subclg.get(clgsize - 2);
					subclg.remove(clgsize - 1);
					subclg.remove(clgsize - 2);
					left.graphAnd(right);
					subclg.add(left);
				}
				return subclg.get(0);
			} else if ((opconstraint.getOperator().equals("&&") || opconstraint.getOperator().equals("and"))
					&& (criterion.equals(CriterionFactory.Criterion.dcc) || criterion.equals(CriterionFactory.Criterion.mcc))) {

				CLGGraph t = parseConstraint(opconstraint.getLeftOperand(), criterion);

				if (opconstraint.getRightOperand() != null) {
					CLGGraph y = parseConstraint(opconstraint.getRightOperand(), criterion);

					t.graphAnd(y);
				}

				return t;
			}
			if ((opconstraint.getOperator().equals("||") || opconstraint.getOperator().equals("or")) && criterion.equals(CriterionFactory.Criterion.dcc)) {
				CLGConstraint left = opconstraint.getLeftOperand();

				CLGConstraint notleft = null;
				//if(left instanceof CLGOperatorNode)
				notleft =this.Demongan(left);
				

				CLGConstraint right = opconstraint.getRightOperand();
				CLGConstraint notright = null;
				if (opconstraint.getRightOperand() instanceof CLGOperatorNode)
					notright = this.Demongan(((CLGOperatorNode) opconstraint).getRightOperand());
				else
					notright = opconstraint.getRightOperand();
				CLGGraph A = parseConstraint(left, criterion);

				CLGGraph notA = parseConstraint(notleft, criterion);

				if (right != null) {
					CLGGraph B = parseConstraint(right, criterion);

					CLGGraph notB = parseConstraint(notright, criterion);

					A.graphAnd(notB);
					notA.graphAnd(B);

					A.graphOr(notA);
				}

				return A;
			}
			if ((opconstraint.getOperator().equals("||") || opconstraint.getOperator().equals("or")) && criterion.equals(CriterionFactory.Criterion.mcc)) {
				CLGConstraint left = opconstraint.getLeftOperand();

				CLGConstraint notleft = this.Demongan(left);
				

				CLGConstraint right = opconstraint.getRightOperand();
				CLGConstraint notright = null;
				if (opconstraint.getRightOperand() instanceof CLGOperatorNode)
					notright = this.Demongan((CLGOperatorNode) opconstraint.getRightOperand());
				else
					notright = opconstraint.getRightOperand();
				CLGGraph A = parseConstraint(left, criterion);

				CLGGraph mid = parseConstraint(left, criterion);

				CLGGraph notA = parseConstraint(notleft, criterion);

				if (right != null) {
					CLGGraph mid2 = parseConstraint(right, criterion);

					CLGGraph B = parseConstraint(right, criterion);

					CLGGraph notB = parseConstraint(notright, criterion);

					mid.graphAnd(mid2);

					A.graphAnd(notB);

					notA.graphAnd(B);

					A.graphOr(notA);

					A.graphOr(mid);
				}

				return A;
			}
			if (opconstraint.getOperator().equals("xor")) {
				CLGConstraint left = opconstraint.getLeftOperand();

				CLGConstraint notleft = this.Demongan((CLGOperatorNode) opconstraint.getLeftOperand());
				;

				CLGConstraint right = opconstraint.getRightOperand();

				CLGConstraint notright = this.Demongan((CLGOperatorNode) opconstraint.getRightOperand());

				CLGGraph A = parseConstraint(left, criterion);

				CLGGraph notA = parseConstraint(notleft, criterion);

				CLGGraph B = parseConstraint(right, criterion);

				CLGGraph notB = parseConstraint(notright, criterion);

				A.graphAnd(notB);

				B.graphAnd(notA);

				A.graphOr(B);
				return A;
			}
			if (opconstraint.getOperator().equals("implies")) {
				CLGConstraint left = opconstraint.getLeftOperand();

				CLGConstraint notleft = this.Demongan((CLGOperatorNode) opconstraint.getLeftOperand());
				;

				CLGConstraint right = opconstraint.getRightOperand();

				CLGConstraint notright = this.Demongan((CLGOperatorNode) opconstraint.getRightOperand());

				CLGGraph A = parseConstraint(left, criterion);

				CLGGraph notA = parseConstraint(notleft, criterion);

				CLGGraph B = parseConstraint(right, criterion);

				CLGGraph notB = parseConstraint(notright, criterion);

				notA.graphAnd(notB);

				A.graphAnd(B);

				notA.graphOr(A);
				return A;
			}
		}
		// }

		return new CLGGraph(constraint);
	}

	private CLGGraph parseConstraint(CLGConstraint constraint, String criterion) {// 仿

		if (constraint instanceof CLGIfNode) {
			CLGIfNode ifconstraint = (CLGIfNode) constraint;
			CLGConstraint condition = ifconstraint.getCondition();
			CLGGraph left = parseConstraint(condition, criterion);
			CLGConstraint then = ifconstraint.getThen();
			CLGGraph middle = parseConstraint(then, criterion);

			CLGConstraint notcondition = this.Demongan(condition);
			CLGGraph notleft = parseConstraint(notcondition, criterion);
			left.graphAnd(middle);

			if (ifconstraint.getElse() != null) {
				CLGConstraint elseExp = ifconstraint.getElse();
				CLGGraph right = parseConstraint(elseExp, criterion);
				notleft.graphAnd(right);
			}
			left.graphOr(notleft);

			return left;
		}

		else if ((constraint instanceof CLGIterateNode) && criterion.equals("dc")) {
			CLGIterateNode collconstraint = (CLGIterateNode) constraint;
			CLGGraph initial = new CLGGraph(((CLGOperatorNode) collconstraint.getInitial()).getLeftOperand());
			initial.graphAnd(new CLGGraph(((CLGOperatorNode) collconstraint.getInitial()).getRightOperand()));

			CLGGraph condition = new CLGGraph(collconstraint.getCondition());
			condition.graphAnd(new CLGGraph(collconstraint.getBody()));
			condition.graphAnd(new CLGGraph(collconstraint.getIncrement()));

			condition.graphClosure();
			condition.graphAnd(new CLGGraph(this.Demongan(collconstraint.getCondition())));
			initial.graphAnd(condition);
			return initial;

		} else if (constraint instanceof CLGIterateNode) {
			CLGIterateNode collconstraint = (CLGIterateNode) constraint;
			CLGGraph condition = parseConstraint(collconstraint.getCondition(), criterion);
			CLGGraph body = parseConstraint(collconstraint.getBody(), criterion);
			CLGGraph increment = parseConstraint(collconstraint.getIncrement(), criterion);
			condition.graphAnd(body);
			condition.graphAnd(increment);
			condition.graphClosure();
			condition.graphAnd(new CLGGraph(this.Demongan(collconstraint.getCondition())));
			// CLGConstraint initialCon=collconstraint.getInitial();
			CLGGraph initial = parseConstraint(collconstraint.getInitial(), criterion);
			initial.graphAnd(condition);
			return initial;
		}

		else if (constraint instanceof CLGMethodInvocationNode) {
			return new CLGGraph(constraint);
		} else if (constraint instanceof CLGVariableNode) {
			return new CLGGraph(constraint);
		} else {
			CLGOperatorNode opconstraint = (CLGOperatorNode) constraint;
			if ((opconstraint.getOperator().equals("&&") || opconstraint.getOperator().equals("and") || opconstraint.getOperator().equals("||") || opconstraint.getOperator().equals("or"))
					&& criterion.equals("dc")) {

				this.constraint = new ArrayList<CLGConstraint>();// 更動
				ArrayList<CLGGraph> subclg = new ArrayList<CLGGraph>();
				if (opconstraint.getLeftOperand() instanceof CLGIfNode) {
					this.constraint.add(opconstraint.getLeftOperand());
					findChildNodeIsIf(opconstraint.getRightOperand());
				} else {
					findChildNodeIsIf(opconstraint);
				}

				ArrayList<CLGConstraint> otherConstraint = new ArrayList<CLGConstraint>();
				for (CLGConstraint cons : this.constraint) {
					if (cons instanceof CLGIfNode) {
						subclg.add(this.parseConstraint(cons, criterion));
						otherConstraint.add(cons);
					} else {

						this.constraintIterate = new ArrayList<CLGConstraint>();
						findChildNodeIsIterate(cons);
						subclg.add(new CLGGraph(cons));
						if (constraintIterate.size() > 1)
							for (CLGConstraint tempcons : this.constraintIterate) {

								if (tempcons instanceof CLGIterateNode) {
									subclg.add(parseConstraint(tempcons, criterion));
								} else
									subclg.add(new CLGGraph(tempcons));
							}

					}
				}

				if (this.tempconstraint == null) {
					this.tempconstraint = new ArrayList<CLGConstraint>();
					this.tempconstraint.add(opconstraint);
					this.tempconstraint.addAll(this.constraint);
				}
				for (int clgsize = subclg.size(); clgsize >= 2; clgsize--) {
					CLGGraph right = subclg.get(clgsize - 1);
					CLGGraph left = subclg.get(clgsize - 2);
					subclg.remove(clgsize - 1);
					subclg.remove(clgsize - 2);
					left.graphAnd(right);
					subclg.add(left);
				}
				return subclg.get(0);
			} else if ((opconstraint.getOperator().equals("&&") || opconstraint.getOperator().equals("and")) && (criterion.equals("dcc") || criterion.equals("mcc"))) {

				CLGGraph t = parseConstraint(opconstraint.getLeftOperand(), criterion);

				if (opconstraint.getRightOperand() != null) {
					CLGGraph y = parseConstraint(opconstraint.getRightOperand(), criterion);

					t.graphAnd(y);
				}

				return t;
			}
			if ((opconstraint.getOperator().equals("||") || opconstraint.getOperator().equals("or")) && criterion.equals("dcc")) {
				CLGConstraint left = opconstraint.getLeftOperand();

				CLGConstraint notleft = this.Demongan((CLGOperatorNode) opconstraint.getLeftOperand());
				;

				CLGConstraint right = opconstraint.getRightOperand();
				CLGConstraint notright = null;
				if (opconstraint.getRightOperand() instanceof CLGOperatorNode)
					notright = this.Demongan(((CLGOperatorNode) opconstraint).getRightOperand());
				else
					notright = opconstraint.getRightOperand();
				CLGGraph A = parseConstraint(left, criterion);

				CLGGraph notA = parseConstraint(notleft, criterion);

				if (right != null) {
					CLGGraph B = parseConstraint(right, criterion);

					CLGGraph notB = parseConstraint(notright, criterion);

					A.graphAnd(notB);
					notA.graphAnd(B);

					A.graphOr(notA);
				}

				return A;
			}
			if ((opconstraint.getOperator().equals("||") || opconstraint.getOperator().equals("or")) && criterion.equals("mcc")) {
				CLGConstraint left = opconstraint.getLeftOperand();

				CLGConstraint notleft = this.Demongan((CLGOperatorNode) opconstraint.getLeftOperand());
				;

				CLGConstraint right = opconstraint.getRightOperand();
				CLGConstraint notright = null;
				if (opconstraint.getRightOperand() instanceof CLGOperatorNode)
					notright = this.Demongan((CLGOperatorNode) opconstraint.getRightOperand());
				else
					notright = opconstraint.getRightOperand();
				CLGGraph A = parseConstraint(left, criterion);

				CLGGraph mid = parseConstraint(left, criterion);

				CLGGraph notA = parseConstraint(notleft, criterion);

				if (right != null) {
					CLGGraph mid2 = parseConstraint(right, criterion);

					CLGGraph B = parseConstraint(right, criterion);

					CLGGraph notB = parseConstraint(notright, criterion);

					mid.graphAnd(mid2);

					A.graphAnd(notB);

					notA.graphAnd(B);

					A.graphOr(notA);

					A.graphOr(mid);
				}

				return A;
			}
			if (opconstraint.getOperator().equals("xor")) {
				CLGConstraint left = opconstraint.getLeftOperand();

				CLGConstraint notleft = this.Demongan((CLGOperatorNode) opconstraint.getLeftOperand());
				;

				CLGConstraint right = opconstraint.getRightOperand();

				CLGConstraint notright = this.Demongan((CLGOperatorNode) opconstraint.getRightOperand());

				CLGGraph A = parseConstraint(left, criterion);

				CLGGraph notA = parseConstraint(notleft, criterion);

				CLGGraph B = parseConstraint(right, criterion);

				CLGGraph notB = parseConstraint(notright, criterion);

				A.graphAnd(notB);

				B.graphAnd(notA);

				A.graphOr(B);
				return A;
			}
			if (opconstraint.getOperator().equals("implies")) {
				CLGConstraint left = opconstraint.getLeftOperand();

				CLGConstraint notleft = this.Demongan((CLGOperatorNode) opconstraint.getLeftOperand());
				;

				CLGConstraint right = opconstraint.getRightOperand();

				CLGConstraint notright = this.Demongan((CLGOperatorNode) opconstraint.getRightOperand());

				CLGGraph A = parseConstraint(left, criterion);

				CLGGraph notA = parseConstraint(notleft, criterion);

				CLGGraph B = parseConstraint(right, criterion);

				CLGGraph notB = parseConstraint(notright, criterion);

				notA.graphAnd(notB);

				A.graphAnd(B);

				notA.graphOr(A);
				return A;
			}
		}
		// }

		return new CLGGraph(constraint);
	}

	public CLGConstraint Demonganif(CLGIfNode Demonganconstraint) {
		CLGIfNode newif;
		CLGConstraint then;
		CLGConstraint elseExp;

		if (Demonganconstraint.getThen() instanceof CLGIfNode)
			then = Demonganif((CLGIfNode) Demonganconstraint.getThen());
		else
			then = Demongan(Demonganconstraint.getThen());
		if (Demonganconstraint.getElse() != null) {
			if (Demonganconstraint.getElse() instanceof CLGIfNode)
				elseExp = Demonganif((CLGIfNode) Demonganconstraint.getElse());
			else
				elseExp = Demongan(Demonganconstraint.getElse());
			newif = new CLGIfNode(Demonganconstraint.getCondition(), then, elseExp);
		} else
			newif = new CLGIfNode(Demonganconstraint.getCondition(), then);

		return newif;
	}

	public CLGConstraint Demongan(CLGConstraint Demonganconstraint) {

		CLGConstraint finaltree = null;
if(Demonganconstraint instanceof CLGIterateNode)
{
	finaltree = new CLGIterateNode();
	((CLGIterateNode) finaltree).setInitial(((CLGIterateNode) Demonganconstraint).getInitial());
	((CLGIterateNode) finaltree).setCondition(((CLGIterateNode) Demonganconstraint).getCondition());
	((CLGIterateNode) finaltree).setIncrement(((CLGIterateNode) Demonganconstraint).getIncrement());
	((CLGIterateNode) finaltree).setStart(((CLGIterateNode) Demonganconstraint).getStart());
	((CLGIterateNode) finaltree).setStart(((CLGIterateNode) Demonganconstraint).getStart());
	((CLGIterateNode) finaltree).setAccType(((CLGIterateNode) Demonganconstraint).getAccType());
	//if(((CLGOperatorNode) ((CLGIterateNode) Demonganconstraint).getBody()).getBoundary())
		
	((CLGIterateNode) finaltree).setBody(Demongan(((CLGIterateNode) Demonganconstraint).getBody()));
	if( ((CLGIterateNode) Demonganconstraint).getBody()instanceof CLGOperatorNode)
	{
		if(((CLGOperatorNode) ((CLGIterateNode) Demonganconstraint).getBody()).getBoundary())
		{
			((CLGOperatorNode) ((CLGIterateNode) Demonganconstraint).getBody()).setBoundary();
		}
	}
}
else
{
		if (((CLGOperatorNode) Demonganconstraint).getOperator().equals("&&") || ((CLGOperatorNode) Demonganconstraint).getOperator().equals("and")) {
			finaltree = new CLGOperatorNode("||");
		} else if (((CLGOperatorNode) Demonganconstraint).getOperator().equals("||") || ((CLGOperatorNode) Demonganconstraint).getOperator().equals("or")) {
			finaltree = new CLGOperatorNode("&&");
		} else if (((CLGOperatorNode) Demonganconstraint).getOperator().equals("==") || ((CLGOperatorNode) Demonganconstraint).getOperator().equals("=")) {
			finaltree = new CLGOperatorNode("<>");
		} else if (((CLGOperatorNode) Demonganconstraint).getOperator().equals("<>")) {
			finaltree = new CLGOperatorNode("==");
		} else if (((CLGOperatorNode) Demonganconstraint).getOperator().equals("<=")) {
			finaltree = new CLGOperatorNode(">");
		} else if (((CLGOperatorNode) Demonganconstraint).getOperator().equals(">")) {
			finaltree = new CLGOperatorNode("<=");
		} else if (((CLGOperatorNode) Demonganconstraint).getOperator().equals(">=")) {
			finaltree = new CLGOperatorNode("<");
		} else if (((CLGOperatorNode) Demonganconstraint).getOperator().equals("<")) {
			finaltree = new CLGOperatorNode(">=");
		}
		if (((CLGOperatorNode) Demonganconstraint).getLeftOperand() instanceof ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode
				&& !((CLGOperatorNode) ((CLGOperatorNode) Demonganconstraint).getLeftOperand()).getOperator().contains("*")
				&& !((CLGOperatorNode) ((CLGOperatorNode) Demonganconstraint).getLeftOperand()).getOperator().contains("%")
				&& !((CLGOperatorNode) ((CLGOperatorNode) Demonganconstraint).getLeftOperand()).getOperator().contains("+")
				&& !((CLGOperatorNode) ((CLGOperatorNode) Demonganconstraint).getLeftOperand()).getOperator().contains("-")) {

			((CLGOperatorNode) finaltree).setLeftOperand(Demongan(((CLGOperatorNode) Demonganconstraint).getLeftOperand()));

		}else if(((CLGOperatorNode) Demonganconstraint).getLeftOperand() instanceof CLGIterateNode)
		{
			((CLGOperatorNode) finaltree).setLeftOperand(Demongan(((CLGOperatorNode) Demonganconstraint).getLeftOperand()));
		}
		else {
			((CLGOperatorNode) finaltree).setLeftOperand(((CLGOperatorNode) Demonganconstraint).getLeftOperand());
		}

		if (((CLGOperatorNode) Demonganconstraint).getRightOperand() instanceof ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode
				&& !((CLGOperatorNode) ((CLGOperatorNode) Demonganconstraint).getRightOperand()).getOperator().contains("*")
				&& !((CLGOperatorNode) ((CLGOperatorNode) Demonganconstraint).getRightOperand()).getOperator().contains("+")
				&& !((CLGOperatorNode) ((CLGOperatorNode) Demonganconstraint).getRightOperand()).getOperator().contains("-")
				&& !((CLGOperatorNode) ((CLGOperatorNode) Demonganconstraint).getRightOperand()).getOperator().contains("%")) {

			((CLGOperatorNode) finaltree).setRightOperand(Demongan(((CLGOperatorNode) Demonganconstraint).getRightOperand()));
		} else {
			((CLGOperatorNode) finaltree).setRightOperand(((CLGOperatorNode) Demonganconstraint).getRightOperand());
		}
}
		return finaltree;

	}

	public void AddConstraintNode(CLGGraph clg1, CLGGraph clg2) {

		for (int j = 0; j < clg2.getConstraintCollection().size(); j++) {
			String[] index = clg2.getConstraintCollection().keySet().toString().substring(1, clg2.getConstraintCollection().keySet().toString().length() - 1).split(", ");
			int b = Integer.parseInt(index[j]);
			clg1.getConstraintCollection().put(b, clg2.getConstraintNodeById(b));
		}

	}

}
