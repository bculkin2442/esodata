/* 
 * esodata - data structures of varying use
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

import java.util.Collection;

/**
 * Represents an optic allowing access to the elements of a given item
 * 
 * @author bjcul
 *
 * @param <C1> The first collection type
 * @param <C2> The second collection type
 * @param <E1> The first element type
 * @param <E2> The second element type
 */
public interface TraversalX<C1, C2, E1, E2> extends Optic<C1, C2, E1, E2> {
	// Note: these should be fixed size collections of the same type, but that would
	// be awkward to represent
	/**
	 * Extract the contents of the collection
	 * 
	 * @param whole The collection to get the contents of.
	 * @return The contents of the given collection.
	 */
	Collection<E1> contents(C1 whole);

	/**
	 * Replace the contents of the given collection
	 * 
	 * @param whole The collection to replace the contents of
	 * @param bits  The new contents of the collection
	 * 
	 * @return A collection with the new contents
	 */
	C2 fill(C1 whole, Collection<E2> bits);
	
	// TODO implement 'of'
}
