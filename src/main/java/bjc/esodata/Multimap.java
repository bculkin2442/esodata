package bjc.esodata;

import java.util.*;

import bjc.data.*;

/**
 * A map that has support for multiple values for a given key.
 *
 * Whenever you give another value for a key, that is then returned for that key. About the only
 * somewhat complex thing, is that, if you add the same key-value pair multiple times, it will only
 * show up once. However, you will have to remove that pair as many times as you added it.
 *
 * @author Ben Culkin
 */
public class Multimap<KeyType, ValueType> {
	private Map<KeyType, ThresholdSet<ValueType>> backing;

	/**
	 * Create a new empty multimap.
	 */
	public Multimap() {
		backing = new HashMap<>();
	}

	/**
	 * Add a key-value mapping to the map.
	 *
	 * @param key
	 * 	The key to store the value under.
	 *
	 * @param value
	 * 	The value to store.
	 */
	public void add(KeyType key, ValueType value) {
		ThresholdSet<ValueType> container = backing.computeIfAbsent(key,
				(k) -> new ThresholdSet());

		container.add(value);
	}

	/**
	 * Delete a particular key-value mapping from the map.
	 *
	 * @param key
	 * 	The key of the mapping to remove.
	 *
	 * @param value
	 * 	The value of the mapping to remove.
	 */
	public void remove(KeyType key, ValueType value) {
		// We have no values for that key; bail.
		if (!backing.containsKey(key)) return;

		backing.get(key).remove(value);
	}

	/**
	 * Delete all of the values associated with a particular key.
	 *
	 * @param key
	 * 	The key to remove values for.
	 */
	public void remove(KeyType key) {
		backing.remove(key);
	}

	/**
	 * Get a set containing all of the values that are recorded for that key.
	 *
	 * @param key
	 * 	The key to look up values for.
	 *
	 * @return A set containing all of the values that have been mapped to that key.
	 */
	public Set<ValueType> get(KeyType key) {
		if (!backing.containsKey(key)) return new HashSet<>();

		return backing.get(key).values();
	}

	/**
	 * Check if there is at least one value mapped to the given key.
	 *
	 * @param key
	 * 	The key to check for mappings for.
	 * 
	 * @return Whether or not there is at least one value mapped to the key.
	 */
	public boolean contains(KeyType key) {
		return backing.containsKey(key);
	}

	/**
	 * Check if there is at least one instance of a particular key-value mapping.
	 *
	 * @param key
	 * 	The key to check for mappings for.
	 *
	 * @param value
	 * 	The value to check for mappings for.
	 * 
	 * @return Whether or not there is at least one instance of the given key-value mapping.
	 */
	public boolean contains(KeyType key, ValueType value) {
		if (!backing.containsKey(key)) return false;

		return backing.get(key).contains(value) > 0;
	}
}
