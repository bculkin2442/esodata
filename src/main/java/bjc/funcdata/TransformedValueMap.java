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
package bjc.funcdata;

import java.util.*;
import java.util.function.*;

/**
 * A map that transforms values from one type to another
 *
 * @author ben
 *
 * @param <OldKey>
 *                   The type of the map's keys
 *
 * @param <OldValue>
 *                   The type of the map's values
 *
 * @param <NewValue>
 *                   The type of the transformed values
 *
 */
final class TransformedValueMap<OldKey, OldValue, NewValue>
		implements MapEx<OldKey, NewValue> {
	/* Our backing map. */
	private final MapEx<OldKey, OldValue> backing;
	/* Our transforming function. */
	private final Function<OldValue, NewValue> transformer;

	private boolean isFrozen    = false;
	private boolean thawEnabled = true;
	
	/**
	 * Create a new transformed-value loop.
	 *
	 * @param backingMap
	 *                   The map to use as backing.
	 *
	 * @param transform
	 *                   The function to use for the transform.
	 */
	public TransformedValueMap(final MapEx<OldKey, OldValue> backingMap,
			final Function<OldValue, NewValue> transform) {
		backing = backingMap;
		transformer = transform;
	}

	@Override
	public void clear() {
		if (isFrozen) throw new ObjectFrozen("Can't clear frozen map");
		
		backing.clear();
	}

	@Override
	public boolean containsKey(final OldKey key) {
		return backing.containsKey(key);
	}

	@Override
	public void forEach(final BiConsumer<OldKey, NewValue> action) {
		backing.forEach((key, value) -> {
			action.accept(key, transformer.apply(value));
		});
	}

	@Override
	public Optional<NewValue> get(final OldKey key) {
		return backing.get(key).map(transformer);
	}

	@Override
	public int size() {
		return backing.size();
	}

	@Override
	public ListEx<OldKey> keyList() {
		return backing.keyList();
	}

	@Override
	public NewValue put(final OldKey key, final NewValue value) {
		throw new UnsupportedOperationException("Can't add items to transformed map");
	}

	@Override
	public NewValue remove(final OldKey key) {
		if (isFrozen) throw new ObjectFrozen("Can't remove key " + key + " from frozen map");
		
		return transformer.apply(backing.remove(key));
	}

	@Override
	public String toString() {
		return backing.toString();
	}
	
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
		}
		
		return false;
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