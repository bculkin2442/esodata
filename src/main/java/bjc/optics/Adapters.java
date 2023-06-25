/* 
 * esodata - data structures of varying use
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
package bjc.optics;

import bjc.data.Pair;
import bjc.data.Triple;

/**
 * Various utilities for dealing with adapters
 * 
 * @author bjcul
 *
 */
public class Adapters {
	/**
	 * Create an adapter between triples and left-nested pairs.
	 * 
	 * @param <A1> The first left type
	 * @param <A2> The second left type
	 * @param <B1> The first middle type
	 * @param <B2> The second middle type
	 * @param <C1> The first right type
	 * @param <C2> The second right type
	 * 
	 * @return An adapter between triples and left-nested pairs
	 */
	public static <A1, A2, B1, B2, C1, C2> AdapterX<Triple<A1, B1, C1>, Triple<A2, B2, C2>, Pair<Pair<A1, B1>, C1>,
			Pair<Pair<A2, B2>, C2>> flatten() {
		return AdapterX.of((par) -> Triple.of(par.getLeft().getLeft(), par.getLeft().getRight(), par.getRight()),
				(trp) -> Pair.pair(Pair.pair(trp.left(), trp.middle()), trp.right()));
	}
}
