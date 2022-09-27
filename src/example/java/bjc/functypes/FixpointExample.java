/* 
 * esodata - Data structures of varying utility
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

import static bjc.functypes.Combinators.*;

import java.util.function.*;

/**
 * Examples about how fixpoints work.
 * 
 * @author Ben Culkin
 *
 */
public class FixpointExample {
	/**
	 * Main method.
	 * 
	 * @param args Unused CLI args.
	 */
	public static void main(String[] args) {
		Function<Integer,
				Integer> factorial = Fixpoints.fix(
						(input, self) -> input <= 1 ? 1 : input * self.apply(input - 1));

		times(10, andThen(factorial::apply, System.out::println));
	}
}