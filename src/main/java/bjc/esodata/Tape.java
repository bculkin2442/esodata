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

/**
 * Interface for something that acts like a tape.
 *
 * A tape is essentially a 1D array with a cursor attached to it, and you can
 * only affect elements at that cursor. The size of the array is theoretically
 * unbounded to the right, but in practice bounded by available memory.
 *
 * @param <T>
 *            The element type of the tape.
 *
 * @author bjculkin
 */
public interface Tape<T> {
	/**
	 * Get the item the tape is currently on.
	 *
	 * @return The item the tape is on.
	 */
	T item();

	/**
	 * Set the item the tape is currently on.
	 *
	 * @param itm
	 *            The new value for the tape item.
	 */
	void item(T itm);

	/**
	 * Get the current number of elements in the tape.
	 *
	 * @return The current number of elements in the tape.
	 */
	int size();

	/**
	 * Get the position of the current item.
	 *
	 * @return The position of the current item.
	 */
	int position();

	/**
	 * Insert an element before the current item.
	 *
	 * @param itm
	 *            The item to add.
	 */
	void insertBefore(T itm);

	/**
	 * Insert an element after the current item.
	 *
	 * @param itm
	 *            The item to insert.
	 */
	void insertAfter(T itm);

	/**
	 * Remove the current element.
	 *
	 * Also moves the cursor back one step if possible to maintain relative
	 * position.
	 *
	 * @return The removed item.
	 */
	T remove();

	/** Move the cursor to the left-most position. */
	void first();

	/** Move the cursor to the right-most position. */
	void last();

	/**
	 * Move the cursor one space left.
	 *
	 * The cursor can't go past zero.
	 *
	 * @return True if the cursor was moved left.
	 */
	boolean left();

	/**
	 * Move the cursor the specified amount left.
	 *
	 * The cursor can't go past zero. Attempts to move the cursor by amounts that
	 * would exceed zero don't move the cursor at all.
	 *
	 * @param amt
	 *            The amount to attempt to move the cursor left.
	 *
	 * @return True if the cursor was moved left.
	 */
	boolean left(int amt);

	/**
	 * Move the cursor one space right.
	 *
	 * @return Whether the cursor was moved right.
	 */
	boolean right();

	/**
	 * Move the cursor the specified amount right.
	 *
	 * @param amt
	 *            The amount to move the cursor right by.
	 *
	 * @return Whether the cursor was moved right.
	 */
	boolean right(int amt);

	/**
	 * Seek to an absolute position on the tape.
	 *
	 * @param pos
	 *            The position to seek to.
	 * @return Whether or not the tape successfully seeked to that position.
	 */
	boolean seekTo(int pos);

	/**
	 * Check if this tape is at its end.
	 *
	 * Equivalent to checking if position() == size().
	 *
	 * @return Whether or not the tape is at its end.
	 */
	default boolean atEnd() {
		return position() == size();
	}

	/**
	 * Append an item to the tape.
	 *
	 * By default, uses a fairly non-performant implementation. Should be overidden
	 * in subclasses to be more performant.
	 * 
	 * @param itm
	 *            The item to append.
	 */
	default void append(T itm) {
		int pos = position();

		last();

		insertAfter(itm);

		seekTo(pos);
	}
}
