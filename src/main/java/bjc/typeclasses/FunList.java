package bjc.typeclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public sealed interface FunList<A, B> permits FunList.Done<A, B>, FunList.More<A, B> {
	public final class Done<A, B> implements FunList<A, B> {
		private final B val;

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

	public final class More<A, B> implements FunList<A, B> {
		private A val;
		private FunList<A, Function<A, B>> rest;

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

	public B getB();

	public List<A> getAs();
	
	// https://twanvl.nl/blog/haskell/non-regular1
}
