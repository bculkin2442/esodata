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
package bjc.functypes;

import java.util.function.UnaryOperator;

/**
 * Identity function.
 *
 * @author bjculkin
 */
public class ID {
	/**
	 * Create an identity function.
	 * 
	 * @param <A> The type of the function.
	 *
	 * @return A identity function.
	 */
	public static <A> UnaryOperator<A> id() {
		return x -> x;
	}
}
