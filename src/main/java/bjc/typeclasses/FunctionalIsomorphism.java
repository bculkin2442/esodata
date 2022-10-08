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

import java.util.function.Function;

/**
 * A pair of functions to transform between a pair of types.
 *
 * @author bjculkin
 *
 * @param <Source>
 *                 The source type of the isomorphism.
 *
 * @param <Dest>
 *                 The destination type of isomorphism.
 */
public class FunctionalIsomorphism<Source, Dest> implements Isomorphism<Source, Dest>
{
	/* The function to the destination type. */
	private Function<Source, Dest> toFunc;
	/* The function to the source type. */
	private Function<Dest, Source> fromFunc;

	/**
	 * Create a new isomorphism.
	 *
	 * @param to
	 *             The 'forward' function, from the source to the definition.
	 *
	 * @param from
	 *             The 'backward' function, from the definition to the source.
	 */
	public FunctionalIsomorphism(Function<Source, Dest> to,
			Function<Dest, Source> from)
	{
		toFunc = to;
		fromFunc = from;
	}

	@Override
	public Dest to(Source val)
	{
		return toFunc.apply(val);
	}

	@Override
	public Source from(Dest val)
	{
		return fromFunc.apply(val);
	}
}
