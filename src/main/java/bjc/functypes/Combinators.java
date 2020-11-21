package bjc.functypes;

import java.util.function.*;

/**
 * A bunch of only slightly related function combinators.
 * 
 * @author Ben Culkin
 *
 */
public class Combinators {
	/**
	 * If-then-else combinator.
	 * 
	 * @param <Input> The input type.
	 * @param <Output> The output type.
	 * 
	 * @param in The predicate to run.
	 * @param ifTrue The condition to run when it is true.
	 * @param ifFalse The condition to run when it is false.
	 * 
	 * @return A function which will invoke one or the other action, based on the predicate.
	 */
	@SuppressWarnings("unused")
	public static <Input, Output> Function<Input, Output> iftt(
			Predicate<Input> in,
			Function<Input, Output> ifTrue,
			Function<Input, Output> ifFalse) 
	{
		return arg -> in.test(arg) ? ifTrue.apply(arg) : ifFalse.apply(arg);
	}
	
	/**
	 * Execute an action before calling a function.
	 * 
	 * @param <Input> The input to the function.
	 * @param <Output> The output from the function.
	 * 
	 * @param action The action to run on the input.
	 * @param terminal The function to call.
	 * 
	 * @return A function that runs the provided action before calling the function.
	 */
	public static <Input, Output> Function<Input, Output> beforeThis(
			Consumer<Input> action, Function<Input, Output> terminal) 
	{
		return (arg) -> {
			action.accept(arg);
			return terminal.apply(arg);
		};
	}
	
	/**
	 * Execute an action after calling a function.
	 * 
	 * @param <Input> The input to the function.
	 * @param <Output> The output to the function.
	 * 
	 * @param initial The function to call.
	 * @param action The action to call after doing the function.
	 * 
	 * @return A function that calls the provided action after the function.
	 */
	public static <Input, Output> Consumer<Input> andThen(
			Function<Input, Output> initial, Consumer<Output> action)
	{
		return (arg) -> action.accept(initial.apply(arg));
	}
	
	/**
	 * Standalone function composer.
	 * 
	 * @param <Left> The input type of the initial function.
	 * @param <Middle> The shared input/output type.
	 * @param <Right> The output type of the terminal function.
	 * 
	 * @param initial The first function to call.
	 * @param terminal The second function to call.
	 * 
	 * @return A function that composes the provided functions together.
	 */
	public static <Left, Middle, Right> Function<Left, Right> compose(
			Function<Left, Middle> initial,
			Function<Middle, Right> terminal) 
	{
		return (arg) -> terminal.apply(initial.apply(arg));
	}
	
	/**
	 * Execute a function with some internal state.
	 * 
	 * @param <Input> The input type of the function.
	 * @param <Output> The output type of the function.
	 * @param <State> The type of the internal state.
	 * 
	 * @param source The function which provides internal state.
	 * @param action The function to call.
	 * 
	 * @return A function which hides the production of the internal state.
	 */
	public static <Input, Output, State> Function<Input, Output> introducing(
			Supplier<State> source,
			BiFunction<State, Input, Output> action)
	{
		return (input) -> action.apply(source.get(), input);
	}
	
	/**
	 * Invoke a given function with null to produce its result.
	 * 
	 * @param <Input> The input type of the function.
	 * @param <Output> The output type of the function.
	 * 
	 * @param action The function to invoke.
	 * 
	 * @return The result of calling the function with null.
	 */
	public static <Input, Output> Output invoke(Function<Input, Output> action) 
	{
		return action.apply(null);
	}
	
	/**
	 * Execute an action a given number of times.
	 * 
	 * @param times The number of times to execute the action.
	 * @param action The action to execute.
	 */
	public static void times(int times, Consumer<Integer> action)
	{
		for (int i = 0; i < times; i++) action.accept(i);
	}
}