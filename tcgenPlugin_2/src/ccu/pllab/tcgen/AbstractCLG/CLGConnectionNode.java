package ccu.pllab.tcgen.AbstractCLG;


import java.util.ArrayList;

public class CLGConnectionNode extends CLGNode {
	private static ArrayList visted = new ArrayList();
	private static int connection_count=1;
	private int connectionId;
	private String connectionName = "";

	public CLGConnectionNode() {
		super();
		this.connectionId=connection_count++;
	}

	public CLGConnectionNode(int nodeId) {
		super();
		this.connectionId = nodeId;
	}

	public void setConnectionId(int id) {//建瓏新增
		 this.connectionId=id;
	}
	
	
	public int getConnectionId() {
		return this.connectionId;
	}

	public void setConnectionName(String name) {
		this.connectionName = name;
	}

	public String getConnectionName() {
		return this.connectionName;
	}

	public String toGetImgInfo() {
		String result = "";
		if (connectionName.length() > 0) {
			result += (this.getId() + " "
					+ String.format("[shape=\"diamond\", label=\"(%d)_%s\", fixedsize=true, width=1, height=1]" + "\n",
							this.getConnectionId(), this.getConnectionName()));
		} else {
			result += (this.getId() + " "
					+ String.format("[shape=\"diamond\", label=\"\",xlabel=\"<%d>\",style = \"filled\",fillcolor = \"aquamarine\", fixedsize=true, width=.2, height=.2]" + "\n",this.getConnectionId()));
//			aquamarine
		}		
		
		return result;
	}
	public String toString(){
		return "<"+this.getConnectionId()+">";
	}
	@Override
	public String toCLPInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters, ArrayList localParameters, String result) {
		CLGNode nextNode ;
		ArrayList attributes_pre = new ArrayList();
		ArrayList arg_pre = new ArrayList();
		ArrayList local_pre = new ArrayList();
		ArrayList<ArrayList<String>> clp = new ArrayList();

		/*add pre*/
		for(int i = 0; i < classAttributes.size(); i++) {		
			attributes_pre.add(classAttributes.get(i)+"_pre");
		}
		for(int j = 0; j < methodParameters.size(); j++) {		
			arg_pre.add(methodParameters.get(j)+"_pre");
		}
		for(int k = 0; k < localParameters.size(); k++) {
			if(localParameters.get(k).equals("It_1")) {
				localParameters.set(k, "It");
			}
			local_pre.add(localParameters.get(k));
		}
		
		if (visted.contains(this.getId()) != true) {
			visted.add(this.getId());
			for(int i=0; i < this.getSuccessor().size(); i++) {
				nextNode = this.getSuccessor().get(i);
				/*下一個如果是連結點*/
				if(nextNode.getClass().equals(this.getClass())) {
					ArrayList connect = new ArrayList();
					connect.add(this.connectionId + "_" + i);
					connect.add(className + methodName + "_node_" + this.getConnectionId()+"("+ attributes_pre +","+ arg_pre +","+ classAttributes +","+ methodParameters +", Result, Exception, "+local_pre+"):- \n");
					connect.add("	" + className  + methodName + "_node_" + ((CLGConnectionNode )nextNode).getConnectionId()+"("+ attributes_pre +","+ arg_pre +","+ classAttributes +","+ methodParameters +", "+ result +", Exception, "+ localParameters +"). \n");
					clp.add(connect);
					clp.addAll(nextNode.genMethodCLP(className, methodName, classAttributes, methodParameters, localParameters, result));
				}
				/*下一個如果是結束點*/
				else if(nextNode.getClass().equals(CLGEndNode.class)) {
					ArrayList end = new ArrayList();
					end.add(this.connectionId + "_" + i);
					end.add(className + methodName + "_node_" + this.getConnectionId()+"("+ attributes_pre +","+ arg_pre +","+ classAttributes +","+ methodParameters +", Result, Exception, "+local_pre+"):- \n");
					end.add("	"+className + methodName + "_endNode(ObjPre, ArgPre, ObjPost, ArgPost, Result, Exception). \n");
					clp.add(end);
					clp.addAll(nextNode.genMethodCLP(className, methodName, classAttributes, methodParameters, localParameters, result));
				}
				else {
					ArrayList a = new ArrayList();
					a.add(this.connectionId + "_" + i);
					a.add(className + methodName + "_node_" + this.getConnectionId()+"("+ attributes_pre +","+ arg_pre +","+ classAttributes +","+ methodParameters +", "+result+", Exception, "+local_pre+"):- \n");
					clp.add(a);
					clp.addAll(nextNode.genMethodCLP(className, methodName, classAttributes, methodParameters, localParameters, result));
					/*移除空陣列*/
					for(int j=0; j < clp.size(); j++) {
						if(clp.get(j).isEmpty())
							clp.remove(j);
					}
					/*設定相同clp名稱*/
					for(int k=0; k < clp.size(); k++) {
						if(clp.get(k).get(0).equals("0")) {
							clp.get(k).set(0, this.connectionId + "_" + i);
						}
					}
				}
			}
		}
		else {
			localParameters.clear();
		}
		return clp;
	}
	
//	@Override
//	public String genMethodCLP(String className, String methodName, ArrayList classAttributes, ArrayList methodParameters) {
//		String CLP = "";
//		CLGNode nextNode ;
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
//			for(int i=0; i < this.getSuccessor().size(); i++) {
//				nextNode = this.getSuccessor().get(i);
//				/*下一個如果是連結點就直接遞迴*/
//				if(nextNode.getClass().equals(this.getClass())) {
//					//nextNode = nextNode.getSuccessor().get(0);
//					nextNode.genMethodCLP(className, methodName, classAttributes, methodParameters);
//				}
//				/*下一個如果是結束點就直接遞迴*/
//				else if(nextNode.getClass().equals(CLGEndNode.class)) {
//					nextNode.genMethodCLP(className, methodName, classAttributes, methodParameters);
//				}
//				else {
//					CLP = CLP + className + "_" + methodName + "_node_" + this.getConnectionId()+"("+ attributes_pre +","+ arg_pre +","+ classAttributes +","+ methodParameters +", Result):- \n";
//					CLP = CLP + nextNode.genMethodCLP(className, methodName, classAttributes, methodParameters);
//
//				}
//			}
//		}
//		return CLP;
//	}
	
	public static void reset(){
		connection_count=1;
	}
	
}
