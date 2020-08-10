package ccu.pllab.tcgen.ast;

 
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import ccu.pllab.tcgen.libs.pivotmodel.type.Classifier;

public abstract class LoopExp extends PropertyCallExp {

	public LoopExp(Constraint obj, ASTNode source, String name, Classifier type) {
		super(obj, source, name, type);
	}

}
