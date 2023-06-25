package bjc.esodata;

/**
 * Exception thrown for attempting to do something with a closed spool that you
 * should not have.
 * 
 * @author bjcul
 *
 */
public class ClosedSpoolException extends RuntimeException {
	private static final long serialVersionUID = 2846671972910718257L;

	/**
	 * Create a new default closed spool exception
	 */
	public ClosedSpoolException() {
		super();
	}

	/**
	 * Create a new closed spool exception with a given message and cause.
	 * 
	 * @param message The message for the exception.
	 * @param cause   The cause of the exception.
	 */
	public ClosedSpoolException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new closed spool exception with a given message.
	 * 
	 * @param message The message for the exception
	 */
	public ClosedSpoolException(String message) {
		super(message);
	}

	/**
	 * Create a new closed spool exception with a given cause.
	 * 
	 * @param cause The cause for the exception.
	 */
	public ClosedSpoolException(Throwable cause) {
		super(cause);
	}

}
