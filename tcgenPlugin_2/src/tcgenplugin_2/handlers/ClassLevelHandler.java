package tcgenplugin_2.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.json.JSONException;

import com.parctechnologies.eclipse.EclipseException;

import ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGStartNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.AbstractSyntaxTree.AbstractSyntaxTreeNode;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.exe.main.*;
import ccu.pllab.tcgen.sd2clg.SD2TestCase;
import ccu.pllab.tcgen.transform.AST2CLG;
import ccu.pllab.tcgen.transform.CLG2Path;
import ccu.pllab.tcgen.transform.OCL2AST;
import ccu.pllab.tcgen.AbstractSyntaxTree.SymbolTable;
import ccu.pllab.tcgen.DataWriter.DataWriter;


public class ClassLevelHandler extends AbstractHandler {

	public static String config_file_path;
	public static String log4j_property_path;
	public static String ocl_model_path;
	public static String output_folder_path = null;
	public static String CurrentEditorProjectPath; //軟測外掛用，存取當前選取Editor路徑
	public static String CurrentEditorName;
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
	
	
	private File ocl,classUml, stateUml;
	private static String text,umlText;
	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		msort=false;

		CurrentEditorName = getCurrentFileRealPath().getName();
		CurrentEditorName = getFileNameNoEx(CurrentEditorName);
		System.out.println("當前使用editor檔案名稱"+CurrentEditorName);
		
		String CurrentEditorPath = getCurrentFileRealPath().getLocation().toOSString();
		CurrentEditorProjectPath = CurrentEditorPath.substring(0, CurrentEditorPath.lastIndexOf("\\spec\\"+CurrentEditorName));
		System.out.println("當前使用editor project資料夾路徑"+CurrentEditorProjectPath);
		
//		預設為DCC覆蓋度，有邊界值分析
		Main.criterion=Criterion.dcc;
		Main.boundary_analysis=true;
		Main.TestType = "ClassLevel";
		
		DataWriter.output_folder_path = ClassLevelHandler.CurrentEditorProjectPath;
		
		Path SDumlPath = Paths.get(DataWriter.output_folder_path+"/spec/"+ClassLevelHandler.CurrentEditorName+"SD.uml");
		stateUml = SDumlPath.toFile();
		
		Path CDumlPath = Paths.get(DataWriter.output_folder_path+"/spec/"+ClassLevelHandler.CurrentEditorName+".uml");
		classUml = CDumlPath.toFile();
		
		Path OclPath = Paths.get(DataWriter.output_folder_path+"/spec/"+ClassLevelHandler.CurrentEditorName+".ocl");
		ocl = OclPath.toFile();	
		
//		類別層級
		try {
			SD2TestCase sd2TC = new SD2TestCase(stateUml, classUml, ocl);
			
			JOptionPane.showMessageDialog(null, "執行成功", "結果", JOptionPane.INFORMATION_MESSAGE );
			Main.invCLP="";
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	
	public static IFile getCurrentFileRealPath(){
        IWorkbenchWindow win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        
        IWorkbenchPage page = win.getActivePage();
        if (page != null) {
            IEditorPart editor = page.getActiveEditor();
            if (editor != null) {
                IEditorInput input = editor.getEditorInput();
                if (input instanceof IFileEditorInput) {
//                    return ((IFileEditorInput)input).getFile().getLocation().toOSString();
                    return ((IFileEditorInput)input).getFile();
                }
            }
        }
        return null;
	}
	
	public static String getFileNameNoEx(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf(".");   
            if ((dot >-1) && (dot < (filename.length()))) {   
                return filename.substring(0, dot);   
            }   
        }   
        return filename;   
    } 
}
