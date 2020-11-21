package bjc.data;

import java.util.*;

/**
 * A ListIterator which is guaranteed to never throw {@link ConcurrentModificationException}.
 * 
 * The intended use case for this iterator is that you want to be able to modify
 * a list you are iterating over, and not get an immediate exception.
 * 
 * Note that using this is an agreement that you will not complain if it behaves
 * oddly in the face of you concurrently modifying the list, as this class was
 * designed with the usecase of modifying the list from the same thread, without
 * invalidating the iterators. After all, there is a reason that most iterator
 * types throw that exception.
 * 
 * However, sometimes you want to play with fire, even if you might get burnt.
 * This allows you to do so.
 * 
 * @author Ben Culkin
 *
 * @param <ElementType> The type being iterated over.
 */
public class NonCMEListIterator<ElementType> extends NonCMEIterator<ElementType> implements ListIterator<ElementType> {
	/**
	 * Create a new list iterator which won't throw {@link ConcurrentModificationException}
	 * 
	 * @param source The list to iterate over.
	 */
	public NonCMEListIterator(List<ElementType> source) {
		super(source);
	}
	
	@Override
	public boolean hasPrevious() {
		return currIndex > 0;
	}

	@Override
	public ElementType previous() {
		return source.get(--currIndex);
	}

	@Override
	public int nextIndex() {
		return currIndex + 1;
	}

	@Override
	public int previousIndex() {
		return currIndex - 1;
	}

	@Override
	public void remove() {
		source.remove(currIndex);
	}

	@Override
	public void set(ElementType element) {
		source.set(currIndex, element);
	}

	@Override
	public void add(ElementType element) {
		source.add(currIndex, element);
	}
}
