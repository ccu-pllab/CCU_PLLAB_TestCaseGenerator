package ccu.pllab.tcgen.csp;

 
import org.apache.commons.lang3.Range;

import ccu.pllab.tcgen.libs.pivotmodel.Association;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;

public interface CreateObjectConfig {

	public Range<Integer> getRangeOfInstance(UML2Class info);

	public Range<Integer> getRangeOfInstance(Association info);

	public Range<Integer> getIntDomain();
}
