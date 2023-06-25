package bjc.functypes;

import java.util.List;

/**
 * Functional interface for a function that takes and returns an array of arguments
 * @author bjcul
 *
 * @param <Input> The argument type
 * @param <Output> The return type
 */
public interface VarArgFunction<Input, Output> {
	/**
	 * Apply this function
	 * 
	 * @param inputs The inputs to apply it to
	 * @return The results of applying it
	 */
	// It'd certainly be more convenient to have this return an array, but generics
	// + arrays do not play well together
	public List<Output> apply(@SuppressWarnings("unchecked") Input... inputs);
}
