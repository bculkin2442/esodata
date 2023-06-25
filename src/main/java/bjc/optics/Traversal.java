package bjc.optics;

import bjc.typeclasses.BiContainer;

/**
 * A traversal
 * @author bjcul
 *
 * @param <C> The container type
 * @param <E> The element type
 */
public interface Traversal<C, E> extends TraversalX<C, C, E, E>, BiContainer<C, E, Traversal<C, E>> {
	// TODO implement 'of'
}
