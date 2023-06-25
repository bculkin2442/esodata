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
 * A simple implementation of {@link Toggle}.
 *
 * @author EVE
 *
 * @param <E>
 *            The type of value to toggle between.
 */
public class ValueToggle<E> implements Toggle<E> {
	/* Our left value. */
	private final E lft;
	/* Our right value. */
	private final E rght;
	/* Our alignment. */
	private final BooleanToggle alignment;

	/**
	 * Create a new toggle.
	 *
	 * All toggles start right-aligned.
	 *
	 * @param left
	 *              The value when the toggle is left-aligned.
	 *
	 * @param right
	 *              The value when the toggle is right-aligned.
	 */
	public ValueToggle(final E left, final E right) {
		lft = left;

		rght = right;

		alignment = new BooleanToggle();
	}

	@Override
	public E get() {
		return alignment.get() ? lft : rght;
	}

	@Override
	public E peek() {
		return alignment.peek() ? lft : rght;
	}

	@Override
	public void set(final boolean isLeft) {
		alignment.set(isLeft);
	}
}
