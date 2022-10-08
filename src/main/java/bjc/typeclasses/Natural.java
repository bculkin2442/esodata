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
package bjc.typeclasses;

/**
 * A natural transformation; a mapping between two type-containers which
 * preserves the contents of the containers.
 * 
 * @author bjcul
 *
 * @param <F> The actual type of the first container
 * @param <G> The actual type of the second container
 */
@FunctionalInterface
public interface Natural<F extends Container<?, F>, G extends Container<?, G>>
		extends BiContainer<F, G, Natural<?, ?>> {
	/**
	 * Apply the natural transformation.
	 * 
	 * @param <X> The type contained in the container
	 * 
	 * @param val The container to be transformed
	 * 
	 * @return The value, in the other container type
	 */
	<X> Container<X, G> apply(Container<X, F> val);

	/**
	 * The simplest possible natural transform, mapping a type to itself.
	 * 
	 * @return The ID natural transform
	 */
	public static <C extends Container<?, C>> Natural<C, C> ID() {
		return new Natural<>() {
			@Override
			public <X> Container<X, C> apply(Container<X, C> val) {
				return val;
			}
		};
	}
}
