package gov.sgpr.fgv.osc.portalosc.map.shared.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class OscCoordinateTest extends TestCase {

	private double x;
	private double y;
	private int id;
	private OscCoordinate coordinate;

	@Before
	public void setUp() throws Exception {
		// DecimalFormat df = new DecimalFormat("00000000000000");
		// String cnpjStr = df.format(12345678);
		x = 25.45674;
		y = 42.6859;
		id = 6892;
		coordinate = new OscCoordinate();
		coordinate.setId(id);
		coordinate.setX(x);
		coordinate.setY(y);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		assertEquals(787078424, coordinate.hashCode());
	}

	//@Test
	/*public void testToString() {
		assertEquals("OscCoordinate [id=" + id + ",x=" + x + ",y=" + y + "]", coordinate.toString());
	}*/

	@Test
	public void testEqualsObject() {
		OscCoordinate expected = new OscCoordinate();
		expected.setId(id);
		expected.setX(x);
		expected.setY(y);
		assertTrue(coordinate.equals(expected));
	}

	@Test
	public void testGetId() {
		assertEquals(id, coordinate.getId());
	}

	@Test
	public void testSetId() {
		coordinate.setId(id);
		assertEquals(id, coordinate.getId());
		coordinate.setId(20);
		assertNotSame(id, coordinate.getId());
	}

}
