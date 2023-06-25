/* 
 * esodata - data structures of varying utility
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

import java.util.Optional;

import bjc.data.Either;

/**
 * Various utilities for working with prisms
 * 
 * @author bjcul
 *
 */
public class Prisms {
	/**
	 * Create a prism that reflects on the value in an optional
	 * 
	 * @param <L> The type contained in the optional
	 * @param <R> An auxiliary type
	 * 
	 *            TODO: better notes for what R is for
	 * 
	 * @return A prism that reflects on an optional
	 */
	public static <L, R> PrismX<L, R, Optional<L>, Optional<R>> optional() {
		return PrismX.of((opt) -> opt.isPresent() ? Either.right(opt.get()) : Either.left(Optional.empty()),
				Optional::ofNullable);
	}

	/**
	 * Create a prism that reflects on whether a double corresponds to a given
	 * integer.
	 * 
	 * @return A prism on the int view of a double.
	 */
	public static Prism<Integer, Double> whole() {
		return Prism.of((vl) -> Math.rint(vl) == vl ? Either.right(vl.intValue()) : Either.left(vl),
				Integer::doubleValue);
	}
}
