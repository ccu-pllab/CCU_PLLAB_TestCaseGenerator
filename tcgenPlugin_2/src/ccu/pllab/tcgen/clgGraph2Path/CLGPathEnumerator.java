package ccu.pllab.tcgen.clgGraph2Path;

  
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;

import ccu.pllab.tcgen.AbstractCLG.CLGConnectionNode;
import ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode;
import ccu.pllab.tcgen.AbstractCLG.CLGEndNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;

public class CLGPathEnumerator {
	private Queue<LinkedList<CLGNode>> path_queue;
	private Boolean found;
	//clg hashtable
	private Hashtable<CLGNode, String> CLGht = new Hashtable<CLGNode, String>();
	
	public CLGPathEnumerator() {
	}

//	test 將確定不可行的路徑直接在路徑條列時刪除，這樣原則上除了第一次不可行外，將不會產生多餘的不可行路徑
	public CLGPath next(List<List<CLGNode>> SIPNodesList) {
		while (path_queue.size() > 0) {
			//取出queue中一條路徑，判斷是否為完整路徑
			LinkedList<CLGNode> path = path_queue.poll();
			
			if (isCompletePath(path)) {				
				return new CLGPath(path);
			}
			
			//取出下一點加入queue中
			for (CLGNode n : path.peekLast().getSuccessor()) {
				LinkedList<CLGNode> clone_path = new LinkedList<CLGNode>();
				clone_path.addAll(path);
				clone_path.add(n);
				path_queue.add(clone_path);
			}
			
			if(SIPNodesList != null) {
				Queue<LinkedList<CLGNode>> tempQueueRemove = new LinkedList<>();
				List<List<CLGNode>> tempSIPRemove = new ArrayList<>();
				
				for(List<CLGNode> SIPNodes : SIPNodesList) {
					int SIPNodesSize = SIPNodes.size();
					int equalNodes = 0;
					
					for(LinkedList<CLGNode> unfinishPath : path_queue) {
//						更換路徑比較時計數重置
//						equalNodes = 0;
//						
//						for(int i=0; i<SIPNodesSize; i++) {
//							if(unfinishPath.contains(SIPNodes.get(i))) {
//								equalNodes++;
//							}
//						}
//						
//						if(equalNodes == SIPNodesSize) {
//							tempQueueRemove.add(unfinishPath);
//							tempSIPRemove.add(SIPNodes);
//						}
//						
						if(unfinishPath.containsAll(SIPNodes)) {
							tempQueueRemove.add(unfinishPath);
							tempSIPRemove.add(SIPNodes);
						}
					}
				}
				path_queue.removeAll(tempQueueRemove);
				SIPNodesList.removeAll(tempSIPRemove);
			}
		}
		return null;
	}
	
	//實驗，以hashtable做存放，加速預先去除不可實行的速度
	public CLGPath nextByHashtable(List<List<CLGNode>> SIPNodesList, Set<List<String>> conflictConstraints, Boolean isFeasible, Boolean skipSIPPart) {
//		pre-removal infeasible path
//		if(!isFeasible && skipSIPPart == false) {
//			preremovalInfeasiblePath(SIPNodesList, conflictConstraints);
//		}
//		路徑條列
		while (path_queue.size() > 0) {
			LinkedList<CLGNode> path = path_queue.poll();
			
			if (isCompletePath(path)) {				
				return new CLGPath(path);
			}
			
			for (CLGNode n : path.peekLast().getSuccessor()) {
				LinkedList<CLGNode> clone_path = new LinkedList<CLGNode>();
				clone_path.addAll(path);
				clone_path.add(n);
				path_queue.add(clone_path);
//				走訪過程中將結點置入Hashtable中建表
//				if(!CLGht.containsKey(n) && n instanceof CLGConstraintNode) {
//					CLGht.put(n, n.toCLPInfo() + ",");
//				}
			}
		}
		return null;
	}
	
	public void preremovalInfeasiblePath(List<List<CLGNode>> SIPNodesList, Set<List<String>> conflictConstraints) {
		Queue<LinkedList<CLGNode>> tempQueueRemove = new LinkedList<>();
		List<List<CLGNode>> tempSIPRemove = new ArrayList<>();
		
		for(LinkedList<CLGNode> unfinishPath : path_queue) {
//			SIP
			for(List<CLGNode> SIPNodes : SIPNodesList) {
				if(unfinishPath.containsAll(SIPNodes)) {
					tempQueueRemove.add(unfinishPath);
					tempSIPRemove.add(SIPNodes);
					break;
				}
			}
			
//			CCS
			for(List<String> subset : conflictConstraints) {
				int size = subset.size();
				int count = 0;
				for(String s : subset) {
					for(CLGNode node : unfinishPath) {
//						if(s.equals(CLGht.get(node))) {
//							count++;
//						}
						if(s.equals(node.toCLPInfo() + ",")) {
							count++;
						}
					}
				}
				if(count == size) {
					tempQueueRemove.add(unfinishPath);
				}
			}
		}
		path_queue.removeAll(tempQueueRemove);
		SIPNodesList.removeAll(tempSIPRemove);
	}
	
	//去除佇列中路徑
	public void removePathQueue(Queue<LinkedList<CLGNode>> tempQueueRemove) {
		this.path_queue.removeAll(tempQueueRemove);
	}
	
//	廣度優先搜尋，條列路徑
	public CLGPath next() {
		while (path_queue.size() > 0) {
			LinkedList<CLGNode> path = path_queue.poll();
			if (isCompletePath(path)) {				
				return new CLGPath(path);
			}
			
			for (CLGNode n : path.peekLast().getSuccessor()) {
				LinkedList<CLGNode> clone_path = new LinkedList<CLGNode>();
				clone_path.addAll(path);
				clone_path.add(n);
				path_queue.add(clone_path);
			}
		}
		return null;
	}
	
	public boolean hasNext()
	{
		if(path_queue.size() > 0)
			return true;
		return false;
	}
	
	public List<CLGNode> filterConstraintNode(List<CLGNode> path) {
		ArrayList<CLGNode> nodeList = new ArrayList<CLGNode>();
		if (path != null) {
			for (CLGNode n : path) {
				if (!(n instanceof CLGConnectionNode))
					nodeList.add(n);
			}
			for (CLGNode n : nodeList) {
				System.out.println(n.toString());
			}
		}
		return nodeList;

	}


	public void init(CLGGraph graph) {
		path_queue = new LinkedList<LinkedList<CLGNode>>();
		LinkedList<CLGNode> one_node_path = new LinkedList<CLGNode>();
		one_node_path.add(graph.getStartNode());
		path_queue.offer(one_node_path);
	}

	public boolean isCompletePath(List<CLGNode> path) {
		return path.get(path.size() - 1) instanceof CLGEndNode;
	}


	public Queue<LinkedList<CLGNode>> getQueue() {
		return this.path_queue;
		
	}
	
	public Hashtable<CLGNode, String> getCLGht() {
		return this.CLGht;
	}

}
