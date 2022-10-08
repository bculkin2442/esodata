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
package bjc.optics;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import bjc.typeclasses.BiContainer;

/**
 * A type-invariant var Laarhoven lens.
 * 
 * @author bjcul
 *
 * @param <Whole> The item this lens can focus on
 * @param <Part> The field this lens focuses on
 */
public interface Lens<Whole, Part> extends LensX<Whole, Whole, Part, Part>, BiContainer<Whole, Part, Lens<Whole, Part>> {
	/**
	 * Modify a given whole using an operation
	 * 
	 * @param source The whole to modify.
	 * @param mod The operation to use for modifying a part
	 * 
	 * @return A modified whole
	 */
	default Whole modify(Whole source, UnaryOperator<Part> mod) {
		return set(source, mod.apply(get(source)));
	}
	
	/**
	 * Create a function which sets the part of a given whole.
	 * 
	 * @param part The part to set
	 * 
	 * @return A function that sets the given part on a whole.
	 */
	default Function<Whole, Whole> setting(Part part) {
		return (whole) -> set(whole, part);
	}
	
	/**
	 * Lift a function that modifies parts to one that modifies wholes.
	 * 
	 * @param f The function which operates on parts
	 * 
	 * @return A corresponding function which applies the given modification to a part.
	 */
	default Function<Whole, Whole> lift(UnaryOperator<Part> f) {
		// modify will be more efficient for some lenses
		return (whole) -> modify(whole, f);
	}
}
