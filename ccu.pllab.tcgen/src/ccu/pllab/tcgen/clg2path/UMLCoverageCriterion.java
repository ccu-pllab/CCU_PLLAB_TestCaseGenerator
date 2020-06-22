package ccu.pllab.tcgen.clg2path;

 
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import ccu.pllab.tcgen.libs.pivotmodel.Model;

public interface UMLCoverageCriterion {
	void analysisModel(Model model);

	List<JSONObject> getCardinality(JSONObject config) throws JSONException;

	boolean meetRequirement();

}
