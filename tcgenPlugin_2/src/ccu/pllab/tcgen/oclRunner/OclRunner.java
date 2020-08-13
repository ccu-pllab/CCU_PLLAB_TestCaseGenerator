package ccu.pllab.tcgen.oclRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import ccu.pllab.tcgen.AbstractSyntaxTree.*;
public class OclRunner {

	public static void main(String[] args) throws Exception {
	       // create a CharStream thatreads from standard input
	       String inputFile = "C:\\Users\\chienLung\\tcgen\\ccu.pllab.tcgen\\src\\ccu\\pllab\\tcgen\\oclRunner\\data.txt";
	       InputStream is = System.in;
	      
	       if ( inputFile!=null ) is = new FileInputStream(inputFile);
	       ANTLRInputStream input = new ANTLRInputStream(is);
	      
	      OclLexer lexer = new OclLexer(input);
	      CommonTokenStream tokens = new CommonTokenStream(lexer);
	      OclParser parser = new OclParser(tokens);
	      AbstractSyntaxTreeNode node= parser.packageDeclarationCS().astRoot; // parse
	   //   node.toGraphViz();
	       //node.parse();
	       
	      
	    }
}
