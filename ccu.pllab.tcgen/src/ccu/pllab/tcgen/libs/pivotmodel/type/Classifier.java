package ccu.pllab.tcgen.libs.pivotmodel.type;

 
import java.util.List;

import ccu.pllab.tcgen.libs.pivotmodel.Operation;

public abstract class Classifier {
	public abstract String getName();

	public abstract List<Operation> getOwnedOperations();
}
