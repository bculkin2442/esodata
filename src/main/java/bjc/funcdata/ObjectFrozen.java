package bjc.funcdata;

/**
 * Exception that implementations of {@link IFreezable} can throw if you attempt
 * to modify a frozen object.
 * 
 * @author Ben Culkin
 *
 */
public class ObjectFrozen extends RuntimeException {
	private static final long serialVersionUID = -1567447627139090728L;

	/**
	 * Create a new ObjectFrozen exception.
	 */
	public ObjectFrozen() {
		super();
	}

	/**
	 * Create a new ObjectFrozen exception.
	 * 
	 * @param message The message of the exception.
	 */
	public ObjectFrozen(String message) {
		super(message);
	}

	/**
	 * Create a new ObjectFrozen exception.
	 * 
	 * @param cause The root cause of this exception.
	 */
	public ObjectFrozen(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a new ObjectFrozen exception.
	 * 
	 * @param message The message of the exception.
	 * @param cause The root cause of this exception.
	 */
	public ObjectFrozen(String message, Throwable cause) {
		super(message, cause);
	}
}
