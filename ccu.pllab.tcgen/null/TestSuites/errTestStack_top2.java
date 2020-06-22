package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class errTestStack_top_c2 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testtop() {
		try {	
			objStack = new Stack( 1);
			objStackPost = new Stack( 1);
			objStackPost.push();

			String result = objStack.top();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}

