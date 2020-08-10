package ccu.pllab.tcgen.srcASTVisitor;

 
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.xml.sax.SAXException;

import com.parctechnologies.eclipse.EclipseException;

import ccu.pllab.tcgen.AbstractCLG.*;

import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestCaseFactory;
import ccu.pllab.tcgen.TestCase.TestScriptGenerator;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.clgGraph2Path.CoverageCriterionManager;
import ccu.pllab.tcgen.exe.main.Main;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;

public class SrcVisitProcess {

	public SrcVisitProcess(String path) throws IOException, ParserConfigurationException, SAXException, TemplateException, ModelAccessException, ParseException, EclipseException {
		TestCaseFactory tcFactory = new TestCaseFactory();
		/******************************************************/

		CompilationUnit comp = SrcJdtAstUtil.getCompilationUnit(path);

		SrcVisitorUnit visitor = new SrcVisitorUnit();
		comp.accept(visitor);
		CLGCriterionTransformer clgTF = new CLGCriterionTransformer();
		for (CLGGraph graph : visitor.getCLGGraph()) {
			if (Main.criterion.equals(Criterion.dcc) || Main.criterion.equals(Criterion.dccdup)) {
				graph = clgTF.CriterionTransformer(graph, Criterion.dcc);
			} else if (Main.criterion.equals(Criterion.mcc) || Main.criterion.equals(Criterion.mccdup)) {
				graph = clgTF.CriterionTransformer(graph, Criterion.mcc);
			}else{
				
			}
			this.drawGraph(graph);

			CoverageCriterionManager criterionManger = new CoverageCriterionManager();
			criterionManger.init(graph);
			//
			TestScriptGenerator testScriptGenerator = new TestScriptGenerator();
			testScriptGenerator.init(criterionManger.genTestSuite());
			testScriptGenerator.genTestScript();
			tcFactory.createTestCase(((CLGStartNode) graph.getStartNode()).getGraphName());
		}

	}

	public void drawGraph(CLGGraph graph) throws IOException {
		String content = graph.graphDraw();
		String fileName = ((CLGStartNode) graph.getStartNode()).getClassName() + "_" + ((CLGStartNode) graph.getStartNode()).getMethodName();
		DataWriter.writeInfo(content, fileName, "dot", DataWriter.output_folder_path, "CLG");

		new ProcessBuilder("dot", "-Tpng", DataWriter.output_folder_path + "\\CLG\\" + fileName + ".dot", "-o", DataWriter.output_folder_path + "\\CLG\\" + fileName + ".png").start();
		/* need to find better approach */
	}
}
