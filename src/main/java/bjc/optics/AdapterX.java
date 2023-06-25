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
package bjc.optics;

import java.util.function.Function;

/**
 * An adapter that maps between two sets of types
 * 
 * @author bjcul
 *
 * @param <F1> The type of the first source
 * @param <F2> The type of the second source
 * @param <T1> The type of the first destination
 * @param <T2> The type of the second destination
 */
public interface AdapterX<F1, F2, T1, T2> extends Optic<F1, F2, T1, T2> {

	/**
	 * Create a source from a destination
	 * 
	 * @param source The destination to use
	 * 
	 * @return A source corresponding to the destination
	 */
	F1 from(T1 source);

	/**
	 * Create a destination from a source
	 * 
	 * @param source The source to use
	 * 
	 * @return A destination corresponding to the source
	 */
	T2 to(F2 source);

	/**
	 * Create an adapter from its component parts
	 * 
	 * @param <F1> The type of the first source
	 * @param <F2> The type of the second source
	 * @param <T1> The type of the first destination
	 * @param <T2> The type of the second destination
	 * 
	 * @param f    The 'from' function
	 * @param g    The 'to' function
	 * 
	 * @return The adapter constructed from the given parts.
	 */
	public static <F1, F2, T1, T2> AdapterX<F1, F2, T1, T2> of(Function<T1, F1> f, Function<F2, T2> g) {
		return new FunctionalAdapterX<>(f, g);
	}
}

final class FunctionalAdapterX<W1, W2, P1, P2> implements AdapterX<W1, W2, P1, P2> {
	private final Function<P1, W1> f;
	private final Function<W2, P2> g;

	public FunctionalAdapterX(Function<P1, W1> f, Function<W2, P2> g) {
		this.f = f;
		this.g = g;
	}

	@Override
	public W1 from(P1 source) {
		return f.apply(source);
	}

	@Override
	public P2 to(W2 source) {
		return g.apply(source);
	}
}
