package bjc.functypes.optics;

/**
 * A type-invariant var Laarhoven lens.
 * 
 * @author bjcul
 *
 * @param <Whole> The item this lens can focus on
 * @param <Part> The field this lens focuses on
 */
public interface Lens<Whole, Part> extends LensX<Whole, Whole, Part, Part> {
	// Alias type
}
