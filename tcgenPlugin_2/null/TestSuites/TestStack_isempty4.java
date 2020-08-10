//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isempty4 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisempty() {	
		try {
		    objStack = new Stack( 1);
		    objStack.push(5849], );
		    objStackPost = new Stack( 1);
		    objStackPost.push(5849);

			//String result = objStack.isempty();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=false]));
			assertEquals(objStack.isempty(),com.parctechnologies.eclipse.Atom with [functor=false]);
		} catch (Exception e) {
			fail();
		}
	}
}

