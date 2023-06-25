package bjc.esodata;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * A single spool, or batch of input.
 * 
 * @author bjcul
 * 
 * @param <T> The type contained by the spool
 */
public interface Spool<T> {
	/**
	 * Get the label that identifies this spool, if it has one.
	 * 
	 * @return The label of the spool, or null if it is unlabeled.
	 */
	public String label();

	/**
	 * Get the group that this spool belongs to, if it belongs to one.
	 * 
	 * @return The group the spool is in, or null if it is not in a group.
	 */
	public String group();

	/**
	 * Get a function that will add input to this spool
	 * 
	 * @return A function that will add input to this spool.
	 */
	public Consumer<T> getInput();

	/**
	 * Get an iterator that will iterate over the data contained in this spool.
	 * 
	 * @return An iterator over the data in this spool.
	 */
	public Iterator<T> getOutput();

	/**
	 * Is this spool closed, meaning it can accept no more input?
	 * 
	 * @return Whether the spool is closed
	 */
	public boolean isClosed();
}