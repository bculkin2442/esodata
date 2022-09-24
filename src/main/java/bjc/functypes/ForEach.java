package bjc.functypes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.function.BiFunction;

/**
 * Parallel forEach
 * 
 * @author bjcul
 *
 * @param <Output> The type output by each iteration
 * @param <Index>  The type indexing each iteration
 * @param <Input>  The type fed to each iteration
 */
public abstract class ForEach<Output, Index, Input> {
	protected static final Exception CONTINUE = new Exception("forEach Continue");
	// we don't actually care about the stack trace
	static {
		CONTINUE.setStackTrace(new StackTraceElement[0]);
	};

	/**
	 * Run a single instance of the loop
	 * 
	 * Throw CONTINUE to cancel the instance without yielding a value
	 * 
	 * @param idx The index for the loop
	 * @param in  The value to process for the loop
	 * @return The output of the loop
	 * 
	 * @throws Exception If something goes wrong in the loop.
	 */
	public abstract Output block(Index idx, Input in) throws Exception;

	Callable<Output> loop(Index idx, Input in) {
		return () -> block(idx, in);
	}

	/**
	 * Create a ForEach from a function
	 * 
	 * @param <Output> The type output by each iteration
	 * @param <Index>  The type indexing each iteration
	 * @param <Input>  The type fed to each iteration
	 * 
	 * @param func     The function for an iteration
	 * 
	 * @return A ForEach backed by the given function
	 */
	public static <Output, Index, Input> ForEach<Output, Index, Input> from(BiFunction<Index, Input, Output> func) {
		return new FunctionalForEach<>(func);
	}

	/**
	 * Execute a parallel forEach.
	 * 
	 * @param <Output> The type output by each iteration
	 * @param <Index>  The type indexing each iteration
	 * @param <Input>  The type fed to each iteration
	 * 
	 * @param pool     The concurrent executor
	 * @param in       The collection to iterate over
	 * @param block    The block to run an iteration
	 * 
	 * @return A map representing the results of each iteration.
	 */
	public static <Output, Index, Input> LinkedHashMap<Index, Output> forEach(ExecutorService pool,
			Map<Index, Input> in, ForEach<Output, Index, Input> block) {
		int size = in.size();

		final Map<Index, Future<Output>> futures = new LinkedHashMap<>(size);
		for (Entry<Index, Input> entry : in.entrySet()) {
			Index key = entry.getKey();
			futures.put(key, pool.submit(block.loop(key, entry.getValue())));
		}
		final LinkedHashMap<Index, Output> results = new LinkedHashMap<>(size);
		for (Entry<Index, Future<Output>> entry : futures.entrySet()) {
			Index key = entry.getKey();
			try {
				results.put(key, entry.getValue().get());
			} catch (ExecutionException eex) {
				Throwable cause = eex.getCause();
				if (cause != CONTINUE) {
					// CONTINUE is used to exit a given loop, so ignore it
					futures.values().forEach((f) -> f.cancel(true));
					throw new IllegalStateException(
							"Exception thrown evaluating forEach index " + key + " of value " + in.get(key), cause);
				}
			} catch (CancellationException cex) {
				// Ignore these
			} catch (InterruptedException iex) {
				// Carryover interrupt
				Thread.currentThread().interrupt();
			}
		}

		return results;
	}
}

final class FunctionalForEach<Output, Index, Input> extends ForEach<Output, Index, Input> {
	private final BiFunction<Index, Input, Output> func;

	FunctionalForEach(BiFunction<Index, Input, Output> func) {
		this.func = func;
	}

	@Override
	public Output block(Index idx, Input in) throws Exception {
		return func.apply(idx, in);
	}
}
