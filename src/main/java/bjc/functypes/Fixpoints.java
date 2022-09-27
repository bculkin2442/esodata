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

import bjc.data.*;

/**
 * Function combinators to form 'fixpoints' or recursive functions.
 * 
 * @author Ben Culkin
 *
 */
public interface Fixpoints {
	/**
	 * Convert a two-argument function to a single argument recursive one.
	 * 
	 * @param <InputType> The input type for the function.
	 * @param <ReturnType> The output type for the function.
	 * 
	 * @param func The function to make recursive.
	 * 
	 * @return The newly recursive function.
	 */
	static <InputType, ReturnType> Function<InputType, ReturnType> fix(
			BiFunction<InputType, Function<InputType, ReturnType>, ReturnType> func) {
		Holder<Function<InputType, ReturnType>> inner = Holder.of(null);
		inner.replace((arg) -> {
			return func.apply(arg, inner.getValue());
		});
		return inner.getValue();
	}
	
	/**
	 * Convert a two-argument function to a single argument recursive memoized one.
	 * 
	 * @param <InputType> The input type for the function.
	 * @param <ReturnType> The output type for the function.
	 * 
	 * @param func The function to make recursive.
	 * 
	 * @return The newly recursive memoized function.
	 */
	static <InputType, ReturnType> Function<InputType, ReturnType> memoFix(
			BiFunction<InputType, Function<InputType, ReturnType>, ReturnType> func) {
		Map<InputType, ReturnType> memoMap = new HashMap<>();
		
		Holder<Function<InputType, ReturnType>> inner = Holder.of(null);
		inner.replace((arg) ->
			memoMap.computeIfAbsent(
					arg,
					(newArg) -> func.apply(newArg, inner.getValue())
			)
		);
		
		return inner.getValue();
	}
}
