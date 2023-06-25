package bjc.typeclasses;

import java.util.function.Function;

/**
 * Represents an applicative
 * @author bjcul
 *
 * @param <A> The contained type
 * @param <App> The self type
 */
public interface Applicative<A, App extends Applicative<?, App>> extends FunctorX<A, App> {
	/**
	 * Create an applicative from a value
	 * 
	 * @param <B> The type of the value
	 * 
	 * @param val The value
	 * 
	 * @return An applicative containing the given value
	 */
	<B> Applicative<B, App> pure(B val);
	
	/**
	 * Apply the given applicative
	 * 
	 * @param <B> The type of the result
	 * 
	 * @param func An applicative containing a function
	 * 
	 * @return An applicative containing the result of applying the function
	 */
	<B> Applicative<B, App> app(Applicative<Function<? super A, ? extends B>, App> func);
	
	@Override
	default <B> FunctorX<B, App> fmap(Function<? super A, ? extends B> fn) {
		return app(pure(fn));
	}
}
