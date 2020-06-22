package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class errTestStack_pop_c1 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testpop() {
		try {
			objStack = new Stack( 0);
			objStackPost = new Stack( 0);
			objStackPost.push();

			objStack.pop();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}	

	}
}

