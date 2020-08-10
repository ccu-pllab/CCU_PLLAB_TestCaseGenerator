//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_size6 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testsize() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(6948);
		    objStack.push( 4291);
		    objStackPost = new Stack( 4);
		    objStackPost.push(6948);
		    objStackPost.push( 4291);

			//String result = objStack.size();
			//assertTrue(result.equals(2));
			assertEquals(objStack.size(),2);
		} catch (Exception e) {
			fail();
		}
	}
}

