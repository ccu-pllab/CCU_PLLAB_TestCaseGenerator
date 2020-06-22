package ccu.pllab.tcgen.pathCLPFinder;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.parctechnologies.eclipse.EXDRInputStream;
import com.parctechnologies.eclipse.EclipseEngine;
import com.parctechnologies.eclipse.EclipseException;
import com.parctechnologies.eclipse.EmbeddedEclipse;
import com.parctechnologies.eclipse.FromEclipseQueue;

import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestData;
import ccu.pllab.tcgen.TestCase.TestDataClassLevel;
import ccu.pllab.tcgen.exe.main.Main;
import ccu.pllab.tcgen.pathCLP2data.CLP2Data;
import ccu.pllab.tcgen.pathCLP2data.CLP2DataFactory;
import ccu.pllab.tcgen.pathCLP2data.ECLiPSe_CompoundTerm;
import scala.collection.generic.BitOperations.Int;

public class CLPSolver {
	private CLP2Data clp2data;
	private String EclDirectPath = DataWriter.output_folder_path + "\\ECL\\";

	private List<ECLiPSe_CompoundTerm> sol;
	private TestData testData;
	private TestDataClassLevel testDataclass;
	public CLPSolver() {

	}
	
	public boolean solving(String className, String methodName, int pathNum,int testcaseID, boolean isConstructor, String retType, String objPre, String argPre, String objPost, String argPost,
			String retVal) {
		//this.EclDirectPath = DataWriter.output_folder_path + "C:\\Users\\chienLung\\tcgen\\examples\\output";
		//this.EclDirectPath = "C:\\Users\\chienLung\\tcgen\\examples\\output\\CLG\\ECL\\";
		this.EclDirectPath = Main.output_folder_path+"\\ECL\\";
		File eclFile = new File(EclDirectPath + className + methodName + "_" + pathNum + ".ecl");
		int testCaseID = 1;
		try {
//			
			this.connectCLPSolver();
			this.clp2data.compile(eclFile);
			this.sol = this.clp2data.solvingCSP_term("test" + className + methodName, objPre, argPre, objPost, argPost, retVal, 5);
			this.testData = new TestData(className, methodName, pathNum, testCaseID, isConstructor, retType, this.sol);
			System.out.println("TD: " + testData.toString());
			return true;
		} catch (Exception e) {
			eclFile.renameTo(new File(eclFile.getParentFile(), "fail_" + eclFile.getName()));
			//e.printStackTrace();
			return false;
		}
	}
  
	public boolean solving(String className, String methodName, int pathNum, int testcaseID, boolean isConstructor, String retType, String objPre, String argPre, String objPost, String argPost,
			String retVal,String output_path) {
		this.EclDirectPath = output_path+"\\ECL\\";
		File eclFile = new File(EclDirectPath + className + methodName + "_" + pathNum + ".ecl");
		int testCaseID = 1;
		try {
//			
			this.connectCLPSolver();
			this.clp2data.compile(eclFile);
			this.sol = this.clp2data.solvingCSP_term("test" + className + methodName, objPre, argPre, objPost, argPost, retVal, 4);
			this.testData = new TestData(className, methodName, pathNum, testCaseID, isConstructor, retType, this.sol);
			System.out.println("TD: " + testData.toString());
			return true;
		} catch (Exception e) {
			eclFile.renameTo(new File(eclFile.getParentFile(), "fail_" + eclFile.getName()));
			//e.printStackTrace();
			return false;
		}
	}
	//824
	public boolean solving(String classN, int pathNum, ArrayList<String> methodN){
		File eclFile = null;		
		int testCaseID = 1;
		try {	
//			String pathecl ="src/ccu/pllab/tcgen/TCGenExample824/ECL/";
			String pathecl =ccu.pllab.tcgen.DataWriter.DataWriter.output_folder_path+"/ECL/";
			//this.EclDirectPath = ccu.pllab.tcgen.DataWriter.DataWriter.output_folder_path+"ECL/";
			eclFile = new File(pathecl + classN+"_"+pathNum + ".ecl");
			this.connectCLPSolver();	
			
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPCoffeeMachine.ecl"));
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPInsert.ecl"));
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPWithdraw.ecl"));
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPCook.ecl"));
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPDone.ecl"));
			
			this.clp2data.compile(eclFile);
			
			String comstr = "testpath"+pathNum+"(Obj_pre, Arg_pre, Obj_post, Arg_post, Ret_val).";
			this.sol= this.clp2data.solvingCSP_new(comstr, 5);
			
			if(!this.sol.contains(null)){
				this.testDataclass = new TestDataClassLevel(classN,methodN,pathNum,testCaseID,this.sol);
			}
			else {
				this.testDataclass = null; //pathNum-=1;
			}
//			this.testDataclass = new TestDataClassLevel(classN,methodN,pathNum,testCaseID,this.sol);
			return true;
			
		} catch (Exception e) {
			this.testDataclass = null;
			eclFile.renameTo(new File(this.EclDirectPath + "Fail_"+classN+methodN+"_"+pathNum + ".ecl"));
		//	e.printStackTrace();
			return false;
		}
	}
	
	public TestData getTestData(){
		return this.testData;
	}
	public TestDataClassLevel getTestDataclass(){
		return this.testDataclass;
	}

	public void connectCLPSolver() throws EclipseException, IOException {
		clp2data = CLP2DataFactory.getEcl2DataInstance();
	}

	public void disconnectCLPSolver() {
		clp2data.Destroy();
	}

}
