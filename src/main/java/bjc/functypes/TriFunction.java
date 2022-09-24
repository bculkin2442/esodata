package bjc.functypes;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a function which takes 3 input values.
 * 
 * @author bjcul
 *
 * @param <In1> The first input type
 * @param <In2> The second input type
 * @param <In3> The third input type
 * @param <Out> The output type
 */
@FunctionalInterface
public interface TriFunction<In1, In2, In3, Out> {
	/**
	 * Apply this function.
	 * 
	 * @param x The first input
	 * @param y The second input
	 * @param z The third input
	 * 
	 * @return The output
	 */
	public Out apply(In1 x, In2 y, In3 z);

	// TODO: come up with some way to implement a uncurry that won't stack on top of a curry
	
	/**
	 * Curry the first argument for this function.
	 * 
	 * @return The function with the first argument curried.
	 */
	public default Function<In1, BiFunction<In2, In3, Out>> curryFirst() {
		return (x) -> (y, z) -> apply(x, y, z);
	}

	/**
	 * Curry the third argument for this function.
	 * 
	 * @return The function with the third argument curried.
	 */
	public default BiFunction<In1, In2, Function<In3, Out>> curryLast() {
		return (x, y) -> (z) -> apply(x, y, z);
	}

	/**
	 * Fully curry this function
	 * 
	 * @return The fully curried function
	 */
	public default Function<In1, Function<In2, Function<In3, Out>>> fullCurry() {
		return (x) -> (y) -> (z) -> apply(x, y, z);
	}

	/**
	 * Partially apply the third argument.
	 * 
	 * @param z The value for the third argument
	 * 
	 * @return The function w/ the third argument partially applied
	 */
	public default BiFunction<In1, In2, Out> partialLast(In3 z) {
		return (x, y) -> apply(x, y, z);
	}

	public default BiFunction<In1, In3, Out> partialMiddle(In2 y) {
		return (x, z) -> apply(x, y, z);
	}

	public default BiFunction<In2, In3, Out> partialFirst(In1 x) {
		return (y, z) -> apply(x, y, z);
	}

	/**
	 * Apply a transform to the output of this function
	 * 
	 * @param <Out2> The new output type
	 * 
	 * @param f The mapping function
	 * 
	 * @return The function with its outputs transformed
	 */
	public default <Out2> TriFunction<In1, In2, In3, Out2> mapOutput(Function<Out, Out2> f) {
		return (x, y, z) -> f.apply(apply(x, y, z));
	}
	// Possible additions: partial applications which take a Supplier, functions to
	// determine one/two of the args based on the others, functions to map one of the args
}
