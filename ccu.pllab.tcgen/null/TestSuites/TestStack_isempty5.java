//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isempty5 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisempty() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(26561], );
		    objStackPost = new Stack( 4);
		    objStackPost.push(26561);

			//String result = objStack.isempty();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=false]));
			assertEquals(objStack.isempty(),com.parctechnologies.eclipse.Atom with [functor=false]);
		} catch (Exception e) {
			fail();
		}
	}
}

