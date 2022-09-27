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
package bjc.funcdata.theory;

import java.util.Set;
import java.util.function.Function;

import bjc.data.Pair;
import bjc.esodata.PairMap;

// NOTE: haskell uses Category cat: ... where the parameter is the arrow type
public interface Category<Element, Arrow> {	
	Arrow identity(Element elem);
	Arrow compose(Arrow left, Arrow right);
}
// Java objects would form a category as Category<Class<?>, Function<?, ?>>

class FuncCategory implements Category<Class<?>, Function<?, ?>> {
	@Override
	public Function<?, ?> identity(Class<?> elem) {
		return (vl) -> vl;
	}

	@Override
	public Function<?, ?> compose(Function<?, ?> left, Function<?, ?> right) {
		// right.compose((Function<? super V, ?>) left);
		return null;
	}
	
}