package bjc.functypes.optics;

/**
 * A type-invariant lens for mutating objects.
 * 
 * Note that there is no type-variant version, because that wouldn't make much sense.
 * 
 * Also, mixing mutable and immutable lenses may lead to confusion.
 * 
 * @author bjcul
 *
 * @param <Whole> The type the lens is used on
 * @param <Part> The type the lens is focused on
 */
public interface MutableLens<Whole, Part> extends Lens<Whole, Part> {
	/**
	 * Apply a mutation to an item.
	 * 
	 * @param source The item to use the lens on.
	 * @param val The new value for the focused field.
	 */
	void mutate(Whole source, Part val);
	
	@Override
	default Whole set(Whole source, Part val) {
		mutate(source, val);
		return source;
	}
}
