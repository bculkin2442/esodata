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

/**
 * A type-invariant lens for mutating objects.
 * 
 * Note that there is no type-variant version, because that wouldn't make much sense.
 * 
 * Also, mixing mutable and immutable lenses may lead to confusion.
 * 
 * @author bjcul
 *
 * @param <Whole> The type the lens is used on
 * @param <Part> The type the lens is focused on
 */
public interface MutableLens<Whole, Part> extends Lens<Whole, Part> {
	/**
	 * Apply a mutation to an item.
	 * 
	 * @param source The item to use the lens on.
	 * @param val The new value for the focused field.
	 */
	void mutate(Whole source, Part val);
	
	@Override
	default Whole set(Whole source, Part val) {
		mutate(source, val);
		return source;
	}
}
