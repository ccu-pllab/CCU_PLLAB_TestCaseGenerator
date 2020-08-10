//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isfull8 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisfull() {	
		try {
		    objStack = new Stack( 0);
		    objStackPost = new Stack( 0);
		    objStackPost.push();

			//String result = objStack.isfull();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=false]));
			assertEquals(objStack.isfull(),com.parctechnologies.eclipse.Atom with [functor=false]);
		} catch (Exception e) {
			fail();
		}
	}
}

