package ccu.pllab.tcgen.ASTGraph;

 
import java.util.ArrayList;

import ccu.pllab.tcgen.AbstractSyntaxTree.AbstractSyntaxTreeNode;

public class ASTParseNode {
	private String id;
	private ArrayList<AbstractSyntaxTreeNode> operand=new ArrayList<AbstractSyntaxTreeNode>();
	private ASTParseNode next;
	public ASTParseNode() {
		// TODO Auto-generated constructor stub
		
	}
}
