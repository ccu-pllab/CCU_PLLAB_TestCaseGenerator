package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class errTestStack_push_c1 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testpush() {
		try {
			objStack = new Stack( 0);
			objStackPost = new Stack( 0);
			objStackPost.push();

			objStack.push(-32768);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}	

	}
}

