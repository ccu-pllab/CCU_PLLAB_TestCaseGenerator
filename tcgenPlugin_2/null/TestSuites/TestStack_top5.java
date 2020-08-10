//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_top5 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testtop() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(2268], );
		    objStackPost = new Stack( 4);
		    objStackPost.push(2268);

			//String result = objStack.top();
			//assertTrue(result.equals(2268));
			assertEquals(objStack.top(),2268);
		} catch (Exception e) {
			fail();
		}
	}
}

