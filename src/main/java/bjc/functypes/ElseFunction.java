package bjc.functypes;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 'Grammar' type used for making certain types of conditional processing read
 * easier.
 * 
 * @author bjcul
 *
 * @param <Input>  The input type
 * @param <Output> The output type
 */
@FunctionalInterface
public interface ElseFunction<Input, Output> extends Function<Input, Output> {
	/**
	 * Apply the alternative.
	 * 
	 * Defaults to just applying this function
	 * 
	 * @param x The input
	 * 
	 * @return The result of applying the function
	 */
	public default Output elseApply(Input x) {
		return apply(x);
	}

	/**
	 * Handle a value present in an optional.
	 * 
	 * @param <Input> The type contained in the optional.
	 * 
	 * @param val The optional
	 * 
	 * @return A function for processing the optional
	 */
	public static <Input> ElseFunction<Consumer<Input>, ElseFunction<Runnable, Void>> ifPresent(Optional<Input> val) {
		return (f) -> (g) -> {
			if (val.isPresent())
				f.accept(val.get());
			else
				g.run();
			return null;
		};
	}
	
	/**
	 * Handle a value not present in an optional.
	 * 
	 * @param <Input> The type contained in the optional.
	 * 
	 * @param val The optional
	 * 
	 * @return A function for processing the optional
	 */
	public static <Input> ElseFunction<Runnable, ElseFunction<Consumer<Input>, Void>> ifNotPresent(Optional<Input> val) {
		return (f) -> (g) -> {
			if (val.isPresent())
				g.accept(val.get());
			else
				f.run();
			return null;
		};
	}
}
