package bjc.data;

import java.util.*;

/*
 * @TODO Oct 6, 2020 - Ben Culkin - :CleverCache
 * 
 * In the future, there are certain efficencies we could take with our cached 
 * elements; namely, the case where we repeat the same element multiple times,
 *  or the case where we have a mixture of identical (and probably sizable) elements
 * 
 *  The general downside to these of course, is that these efficencies would cost
 *  us something in terms of complexity, as well as not benefitting iterators which
 *  aren't large enough.
 *  
 *  Still an interesting thought as to the best way to implement such a thing though.
 */

/**
 * An iterator which can go back to the beginning of its iteration sequence at
 * any point.
 * 
 * @author Ben Culkin
 *
 * @param <T>
 *            The type iterated over.
 */
public class ResettableIterator<T> implements Iterator<T> {
	private List<T> cache;
	private Iterator<T> backing;

	private boolean isRepeating = false;
	private Iterator<T> cacheIterator;

	/**
	 * Create a new resettable iterator.
	 * 
	 * @param itr
	 *            The iterator to iterate over.
	 */
	public ResettableIterator(Iterator<T> itr) {
		backing = itr;

		cache = new ArrayList<>();
	}

	/**
	 * Create a new resettable iterator.
	 * 
	 * @param itr
	 *            The iterable to iterate over.
	 */
	public ResettableIterator(Iterable<T> itr) {
		this(itr.iterator());
	}

	@Override
	public boolean hasNext() {
		if (isRepeating) return cacheIterator.hasNext() ? true : backing.hasNext();
		else             return backing.hasNext();
	}

	@Override
	public T next() {
		if (isRepeating) {
			if (cacheIterator.hasNext()) {
				return cacheIterator.next();
			} else {
				cacheIterator = null;
				isRepeating = false;
			}
		}

		T itm = backing.next();

		cache.add(itm);

		return itm;
	}

	/**
	 * Reset this iterator to its starting point.
	 */
	public void reset() {
		isRepeating = true;

		cacheIterator = cache.iterator();
	}

	/**
	 * Clears the cache of items.
	 * 
	 * This will immediately return the iterator to the last actual item from the
	 * sequence, as well as making that the first item to be iterated over in the
	 * future.
	 */
	public void clearCache() {
		isRepeating = false;
		cacheIterator = null;

		cache = new ArrayList<>();
	}
}
