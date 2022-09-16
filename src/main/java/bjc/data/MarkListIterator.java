package bjc.data;

import java.util.*;

/**
 * ListIterator which allows navigation/marking of an iterator.
 * 
 * @author bjcul
 *
 * @param <E> The element type
 */
public class MarkListIterator<E> implements ListIterator<E> {
	private Iterator<E> backing;

	private List<E> cache;
	private Deque<Integer> marks;

	private int currIdx;
	private int maxIdx;

	/**
	 * Create a new marking list iterator.
	 * 
	 * @param backing The iterator which backs us.
	 */
	public MarkListIterator(Iterator<E> backing) {
		this.backing = backing;

		this.currIdx = 0;
		this.maxIdx = 0;

		this.cache = new ArrayList<>();
		this.marks = new ArrayDeque<>();
	}

	/**
	 * Get the current element of the iterator.
	 * 
	 * @return The current iterator of the element
	 */
	public E current() {
		return cache.get(currIdx);
	}
	
	/**
	 * Create a new marking list iterator.
	 * 
	 * @param backing The iterable to get the backing iterator from.
	 */
	public MarkListIterator(Iterable<E> backing) {
		this(backing.iterator());
	}

	@Override
	public boolean hasNext() {
		if (currIdx < maxIdx)
			return true;
		return backing.hasNext();
	}

	@Override
	public E next() {
		if (currIdx < maxIdx) {
			return cache.get(currIdx++);
		}
		currIdx++;
		maxIdx++;
		E next = backing.next();
		cache.add(next);
		return next;
	}

	@Override
	public boolean hasPrevious() {
		return maxIdx > 0 && currIdx > 0;
	}

	@Override
	public E previous() {
		currIdx--;
		return cache.get(currIdx);
	}

	@Override
	public int nextIndex() {
		return currIdx + 1;
	}

	@Override
	public int previousIndex() {
		return currIdx - 1;
	}

	/**
	 * Mark the current position in the iterator.
	 */
	public void mark() {
		marks.push(currIdx);
	}

	/**
	 * Reset the iterator to the last position that was marked, leaving the mark in
	 * place.
	 * 
	 * @return Whether or not a rollback actually happened
	 */
	public boolean rollback() {
		return rollback(false);
	}

	/**
	 * Reset the iterator to the last position that was marked.
	 * 
	 * @param clearMark Whether to clear the mark being rolled back to.
	 *
	 * @return Whether or not a rollback actually happened
	 */
	public boolean rollback(boolean clearMark) {
		if (marks.isEmpty()) return false;
		
		if (clearMark) {
			currIdx = marks.pop();
		} else {
			currIdx = marks.peek();
		}
		return true;
	}

	/**
	 * Remove the last position that was marked.
	 * 
	 * @return The marked position
	 */
	public int commit() {
		return marks.pop();
	}

	/**
	 * Resets the cache state of this iterator.
	 * 
	 * Once this has been called, all of the previous elements and marks are
	 * discarded.
	 */
	public void reset() {
		this.cache = new ArrayList<>();
		this.marks = new ArrayDeque<>();

		this.currIdx = 0;
		this.maxIdx = 0;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Can't remove items");
	}

	@Override
	public void set(E e) {
		throw new UnsupportedOperationException("Can't set items");
	}

	@Override
	public void add(E e) {
		throw new UnsupportedOperationException("Can't add items");
	}

	/**
	 * Check if this iterator has at least one active mark.
	 * 
	 * @return Whether this iterator has any active marks.
	 */
	public boolean hasMark() {
		return !marks.isEmpty();
	}
}
