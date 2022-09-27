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
 * An iterator that repeats elements from a provided iterable.
 *
 * @author EVE
 *
 * @param <E>
 *            The type of the iterable.
 */
public class CircularIterator<E> implements Iterator<E> {
	/* The iterable, and our current iterator into it. */
	private Iterable<E> source;
	private Iterator<E> curr;

	/* Our current element. */
	private E curElm;

	/*
	 * Should we actually get new iterators, or just repeat the last element?
	 */
	private boolean doCircle;

	/**
	 * Create a new circular iterator.
	 *
	 * @param src
	 *             The iterable to iterate from.
	 *
	 * @param circ
	 *             Should we actually do circular iteration, or just repeat the
	 *             terminal element?
	 */
	public CircularIterator(final Iterable<E> src, final boolean circ) {
		source = src;
		curr = source.iterator();

		doCircle = circ;
	}

	/**
	 * Create a new circular iterator that does actual circular iteration.
	 *
	 * @param src
	 *            The iterable to iterate from.
	 */
	public CircularIterator(final Iterable<E> src) {
		this(src, true);
	}

	@Override
	public boolean hasNext() {
		/* We always have something. */
		return true;
	}

	@Override
	public E next() {
		if (!curr.hasNext()) {
			if (!doCircle) {
				return curElm;
			}

			curr = source.iterator();
		}

		curElm = curr.next();

		return curElm;
	}

	@Override
	public void remove() {
		curr.remove();
	}
}
