/* 
 * esodata - data structures and other things, of varying utility
 * Copyright 2022, Ben Culkin
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *   
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package bjc.esodata;

import java.util.*;

import bjc.data.Pair;

/**
 * A map that supports multiple values for a given key.
 * 
 * The thing that will tend to vary with the implementation is what they do with
 * duplicate values for the same key.
 * 
 * @author bjcul
 *
 * @param <KeyType> The key type of the map
 * @param <ValueType> The value type of the map.
 */
public interface Multimap<KeyType, ValueType> {
	/**
	 * Add a key-value mapping to the map.
	 *
	 * @param key   The key to store the value under.
	 *
	 * @param value The value to store.
	 */
	void add(KeyType key, ValueType value);

	/**
	 * Delete a particular key-value mapping from the map.
	 *
	 * @param key   The key of the mapping to remove.
	 *
	 * @param value The value of the mapping to remove.
	 */
	void remove(KeyType key, ValueType value);

	/**
	 * Delete all of the values associated with a particular key.
	 *
	 * @param key The key to remove values for.
	 */
	void remove(KeyType key);

	/**
	 * Get a set containing all of the values that are recorded for that key.
	 *
	 * @param key The key to look up values for.
	 *
	 * @return A set containing all of the values that have been mapped to that key.
	 */
	Set<ValueType> get(KeyType key);

	/**
	 * Get the single value in the map, if there is one.
	 * 
	 * @param key The key to look up
	 * @return An optional containing the key if it is there once, or empty if it is
	 *         there either no or more than one times
	 */
	Optional<ValueType> getSingle(KeyType key);

	/**
	 * Check if there is at least one value mapped to the given key.
	 *
	 * @param key The key to check for mappings for.
	 *
	 * @return Whether or not there is at least one value mapped to the key.
	 */
	boolean contains(KeyType key);

	/**
	 * Check if there is at least one instance of a particular key-value mapping.
	 *
	 * @param key   The key to check for mappings for.
	 *
	 * @param value The value to check for mappings for.
	 *
	 * @return Whether or not there is at least one instance of the given key-value
	 *         mapping.
	 */
	boolean contains(KeyType key, ValueType value);

	Iterator<Pair<KeyType, ValueType>> iterator();

}