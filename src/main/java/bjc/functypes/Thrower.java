package bjc.functypes;

import java.util.*;
import java.util.function.*;

/**
 * A source for a value that could produce an exception.
 * 
 * @author Ben Culkin
 *
 * @param <ValueType> The type of value possibly produced.
 * @param <ExceptionType> The type of exception possibly thrown.
 */
@FunctionalInterface
public interface Thrower<ValueType, ExceptionType extends Throwable>
{
	/**
	 * Attempt to get the value.
	 * 
	 * @return The value from this source.
	 * 
	 * @throws ExceptionType If something goes wrong getting the value.
	 */
	ValueType extract() throws ExceptionType;
	
	/**
	 * Converts this thrower into a memoized one.
	 * 
	 * Note that if this does throw an exception, it won't be 'memoized' so
	 * that the next call will call this Thrower again.
	 * 
	 * @return A memoizing thrower.
	 */
	default Thrower<ValueType, ExceptionType> memoize() {
		return new MemoizedThrower<>(this);
	}
	
	/**
	 * Applies a function to the result of this thrower.
	 * 
	 * @param <NewOutputType> The new type output by the function.
	 * 
	 * @param func The function to apply.
	 * 
	 * @return A thrower that is the result of applying the given function.
	 */
	default <NewOutputType> Thrower<NewOutputType, ExceptionType> bind(
			Function<ValueType, Thrower<NewOutputType, ExceptionType>> func)
	{
		return () -> func.apply(extract()).extract();
	}
	/**
	 * Create a thrower that yields a given value.
	 * 
	 * @param <ValType> The type of the value.
	 * 
	 * @param val The value to yield.
	 * 
	 * @return A thrower that will always yield that value.
	 */
	static <ValType> 
	Thrower<ValType, ? extends Throwable> from(ValType val)
	{
		return () -> val;
	}
	
	/**
	 * Create a thrower that yields a given value.
	 * 
	 * @param <ValType> The type of the value.
	 * 
	 * @param val The value to yield.
	 * 
	 * @return A thrower that will always yield that value.
	 */
	static <ValType> 
	Thrower<ValType, ? extends Throwable> from(Supplier<ValType> val)
	{
		return val::get;
	}
	
	/**
	 * Convert a function on values to one over throwers.
	 * 
	 * @param <Input> The function input type.
	 * @param <Output> The function output type.
	 * @param <ExType> The exception possibly thrown.
	 * 
	 * @param func The function to convert.
	 * 
	 * @return A function which operates on throwers instead.
	 */
	static <Input, Output, ExType extends Throwable>
	Function<Thrower<Input, ExType>,Thrower<Output, ExType>> fmap(
			Function<Input, Output> func) 
	{
		return (input) -> () -> func.apply(input.extract());
	}
	
	/**
	 * Convert a list of throwers into a thrower that returns a list.
	 * 
	 * @param <Output> The type output by the thrower.
	 * @param <ExType> The type of exception thrown.
	 * 
	 * @param throwers The list of throwers.
	 * 
	 * @return A thrower that returns a list of results.
	 */
	static <Output, ExType extends Throwable>
	Thrower<List<Output>, ExType> seq(List<Thrower<Output, ExType>> throwers)
	{
		return () -> {
			List<Output> results = new ArrayList<>(throwers.size());
			for (Thrower<Output, ExType> thrower : throwers)
			{
				results.add(thrower.extract());
			}
			return results;
		};
	}
	
	/**
	 * Convert a array of throwers into a thrower that returns a list.
	 * 
	 * @param <Output> The type output by the thrower.
	 * @param <ExType> The type of exception thrown.
	 * 
	 * @param throwers The array of throwers.
	 * 
	 * @return A thrower that returns a list of results.
	 */
	@SafeVarargs
	static <Output, ExType extends Throwable>
	Thrower<List<Output>, ExType> seq(Thrower<Output, ExType>... throwers)
	{
		return () -> {
			List<Output> results = new ArrayList<>(throwers.length);
			for (Thrower<Output, ExType> thrower : throwers)
			{
				results.add(thrower.extract());
			}
			return results;
		};
	}
}

class MemoizedThrower<ValueType, ExceptionType extends Throwable>
implements Thrower<ValueType, ExceptionType>
{
	private final Thrower<ValueType, ExceptionType> source;
	private ValueType val;
	
	public MemoizedThrower(Thrower<ValueType, ExceptionType> source)
	{
		this.source = source;
	}
	
       	@Override
        public ValueType extract() throws ExceptionType
        {
       		if (val == null) val = source.extract();
       		return val;
        }
}
