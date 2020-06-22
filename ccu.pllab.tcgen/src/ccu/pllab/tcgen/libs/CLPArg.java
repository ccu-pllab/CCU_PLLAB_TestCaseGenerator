package ccu.pllab.tcgen.libs;
 

import java.util.ArrayList;

public class CLPArg {
	// (, literal|pairedObj)*
	ArrayList<String> mArgList;
	// paried_obj
	String mSelf;

	// literal|obj_elm
	String Ret;

	public CLPArg() {
		mArgList = new ArrayList<String>();
	}

	public ArrayList<String> getArgList() {
		return mArgList;
	}

	public String getSelf() {
		return mSelf;
	}

	public String getRet() {
		return Ret;
	}

	public void setArgList(ArrayList<String> mArgList) {
		this.mArgList = mArgList;
	}

	public void setSelf(String mSelf) {
		this.mSelf = mSelf;
	}

	public void setRet(String ret) {
		Ret = ret;
	}

}
