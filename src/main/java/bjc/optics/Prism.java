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

import java.util.function.Function;

import bjc.data.Either;
import bjc.typeclasses.BiContainer;

/**
 * A type-invariant prism
 * 
 * @author bjcul
 *
 * @param <Whole> The type of the whole
 * @param <Part>  The type of the part
 */
public interface Prism<Whole, Part>
		extends PrismX<Whole, Whole, Part, Part>, BiContainer<Whole, Part, Prism<Whole, Part>> {

	/**
	 * Create a type-invariant prism from its component parts.
	 * 
	 * @param <Whole> The type of the whole
	 * @param <Part>  The type of the part
	 * 
	 * @param f       The 'match' function of the prism
	 * @param g       The 'build' function of the prism
	 * 
	 * @return The prism composed of the given parts.
	 */
	static <Whole, Part> Prism<Whole, Part> of(Function<Part, Either<Part, Whole>> f, Function<Whole, Part> g) {
		return new FunctionalPrism<>(g, f);
	}
}

final class FunctionalPrism<Whole, Part> implements Prism<Whole, Part> {
	private final Function<Whole, Part> g;
	private final Function<Part, Either<Part, Whole>> f;

	public FunctionalPrism(Function<Whole, Part> g, Function<Part, Either<Part, Whole>> f) {
		this.g = g;
		this.f = f;
	}

	@Override
	public Part build(Whole whole) {
		return g.apply(whole);
	}

	@Override
	public Either<Part, Whole> match(Part part) {
		return f.apply(part);
	}
}