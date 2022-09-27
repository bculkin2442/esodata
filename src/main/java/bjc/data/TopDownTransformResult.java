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
 * Represents the results for doing a top-down transform of a tree.
 *
 * @author ben
 *
 */
public enum TopDownTransformResult {
	/** Do not do anything to this node, and ignore its children. */
	SKIP,
	/** Transform this node, and don't touch its children. */
	TRANSFORM,
	/** Transform this node, then recursively transform the result. */
	RTRANSFORM,
	/** Ignore this node, and traverse its children. */
	PASSTHROUGH,
	/** Traverse the nodes of this children, then transform it. */
	PUSHDOWN,
	/** Transform this node, then traverse its children. */
	PULLUP;
}
