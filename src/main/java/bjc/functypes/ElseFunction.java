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
