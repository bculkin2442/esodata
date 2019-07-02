package bjc.data;

import java.util.Iterator;
/**
 * Represents an iterator over an array of values.
 *
 * @author Ben Culkin
 */
public class ArrayIterator<T> implements Iterator<T> {
	private Object[] arr;
	private int      idx;

	public ArrayIterator(T... elms) {
		arr = elms;
		idx = 0;
	}

	@Override
	public boolean hasNext() {
		return idx < arr.length;
	}

	@Override
	public T next() {
		if (idx >= arr.length) return null;

		return (T)(arr[idx++]);
	}
}
