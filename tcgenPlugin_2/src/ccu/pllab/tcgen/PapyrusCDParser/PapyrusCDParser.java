package ccu.pllab.tcgen.PapyrusCDParser;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.UMLFactory;

public class PapyrusCDParser {
	private SingleCDParser parser = null ;
	private File testCD ;
	private ArrayList<File> ref = null ;
	private ArrayList<ClassInfo> testClassList = null ;
	private ArrayList<ClassInfo> refClassList = null ;
	
	// constructor ==============================================================================
	// 單一圖
	public PapyrusCDParser(File cd) {
		if(cd.exists()) {
			testCD = cd;
		}
		else System.err.println("No such file!");
	} 
	
	// 其中有自訂型别需參考其他類別圖
	public PapyrusCDParser(File cd, ArrayList<File> ref) {
		if(cd.exists()) {
			testCD = cd ;
			if( ref != null && ref.size() != 0 ) {
				for(int i = 0 ; i < ref.size() ; i++) {
					if( ref.get(i).exists() ) ;
					else System.err.println("No such file!");
				} // for
				
				this.ref = ref;
			} // if
		} // if
		
		else System.err.println("No such file!");
	}
	// constructor ==============================================================================
	
	
	
	// 外部拿取資料 method =======================================================================
	// Get package name
	public String getPkgName() {
		return parser.getPkgName();   // XML根節點
	}
	
	// Get Class List
	public ArrayList<ClassInfo> getClassList() {
		return testClassList;
	}
	
	
	// 輸入Class的名字, 只回傳名字符合的Class的資訊
	public ClassInfo findClass( String name ) {
		if( testClassList == null ) {
			return null ;
		} // if
		
		else {
			for( int i = 0 ; i < testClassList.size() ; i++) {
				if ( testClassList.get(i).getName().equals(name) )
					return testClassList.get(i);
			} // for
			
			return null;
		} // else
	} // findClass()
	

	
	// 外部拿取資料 method =======================================================================
	
	
	public ClassDiagram Parse() {
		if( ref != null ) {
			refClassList = new ArrayList<ClassInfo>();
			for(int i = 0 ; i < ref.size() ; i++) {
				parser = new SingleCDParser(ref.get(i));
				parser.Parse();
				parser.changeTypeStr();
				System.out.println(parser.getClassList().size());
				
				for(int j = 0 ; j < parser.getClassList().size(); j++ ) {
					refClassList.add(parser.getClassList().get(j));
				} // for
			} // for
		} // if

		parser = new SingleCDParser(testCD);
		parser.Parse();
		parser.changeTypeStr();
		testClassList = parser.getClassList();
		//  *****
		if( ref != null ) searchTypeStrFromRef();
		// *****
		// testClassList = parser.getClassList();
		checkTypeIsArray();
		
		ClassDiagram cd = new ClassDiagram( parser.getPkgName(), testClassList, refClassList ) ;
		return cd ;
	} // Parse()
	
	
	private void searchTypeStrFromRef() {
		for( int k = 0 ; k < testClassList.size() ; k++ ) {
		    // class local variable
            for(int j = 0 ; testClassList.get(k).getProperties() != null && 
            		j < testClassList.get(k).getProperties().size();j++) {
            	VariableInfo p = testClassList.get(k).getProperties().get(j);
            	p.setType(searchRef(p.getType()));
            	
            	// if(p.getSize() != null) p.setType(p.getType()+"[]"); // size非空即為陣列
		    } // for
            

		    // class method 
            for(int j = 0 ; testClassList.get(k).getOperations() != null &&
            		j < testClassList.get(k).getOperations().size();j++){
            	OperationInfo o=testClassList.get(k).getOperations().get(j);
            	// class method parameter
            	for(int para_i = 0 ; o.getParameter() != null && 
            			para_i < o.getParameter().size();para_i++) {
            		VariableInfo parameter = o.getParameter().get(para_i);
            		parameter.setType(searchRef(parameter.getType()));
            		// if(parameter.getSize() != null) parameter.setType(parameter.getType()+"[]"); // size非空即為陣列
                } // for
            	
            	// return type
            	VariableInfo rt = o.getReturnType() ;
            	rt.setType(searchRef(rt.getType()));
            	// *** if(o.getReturnType().getSize() != null) o.setType(p.getType()+"[]"); // size非空即為陣列
            }//for operation
			
		} // for
		
	} // changeTypeStr()
	
	
	
	public String searchRef(String type) {
		String s = type ;
		for(int k = 0 ; k < refClassList.size();k++ ) {
			
			if (type.equals(refClassList.get(k).getID())) {
				// System.out.println(type + "**"+this.classList.get(k).getID() + " "+ this.classList.get(k).getName());
				s = refClassList.get(k).getName();
			}
		} //for
		
		return s;
	} // searchRef()
	
	
	
	private void checkTypeIsArray() {
		for( int k = 0 ; k < testClassList.size() ; k++ ) {
		    // class local variable
            for(int j = 0 ; testClassList.get(k).getProperties() != null && 
            		j < testClassList.get(k).getProperties().size();j++) {
            	VariableInfo p = testClassList.get(k).getProperties().get(j);
            	p.setType(searchRef(p.getType()));
            	
            	if(!p.getLowerValue().equals("1") && !p.getUpperValue().equals("1") )
            		p.setType(p.getType()+"[]"); // size非空即為陣列
		    } // for
            

		    // class method 
            for(int j = 0 ; testClassList.get(k).getOperations() != null &&
            		j < testClassList.get(k).getOperations().size();j++){
            	OperationInfo o=testClassList.get(k).getOperations().get(j);
            	// class method parameter
            	for(int para_i = 0 ; o.getParameter() != null && 
            			para_i < o.getParameter().size();para_i++) {
            		VariableInfo parameter = o.getParameter().get(para_i);
            		parameter.setType(searchRef(parameter.getType()));
                	if(!parameter.getLowerValue().equals("1") && !parameter.getUpperValue().equals("1") )
                		parameter.setType(parameter.getType()+"[]"); // size非空即為陣列
                } // for
            	
            	// return type
            	VariableInfo rt = o.getReturnType() ;
            	if(!rt.getLowerValue().equals("1") && !rt.getUpperValue().equals("1") )
            		rt.setType(rt.getType()+"[]"); // size非空即為陣列

            }//for operation
			
		} // for
		
		
	} // changeTypeStr()
	
	
	/*
	public void Test() {
		// Create the new Imported Package element 
		PackageImport packageImport = UMLFactory.eINSTANCE.createPackageImport();
		
		AbstractImportHandler a = new AbstractImportHandler();

		// Load library to import
		Package importedPackage = EMFHelper.reloadIntoContext(libraryToImport, targetElement);

		// Add to target model 
		targetElement.getPackageImports().add(packageImport);
		packageImport.setImportedPackage(importedPackage);
	}
	*/
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		    // File uml = new File("C:/Users/p/Downloads/coffeeMachineClass.uml") ;
			File uml = new File("D:\\Eclipse_Code\\AA\\cal\\model2.uml") ;
			 ArrayList<File> ref = new  ArrayList<File>();
			 ref.add(new File("D:\\Eclipse_Code\\AA\\cal\\model3.uml")) ;
			
		    PapyrusCDParser parser = new PapyrusCDParser(uml, ref) ;


		    parser.Parse();
		    System.out.println("Package Name :" + parser.getPkgName());   // package name

		    for( int i = 0 ; i < parser.getClassList().size() ; i++ ) {
		    	ClassInfo c = parser.getClassList().get(i);
			    System.out.println("Class Name: ") ;
		    	System.out.println((i+1) + " "+c.getName() + " " + c.getID() +"\n");
		    	
		    	System.out.println("Attributes: ");
		    	for(int j= 0; c.getProperties()!= null && j < c.getProperties().size();j++ ) {
		    		VariableInfo p = c.getProperties().get(j);
		    		System.out.println(p.getType() + " " + p.getName() + " " + p.getID() + " " + p.getVisibility()
		    		+" " + p.getLowerValue() + " " + p.getUpperValue());
		    	}
		    	
		    	System.out.println("\nOperations: ");
		    	for(int k= 0; c.getOperations()!= null && k < c.getOperations().size();k++ ) {
		    		OperationInfo o = c.getOperations().get(k);
		    		System.out.println(o.getReturnType() +" " + o.getName() + " " + o.getID() + " " + o.getVisibility());
		    		System.out.println("Parameter: ");
		    	    for(int index = 0 ;o.getParameter()!= null && index < o.getParameter().size();index++) {
			    		VariableInfo p = o.getParameter().get(index);
			    		System.out.println(p.getType() + " " + p.getName() + " " + p.getID() + " " + p.getVisibility()
			    		+" " + p.getLowerValue() + " " + p.getUpperValue());
		    	    }
		    	    System.out.println();
		    	}
		    	System.out.println("\n================================");
		    }

		} catch (Exception e) {
			 e.printStackTrace();
	    }
	}
}
