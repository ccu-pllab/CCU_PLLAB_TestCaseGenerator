//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_pop8 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testpop() {	
		try {
			objStack = new Stack( 4);
			objStack.push(14218);
			objStack.push( 3807);
			objStack.push( 287);
			objStack.push( 18952);
			objStackPost = new Stack( 4);
			objStackPost.push(14218);
			objStackPost.push( 3807);
			objStackPost.push( 287);

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

