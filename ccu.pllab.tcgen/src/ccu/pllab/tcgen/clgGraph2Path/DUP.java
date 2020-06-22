package ccu.pllab.tcgen.clgGraph2Path;

  
import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.AbstractConstraint.*;

public class DUP {
	
	private CLGConstraintNode definenode;
	private CLGConstraintNode usenode;
	private String variable;

	public DUP(String variable, CLGConstraintNode definenode, CLGConstraintNode usenode){
		this.variable=variable;
		this.definenode=definenode;
		this.usenode=usenode;
	}
	public CLGConstraintNode getDefineNode(){
		return this.definenode;
	}
	public CLGConstraintNode getUseNode(){
		return this.usenode;
	}
	public String getVariable(){
		return this.variable;
	}

	public String DUP2Str(){
		String result = "";
		result += "([" + this.definenode.getXLabelId() +"],[" + 
				this.usenode.getXLabelId() +"]," + this.variable + ")";
		return result;
	}
}
