package ccu.pllab.tcgen.libs.pivotmodel;

 
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Triple;

public class AscInstance {
	String mName;
	// role_name, type, id
	ArrayList<Triple<String, String, Integer>> mRoleList;

	public AscInstance() {
		mName = "";
		mRoleList = new ArrayList<Triple<String, String, Integer>>();
	}

	public String getName() {
		return mName;
	}

	public ArrayList<Triple<String, String, Integer>> getRoleList() {
		return mRoleList;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public void setRoleList(ArrayList<Triple<String, String, Integer>> mRoleList) {
		this.mRoleList = mRoleList;
	}

}
