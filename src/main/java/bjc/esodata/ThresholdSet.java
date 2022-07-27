package bjc.esodata;

import java.util.*;

import bjc.data.Pair;
import bjc.data.SimplePair;
import bjc.data.TransformIterator;

/**
 * Represents a counted set, that overflows to a map.
 *
 * More specifically, this is a set/map combo type.
 *
 * Initially, when you add an item, it will go into the set. Attempting to add a
 * duplicate item to that set will cause the entry to be removed from the set,
 * and added to the map, which will count the number of times that particular
 * item has been added to the set. If you remove enough copies of that item to
 * put it back down to 1 copy, that copy will be removed from the map, and
 * readded to the set.
 *
 * The iterator that this type gives by default is an iterator over all of the
 * values in the set, not including any of those in the map.
 *
 * @param <KeyType> The value being counted.
 *
 * @author Ben Culkin
 */
public class ThresholdSet<KeyType> implements Iterable<Pair<KeyType, Integer>> {
	// View of this class as a java.util.Set
	private class SetView extends AbstractSet<KeyType> {
		/*
		 * This is technically not a valid implementation of add, because it does not
		 * guarantee that the set will contain key after it returns (as a matter of
		 * fact, attempting to add the component might actually cause it to be removed
		 * from the collection).
		 */
		@Override
		public boolean add(KeyType key) {
			// Qualified-this; allows us to reference the 'this' of our enclosing type.
			int ret = ThresholdSet.this.add(key);

			// No change to set contents
			if (ret > 2)
				return false;

			return true;
		}

		@Override
		public boolean remove(Object o) {
			// Will throw a ClassCastException if you give us something bad.
			@SuppressWarnings("unchecked")
			KeyType k = (KeyType) o;

			int ret = ThresholdSet.this.remove(k);

			// We removed the element.
			if (ret == 0)
				return true;

			return false;
		}

		@Override
		public boolean contains(Object o) {
			// Will throw a ClassCastException if you give us something bad.
			@SuppressWarnings("unchecked")
			KeyType k = (KeyType) o;

			int ret = ThresholdSet.this.contains(k);

			// The object is set-visible
			if (ret == 1)
				return true;

			return false;
		}

		@Override
		public int size() {
			return ThresholdSet.this.setSize();
		}

		@Override
		public Iterator<KeyType> iterator() {
			return ThresholdSet.this.setIterator();
		}
	}

	// Set of uniquely stored keys
	private Set<KeyType> keySet;

	// @TODO :CountMap Ben Culkin 6/19/2019
	// Replace this with a CountSet or some equivalent concept, whenever that gets
	// written, if that
	// gets written
	private Map<KeyType, Integer> keyMap;

	/**
	 * Create a new empty threshold set.
	 */
	public ThresholdSet() {
		keySet = new HashSet<>();
		keyMap = new HashMap<>();
	}

	/**
	 * Add multiple keys at once to the map.
	 *
	 * @param keys The keys to add.
	 *
	 * @return An array containing the results of adding the keys.
	 */
	public int[] addKeys(@SuppressWarnings("unchecked") KeyType... keys) {
		int[] ret = new int[keys.length];

		for (int i = 0; i < keys.length; i++)
			ret[i] = add(keys[i]);

		return ret;
	}

	/**
	 * Add a key to the collection.
	 *
	 * @param key The key to add to the collection.
	 *
	 * @return The number of times that key now exists in the collection. Should
	 *         always be &gt; 0.
	 */
	public int add(KeyType key) {
		if (keySet.contains(key)) {
			// Promote to counted item
			keySet.remove(key);

			keyMap.put(key, 2);

			return 2;
		} else if (keyMap.containsKey(key)) {
			// Increment count
			int cnt = keyMap.get(key) + 1;

			keyMap.put(key, cnt);

			return cnt;
		} else {
			// New key
			keySet.add(key);

			return 1;
		}
	}

	/**
	 * Remove a bunch of keys from the collection.
	 *
	 * @param keys The keys to remove from the collection.
	 *
	 * @return The results from removing the keys.
	 */
	public int[] removeKeys(@SuppressWarnings("unchecked") KeyType... keys) {
		int[] ret = new int[keys.length];

		for (int i = 0; i < keys.length; i++)
			ret[i] = remove(keys[i]);

		return ret;
	}

	/**
	 * Remove a key from the collection.
	 *
	 * @param key The key to remove from the collection.
	 *
	 * @return The number of times that key now exists in the collection. Returns -1
	 *         if that key wasn't in the collection beforehand.
	 */
	public int remove(KeyType key) {
		if (keySet.contains(key)) {
			// No more occurrences
			keySet.remove(key);

			return 0;
		} else if (keyMap.containsKey(key)) {
			// Decrement count
			int cnt = keyMap.get(key) - 1;

			if (cnt == 1) {
				// Move key to set
				keyMap.remove(key);

				keySet.add(key);

				return 1;
			}

			keyMap.put(key, cnt);

			return cnt;
		} else {
			// We don't know about that key
			return -1;
		}
	}

	/**
	 * Get the number of times the set contains a set of given keys.
	 *
	 * @param keys The keys to look for.
	 *
	 * @return The containment counts for each key.
	 */
	public int[] containsKeys(@SuppressWarnings("unchecked") KeyType... keys) {
		int[] ret = new int[keys.length];

		for (int i = 0; i < keys.length; i++)
			ret[i] = contains(keys[i]);

		return ret;
	}

	/**
	 * Get the number of times the set contains a given key.
	 *
	 * @param key The key to look for.
	 *
	 * @return The number of times the key occurs; -1 if it doesn't occur.
	 */
	public int contains(KeyType key) {
		if (keySet.contains(key))
			return 1;
		if (!keyMap.containsKey(key))
			return -1;

		return keyMap.get(key);
	}

	/**
	 * Get a set containing all of the values that are in this set at least once.
	 *
	 * @return A set containing every value that occurs at least once in this set.
	 */
	public Set<KeyType> values() {
		Set<KeyType> retSet = new HashSet<>();

		retSet.addAll(keySet);
		retSet.addAll(keyMap.keySet());

		return retSet;
	}

	/**
	 * Get a view of this collection as a java.util.Set.
	 *
	 * @return A view of the collection as a set.
	 */
	public Set<KeyType> setView() {
		return new SetView();
	}

	@Override
	public Iterator<Pair<KeyType, Integer>> iterator() {
		return new TransformIterator<>(keyMap.entrySet().iterator(),
				(entry) -> new SimplePair<>(entry.getKey(), entry.getValue()));
	}

	/**
	 * Static threshold set constructor.
	 * 
	 * @param <KType> The type of keys for the threshold set.
	 *
	 * @param keys    The initial keys to add to the threshold set.
	 * @return A threshold set with the given keys.
	 */
	@SafeVarargs
	public static <KType> ThresholdSet<KType> TS(KType... keys) {
		ThresholdSet<KType> ts = new ThresholdSet<>();

		ts.addKeys(keys);

		return ts;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Set: ");
		sb.append(keySet);
		sb.append("\nMap: ");
		sb.append(keyMap);

		return sb.toString();
	}

	// Implementation methods for setView

	int setSize() {
		return keySet.size();
	}

	Iterator<KeyType> setIterator() {
		return keySet.iterator();
	}
}
