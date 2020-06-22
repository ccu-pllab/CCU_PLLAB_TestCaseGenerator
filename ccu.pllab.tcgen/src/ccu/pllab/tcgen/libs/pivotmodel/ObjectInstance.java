package ccu.pllab.tcgen.libs.pivotmodel;

 
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Triple;

public class ObjectInstance {
	// name, type, value
	ArrayList<Triple<String, String, String>> attrValueList;
	String name;
	int oid;

	public ObjectInstance() {
		name = "";
		oid = -1;
		attrValueList = new ArrayList<Triple<String, String, String>>();
	}

	public ArrayList<Triple<String, String, String>> getAttrValueList() {
		return attrValueList;
	}

	public String getName() {
		return name;
	}

	public int getOid() {
		return oid;
	}

	public void setAttrValueList(ArrayList<Triple<String, String, String>> mAttrValueList) {
		this.attrValueList = mAttrValueList;
	}

	public void setName(String mName) {
		this.name = mName;
	}

	public void setOid(int mOid) {
		this.oid = mOid;
	}

}
