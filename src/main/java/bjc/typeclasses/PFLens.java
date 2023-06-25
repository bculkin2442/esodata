package bjc.typeclasses;

import java.util.function.Function;

/**
 * A profunctor lens
 * 
 * @author bjcul
 *
 * @param <Whole1> The first whole
 * @param <Whole2> The second whole
 * @param <Part1> The first part
 * @param <Part2> The second part
 */
public interface PFLens<Whole1, Whole2, Part1, Part2> {
	// Container should be Functor once that is ironed out
	/**
	 * Run the lens in the given functor
	 * 
	 * @param <A> First argument 
	 * @param <B> second argument
	 * @param <F> Underlying functor
	 * 
	 * @param f The function to apply
	 * 
	 * @return The result of applying the functor
	 */
	<A, B, F extends Container<?, F>> Function<Whole1, Container<Whole2, F>> run(Function<Part1, Container<Part2, F>> f);
}
