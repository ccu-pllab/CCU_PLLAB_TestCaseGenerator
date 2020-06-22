package Date;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;



public class errTestDate_date_c9_1 extends TestCase {
	Date objDate = null;
	Date objDate_Post = null;




	protected void tearDown() throws Exception {
		objDate = null;
		objDate_Post = null;

		super.tearDown();
	}

	public void testdate() {
		try {
			new Date(1, 13, 1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}

