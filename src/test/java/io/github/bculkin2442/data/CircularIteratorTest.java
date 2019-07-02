package io.github.bculkin2442.data;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import bjc.data.CircularIterator;

import static io.github.bculkin2442.TestUtils.*;
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
}
