//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isempty3 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisempty() {	
		try {
		    objStack = new Stack( 4);
		    objStackPost = new Stack( 4);
		    objStackPost.push();

			//String result = objStack.isempty();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=true]));
			assertEquals(objStack.isempty(),com.parctechnologies.eclipse.Atom with [functor=true]);
		} catch (Exception e) {
			fail();
		}
	}
}

