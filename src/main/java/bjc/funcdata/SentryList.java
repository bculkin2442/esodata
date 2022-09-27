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
package bjc.funcdata;

import java.util.List;

/**
 * A list that logs when items are inserted into it.
 *
 * @author bjculkin
 *
 * @param <T>
 *            The type of item in the list.
 */
public class SentryList<T> extends FunctionalList<T> {
	/** Create a new sentry list. */
	public SentryList() {
		super();
	}

	/**
	 * Create a new sentry list backed by an existing list.
	 *
	 * @param backing
	 *                The backing list.
	 */
	public SentryList(final List<T> backing) {
		super(backing);
	}

	@Override
	public boolean add(final T item) {
		final boolean val = super.add(item);

		if (val) {
			System.out.println("Added item (" + item + ") to list");
		}

		return val;
	}
}
