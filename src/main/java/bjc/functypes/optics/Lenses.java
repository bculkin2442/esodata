package bjc.functypes.optics;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import bjc.data.Holder;
import bjc.data.Pair;

/**
 * Utility and constructor functions for lenses
 * 
 * @author bjcul
 *
 */
public class Lenses {
	/**
	 * Create an immutable lens from a pair of functions.
	 * 
	 * @param <W1>   The first type the lens is used on
	 * @param <W2>   The second type the lens is used on
	 * @param <P1>   The first type the lens focuses on
	 * @param <P2>   The second type the lens focuses on.
	 * 
	 * @param getter The getter for the lens
	 * @param setter The setter for the lens
	 * 
	 * @return The lens composed from the two given functions
	 */
	public static <W1, W2, P1, P2> LensX<W1, W2, P1, P2> immutable(Function<W1, P1> getter,
			BiFunction<W1, P2, W2> setter) {
		return new FunctionalLensX<>(getter, setter);
	}

	/**
	 * Create a mutable lens from a pair of functions.
	 * 
	 * @param <Whole> The type the lens is used on
	 * @param <Part>  The type the lens is focused on
	 * 
	 * @param getter  The getter for the lens
	 * @param mutator The mutator for the lens
	 * 
	 * @return The mutable lens composed from the two given functions
	 */
	public static <Whole, Part> MutableLens<Whole, Part> mutable(Function<Whole, Part> getter,
			BiConsumer<Whole, Part> mutator) {
		return new FunctionalMutableLens<>(getter, mutator);
	}

	/**
	 * Create a lens that reflects over the value in a holder
	 * 
	 * @param <Part1> The first type contained in the holder
	 * @param <Part2> The second type contained in the holder
	 * 
	 * @return A lens that focuses on the value of a holder
	 */
	public static <Part1, Part2> LensX<Holder<Part1>, Holder<Part2>, Part1, Part2> holder() {
		return immutable((hld) -> hld.getValue(), (hld, val) -> hld.map((vl) -> val));
	}
	
	/**
	 * Create a lens for updating tagged pairs.
	 * 
	 * TODO: Should this be on a Tagged<T, A> class that is isomorphic to pair
	 * instead?
	 * 
	 * @param <A> The first data type
	 * @param <B> The second data type
	 * @param <T> The tag type
	 * 
	 * @return A lens that operates on the value of a tagged pair.
	 */
	public static <A, B, T> LensX<Pair<T, A>, Pair<T, B>, A, B> tagged() {
		return immutable((par) -> par.getRight(), (par, val) -> par.mapRight((vl) -> val));
	}
	
	/**
	 * Creates a lens which focuses on a piece of internal state.
	 * 
	 * @param <A> The first state type
	 * @param <B> The second state type
	 * 
	 * @param val The initial state value
	 * 
	 * @return A lens that focuses on the given internal state.
	 */
	public static <A, B> LensX<?, ?, A, B> state(A val) {
		Holder<A> hold = Holder.of(val);
		return immutable((whole) -> hold.getValue(), (whole, vl) -> hold.map((arg) -> vl));
	}
	
	/**
	 * Creates a lens which focuses on a piece of mutable internal state.
	 * 
	 * @param <A> The state type
	 * 
	 * @param val The initial state value
	 * 
	 * @return A lens that focuses on the given internal state.
	 */
	public static <A> MutableLens<?, A> stateM(A val) {
		Holder<A> hold = Holder.of(val);
		return mutable((whole) -> hold.getValue(), (whole, vl) -> hold.replace(vl));
	}
}

final class FunctionalLensX<W1, W2, P1, P2> implements LensX<W1, W2, P1, P2> {
	private final BiFunction<W1, P2, W2> setter;
	private final Function<W1, P1> getter;

	FunctionalLensX(Function<W1, P1> getter, BiFunction<W1, P2, W2> setter) {
		this.setter = setter;
		this.getter = getter;
	}

	@Override
	public P1 get(W1 source) {
		return getter.apply(source);
	}

	@Override
	public W2 set(W1 source, P2 val) {
		return setter.apply(source, val);
	}
}

final class FunctionalMutableLens<Whole, Part> implements MutableLens<Whole, Part> {
	private final BiConsumer<Whole, Part> mutator;
	private final Function<Whole, Part> getter;

	FunctionalMutableLens(Function<Whole, Part> getter, BiConsumer<Whole, Part> mutator) {
		this.mutator = mutator;
		this.getter = getter;
	}

	@Override
	public Part get(Whole source) {
		return getter.apply(source);
	}

	@Override
	public void mutate(Whole source, Part val) {
		mutator.accept(source, val);
	}
}
