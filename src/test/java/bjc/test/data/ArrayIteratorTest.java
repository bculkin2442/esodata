/**
 * 
 */
package bjc.test.data;

import static org.junit.Assert.*;

import org.junit.*;

import bjc.data.ArrayIterator;

/**
 * Test ArrayIterator
 * @author Ben Culkin
 *
 */
public class ArrayIteratorTest {

	/**
	 * Test ArrayIterator
	 */
	@Test
	public void test() {
		ArrayIterator<String> itr = new ArrayIterator<>("a", "b", "c");
		
		assertTrue(itr.hasNext());
		assertEquals("a", itr.next());
		assertEquals("b", itr.next());
		assertEquals("c", itr.next());
		
		assertFalse(itr.hasNext());
		assertNull(itr.next());
	}

}
