package ccu.pllab.tcgen.libs.pivotmodel;
 

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClassDiagInfo {
	ArrayList<Association> mAscInfoList;
	ArrayList<UML2Class> mClsInfoList;
	String packageName;

	public ClassDiagInfo(ClassDiagToJson clsJson) {
		this.mAscInfoList = new ArrayList<Association>();
		this.mClsInfoList = new ArrayList<UML2Class>();

		if (!ParseClassDiagInfoJson(clsJson.toJSONString())) {
			throw new IllegalStateException("Parse json content error!!");
		}
	}

	public Association findAscInfo(String ascName) {
		Association ascInfo = null;
		for (Association asc : mAscInfoList) {
			if (asc.getName().equals(ascName)) {
				ascInfo = asc;
				break;
			}
		}

		return ascInfo;
	}

	public UML2Class findClassInfo(String clsName) {
		UML2Class clsInfo = null;
		for (UML2Class cls : mClsInfoList) {
			if (cls.getName().equals(clsName)) {
				clsInfo = cls;
				break;
			}
		}

		return clsInfo;
	}

	public UML2Operation findMethodInfo(String clsName, String methodName) {
		UML2Class clsInfo = findClassInfo(clsName);
		if (clsInfo == null)
			return null;
		UML2Operation ret = clsInfo.findMethod(methodName);

		return ret;
	}

	public ArrayList<Association> getAscInfoList() {
		return mAscInfoList;
	}

	public ArrayList<UML2Class> getClsInfoList() {
		return mClsInfoList;
	}

	public String getPackageName() {
		return packageName;
	}

	/*
	 * { "package":"ccu.lab", "class":[
	 * {"name":"Teacher","attrList":[],"methodList":[]},
	 * {"name":"Laboratory","attrList"
	 * :[{"name":"limit","type":"Integer"}],"methodList"
	 * :[{"argList":[],"name":"isAvailable"
	 * ,"ret_type":"Boolean"},{"argList":[{"name"
	 * :"student","type":"Student"}],"name"
	 * :"canRegister","ret_type":"Boolean"},{
	 * "argList":[{"name":"student","type":
	 * "Student"}],"name":"register","ret_type"
	 * :"void"},{"argList":[{"name":"increasement"
	 * ,"type":"Integer"}],"name":"increaseLimit"
	 * ,"ret_type":"void"},{"argList":[
	 * {"name":"newLimit","type":"Integer"}],"name"
	 * :"setLimit","ret_type":"Boolean"}]},
	 * {"name":"Student","attrList":[],"methodList"
	 * :[{"argList":[{"name":"instructor"
	 * ,"type":"Teacher"}],"name":"setInstructor"
	 * ,"ret_type":"void"},{"argList":[
	 * {"name":"laboratory","type":"Laboratory"}]
	 * ,"name":"setLaboratory","ret_type":"void"}]}], "association":[
	 * {"name":instruct
	 * "roleList":[{"name":"students","type":"Teacher"},{"name":"instructor"
	 * ,"type":"Student"}]},
	 * {"name":contains"roleList":[{"name":"students","type"
	 * :"Laboratory"},{"name":"laboratory","type":"Student"}]},
	 * {"name":guide"roleList"
	 * :[{"name":"labortory","type":"guide"},{"name":"instructor"
	 * ,"type":"Laboratory"}]} ] }
	 */
	// import from a json string
	private boolean ParseClassDiagInfoJson(String json) {
		try {
			JSONObject entry = new JSONObject(json);
			// setup package name
			packageName = entry.get("package").toString();

			JSONArray ascObjArray = (JSONArray) entry.get("association");
			for (int i = 0; i < ascObjArray.length(); i++) {
				JSONObject ascObj = (JSONObject) ascObjArray.get(i);
				Association ascInfo = new Association(ascObj);
				mAscInfoList.add(ascInfo);
			}

			JSONArray clsObjArray = (JSONArray) entry.get("class");

			for (int i = 0; i < clsObjArray.length(); i++) {
				JSONObject clsObj = (JSONObject) clsObjArray.get(i);
				UML2Class clsInfo = new UML2Class(clsObj, this.getAssociationsForClass(clsObj.optString("name")));
				mClsInfoList.add(clsInfo);
			}
		} catch (JSONException pe) {
			pe.printStackTrace();
			return false;
		}

		return true;
	}

	private List<Association> getAssociationsForClass(String clazz) {
		List<Association> attOfClass = new ArrayList<Association>();
		List<Association> ascs = this.mAscInfoList;
		for (Association asc : ascs) {
			for (AssociationEnd end : asc.getRoleList()) {
				if (end.getType().equals(clazz)) {
					if (asc.getRoleList().indexOf(end) == 0) {
						;
						attOfClass.add(new Association(asc.getName(), asc.getRoleList().get(1), asc.getRoleList().get(0)));
					} else {
						attOfClass.add(new Association(asc.getName(), asc.getRoleList().get(0), asc.getRoleList().get(1)));
					}
				}
			}
		}
		return attOfClass;
	}
}
