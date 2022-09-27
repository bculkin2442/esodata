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

/**
 * An iterator that will only ever yield one item.
 *
 * @author EVE
 *
 * @param <T>
 *            The type of the item.
 */
public class SingleIterator<T> implements Iterator<T> {
	/* The item being held. */
	private final T itm;
	/* Whether we've yielded the item yet. */
	private boolean yielded;

	/**
	 * Create a iterator that yields a single item.
	 *
	 * @param item
	 *             The item to yield.
	 */
	public SingleIterator(final T item) {
		itm = item;

		yielded = false;
	}

	@Override
	public boolean hasNext() {
		return !yielded;
	}

	@Override
	public T next() {
		yielded = true;

		return itm;
	}
}
