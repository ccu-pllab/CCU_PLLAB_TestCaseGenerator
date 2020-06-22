package ccu.pllab.tcgen.clgGraph2Path;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.print.attribute.standard.MediaName;

import ccu.pllab.tcgen.AbstractCLG.CLGConstraintNode;
import ccu.pllab.tcgen.AbstractCLG.CLGEdge;
import ccu.pllab.tcgen.AbstractCLG.CLGEndNode;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGNode;
import ccu.pllab.tcgen.AbstractConstraint.CLGConstraint;
import ccu.pllab.tcgen.AbstractConstraint.CLGOperatorNode;
import ccu.pllab.tcgen.exe.main.Main;






public class BoundaryEnumerator {

	public BoundaryEnumerator(){
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void init(CLGPath path){
		boundarypath=path;
		tempboundarypath=path;
		used=false;
		boundaryConstraints = new HashSet();
		op=" ";
		current_constraint=null;
		bConstraints=new ArrayList<CLGOperatorNode>();
		System.out.println("testboundarynodes:"+boundarypath.getPathNodes().size());
		Main.iterateBoundary=new HashMap<String,Integer>();
		this.con2ConNode=new HashMap<CLGConstraint, CLGConstraintNode>();
		Main.conNodeiterateBoundary=new HashMap<CLGConstraintNode,Integer> ();
		for(int i=0;i< boundarypath.getPathNodes().size();i++)
		{
		 
			CLGNode n = boundarypath.getPathNodes().get(i);;
		
			if(n instanceof CLGConstraintNode)
			{
		    	if(((CLGConstraintNode) n).getConstraint() instanceof CLGOperatorNode) 
		    	{
		    		CLGOperatorNode opconstarint = (CLGOperatorNode) ((CLGConstraintNode) n).getConstraint() ;
		    		flattenBoundaryConstraint(opconstarint,(CLGConstraintNode) n);		
		    	}
			}
		}
		
		
		
	}
	
	
	private HashSet<CLGOperatorNode> boundaryConstraints ;
	private ArrayList<CLGOperatorNode> bConstraints;
	private String op;
	private CLGOperatorNode current_constraint; 
	private CLGPath boundarypath;
	private CLGPath tempboundarypath;
	private boolean used;
	private boolean haveBoundary=false;
	private HashMap<CLGConstraint, CLGConstraintNode> con2ConNode;
	private CLGConstraint boundaryConstraint;
	//private HashSet<CLGOperatorNode> tempboundaryConstraints ;
	private void flattenBoundaryConstraint(CLGOperatorNode constraint,CLGConstraintNode node){

		if(constraint.getOperator().equals(">=") || constraint.getOperator().equals("<="))
		{
			boundaryConstraints.add((CLGOperatorNode) constraint);
			if(constraint.getBoundary())
			{
				if(Main.iterateBoundary.get(constraint.getConstraintId())!=null)
				{
					Main.iterateBoundary.put(constraint.getConstraintId(), Main.iterateBoundary.get(constraint.getConstraintId())+1);
					//System.out.println("constraint"+constraint.getBoundary());
					Main.conNodeiterateBoundary.put(node, Main.conNodeiterateBoundary.get(node)+1);
				}
				else
				{
					Main.iterateBoundary.put(constraint.getConstraintId(),1);
					con2ConNode.put(constraint, node);
					Main.conNodeiterateBoundary.put(node, 1);
				}
			//	 haveBoundary=true;
			//	 this.boundaryConstraint=constraint;
			}
		}
		if(constraint.getLeftOperand() instanceof CLGOperatorNode)
		{
			if(constraint.getRightOperand() instanceof CLGOperatorNode)
			{
				flattenBoundaryConstraint((CLGOperatorNode)constraint.getRightOperand(),node);
			}
			flattenBoundaryConstraint((CLGOperatorNode)constraint.getLeftOperand(),node);
			
		}
		
		else if(constraint.getRightOperand() instanceof CLGOperatorNode)
		{
			
			if(constraint.getLeftOperand() instanceof CLGOperatorNode)
			{
				flattenBoundaryConstraint((CLGOperatorNode)constraint.getLeftOperand(),node);
			}
			flattenBoundaryConstraint((CLGOperatorNode)constraint.getRightOperand(),node);
			
		}	
	}
	

	
	
	public CLGPath next(){
	
	if(boundaryConstraints.isEmpty())
	{
		System.out.println("There is no more boundary path !!");
		return null;
	}
	
		
		if(current_constraint == null)
		{
			//tempboundaryConstraints=boundaryConstraints;
			for(CLGOperatorNode c : boundaryConstraints)
			{
				if(c.getOperator().equals("<=")&&!c.getFromIterateExp())
					c.setOperator("<");
				if(c.getOperator().equals(">=")&&!c.getFromIterateExp())
					c.setOperator(">");
				op ="1";
				current_constraint = c;
			}
			return boundarypath;
		}	
		  
		
		if(op=="1" && boundaryConstraints.size()>0)
		{
		
			for(CLGOperatorNode c : boundaryConstraints)
			{
				if(c.getOperator().equals("<") && !c.getFromIterateExp())
					c.setOperator("<=");
				if(c.getOperator().equals(">")&&!c.getFromIterateExp())
					c.setOperator(">="); 
			}
			op=null;
		}
		
		    		if(current_constraint != null && op !=null)
		    		{
		    			if(!current_constraint.getBoundary())
		    			{
		    			current_constraint.setOperator(op);
		    			boundaryConstraints.remove(current_constraint);
		    			}
		    			else
		    			{
		    				if(Main.iterateBoundary.get(current_constraint.getConstraintId())!=1)
		    				{
		    					Main.iterateBoundary.put(current_constraint.getConstraintId(),Main.iterateBoundary.get(current_constraint.getConstraintId())-1);
		    					Main.conNodeiterateBoundary.put(con2ConNode.get(current_constraint),Main.iterateBoundary.get(current_constraint.getConstraintId()));
		    					return boundarypath;
		    				}
		    				else
		    					boundaryConstraints.remove(current_constraint);
		    			}
    				}
		    	
		    		for(CLGOperatorNode c : boundaryConstraints) 
		    		{	
		    			if(!c.getFromIterateExp())
		    			{
		    				switch(((CLGOperatorNode) c).getOperator())
		    				{
		    				case ">=":
		    			
		    					op=((CLGOperatorNode) c).getOperator();
		    				
		    					((CLGOperatorNode) c).setOperator("==");
		    					current_constraint=(CLGOperatorNode) c;
		    				
		    					return boundarypath;
		    				case "<=":
		    					
		    					op=((CLGOperatorNode) c).getOperator();
		    					if(!Main.iterateBoundary.containsKey(c.getConstraintId()))
		    					((CLGOperatorNode) c).setOperator("=="); 
		    					else {
		    						Main.conNodeiterateBoundary.put(con2ConNode.get(current_constraint),Main.iterateBoundary.get(current_constraint.getConstraintId()));
								}
		    					current_constraint=(CLGOperatorNode) c;
		    					return boundarypath;
		    				default:  				
		    					current_constraint=(CLGOperatorNode) c;
		    					
		    					break;
		    				}
		    			}
		    				
		    			}
		
		return null;
	}

}
