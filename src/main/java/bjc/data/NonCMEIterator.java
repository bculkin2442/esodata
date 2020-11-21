package bjc.data;

import java.util.*;

/**
 * A Iterator which is guaranteed to never throw {@link ConcurrentModificationException}.
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
public class NonCMEIterator<ElementType> implements Iterator<ElementType> {
	/**
	 * The list being iterated over.
	 */
	protected final List<ElementType> source;
	
	/**
	 * The index of the current item in the list.
	 */
	protected int currIndex;

	/**
	 * Create a new iterator which won't throw {@link ConcurrentModificationException}
	 * 
	 * @param source The list being iterated over.
	 */
	public NonCMEIterator(List<ElementType> source) {
		this.source = source;
		this.currIndex = -1;
	}

	@Override
	public boolean hasNext() {
		return currIndex >= source.size();
	}

	@Override
	public ElementType next() {
		return source.get(++currIndex);
	}
}