package ccu.pllab.tcgen.facade;

 
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.json.JSONException;

import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;

import com.parctechnologies.eclipse.EclipseException;

public class Main {

	private static String config_file_path;
	private static String log4j_property_path;
	private static String ocl_model_path;
	private static String output_folder_path;
	private static String uml_model_path;
	private static String uml_resource_path;
	private static Boolean enable_debug;

	public static void main(String[] args) {
		parseOptions(args);

		Parameter.setEnableDebug(enable_debug);

		try {
			File config_file = new File(config_file_path);
			FacadeConfig facade_config = new FacadeConfig(new URL("file:" + output_folder_path), new URL("file:" + uml_resource_path), new URL("file:" + log4j_property_path));
			Facade facade = new Facade(facade_config);
			facade.loadModel(new File(uml_model_path), new File(ocl_model_path), true, config_file);
			facade.genTestCases();
		} catch (IOException | JSONException | EclipseException | TemplateException | ModelAccessException | tudresden.ocl20.pivot.parser.ParseException e) {
			throw new IllegalStateException(e);
		}
	}

	private static void parseOptions(String[] args) {
		Options options = new Options();
		options.addOption("uml_model", true, "path of uml file");
		options.addOption("ocl_model", true, "path of ocl file");
		options.addOption("output_folder", true, "path to put generated files");
		options.addOption("log4j_properties", true, "path of log4j.properties");
		options.addOption("uml_resource", true, "paht of org.eclipse.uml2.uml.resources.jar");
		options.addOption("config", true, "config file");
		options.addOption("enable_debug", true, "enable debug information");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
			uml_model_path = cmd.getOptionValue("uml_model", "");
			ocl_model_path = cmd.getOptionValue("ocl_model", "");
			output_folder_path = cmd.getOptionValue("output_folder", "");
			uml_resource_path = cmd.getOptionValue("uml_resource", "");
			log4j_property_path = cmd.getOptionValue("log4j_properties", "");
			config_file_path = cmd.getOptionValue("config", "");
			enable_debug = Boolean.valueOf(cmd.getOptionValue("enable_debug", "0"));
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}
	}

}
