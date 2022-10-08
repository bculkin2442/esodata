package bjc.functypes;

import bjc.typeclasses.Container;

public class Const<V, A> implements Container<V, Const<V, ?>>{
	private V payload;
	
	private Const(V payload) {
		this.payload = payload;
	}
	
	public static <V, A> Const<V, A> of(V val) {
		return new Const<>(val);
	}
	
	V get() {
		return payload;
	}
}
