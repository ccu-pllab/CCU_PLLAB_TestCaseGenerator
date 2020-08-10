package ccu.pllab.tcgen.clg2path;
 

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg.ConstraintNode;
import ccu.pllab.tcgen.clg.EndNode;
import ccu.pllab.tcgen.libs.node.INode;
import ccu.pllab.tcgen.libs.pivotmodel.Model;

public class FeasiblePathFinder {
	private CoverageCriterion criterion;
	Queue<List<CLGNode>> path_queue;
	private Model model;

	public FeasiblePathFinder(CoverageCriterion criterion, CLGNode graph, Model model) {
		this.criterion = criterion;
		this.model = model;
		criterion.analysisTagetGraph(graph);
		path_queue = new LinkedList<List<CLGNode>>();
		List<CLGNode> one_node_path = new ArrayList<CLGNode>();
		one_node_path.add(graph);
		path_queue.add(one_node_path);
	}

	public Path getNextPath() {
		while (path_queue.size() > 0 && !criterion.meetRequirement()) {
			
			List<CLGNode> path = path_queue.remove();
			if (isCompletePath(path)) {
				List<CLGNode> copy_node_list = new ArrayList<CLGNode>();
				for (CLGNode node : path) {
					copy_node_list.add(node.clone());
				}
				return new Path(copy_node_list, this.model);
			}

			for (INode child : path.get(path.size() - 1).getNextNodes()) {
				List<CLGNode> clone_path = new ArrayList<CLGNode>();
				clone_path.addAll(path);
				clone_path.add((CLGNode) child);
				path_queue.add(clone_path);
			}
		}
		return null;
	}

	private boolean isCompletePath(List<CLGNode> path) {
		return path.get(path.size() - 1) instanceof EndNode;
	}
}
