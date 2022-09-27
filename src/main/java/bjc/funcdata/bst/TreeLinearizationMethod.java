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
 * Represents the ways to linearize a tree for traversal.
 *
 * @author ben
 */
public enum TreeLinearizationMethod {
	/**
	 * Visit the left side of this tree part, the tree part itself, and then the
	 * right part.
	 */
	INORDER,
	/**
	 * Visit the left side of this tree part, the right side, and then the tree part
	 * itself.
	 */
	POSTORDER,
	/**
	 * Visit the tree part itself, then the left side of this tree part and then
	 * the right part.
	 */
	PREORDER
}
