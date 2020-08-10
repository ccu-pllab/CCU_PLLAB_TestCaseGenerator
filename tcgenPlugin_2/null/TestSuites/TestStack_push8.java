//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_push8 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testpush() {	
		try {
			objStack = new Stack( 4);
			objStack.push(18226);
			objStack.push( 22205);
			objStack.push( 26808);
			objStackPost = new Stack( 4);
			objStackPost.push(18226);
			objStackPost.push( 22205);
			objStackPost.push( 26808);
			objStackPost.push( -32768);

			objStack.push(-32768);
		//	String expect=objStackPost.toString();
		//	String real=objStack.toString();
		//	Boolean result=expect.equals(real);
		//	assertTrue(result);
			assertEquals(objStackPost.toString(),objStack.toString());
		} catch (ArraySizeException e) {
			fail();
		}
			
	}
}

