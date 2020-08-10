package ccu.pllab.tcgen.clg2path;
 

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import ccu.pllab.tcgen.facade.FacadeConfig;
import ccu.pllab.tcgen.libs.pivotmodel.Association;
import ccu.pllab.tcgen.libs.pivotmodel.AssociationEnd;
import ccu.pllab.tcgen.libs.pivotmodel.Model;

public class AEMCriterion implements UMLCoverageCriterion {
	private List<Association> ascs;

	@Override
	public void analysisModel(Model model) {
		this.ascs = model.getAssociations();
	}

	@Override
	public List<JSONObject> getCardinality(JSONObject config) throws JSONException {
		List<JSONObject> clone_cfg_list = new ArrayList<JSONObject>();
		for (Association asc : this.ascs) {
			List<Integer[]> role1Set = calculateMultiplexitySet(asc.getRoleList().get(0));
			List<Integer[]> role2Set = calculateMultiplexitySet(asc.getRoleList().get(1));
			for (Integer[] role1SetItem : role1Set) {
				for (Integer[] role2SetItem : role2Set) {
					AssociationEnd asc1End = new AssociationEnd();
					asc1End.setLower(role1SetItem[0]);
					asc1End.setUpper(role1SetItem[1]);
					asc1End.setType(asc.getRoleList().get(0).getType());
					asc1End.setName(asc.getRoleList().get(0).getName());

					AssociationEnd asc2End = new AssociationEnd();
					asc2End.setLower(role2SetItem[0]);
					asc2End.setUpper(role2SetItem[1]);
					asc2End.setType(asc.getRoleList().get(1).getType());
					asc2End.setName(asc.getRoleList().get(1).getName());
					Association new_asc = new Association(asc.getName(), asc1End, asc2End);
					JSONObject new_obj_config = new JSONObject();
					new_obj_config.put(new_asc.getRoleList().get(0).getType(), new JSONObject("{\"" + FacadeConfig.CFG_RANGE_INSNS + "\":\"" + new_asc.getRoleList().get(0).getLower() + ".."
							+ new_asc.getRoleList().get(0).getUpper() + "\"}"));
					new_obj_config.put(asc.getRoleList().get(1).getType(), new JSONObject("{\"" + FacadeConfig.CFG_RANGE_INSNS + "\":\"" + new_asc.getRoleList().get(1).getLower() + ".."
							+ new_asc.getRoleList().get(1).getUpper() + "\"}"));
					JSONObject clone_cfg = new JSONObject(config.toString());
					clone_cfg.put("objects", new_obj_config);
					clone_cfg_list.add(clone_cfg);
				}
			}
		}
		return clone_cfg_list;
	}

	private static List<Integer[]> calculateMultiplexitySet(AssociationEnd associationEnd) {
		List<Integer[]> result = new ArrayList<Integer[]>();
		if (associationEnd.getLower() == associationEnd.getUpper()) {
			result.add(new Integer[] { associationEnd.getLower(), associationEnd.getLower() });
		} else {
			if (associationEnd.getUpper() < associationEnd.getLower()) {
				result.add(new Integer[] { associationEnd.getLower(), associationEnd.getLower() });
				result.add(new Integer[] { -1, -1 });
			} else if (associationEnd.getUpper() == associationEnd.getLower() + 1) {
				result.add(new Integer[] { associationEnd.getLower(), associationEnd.getLower() });
				result.add(new Integer[] { associationEnd.getUpper(), associationEnd.getUpper() });
			} else {
				result.add(new Integer[] { associationEnd.getLower(), associationEnd.getLower() });
				result.add(new Integer[] { associationEnd.getLower() + 1, associationEnd.getUpper() - 1 });
				result.add(new Integer[] { associationEnd.getUpper(), associationEnd.getUpper() });
			}
		}

		return result;
	}

	@Override
	public boolean meetRequirement() {
		// TODO Auto-generated method stub
		return false;
	}

}
