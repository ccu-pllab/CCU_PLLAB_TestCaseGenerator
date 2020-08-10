package ccu.pllab.tcgen.clg2path;

 
public class CriterionFactory {
	private CriterionFactory() {

	}

	public enum Criterion {
		dc, dcc, mcc,dcdup,dccdup,mccdup;
	}

	static public CoverageCriterion getCLGCoverage(Criterion criterion) {
		if (criterion.equals(CriterionFactory.Criterion.dc) || criterion.equals(CriterionFactory.Criterion.dcc) || criterion.equals(CriterionFactory.Criterion.mcc)|| criterion.equals(CriterionFactory.Criterion.dcdup)|| criterion.equals(CriterionFactory.Criterion.dccdup)|| criterion.equals(CriterionFactory.Criterion.mccdup)) {
			return new BranchCriterion();
		} else {
			return null;
		}
	}

	static public CoverageCriterion getAllDecisionConditionCriterion() {
		return new BranchCriterion();
	}

	static public UMLCoverageCriterion getAEMCriterion() {
		return new AEMCriterion();
	}
}
