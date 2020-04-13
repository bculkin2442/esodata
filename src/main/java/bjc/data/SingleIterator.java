package bjc.data;

import java.util.Iterator;

/**
 * An iterator that will only ever yield one item.
 *
 * @author EVE
 *
 * @param <T>
 *            The type of the item.
 */
public class SingleIterator<T> implements Iterator<T> {
	/* The item being held. */
	private final T itm;
	/* Whether we've yielded the item yet. */
	private boolean yielded;

	/**
	 * Create a iterator that yields a single item.
	 *
	 * @param item
	 *             The item to yield.
	 */
	public SingleIterator(final T item) {
		itm = item;

		yielded = false;
	}

	@Override
	public boolean hasNext() {
		return !yielded;
	}

	@Override
	public T next() {
		yielded = true;

		return itm;
	}
}
