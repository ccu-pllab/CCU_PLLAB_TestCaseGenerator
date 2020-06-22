package ccu.pllab.tcgen.transform;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.parctechnologies.eclipse.EclipseException;

import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractSyntaxTree.SymbolTable;
import ccu.pllab.tcgen.AbstractSyntaxTree.VariableToken;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestCaseFactory;
import ccu.pllab.tcgen.TestCase.TestScriptGenerator;
import ccu.pllab.tcgen.clgGraph2Path.CLGPath;
import ccu.pllab.tcgen.clgGraph2Path.CLGPathEnumerator;
import ccu.pllab.tcgen.clgGraph2Path.CoverageCriterionManager;
import ccu.pllab.tcgen.exe.main.Main;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;

public class CLG2Path {
	LinkedList<CLGNode> path;
	ArrayList<String> attribute=new ArrayList<String>();
	String invCLP;
	String criterion;
	
	public CLG2Path(ArrayList<CLGGraph> clg, CLGGraph invCLG,SymbolTable symbolTable) throws IOException, ParserConfigurationException, SAXException, TemplateException, ModelAccessException, ParseException, EclipseException
	{	//基本上，黑箱白箱都要用，單純是和張振鴻學長一樣的code
		this.setattribute(invCLG,symbolTable);
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
			String ts = testScriptGenerator.genTestScriptByPreamble();
			((CLGStartNode) clg.get(number).getStartNode()).getMethodParameterTypes();
			testScript += ts;			
			tcFactory.createTestCase(((CLGStartNode) subclg.getStartNode()).getGraphName());
		}
		testScript += "}";
		DataWriter.writeInfo(testScript, className + "Test", "java", DataWriter.output_folder_path, "TestSuites");
	}
	
	
	public LinkedList<CLGNode> getPath()
	{
		return this.path;
	}	
	
	public ArrayList<String> getAttribute(){return this.attribute;}
	
	public String getInvCLP() {return this.invCLP;}
	
	public void setattribute(CLGGraph clg, SymbolTable symbolTable)
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
}
