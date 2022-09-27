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

import java.util.Deque;
import java.util.LinkedList;

/**
 * A FIFO implementation of a stack.
 *
 * Basically, a stack that actually acts like a queue.
 *
 * @param <T>
 *            The datatype stored in the stack.
 *
 * @author Ben Culkin
 */
public class QueueStack<T> extends Stack<T> {
	/* Our backing queue. */
	private final Deque<T> backing;

	/** Create a new empty stack queue. */
	public QueueStack() {
		backing = new LinkedList<>();
	}

	@Override
	public void push(final T elm) {
		backing.add(elm);
	}

	@Override
	public T pop() {
		if (backing.isEmpty()) throw new StackUnderflow();

		return backing.remove();
	}

	@Override
	public T top() {
		if (backing.isEmpty()) throw new StackUnderflow();

		return backing.peek();
	}

	@Override
	public int size() {
		return backing.size();
	}

	@Override
	public boolean isEmpty() {
		return backing.size() == 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] toArray() {
		return (T[]) backing.toArray();
	}

	@Override
	public String toString() {
		return String.format("QueueStack [backing=%s]", backing);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + (backing == null ? 0 : backing.hashCode());

		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                     return true;
		if (obj == null)                     return false;
		if (!(obj instanceof QueueStack<?>)) return false;

		final QueueStack<?> other = (QueueStack<?>) obj;

		if (backing == null) {
			if (other.backing != null) return false;
		} else if (!backing.equals(other.backing)) {
			return false;
		}

		return true;
	}
}
