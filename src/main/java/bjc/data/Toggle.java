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

/**
 * A stateful holder that swaps between two values of the same type.
 *
 * @author EVE
 *
 * @param <E>
 *            The value stored in the toggle.
 */
public interface Toggle<E> {
	/**
	 * Retrieve the currently-aligned value of this toggle, and swap the value to
	 * the new one.
	 *
	 * @return The previously-aligned value.
	 */
	E get();

	/**
	 * Retrieve the currently-aligned value without altering the alignment.
	 *
	 * @return The currently-aligned value.
	 */
	E peek();

	/**
	 * Change the alignment of the toggle.
	 *
	 * @param isLeft
	 *               Whether the toggle should be left-aligned or not.
	 */
	void set(boolean isLeft);
}
