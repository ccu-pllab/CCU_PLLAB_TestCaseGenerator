//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isfull6 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisfull() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(20744);
		    objStack.push( 8849);
		    objStackPost = new Stack( 4);
		    objStackPost.push(20744);
		    objStackPost.push( 8849);

			//String result = objStack.isfull();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=false]));
			assertEquals(objStack.isfull(),com.parctechnologies.eclipse.Atom with [functor=false]);
		} catch (Exception e) {
			fail();
		}
	}
}

