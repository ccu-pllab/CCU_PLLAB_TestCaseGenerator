package ccu.pllab.tcgen.clgGraph2Path;

  
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;

import ccu.pllab.tcgen.AbstractCLG.CLGConnectionNode;
import ccu.pllab.tcgen.AbstractCLG.CLGEndNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;

public class CLGPathEnumerator {
	private Queue<LinkedList<CLGNode>> path_queue;
	private Boolean found;
	public CLGPathEnumerator() {
	}


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

}
