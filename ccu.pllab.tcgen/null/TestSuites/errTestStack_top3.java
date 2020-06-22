package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class errTestStack_top_c3 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testtop() {
		try {	
			objStack = new Stack( 4);
			objStackPost = new Stack( 4);
			objStackPost.push();

			String result = objStack.top();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}

