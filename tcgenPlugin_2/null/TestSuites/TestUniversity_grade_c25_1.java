package University;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestUniversity_grade_c25_1 extends TestCase {
	University objUniversity = null;
	University objUniversity_Post = null;


	protected void setUp() throws Exception {
		objUniversity = new University();
		objUniversity_Post = new University();

		super.setUp();
	}

	protected void tearDown() throws Exception {
		objUniversity = null;
		objUniversity_Post = null;

		super.tearDown();
	}

	public void testgrade() {
		try {
			String result = objUniversity.grade(70);
			assertTrue(result.equals("C"));
		} catch (Exception e) {
			fail();
		}
	}
}

