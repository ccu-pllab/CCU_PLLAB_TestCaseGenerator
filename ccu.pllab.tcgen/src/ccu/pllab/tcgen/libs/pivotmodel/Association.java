package ccu.pllab.tcgen.libs.pivotmodel;

 
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * 
 {
 "name":"instruct",
 "roleList":[{"name":"student","type":"Student"},{"name":"instructor","type":"Teacher"}]
 }
 */
public class Association {
	ArrayList<AssociationEnd> mRoleList;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mRoleList == null) ? 0 : mRoleList.hashCode());
		result = prime * result + ((sName == null) ? 0 : sName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Association other = (Association) obj;
		if (mRoleList == null) {
			if (other.mRoleList != null)
				return false;
		} else if (!mRoleList.equals(other.mRoleList))
			return false;
		if (sName == null) {
			if (other.sName != null)
				return false;
		} else if (!sName.equals(other.sName))
			return false;
		return true;
	}

	String sName;

	public Association(JSONObject ascObj) {
		mRoleList = new ArrayList<AssociationEnd>();

		ParseAscInfo(ascObj);
	}

	public Association(String json) {
		mRoleList = new ArrayList<AssociationEnd>();

		if (!ParseAscInfo(json)) {
			throw new IllegalStateException("Parsing assocation info error!!");
		}
	}

	public Association(String name, AssociationEnd ascEnd1, AssociationEnd ascEnd2) {
		this.sName = name;
		mRoleList = new ArrayList<AssociationEnd>();
		mRoleList.add(ascEnd1);
		mRoleList.add(ascEnd2);
	}

	public ArrayList<AssociationEnd> getRoleList() {
		return mRoleList;
	}

	public String getName() {
		return sName;
	}

	private boolean ParseAscInfo(JSONObject ascObj) {
		sName = ascObj.optString("name");
		// role list
		mRoleList = new ArrayList<AssociationEnd>();
		JSONArray role_list_array = ascObj.optJSONArray("roleList");
		for (int i = 0; i < role_list_array.length(); i++) {
			JSONObject role_obj = role_list_array.optJSONObject(i);
			AssociationEnd attr = new AssociationEnd();
			attr.setName(role_obj.optString("name"));
			attr.setType(role_obj.optString("type"));
			attr.setUpper(Integer.parseInt(role_obj.optString("upper")));
			attr.setLower(Integer.parseInt(role_obj.optString("lower")));
			attr.setUnique(role_obj.optString("unique"));
			mRoleList.add(attr);
		}

		return true;
	}

	private boolean ParseAscInfo(String json) {
		boolean ret = true;
		try {
			JSONArray array = new JSONArray(json);
			JSONObject ascObj = array.getJSONObject(0);

			ret = ParseAscInfo(ascObj);

		} catch (JSONException pe) {
			pe.printStackTrace();
			ret = false;
		}

		return ret;
	}

	public void setRoleList(ArrayList<AssociationEnd> mRoleList) {
		this.mRoleList = mRoleList;
	}

	public void setName(String sName) {
		this.sName = sName;
	}

	@Override
	public String toString() {
		String result = "";
		result += this.getName() + "[";
		result += StringUtils.join(this.mRoleList, ",");
		result += "]";
		return result;
	}

	public boolean isUnique() {
		AssociationEnd end1 = this.getRoleList().get(0);
		AssociationEnd end2 = this.getRoleList().get(1);
		if (end1.getLower() == 1 && end1.getUpper() == 1 && end2.getLower() == 1 && end2.getUpper() == 1) {
			return true;
		}
		return false;
	}
}
