package ccu.pllab.tcgen.transform;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.parctechnologies.eclipse.EclipseException;

//import antlr.collections.List;
import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractSyntaxTree.SymbolTable;
import ccu.pllab.tcgen.AbstractSyntaxTree.VariableToken;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestCaseFactory;
import ccu.pllab.tcgen.TestCase.TestData;
import ccu.pllab.tcgen.TestCase.TestScriptGenerator;
import ccu.pllab.tcgen.clgGraph2Path.CLGPath;
import ccu.pllab.tcgen.clgGraph2Path.CLGPathEnumerator;
import ccu.pllab.tcgen.clgGraph2Path.CoverageCriterionManager;
import ccu.pllab.tcgen.exe.main.Main;
import tcgenplugin_2.handlers.BlackBoxHandler;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;

public class CLG2Path {
	private LinkedList<CLGNode> path;
	private ArrayList<String> attribute=new ArrayList<String>();
	private String invCLP;
	private String criterion;
	private String className;
	private List<TestData> testData;
	private String testScript;
	
	public CLG2Path() throws IOException, ParserConfigurationException, SAXException, TemplateException, ModelAccessException, ParseException, EclipseException, TransformerException{
		
	}
	
	public CLG2Path(ArrayList<CLGGraph> clg, CLGGraph invCLG,SymbolTable symbolTable,File uml) throws IOException, ParserConfigurationException, SAXException, TemplateException, ModelAccessException, ParseException, EclipseException, TransformerException, CloneNotSupportedException
	{	//基本上，黑箱白箱都要用，單純是和張振鴻學長一樣的code
		this.setAttribute(invCLG,symbolTable);
		Main.attribute=this.attribute;
		if(this.invCLP!=null)
		Main.invCLP=this.invCLP.substring(0, this.invCLP.length()-2);
		TestCaseFactory tcFactory = new TestCaseFactory();
		//2019/07/01 修改
		String className ="null";
		className = ((CLGStartNode) clg.get(0).getStartNode()).getClassName();
		((CLGStartNode) clg.get(0).getStartNode()).getMethodParameterTypes();
		String testScript = "import junit.framework.TestCase;\n" + 
				"import java.util.ArrayList;\n" + 
				"import java.util.Arrays;\n\n" +
				"public class "+className+"Test extends TestCase {\n";
		for(int number=0;number<clg.size();number++)
		{
			CoverageCriterionManager manager=new CoverageCriterionManager();	
			CLGGraph subclg=clg.get(number);
			((CLGStartNode)subclg.getStartNode()).setClassAttributes(this.attribute);;
			manager.init(subclg);
			TestScriptGenerator testScriptGenerator = new TestScriptGenerator();
			testScriptGenerator.init(manager.genTestSuite());
			/*add preamble 2019/06/24*/
			//testScriptGenerator.genTestScript();
			String ts = testScriptGenerator.genTestScriptByPreamble(uml);
			((CLGStartNode) clg.get(number).getStartNode()).getMethodParameterTypes();
			testScript += ts;			
			tcFactory.createTestCase(((CLGStartNode) subclg.getStartNode()).getGraphName());
		}
		testScript += "}";
		DataWriter.writeInfo(testScript, className + "Test", "java", DataWriter.output_folder_path, "test");
	}
	
	
	public LinkedList<CLGNode> getPath()
	{
		return this.path;
	}	
	
	public ArrayList<String> getAttribute(){return this.attribute;}
	
	public String getInvCLP() {return this.invCLP;}
	
	public void setAttribute(CLGGraph clg, SymbolTable symbolTable)
	{
		if(clg!=null)
		{
			String bodyCLP = "";
			ArrayList<CLGConstraint> constraintList = new ArrayList<CLGConstraint>();
			CLGPathEnumerator clgPathEnumerator = new CLGPathEnumerator();
			clgPathEnumerator.init(clg);
			CLGPath path=clgPathEnumerator.next();
			CLGNode endNode = path.getPathNodes().get(path.getPathNodes().size() - 1);
			CLGStartNode startNode = (CLGStartNode) path.getPathNodes().get(0);
			for (int i = 1; i < path.getPathNodes().size() - 1; i++) {
				if (path.getPathNodes().get(i) instanceof CLGConstraintNode) {
					((CLGConstraintNode) path.getPathNodes().get(i)).getId();
					constraintList.add(((CLGConstraintNode) path.getPathNodes().get(i)).getConstraint().clone());
				}
			}
			
			ArrayList<String> tempAttribute=new ArrayList<String>();
			for (CLGConstraint c : constraintList) {
					bodyCLP += c.getCLPInfo() + ",\n";
					tempAttribute.addAll(c.getInvCLPInfo());
			}
			
			for(int i=0;i<tempAttribute.size();i++)
				for(int j=tempAttribute.size()-1;j>i;j--)
					if(tempAttribute.get(i).equals(tempAttribute.get(j)))
						tempAttribute.remove(j);
			for(String s:tempAttribute)
			{
				s=s.replace("Self_", "");
		//		this.attribute.add(s);
			}
			bodyCLP=bodyCLP.replaceAll("Self_","");
			this.invCLP=bodyCLP;
		}
		//else
		//{
			ArrayList<VariableToken> attribute=((SymbolTable) symbolTable).getAttribute();
			for(VariableToken key:attribute)
			{
				this.attribute.add(key.getVariableName());
			}
		//}
	}
	
	public void init(ArrayList<CLGGraph> clg) {
		Main.attribute=this.attribute; //有使用到
		if(this.invCLP!=null)
			Main.invCLP=this.invCLP.substring(0, this.invCLP.length()-2);
		
		this.className = ((CLGStartNode) clg.get(0).getStartNode()).getClassName();
		
		String packagePath = "";
		switch (BlackBoxHandler.coverageCriteria) {
		case "DC":					 	
			packagePath = "package team.ccu.pllab.dc;";
			break;
		case "DCC":					 	
			packagePath = "package team.ccu.pllab.dcc;";
			break;
		case "MCC":					 	
			packagePath = "package team.ccu.pllab.mcc;";
			break;
		default :
			packagePath = "package team.ccu.pllab.dcc;";
						
	}
		this.testScript = packagePath + "\n" + 
				"import junit.framework.TestCase;\n" + 
				"import java.util.ArrayList;\n" + 
				"import java.util.Arrays;\n" +
				"import org.junit.Test;\n\n" +
				"public class "+this.className+"Test extends TestCase {\n";
	}
	
	public List<TestData> genTestData(ArrayList<CLGGraph> clg, int number) throws IOException, CloneNotSupportedException{
		CoverageCriterionManager manager=new CoverageCriterionManager();	
		CLGGraph subclg=clg.get(number);
		((CLGStartNode)subclg.getStartNode()).setClassAttributes(this.attribute);
		manager.init(subclg);
		this.testData = manager.genTestSuite();
		
		return this.testData;
	}
	
	public void genTestScript(File uml) throws ParserConfigurationException, SAXException, IOException, TemplateException, ModelAccessException, ParseException, EclipseException, TransformerException {
//		TestScriptGenerator testScriptGenerator = new TestScriptGenerator();
//		testScriptGenerator.init(this.testData);
//		String ts = testScriptGenerator.genTestScriptByPreamble(uml);
//		this.testScript += ts;
		
		if(this.className.contains("Array") || this.className.contains("Date") || this.className.contains("Time")) {
			TestScriptGenerator testScriptGenerator = new TestScriptGenerator();
			testScriptGenerator.init(this.testData);
			String ts = testScriptGenerator.genTestCase();
			this.testScript += ts;
		}else {
			TestScriptGenerator testScriptGenerator = new TestScriptGenerator();
			testScriptGenerator.init(this.testData);
			String ts = testScriptGenerator.genTestScriptByPreamble(uml);
			this.testScript += ts;
		}
	}
	
	public String getClassName() {
		return this.className;
	}
	
	public List<TestData> getTestData(){
		return this.testData;
	}
	
	public String getTestScript() {
		return this.testScript;
	}
	
	public void genTestScripts(ArrayList<CLGGraph> clg, File uml) throws IOException, ParserConfigurationException, SAXException, TemplateException, ModelAccessException, ParseException, EclipseException, TransformerException, CloneNotSupportedException {
		Main.attribute=this.attribute; //有使用到
		if(this.invCLP!=null)
			Main.invCLP=this.invCLP.substring(0, this.invCLP.length()-2);
		
		String className = ((CLGStartNode) clg.get(0).getStartNode()).getClassName();
		
		String testScript = "import junit.framework.TestCase;\n" + 
				"import java.util.ArrayList;\n" + 
				"import java.util.Arrays;\n\n" +
				"public class "+className+"Test extends TestCase {\n";
		
		for(int number=0;number<clg.size();number++)
		{
			CoverageCriterionManager manager=new CoverageCriterionManager();	
			CLGGraph subclg=clg.get(number);
			((CLGStartNode)subclg.getStartNode()).setClassAttributes(this.attribute);
			manager.init(subclg);
			TestScriptGenerator testScriptGenerator = new TestScriptGenerator();
			testScriptGenerator.init(manager.genTestSuite());
			String ts = testScriptGenerator.genTestScriptByPreamble(uml);
			testScript += ts;			
		}
		testScript += "}";
		DataWriter.writeInfo(testScript, className + "Test", "java", DataWriter.output_folder_path, "test");
	}
}
