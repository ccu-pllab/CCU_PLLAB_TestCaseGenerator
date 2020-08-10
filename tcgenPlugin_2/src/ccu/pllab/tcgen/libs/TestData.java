package ccu.pllab.tcgen.libs;

 
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Triple;

import ccu.pllab.tcgen.libs.pivotmodel.AscInstance;
import ccu.pllab.tcgen.libs.pivotmodel.ObjectInstance;

public class TestData {
	// name, type, value
	// if the type is an object, the value keep it's id only
	ArrayList<Triple<String, String, String>> mArgList;
	String mClsName;

	String mMethodName;
	ArrayList<AscInstance> mPostAscList;
	ArrayList<ObjectInstance> mPostObjList;
	ArrayList<AscInstance> mPreAscList;
	ArrayList<ObjectInstance> mPreObjList;
	// same to arg list
	// type, value
	ImmutablePair<String, String> mRet;

	// keep the self object id
	Integer mSelfID;
	int pathid;

	// keep the path id
	String pathName;
	private boolean isConstructor;
	private boolean invalidated;
	private String preconditionName;

	public TestData(String cls_name, String method_name, int pathid, boolean isConstructor) {
		mClsName = cls_name;
		mMethodName = method_name;
		mPreObjList = new ArrayList<ObjectInstance>();
		mPreAscList = new ArrayList<AscInstance>();
		mPostObjList = new ArrayList<ObjectInstance>();
		mPostAscList = new ArrayList<AscInstance>();
		mArgList = new ArrayList<Triple<String, String, String>>();
		mRet = new ImmutablePair<String, String>("", "");
		this.pathid = pathid;
		this.isConstructor = isConstructor;
	}

	public ArrayList<Triple<String, String, String>> getArgList() {
		return mArgList;
	}

	public String getClsName() {
		return mClsName;
	}

	public String getMethodName() {
		return mMethodName;
	}

	public ArrayList<AscInstance> getPostAscList() {
		return mPostAscList;
	}

	public ArrayList<ObjectInstance> getPostObjList() {
		return mPostObjList;
	}

	public ArrayList<AscInstance> getPreAscList() {
		return mPreAscList;
	}

	public ArrayList<ObjectInstance> getPreObjList() {
		return mPreObjList;
	}

	public ImmutablePair<String, String> getRet() {
		return mRet;
	}

	public Integer getSelfID() {
		return mSelfID;
	}

	public Integer getPathID() {
		return pathid;
	}

	public void setArgList(ArrayList<Triple<String, String, String>> mArgList) {
		this.mArgList = mArgList;
	}

	public void setClsName(String mClsName) {
		this.mClsName = mClsName;
	}

	public void setMethodName(String mMethodName) {
		this.mMethodName = mMethodName;
	}

	public void setPostAscList(ArrayList<AscInstance> mPostAscList) {
		this.mPostAscList = mPostAscList;
	}

	public void setPostObjList(ArrayList<ObjectInstance> mPostObjList) {
		this.mPostObjList = mPostObjList;
	}

	public void setPreAscList(ArrayList<AscInstance> mPreAscList) {
		this.mPreAscList = mPreAscList;
	}

	public void setPreObjList(ArrayList<ObjectInstance> mPreObjList) {
		this.mPreObjList = mPreObjList;
	}

	public void setRet(ImmutablePair<String, String> mRet) {
		this.mRet = mRet;
	}

	public void setSelfID(Integer mSelfID) {
		this.mSelfID = mSelfID;
	}

	public void setPathName(String pathID) {
		this.pathName = pathID;
	}

	public boolean isConstructor() {
		return this.isConstructor;
	}

	public void setInvalidate(boolean isInvalidated) {
		this.invalidated = isInvalidated;
	}

	public boolean isInvalidated() {
		return this.invalidated;
	}

	public String getPreconditionName() {
		return this.preconditionName;
	}

	public void setPreconditionName(String preconditionName) {
		this.preconditionName = preconditionName;
	}

}
