package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class errTestStack_push_c2 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testpush() {
		try {
			objStack = new Stack( 1);
			objStack.push(21956], );
			objStackPost = new Stack( 1);
			objStackPost.push(21956);

			objStack.push(-32768);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}	

	}
}

