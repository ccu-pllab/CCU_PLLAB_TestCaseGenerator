//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_pop6 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testpop() {	
		try {
			objStack = new Stack( 4);
			objStack.push(16809);
			objStack.push( 21352);
			objStackPost = new Stack( 4);
			objStackPost.push(16809);

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

