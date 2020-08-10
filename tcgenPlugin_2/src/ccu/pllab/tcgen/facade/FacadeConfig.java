package ccu.pllab.tcgen.facade;

 
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

import ccu.pllab.tcgen.clg2path.CriterionFactory;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.csp.CreateObjectConfig;
import ccu.pllab.tcgen.libs.pivotmodel.Association;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;
import ccu.pllab.tcgen.tc.TCGenConfig;

public class FacadeConfig implements TCGenConfig, CreateObjectConfig {
	public static String CFG_ASSOCIATION = "associations";
	public static String CFG_RANGE_INSNS = "range_of_instances";
	public static String CFG_INT_DOMAIN = "int_domain";
	public static String CFG_OBJ = "objects";
	public static String CFG_TARGET_CLS = "target_class";
	public static String CFG_TARGET_METHOD = "target_method";

	public static String CONFIG_FAIL_TRAIL_TIME = "fail_trial_time";

	private JSONObject config;
	private URL log4jPropertyFile;
	private URL outputFolder;
	private URL umlResourcesFile;

	public FacadeConfig(URL output_folder_path, URL uml_resource_path, URL log4j_property_path) {
		umlResourcesFile = uml_resource_path;
		log4jPropertyFile = log4j_property_path;
		outputFolder = output_folder_path;
		config = new JSONObject();
	}

	public File getDotInstallFolder() {
		return new File(config.optString("dot_execute_path", "/usr/bin/dot"));
	}

	public JSONObject getExtraConfig() {
		return this.config;
	}

	public URL getLog4jPropertyURL() {
		return log4jPropertyFile;
	}

	@Override
	public URL getOutputFolder() {
		return outputFolder;
	}

	@Override
	public URL getUmlResourcesURL() {
		return umlResourcesFile;
	}

	public void readConfiguration(String config_str) {
		try {
			JSONObject config_json = new JSONObject(config_str);
			this.readConfiguration(config_json);
		} catch (JSONException e) {
			this.config = new JSONObject();
			e.printStackTrace();
		}

	}

	public void readConfiguration(JSONObject config_json) {
		this.config = config_json;
	}

	public void setConfig(String key, Object value) {
		try {
			this.config.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTargetClass() {
		try {
			String[] packageAndClassName = this.config.getString(CFG_TARGET_CLS).split("\\.");
			return packageAndClassName[packageAndClassName.length - 1];
		} catch (JSONException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getTargetMethod() {
		try {
			return this.config.getString(CFG_TARGET_METHOD);
		} catch (JSONException e) {
			throw new IllegalStateException(e);
		}
	}

	public int getFailTrialTime() {
		return this.config.optInt(CONFIG_FAIL_TRAIL_TIME, 5);
	}

	public int getSolvingTimeout() {
		return this.config.optInt("solving_timeout", 5);
	}

	@Override
	public File getABSTestCaseFolder() {
		return new File(this.getOutputFolder().getFile(), this.getTestCasePackage().replace(".", File.separator));
	}

	@Override
	public String getTestCasePackage() {
		String[] packageAndClassName;
		try {
			packageAndClassName = this.config.getString(CFG_TARGET_CLS).split("\\.");
			List<String> names = Arrays.asList(packageAndClassName);
			return StringUtils.join(names.subList(0, names.size() - 1), ".") + "." + WordUtils.uncapitalize(this.getTestSuiteName());
		} catch (JSONException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public File getTestCaseSuiteFile() {
		return new File(this.getOutputFolder().getFile(), this.getTestSuiteName() + ".java");
	}

	@Override
	public String getTestSuiteName() {
		return this.getTargetClass() + this.getTargetMethod();
	}

	@Override
	public Range<Integer> getRangeOfInstance(UML2Class info) {
		JSONObject objects_config = this.config.optJSONObject(FacadeConfig.CFG_OBJ);
		if (objects_config == null) {
			objects_config = new JSONObject();
		}
		JSONObject obj_config = objects_config.optJSONObject(info.getName());
		if (obj_config == null) {
			obj_config = new JSONObject();
		}
		String range_str = obj_config.optString(FacadeConfig.CFG_RANGE_INSNS, "0..10");
		int low = Integer.parseInt(range_str.split("\\.\\.")[0]);
		if (low < 0) {
			low = 0;
		}
		int high = Integer.parseInt(range_str.split("\\.\\.")[1]);
		if (high < 0) {
			high = 10;
		}
		return Range.between(low, high);

	}

	@Override
	public Range<Integer> getRangeOfInstance(Association info) {
		JSONObject assoications_config = this.config.optJSONObject(FacadeConfig.CFG_ASSOCIATION);
		if (assoications_config == null) {
			assoications_config = new JSONObject();
		}
		JSONObject asc_config = assoications_config.optJSONObject(info.getName());
		if (asc_config == null) {
			asc_config = new JSONObject();
		}
		String range_str = asc_config.optString(FacadeConfig.CFG_RANGE_INSNS, "0..10");
		int low = Integer.parseInt(range_str.split("\\.\\.")[0]);
		if (low < 0) {
			low = 0;
		}
		int high = Integer.parseInt(range_str.split("\\.\\.")[1]);
		if (high < 0) {
			high = 10;
		}
		return Range.between(low, high);
	}

	public File getReportFile() {
		return new File(this.getOutputFolder().getFile(), "report.csv");
	}

	@Override
	public Range<Integer> getIntDomain() {
		String domain_string = config.optString(FacadeConfig.CFG_INT_DOMAIN, "-65535..65535");
		String[] up_low = domain_string.split("\\.\\.");
		return Range.between(Integer.parseInt(up_low[0]), Integer.parseInt(up_low[1]));
	}

	public Criterion getPathCoverage() {
		return parsePathCoverage(config.optString("path_coverage", "node"));

	}

	public boolean isInvalidCase() {
		return config.optBoolean("invalid_case", false);
	}
/*criterion based */
	public static Criterion parsePathCoverage(String coverage) {
		if (coverage.equals("dcc")) {
			return CriterionFactory.Criterion.dcc;
		} else if (coverage.equals("dc")) {
			return CriterionFactory.Criterion.dc;
		} else if (coverage.equals("mcc")) {
			return CriterionFactory.Criterion.mcc;
		}else if (coverage.equals("dcdup")) {
			return CriterionFactory.Criterion.dcdup;
		}else if (coverage.equals("dccdup")) {
			return CriterionFactory.Criterion.dccdup;
		}else if (coverage.equals("mccdup")) {
			return CriterionFactory.Criterion.mccdup;
		} else {
			return CriterionFactory.Criterion.dcc;
		}
	}
}
