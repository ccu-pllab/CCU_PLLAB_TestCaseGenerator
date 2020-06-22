//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_top6 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testtop() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(9752);
		    objStack.push( 24908);
		    objStackPost = new Stack( 4);
		    objStackPost.push(9752);
		    objStackPost.push( 24908);

			//String result = objStack.top();
			//assertTrue(result.equals(24908));
			assertEquals(objStack.top(),24908);
		} catch (Exception e) {
			fail();
		}
	}
}

