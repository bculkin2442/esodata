package bjc.esodata;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

/**
 * A default implementation of {@link Spool} using a {@link LinkedBlockingDeque}
 * as the backing storage.
 * 
 * @author bjcul
 *
 * @param <T> The type contained in the spool
 */
public class DefaultSpool<T> implements Spool<T>, AutoCloseable {
	private String label;
	private String group;

	private BlockingQueue<T> container;

	private boolean closed;

	/**
	 * Create a new spool without label or group
	 */
	public DefaultSpool() {
		this(null, null);
	}

	/**
	 * Create a new spool with a given label and group.
	 * 
	 * @param label The label of the spool
	 * @param group The group for the spool
	 */
	public DefaultSpool(String label, String group) {
		this.label = label;
		this.group = group;

		this.container = new LinkedBlockingDeque<>();
		this.closed = false;
	}

	@Override
	public String label() {
		return label;
	}

	@Override
	public String group() {
		return group;
	}

	@Override
	public Consumer<T> getInput() {
		return (val) -> {
			if (closed) {
				throw new ClosedSpoolException();
			}
			container.add(val);
		};
	}

	@Override
	public Iterator<T> getOutput() {
		return container.iterator();
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public void close() throws Exception {
		closed = true;
	}

}
