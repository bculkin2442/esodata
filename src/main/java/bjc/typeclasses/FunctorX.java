package bjc.typeclasses;

import java.util.function.Function;

// TODO: determine if it makes sense to replace regular Functor w/ this
public interface FunctorX<A, F extends FunctorX<?, F>> extends Container<A, F> {
	<B> FunctorX<B, F> fmap(Function<? super A, ? extends B> fn);
	
	// Actually no way to write a 'of' function here; the embedded B gets in our way
}
