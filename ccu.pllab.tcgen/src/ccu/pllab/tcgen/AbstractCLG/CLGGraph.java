package ccu.pllab.tcgen.AbstractCLG;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;

public class CLGGraph {

	private CLGNode startNode;
	private CLGNode endNode;
	private List<CLGNode> connectionNodes = new ArrayList<CLGNode>();

	private HashMap<Integer, CLGConstraintNode> constraintNodes = new HashMap<Integer, CLGConstraintNode>();

	public CLGGraph() {
		this.startNode = new CLGStartNode();
		this.endNode = new CLGEndNode();
		this.startNode.addSuccessor(this.endNode);
		this.endNode.addPredecessor(this.startNode);
	}

	public CLGGraph(int nums) {
		for (int i = 1; i < (nums + 3); i++) {
			connectionNodes.add(new CLGConnectionNode(i));
		}
		this.startNode = new CLGStartNode();
		this.endNode = new CLGEndNode();
		this.startNode.addSuccessor(this.getConnectionNode(1));
		//this.endNode.addPredecessor(this.getConnectionNode(2));
		this.endNode.addPredecessor(this.getConnectionNode(nums+2));
		for (int i = 1; i < (nums + 2); i++) {
			this.getConnectionNode(i).addSuccessor(this.getConnectionNode(i+1));
		}
		this.getConnectionNode(nums+2).addSuccessor(this.endNode);

	}

	public CLGGraph(CLGConstraint constraint) {
		CLGConstraintNode consNode = new CLGConstraintNode(constraint);
		this.startNode = new CLGStartNode();
		this.endNode = new CLGEndNode();
		this.startNode.addSuccessor(consNode);
		this.endNode.addPredecessor(consNode);

		this.constraintNodes.put(consNode.getXLabelId(), consNode);

	}

	public CLGNode getStartNode() {
		return this.startNode;
	}

	public CLGNode getEndNode() {
		return this.endNode;
	}

	private void setStartSuccessor(CLGNode node) {
		this.startNode.addSuccessor(node);
	}

	private void setEndPredecessor(CLGNode node) {
		this.endNode.addPredecessor(node);
	}

	private CLGNode getEndPredecessor() {
		return this.endNode.getPredecessor().get(0);
	}

	private CLGNode getStartSuccessor() {
		return this.startNode.getSuccessor().get(0);
	}

	public HashMap<Integer, CLGConstraintNode> getConstraintCollection() {
		return this.constraintNodes;
	}

	public CLGConstraintNode getConstraintNodeById(int consId) {
		return this.constraintNodes.get(consId);
	}

	public CLGConnectionNode getConnectionNode(int connId) {
		CLGNode node = null;
		for (CLGNode n : this.connectionNodes) {
			if (((CLGConnectionNode) n).getConnectionId() == connId) {
				node = n;
			}
		}
		return (CLGConnectionNode) node;
	}
	public List<CLGNode>getConnectionNode()
	{
		return this.connectionNodes;
	}
	public void InsertCompoundStateCLG(int nodeId,CLGGraph clgGraph) {
		//做外層CLG和內層CLG做連接，如果有混合狀態，直接改變內層的初始狀態/外層的結束狀態的連接節點，就是插入圖
		int externalId=((CLGConnectionNode)(this.getEndPredecessor())).getConnectionId()+1;
		while(this. getConnectionNode(externalId)!=null)
		{//to find external connection node max id
			externalId++;
		}
		int maxId=((CLGConnectionNode)(clgGraph.getEndPredecessor())).getConnectionId();
		for(int connId=((CLGConnectionNode)(clgGraph.getStartSuccessor())).getConnectionId();connId<=maxId;connId++)
		{
			if(clgGraph.getConnectionNode(connId)!=null)
			clgGraph.getConnectionNode(connId).setConnectionId(externalId++);
		}
		int size;
		for (size=0;size<this.connectionNodes.size();size++) {//找所有外層CLG的連接節點
			if (((CLGConnectionNode)this.connectionNodes.get(size)).getConnectionId() == nodeId) {//找到混合式的連接節點的ID
				for(int i=0;i<this.connectionNodes.get(size).getPredecessor().size();i++)
				{//處理外層CLG連接到此混合狀態的前面那些節點，使他們連接到內層CLG的初始狀態的連接節點
					this.connectionNodes.get(size).getPredecessor().get(i).removeSuccessor(this.connectionNodes.get(size));//所有連接到此混合狀態的節點都不要連接此混合狀態的連接節點
					this.connectionNodes.get(size).getPredecessor().get(i).addSuccessor(clgGraph.getStartNode().getSuccessor().get(0));//直接連到內層CLG的初始連接節點
					clgGraph.getStartSuccessor().removePredecessor(clgGraph.getStartNode());//內層CLG移除開始結點
					clgGraph.getStartSuccessor().addPredecessor(this.connectionNodes.get(size).getPredecessor().get(i));//初始連接節點連到外層CLG之前的狀態
				}
				for(int i=0;i<this.connectionNodes.get(size).getSuccessor().size();i++)
				{//與前者同理，只是處理內層結束連接節點
					this.connectionNodes.get(size).getSuccessor().get(i).removePredecessor(this.connectionNodes.get(size));
					this.connectionNodes.get(size).getSuccessor().get(i).addPredecessor(clgGraph.getEndNode().getPredecessor().get(0));
					clgGraph.getEndPredecessor().addSuccessor(this.connectionNodes.get(size).getSuccessor().get(i));
					clgGraph.getEndPredecessor().removeSuccessor(clgGraph.getEndNode());	
				}	
				break;
			}
		}
		this.connectionNodes.remove(size);//移除混合狀態
		maxId=((CLGConnectionNode)(clgGraph.getEndPredecessor())).getConnectionId();
		for(int connId=((CLGConnectionNode)(clgGraph.getStartSuccessor())).getConnectionId();connId<=maxId;connId++)
		{
			if(clgGraph.getConnectionNode(connId)!=null)
				this.connectionNodes.add(clgGraph.getConnectionNode(connId));
		}
		
		this.constraintNodes.putAll(clgGraph.getConstraintCollection());//合併
		
		
		for(int num=0;num<this.connectionNodes.size();num++)
		{
		//	System.out.println(this.connectionNodes.get(num).toGenImg2());
		}
	}
	
	
	public void graphAnd(CLGGraph clgGraph) {
		this.getEndNode().getPredecessor().get(0).addSuccessor(clgGraph.getStartNode().getSuccessor().get(0));
		clgGraph.getStartNode().getSuccessor().get(0).addPredecessor(this.getEndNode().getPredecessor().get(0));
		this.getEndNode().getPredecessor().get(0).removeSuccessor(this.getEndNode());
		clgGraph.getStartNode().getSuccessor().get(0).removePredecessor(clgGraph.getStartNode());

		this.endNode = clgGraph.getEndNode();

		this.constraintNodes.putAll(clgGraph.getConstraintCollection());
	}

	public void graphOr(CLGGraph clgGraph) {
		if (clgGraph.getStartNode().getSuccessor().get(0).getClass().equals("class ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode")) {
			CLGNode inputStartNode = clgGraph.getStartNode().getSuccessor().get(0);
			CLGNode inputEndNode = clgGraph.getEndNode().getPredecessor().get(0);
			inputStartNode.clearPredecessors();
			inputEndNode.clearSuccessors();
			this.getStartNode().getSuccessor().get(0).addSuccessor(inputStartNode);
			this.getEndNode().getPredecessor().get(0).addPredecessor(inputEndNode);
		} else {
			CLGNode connectionStart = new CLGConnectionNode();
			CLGNode connectionEnd = new CLGConnectionNode();
			this.connectionNodes.add(connectionStart);
			this.connectionNodes.add(connectionEnd);

			CLGNode thisStartNode = this.getStartSuccessor();
			CLGNode thisEndNode = this.getEndPredecessor();
			CLGNode inputStartNode = clgGraph.getStartNode().getSuccessor().get(0);
			CLGNode inputEndNode = clgGraph.getEndNode().getPredecessor().get(0);
			thisStartNode.clearPredecessors();
			thisEndNode.clearSuccessors();
			inputStartNode.clearPredecessors();
			inputEndNode.clearSuccessors();
			this.getStartNode().clearSuccessors();
			this.getEndNode().clearPredecessors();
			connectionStart.addSuccessor(thisStartNode);
			connectionStart.addSuccessor(inputStartNode);
			connectionEnd.addPredecessor(thisEndNode);
			connectionEnd.addPredecessor(inputEndNode);
			this.getStartNode().addSuccessor(connectionStart);
			this.getEndNode().addPredecessor(connectionEnd);
		}
		this.constraintNodes.putAll(clgGraph.getConstraintCollection());
	}

	public void graphClosure() {
		CLGNode connNode = new CLGConnectionNode();
		this.connectionNodes.add(connNode);
		CLGNode originalStartNode = this.getStartSuccessor();
		CLGNode originalEndNode = this.getEndPredecessor();
		originalStartNode.clearPredecessors();
		originalEndNode.clearSuccessors();
		connNode.addSuccessor(originalStartNode);
		originalEndNode.addSuccessor(connNode);
		this.getStartNode().clearSuccessors();
		this.getEndNode().clearPredecessors();
		this.setStartSuccessor(connNode);
		this.setEndPredecessor(connNode);
	}

	public void graphInsert(int source, CLGGraph clgGraph, int target) {
		CLGNode inputStartNode = clgGraph.getStartNode().getSuccessor().get(0);
		CLGNode inputEndNode = clgGraph.getEndNode().getPredecessor().get(0);
		if (clgGraph.getStartNode().equals(clgGraph.getEndPredecessor())) {
			inputStartNode.clearPredecessors();
			inputEndNode.clearSuccessors();
			this.getConnectionNode(source).addSuccessor(this.getConnectionNode(target));
			this.getConnectionNode(target).addPredecessor(this.getConnectionNode(source));
		} else {
			inputStartNode.clearPredecessors();
			inputEndNode.clearSuccessors();
			for (CLGNode node : connectionNodes) {
				if (((CLGConnectionNode) node).getConnectionId() == source) {
					node.addSuccessor(inputStartNode);
				}
				if (((CLGConnectionNode) node).getConnectionId() == target) {
					node.addPredecessor(inputEndNode);
				}
			}
		}
		this.constraintNodes.putAll(clgGraph.getConstraintCollection());
	}

	public Set<CLGEdge<CLGNode, CLGNode>> getAllBranches() {
		Set<CLGEdge<CLGNode, CLGNode>> branches = new HashSet<CLGEdge<CLGNode, CLGNode>>();
		Queue<CLGNode> bfs_queue = new LinkedList<CLGNode>();
		Set<CLGNode> visited_nodes = new HashSet<CLGNode>();
		bfs_queue.add(this.getStartNode());

		while (bfs_queue.size() > 0) {
			final CLGNode currentNode = bfs_queue.poll();
			if (visited_nodes.contains(currentNode)) {
				continue;
			}
			visited_nodes.add(currentNode);
			for (CLGNode sucessor : currentNode.getSuccessor()) {
				final CLGEdge<CLGNode, CLGNode> node_pair = new CLGEdge<CLGNode, CLGNode>(currentNode, sucessor);
				branches.add(node_pair);
				bfs_queue.add(sucessor);
			}
		}
		return branches;
	}

	public String graphDraw() {
		return this.startNode.toGenImg() + "}";
	}

	public void graphReset() {
		ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode.reset();
		ccu.pllab.tcgen.AbstractCLG.CLGConnectionNode.reset();
	}

}
