//maker:王建瓏
package ccu.pllab.tcgen.transform;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


import ccu.pllab.tcgen.AbstractSyntaxTree.*;
import ccu.pllab.tcgen.exe.main.Main;
import ccu.pllab.tcgen.oclRunner.OclLexer;
import ccu.pllab.tcgen.oclRunner.OclParser;
import ccu.pllab.tcgen.PapyrusCDParser.*;


import java.util.ArrayList;


import java.io.File; 
public class OCL2AST {
	private PackageExp ast;//抽象語法樹
	private SymbolTable symbolTable;

	public OCL2AST(File ocl,File classuml) throws Exception 
	{
		  Main.className="";
		 makeAST(ocl);
	      makeSymbolTable(classuml);
	      Main.symbolTable=this.symbolTable;
	      typeToAst(); 
	}	
	
	public AbstractSyntaxTreeNode getAbstractSyntaxTree()
	{
		return this.ast;
	}		
	
	public SymbolTable getSymbolTable()
	{
		return this.symbolTable;
	}
	  
	public void makeAST(File ocl) throws IOException
	{//OCL轉換成抽象語法樹
		InputStream is = System.in;
	      if ( ocl!=null ) is = new FileInputStream(ocl);//測試的OCL檔
	      ANTLRInputStream input = new ANTLRInputStream(is);
	      OclLexer lexer = new OclLexer(input);
	      CommonTokenStream tokens = new CommonTokenStream(lexer);
	      OclParser parser = new OclParser(tokens);
    
	      this.ast= parser.packageDeclarationCS().astRoot;//跑完編譯後，會回傳AST結構tree
	      is.close();
	}
	
	public void makeSymbolTable(File classuml)
	{
		//類別圖分析..黎怡伶做的
			SingleCDParser classParser = new SingleCDParser(classuml) ;
			classParser.Parse();
			classParser.changeTypeStr();
			
		//開始做symbol table
			this.symbolTable=new SymbolTable(classParser.getPkgName());
		    ClassInfo c = classParser.getClassList().get(0);
		    Main.className=c.getName();
		   
		    for(int j= 0; c.getProperties()!= null && j < c.getProperties().size();j++ ) {
		    	VariableInfo p = c.getProperties().get(j);
		  
		    	//....處理sort
		    	if(p.getType().equals("ArrayList"))
		    	{
		    		p.setType("int[x]");
		    		Main.isArraylist=true;
		    		Main.doArray=true;
		    	}
		    	else if(p.getType().equals("ArrayList<ArrayList>"))
		    	{
		    		p.setType("int[x][x]");
		    		Main.isArraylist=true;
		    		Main.twoD=true;
		    	}
		    	else if(p.getType().contains("int["))
		    	{
		    		
		    		if(p.getLowerValue().equals(p.getUpperValue())&& p.getLowerValue()!="1")
			    		//p.setType(p.getType()+"["+p.getLowerValue()+"]");
		    		{
		    			if(p.getLowerValue().contains("col"))
		    			p.setType("int[x][x]");
		    			else
		    				p.setType(p.getType()+"["+p.getLowerValue()+"]");
		    		}
			    	else if(!p.getLowerValue().equals(p.getUpperValue()))
			    		p.setType(p.getType()+"["+p.getLowerValue()+"..."+p.getUpperValue()+"]");
		    		Main.twoD=true;
		    		Main.doArray=true;
		    	}
		    	else {
		    	 if(p.getLowerValue().equals(p.getUpperValue())&& p.getLowerValue()!="1")
		    	//	p.setType("int["+p.getLowerValue()+"]");
		    		 if(p.getLowerValue().equals("size")||p.getLowerValue().equals("bound"))
		    		 {
		    		 p.setType("int[x]");
		    		 Main.doArray=true;
		    		 }
		    		 else {
		    			 p.setType("int["+p.getLowerValue()+"]");
		    			 Main.doArray=true;
					}
		    	else if(!p.getLowerValue().equals(p.getUpperValue()))
		    	{
		    		p.setType("int["+p.getLowerValue()+"..."+p.getUpperValue()+"]");
		    	//	 Main.doArray=true;
		    	}
		    	
		    	}
				//put property into symboltalbe
		    	VariableToken variable=new VariableToken(p.getName(),p.getType());
		    	this.symbolTable.addAttribute(variable);
		    	this.symbolTable.addAttributeMap(variable);
		    }
		    	
		    for(int k= 0; c.getOperations()!= null && k < c.getOperations().size();k++ ) {
		    	OperationInfo o = c.getOperations().get(k);
		    	MethodToken method=new MethodToken(o.getName());
		    	//處理method的回傳值
		    	if(o.getReturnType()==null)
		    		method.setReturnType("OclVoid");
		    	else
		    		method.setReturnType(o.getReturnType().getType());
		    	
		    	//處理參數
		    	for(int index = 0 ;o.getParameter()!= null && index < o.getParameter().size();index++) {
			    	VariableInfo p = o.getParameter().get(index);
			    	if(p.getType().equals("ArrayList"))
			    		p.setType("int[x]");
			    	else if(p.getType().equals("ArrayList<ArrayList>"))
			    	{
			    		p.setType("int[x][x]");
			    		Main.isArraylist=true;
			    		Main.twoD=true;
			    	}
			    	else if(p.getType().contains("int["))
			    	{
			    	//	System.out.println("testtype:"+p.getLowerValue()+p.getUpperValue());
			    		if(p.getLowerValue().equals(p.getUpperValue())&& p.getLowerValue()!="1")
			    		{
				    	//	p.setType(p.getType()+"["+p.getLowerValue()+"]");
			    			if(p.getLowerValue().contains("col"))
				    			p.setType("int[x][x]");
			    			else
			    			{
			    			p.setType(p.getType()+"["+p.getLowerValue()+"]");
			    			}
			    		}
				    	else if(!p.getLowerValue().equals(p.getUpperValue()))
				    		p.setType(p.getType()+"["+p.getLowerValue()+"..."+p.getUpperValue()+"]");
			    	//	System.out.println("testtype:"+p.getType());
			    		Main.twoD=true;
			    		Main.doArray=true;
			    	}
			    	else {
			    	 if(p.getLowerValue().equals(p.getUpperValue())&& p.getLowerValue()!="1")
			    	//	p.setType("int["+p.getLowerValue()+"]");
			    		 if(p.getLowerValue().equals("size")||p.getLowerValue().equals("bound")||p.getLowerValue().contains("Size"))
				    		 p.setType("int[x]");
				    		 else {
				    			 p.setType("int["+p.getLowerValue()+"]");
				    			 Main.doArray=true;
							}
			    	else if(!p.getLowerValue().equals(p.getUpperValue()))
			    	{
			    		p.setType("int["+p.getLowerValue()+"..."+p.getUpperValue()+"]");
			    	//	 Main.doArray=true;
			    	}
			    	}
			    	method.addArgument(new VariableToken(p.getName(),p.getType()));
			    	this.symbolTable.addArgumentMap(new VariableToken(p.getName(),p.getType()));
		    	}
		    	//put method into symboltable
		    	this.symbolTable.addMethod(method);
		    }
		    for(MethodToken methodToken:this.symbolTable.getMethod())
		    	this.symbolTable.addArgument(methodToken.getArgument());
	}
	
	public void typeToAst()
	{
		 int ocl_start=0;//代表operationContext是從第幾個開始
	      if(this.ast.getTreeNode().get(0) instanceof ClassifierContext)//因為有些OCL檔案沒有inv
	      {  //設定屬性的type
	    	  ((ClassifierContext)(this.ast.getTreeNode().get(0))).getInv().getTreeNode().addVariableType(this.symbolTable,((ClassifierContext)(this.ast.getTreeNode().get(0))).getClassName());
	    	  
	    	 ocl_start=1;//因為有classifierContext所以operationContext必定在第二個地方出現
	      }
	      
	      
	      //........處理operationContext.........
	      for(;ocl_start<this.ast.getTreeNode().size();ocl_start++)
	      {//走訪所有OperationContext
	    	  ArrayList<StereoType> stereo=((OperationContext)(this.ast.getTreeNode().get(ocl_start))).getStereoType();
	    	  for(int stereo_number=0;stereo_number<stereo.size();stereo_number++)
	    	  {
	    	  stereo.get(stereo_number).getTreeNode().addVariableType(this.symbolTable,((OperationContext)(this.ast.getTreeNode().get(ocl_start))).getClassName());
	    	  if(stereo_number==0 && stereo.size()>1)
	    		  stereo.get(stereo_number).getTreeNode().changeAssignToEqual();
	    	  else 
	    		  stereo.get(stereo_number).getTreeNode().conditionChangeAssignToEqual();
	    	  	
	    	  }
	      }
	}
	
}
