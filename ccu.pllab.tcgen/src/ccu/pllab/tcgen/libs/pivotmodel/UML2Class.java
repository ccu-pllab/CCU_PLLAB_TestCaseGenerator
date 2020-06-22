package ccu.pllab.tcgen.libs.pivotmodel;

 
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import ccu.pllab.tcgen.ast.Constraint;
import ccu.pllab.tcgen.libs.JsonUtils;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;

public class UML2Class extends Classifier {
	ArrayList<Attribute> mAttrList;
	List<Association> mAscList;
	List<UML2Operation> mMethodList;
	String mName;
	List<Constraint> invariants;

	protected UML2Class(JSONObject clsJsonObj, List<Association> ascs) {
		mAttrList = new ArrayList<Attribute>();
		mMethodList = new ArrayList<UML2Operation>();
		invariants = new ArrayList<Constraint>();
		mAscList = ascs;
		ParseClassInfoJson(clsJsonObj);
	}

	public UML2Operation findMethod(String methodName) {
		UML2Operation ret = null;
		for (UML2Operation info : mMethodList) {
			if (info.getName().equals(methodName)) {
				ret = info;
				break;
			}
		}

		return ret;
	}

	public ArrayList<Attribute> getAttrList() {
		return mAttrList;
	}

	public List<Association> getAscList() {
		return this.mAscList;
	}

	public List<Attribute> getAscAsAttrList() {
		List<Attribute> ascAsAttr = new ArrayList<Attribute>();
		for (Association asc : mAscList) {
			AssociationEnd ascEnd = asc.getRoleList().get(0);
			ascAsAttr.add(new Attribute(ascEnd.getName(), ascEnd.getType(),ascEnd.getLower(),ascEnd.getUpper(),ascEnd.getUnique()));
		}
		return ascAsAttr;
	}

	public Association findAscByAttributeName(String name) {
		for (Association asc : mAscList) {
			if (asc.getRoleList().get(0).getName().equals(name)) {
				return asc;
			}
		}
		throw new IllegalStateException("unable to find asc-attribute " + name);
	}

	public List<Attribute> getAttrAndAscList() {
		List<Attribute> list = new ArrayList<Attribute>();
		list.addAll(this.mAttrList);
		list.addAll(this.getAscAsAttrList());
		return list;
	}

	@Override
	public List<Operation> getOwnedOperations() {
		return new ArrayList<Operation>(mMethodList);
	}

	@Override
	public String getName() {
		return mName;
	}

	private boolean ParseClassInfoJson(JSONObject clsJsonObj) {
		mName = clsJsonObj.optString("name");
		// attrList
		JSONArray attr_list_array = clsJsonObj.optJSONArray("attrList");
		attr_list_array = JsonUtils.SortIDFile(attr_list_array);

		for (int i = 0; i < attr_list_array.length(); i++) {
			JSONObject attr_obj = attr_list_array.optJSONObject(i);
			if(attr_obj.optInt("upper")!=1)
			{
				mAttrList.add(new Attribute(attr_obj.optString("name"), "collection_"+attr_obj.optString("type"),attr_obj.optInt("lower"),attr_obj.optInt("upper"),attr_obj.optString("unique")));
			}
			else{
			mAttrList.add(new Attribute(attr_obj.optString("name"), attr_obj.optString("type"),attr_obj.optInt("lower"),attr_obj.optInt("upper"),attr_obj.optString("unique")));
			}
		}
		// method list
		JSONArray method_list_array = clsJsonObj.optJSONArray("methodList");
		for (int i = 0; i < method_list_array.length(); i++) {
			JSONObject method_obj = method_list_array.optJSONObject(i);
			UML2Operation method_info = new UML2Operation(this, method_obj);
			mMethodList.add(method_info);
		}

		return true;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public void addInvariant(Constraint constraint) {
		this.invariants.add(constraint);
	}

	public List<Constraint> getInvariants() {
		return this.invariants;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
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
		UML2Class other = (UML2Class) obj;
		if (mName == null) {
			if (other.mName != null)
				return false;
		} else if (!mName.equals(other.mName))
			return false;
		return true;
	}

}
