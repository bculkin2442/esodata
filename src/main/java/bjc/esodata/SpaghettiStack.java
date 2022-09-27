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
package bjc.esodata;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Implements a spaghetti stack, which is a stack that is branched off of a
 * parent stack.
 *
 * @param <T>
 *            The datatype stored in the stack.
 *
 * @author Ben Culkin
 */
class SpaghettiStack<T> extends Stack<T> {
	/* Our backing stack. */
	private final Stack<T> backing;
	/* The stack we branched off of. */
	private final Stack<T> parent;

	/**
	 * Create a new empty spaghetti stack, off of the specified parent.
	 *
	 * @param par
	 *            The parent stack
	 */
	public SpaghettiStack(final Stack<T> par) {
		backing = new SimpleStack<>();

		parent = par;
	}

	@Override
	public void push(final T elm) {
		backing.push(elm);
	}

	@Override
	public T pop() {
		if (backing.isEmpty()) return parent.pop();

		return backing.pop();
	}

	@Override
	public T top() {
		if (backing.isEmpty()) return parent.top();

		return backing.top();
	}

	@Override
	public int size() {
		return parent.size() + backing.size();
	}

	@Override
	public boolean isEmpty() {
		return backing.isEmpty() && parent.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] toArray() {
		return (T[]) Stream
				.concat(Arrays.stream(parent.toArray()), Arrays.stream(backing.toArray()))
				.toArray();
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 1;
		result = prime * result + (backing == null ? 0 : backing.hashCode());
		result = prime * result + (parent == null ? 0 : parent.hashCode());

		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                         return true;
		if (obj == null)                         return false;
		if (!(obj instanceof SpaghettiStack<?>)) return false;

		final SpaghettiStack<?> other = (SpaghettiStack<?>) obj;

		if (backing == null) {
			if (other.backing != null) return false;
		} else if (!backing.equals(other.backing)) {
			return false;
		}

		if (parent == null) {
			if (other.parent != null) return false;
		} else if (!parent.equals(other.parent)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return String.format("SpaghettiStack [backing=%s, parent=%s]", backing, parent);
	}
}
