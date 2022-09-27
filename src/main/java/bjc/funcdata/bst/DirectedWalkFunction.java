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
package bjc.funcdata.bst;

/**
 * Represents a function for doing a directed walk of a binary tree.
 *
 * @author ben
 *
 * @param <T>
 *            The type of element stored in the walked tree
 */
@FunctionalInterface
public interface DirectedWalkFunction<T> {
	/**
	 * Represents the results used to direct a walk in a binary tree.
	 *
	 * @author ben
	 */
	public enum DirectedWalkResult {
		/** Specifies that the function has failed. */
		FAILURE,
		/**
		 * Specifies that the function wants to move left in the tree next.
		 */
		LEFT,
		/**
		 * Specifies that the function wants to move right in the tree next.
		 */
		RIGHT,
		/** Specifies that the function has succesfully completed */
		SUCCESS
	}

	/**
	 * Perform a directed walk on a node of a tree.
	 *
	 * @param element
	 *                The data stored in the node currently being visited.
	 *
	 * @return The way the function wants the walk to go next.
	 */
	public DirectedWalkResult walk(T element);
}
