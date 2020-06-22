package ccu.pllab.tcgen.clg2path;
 

import java.util.List;

import ccu.pllab.tcgen.clg.CLGNode;

public interface CoverageCriterion {

	void analysisTagetGraph(CLGNode graph);

	void addFeasiblePath(List<CLGNode> path);

	void addInfeasiblePath(List<CLGNode> path);

	boolean isVisitedInfeasiblePath(List<CLGNode> path);

	boolean isVisitedFeasiblePath(List<CLGNode> path);

	boolean meetRequirement();
}
