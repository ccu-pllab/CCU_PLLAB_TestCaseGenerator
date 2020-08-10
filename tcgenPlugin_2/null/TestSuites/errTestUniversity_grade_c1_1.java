package University;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class errTestUniversity_grade_c1_1 extends TestCase {
	University objUniversity = null;
	University objUniversity_Post = null;




	protected void tearDown() throws Exception {
		objUniversity = null;
		objUniversity_Post = null;

		super.tearDown();
	}

	public void testgrade() {
		try {
			String result = objUniversity.grade(101);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}

