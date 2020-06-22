package ccu.pllab.tcgen.pathCLP2data;


public class SolvingFailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7617628390627791642L;
	private String path;

	public SolvingFailException(String path) {
		this.path = path;
	}
 
	@Override
	public String getMessage() {
		return path + " failed";
	}

}
 