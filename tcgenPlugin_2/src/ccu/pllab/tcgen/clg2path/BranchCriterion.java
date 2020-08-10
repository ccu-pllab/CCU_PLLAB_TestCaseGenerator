package ccu.pllab.tcgen.clg2path;
 

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.libs.node.INode;

public class BranchCriterion implements CoverageCriterion {
	private Set<ImmutablePair<CLGNode, CLGNode>> all_branches;
	private HashSet<ImmutablePair<CLGNode, CLGNode>> visited_branches;
	private Set<List<CLGNode>> infeasible_path;
	private Set<List<CLGNode>> feasible_path;

	@Override
	public void analysisTagetGraph(CLGNode graph) {
		all_branches = findAllBranchs(graph);
		visited_branches = new HashSet<ImmutablePair<CLGNode, CLGNode>>();
		infeasible_path = new HashSet<List<CLGNode>>();
		feasible_path = new HashSet<List<CLGNode>>();
	}

	private Set<ImmutablePair<CLGNode, CLGNode>> findAllBranchs(CLGNode node) {
		Set<ImmutablePair<CLGNode, CLGNode>> branches = new HashSet<ImmutablePair<CLGNode, CLGNode>>();
		Queue<CLGNode> bfs_queue = new LinkedList<CLGNode>();
		Set<CLGNode> visited_nodes = new HashSet<CLGNode>();
		bfs_queue.add(node);

		while (bfs_queue.size() > 0) {

			final CLGNode current_node = bfs_queue.poll();
			if (visited_nodes.contains(current_node)) {
				continue;
			}
			visited_nodes.add(current_node);

			for (INode child : current_node.getNextNodes()) {
				final ImmutablePair<CLGNode, CLGNode> node_pair = new ImmutablePair<CLGNode, CLGNode>(current_node, ((CLGNode) child));
				branches.add(node_pair);
				bfs_queue.add((CLGNode) child);
			}
		}
		return branches;
	}

	@Override
	public boolean meetRequirement() {
		return visited_branches.containsAll(all_branches);
	}

	@Override
	public void addFeasiblePath(List<CLGNode> path) {
		for (int i = 0; i < path.size() - 1; i++) {
			final ImmutablePair<CLGNode, CLGNode> node_pair = new ImmutablePair<CLGNode, CLGNode>(path.get(i), path.get(i + 1));
			visited_branches.add(node_pair);
		}
		feasible_path.add(path);
	}

	@Override
	public void addInfeasiblePath(List<CLGNode> path) {
		infeasible_path.add(path);
	}

	@Override
	public boolean isVisitedInfeasiblePath(List<CLGNode> path) {
		return infeasible_path.contains(path);
	}

	@Override
	public boolean isVisitedFeasiblePath(List<CLGNode> path) {
		return feasible_path.contains(path);
	}
}
