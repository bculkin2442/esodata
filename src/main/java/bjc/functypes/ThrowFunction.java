/* 
 * esodata - data structures and other things, of varying utility
 * Copyright 2022, Ben Culkin
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *   
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package bjc.functypes;

import java.util.*;
import java.util.function.*;

import bjc.typeclasses.Isomorphism;

/**
 * An instance of {@link Function} that can throw an exception.
 * 
 * @author Ben Culkin
 *
 * @param <InputType> The input to the function.
 * @param <ReturnType> The output to the function.
 * @param <ExType> The type of exception thrown.
 */
public interface ThrowFunction<InputType, ReturnType, ExType extends Throwable>
{
	/**
	 * Does the possibly throwing computation embodied by this function.
	 * 
	 * @param arg The input to the function.
	 * 
	 * @return The result of the function.
	 * 
	 * @throws ExType If something went wrong with the function.
	 */
	public ReturnType apply(InputType arg) throws ExType;
	
	/**
     * Converts this into a {@link Function} by handling any thrown exceptions,
     * then mapping the return type to get a consistent return type.
     * 
	 * @param <NewReturn> The new return type.
     * 
     * @param clasz The class of the handled exception. This needs to be provided
     *              because you can't catch a generic exception, and we want to
     *              make sure that we aren't catching any types of exception that
     *              we aren't supposed to.
	 * @param mapper The function which maps the normal return.         
     * @param handler The handler to use.
     * 
     * @return A function which will either return its proper value, or the result
     *         of invoking the handler.
     */
    @SuppressWarnings("unchecked")
    default <NewReturn> Function<InputType, NewReturn> swallowMap(
            Class<ExType> clasz,
            Function<ReturnType, NewReturn> mapper,
            Function<ExType, NewReturn> handler)
    {
        return (inp) -> {
            try {
                return mapper.apply(this.apply(inp));
            } catch (Throwable ex) {
                if (clasz.isInstance(ex)) {
                    // Swallow this
                    return handler.apply((ExType) ex);
                }
                
				String msg = "Exception of incorrect type to be handled, only "
				                         + clasz.getName()
				                         + " are handled";
				
				throw new RuntimeException(msg, ex);
            }
        };
    }
    
	/**
	 * Converts this into a {@link Function} by handling any thrown exceptions.
	 * 
	 * @param clasz The class of the handled exception. This needs to be provided
	 *              because you can't catch a generic exception, and we want to
	 *              make sure that we aren't catching any types of exception that
	 *              we aren't supposed to.
	 *              
	 * @param handler The handler to use.
	 * 
	 * @return A function which will either return its proper value, or the result
	 *         of invoking the handler.
	 */
	default Function<InputType, ReturnType> swallow(
			Class<ExType> clasz, Function<ExType, ReturnType> handler)
	{
		return swallowMap(clasz, (arg) -> arg, handler);
	}
	
	/**
	 * Convert this to a function which will attempt to recover from the thrown exception.
	 * 
	 * @param clasz The class of the exception. Needed for type-safety reasons.
	 * @param rescue The function to use to convert an exception into a safe input value.
	 * 
	 * @return A function which attempts to recover from a exception.
	 */
	@SuppressWarnings("unchecked")
	default Function<InputType, ReturnType> recover(
			Class<ExType> clasz, BiFunction<InputType, ExType, InputType> rescue)
	{
		return Fixpoints.fix((arg, self) ->  {
			try {
				return this.apply(arg);
			} catch (Throwable ex) {
				if (clasz.isInstance(ex)) {
					// Swallow this
					return self.apply(rescue.apply(arg, (ExType) ex));
				}
				
				String msg = "Exception of incorrect type to be handled, only "
										 + clasz.getName()
										 + " are handled";
				
				throw new RuntimeException(msg, ex);
			}
		});
	}
	
	/**
	 * Create a {@link Thrower} which will yield the result of calling this
	 * function with the given argument.
	 * 
	 * @param arg The argument to use.
	 * 
	 * @return A thrower which will call this function with the given value.
	 */
	default Thrower<ReturnType, ExType> suspend(InputType arg) 
	{
		return () -> this.apply(arg);
	}
	
	/**
	 * Compose two throwing functions together.
	 * 
	 * @param <NewOutput> The newly output type.
	 * 
	 * @param func The throwing function to compose this with.
	 * 
	 * @return A throwing function that composes the two.
	 */
	default <NewOutput> ThrowFunction<InputType, NewOutput, ExType>
	compose(
			ThrowFunction<ReturnType, NewOutput, ExType> func) {
		return (arg) -> func.apply(this.apply(arg));
	}
	
	/** Convert this function into one which will return an empty optional if an
	 * exception is thrown, returning an optional containing the value otherwise.
	 * 
	 * Note that if this function returns a null value by itself, that will also
	 * yield an empty nullable.
	 * 
	 * @param clasz The class of the exception. Needed because of type erasure,
	 *              to ensure that we are catching the proper class.
	 * 
	 * @return A function which returns an optional instead.
	 */
	default Function<InputType, Optional<ReturnType>>
	makeTotal(Class<ExType> clasz)
	{
	    return swallowMap(clasz, Optional::ofNullable, (ignored) -> Optional.empty());
	}
	/**
	 * ThrowFunctions and functions which return a {@link Thrower} are isomorphic.
	 * 
	 * @param <InpType> The function input type.
	 * @param <OutType> The function output type.
	 * @param <ExType> The exception type.
	 * 
	 * @return The isomorphism between them.
	 */
	static <InpType, OutType, ExType extends Throwable>
	Isomorphism<ThrowFunction<InpType, OutType, ExType>, Function<InpType, Thrower<OutType, ExType>>>
	getIso() 
	{
		// @FIXME Nov 23, 2020 Ben Culkin :EquivISO
		// Fix this to strip wrappers when appropriate, so that going
		// backwards and forwards leaves you where you started, not under
		// two levels of indirection.
		return Isomorphism.from((throwFun) -> {
			return (arg) -> throwFun.suspend(arg);
		}, (thrower) -> {
			return (arg) -> thrower.apply(arg).extract();
		}); 
	}
}
