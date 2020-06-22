package ccu.pllab.tcgen.tc;

  
import java.io.File;
import java.net.URL;

// keep configuration for each generation session
public interface TCGenConfig {

	public File getABSTestCaseFolder();

	public String getTargetClass();

	public String getTargetMethod();

	public String getTestCasePackage();

	public URL getOutputFolder();

	public File getTestCaseSuiteFile();

	public String getTestSuiteName();

	public URL getUmlResourcesURL();

}
