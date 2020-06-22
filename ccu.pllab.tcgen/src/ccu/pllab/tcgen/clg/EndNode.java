package ccu.pllab.tcgen.clg;

 
import tudresden.ocl20.pivot.pivotmodel.Constraint;

public class EndNode extends ConnectionNode {

	public EndNode(Constraint constraint) {
		super(constraint);
	}

	@Override
	public String getShape() {
		return "doublecircle";
	}
	
	@Override
	public CLGNode clone() {
		return new EndNode(this.getConstraint());
	}
}
