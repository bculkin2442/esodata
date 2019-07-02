package bjc;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class TestUtils {
	/**
	 * Assert an iterator provides a particular sequence of values.
	 * 
	 * @param src
	 *                The iterator to pull values from.
	 * @param vals
	 *                The values to expect from the iterator.
	 */
	@SafeVarargs
	public static <T> void assertIteratorEquals(Iterator<T> src, T... vals) {
		for (T val : vals) {
			assertEquals(val, src.next());
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
	public static <T> void assertIteratorEquals(boolean hasMore, Iterator<T> src, T... vals) {
		/*
		 * @NOTE
		 * 
		 * Even though it's awkward, the boolean has to come first.
		 * Otherwise, there are cases where the compiler will get
		 * confused as to what the right value for T is, and be unable
		 * to pick an overload.
		 */
		assertIteratorEquals(src, vals);

		assertEquals("iterator not exhausted", hasMore, src.hasNext());
	}

	@SafeVarargs
	public static <T> void assertListEquals(List<T> src, T... exps) {
		assertEquals(exps.length, src.size());

		int i = 0;
		for (T act : src) {
			T exp = exps[i++];

			assertEquals(exp, act);
		}
	}
}
