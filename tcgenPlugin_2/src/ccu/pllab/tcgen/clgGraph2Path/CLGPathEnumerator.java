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

//	test �N�T�w���i�檺���|�����b���|���C�ɧR���A�o�˭�h�W���F�Ĥ@�����i��~�A�N���|���ͦh�l�����i����|
	public CLGPath next(List<List<CLGNode>> SIPNodesList) {
		while (path_queue.size() > 0) {
			//���Xqueue���@�����|�A�P�_�O�_��������|
			LinkedList<CLGNode> path = path_queue.poll();
			
			if (isCompletePath(path)) {				
				return new CLGPath(path);
			}
			
			//���X�U�@�I�[�Jqueue��
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
//						�󴫸��|����ɭp�ƭ��m
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
	
	//����A�Hhashtable���s��A�[�t�w���h�����i��檺�t��
	public CLGPath nextByHashtable(List<List<CLGNode>> SIPNodesList, Set<List<String>> conflictConstraints, Boolean isFeasible, Boolean skipSIPPart) {
//		pre-removal infeasible path
//		if(!isFeasible && skipSIPPart == false) {
//			preremovalInfeasiblePath(SIPNodesList, conflictConstraints);
//		}
//		���|���C
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
//				���X�L�{���N���I�m�JHashtable���ت�
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
	
	//�h����C�����|
	public void removePathQueue(Queue<LinkedList<CLGNode>> tempQueueRemove) {
		this.path_queue.removeAll(tempQueueRemove);
	}
	
//	�s���u���j�M�A���C���|
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
