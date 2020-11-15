package bjc.functypes;

/**
 * Function which picks a single element from an array of elements.
 * 
 * @author Ben Culkin
 *
 * @param <ElementType> The type of element stored in the array.
 */
@FunctionalInterface
public interface ArrayChooser<ElementType> {
	/**
	 * Select a single element from an array of elements.
	 * 
	 * @param elements The elements to pick from.
	 * 
	 * @return The selected element.
	 */
	public ElementType choose(@SuppressWarnings("unchecked") ElementType... elements);
}
