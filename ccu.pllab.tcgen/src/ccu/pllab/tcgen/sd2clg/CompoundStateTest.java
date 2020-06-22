package ccu.pllab.tcgen.sd2clg;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.xml.sax.SAXException;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGStartNode;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestDataClassLevel;
import ccu.pllab.tcgen.TestCase.TestScriptGeneratorClassLevel;
import ccu.pllab.tcgen.ast.ASTUtil;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.clgGraph2Path.CoverageCriterionManager;
import ccu.pllab.tcgen.exe.main.Main;
import ccu.pllab.tcgen.libs.DresdenOCLASTtoInternelAST;
import ccu.pllab.tcgen.libs.pivotmodel.ClassDiagInfo;
import ccu.pllab.tcgen.libs.pivotmodel.ClassDiagToJson;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.type.TypeFactory;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;



import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;



import com.parctechnologies.eclipse.EclipseException;

/*import ccu.pllab.tcgen.AbstractCLG.CLGCriterionTransformer;
import ccu.pllab.tcgen.sd2clg.SD2CLG;
import ccu.pllab.tcgen.sd2clg.SDXML2SD;
import ccu.pllab.tcgen.sd2clg.StateDigram;
import ccu.pllab.tcgen.sd2clp.SD2CLP;*/
//import ccu.pllab.tcgen.clgGraph2Path.CoverageCriterionManager.FlowCriterion;
import ccu.pllab.tcgen.clgGraph2Path.CoverageCriterionManager;
public class CompoundStateTest {//*****This is like-main 
 
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TemplateException, ModelAccessException, ParseException, org.apache.commons.cli.ParseException, JSONException, EclipseException {
		// TODO Auto-generated method stub

		SDXML2SD parsd = new SDXML2SD();
		SD2CLG cpsd = new SD2CLG();
		StateDigram st = new StateDigram();
		CLGGraph gtclggraph=new CLGGraph();
		st=new SDXML2SD().convert(
						new File("./CoffeeMachineSD1.uml"),
						//new File("./coffeemachine1.uml"));
				new File("./coffeeMachineClass.uml"));
					//new File("./CoffeeMachineSD.uml"),
			  // 	new File("./tempCoffeeMachine.uml"));
					//new File("./coffeemachine1.uml"));
	//	gtclggraph=cpsd.convert(st);
		
		CLGGraph clg=new SDXML2SD().converts(
				//new File("./CoffeeMachineSD1.uml"),
				new File("./coffeeMachineClass.uml"),
				new File("./coffeemachine1.uml"));
		
			// File("./CoffeeMachineSD.uml"),
	   	//new File("./tempCoffeeMachine.uml"));
		//new File("./A.uml"),
		//new File("./Aclass.uml"));
	//	String content="";
	/*	content += clg.getStartNode().getSuccessor().get(0).toGenImg2();
		for(Object key:clg.getConstraintCollection().keySet())
		{
			content+=clg.getConstraintCollection().get(key).toGenImg2();
		}
		for(int i=0;i<clg.getConnectionNode().size();i++)
		{
			content+=clg.getConnectionNode().get(i).toGenImg2();
		}
		DataWriter.writeInfo(content,
				//clg.getStartNode().getSuccessor().get(0).toGenImg2(), 
				"CLG", "dot", "../examples/ACLG","CLG");
		new ProcessBuilder("dot", "-Tpng", "../examples/ACLG/CLG/CLG.dot",
				"-o", "../examples/ACLG/CLG/CLG.png").start();*/
		//List<ccu.pllab.tcgen.ast.Constraint>OCLCons=parseOCL(new File("./A.ocl"), new File("./Aclass.uml"));
	/*	List<ccu.pllab.tcgen.ast.Constraint>OCLCons=parseOCL(new File("./coffeemachine.ocl"), new File("./CoffeeMachine.uml"));
		
		CoverageCriterionManager coverman = new CoverageCriterionManager();
		Main.criterion=Criterion.dc;
		//coverman.init(clg, Main.criterion,OCLCons);
		coverman.init(gtclggraph, Main.criterion,OCLCons);*/

		/*List<TestDataClassLevel> resulttd = new ArrayList<TestDataClassLevel>();
		resulttd = coverman.genClassLevelTestSuite();
		TestScriptGeneratorClassLevel testscri = new TestScriptGeneratorClassLevel();
		testscri.genTestCase(resulttd,("ccu.pllab.tcgen.TCGenExample.example."+
				st.getSDName()) , st.getSDName(), st.getSDAttribute());*/
	}
	public static List<ccu.pllab.tcgen.ast.Constraint> parseOCL(File ocl, File uml) throws TemplateException, ModelAccessException, IOException, ParseException{
		Model context;
		IModel model;
		ClassDiagInfo class_diag_info;
		ClassDiagToJson class_diag_to_json;
		List<ccu.pllab.tcgen.ast.Constraint>OCLCons;
		//File uml = new File("../Examples/test20170304/coffeemachine.uml");
		class_diag_to_json = new ClassDiagToJson(uml);
		class_diag_info = new ClassDiagInfo(class_diag_to_json);
		context = new Model(class_diag_info);
		TypeFactory.getInstance().setModel(context);
		ASTUtil ast_util = new ASTUtil();
		DresdenOCLASTtoInternelAST ast_constructor = new DresdenOCLASTtoInternelAST(ast_util, context);
		
		StandaloneFacade.INSTANCE.initialize(new URL("file:./log4j.properties"));
		model = StandaloneFacade.INSTANCE.loadUMLModel(uml, new File("./resources/org.eclipse.uml2.uml.resources.jar"));
		//List<tudresden.ocl20.pivot.pivotmodel.Constraint> loaduml= StandaloneFacade.  //test20170304/date/date.ocl"
		//		INSTANCE.parseOclConstraints(model, new File("../Examples/test20170304/coffeemachine.ocl"));
		List<tudresden.ocl20.pivot.pivotmodel.Constraint> loaduml= StandaloneFacade.  //test20170304/date/date.ocl"
				INSTANCE.parseOclConstraints(model, ocl);
		OCLCons = ast_constructor.parseOclTreeNodeFromPivotModel(loaduml); 
		
		return OCLCons;
	}
}
