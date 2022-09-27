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

import java.util.function.*;

/**
 * Assorted functional combinators, named after birds as in To Mock a
 * Mockingbird
 * 
 * Consult <a href="https://blog.lahteenmaki.net/combinator-birds.html">this
 * site</a> for more info
 * 
 * @author bjcul
 *
 */
@SuppressWarnings("javadoc")
public class CombinatorBirds {
	// Note that a good number of these birds can be composed from others, but I've
	// generally written them in the fully explicit style

	/**
	 * The 'idiot' or identity bird.
	 * 
	 * @param <A> A type
	 * 
	 * @return A function that returns its argument
	 */
	public static <A> UnaryOperator<A> idiot() {
		return (a) -> a;
	}

	public static <A, B> Function<A, B> idiotOnceRemoved(Function<A, B> func) {
		return func;
	}

	// should variants be written that take BiFunction or a TriFunction?
	// They'd be simple to write, just a bit of a nuisance
	public static <A, B, C> Function<A, Function<B, C>> idiotTwiceRemoved(Function<A, Function<B, C>> func) {
		return func;
	}

	// 'true' in lambda calculus
	public static <A, B> Function<B, A> kestrel(A val) {
		return (vl) -> val;
	}

	// 'false' in lambda calculus
	public static <A, B> Function<B, B> kite(A val) {
		return (vl) -> vl;
	}

	public static <A, B, C> Function<A, C> bluebird(Function<A, B> left, Function<B, C> right) {
		return (vl) -> right.apply(left.apply(vl));
	}

	public static <A, B, C, D> Function<A, Function<B, D>> blackbird(Function<C, D> left,
			Function<A, Function<B, C>> right) {
		return (first) -> (second) -> left.apply(right.apply(first).apply(second));
	}

	public static <A, B, C, D, E> Function<A, Function<B, Function<C, E>>> bunting(Function<D, E> left,
			Function<A, Function<B, Function<C, D>>> right) {
		return (first) -> (second) -> (third) -> left.apply(right.apply(first).apply(second).apply(third));
	}

	public static <A, B, C, D> Function<A, D> becard(Function<C, D> f, Function<B, C> g, Function<A, B> h) {
		return (vl) -> f.apply(g.apply(h.apply(vl)));
	}

	public static <A, B, C> Function<A, C> starling(Function<A, Function<B, C>> f, Function<A, B> g) {
		return (vl) -> f.apply(vl).apply(g.apply(vl));
	}

	public static <A, B, C> Function<A, C> missingBird(Function<B, Function<A, C>> f, Function<A, B> g) {
		return (vl) -> f.apply(g.apply(vl)).apply(vl);
	}

	public static <A, B, C, D> Function<A, D> phoenix(Function<B, Function<C, D>> f, Function<A, B> g,
			Function<A, C> h) {
		return (vl) -> f.apply(g.apply(vl)).apply(h.apply(vl));
	}

	public static <A, B, C, D> Function<B, D> dove(Function<A, Function<C, D>> f, A x, Function<B, C> g) {
		return (vl) -> f.apply(x).apply(g.apply(vl));
	}

	public static <A, B, C, D> Function<A, Function<B, D>> cardinalPrime(Function<C, Function<A, D>> f,
			Function<B, C> g) {
		return (x) -> (y) -> f.apply(g.apply(y)).apply(x);
	}

	public static <A, B, C, D, E> Function<B, Function<C, E>> eagle(Function<A, Function<D, E>> f, A x,
			Function<B, Function<C, D>> g) {
		return (y) -> (z) -> f.apply(x).apply(g.apply(y).apply(z));
	}

	public static <A, B, C, D, E> Function<C, E> dickcissel(Function<A, Function<B, Function<D, E>>> f, A x, B y,
			Function<C, D> g) {
		return (z) -> f.apply(x).apply(y).apply(g.apply(z));
	}

	public static <A, B, C> Function<A, Function<A, C>> psi(Function<B, Function<B, C>> f, Function<A, B> g) {
		return (x1) -> (x2) -> f.apply(g.apply(x1)).apply(g.apply(x2));
	}

	public static <A, B, C, D, E> Function<B, E> dovekie(Function<C, Function<D, E>> f, Function<A, C> g, A x,
			Function<B, D> h) {
		return (y) -> f.apply(g.apply(x)).apply(h.apply(y));
	}

	public static <A, B, C, D, E, F, G> Function<C, Function<D, G>> baldEagle(Function<E, Function<F, G>> f,
			Function<A, Function<B, E>> g, A x, B y, Function<C, Function<D, F>> h) {
		return (z) -> (zz) -> f.apply(g.apply(x).apply(y)).apply(h.apply(z).apply(zz));
	}

	public static <A, B> Function<A, B> warbler(Function<A, Function<A, B>> f) {
		return (x) -> f.apply(x).apply(x);
	}

	public static <A, B, C> Function<A, Function<B, C>> warblerOnceRemoved(Function<A, Function<B, Function<B, C>>> f) {
		return (x) -> (y) -> f.apply(x).apply(y).apply(y);
	}

	public static <A, B, C, D> Function<A, Function<B, Function<C, D>>> warblerTwiceRemoved(
			Function<A, Function<B, Function<C, Function<C, D>>>> f) {
		return (x) -> (y) -> (z) -> f.apply(x).apply(y).apply(z).apply(z);
	}

	public static <A, B, C> Function<A, Function<B, C>> hummingbird(Function<A, Function<B, Function<A, C>>> f) {
		return (x) -> (y) -> f.apply(x).apply(y).apply(x);
	}

	public static <A, B> Function<A, Function<B, Function<A, B>>> jay(Function<A, Function<B, B>> f) {
		return (x1) -> (y) -> (x2) -> f.apply(x1).apply(f.apply(x2).apply(y));
	}

	public static <A, B, C> Function<A, Function<B, C>> jalt(Function<A, C> f) {
		return (x) -> (y) -> f.apply(x);
	}

	public static <A, B, C, D> Function<A, Function<B, Function<C, D>>> jaltPrime(Function<A, Function<B, D>> f) {
		return (x) -> (y) -> (z) -> f.apply(x).apply(y);
	}

	public static <A, B, C, D, E> Function<D, Function<E, C>> gamma(
			Function<Function<A, Function<B, C>>, Function<D, Function<E, B>>> f, Function<A, Function<B, C>> g,
			Function<D, A> h) {
		return (dVal) -> (eVal) -> g.apply(h.apply(dVal)).apply(f.apply(g).apply(dVal).apply(eVal));
	}

	// Mockingbird/Lark aren't expressible easily in a static language

	public static <A, B> Function<Function<A, B>, B> owl(Function<Function<A, B>, A> f) {
		return (g) -> g.apply(f.apply(g));
	}

	// Sage has the type-signature (a -> a) -> a, but we need to introduce the
	// Supplier to add necessary laziness. Otherwise we'd recur without end
	public static <A> A sage(Function<Supplier<A>, A> f) {
		return f.apply(() -> sage(f));
	}

	public static <A, B, C, D> Function<A, Function<B, D>> goldfinch(Function<B, Function<C, D>> f, Function<A, C> h) {
		return (x) -> (y) -> f.apply(y).apply(h.apply(x));
	}

	public static <A, B> Function<Function<A, B>, B> thrush(A x) {
		return (f) -> f.apply(x);
	}

	public static <A, B, C> Function<B, Function<A, C>> cardinal(Function<A, Function<B, C>> f) {
		return (y) -> (x) -> f.apply(x).apply(y);
	}

	public static <A, B, C> Function<Function<A, Function<B, C>>, C> finch(B y, A x) {
		return (f) -> f.apply(x).apply(y);
	}

	public static <A, B, C> Function<A, C> robin(B y, Function<A, Function<B, C>> f) {
		return (x) -> f.apply(x).apply(y);
	}

	// the merit of having these instead of just a general 'swap' combinator is a
	// bit questionable
	public static <A, B, C> Function<Function<A, Function<B, C>>, C> vireo(A x, B y) {
		return (f) -> f.apply(x).apply(y);
	}

	// not written here: robin/finch/cardinal/vireo once/twice removed
	public static <A, B, C> Function<A, C> queer(Function<A, B> f, Function<B, C> g) {
		return (x) -> g.apply(f.apply(x));
	}

	public static <A, B, C> Function<Function<A, B>, C> quixotic(Function<B, C> f, A x) {
		return (g) -> f.apply(g.apply(x));
	}

	public static <A, B, C> Function<Function<A, B>, C> quizzical(A x, Function<B, C> f) {
		return (g) -> f.apply(g.apply(x));
	}
	
	public static <A, B, C> Function<Function<B, C>, C> quirky(Function<A, B> f, A x) {
		return (g) -> g.apply(f.apply(x));
	}
	
	public static <A, B, C> Function<Function<B, C>, C> quacky(A x, Function<A, B> f) {
		return (g) -> g.apply(f.apply(x));
	}
	
	public static <A, B> Function<Function<A, Function<A, B>>, B> converseWarbler(A x) {
		return (f) -> f.apply(x).apply(x);
	}
}
