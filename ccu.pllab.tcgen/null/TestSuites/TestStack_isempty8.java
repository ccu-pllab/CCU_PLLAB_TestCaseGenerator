//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isempty8 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisempty() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(11022);
		    objStack.push( 1297);
		    objStack.push( 9312);
		    objStack.push( 5463);
		    objStackPost = new Stack( 4);
		    objStackPost.push(11022);
		    objStackPost.push( 1297);
		    objStackPost.push( 9312);
		    objStackPost.push( 5463);

			//String result = objStack.isempty();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=false]));
			assertEquals(objStack.isempty(),com.parctechnologies.eclipse.Atom with [functor=false]);
		} catch (Exception e) {
			fail();
		}
	}
}

