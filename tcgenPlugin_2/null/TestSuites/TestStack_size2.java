//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_size2 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testsize() {	
		try {
		    objStack = new Stack( 1);
		    objStackPost = new Stack( 1);
		    objStackPost.push();

			//String result = objStack.size();
			//assertTrue(result.equals(0));
			assertEquals(objStack.size(),0);
		} catch (Exception e) {
			fail();
		}
	}
}

