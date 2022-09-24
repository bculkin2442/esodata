package bjc.functypes;

/**
 * The class that exists, but does nothing else.
 * @author bjcul
 *
 */
public class Unit {
	/**
	 * The single instance of Unit that exists
	 */
	public static final Unit UNIT = new Unit();
	
	private Unit() {
		// Empty
	}
}
