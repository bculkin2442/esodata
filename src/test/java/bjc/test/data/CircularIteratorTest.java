package bjc.test.data;

import static bjc.test.TestUtils.*;
import java.util.*;

import org.junit.Test;

import bjc.data.ArrayIterator;
import bjc.data.CircularIterator;

/**
 * Test for circular iterators.,
 *
 * @author bjculkin
 *
 */
public class CircularIteratorTest {
	/**
	 * Test regular repetition of the entire iterator.
	 */
	@Test
	public void testRegular() {
		List<String> lst = Arrays.asList("a", "b", "c");

		CircularIterator<String> itr = new CircularIterator<>(lst);

		// Check we get initial values correctly, and have more remaining
		assertIteratorEquals(true, itr, "a", "b", "c");

		// Check we repeat correctly, and can still repeat
		assertIteratorEquals(true, itr, "a", "b", "c");
	}

	/**
	 * Test that the last element repeats correctly.
	 */
	@Test
	public void testRepLast() {
		List<String> lst = Arrays.asList("a", "b", "c");

		CircularIterator<String> itr = new CircularIterator<>(lst, false);

		// Check we get initial values correctly, and have more remaining
		assertIteratorEquals(true, itr, "a", "b", "c");

		// Check we repeat correctly, and can still repeat
		assertIteratorEquals(true, itr, "c", "c", "c");
	}
	
	/**
	 * Test that remove throws an exception.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() {
		Iterator<String> arrayItr = new ArrayIterator<>("a", "b");
		CircularIterator<String> itr = new CircularIterator<>(() -> arrayItr);
		
		itr.remove();
	}
}
