package bjc.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test for boolean toggles.
 * 
 * @author bjculkin
 *
 */
public class BooleanToggleTest {

	/**
	 * Test that boolean toggles work right.
	 */
	@Test
	public void test() {
		BooleanToggle tog = new BooleanToggle();

		// Check initial value is false.
		assertEquals(false, tog.peek());
		// Check that 'get' returns the old value
		assertEquals(false, tog.get());
		// Check that 'get' swaps the value
		assertEquals(true, tog.peek());
		// Check that we can round-trip back.
		assertEquals(true, tog.get());
		assertEquals(false, tog.peek());

		tog.set(true);

		// Check set works
		assertEquals(true, tog.peek());
		
		BooleanToggle tog2 = new BooleanToggle(true);
		
		// Test equals/hashcode
		assertEquals(tog, tog2);
		assertEquals(tog.hashCode(), tog2.hashCode());
		
		// Test toString
		assertEquals("true", tog.toString());
	}
}
