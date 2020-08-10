//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isempty7 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisempty() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(6026);
		    objStack.push( 14278);
		    objStack.push( 10622);
		    objStackPost = new Stack( 4);
		    objStackPost.push(6026);
		    objStackPost.push( 14278);
		    objStackPost.push( 10622);

			//String result = objStack.isempty();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=false]));
			assertEquals(objStack.isempty(),com.parctechnologies.eclipse.Atom with [functor=false]);
		} catch (Exception e) {
			fail();
		}
	}
}

