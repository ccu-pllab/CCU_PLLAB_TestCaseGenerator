//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_top4 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testtop() {	
		try {
		    objStack = new Stack( 1);
		    objStack.push(21881], );
		    objStackPost = new Stack( 1);
		    objStackPost.push(21881);

			//String result = objStack.top();
			//assertTrue(result.equals(21881));
			assertEquals(objStack.top(),21881);
		} catch (Exception e) {
			fail();
		}
	}
}

