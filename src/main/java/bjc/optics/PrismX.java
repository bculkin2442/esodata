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
package bjc.optics;

import java.util.function.Function;

import bjc.data.Either;

/**
 * Represents a Prism, which is a type of optic.
 * 
 * TODO: Add better description
 * 
 * @author bjcul
 *
 * @param <W1> The type of the first whole
 * @param <W2> The type of the second whole
 * @param <P1> The type of the first part
 * @param <P2> The type of the second part
 */
public interface PrismX<W1, W2, P1, P2> extends Optic<W1, W2, P1, P2> {
	/**
	 * Match against this prism.
	 * 
	 * @param part The part to match
	 * @return Either the matched value or the prism
	 */
	Either<P2, W1> match(P1 part);

	/**
	 * Build this prism from a given part.
	 * 
	 * @param whole The whole to build from
	 * @return The part that is constructed
	 */
	P2 build(W2 whole);

	/**
	 * Create a prism from its component parts
	 * 
	 * @param <W1> The type of the first whole
	 * @param <W2> The type of the second whole
	 * @param <P1> The type of the first part
	 * @param <P2> The type of the second part
	 * 
	 * @param f The 'match' function for the prism
	 * @param g The 'build' function for the prism
	 * 
	 * @return A prism built from the given parts
	 */
	static <W1, W2, P1, P2> PrismX<W1, W2, P1, P2> of(Function<P1, Either<P2, W1>> f, Function<W2, P2> g) {
		return new FunctionalPrismX<>(g, f);
	}
}

final class FunctionalPrismX<W1, W2, P1, P2> implements PrismX<W1, W2, P1, P2> {
	private final Function<W2, P2> g;
	private final Function<P1, Either<P2, W1>> f;

	public FunctionalPrismX(Function<W2, P2> g, Function<P1, Either<P2, W1>> f) {
		this.g = g;
		this.f = f;
	}

	@Override
	public P2 build(W2 whole) {
		return g.apply(whole);
	}

	@Override
	public Either<P2, W1> match(P1 part) {
		return f.apply(part);
	}
}