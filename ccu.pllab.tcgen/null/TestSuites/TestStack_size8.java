//package Stack;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestStack_size8 extends TestCase {
	Stack objStack = null;
	Stack objStackPost = null;


	public void testsize() {	
		try {
		    objStack = new Stack( 4);
		    objStack.push(629);
		    objStack.push( 5297);
		    objStack.push( 2157);
		    objStack.push( 9748);
		    objStackPost = new Stack( 4);
		    objStackPost.push(629);
		    objStackPost.push( 5297);
		    objStackPost.push( 2157);
		    objStackPost.push( 9748);

			//String result = objStack.size();
			//assertTrue(result.equals(4));
			assertEquals(objStack.size(),4);
		} catch (Exception e) {
			fail();
		}
	}
}

