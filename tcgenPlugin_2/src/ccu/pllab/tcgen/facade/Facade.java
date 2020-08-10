package ccu.pllab.tcgen.facade;

 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;
import ccu.pllab.tcgen.ast.ASTNode;
import ccu.pllab.tcgen.ast.ASTUtil;
import ccu.pllab.tcgen.clg.CLGNode;
import ccu.pllab.tcgen.clg2path.CoverageCriterion;
import ccu.pllab.tcgen.clg2path.CriterionFactory;
import ccu.pllab.tcgen.clg2path.FeasiblePathFinder;
import ccu.pllab.tcgen.clg2path.Path;
import ccu.pllab.tcgen.clg2path.UMLCoverageCriterion;
import ccu.pllab.tcgen.csp.CreateObject;
import ccu.pllab.tcgen.csp.EnvironmentSetUp;
import ccu.pllab.tcgen.ecl2data.ECLiPSeCompoundTerm;
import ccu.pllab.tcgen.ecl2data.Ecl2Data;
import ccu.pllab.tcgen.ecl2data.Ecl2DataFactory;
import ccu.pllab.tcgen.ecl2data.SolvingFailException;
import ccu.pllab.tcgen.ecl2data.SolvingTimeOutException;
import ccu.pllab.tcgen.libs.DresdenOCLASTtoInternelAST;
import ccu.pllab.tcgen.libs.Predicate;
import ccu.pllab.tcgen.libs.TestData;
import ccu.pllab.tcgen.libs.node.GraphVisitor;
import ccu.pllab.tcgen.libs.node.NodeVisitHandler;
import ccu.pllab.tcgen.libs.node.QueueFrontier;
import ccu.pllab.tcgen.libs.pivotmodel.Association;
import ccu.pllab.tcgen.libs.pivotmodel.ClassDiagInfo;
import ccu.pllab.tcgen.libs.pivotmodel.ClassDiagToJson;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.Operation;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Operation;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
import ccu.pllab.tcgen.tc.TestDataFactory;
import ccu.pllab.tcgen.tc.TestSuiteGen;

import com.parctechnologies.eclipse.EclipseException;

public class Facade {

	public static String OUTPUT_FILENAME = "clp.ecl";

	private ClassDiagInfo class_diag_info;
	private ClassDiagToJson class_diag_to_json;
	private FacadeConfig config;
	private Model context;
	private List<Path> csp_path_list;
	private IModel model;
	private File ast_predicate_file;
	private Set<Predicate> predicate_pool;
	private Ecl2Data ecl2data;
	private FacadeHook hook;

	private List<JSONObject> detail_config_list;

	public Facade(FacadeConfig pConfig) {
		this.config = pConfig;
		this.predicate_pool = new HashSet<Predicate>();
		this.csp_path_list = new ArrayList<Path>();
		this.hook = new FacadeHook();
	}

	public void generateCLPForMethodSimulation() throws IOException {
		StringWriter string_writer = new StringWriter();
		final PrintWriter print_writer = new PrintWriter(string_writer);
		print_writer.println(":-lib(apply).");
		print_writer.println(":-lib(apply_macros).");
		print_writer.println(":-lib(lists).");
		print_writer.println(":-lib(listut).");
		print_writer.println(":-lib(timeout).");
		print_writer.println(":-lib(ic).");
		print_writer.println();
		print_writer.println();
		print_writer.flush();

		for (UML2Class clazz : context.getClasses()) {
			GraphVisitor<ASTNode> bfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.PREORDER, new QueueFrontier<ASTNode>());
			NodeVisitHandler<ASTNode> predicate_pool_adder = new NodeVisitHandler<ASTNode>() {

				@Override
				public void visit(ASTNode current_node) {
					predicate_pool.add(current_node);
				}
			};
			for (ASTNode constraint : clazz.getInvariants()) {
//				System.out.println("Invariant: "+constraint.toOCL());
				bfs.traverse(constraint, predicate_pool_adder);
//				System.out.println(constraint.OCL2CLG().graphDraw());
			}
//			ArrayList<ASTNode> consList = new ArrayList<ASTNode>();
//			for (ASTNode constraint : clazz.getInvariants()) {	
//				System.out.println("Times ");
//				consList.add(constraint.clone());				
//			}
//			for(ASTNode constraint : consList){
//				System.out.println("constraint.getConstraint() ");
//				System.out.println(constraint.OCL2CLG().graphDraw());
//				
//			}

			for (Operation op : clazz.getOwnedOperations()) {
				UML2Operation uml2op = (UML2Operation) op;
				predicate_pool.add(uml2op);
				bfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.PREORDER, new QueueFrontier<ASTNode>());
				for (ASTNode constraint : uml2op.getPreConstraints()) {					
					bfs.traverse(constraint, predicate_pool_adder);
					System.out.println("preCons: "+constraint.toOCL());
				}
				bfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.PREORDER, new QueueFrontier<ASTNode>());
				for (ASTNode constraint : uml2op.getPostConstraints()) {					
					bfs.traverse(constraint, predicate_pool_adder);
					System.out.println("postCons: "+constraint.toOCL());
				}
			}
		}

		final Map<String, String> tpl_arg = new HashMap<String, String>();
		List<Predicate> predicates = new ArrayList<Predicate>(predicate_pool);
		
		Collections.sort(predicates, new Comparator<Predicate>() {
			@Override
			public int compare(Predicate arg0, Predicate arg1) {
				String name1 = arg0.getPredicateName(tpl_arg);
				String name2 = arg1.getPredicateName(tpl_arg);
				if (name1.startsWith("n_") && name2.startsWith("n_")) {
					return Integer.compare(Integer.parseInt(name1.split("_")[1]), Integer.parseInt(name2.split("_")[1]));
				} else {
					return name1.compareTo(name2);
				}
			}
		});
		EnvironmentSetUp create_object = new EnvironmentSetUp(context);
		for (Predicate p : predicates) {
			print_writer.println(p.getEntirePredicate(tpl_arg));
			print_writer.println();
		}
		create_object.generateCLP(print_writer);
		ast_predicate_file = new File(config.getOutputFolder().getFile(), OUTPUT_FILENAME);
		if (!ast_predicate_file.exists()) {
			ast_predicate_file.createNewFile();
		}
		this.hook.writeFile(string_writer.toString(), ast_predicate_file);
	}

	public Model loadModel(File uml_file, File ocl_file, boolean isStandalone, File config_file) throws IOException, TemplateException, ModelAccessException, ParseException, JSONException {
		class_diag_to_json = new ClassDiagToJson(uml_file);
		class_diag_info = new ClassDiagInfo(class_diag_to_json);
		context = new Model(class_diag_info);
		TypeFactory.getInstance().setModel(context);
		ASTUtil ast_util = new ASTUtil();
		DresdenOCLASTtoInternelAST ast_constructor = new DresdenOCLASTtoInternelAST(ast_util, context);
		context.attachConstraints(ast_constructor.parseOclTreeNodeFromPivotModel(this.loadModelStandalone(uml_file, ocl_file)));
		this.hook.when_gen_ast_graph(context, config);
		detail_config_list = parseConfigFile(config_file, context);
		this.hook.when_gen_clg_graph(context, config, detail_config_list);
		this.hook.when_gen_invalid_ast_graph(context, config);
		this.hook.when_gen_invalid_clg_graph(context, config, detail_config_list);

		return context;
	}

	private List<Constraint> loadModelStandalone(File uml_file, File ocl_file) throws TemplateException, ModelAccessException, IOException, ParseException {
		StandaloneFacade.INSTANCE.initialize(this.config.getLog4jPropertyURL());
		model = StandaloneFacade.INSTANCE.loadUMLModel(uml_file, new File(config.getUmlResourcesURL().getFile()));
		return StandaloneFacade.INSTANCE.parseOclConstraints(model, ocl_file);
	}

	public void findFeasiblePathAndGetSolution() throws IOException, EclipseException {
		if (this.ecl2data == null) {
			throw new IllegalStateException("please connect to clp solver");
		}
		FileWriter fw = new FileWriter(this.config.getReportFile(), true);
		String target_cls = this.config.getTargetClass();
		String target_method = this.config.getTargetMethod();
		ecl2data.compile(ast_predicate_file);
		TestDataFactory testDataFactory = new TestDataFactory(context, target_cls, target_method);
		UML2Operation target_method_instance = context.findClassInfoByName(target_cls).findMethod(target_method);
		List<TestData> testDatas = new ArrayList<TestData>();
//		if (target_method_instance != null) {
//			CoverageCriterion criterion = CriterionFactory.getCLGCoverage(this.config.getPathCoverage());
//			if (this.config.isInvalidCase()) {
//				for (CLGNode _clg : target_method_instance.getInvalidCLG(this.config.getPathCoverage())) {
//					FeasiblePathFinder pathFinder = new FeasiblePathFinder(criterion, _clg, context);
//					genPathEcl(fw, testDataFactory, target_method_instance, testDatas, criterion, pathFinder);
//				}
//			} else {
//				FeasiblePathFinder pathFinder = new FeasiblePathFinder(criterion, target_method_instance.getCLG(this.config.getPathCoverage()), context);
//				genPathEcl(fw, testDataFactory, target_method_instance, testDatas, criterion, pathFinder);
//			}
//
//		}
		fw.close();
	}

	private void genPathEcl(FileWriter fw, TestDataFactory testDataFactory, UML2Operation target_method_instance, List<TestData> testDatas, CoverageCriterion criterion, FeasiblePathFinder pathFinder)
			throws IOException, EclipseException {
		Path path;
		int failure_count = 0;
		int path_count = 0;
		while ((path = pathFinder.getNextPath()) != null && failure_count < this.config.getFailTrialTime()) {

			if (criterion.isVisitedInfeasiblePath(path.getCLGNodes()) || criterion.isVisitedFeasiblePath(path.getCLGNodes())) {
				break;
			}
			path_count++;
			{
				File test_path_clp_file = File.createTempFile(path.getPredicateName(), ".ecl");
				test_path_clp_file.deleteOnExit();
				CreateObject createObject = new CreateObject(context, path, this.config);
				write_test_case_predicate(path, test_path_clp_file, createObject);
				ecl2data.compile(test_path_clp_file);

				try {
					ecl2data.multiplexityValidation(createObject.getPredicateName());
					System.out.println(path.getPredicateName());
					ecl2data.solvingCSP(path.getPredicateName(), this.config.getSolvingTimeout());
				} catch (SolvingFailException | SolvingTimeOutException e) {
					criterion.addInfeasiblePath(path.getCLGNodes());
					failure_count++;
					fw.append(e.getMessage() + "\n");
					fw.flush();
					continue;
				}
			}
			for (Path pathVariants : path.getBoundaryCombinationVariants()) {
				File test_path_clp_file = new File(this.config.getOutputFolder().getFile(), pathVariants.getPredicateName() + ".ecl");
				CreateObject createObject = new CreateObject(context, pathVariants, this.config);
				write_test_case_predicate(pathVariants, test_path_clp_file, createObject);
				ecl2data.compile(test_path_clp_file);

				fw.append(pathVariants.getPredicateName() + ",");
				for (UML2Class clazz : context.getClasses()) {
					fw.append(this.config.getRangeOfInstance(clazz) + ",");
				}
				for (Association asc : context.getAssociations()) {
					fw.append(this.config.getRangeOfInstance(asc) + ",");
				}
				Date beforeSolvingTime = new Date();
				try {
					String testDataInParsableFormat = ECLiPSeCompoundTerm.toParsableFormat(ecl2data.solvingCSP(pathVariants.getPredicateName(), this.config.getSolvingTimeout()));
					Date afterSolvingTime = new Date();
					File test_data_file = new File(this.config.getOutputFolder().getFile(), String.format("%s%s" + File.separatorChar + String.format("%s_data.txt", pathVariants.getPredicateName()),
							target_method_instance.getOwner().getName(), target_method_instance.getName()));
					this.hook.writeFile(testDataInParsableFormat, test_data_file);
					testDatas.add(testDataFactory.ConvResult2Data(pathVariants, testDataInParsableFormat, this.config.isInvalidCase()));
					criterion.addFeasiblePath(path.getCLGNodes());
					this.csp_path_list.add(path);
					fw.append(Long.toString((afterSolvingTime.getTime() - beforeSolvingTime.getTime())) + "\n");
					fw.flush();
				} catch (SolvingFailException | SolvingTimeOutException e) {
					criterion.addInfeasiblePath(path.getCLGNodes());
					Date afterSolvingTime = new Date();
					failure_count++;
					if (e instanceof SolvingTimeOutException) {
						test_path_clp_file.renameTo(new File(test_path_clp_file.getParentFile(), "timeout_" + this.config.getSolvingTimeout() + "_" + test_path_clp_file.getName()));
						fw.append(e.getMessage() + "\n");
					} else {
						test_path_clp_file.renameTo(new File(test_path_clp_file.getParentFile(), "fail_" + test_path_clp_file.getName()));
						fw.append(e.getMessage() + String.format(" %d", (afterSolvingTime.getTime() - beforeSolvingTime.getTime())) + "\n");
					}
					fw.flush();
					System.err.println(e.getMessage());
				}
			}
		}

		if (!criterion.meetRequirement()) {
			System.err.println("not meet criterion");
		}
		if (failure_count >= this.config.getFailTrialTime()) {
			System.err.println("has fail over " + this.config.getFailTrialTime() + " times, please make sure that the model is validated");
		}
		if (path_count == 0) {
			System.err.println("can not find any path on graph");
		}
		if (testDatas.size() > 0) {
			TestSuiteGen testSuite = new TestSuiteGen(testDatas, context, this.config);
			testSuite.generateTestSuite();
		}
	}

	private void write_test_case_predicate(Path path, File test_path_clp_file, CreateObject createObject) throws IOException, EclipseException {
		try (StringWriter string_writer = new StringWriter(); PrintWriter print_writer = new PrintWriter(string_writer)) {
			print_writer.println(path.getEntirePredicate(new HashMap<String, String>()));
			for (ASTNode node : path.getASTNodes()) {
				GraphVisitor<ASTNode> bfs = new GraphVisitor<ASTNode>(GraphVisitor.TRAVERSAL_ORDER.PREORDER, new QueueFrontier<ASTNode>());
				bfs.traverse(node, new NodeVisitHandler<ASTNode>() {
					@Override
					public void visit(ASTNode current_node) {
						if (!predicate_pool.contains(current_node)) {
							predicate_pool.add(current_node);
							print_writer.println(current_node.getEntirePredicate(new HashMap<String, String>()));
						}
					}
				});
			}
			print_writer.println(createObject.getEntirePredicate(new HashMap<String, String>()));
			System.out.println("test_path_clp_file name: "+test_path_clp_file);
			this.hook.writeFile(string_writer.toString(), test_path_clp_file);
		}
	}

	public void disconnectCLPSolver() {
		ecl2data.Destroy();
	}

	public void connectCLPSolver() throws EclipseException, IOException {
		ecl2data = Ecl2DataFactory.getEcl2DataInstance();
	}

	public void genTestCases() throws IOException, FileNotFoundException, JSONException, EclipseException {
		FileWriter fw = new FileWriter(this.config.getReportFile(), true);
		fw.write((new Date()).toString() + "\n");
		fw.write("test case,");
		for (UML2Class clazz : context.getClasses()) {
			fw.write(clazz.getName() + ",");
		}
		for (Association asc : context.getAssociations()) {
			fw.write(asc.getName() + ",");
		}
		fw.write("time(ms)\n");
		fw.close();
		this.connectCLPSolver();
		this.generateCLPForMethodSimulation();
		Date beforeSolvingTime = new Date();
		for (JSONObject config : this.detail_config_list) {
			fw = new FileWriter(this.config.getReportFile(), true);
			Date beforeSolvingTimeForAMethod = new Date();
			this.config.readConfiguration(config.toString());
			this.findFeasiblePathAndGetSolution();
			Date afterSolvingTimeForAMethod = new Date();
			fw.write(config.optString("target_class") + "::" + config.optString("target_method") + "," + (afterSolvingTimeForAMethod.getTime() - beforeSolvingTimeForAMethod.getTime()) + "\n");
			fw.close();
		}
		Date afterSolvingTime = new Date();
		fw = new FileWriter(this.config.getReportFile(), true);
		fw.write("Total," + (afterSolvingTime.getTime() - beforeSolvingTime.getTime()) + "\n");
		fw.close();
		this.hook.when_gen_path_graph(context, config, csp_path_list);
		this.disconnectCLPSolver();
	}

	private static List<JSONObject> parseConfigFile(File config_file, Model context) throws FileNotFoundException, IOException, JSONException {
		BufferedReader reader = new BufferedReader(new FileReader(config_file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		reader.close();
		List<JSONObject> config_list = new ArrayList<JSONObject>();
		org.json.JSONTokener tokener = new org.json.JSONTokener(stringBuilder.toString());
		Object config_content = tokener.nextValue();
		if (config_content instanceof JSONArray) {
			JSONArray ary = (JSONArray) config_content;
			for (int i = 0; i < ary.length(); i++) {
				config_list.add(ary.getJSONObject(i));
			}
		} else {
			config_list.add((JSONObject) config_content);
		}
		List<JSONObject> clone_config_list = new ArrayList<JSONObject>(config_list);
		for (JSONObject config : clone_config_list) {
			if (config.optBoolean("aem")) {
				config_list.remove(config);
				UMLCoverageCriterion aem = CriterionFactory.getAEMCriterion();
				aem.analysisModel(context);
				config_list.addAll(aem.getCardinality(config));
			}
		}
		return config_list;
	}
}