package bjc.data;

/**
 * Represents a tuple of three values
 * @author bjcul
 *
 * @param <Left> The type of the first value
 * @param <Middle> The type of the second value
 * @param <Right> The type of the third value
 */
public interface Triple<Left, Middle, Right> {
	// TODO: fill this out more; mapping and the like
	/**
	 * Get the left value for this triple.
	 * 
	 * @return The left value for this triple.
	 */
	public Left left();
	
	/**
	 * Get the right value for this triple.
	 * 
	 * @return The right value for this triple.
	 */
	public Right right();

	/**
	 * Get the middle value for this triple.
	 * 
	 * @return The middle value for this triple.
	 */
	public Middle middle();
	
	/**
	 * Create a new triple
	 * 
	 * @param <Left> The type for the left
	 * @param <Middle> The type for the middle
	 * @param <Right> The type for the right
	 * 
	 * @param l The left value
	 * @param m The middle value
	 * @param r The right value
	 * 
	 * @return A triple of the given values
	 */
	public static <Left, Middle, Right> Triple<Left, Middle, Right> of(Left l, Middle m, Right r) {
		return new SimpleTriple<>(r, m, l);
	}
}

final class SimpleTriple<Left, Middle, Right> implements Triple<Left, Middle, Right> {
	private final Right r;
	private final Middle m;
	private final Left l;

	SimpleTriple(Right r, Middle m, Left l) {
		this.r = r;
		this.m = m;
		this.l = l;
	}
	
	
	@Override
	public Left left() {
		return l;
	}

	@Override
	public Right right() {
		return r;
	}

	@Override
	public Middle middle() {
		return m;
	}
}