//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_pop4 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testpop() {	
		try {
			objStack = new Stack( 1);
			objStack.push(], );
			objStackPost = new Stack( 1);
			objStackPost.push();

			objStack.pop();
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

