package bjc.typeclasses;

import java.util.function.Function;

/**
 * A profunctor lens
 * 
 * @author bjcul
 *
 * @param <Whole1>
 * @param <Whole2>
 * @param <Part1>
 * @param <Part2>
 */
public interface PFLens<Whole1, Whole2, Part1, Part2> {
	// Container should be Functor once gthat is ironed out
	<A, B, F extends Container<?, F>> Function<Whole1, Container<Whole2, F>> run(Function<Part1, Container<Part2, F>> f);
}
