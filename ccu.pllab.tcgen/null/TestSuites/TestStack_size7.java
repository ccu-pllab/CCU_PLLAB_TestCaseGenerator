//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_size7 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testsize() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(18694);
		    objStack.push( 26379);
		    objStack.push( 23135);
		    objStackPost = new Stack( 4);
		    objStackPost.push(18694);
		    objStackPost.push( 26379);
		    objStackPost.push( 23135);

			//String result = objStack.size();
			//assertTrue(result.equals(3));
			assertEquals(objStack.size(),3);
		} catch (Exception e) {
			fail();
		}
	}
}

