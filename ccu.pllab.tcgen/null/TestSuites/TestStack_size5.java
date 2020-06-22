//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_size5 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testsize() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(12676], );
		    objStackPost = new Stack( 4);
		    objStackPost.push(12676);

			//String result = objStack.size();
			//assertTrue(result.equals(1));
			assertEquals(objStack.size(),1);
		} catch (Exception e) {
			fail();
		}
	}
}

