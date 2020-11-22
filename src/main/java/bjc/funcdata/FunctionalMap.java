package bjc.funcdata;

import java.util.*;
import java.util.function.*;

import bjc.data.*;

/**
 * Basic implementation of {@link IMap}
 *
 * @author ben
 *
 * @param <KeyType>
 *                    The type of the map's keys.
 *
 * @param <ValueType>
 *                    The type of the map's values.
 */
public class FunctionalMap<KeyType, ValueType> implements IMap<KeyType, ValueType> {
	/* Our backing store. */
	private Map<KeyType, ValueType> wrappedMap;

	private boolean isFrozen    = false;
	private boolean thawEnabled = true;
	
	/** Create a new blank functional map */
	public FunctionalMap() {
		wrappedMap = new HashMap<>();
	}

	/**
	 * Create a new functional map with the specified entries.
	 *
	 * @param entries
	 *                The entries to put into the map.
	 */
	@SafeVarargs
	public FunctionalMap(final IPair<KeyType, ValueType>... entries) {
		this();

		for (final IPair<KeyType, ValueType> entry : entries) {
			entry.doWith(wrappedMap::put);
		}
	}

	/**
	 * Create a new functional map wrapping the specified map.
	 *
	 * @param wrap
	 *             The map to wrap.
	 */
	public FunctionalMap(final Map<KeyType, ValueType> wrap) {
		if (wrap == null) throw new NullPointerException("Map to wrap must not be null");

		wrappedMap = wrap;
	}

	@Override
	public void clear() {
		if (isFrozen) throw new ObjectFrozen("Can't clear frozen map");
		
		wrappedMap.clear();
	}

	@Override
	public boolean containsKey(final KeyType key) {
		return wrappedMap.containsKey(key);
	}

	@Override
	public void forEach(final BiConsumer<KeyType, ValueType> action) {
		wrappedMap.forEach(action);
	}

	@Override
	public Optional<ValueType> get(final KeyType key) {
		if (key == null) throw new NullPointerException("Key must not be null");

		if (wrappedMap.containsKey(key)) {
			return Optional.of(wrappedMap.get(key));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public int size() {
		return wrappedMap.size();
	}

	@Override
	public IList<KeyType> keyList() {
		final FunctionalList<KeyType> keys = new FunctionalList<>();

		wrappedMap.keySet().forEach(keys::add);

		return keys;
	}

	@Override
	public ValueType put(final KeyType key, final ValueType val) {
		if (isFrozen)    throw new ObjectFrozen("Can't put key " + key + " into frozen map");
		if (key == null) throw new NullPointerException("Key must not be null");

		return wrappedMap.put(key, val);
	}

	@Override
	public ValueType remove(final KeyType key) {
		if (isFrozen) throw new ObjectFrozen("Can't remove key " + key + " from frozen map");
		
		return wrappedMap.remove(key);
	}

	@Override
	public String toString() {
		return wrappedMap.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (wrappedMap == null ? 0 : wrappedMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                     return true;
		if (obj == null)                     return false;
		if (!(obj instanceof FunctionalMap)) return false;

		final FunctionalMap<?, ?> other = (FunctionalMap<?, ?>) obj;

		if (wrappedMap == null) {
			if (other.wrappedMap != null) return false;
		} else if (!wrappedMap.equals(other.wrappedMap)) {
			return false;
		}
		
		return true;
	}

	// IFreezable support
	@Override
	public boolean freeze() {
		isFrozen = true;
		
		return true;
	}

	@Override
	public boolean thaw() {
		if (thawEnabled) {
			isFrozen = false;
			return true;			
		} else {
			return false;
		}
	}

	@Override
	public boolean deepFreeze() {
		thawEnabled = false;
		
		return freeze();
	}
	
	@Override
	public boolean canFreeze() {
		return true;
	}
	
	@Override
	public boolean canThaw() {
		return thawEnabled;
	}
	
	@Override
	public boolean isFrozen() {
		return isFrozen;
	}
}
