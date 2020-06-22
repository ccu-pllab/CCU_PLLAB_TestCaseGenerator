package ccu.pllab.tcgen.AbstractConstraint;

import java.util.ArrayList;

import org.antlr.v4.parse.ANTLRParser.ruleReturns_return;
import org.stringtemplate.v4.compiler.STParser.template_return;

import ccu.pllab.tcgen.exe.main.Main;


//import ccu.pllab.tcgen.ACLG2path.CriterionFactory.Criterion;

public class CLGOperatorNode extends CLGConstraint {
	private String operator;
	private String type;
	private CLGConstraint leftOperand;
	private CLGConstraint rightOperand;
	private boolean fromIterateExp=false;
	private boolean boundary=false;
	private  CLGOperatorNode useclone;
	private String cloneConId;
	public CLGOperatorNode(String operation) {
		super();
		switch(operation)
		{
		case "||":
		case "or":
		this.operator = "||";
		break;
		case "&&":
		case "and":
			this.operator = "&&";
			break;
		default:
			this.operator=operation;
		}
		this.type = "";
		leftOperand = null;
		rightOperand = null;
	}

	public CLGOperatorNode() {
		super();
		this.operator = "";
		this.type = "";
		leftOperand = null;
		rightOperand = null;
	}

	public void setOperator(String op) {
		this.operator = op;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setLeftOperand(CLGConstraint constraint) {
		this.leftOperand = constraint;
	}

	public CLGConstraint getLeftOperand() {
		return this.leftOperand;
	}

	public void setRightOperand(CLGConstraint constraint) {
		this.rightOperand = constraint;
	}

	public CLGConstraint getRightOperand() {
		return this.rightOperand;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
	
	public void setFromIterateExp() {
		this.fromIterateExp=true;
	}
	
	public boolean getFromIterateExp() {
		return this.fromIterateExp;
	}
	
	public void setBoundary() {
		this.boundary=true;
	}
	
	public boolean getBoundary() {
		return this.boundary;
	}
	
	private void setUseClone(CLGOperatorNode node)
	{
		this.useclone=node;
	}

	public CLGOperatorNode getUseClone()
	{
		return this.useclone;
	}
	
	private void setCloneCID(String id)
	{
		this.cloneConId=id;
	}

	public String getCloneCID()
	{
		return this.cloneConId;
	}
	
	public void negation() {
		/* criteria */

		if (this.operator.equals("==")) {
			this.operator = "<>";
		} else if (this.operator.equals("!=")) {
			this.operator = "==";
		} else if (this.operator.equals(">")) {
			this.operator = "<=";
		} else if (this.operator.equals("<")) {
			this.operator = ">=";
		} else if (this.operator.equals(">=")) {
			this.operator = "<";
		} else if (this.operator.equals("<=")) {
			this.operator = ">";
		} else if (this.operator.equals("&&")) {
			this.operator = "||";
			CLGConstraint opClass = new CLGOperatorNode();
			if (this.leftOperand.getClass().equals(opClass.getClass())) {
				CLGConstraint newLeftCons = this.leftOperand.clone();
				((CLGOperatorNode) newLeftCons).negation();
				this.leftOperand = newLeftCons;
			}
			if (this.rightOperand.getClass().equals(opClass.getClass())) {
				CLGConstraint newRightCons = this.rightOperand.clone();
				((CLGOperatorNode) newRightCons).negation();
				this.rightOperand = newRightCons;
			}
		} else if (this.operator.equals("and")) {
			this.operator = "or";
			CLGConstraint opClass = new CLGOperatorNode();
			if (this.leftOperand.getClass().equals(opClass.getClass())) {
				CLGConstraint newLeftCons = this.leftOperand.clone();
				((CLGOperatorNode) newLeftCons).negation();
				this.leftOperand = newLeftCons;
			}
			if (this.rightOperand.getClass().equals(opClass.getClass())) {
				CLGConstraint newRightCons = this.rightOperand.clone();
				((CLGOperatorNode) newRightCons).negation();
				this.rightOperand = newRightCons;
			}
		}else if (this.operator.equals("||")) {
			this.operator = "&&";
			CLGConstraint opClass = new CLGOperatorNode();
			if (this.leftOperand.getClass().equals(opClass.getClass())) {
				CLGConstraint newLeftCons = this.leftOperand.clone();
				((CLGOperatorNode) newLeftCons).negation();
				this.leftOperand = newLeftCons;
			}
			if (this.rightOperand.getClass().equals(opClass.getClass())) {
				CLGConstraint newRightCons = this.rightOperand.clone();
				((CLGOperatorNode) newRightCons).negation();
				this.rightOperand = newRightCons;
			}
		}else if (this.operator.equals("or")) {
			this.operator = "and";
			CLGConstraint opClass = new CLGOperatorNode();
			if (this.leftOperand.getClass().equals(opClass.getClass())) {
				CLGConstraint newLeftCons = this.leftOperand.clone();
				((CLGOperatorNode) newLeftCons).negation();
				this.leftOperand = newLeftCons;
			}
			if (this.rightOperand.getClass().equals(opClass.getClass())) {
				CLGConstraint newRightCons = this.rightOperand.clone();
				((CLGOperatorNode) newRightCons).negation();
				this.rightOperand = newRightCons;
			}
		}
	}

	@Override
	public String getImgInfo() {
		if (this.operator.equals("!=")) {
			this.operator = "<>";
		}
		String info="";
		if(leftOperand!=null)
			info+=leftOperand.getImgInfo();
		if(rightOperand!=null)
			info+=this.operator +rightOperand.getImgInfo();
			return info;
		//return leftOperand.getImgInfo() + this.operator + rightOperand.getImgInfo();
	}

	@Override
	public String getCLPInfo() {
		String new_op = "";
		/*if(Main.boundary_analysis&&this.boundary&&(this.operator.equals(">=")||this.operator.equals("<="))&&Main.changeBoundary)
		{
				new_op="#=";
				
		}
		else*/
		switch (this.operator) {
		case "==":
			new_op = "#=";
			break;
		case "!=":
			new_op = "#\\=";
			break;
		case "<>":
			new_op = "#\\=";
			break;
		case "<":
			new_op = "#<";
			break;
		case ">":
			new_op = "#>";
			break;
		case "<=":
			
			if(Main.boundary_analysis&&this.boundary&&Main.changeBoundary)
				new_op = "#=";
			//else if(Main.boundary_analysis)
			//	new_op = "#<";
			else
			new_op = "#=<";
			break;
		case ">=":
			if(Main.boundary_analysis&&this.boundary&&Main.changeBoundary)
				new_op = "#=";
			//else if(Main.boundary_analysis)
			//	new_op = "#>";
			else
			new_op = "#>=";
			break;
		case "&&":
			new_op = ",";
			break;
		case "||":
		case "or":
			new_op = " or ";
			break;
		case "=":
			
			if (rightOperand instanceof CLGLiteralNode) {
				//if (((CLGLiteralNode) rightOperand).getType().toLowerCase() != "string")
				//{
				new_op = "#=";
					if (((CLGLiteralNode) rightOperand).getValue().contains("\"") || ((CLGLiteralNode) rightOperand).getValue().contains("true")||((CLGLiteralNode) rightOperand).getValue().contains("false")) 
						new_op = "=";
					if(((CLGLiteralNode) rightOperand).getValue().equals("[]"))
						new_op = "=";
				//	} else {
				//		new_op = "#=";
				//		this.operator="#=";
				//	}
			//	}
			//	else {
			//		new_op = "=";
				}
			/*} else if (rightOperand instanceof CLGVariableNode) {
				new_op = "=";
				this.operator="=";
			} else {

				if (rightOperand instanceof CLGOperatorNode) {
				/*	if (((CLGOperatorNode) rightOperand).getRightOperand() instanceof CLGLiteralNode) {
						CLGConstraint tempCLGOperatorConstraint = ((CLGOperatorNode) rightOperand).getRightOperand();
						if (((CLGLiteralNode) tempCLGOperatorConstraint).getType() != "String" ) {
							new_op = "#=";
							this.operator="#=";
						}
//
					} else {
						new_op = "=";
					}
				}*/ else {
					new_op = "#=";
					//this.operator="#=";
				}
			//}
			break;
		default:
			new_op = this.operator;
			break;
		}

		/* rDign */
		if(this.leftOperand instanceof CLGMethodInvocationNode)
		{
			if(new_op=="#\\=")
				return "";
			if(this.rightOperand.getCLPInfo().equals("true"))
			return leftOperand.getCLPInfo();
		}
		if (this.leftOperand instanceof CLGOperatorNode) {
			if (((CLGOperatorNode) this.leftOperand).getOperator().equals("%")) {
				//if (!this.operator.equals("==") ) {
					return this.leftOperand.getCLPInfo() + "," + "Remainder"
							+ "),Remainder"+new_op+this.rightOperand.getCLPInfo();/* TCSE help */
				//} else {
				//	return this.leftOperand.getCLPInfo() + "," + this.rightOperand.getCLPInfo() + ")";
				//}

			}
			
			/* rDign */
		} else if (this.rightOperand!=null &&this.rightOperand instanceof CLGOperatorNode) {

			if (((CLGOperatorNode) this.rightOperand).getOperator().equals("%")) {
				if (!this.operator.equals("==")) {
					return this.rightOperand.getCLPInfo() + "," + this.leftOperand.getCLPInfo()
							+ ")";/* TCSE help */
				} else {
					return this.rightOperand.getCLPInfo() + "," + this.leftOperand.getCLPInfo() + ")";
				}
			}
		}

		if (this.operator.equals("%")) {

			/* rFact */
			/* TCSE help */
			return "o_mod(" + this.leftOperand.getCLPInfo() + "," + this.rightOperand.getCLPInfo();
		} else {

			if (this.operator.equals("||")) {
				if(this.rightOperand!=null)
				{
					if(leftOperand!=null)
				return "(" + this.leftOperand.getCLPInfo() + ";" + this.rightOperand.getCLPInfo() + ")";
					else
						return  this.rightOperand.getCLPInfo();
				}
				else
					return  this.leftOperand.getCLPInfo();
				} else {

				if (this.rightOperand instanceof CLGOperatorNode && this.operator.equals("=")) {

					if (((CLGOperatorNode) this.rightOperand).getOperator().equals("%")) {
						return this.leftOperand.getCLPInfo() + new_op + "Remainder";
					}
				}
				String left;
				if(this.leftOperand!=null)
				left=this.leftOperand.getCLPInfo();
				else
					left="";
				String right="";
				if(this.rightOperand!=null)
				{
				right=this.rightOperand.getCLPInfo();
			//	return this.leftOperand.getCLPInfo() + new_op + this.rightOperand.getCLPInfo();
				String  returnString="";
				if(!(this.operator.equals("and")||this.operator.equals("or")))
				{
				if(left.contains("["))
				{
					if(left.contains("]["))
					{
						//System.out.println("left:"+left);
						int indexRow;
						if(Main.indexMap.containsKey("IndexRow"))
						{
							indexRow=Main.indexMap.get("IndexRow");
							Main.indexMap.put("IndexRow", Main.indexMap.get("IndexRow")+1);
						}
						else
						{
							indexRow=1;
							Main.indexMap.put("IndexRow", 2);
						}
						returnString+="IndexRow"+indexRow+"#=";
						if(left.contains("it"))
							returnString+=left.substring(left.indexOf("[")+1,left.indexOf("]"))+",\n";
						else
						returnString+=left.substring(left.indexOf("[")+1,left.indexOf("[")+2).toUpperCase()+left.substring(left.indexOf("[")+2,left.indexOf("]"))+",\n";
						returnString+="array_list(";
						String leftArray="";
						if(Main.msort && !left.contains("_pre"))
							leftArray+=left.substring(0, left.indexOf("["))+"_pre";
						else
							leftArray+=left.substring(0, left.indexOf("["));
						returnString+=leftArray+",Temp"+leftArray+"),\n";//array_list(Data,TempData),\n
						int arrayRow;
						if(Main.arrayMap.containsKey("ArrayRow"))
						{
							arrayRow=Main.arrayMap.get("ArrayRow");
							Main.arrayMap.put("ArrayRow", Main.arrayMap.get("ArrayRow")+1);
						}
						else
						{
							arrayRow=1;
							Main.arrayMap.put("ArrayRow", 2);
						}
						returnString+="findelement(Temp"+leftArray+",ArrayRow"+arrayRow+",IndexRow"+indexRow+"),\n";//findelement(TempData,ArrayRow1,IndexRow1),
						
						int index;
						if(Main.indexMap.containsKey("Index"))
						{
							index=Main.indexMap.get("Index");
							Main.indexMap.put("Index", Main.indexMap.get("Index")+1);
						}
						else
						{
							index=1;
							Main.indexMap.put("Index", 2);
						}
						returnString+="Index"+index+"#=";
						String col=left.substring(left.lastIndexOf("[")+1,left.lastIndexOf("]"));
						if(col.contains("it"))
							returnString+=col+",\n";
						else
							returnString+=col.substring(0,1).toUpperCase()+col.substring(1)+",\n";
						int array;
						if(Main.arrayMap.containsKey("Array"))
						{
							array=Main.arrayMap.get("Array");
							Main.arrayMap.put("Array", Main.arrayMap.get("Array")+1);
						}
						else
						{
							array=1;
							Main.arrayMap.put("Array", 2);
						}
						returnString+="array_list(ArrayRow"+arrayRow+",Array"+array+"),\n";//array_list(ArrayRow1,Array1),
						int element;
						if(Main.arrayMap.containsKey("Element"))
						{
							element=Main.arrayMap.get("Element");
							Main.arrayMap.put("Element", Main.arrayMap.get("Element")+1);
						}
						else
						{
							element=1;
							Main.arrayMap.put("Element", 2);
						}
						returnString+="findelement(Array"+array+",Element"+element+",Index"+index+"),\n";//findelement(Array1,Element1,Index1),
						returnString=returnString.replaceAll("it2", "i2t");
						left="Element"+element;
						/*returnString+="IndexRow#=";
						if(left.contains("it"))
							returnString+=left.substring(left.indexOf("[")+1,left.indexOf("]"))+",\n";
						else
						returnString+=left.substring(left.indexOf("[")+1,left.indexOf("[")+2).toUpperCase()+left.substring(left.indexOf("[")+2,left.indexOf("]"))+",\n";
						returnString+="arg(IndexRow,";
						if(Main.msort && !left.contains("_pre"))
							returnString+=left.substring(0, left.indexOf("["))+"_pre";
						else
						returnString+=left.substring(0, left.indexOf("["));
						
						returnString+=",ArrayRowi),\n";
						returnString+="Index#=";
						String col=left.substring(left.lastIndexOf("[")+1,left.lastIndexOf("]"));
						if(col.contains("it"))
							returnString+=col+",\n";
						else
							returnString+=col.substring(0,1).toUpperCase()+col.substring(1)+",\n";
						returnString+="arg(Index,ArrayRowi,Arrayi),\n";
					//	returnString=returnString.replaceAll("it", "It");
						returnString=returnString.replaceAll("it2", "i2t");
						left="Arrayi";*/
					}
					else
					{
						int arrayIndex;
						if(Main.indexMap.containsKey("Index"))
						{
							arrayIndex=Main.indexMap.get("Index");
							Main.indexMap.put("Index", Main.indexMap.get("Index")+1);
						}
						else
						{
							arrayIndex=1;
							Main.indexMap.put("Index", 2);
						}
						returnString+="Index"+arrayIndex+"#=";
						if(left.contains("it"))
							returnString+=left.substring(left.indexOf("[")+1,left.indexOf("]"))+",\n";
						else
						returnString+=left.substring(left.indexOf("[")+1,left.indexOf("[")+2).toUpperCase()+left.substring(left.indexOf("[")+2,left.indexOf("]"))+",\n";
						if(left.contains("ArrayData"))
							returnString+="dcl_1dInt_array(ArrayData,Size_pre),\n";
						returnString+="findelement(";
						if(Main.msort && !left.contains("_pre"))
							returnString+=left.substring(0, left.indexOf("["))+"_pre";
						else
						returnString+=left.substring(0, left.indexOf("["));
						//returnString+=",Index"+arrayIndex+",";
						int element;
						if(Main.arrayMap.containsKey("Element"))
						{
							element=Main.arrayMap.get("Element");
							Main.arrayMap.put("Element", Main.arrayMap.get("Element")+1);
						}
						else
						{
							element=1;
							Main.arrayMap.put("Element", 2);
						}
						returnString+=",Element"+element+",Index"+arrayIndex+"),\n";
						
						/*returnString+="arg(Index"+ArrayIndex+",";
						if(Main.msort && !left.contains("_pre"))
							returnString+=left.substring(0, left.indexOf("["))+"_pre";
						else
						returnString+=left.substring(0, left.indexOf("["));
						returnString+=",Element";
						
						int element;
						if(Main.arrayMap.containsKey("Element"))
						{
							element=Main.arrayMap.get("Element");
							Main.arrayMap.put("Element", element+1);
						}
						else
						{
							element=1;
							Main.arrayMap.put("Element", 1);
						}
						returnString+=element+"),\n";*/
						left="Element"+element;
						
						
				/*	returnString+="Index#=";
					if(left.contains("it"))
						returnString+=left.substring(left.indexOf("[")+1,left.indexOf("]"))+",\n";
					else
					returnString+=left.substring(left.indexOf("[")+1,left.indexOf("[")+2).toUpperCase()+left.substring(left.indexOf("[")+2,left.indexOf("]"))+",\n";
					//returnString=returnString.replace("@", "");
					//returnString=returnString.replace("()", "_pre");
			/*		returnString+="findelement(";//0117 me
					if(Main.msort && !left.contains("_pre"))
						returnString+=left.substring(0, left.indexOf("["))+"_pre";
					else
					returnString+=left.substring(0, left.indexOf("["));
					
					returnString+=",Arrayi,Index),\n";
					left="Arrayi";*/
					}
				}
				if(right.contains("["))
				{
					if(right.contains("]["))
					{
						int indexRow;
						if(Main.indexMap.containsKey("IndexRow"))
						{
							indexRow=Main.indexMap.get("IndexRow");
							Main.indexMap.put("IndexRow", Main.indexMap.get("IndexRow")+1);
						}
						else
						{
							indexRow=1;
							Main.indexMap.put("IndexRow", 2);
						}
						returnString+="IndexRow"+indexRow+"#=";
						String row=right.substring(right.indexOf("[")+1,right.indexOf("]"));
						if(row.contains("it"))
							returnString+=row+",\n"; 
						else
						returnString+=row.substring(0,1).toUpperCase()+row.substring(1)+",\n";
						returnString+="array_list(";
						String rightArray="";
						if(Main.msort && !right.contains("_pre"))
							returnString+=right.substring(0, right.indexOf("["))+"_pre";
						else
						returnString+=right.substring(0, right.indexOf("["));
						
						returnString+=rightArray+",Temp"+rightArray+"),\n";//array_list(Data,TempData),\n
						int arrayRow;
						if(Main.arrayMap.containsKey("ArrayRow"))
						{
							arrayRow=Main.arrayMap.get("ArrayRow");
							Main.arrayMap.put("ArrayRow", Main.arrayMap.get("ArrayRow")+1);
						}
						else
						{
							arrayRow=1;
							Main.arrayMap.put("ArrayRow", 2);
						}
						returnString+="findelement(Temp"+rightArray+",ArrayRow"+arrayRow+",IndexRow"+indexRow+"),\n";//findelement(TempData,ArrayRow1,IndexRow1),
						int index;
						if(Main.indexMap.containsKey("Index"))
						{
							index=Main.indexMap.get("Index");
							Main.indexMap.put("Index", Main.indexMap.get("Index")+1);
						}
						else
						{
							index=1;
							Main.indexMap.put("Index", 2);
						}
						returnString+="Index"+index+"#=";
						String col=right.substring(right.lastIndexOf("[")+1,right.lastIndexOf("]"));
						if(col.contains("it"))
							returnString+=col+",\n";
						else
							returnString+=col.substring(0,1).toUpperCase()+col.substring(1)+",\n";
						int array;
						if(Main.arrayMap.containsKey("Array"))
						{
							array=Main.arrayMap.get("Array");
							Main.arrayMap.put("Array", Main.arrayMap.get("Array")+1);
						}
						else
						{
							array=1;
							Main.arrayMap.put("Array", 2);
						}
						returnString+="array_list(ArrayRow"+arrayRow+",Array"+array+"),\n";//array_list(ArrayRow1,Array1),
						int element;
						if(Main.arrayMap.containsKey("Element"))
						{
							element=Main.arrayMap.get("Element");
							Main.arrayMap.put("Element", Main.arrayMap.get("Element")+1);
						}
						else
						{
							element=1;
							Main.arrayMap.put("Element", 2);
						}
						returnString+="findelement(Array"+array+",Element"+element+",Index"+index+"),\n";//findelement(Array1,Element1,Index1),
						returnString=returnString.replaceAll("it2", "i2t");
						right="Element"+element;
						//System.out.println("right:"+right);
					/*	returnString+="IndexRow#=";
						String row=right.substring(right.indexOf("[")+1,right.indexOf("]"));
						if(row.contains("it"))
							returnString+=row+",\n"; 
						else
						returnString+=row.substring(0,1).toUpperCase()+row.substring(1)+",\n";
						returnString+="arg(IndexRow,";
						if(Main.msort && !right.contains("_pre"))
							returnString+=right.substring(0, right.indexOf("["))+"_pre";
						else
						returnString+=right.substring(0, right.indexOf("["));
						
						returnString+=",ArrayRowi),\n";
						returnString+="Index#=";
						String col=right.substring(right.lastIndexOf("[")+1,right.lastIndexOf("]"));
						if(col.contains("it"))
							returnString+=col+",\n";
						else
							returnString+=col.substring(0,1).toUpperCase()+col.substring(1)+",\n";
						returnString+="arg(Index,ArrayRowi,Arrayi),\n";
						returnString=returnString.replaceAll("it2", "i2t");
						right="Arrayi";*/
					}
					else
					{
						int arrayIndex;
						if(Main.indexMap.containsKey("Index"))
						{
							arrayIndex=Main.indexMap.get("Index");
							Main.indexMap.put("Index", Main.indexMap.get("Index")+1);
						}
						else
						{
							arrayIndex=1;
							Main.indexMap.put("Index", 2);
						}
					//	returnString+="Index"+arrayIndex+"#=";
						if(right.contains("it"))
						{
						//	System.out.println("testRight:"+right);
							String indexArray=right.substring(right.indexOf("[")+1);
							if(!indexArray.contains("["))
							{
							returnString+="Index"+arrayIndex+"#=";
							returnString+=right.substring(right.indexOf("[")+1,right.indexOf("]"))+",\n";
							}
							else
							{
								indexArray=right.substring(right.lastIndexOf("[")+1,right.indexOf("]"));
								
								returnString+="Index"+Main.indexMap.get("Index")+"#="+indexArray+",\n";
								
								returnString+="findelement(ArrayIndex,Element"+Main.arrayMap.get("Element")+",Index"+Main.indexMap.get("Index")+"),\n";
										Main.indexMap.put("Index", Main.indexMap.get("Index")+1);
								returnString+="Index"+arrayIndex+"#=Element"+Main.arrayMap.get("Element")+",\n";
								Main.arrayMap.put("Element", Main.arrayMap.get("Element")+1);
							}
						}
							else
							{
								returnString+="Index"+arrayIndex+"#=";
								returnString+=right.substring(right.indexOf("[")+1,right.indexOf("[")+2).toUpperCase()+right.substring(right.indexOf("[")+2,right.indexOf("]"))+",\n";
							}
								returnString+="findelement(";
						returnString+=right.substring(0, right.indexOf("["));
						int element;
						if(Main.arrayMap.containsKey("Element"))
						{
							element=Main.arrayMap.get("Element");
							Main.arrayMap.put("Element", Main.arrayMap.get("Element")+1);
						}
						else
						{
							element=1;
							Main.arrayMap.put("Element", 2);
						}
						returnString+=",Element"+element+",Index"+arrayIndex+"),\n";
						right="Element"+element;
					/*returnString+="Index#=";
					if(right.contains("it"))
					returnString+=right.substring(right.indexOf("[")+1,right.indexOf("]"))+",\n";
					else
						returnString+=right.substring(right.indexOf("[")+1,right.indexOf("[")+2).toUpperCase()+right.substring(right.indexOf("[")+2,right.indexOf("]"))+",\n";
					returnString+="findelement(";
					returnString+=right.substring(0, right.indexOf("["));
					returnString+=",Arrayi,Index),\n";
					right="Arrayi";*/
					}
				}
				//returnString=returnString.replaceAll("it", "It");
				}
				if(new_op.equals("+")||new_op.equals("-")||new_op.equals("*")||new_op.equals("/"))
					return returnString+"("+left+new_op+right+")";
				/*String  returnString="";
				if(this.operator.equals(">")||this.operator.equals("<")||this.operator.equals(">=")||this.operator.equals("<=")||this.operator.equals("==")||this.operator.equals("!="))
				{
					if((this.leftOperand instanceof CLGVariableNode)&&((CLGVariableNode) this.leftOperand).getConstraint() instanceof CLGOperatorNode)
						returnString+=((CLGVariableNode) this.leftOperand).getConstraint().getCLPInfo()+",";
					if((this.rightOperand instanceof CLGVariableNode)&&((CLGVariableNode) this.rightOperand).getConstraint() instanceof CLGOperatorNode)
						returnString+=((CLGVariableNode) this.rightOperand).getConstraint().getCLPInfo()+",";
					returnString+="\n";
				}*/
				return returnString+left+ new_op +right;
				}
				else
					return left;
			}
		}
	}
	@Override
	public  ArrayList<String> getInvCLPInfo()
	{
		ArrayList<String> variable=new ArrayList<String>();
		String new_op = "";
		switch (this.operator) {
		case "==":
			new_op = "#=";
			break;
		case "!=":
			new_op = "#\\=";
			break;
		case "<>":
			new_op = "#\\=";
			break;
		case "<":
			new_op = "#<";
			break;
		case ">":
			new_op = "#>";
			break;
		case "<=":
			new_op = "#=<";
			break;
		case ">=":
			new_op = "#>=";
			break;
		case "&&":
			new_op = ",";
			break;
		case "||":
		case "or":
			new_op = " or ";
			break;
		case "=":
			if (rightOperand instanceof CLGLiteralNode) {
				if (((CLGLiteralNode) rightOperand).getType().toLowerCase() != "string")
					if (((CLGLiteralNode) rightOperand).getValue().contains("\"")) {
						new_op = "=";
					} else {
						new_op = "#=";
					}
				else {
					new_op = "=";
				}
			} else if (rightOperand instanceof CLGVariableNode) {
				new_op = "#=";
			} else {

				if (rightOperand instanceof CLGOperatorNode) {
					if (((CLGOperatorNode) rightOperand).getRightOperand() instanceof CLGLiteralNode) {
						CLGConstraint tempCLGOperatorConstraint = ((CLGOperatorNode) rightOperand).getRightOperand();
						if (((CLGLiteralNode) tempCLGOperatorConstraint).getType() != "String") {
							new_op = "#=";
						}

					} else {
						new_op = "=";
					}
				} else {
					new_op = "#=";
				}
			}
			break;
		default:
			new_op = this.operator;
			break;
		}
				variable.addAll(this.leftOperand.getInvCLPInfo());
				if(this.rightOperand!=null)
				variable.addAll(this.rightOperand.getInvCLPInfo());
				return variable;
			
	}
	@Override
	public CLGConstraint clone() {
		String newOp = new String(this.operator);
		CLGConstraint cons = new CLGOperatorNode(newOp);
		
		if(this.leftOperand!=null)
		((CLGOperatorNode) cons).setLeftOperand(this.leftOperand.clone());
		if(this.rightOperand!=null)
		((CLGOperatorNode) cons).setRightOperand(this.rightOperand.clone());
		((CLGOperatorNode) cons).setType(this.type);
		cons.setCloneId(this.getConstraintId());
		((CLGOperatorNode) cons).setBoundary();
		((CLGOperatorNode) cons).setUseClone(this);
		((CLGOperatorNode) cons).setCloneCID(this.getConstraintId());
		return cons;
	}

	public String getConstraintImg() {
		String result = "";
		result += (this.getConstraintId() + " "
				+ String.format("[shape=\"ecllipse\", label=\"%s\",style = \"filled\",fillcolor = \"white\",xlabel=\"[%s]\"]" + "\n", this.getOperator(), this.getConstraintId()));
		result += (this.leftOperand.getConstraintId() + " " + String.format("[shape=\"ecllipse\", label=\"%s\",style = \"filled\",fillcolor = \"white\",xlabel=\"[%s]\"]" + "\n",
				this.leftOperand.getImgInfo(), this.leftOperand.getConstraintId()));
		result += (this.rightOperand.getConstraintId() + " " + String.format("[shape=\"ecllipse\", label=\"%s\",style = \"filled\",fillcolor = \"white\",xlabel=\"[%s]\"]" + "\n",
				this.rightOperand.getImgInfo(), this.rightOperand.getConstraintId()));

		result += this.getConstraintId() + "->" + this.leftOperand.getConstraintId() + "\n";
		result += this.getConstraintId() + "->" + this.rightOperand.getConstraintId() + "\n";

		return result;

	}

	@Override
	public String getCLPValue() {
		return null;
	}

	@Override
	public void setCLPValue(String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getLocalVariable() {
		// TODO Auto-generated method stub
		return this.leftOperand.getLocalVariable() + "," +this.rightOperand.getLocalVariable();
	}
	@Override
	public void negConstraint() {
		this.negation();
		this.leftOperand.negConstraint();
		this.rightOperand.negConstraint();
	}
	@Override
	public void preconditionAddPre()
	{
		if(this.leftOperand!=null)
			this.leftOperand.preconditionAddPre();
		if(this.rightOperand!=null)
			this.rightOperand.preconditionAddPre();
	}
	@Override
	public void postconditionAddPre()
	{
		if(this.operator.equals("||")||this.operator.equals("&&"))
		{
		//	System.out.println("testOperator:"+this.operator);
		//	if(this.leftOperand!=null)
			this.leftOperand.postconditionAddPre();
			//if(this.rightOperand!=null)
			this.rightOperand.postconditionAddPre();
		}
		else if(this.operator.equals("="))
		{
			this.addpostpre(this.rightOperand);
		}
	}
	public void addpostpre(CLGConstraint node)
	{
		if(node instanceof CLGOperatorNode)
		{
				addpostpre(((CLGOperatorNode)node).getLeftOperand());
				addpostpre(((CLGOperatorNode)node).getRightOperand());
		}
		else if(node instanceof CLGVariableNode)
		{
			((CLGVariableNode)node).postconditionAddPre();;
		}
	}
}
