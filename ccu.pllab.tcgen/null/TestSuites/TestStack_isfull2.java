//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isfull2 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisfull() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(21199);
		    objStack.push( 24003);
		    objStack.push( 14675);
		    objStack.push( 20762);
		    objStackPost = new Stack( 4);
		    objStackPost.push(21199);
		    objStackPost.push( 24003);
		    objStackPost.push( 14675);
		    objStackPost.push( 20762);

			//String result = objStack.isfull();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=true]));
			assertEquals(objStack.isfull(),com.parctechnologies.eclipse.Atom with [functor=true]);
		} catch (Exception e) {
			fail();
		}
	}
}

