package bjc;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Utility methods for doing testing
 *
 * @author Ben Culkin
 */
public class TestUtils {
	/**
	 * Assert an iterator provides a particular sequence of values.
	 *
	 * @param src
	 *             The iterator to pull values from.
	 * @param vals
	 *             The values to expect from the iterator.
	 */
	@SafeVarargs
	public static <T> void assertIteratorEquals(Iterator<T> src, T... vals) {
		for (int i = 0; i < vals.length; i++) {
			if (src.hasNext()) {
				assertEquals(vals[i], src.next());
			} else {
				String msg = String.format("not enough values: got %d, wanted %d", i,
						vals.length);

				assertTrue(msg, false);
			}
		}
	}

	/**
	 * Assert an iterator provides a particular sequence of values.
	 *
	 * @param src
	 *                The iterator to pull values from.
	 * @param hasMore
	 *                The expected value of hasNext for the iterator.
	 * @param vals
	 *                The values to expect from the iterator.
	 */
	@SafeVarargs
	public static <T> void assertIteratorEquals(boolean hasMore, Iterator<T> src,
			T... vals) {
		/*
		 * @NOTE
		 *
		 * Even though it's awkward, the boolean has to come first. Otherwise, there are
		 * cases where the compiler will get confused as to what the right value for T
		 * is, and be unable to pick an overload.
		 */
		assertIteratorEquals(src, vals);

		assertEquals("iterator not exhausted", hasMore, src.hasNext());
	}

	/**
	 * Assert an iterator provides a particular sequence of values.
	 *
	 * @param src
	 *             The iterator to pull values from.
	 * @param vals
	 *             The values to expect from the iterator.
	 */
	@SafeVarargs
	public static <T> void assertIteratorSet(Iterator<T> src, T... vals) {
		Set<T> s1 = new HashSet<>();
		Set<T> s2 = new HashSet<>();

		for (int i = 0; i < vals.length; i++) {
			if (src.hasNext()) {
				s1.add(vals[i]);
				s2.add(src.next());
			} else {
				String msg = String.format("not enough values: got %d, wanted %d", i,
						vals.length);

				assertTrue(msg, false);
			}
		}

		assertEquals(s1, s2);
	}

	/**
	 * Assert an iterator provides a particular sequence of values.
	 *
	 * @param src
	 *                The iterator to pull values from.
	 * @param hasMore
	 *                The expected value of hasNext for the iterator.
	 * @param vals
	 *                The values to expect from the iterator.
	 */
	@SafeVarargs
	public static <T> void assertIteratorSet(boolean hasMore, Iterator<T> src,
			T... vals) {
		/*
		 * @NOTE
		 *
		 * Even though it's awkward, the boolean has to come first. Otherwise, there are
		 * cases where the compiler will get confused as to what the right value for T
		 * is, and be unable to pick an overload.
		 */
		assertIteratorSet(src, vals);

		assertEquals("iterator not exhausted", hasMore, src.hasNext());
	}

	/**
	 * Assert that a list contains a certain set of values.
	 *
	 * @param src
	 *             The list to read values from.
	 *
	 * @param exps
	 *             The values to expect in the list.
	 */
	@SafeVarargs
	public static <T> void assertListEquals(List<T> src, T... exps) {
		assertEquals(exps.length, src.size());

		int i = 0;
		for (T act : src) {
			T exp = exps[i++];

			assertEquals(exp, act);
		}
	}

	/**
	 * Assert a stack has the given contents.
	 *
	 * @param <T>
	 *             The type of items in the stack.
	 *
	 * @param src
	 *             The stack to inspect.
	 * @param exps
	 *             The values that are expected.
	 */
	public static <T> void assertStackEquals(bjc.esodata.Stack<T> src, T... exps) {
		assertArrayEquals(exps, src.toArray());
	}
}
