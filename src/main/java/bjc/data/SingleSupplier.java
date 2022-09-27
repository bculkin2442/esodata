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

import java.util.function.Supplier;

/**
 * A supplier that can only supply one value.
 *
 * Attempting to retrieve another value will cause an exception to be thrown.
 *
 * @author ben
 *
 * @param <T>
 *            The supplied type
 */
public class SingleSupplier<T> implements Supplier<T> {
	/* The next supplier ID. */
	private static long nextID = 0;
	/* The supplier to yield from. */
	private final Supplier<T> source;
	/* Whether this value has been retrieved yet. */
	private boolean gotten;
	/* The ID of this supplier. */
	private final long id;

	/*
	 * The place where the supplier was instantiated.
	 *
	 * @NOTE This is both slow to create, and generally bad practice to keep
	 * exceptions around without throwing them. However, it is very useful to find
	 * where the first instantiation was.
	 */
	private Exception instSite;

	/**
	 * Create a new single supplier from an existing value.
	 *
	 * @param supp
	 *             The supplier to give a single value from.
	 */
	public SingleSupplier(final Supplier<T> supp) {
		source = supp;

		gotten = false;

		id = nextID++;
	}

	@Override
	public T get() {
		if (gotten == true) {
			final String msg = String.format(
					"Attempted to retrieve value more than once from single supplier #%d",
					id);

			final IllegalStateException isex = new IllegalStateException(msg);

			isex.initCause(instSite);

			throw isex;
		}

		gotten = true;

		try {
			throw new IllegalStateException("Previous instantiation here.");
		} catch (final IllegalStateException isex) {
			instSite = isex;
		}

		return source.get();
	}

	@Override
	public String toString() {
		return String.format("SingleSupplier [source='%s', gotten=%s, id=%s]", source,
				gotten, id);
	}
}
