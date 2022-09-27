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

public class KeyedList<Key, Val> implements Iterable<Val> {
	private List<Val> backing;
	private Map<Key, Integer> indices;

	private int currIdx = 0;

	public KeyedList() {
		backing = new ArrayList<>();
		indices = new HashMap<>();
	}

	/**
	 * Add a item to this list.
	 * 
	 * If an item already exists with the given key, the current one will not be
	 * added. Use <code>set</code> to handle that.
	 * 
	 * @param key The key for this item.
	 * @param val The value for this item
	 * 
	 * @return Whether or not this item was added to the list
	 */
	public boolean add(Key key, Val val) {
		// TODO: Determine if this is the desired behavior
		if (indices.containsKey(key))
			return false;

		backing.add(val);
		indices.put(key, currIdx++);
		return true;
	}

	/**
	 * Set the item associated with a given key.
	 * 
	 * @param key    The key to set
	 * @param newVal The new value for the key
	 * 
	 * @return The previous value for the key, if there was one
	 */
	public Val set(Key key, Val newVal) {
		if (indices.containsKey(key)) {
			return backing.set(indices.get(key), newVal);
		}

		add(key, newVal);
		return null;
	}

	/**
	 * Retrieve all of the keys for this list.
	 * 
	 * @return An immutable set of the keys for this list
	 */
	public Set<Key> keys() {
		// TODO: write mutable wrapper which will update the list appropriately
		return Collections.unmodifiableSet(indices.keySet());
	}

	/**
	 * Retrieve the value associated with the given key.
	 * 
	 * @param key The key to look up.
	 * 
	 * @return The value for the given key.
	 */
	public Val get(Key key) {
		return backing.get(indices.get(key));
	}

	/**
	 * Check if this list contains a value for a given key.
	 * 
	 * @param key The key to look up.
	 * 
	 * @return Whether this list contains a value for the given key.
	 */
	public boolean containsKey(Key key) {
		return indices.containsKey(key);
	}

	@Override
	public Iterator<Val> iterator() {
		return backing.iterator();
	}

	/**
	 * Return an iterator that starts at the value for the given key.'
	 * 
	 * @param key The key to start at.
	 * 
	 * @return An iterator starting at the given key, or null if the key isn't
	 *         present.
	 */
	public ListIterator<Val> iteratorFrom(Key key) {
		if (indices.containsKey(key)) {
			return backing.listIterator(indices.get(key));
		}

		return null;
	}
}
