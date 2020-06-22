package ccu.pllab.tcgen.AbstractCLG;


import java.util.ArrayList;

public class CLGEndNode extends CLGNode{
	/**
	 * 訪問過的點，以ArrayList型態存放
	 */
	private static ArrayList visted = new ArrayList();

	public CLGEndNode() {
		super();
	}
	/**
	 * 
	 */
	public String toGetImgInfo(){
		String result ="";	
		result += (this.getId() + " " + ("[style=filled, fillcolor=black, shape=\"doublecircle\", label=\"\", fixedsize=true, width=.2, height=.2]"+ "\n"));
		return result;
	}
	/**
	 * 回傳"."
	 */
	@Override
	public String toCLPInfo() {
		return ".";
	}
	/**
	 * 
	 */
	@Override
	public ArrayList genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters, ArrayList localParameters, String result) {
		ArrayList<ArrayList<String>> clp = new ArrayList();
		
		if (visted.contains(this.getId()) != true) {
			visted.add(this.getId());
			ArrayList a = new ArrayList();
			a.add("999_999");
			a.add(className + methodName + "_endNode(ObjPre, ArgPre, ObjPost, ArgPost, Result, Exception):- \n");
			a.add("	Exception=[].\n");
			clp.add(a);
		}		
		localParameters.clear();
		return clp;
	}
	
//	@Override
//	public String genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters) {
//		String CLP = "";
//		
//		if (visted.contains(this.getId()) != true) {
//			visted.add(this.getId());
//		}
//		return CLP;
//	}
	/**
	 * 回傳"(CLGEndNode)"
	 */
	public String toString(){
		return "(CLGEndNode)";
	}
}
