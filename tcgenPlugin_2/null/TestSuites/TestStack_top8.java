//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_top8 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testtop() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(26197);
		    objStack.push( 24841);
		    objStack.push( 8310);
		    objStack.push( 18347);
		    objStackPost = new Stack( 4);
		    objStackPost.push(26197);
		    objStackPost.push( 24841);
		    objStackPost.push( 8310);
		    objStackPost.push( 18347);

			//String result = objStack.top();
			//assertTrue(result.equals(18347));
			assertEquals(objStack.top(),18347);
		} catch (Exception e) {
			fail();
		}
	}
}

