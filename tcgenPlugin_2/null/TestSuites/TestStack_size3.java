//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_size3 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testsize() {	
		try {
		    objStack = new Stack( 1);
		    objStack.push(13930], );
		    objStackPost = new Stack( 1);
		    objStackPost.push(13930);

			//String result = objStack.size();
			//assertTrue(result.equals(1));
			assertEquals(objStack.size(),1);
		} catch (Exception e) {
			fail();
		}
	}
}

