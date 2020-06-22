package ccu.pllab.tcgen.ecl2data;


public class SolvingTimeOutException extends Exception {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 5014445395949446140L;
	private String msg;

	public SolvingTimeOutException(String pathName, int timeout) {
		this.msg = String.format("have spent more then %s secs on path: %s", timeout, pathName);
	}

	@Override
	public String getMessage() {
		return this.msg;
	}

}
