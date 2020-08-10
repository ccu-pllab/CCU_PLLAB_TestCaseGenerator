package Date;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class TestDate_date_c3_1 extends TestCase {
	Date objDate = null;
	Date objDate_Post = null;


	protected void setUp() throws Exception {
		objDate = new Date();
		objDate_Post = new Date(1, 2, 1);

		super.setUp();
	}

	protected void tearDown() throws Exception {
		objDate = null;
		objDate_Post = null;

		super.tearDown();
	}

	public void testdate() {
		try {
			assertTrue(objDate.equals(new Date(1, 2, 1)));
		} catch (Exception e) {
			fail();
		}
	}
}

