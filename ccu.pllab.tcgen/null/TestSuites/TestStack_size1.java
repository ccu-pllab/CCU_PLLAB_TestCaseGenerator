//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_size1 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testsize() {	
		try {
		    objStack = new Stack( 0);
		    objStackPost = new Stack( 0);
		    objStackPost.push();

			//String result = objStack.size();
			//assertTrue(result.equals(0));
			assertEquals(objStack.size(),0);
		} catch (Exception e) {
			fail();
		}
	}
}

