package bjc.functypes;

import bjc.typeclasses.Container;

/**
 * Represents a constant container
 * 
 * @author bjcul
 *
 * @param <V> The type of the value
 * @param <A> The ignored argument type
 */
public class Const<V, A> implements Container<V, Const<V, ?>>{
	private V payload;
	
	private Const(V payload) {
		this.payload = payload;
	}
	
	/**
	 * Create a constant container.
	 * 
	 * @param <V> The value contained
	 * @param <A> The ignored argument type
	 * 
	 * @param val The type of the value
	 * 
	 * @return A constant container giving the contained value
	 */
	public static <V, A> Const<V, A> of(V val) {
		return new Const<>(val);
	}
	
	V get() {
		return payload;
	}
}
