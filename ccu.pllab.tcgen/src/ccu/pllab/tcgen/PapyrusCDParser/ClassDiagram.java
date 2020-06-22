package ccu.pllab.tcgen.PapyrusCDParser;

import java.util.ArrayList;


public class ClassDiagram {
	
	private String pkgName ;
	private ArrayList<ClassInfo> testClassList ;
	private ArrayList<ClassInfo> refClassList ;
	
	public ClassDiagram() {
		pkgName = "" ;
		testClassList = null ;
		refClassList = null ;
	}
	
	public ClassDiagram(String pkg, ArrayList<ClassInfo> test, ArrayList<ClassInfo> ref) {
		pkgName = pkg ;
		testClassList = test ;
		refClassList = ref ;		
	}
	
	
	
	public String getPkgName() {
		return pkgName ;
	}
	
	public ArrayList<ClassInfo> getAllClass() {
		return testClassList ;
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
	
	
	// 輸入Class的名字, 只回傳名字符合的reference Class的資訊
	public ClassInfo findRefClass( String name ) {
		if( refClassList == null ) {
			return null ;
		} // if
		
		else {
			for( int i = 0 ; i < refClassList.size() ; i++) {
				if ( refClassList.get(i).getName().equals(name) )
					return refClassList.get(i);
			} // for
			
			return null;
		} // else
	} // findRefClass()

}
