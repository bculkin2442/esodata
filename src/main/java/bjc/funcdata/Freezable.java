package bjc.funcdata;

/**
 * Indicates that an object can switch between immutable and mutable modes.
 * 
 * Note that this only implements 'shallow' immutability. Namely, any sub-objects
 * are not made immutable, and if the type is a collection, the elements are still
 * as mutable as they were before.
 * 
 * Implementations of this interface may choose to throw {@link ObjectFrozen} if
 * you attempt to modify a frozen object, but they may also choose not to.
 * 
 * @author Ben Culkin
 */
public interface Freezable {
	/**
	 * Freezes the internal state of this object, making it immutable.
	 * 
	 * @return True if the object is frozen, false if it couldn't be frozen.
	 */
	public boolean freeze();
	/**
	 * Thaws the internal state of this object, making it mutable.
	 * 
	 * @return True if the object is thawed, false if it couldn't be thawed.
	 */
	public boolean thaw();

	/**
	 * 'Deep-freeze' this object, making it immutable and disabling the ability to
	 * thaw it.
	 * 
	 * @return True if the object was deep-frozen, false if that couldn't happen.
	 */
	default boolean deepFreeze() {
		return false;
	}
	
	/**
	 * Check if this object can be frozen.
	 * 
	 * @return Whether or not the object can be frozen.
	 */
	default boolean canFreeze() {
		return false;
	}
	
	/**
	 * Checks if this object can be thawed.
	 * 
	 * @return Whether or not the object can be thawed.
	 */
	default boolean canThaw() {
		return false;
	}
	
	/**
	 * Determines if this object is frozen.
	 * 
	 * @return True if the object is frozen, false if the object is thawed.
	 */
	public boolean isFrozen();
	

	/**
	 * Determines if this object is thawed.
	 * 
	 * @return True if the object is thawed, false if the object is thawed.
	 */
	default boolean isThawed() {
		return !isFrozen();
	}
}
