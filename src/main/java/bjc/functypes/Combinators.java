package bjc.functypes;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.function.*;

import bjc.data.*;

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
			Consumer<Input> action,
			Function<Input, Output> terminal) 
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
			Function<Input, Output> initial,
			Consumer<Output> action)
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
	
	/**
	 * Invoke a wrapper instead of invoking a normal function.
	 * 
	 * @param <Input> The input type to the function.
	 * @param <Output> The output type to the function.
	 * 
	 * @param function The function to apply.
	 * @param wrapper The wrapper around a function.
	 * 
	 * @return A function that invokes the wrapper instead.
	 */
	public static <Input, Output> Function<Input, Output> around(
			Function<Input, Output> function,
			BiFunction<Input, Function<Input, Output>, Output> wrapper)
	{
		return (input) -> wrapper.apply(input, function);
	}
	
	/**
	 * Only run a given function when the argument satisfies a condition.
	 * 
	 * @param <Input> The input type of the function.
	 * @param <Output> The output type of the function.
	 * 
	 * @param function The function to run.
	 * @param guard The guard to use for checking the input.
	 * 
	 * @return A function which takes the given input, and only calls the
	 *         function if the guard returns true. Otherwise, it will return
	 *         an empty optional.
	 */
	public static <Input, Output> Function<Input, Optional<Output>> provided(
			Function<Input, Output> function,
			Predicate<Input> guard)
	{
		return iftt(guard,
				(arg)     -> Optional.ofNullable(function.apply(arg)),
				(ignored) -> Optional.empty()
		);
	}
	
	/**
	 * Concatenate two functions together, so that they run on the same argument.
	 * 
	 * @param <Input> The type of the input.
	 * @param <Output1> The type of the first output.
	 * @param <Output2> The type of the second output.
	 * 
	 * @param funcA The first function to call.
	 * @param funcB The second function to call.
	 * 
	 * @return A function that returns a pair of the results of calling both
	 *         functions.
	 */
	public static
	<Input, Output1, Output2> Function<Input, Pair<Output1, Output2>>
	concat(Function<Input, Output1> funcA, Function<Input, Output2> funcB)
	{
		return (arg) -> Pair.pair(funcA.apply(arg), funcB.apply(arg));
	}
	
	/**
	 * Concatenate a series of functions together, returning a list of their
	 * results.
	 * 
	 * @param <Input> The input type for the functions.
	 * @param <Output> The output type for the functions.
	 * 
	 * @param funcs The series of functions to call.
	 * 
	 * @return A function that calls each of those functions, and returns a
	 *         list of their results.
	 */
	@SafeVarargs
	public static
	<Input, Output> Function<Input, List<Output>>
	concat(Function<Input, Output>... funcs)
	{
		List<Function<Input, Output>> funcList = Arrays.asList(funcs);
		
		// Kind of a nuisance that Java can't properly guess the type of
		// our mapper function, but oh well.
		
		return (arg) ->
			funcList.stream()
			.map((Function<Function<Input, Output>, Output>)
					(func) -> func.apply(arg))
			.collect(toList());
	}
	
	/**
	 * Return a function that does a series of actions upon a value, then returns
	 * that value.
	 * 
	 * @param <Type> The type given as an argument
	 * 
	 * @param consumers The actions to perform on the value.
	 * 
	 * @return A function that performs those arguments on a value.
	 */
	@SafeVarargs
	public static <Type> Function<Type, Type> doWith(Consumer<Type>... consumers)
	{
		return (arg) -> {
			for (Consumer<Type> consumer : consumers) consumer.accept(arg);
			return arg;
		};
	}
	
	/**
	 * Perform a series of actions upon a value, then return that value.
	 * 
	 * @param <Type> The type given as an argument
	 * 
	 * @param input  The value to use.
	 * @param consumers The actions to perform on the value.
	 * 
	 * @return A function that performs those arguments on a value.
	 */
	@SafeVarargs
	public static <Type> Type with(Type input, Consumer<Type>... consumers)
	{
		return doWith(consumers).apply(input);
	}
	
	/**
	 * Return a function that does a series of actions upon a value, then returns
	 * that value.
	 * 
	 * @param <Type> The type given as an argument
	 * 
	 * @param functions The actions to perform on the value.
	 * 
	 * @return A function that performs those arguments on a value.
	 */
	@SafeVarargs
	public static <Type> Function<Type, Type> doWith(Function<Type, ?>... functions)
	{
		return (arg) -> {
			for (Function<Type, ?> function : functions) function.apply(arg);
			return arg;
		};
	}
	
	/**
	 * Perform a series of actions upon a value, then return that value.
	 * 
	 * @param <Type> The type given as an argument
	 * 
	 * @param input  The value to use.
	 * @param functions The actions to perform on the value.
	 * 
	 * @return A function that performs those arguments on a value.
	 */
	@SafeVarargs
	public static <Type> Type with(Type input, Function<Type, ?>... functions)
	{
		return doWith(functions).apply(input);
	}
}