package ccu.pllab.tcgen.facade;

 
public class Parameter {

	private static Boolean enableDebug;

	public static Boolean getEnableDebug() {
		return enableDebug;
	}

	public static void setEnableDebug(Boolean enableDebug) {
		Parameter.enableDebug = enableDebug;
	}
}
