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
 * Represents an iterator over an array of values.
 *
 * @param <T>
 *            The type of values in the array.
 *
 * @author Ben Culkin
 */
public class ArrayIterator<T> implements Iterator<T> {
	private Object[] arr;
	private int idx;

	/**
	 * Create a new array iterator.
	 *
	 * @param elms
	 *             The array that will be iterated over.
	 */
	@SafeVarargs
	public ArrayIterator(T... elms) {
		arr = elms;
		idx = 0;
	}

	@Override
	public boolean hasNext() {
		return idx < arr.length;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		if (idx >= arr.length) return null;

		return (T) (arr[idx++]);
	}
}
