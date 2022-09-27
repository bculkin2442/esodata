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
import java.util.Map.Entry;

import bjc.data.Pair;

/**
 * A map that has support for multiple values for a given key.
 *
 * Whenever you give another value for a key, that is then returned for that
 * key. About the only somewhat complex thing, is that, if you add the same
 * key-value pair multiple times, it will only show up once. However, you will
 * have to remove that pair as many times as you added it.
 *
 * @author Ben Culkin
 * 
 * @param <KeyType> The type of keys in the map.
 * @param <ValueType> The type of values in the map.
 */
public class TSetMultimap<KeyType, ValueType> implements Iterable<Pair<KeyType, ValueType>>, Multimap<KeyType, ValueType> {
	private final class SetMultimapIterator implements Iterator<Pair<KeyType, ValueType>> {
		private Iterator<Entry<KeyType, ThresholdSet<ValueType>>> mapIter = backing.entrySet().iterator();
		private KeyType currKey;
		private Iterator<ValueType> setIter;

		@Override
		public boolean hasNext() {
			while (setIter == null || !setIter.hasNext()) {
				if (!mapIter.hasNext()) return false;
				Entry<KeyType,ThresholdSet<ValueType>> entry = mapIter.next();
				
				currKey = entry.getKey();
				setIter = entry.getValue().setView().iterator();
			}
			
			return setIter.hasNext();
		}

		@Override
		public Pair<KeyType, ValueType> next() {
			if (setIter == null || !setIter.hasNext()) throw new NoSuchElementException();
			return Pair.pair(currKey, setIter.next()) ;
		}
	}

	private Map<KeyType, ThresholdSet<ValueType>> backing;

	/**
	 * Create a new empty multimap.
	 */
	public TSetMultimap() {
		backing = new HashMap<>();
	}

	@Override
	public void add(KeyType key, ValueType value) {
		ThresholdSet<ValueType> container
				= backing.computeIfAbsent(key, k -> new ThresholdSet<>());

		container.add(value);
	}

	@Override
	public void remove(KeyType key, ValueType value) {
		// We have no values for that key; bail.
		if (!backing.containsKey(key)) return;
		
		backing.get(key).remove(value);
	}

	@Override
	public void remove(KeyType key) {
		backing.remove(key);
	}

	@Override
	public Set<ValueType> get(KeyType key) {
		if (!backing.containsKey(key)) return new HashSet<>();
		else                           return backing.get(key).values();
	}

	@Override
	public Optional<ValueType> getSingle(KeyType key) {
		Set<ValueType> set = get(key);
		
		if (set.size() == 1) return Optional.of(set.iterator().next());
		return Optional.empty();
	}

	@Override
	public boolean contains(KeyType key) {
		return backing.containsKey(key);
	}

	@Override
	public boolean contains(KeyType key, ValueType value) {
		if (!backing.containsKey(key)) return false;

		return backing.get(key).contains(value) > 0;
	}
	
	@Override
	public Iterator<Pair<KeyType, ValueType>> iterator() {
		return new SetMultimapIterator();
	}
}
