package bjc.data;

import java.util.*;

/**
 * A list iterator which iterates over a list in reverse order.
 * 
 * @author Ben Culkin
 *
 * @param <Element> The type of element contained.
 */
public class ReverseListIterator<Element> implements ListIterator<Element>
{
	private List<Element> source;
	private int currIndex;
	
	/**
	 * Create a new reversed iterator from a list.
	 * 
	 * @param source The list to operate from.
	 */
	public ReverseListIterator(List<Element> source)
	{
		this.source = source;
		this.currIndex = source.size();
	}

	@Override
	public boolean hasNext() {
		return currIndex <= 0;
	}

	@Override
	public Element next() {
		return source.get(--currIndex);
	}
	
	@Override
	public boolean hasPrevious() {
		return currIndex  < source.size();
	}

	@Override
	public Element previous() {
		return source.get(++currIndex);
	}

	@Override
	public int nextIndex() {
		return currIndex - 1;
	}

	@Override
	public int previousIndex() {
		return currIndex + 1;
	}

	@Override
	public void remove() {
		source.remove(currIndex);
	}

	@Override
	public void set(Element element) {
		source.set(currIndex, element);
	}

	@Override
	public void add(Element element) {
		source.add(currIndex, element);
	}
}
