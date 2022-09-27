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

import bjc.funcdata.FunctionalMap;
import bjc.funcdata.MapEx;

/**
 * Simple implementation of {@link Directory}.
 *
 * Has a unified namespace for data and children.
 *
 * @author EVE
 *
 * @param <K>
 *            The key type of the directory.
 *
 * @param <V>
 *            The value type of the directory.
 */
public class UnifiedDirectory<K, V> implements Directory<K, V> {
	/* Our directory children. */
	private final MapEx<K, Directory<K, V>> children;
	/* Our data children. */
	private final MapEx<K, V> data;

	/** Create a new directory. */
	public UnifiedDirectory() {
		children = new FunctionalMap<>();
		data = new FunctionalMap<>();
	}

	@Override
	public Directory<K, V> getSubdirectory(final K key) {
		return children.get(key).orElse(null);
	}

	@Override
	public boolean hasSubdirectory(final K key) {
		return children.containsKey(key);
	}

	@Override
	public Directory<K, V> putSubdirectory(final K key, final Directory<K, V> val) {
		if (data.containsKey(key)) {
			final String msg = String.format("Key %s is already used for data", key);

			throw new IllegalArgumentException(msg);
		}

		return children.put(key, val);
	}

	@Override
	public boolean containsKey(final K key) {
		return data.containsKey(key);
	}

	@Override
	public V getKey(final K key) {
		return data.get(key).orElse(null);
	}

	@Override
	public V putKey(final K key, final V val) {
		if (children.containsKey(key)) {
			final String msg
					= String.format("Key %s is already used for sub-directories.", key);

			throw new IllegalArgumentException(msg);
		}

		return data.put(key, val);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (children == null ? 0 : children.hashCode());
		result = prime * result + (data == null ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                              return true;
		if (obj == null)                              return false;
		if (!(obj instanceof UnifiedDirectory<?, ?>)) return false;

		final UnifiedDirectory<?, ?> other = (UnifiedDirectory<?, ?>) obj;

		if (children == null) {
			if (other.children != null) return false;
		} else if (!children.equals(other.children)) {
			return false;
		}

		if (data == null) {
			if (other.data != null) return false;
		} else if (!data.equals(other.data)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return String.format("UnifiedDirectory [children=%s, data=%s]", children, data);
	}
}
