package ccu.pllab.tcgen.exe.main;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;



import org.json.JSONException;

import com.parctechnologies.eclipse.EclipseException;

import ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractSyntaxTree.AbstractSyntaxTreeNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.facade.Facade;
import ccu.pllab.tcgen.facade.FacadeConfig;
import ccu.pllab.tcgen.sd2clg.SD2TestCase;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;
import ccu.pllab.tcgen.transform.*;
import ccu.pllab.tcgen.AbstractSyntaxTree.SymbolTable;;


public class Main {

	/**
	 * no matter WHAT
	 * you need to add "Run configuration" arguments
	 * Program arguments and VM arguments
	 * 
	 * 1.
	 * VM argument :
	 * you need to set ECLiPSe directory address
	 * and follow rule:
	 * -ea -Declipse.directory=Dir address
	 * Example:
	 * -ea -Declipse.directory=C:\ECLiPSe_6.1
	 * 
	 * 2.
	 * Program arguments:
	 * you need to set each type of program arguments
	 * detail info is followed type argument
	 * 
	 *  [1] Black box testing
	 * ocl_model_path
	 * uml_model_path
	 * config_file_path
	 * uml_resource_path
	 * log4j_property_path
	 * output_folder_path
	 * [Notice '-' signal]
	 * Example:
	 * 
	 * -ocl_model_path ${project_loc}/../Examples/grade/grade.ocl
	 * -uml_model_path ${project_loc}/../Examples/grade/grade.uml
	 * -config_file_path ${project_loc}/../Examples/grade/grade.config.json
	 * -uml_resource_path ${project_loc}/resources/org.eclipse.uml2.uml.resources.jar
	 * -log4j_property_path ${project_loc}/log4j.properties
	 * -output_folder_path ${project_loc}/../Examples/test-src
	 * 
	 *  -ocl_model_path C:\\Users\\chienLung\\tcgen\\examples\\time\\time.ocl
	 * -uml_model_path C:\\Users\\chienLung\\tcgen\\examples\\time\\time.uml
	 * -config_file_path C:\\Users\\chienLung\\tcgen\\examples\\time\\time.config.json
	 * -uml_resource_path C:\\Users\\chienLung\\tcgen\\ccu.pllab.tcgen\\resources\org.eclipse.uml2.uml.resources.jar
	 * -log4j_property_path C:\\Users\\chienLung\\tcgen\\tcgen-plugin\\log4j.properties
	 * -output_folder_path C:\\Users\\chienLung\\tcgen\\examples\\output\\CLG
	 * 
	 *  [2] White box testing
	 * -ocl_model_path ${project_loc}/../Examples/grade/grade.ocl
	 * -uml_model_path ${project_loc}/../Examples/grade/grade.uml
	 * -config_file_path ${project_loc}/../Examples/grade/grade.config.json
	 * -uml_resource_path ${project_loc}/resources/org.eclipse.uml2.uml.resources.jar
	 * -java_program_path ${project_loc}/../Examples/javaSrcCode/Grade/Grade.java
	 * -output_folder_path ${project_loc}/../Examples/test-src
	 * 
	 * [3] Class diagram testing
	 * -ocl_model_path ${project_loc}/../Examples/Coffeemachine/coffeemachine.ocl
	 * -uml_model_path ${project_loc}/../Examples/Coffeemachine/coffeemachine.uml
	 * -state_diagram_path ${project_loc}/../Examples/Coffeemachine/CoffeeMachineSD.uml
	 * -output_folder_path ${project_loc}/../Examples/test-src
	 * 
	 * */
	
	
	public static String config_file_path;
	public static String log4j_property_path;
	public static String ocl_model_path;
	public static String output_folder_path;
	public static String uml_model_path;
	public static String uml_resource_path;
	public static Boolean enable_debug;

	public static String state_diagram_path;
	public static String class_diagram__path;

	public static String java_program_path;
	public static boolean boundary_analysis;
	public static String criterion_type="dc";
	public static boolean DUP;
	public static Criterion criterion=null;
	public static AbstractSyntaxTreeNode ast;
	public static ArrayList<CLGGraph> clg;
	public static List<ccu.pllab.tcgen.ast.Constraint> oclCons;
	public static ArrayList<String> attribute=new ArrayList<String>();
	public static String invCLP;
	public static String ifCLP;//++
	public static int count=0;//++
	public static String head;
	public static String className;
	public static SymbolTable symbolTable;
	public static boolean msort;
	public static boolean issort=false;
	public static boolean twoD=false;
	public static boolean isArraylist=false;  
	public static boolean isConstructor=false;
	public static HashMap<String, Integer> arrayMap=new HashMap<String, Integer>();
	public static HashMap<String, Integer> indexMap=new HashMap<String, Integer>();
	public static boolean bodyExpBoundary=false;
	public static HashMap<String,Integer> iterateBoundary;
	public static HashMap<CLGConstraintNode,Integer> conNodeiterateBoundary;
	public static boolean changeBoundary=false;
	public static boolean doArray=false;
	public static boolean boundaryhavesolution=false;
	public static void main(String[] args) throws IOException, TemplateException, ModelAccessException, ParseException,Exception {
		msort=false;
		MainFrame mainFrame=new MainFrame();
		/*output_folder_path="C:\\Users\\chienLung\\tcgen\\examples\\output\\CLG";
		java_program_path="C:\\Users\\chienLung\\tcgen\\examples\\javaSrcCode\\Triangle\\Triangle.java";
	ocl_model_path ="C:\\Users\\chienLung\\tcgen\\examples\\triangle\\triangle.ocl";
		uml_model_path ="C:\\Users\\chienLung\\tcgen\\examples\\triangle\\triangle.uml";
		config_file_path="C:\\Users\\chienLung\\tcgen\\examples\\triangle\\triangle.config.json";
		uml_resource_path ="C:\\Users\\chienLung\\tcgen\\ccu.pllab.tcgen\\resources\\org.eclipse.uml2.uml.resources.jar";
		log4j_property_path ="C:\\Users\\chienLung\\tcgen\\tcgen-plugin\\log4j.properties";
		output_folder_path="C:\\Users\\chienLung\\tcgen\\examples\\output\\CLG";
		oclCons=SD2TestCase.parseOCL(new File(ocl_model_path), new File(uml_model_path));
		boundary_analysis = true;
		criterion_type = "dcc";
	//	criterion = FacadeConfig.parsePathCoverage(criterion_type);
//	SrcVisitProcess srcVisitProcess = new SrcVisitProcess(java_program_path);
		//OCL2AST ast = new OCL2AST(selectedFile);
		//ast.getAbstractSyntaxTree().toGraphViz();
		//clg=new AST2CLG(ast. getAbstractSyntaxTree());
		System.out.println("Select test type");
		System.out.println("1. Method-level Black-box testing.");
		System.out.println("2. Method-level White-box testing.");
		System.out.println("3. Class-level State-diagram testing.");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		//System.out.println(input);
//
	//	switch (input) {
		
		
		//case "1":
			System.out.println("Set ocl_model_path");
			ocl_model_path = scanner.nextLine();
			System.out.println("Set uml_model_path");
			uml_model_path = scanner.nextLine();
			System.out.println("Set config_file_path");
			config_file_path = scanner.nextLine();
			System.out.println("Set uml_resource_path");
			uml_resource_path = scanner.nextLine();
			System.out.println("Set log4j_property_path");
			log4j_property_path = scanner.nextLine();
			System.out.println("Set output folder path");
			output_folder_path = scanner.nextLine();
			System.out.println("boundary_analysis (Y/N)");
			if (scanner.nextLine().equals("Y")) {
				boundary_analysis = true;
			} else {
				boundary_analysis = false;
			}
			System.out.println("Select criterion (dc/ dcc/ mcc)");
			switch (scanner.nextLine()) {
			case "dc":
				criterion_type = "dc";
				break;
			case "dcc":
				criterion_type = "dcc";
				break;
			case "mcc":
				criterion_type = "mcc";
				break;
			}
			System.out.println("Select data flow analysis (Y/ N)");
			if (scanner.nextLine().equals("Y")) {
				DUP = true;
				criterion_type+="dup";
			} else {
				DUP = false;
			}
			criterion = FacadeConfig.parsePathCoverage(criterion_type);

		
		
		OCL2AST ast = new OCL2AST(new File("C:\\Users\\chienLung\\tcgen\\examples\\time\\time.ocl"));
			ast.getAbstractSyntaxTree().toGraphViz();
			AST2CLG	clg=new AST2CLG(ast. getAbstractSyntaxTree());
			CLG2Path path=new CLG2Path(clg.getCLGGraph());
			try {

				File config_file = new File(config_file_path);
				FacadeConfig facade_config = new FacadeConfig(new URL("file:" + output_folder_path), new URL("file:" + uml_resource_path), new URL("file:" + log4j_property_path));
				Facade facade = new Facade(facade_config);
				facade.loadModel(new File(uml_model_path), new File(ocl_model_path), true, config_file);
				facade.genTestCases();
			} catch (IOException | JSONException | EclipseException | TemplateException | ModelAccessException | tudresden.ocl20.pivot.parser.ParseException e) {
				throw new IllegalStateException(e);
			}
			//break;
			
			
			
			
	/*	case "2":
		System.out.println("Set OCL model file path");
			ocl_model_path = scanner.nextLine();
			System.out.println("Set uml_model_path");
			uml_model_path = scanner.nextLine();
			
		*/	
		//	oclCons=SD2TestCase.parseOCL(new File(ocl_model_path), new File(uml_model_path));
			
		/*	System.out.println("Set java file path");
			java_program_path = scanner.nextLine();
			System.out.println("Set output folder path ");
			output_folder_path = scanner.nextLine();
			System.out.println("Select boundary_analysis (Y/N)");
			if (scanner.nextLine().equals("Y")) {
				boundary_analysis = true;
			} else {*/
			//	boundary_analysis = false;
			/*}
			System.out.println("Select criterion (dc/ dcc/ mcc)");
			switch (scanner.nextLine()) {
			case "dc":
				criterion_type = "dc";
				break;
			case "dcc":*/
				//criterion_type = "dcc";
			/*	break;
			case "mcc":
				criterion_type = "mcc";
				break;
			}
			System.out.println("Select data flow analysis (Y/ N)");
			
			if (scanner.nextLine().equals("Y")) {
				DUP = true;
				criterion_type+="dup";
			} else {*/
				//DUP = false;
			//}
		//	criterion = FacadeConfig.parsePathCoverage(criterion_type);
		//	
		//	SrcVisitProcess srcVisitProcess = new SrcVisitProcess(java_program_path);

			//break;
		/*case "3":
			System.out.println("Set ocl_model_path");
			ocl_model_path = scanner.nextLine();
			System.out.println("Set state_diagram_path");
			state_diagram_path = scanner.nextLine();
			System.out.println("Set class_diagram__path");
			class_diagram__path = scanner.nextLine();
			System.out.println("Set output_folder_path");
			output_folder_path = scanner.nextLine();

			System.out.println("Select criterion (dc/ dcc/ mcc)");
			switch (scanner.nextLine()) {
			case "dc":
				criterion_type = "dc";
				break;
			case "dcc":
				criterion_type = "dcc";
				break;
			case "mcc":
				criterion_type = "mcc";
				break;
			}
			System.out.println("Select data flow analysis (Y/ N)");
			
			if (scanner.nextLine().equals("Y")) {
				DUP = true;
				criterion_type+="dup";
			} else {
				DUP = false;
			}
			criterion = FacadeConfig.parsePathCoverage(criterion_type);

			File state_diagram_file = new File(state_diagram_path);
			File class_diagram_file = new File(class_diagram__path);
			File ocl_file = new File(ocl_model_path);

			SD2TestCase sd2TC = new SD2TestCase(state_diagram_file, class_diagram_file, ocl_file);

			break;
		default:
			break;

		}*/
	}

}
