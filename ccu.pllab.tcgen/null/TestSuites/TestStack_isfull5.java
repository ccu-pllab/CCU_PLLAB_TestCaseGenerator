//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_isfull5 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testisfull() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(25089], );
		    objStackPost = new Stack( 4);
		    objStackPost.push(25089);

			//String result = objStack.isfull();
			//assertTrue(result.equals(com.parctechnologies.eclipse.Atom with [functor=false]));
			assertEquals(objStack.isfull(),com.parctechnologies.eclipse.Atom with [functor=false]);
		} catch (Exception e) {
			fail();
		}
	}
}

