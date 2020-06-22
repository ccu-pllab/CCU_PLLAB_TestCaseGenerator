package ccu.pllab.tcgen.libs;

 
import java.util.ArrayList;

/* decompose into two string array */
public class CLPState {
	// each string is an ascElm
	ArrayList<String> mAscList;
	// each string is an objElm
	ArrayList<String> mObjList;

	public CLPState() {
		mObjList = new ArrayList<String>();
		mAscList = new ArrayList<String>();
	}

	public void addAsc(String asc) {
		mAscList.add(asc);
	}

	public void addObj(String obj) {
		mObjList.add(obj);
	}

	public ArrayList<String> getAscList() {
		return mAscList;
	}

	public ArrayList<String> getObjList() {
		return mObjList;
	}
}
