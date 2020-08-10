package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class errTestStack_push_c3 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testpush() {
		try {
			objStack = new Stack( 4);
			objStack.push(13596);
			objStack.push( 18654);
			objStack.push( 30523);
			objStack.push( 23984);
			objStackPost = new Stack( 4);
			objStackPost.push(13596);
			objStackPost.push( 18654);
			objStackPost.push( 30523);
			objStackPost.push( 23984);

			objStack.push(-32768);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}	

	}
}

