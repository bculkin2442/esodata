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
package bjc.data;

import java.util.Iterator;
import java.util.function.Function;

/**
 * An iterator that transforms values from one type to another.
 *
 * @author EVE
 *
 * @param <S>
 *            The source iterator type.
 *
 * @param <D>
 *            The destination iterator type.
 */
public class TransformIterator<S, D> implements Iterator<D> {
	/* Our source of values. */
	private final Iterator<S> source;
	/* Transform function. */
	private final Function<S, D> transform;

	/**
	 * Create a new transform iterator.
	 *
	 * @param source
	 *                  The source iterator to use.
	 *
	 * @param transform
	 *                  The transform to apply.
	 */
	public TransformIterator(final Iterator<S> source, final Function<S, D> transform) {
		this.source = source;
		this.transform = transform;
	}

	@Override
	public boolean hasNext() {
		return source.hasNext();
	}

	@Override
	public D next() {
		return transform.apply(source.next());
	}
}
