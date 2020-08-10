package ccu.pllab.tcgen.TestCase;


import java.util.ArrayList;
import java.util.List;

import ccu.pllab.tcgen.pathCLP2data.ECLiPSe_CompoundTerm;
import scala.collection.generic.BitOperations.Int;



public class TestData {
	private List<ECLiPSe_CompoundTerm> solution;

	private String className;
	private String methodName;
	private String obj_pre;
	private String arg_pre;
	private String obj_post;
	private String arg_post;
	private String ret_val;
	private String exception;
	private int pathId;
	private int testDataID;
	private int arrayID=0;
	private boolean isConstructor;
	private boolean invalidated;
	private String retType;

	public TestData(String className,String methodName, int pathid, int testDataID,boolean isConstructor,String retType, List<ECLiPSe_CompoundTerm> solution) {
		this.className=className;
		this.methodName=methodName.toLowerCase().charAt(0)+methodName.substring(1);
		this.pathId = pathid;
		this.testDataID = testDataID;
		this.solution = solution;
		this.obj_pre = solution.get(0).toString();//.substring(1, solution.get(0).toString().length()-1);
		this.arg_pre = solution.get(1).toString();//.substring(1, solution.get(1).toString().length()-1);
		this.obj_post = solution.get(2).toString();//.substring(1, solution.get(2).toString().length()-1);
		this.arg_post = solution.get(3).toString();//.substring(1, solution.get(3).toString().length()-1);
		this.ret_val = solution.get(4).toString();//.substring(1, solution.get(4).toString().length()-1);
		this.exception=solution.get(5).toString();

		this.isConstructor=isConstructor;
		this.invalidated = false;
		if(this.exception.contains("Exception")){
			this.invalidated= true;
		}
		this.retType=retType;
	}
	
	

	public TestData(String className,String methodName, int pathid, int testDataID,int arrayID,boolean isConstructor,String retType,String objpre,String argpre,String objpost,String argpost,String retval)
	{
		this.className=className;
		this.methodName=methodName.toLowerCase().charAt(0)+methodName.substring(1);
		this.pathId = pathid;
		this.testDataID = testDataID;
		this.arrayID=arrayID;
		this.obj_pre = objpre;//.substring(1, solution.get(0).toString().length()-1);
		this.arg_pre = argpre;//.substring(1, solution.get(1).toString().length()-1);
		this.obj_post = objpost;//.substring(1, solution.get(2).toString().length()-1);
		this.arg_post = argpost;//.substring(1, solution.get(3).toString().length()-1);
		this.ret_val = retval;//.substring(1, solution.get(4).toString().length()-1);
	/*if(ret_val.contains("[["))
		ret_val=ret_val.replaceAll("[[", "[");
	if(ret_val.contains("]]"))
		ret_val=ret_val.replaceAll("]]", "]");*/
		this.isConstructor=isConstructor;
		this.invalidated = false;
		if(this.ret_val.contains("Exception")){
			this.invalidated= true;
		}
		this.retType=retType;
	}
	
	
	public String getTestDataName() {
		return this.className+"_"+this.methodName;
	}
	public String getClassName(){
		return this.className;
	}
	public String getMethodName(){
		return this.methodName;
	}
	public String getTestDataID() {
		//if(this.arrayID==0)
		//{
		return this.pathId + "_" + this.testDataID;
		//}
		//else
		//{
		//	return this.pathId + "_" + this.testDataID+"_"+(this.arrayID+1);
		//}
	}

	public String toString() {
		return "["+this.obj_pre +", "+this.arg_pre+", "+this.obj_post+", "+this.arg_post+", "+this.ret_val+","+this.exception+"]";
	}

	public String getObjPre() {
		return this.obj_pre;
	}

	public void setObjPre(String objpre) {
		this.obj_pre=objpre;
	}
	
	
	public void setRetVal(String retval) {
		this.ret_val=retval;
	}
	public void setArgPre(String arg_pre) {
		this.arg_pre=arg_pre;
	}
	public String getArgPre() {
		return this.arg_pre;
	}
  
	public String getException() {
		String exe="[]";
		if(this.exception!=null && this.exception.length()>0)
			exe=this.exception;
		return exe;
	}
	
	public String getObjPost() {
		return this.obj_post;
	}
	
	public void setObjPost(String objpost) {
		this.obj_post=objpost;
	}
	public void setArgPost(String arg_post) {
		this.arg_post=arg_post;
	}
	public String getArgPost() {
		return this.arg_post;
	}

	public String getRetVal() {
		return this.ret_val;
	}
	
	public int getPathId()
	{
		return this.pathId;
	}
	public int getTestDataId()
	{
		return this.testDataID;
	}
	public int getArrayId()
	{
		return this.arrayID;
	}
	
	public boolean isConstructor(){
		return this.isConstructor;
	}
	public boolean isInvalidated(){
		return this.invalidated;
	}
	public String getRetType(){
		return this.retType;
	}
	public void setIsInvalid(boolean state){
		this.invalidated = state;
	}
}
