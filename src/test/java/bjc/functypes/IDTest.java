package bjc.functypes;

import static org.junit.Assert.*;

import java.util.function.*;

import org.junit.*;

/**
 * Test for ID.
 * 
 * @author Ben Culkin
 *
 */
public class IDTest {

	@Test
	public void testID() {
		UnaryOperator<String> idOp = ID.id();
		
		assertEquals("hello", idOp.apply("hello"));
		
		assertEquals(1, ID.id().apply(1));
	}

}
