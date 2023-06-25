package bjc.esodata;

import java.util.*;
import java.util.function.Consumer;

/**
 * A default implementation of {@link Spooler} that handles spools in a FIFO manner
 * @author bjcul
 *
 * @param <T> The type contained in the spools.
 */
public class DefaultSpooler<T> implements Spooler<T> {
	private Queue<Spool<T>> spools;
	
	/**
	 * Create a new default spooler.
	 */
	public DefaultSpooler() {
		this.spools = new ArrayDeque<>();
	}
	
	@Override
	public void registerSpool(Spool<T> spool) {
		spools.add(spool);
	}

	@Override
	public Consumer<T> getInput() {
		Spool<T> spool = new DefaultSpool<>();
		registerSpool(spool);
		return spool.getInput();
	}

	@Override
	public Iterator<T> getOutput() {
		return spools.remove().getOutput();
	}

	@Override
	public Spool<T> getSpool() {
		return spools.remove();
	}
}
