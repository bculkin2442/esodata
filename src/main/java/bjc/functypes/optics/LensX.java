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
package bjc.functypes.optics;

import static bjc.functypes.optics.Lenses.immutable;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type-variant lens
 * 
 * @author Ben Culkin
 *
 * @param <W1> The first type the lens is used on
 * @param <W2> The second 'whole' type
 * @param <P1> The first 'part' type
 * @param <P2> The second 'part' type
 */
public interface LensX<W1, W2, P1, P2> extends Optic<W1, W2, P1, P2> {
	/**
	 * Retrieve the focused value of this lens.
	 * 
	 * @param source The item to use the lens on.
	 * 
	 * @return The value from the given whole this lens focuses on.
	 */
	P1 get(W1 source);

	/**
	 * Create an updated version of the item this lens focuses on.
	 * 
	 * @param source The item to use the lens on.
	 * @param val    The new value.
	 * 
	 * @return The updated item the lens was used on.
	 */
	W2 set(W1 source, P2 val);

	/**
	 * Update the focused value.
	 * 
	 * NOTE: It will often be more efficient to implement this directly. The
	 * implementation here is provided for convenience.
	 * 
	 * @param source The item to use the lens on.
	 * @param action The action to applied to the focused item.
	 * 
	 * @return The updated item the lens was used on.
	 */
	default W2 update(W1 source, Function<P1, P2> action) {
		return set(source, action.apply(get(source)));
	}
	
	/**
	 * Compose two type-variant lenses together.
	 * 
	 * @param <V1> The first type the second lens focuses on
	 * @param <V2> The second type the second lens focuses on.
	 * 
	 * @param other The second lens to use.
	 * 
	 * @return A lens composed from this one and the given one.
	 */
	default <V1, V2> LensX<W1, W2, V1, V2> compose(LensX<P1, P2, V1, V2> other) {
		return immutable((whole) -> other.get(get(whole)),
				(whole, val) -> update(whole, (P1 val2) -> other.set(val2, val)));
	}
}