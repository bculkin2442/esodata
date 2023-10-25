package bjc.data;

import java.util.*;
import java.util.function.Function;

/**
 * Create an iterator that applies a flat-map function
 * 
 * @author bjcul
 * 
 * @param <T1> The source type
 * @param <T2> The output type
 *
 */
public class FlatMapIterator<T1, T2> implements Iterator<T2> {
	private Function<T1, Iterator<T2>> action;

	private Iterator<T1> source;
	private Iterator<T2> output;
	/**
	 * Create a new flat-map iterator
	 * 
	 * @param source The source iterator
	 * @param action The action to apply to the iterator
	 */
	public FlatMapIterator(Iterator<T1> source, Function<T1, Iterator<T2>> action) {
		this.source = source;
		this.action = action;
		
	}
	
	@Override
	public boolean hasNext() {
		if (output != null && output.hasNext()) return true;
		while (source.hasNext()) {
			output = action.apply(source.next());
			if (output.hasNext()) return true;
		}
		return false;
	}

	@Override
	public T2 next() {
		if (output != null && output.hasNext()) return output.next();
		while (source.hasNext()) {
			output = action.apply(source.next());
			if (output.hasNext()) return output.next();
		}
		throw new NoSuchElementException();
	}

}
