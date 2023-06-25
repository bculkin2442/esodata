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
 * A toggle that will only give the first value once, only yielding the second
 * value afterwards.
 *
 * @author student
 *
 * @param <E>
 *            The type of value stored
 */
public class OneWayToggle<E> implements Toggle<E> {
	private E first;
	private E second;

	private boolean gotFirst;

	/**
	 * Create a new one-way toggle
	 *
	 * @param first
	 *               The value to offer first, and only once
	 * @param second
	 *               The value to offer second and repeatedly
	 */
	public OneWayToggle(E first, E second) {
		this.first = first;

		this.second = second;
	}

	@Override
	public E get() {
		if (gotFirst) return second;

		gotFirst = true;
		
		return first;
	}

	@Override
	public E peek() {
		return gotFirst ? second : first;
	}

	@Override
	public void set(boolean gotFirst) {
		this.gotFirst = gotFirst;
	}
}
