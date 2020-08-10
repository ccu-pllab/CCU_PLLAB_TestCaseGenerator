package ccu.pllab.tcgen.AbstractCLG;


import java.util.ArrayList;

import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;

public class CLGConstraintNode extends CLGNode{
	private static ArrayList visted = new ArrayList();
	private static int xlabel_count=0;
	private int xlabel_id;
	private CLGConstraint constraint;
	public CLGConstraintNode(CLGConstraint constraint){
		super();
		this.constraint=constraint;
		this.xlabel_id=xlabel_count++;
	}
	public CLGConstraint getConstraint(){
		return this.constraint;
	}
	

	private void setXlabelID(int xlabelID) {
		this.xlabel_id = xlabelID;
	}
	@Override
	public String toGetImgInfo(){
		String result ="";	
		result += (this.getId() + " " + String.format("[shape=\"box\", label=\"%s\",style = \"filled\",fillcolor = \"yellow\",xlabel=\"[%d]\"]"+ "\n",this.constraint.getImgInfo(),this.xlabel_id));
		return result;
	}
	@Override
	public String toCLPInfo() {
		String result="";
		result += constraint.getCLPInfo();
		return result;
	}
	public int getXLabelId(){
		return this.xlabel_id;
	}
	public String toString(){
		return "["+this.getXLabelId()+"]";
	}
	public static void reset(){
		xlabel_count=1;
	}
	@Override
	public CLGNode clone() {
		CLGConstraintNode node = new CLGConstraintNode(this.constraint.clone());
		node.setXlabelID(this.xlabel_id);
		return node;
	}

	@Override
	public ArrayList genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters, ArrayList localParameters, String result) {
		CLGNode nextNode = this.getSuccessor().get(0);
		ArrayList attributes_pre = new ArrayList();
		ArrayList arg_pre = new ArrayList();
		ArrayList<ArrayList<String>> clp = new ArrayList();
		ArrayList newLocalParameters = new ArrayList();
		String newClp ="";

		for(int i = 0; i < classAttributes.size(); i++) {		
		//	attributes_pre.add(classAttributes.get(i)+"_0");
			attributes_pre.add(classAttributes.get(i)+"_pre");
		}
		for(int j = 0; j < methodParameters.size(); j++) {		
		//	arg_pre.add(methodParameters.get(j)+"_0");
			arg_pre.add(methodParameters.get(j)+"_pre");
		}
		
		/*判斷有無local*/
		for(int k = 0; k<this.getConstraint().getLocalVariable().split(",").length; k++) {
			if(this.getConstraint().getLocalVariable().split(",")[k].equals("") != true) {
				if(localParameters.contains(this.getConstraint().getLocalVariable().split(",")[k]) != true)
					localParameters.add(this.getConstraint().getLocalVariable().split(",")[k]);
			}
		}
		//System.out.println(localParameters);
		
		if (visted.contains(this.getId()) != true) {
			visted.add(this.getId());
			clp.add(new ArrayList());
			clp.get(0).add("0");
			
			newClp = this.toCLPInfo();
			
			/*fix it=it+1 */		
			if(newClp.contains("It#=(It+1)")) {
				newClp = newClp.replace("It#=(It+1)" , "It_1#=(It+1)");
				if(localParameters.contains("It_1") !=true) {
					localParameters.set(localParameters.indexOf("It"), "It_1");
				}
			}
			
			/*fix it to  It  bug*/
			newClp = newClp.replaceAll("=it" , "=It");
			
			/*下一個還是CLGConstraintNode用逗號隔開，表示and*/
			if(nextNode.getClass().equals(this.getClass())) {
				clp.get(0).add("	"+newClp + ", \n");
			}
			/*判斷是否要產生下一次呼叫*/
			else if(nextNode.getClass().equals(CLGConnectionNode.class)) {
				clp.get(0).add("	"+newClp + ", \n");
				clp.get(0).add("	" + className  + methodName + "_node_" + ((CLGConnectionNode )nextNode).getConnectionId()+"("+ attributes_pre +","+ arg_pre +","+ classAttributes +","+ methodParameters +", "+ result +", Exception, "+ localParameters +"). \n");
			}
			/*遇到EndNode表示predicate結束*/
			else {
				clp.get(0).add("	"+newClp + ", \n");
				clp.get(0).add("	"+className + methodName + "_endNode("+ attributes_pre +","+ arg_pre +","+ classAttributes +","+ methodParameters +", "+ result +", Exception). \n");
			}
			clp.addAll(nextNode.genMethodCLP(className, methodName, classAttributes, methodParameters, localParameters, result));
		}
		return clp;
	}
	
//	@Override
//	public String genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters) {
//		String CLP = "";
//		CLGNode nextNode = this.getSuccessor().get(0);
//		ArrayList attributes_pre = new ArrayList();
//		ArrayList arg_pre = new ArrayList();
//
//
//		for(int i = 0; i < classAttributes.size(); i++) {		
//			attributes_pre.add(classAttributes.get(i)+"_0");
//		}
//		for(int j = 0; j < methodParameters.size(); j++) {		
//			arg_pre.add(methodParameters.get(j)+"_0");
//		}
//		
//		if (visted.contains(this.getId()) != true) {
//			visted.add(this.getId());
//			/*下一個還是CLGConstraintNode用逗號隔開，表示and*/
//			if(nextNode.getClass().equals(this.getClass())) {
//				CLP = CLP + "	"+this.toCLPInfo() + ", \n";
//			}
//			/*判斷是否要產生下一次呼叫*/
//			else if(nextNode.getClass().equals(CLGConnectionNode.class) && nextNode.getSuccessor().get(0).getClass().equals(CLGConstraintNode.class)) {
//				CLP = CLP + "	"+this.toCLPInfo() + ", \n";
//				CLP = CLP +"	" +className + "_" + methodName + "_node_" + ((CLGConnectionNode )nextNode).getConnectionId()+"("+ attributes_pre +","+ arg_pre +","+ classAttributes +","+ methodParameters +", Result). \n";
//			}
//			/*遇到EndNode表示predicate結束*/
//			else {
//				CLP = CLP + "	"+this.toCLPInfo() + ". \n";
//			}
//			CLP = CLP + nextNode.genMethodCLP(className, methodName, classAttributes, methodParameters);
//		}
//		return CLP;
//	}
}
