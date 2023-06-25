package bjc.optics;

import bjc.typeclasses.BiContainer;

/**
 * A type-invariant adapter
 * @author bjcul
 *
 * @param <From> The source type
 * @param <To> The destination type
 */
public interface Adapter<From, To>
		extends AdapterX<From, From, To, To>, BiContainer<From, To, Adapter<From, To>> {
	// TODO: write 'of' function
}
