package bjc.esodata;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Represents a 'spool' facility for gathering batches of input together to be
 * processed.
 * 
 * @author bjcul
 *
 * @param <T> The type of data held in the spools
 */
public interface Spooler<T> {
	/**
	 * Register a pre-existing spool with this spooler.
	 * 
	 * @param spool The spool to register
	 */
	public void registerSpool(Spool<T> spool);
	
	/**
	 * Get a consumer belonging to a new anonymous spool.
	 * 
	 * @return A new anonymous spool.
	 */
	public Consumer<T> getInput();
	
	/**
	 * Get the output for a unprocessed spool.
	 * 
	 * @return The output for a unprocessed spool.
	 */
	public Iterator<T> getOutput();
	
	/**
	 * Get an unprocessed spool.
	 * 
	 * @return An unprocessed spool
	 */
	public Spool<T> getSpool();
}
