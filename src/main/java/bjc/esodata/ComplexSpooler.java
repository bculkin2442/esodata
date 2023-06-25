package bjc.esodata;

import java.util.*;
import java.util.function.Consumer;

/**
 * Implementation of {@link Spooler} which supports a number of additional features.
 * 
 * @author bjcul
 *
 * @param <T> The type of data contained on the spools
 */
public class ComplexSpooler<T> implements Spooler<T> {
	private Queue<Spool<T>> pendingAnonSpools;
	private Map<String, Queue<Spool<T>>> pendingGroupedSpools;
	
	private Map<String, Spool<T>> labelledSpools;
	
	@Override
	public void registerSpool(Spool<T> spool) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Consumer<T> getInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Spool<T> getSpool() {
		// TODO Auto-generated method stub
		return null;
	}

}
