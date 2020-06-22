//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_top7 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testtop() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(10796);
		    objStack.push( 25190);
		    objStack.push( 19582);
		    objStackPost = new Stack( 4);
		    objStackPost.push(10796);
		    objStackPost.push( 25190);
		    objStackPost.push( 19582);

			//String result = objStack.top();
			//assertTrue(result.equals(19582));
			assertEquals(objStack.top(),19582);
		} catch (Exception e) {
			fail();
		}
	}
}

