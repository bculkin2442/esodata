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
 * Represents a map keyed by pairs, where you can look up all the items that
 * have a particular left or right key value.
 * 
 * NOTE: Using the keySet/values/entrySet views to modify the map will break the
 * tracking invariants.
 * 
 * @author bjcul
 *
 * @param <Left>
 * @param <Right>
 * @param <Value>
 */
public class PairMap<Left, Right, Value> implements Map<Pair<Left, Right>, Value> {
	private Map<Pair<Left, Right>, Value> backing;

	private Multimap<Left, Pair<Left, Right>> leftTracker;
	private Multimap<Right, Pair<Left, Right>> rightTracker;

	/**
	 * Create a new pair map.
	 */
	public PairMap() {
		this.backing = new HashMap<>();
		
		this.leftTracker = new TSetMultimap<>();
		this.rightTracker = new TSetMultimap<>();
	}
	
	/**
	 * Get all of the key-pairs which contain the given left value.
	 * 
	 * @param val The value to search for.
	 * 
	 * @return All of the key-pairs containing the given value.
	 */
	public Set<Pair<Left, Right>> getLeft(Left val) {
		return leftTracker.get(val);
	}
	
	/**
	 * Get all of the key-pairs which contain the given right value.
	 * 
	 * @param val The value to search for.
	 * 
	 * @return All of the key-pairs containing the given value.
	 */
	public Set<Pair<Left, Right>> getRight(Right val) {
		return rightTracker.get(val);
	}
	
	/**
	 * Get a value without having to construct a pair callee-side
	 * 
	 * @param lft The left value
	 * @param rght The right value.
	 * 
	 * @return The value corresponding to the given key-pair, if one exists
	 */
	public Value biget(Left lft, Right rght) {
		return backing.get(Pair.pair(lft, rght));
	}
	
	@Override
	public int size() {
		return backing.size();
	}

	@Override
	public boolean isEmpty() {
		return backing.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return backing.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return backing.containsKey(value);
	}

	@Override
	public Value get(Object key) {
		return backing.get(key);
	}

	@Override
	public Value put(Pair<Left, Right> key, Value value) {
		Value ret = backing.put(key, value);
		leftTracker.add(key.getLeft(), key);
		rightTracker.add(key.getRight(), key);
		return ret;
	}

	@Override
	public Value remove(Object key) {
		@SuppressWarnings("unchecked")
		Pair<Left, Right> actKey = (Pair<Left, Right>) key;

		leftTracker.remove(actKey.getLeft(), actKey);
		rightTracker.remove(actKey.getRight(), actKey);

		return backing.remove(key);
	}

	@Override
	public void putAll(Map<? extends Pair<Left, Right>, ? extends Value> m) {
		for (Entry<? extends Pair<Left, Right>, ? extends Value> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		backing.clear();

		leftTracker = new TSetMultimap<>();
		rightTracker = new TSetMultimap<>();
	}

	// TODO: Update these to not break the tracking invariants
	@Override
	public Set<Pair<Left, Right>> keySet() {
		return backing.keySet();
	}

	@Override
	public Collection<Value> values() {
		return backing.values();
	}

	@Override
	public Set<Entry<Pair<Left, Right>, Value>> entrySet() {
		return backing.entrySet();
	}

}
