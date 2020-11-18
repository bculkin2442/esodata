package bjc.funcdata;

import java.util.function.*;

/**
 * Functional wrapper over map providing some useful things.
 *
 * @author ben
 *
 * @param <KeyType>
 *                    The type of this map's keys.
 *
 * @param <ValueType>
 *                    The type of this map's values.
 */
public interface IMap<KeyType, ValueType> extends IFreezable {
	/**
	 * Execute an action for each entry in the map.
	 *
	 * @param action
	 *               The action to execute for each entry in the map.
	 */
	void forEach(BiConsumer<KeyType, ValueType> action);

	/**
	 * Perform an action for each key in the map.
	 *
	 * @param action
	 *               The action to perform on each key in the map.
	 */
	default void forEachKey(final Consumer<KeyType> action) {
		forEach((key, val) -> action.accept(key));
	}

	/**
	 * Perform an action for each value in the map.
	 *
	 * @param action
	 *               The action to perform on each value in the map.
	 */
	default void forEachValue(final Consumer<ValueType> action) {
		forEach((key, val) -> action.accept(val));
	}

	/**
	 * Check if this map contains the specified key.
	 *
	 * @param key
	 *            The key to check.
	 *
	 * @return Whether or not the map contains the key.
	 */
	boolean containsKey(KeyType key);

	/**
	 * Get the value assigned to the given key.
	 *
	 * @param key
	 *            The key to look for a value under.
	 *
	 * @return The value of the key.
	 */
	ValueType get(KeyType key);

	/**
	 * Get a value from the map, and return a default value if the key doesn't
	 * exist.
	 *
	 * @param key
	 *                     The key to attempt to retrieve.
	 *
	 * @param defaultValue
	 *                     The value to return if the key doesn't exist.
	 *
	 * @return The value associated with the key, or the default value if the key
	 *         doesn't exist.
	 */
	default ValueType getOrDefault(final KeyType key, final ValueType defaultValue) {
		try {
			return get(key);
		} catch (final IllegalArgumentException iaex) {
			/*
			 * We don't care about this, because it indicates a key is missing.
			 */
			return defaultValue;
		}
	}

	/**
	 * Add an entry to the map.
	 *
	 * @param key
	 *            The key to put the value under.
	 *
	 * @param val
	 *            The value to add.
	 *
	 * @return The previous value of the key in the map, or null if the key wasn't
	 *         in the map. However, note that it may also return null if the key was
	 *         set to null.
	 *
	 * @throws UnsupportedOperationException
	 *                                       If the map implementation doesn't
	 *                                       support modifying the map.
	 */
	ValueType put(KeyType key, ValueType val);

	/** Delete all the values in the map. */
	default void clear() {
		if (isFrozen()) throw new ObjectFrozen("Can't clear a frozen map");
		
		keyList().forEach(IMap.this::remove);
	}

	/**
	 * Get the number of entries in this map.
	 *
	 * @return The number of entries in this map.
	 */
	default int size() {
		return keyList().getSize();
	}

	/*
	 * @NOTE Do we want this to be the semantics for transform, or do we want to go
	 * to semantics using something like Isomorphism, or doing a one-time bulk
	 * conversion of the values?
	 */
	/**
	 * Transform the values returned by this map.
	 *
	 * NOTE: This transform is applied once for each lookup of a value, so the
	 * transform passed should be a proper function, or things will likely not work
	 * as expected.
	 *
	 * @param <V2>
	 *                    The new type of returned values.
	 *
	 * @param transformer
	 *                    The function to use to transform values.
	 *
	 * @return The map where each value will be transformed after lookup.
	 */
	default <V2> IMap<KeyType, V2> transform(final Function<ValueType, V2> transformer) {
		return new TransformedValueMap<>(this, transformer);
	}

	/**
	 * Extends this map, creating a new map that will delegate queries to the map,
	 * but store any added values itself.
	 *
	 * @return An extended map.
	 */
	default IMap<KeyType, ValueType> extend() {
	   return extend(new FunctionalMap<>());
	};

	/**
   * Extends this map, creating a new map that will delegate queries to this map,
   * but store any added values in the provided map.
   *
   * @param backer The map to store added values in.
   * 
   * @return An extended map.
   */
	default IMap<KeyType, ValueType> extend(IMap<KeyType, ValueType> backer) {
    return new ExtendedMap<>(this, backer);
 };

	/**
	 * Remove the value bound to the key.
	 *
	 * @param key
	 *            The key to remove from the map.
	 *
	 * @return The previous value for the key in the map, or null if the key wasn't
	 *         in the class. NOTE: Just because you received null, doesn't mean the
	 *         map wasn't changed. It may mean that someone put a null value for
	 *         that key into the map.
	 */
	ValueType remove(KeyType key);

	/**
	 * Get a list of all the keys in this map.
	 *
	 * @return A list of all the keys in this map.
	 */
	IList<KeyType> keyList();

	/**
	 * Get a list of the values in this map.
	 *
	 * @return A list of values in this map.
	 */
	default IList<ValueType> valueList() {
		final IList<ValueType> returns = new FunctionalList<>();

		for (final KeyType key : keyList()) {
			returns.add(get(key));
		}

		return returns;
	}
	
	/**
	 * Static method to create a basic instance of IMap.
	 * 
	 * @param <KeyType2> The key type of the map.
	 * @param <ValueType2> The value type of the map.
	 * 
	 * @param args A series of key-value pairs. You will get an error if you don't
	 *             provide the correct number of arguments (a number divisible by 2);
	 *             however, if you pass arguments of the wrong type, you will not
	 *             get a compile error, and usually won't get a runtime error in
	 *             construction. However, you will get a ClassCastException as soon
	 *             as you do something that attempts to iterate over the keys or values
	 *             of the map.
	 * 
	 * @return A map, constructed from the provided values.
	 * 
	 * @throws IllegalArgumentException If you provide an incomplete pair of arguments.
	 */
	@SuppressWarnings("unchecked")
	static <KeyType2, ValueType2> IMap<KeyType2, ValueType2> of(Object... args) {
		if (args.length % 2 != 0) throw new IllegalArgumentException("Args must be in the form of key-value pairs");
		
		IMap<KeyType2, ValueType2> map = new FunctionalMap<>();
		for (int index = 0; index < args.length; index += 2) {
			KeyType2   key   = (KeyType2)   args[index];
			ValueType2 value = (ValueType2) args[index + 1];
			
			map.put(key, value);
		}
		
		return map;
	}
}
