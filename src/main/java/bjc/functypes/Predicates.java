/* 
 * esodata - data structures of varying utility
 * 
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
package bjc.functypes;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Various utility functions for predicates
 * @author bjcul
 *
 */
public class Predicates {
	/**
	 * A predicate for logical implication (!x || y)
	 * 
	 * @param x The LHS
	 * @param y The RHS
	 * 
	 * @return Whether the LHS implies the RHS
	 */
	public static boolean implies(boolean x, boolean y) {
		return !x || y;
	}
}