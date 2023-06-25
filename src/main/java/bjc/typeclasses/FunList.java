package bjc.typeclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Represent a FunList data structure which contains a B and zero or more As
 * @author bjcul
 *
 * @param <A> The type for A
 * @param <B> The type for B
 */
public sealed interface FunList<A, B> permits FunList.Done<A, B>, FunList.More<A, B> {
	/**
	 * Represents a FunList that contains a B and zero As
	 * 
	 * @author bjcul
	 *
	 * @param <A> Unused type 
	 * @param <B> Type of the contained value
	 */
	public final class Done<A, B> implements FunList<A, B> {
		private final B val;

		/**
		 * Create a new Done FunList
		 * 
		 * @param val The contained value
		 */
		public Done(B val) {
			this.val = val;
		}

		@Override
		public B getB() {
			return val;
		}

		@Override
		public List<A> getAs() {
			return new ArrayList<>();
		}
	}

	/**
	 * A FunList that contains an A and another FunList
	 * @author bjcul
	 *
	 * @param <A> The contained type
	 * @param <B> The type of the eventual result
	 */
	public final class More<A, B> implements FunList<A, B> {
		private A val;
		private FunList<A, Function<A, B>> rest;

		/**
		 * Create a FunList
		 * 
		 * @param val The contained value
		 * @param rest The rest of the list
		 */
		public More(A val, FunList<A, Function<A, B>> rest) {
			this.val = val;
			this.rest = rest;
		}

		@Override
		public B getB() {
			return rest.getB().apply(val);
		}

		@Override
		public List<A> getAs() {
			List<A> as = rest.getAs();
			// Note: this is kinda inefficient. Should we instead say that it returns the As
			// in reverse order
			as.add(0, val);
			return as;
		}
	}

	/**
	 * Get the eventual result
	 * @return the eventual result
	 */
	public B getB();

	/**
	 * Get the contained arguments
	 * 
	 * @return The arguments
	 */
	public List<A> getAs();
	
	// https://twanvl.nl/blog/haskell/non-regular1
	
	// Notes have the following signatures instead
	// Done<T> extends Holder<T>
	// More<A, B> extends Pair<A, B>
	// FunList<A, B, T> extends Either<Done<T>, More<A, FunList<A, B, Function<B, T>>>>
}
