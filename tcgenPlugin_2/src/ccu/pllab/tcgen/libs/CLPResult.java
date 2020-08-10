package ccu.pllab.tcgen.libs;

 
public class CLPResult {
	CLPArg mArg;
	CLPState mPostState;
	CLPState mPreState;

	public CLPResult() {
		mPreState = new CLPState();
		mPostState = new CLPState();
		mArg = new CLPArg();
	}

	public CLPArg getArg() {
		return mArg;
	}

	public CLPState getPostState() {
		return mPostState;
	}

	public CLPState getPreState() {
		return mPreState;
	}

	public void setArg(CLPArg mArgList) {
		this.mArg = mArgList;
	}

	public void setPostState(CLPState mPostState) {
		this.mPostState = mPostState;
	}

	public void setPreState(CLPState mPreState) {
		this.mPreState = mPreState;
	}
}
