//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isfull1 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisfull() {	
		try {
		    objStack = new Stack( 1);
		    objStack.push(1790], );
		    objStackPost = new Stack( 1);
		    objStackPost.push(1790);

			//String result = objStack.isfull();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=true]));
			assertEquals(objStack.isfull(),com.parctechnologies.eclipse.Atom with [functor=true]);
		} catch (Exception e) {
			fail();
		}
	}
}

