package bjc.functypes;

import java.util.*;
import java.util.function.Function;

import bjc.funcdata.Freezable;
import bjc.funcdata.ObjectFrozen;

/**
 * Functional interface for a generic builder using a map
 * 
 * @author bjcul
 *
 * @param <K> Key type of the map
 * @param <V> Value type of the map
 * @param <R> Result type of the builder
 */
public interface MapBuilder<K, V, R> extends Freezable<MapBuilder<K, V, R>> {
	/**
	 * Build an instance using the current map
	 * 
	 * @return An instance constructed using the map
	 */
	R build();
	
	/**
	 * Add a value to the map
	 * 
	 * @param key The key to add
	 * @param value The value to add
	 * 
	 * @return The builder, for chaining
	 */
	MapBuilder<K, V, R> add(K key, V value);
	
	/**
	 * Remove a value from the map
	 * 
	 * @param key The key to remove
	 * 
	 * @return The removed value
	 */
	V remove(K key);
	
	/**
	 * Clear the contents of the map
	 */
	void clear();
	
	/**
	 * Get the internal map.
	 * 
	 * @return The internal map
	 */
	Map<K, V> getMap();
	
	/**
	 * Create a new map-builder from a function
	 * 
	 * @param <K> The key type of the map
	 * @param <V> The value type of the map
	 * @param <R> The result of the builder
	 * 
	 * @param f The building function
	 * 
	 * @return A map-builder using the function
	 */
	static <K, V, R> MapBuilder<K, V, R> from(Function<Map<K, V>, R> f) {
		return new FunctionalMapBuilder<>(f);
	}
}

final class FunctionalMapBuilder<K, V, R> implements MapBuilder<K, V, R> {
	private final Function<Map<K, V>, R> f;
	private Map<K, V> mep = new HashMap<>();

	private boolean frozen = false;
	
	FunctionalMapBuilder(Function<Map<K, V>, R> f) {
		this.f = f;
	}

	@Override
	public R build() {
		return f.apply(mep);
	}

	@Override
	public MapBuilder<K, V, R> add(K key, V value) {
		if (frozen) throw new ObjectFrozen();
		
		mep.put(key, value);
		return this;
	}

	@Override
	public V remove(K key) {
		if (frozen) throw new ObjectFrozen();
		
		return mep.remove(key);
	}

	@Override
	public void clear() {
		if (frozen) throw new ObjectFrozen();
		
		mep.clear();
	}

	@Override
	public Map<K, V> getMap() {
		if (frozen) return Collections.unmodifiableMap(mep);
		return mep;
	}

	@Override
	public boolean freeze() {
		frozen = true;
		return true;
	}

	@Override
	public boolean thaw() {
		frozen = false;
		return true;
	}

	@Override
	public boolean isFrozen() {
		return frozen;
	}
}