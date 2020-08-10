package ccu.pllab.tcgen.TestCase;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ccu.pllab.tcgen.pathCLP2data.ECLiPSe_CompoundTerm;

public class TestDataClassLevel {
		private List<ECLiPSe_CompoundTerm> solution;

		private String className;
		private ArrayList<Object> methodName = new ArrayList<Object>();
		private ArrayList<Object> obj_pre = new ArrayList<Object>();
		private ArrayList<Object> arg_pre = new ArrayList<Object>();
		private ArrayList<Object> obj_post = new ArrayList<Object>();
		private ArrayList<Object> arg_post = new ArrayList<Object>();
		private ArrayList<Object> ret_val = new ArrayList<Object>();
		private int pathId;
		private int testDataID;

		private boolean isConstructor;
		private boolean invalidated;
		private String retType;
  
		//---714
		public TestDataClassLevel(String className,ArrayList<String> methodName, int pathid, int testDataID, List<ECLiPSe_CompoundTerm> solution) {
			this.className=className; 
			//System.out.println(" in td "+methodName+" "+methodName.size());
			for(int i=0;i<methodName.size();i++){
				if(methodName.get(i)!=null || !methodName.get(i).isEmpty()){
					this.methodName.add(methodName.get(i));
				}
			}
			this.pathId = pathid;
			this.testDataID = testDataID;
			this.solution = solution;
			
			ArrayList<Object> saveObj = new ArrayList<Object>();
			for(int i=0;i<solution.size();i++){
				if(solution.get(i).getTerm() instanceof LinkedList){ //[[1],[2],[3]]
					LinkedList link = (LinkedList)solution.get(i).getTerm() ;
					for(int i1=0;i1<link.size();i1++){
						saveObj = new ArrayList<Object>();
						if(link.get(i1) instanceof LinkedList){
							LinkedList link1 = (LinkedList)link.get(i1);
							for(int i2=0;i2<link1.size();i2++){
								saveObj.add(link1.get(i2));
							}
							if(i==0) this.obj_pre.add(saveObj);
							else if(i==1) this.arg_pre.add(saveObj);
							else if(i==2) this.obj_post.add(saveObj);
							else if(i==3) this.arg_post.add(saveObj);
							else if(i==4) this.ret_val.add(saveObj);
						}
						else{ 
							if(i==0) this.obj_pre.add(saveObj);
							else if(i==1) this.arg_pre.add(saveObj);
							else if(i==2) this.obj_post.add(saveObj);
							else if(i==3) this.arg_post.add(saveObj);
							else if(i==4) this.ret_val.add(saveObj);
						}
					}
				}
			}
			System.out.println(" 621sol \n"+solution+"\n"+this.obj_pre +"\n"+this.arg_pre
					+"\n"+this.obj_post+"\n"+this.arg_post+"\n"+this.ret_val);
		}
		public String getTestDataName() {
			return this.className+"_"+this.methodName;
		}
		public String getClassName(){
			return this.className;
		}
		public ArrayList<Object> getMethodName(){
			return this.methodName;
		}
		public String getTestDataID() {
			return this.pathId + "_" + this.testDataID;
		}

		public String toString() {
			return "["+this.obj_pre +", "+this.arg_pre+", "+this.obj_post+", "+this.arg_post+", "+this.ret_val+"]";
		}

		public ArrayList<Object> getObjPre() {
			return this.obj_pre;
		}

		public ArrayList<Object> getArgPre() {
			return this.arg_pre;
		}

		public ArrayList<Object> getObjPost() {
			return this.obj_post;
		}

		public ArrayList<Object> getArgPost() {
			return this.arg_post;
		}

		public ArrayList<Object> getRetVal() {
			return this.ret_val;
		}
	}
