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

import java.util.function.*;

/**
 * Denotes that two types are essentially equivalent, by providing a means to
 * convert between them.
 * 
 * @author Ben Culkin
 *
 * @param <Source> The first type.
 * @param <Dest> The second type.
 */
public interface Isomorphism<Source, Dest>
{

	/**
	 * Apply the isomorphism forward.
	 *
	 * @param val
	 *            The source value.
	 *
	 * @return The destination value.
	 */
	Dest to(Source val);

	/**
	 * Apply the isomorphism backward.
	 *
	 * @param val
	 *            The destination value.
	 *
	 * @return The source value.
	 */
	Source from(Dest val);
	
	/**
	 * Create an isomorphism from a pair of functions.
	 * 
	 * @param <Src> The source type.
	 * @param <Dst> The destination type.
	 * 
	 * @param forward The function from source to destination.
	 * @param backward The function from destination to source.
	 * 
	 * @return An isomorphism between the two types.
	 */
	static <Src, Dst> Isomorphism<Src, Dst> from(
			Function<Src, Dst> forward,
			Function<Dst, Src> backward)
	{
		return new FunctionalIsomorphism<>(forward, backward);
	}
}