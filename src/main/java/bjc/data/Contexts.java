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
package bjc.data;

import java.util.*;

/**
 * Utility methods for dealing with contexts.
 * 
 * @author Ben Culkin
 *
 */
public class Contexts {
	/**
	 * The null context, which always throws an exception.
	 */
	public static final Context NULL = new NullContextImpl();

	private Contexts() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Create a new context with no parent.
	 * 
	 * @return A context with no parent.
	 */
	public static Context create() {
		return new ContextImpl(NULL);
	}

	/**
	 * Create a context with the specified parent.
	 * 
	 * @param parent The parent of this context.
	 * 
	 * @return A context with the given context as its parent.
	 */
	public static Context create(Context parent) {
		return new ContextImpl(parent);
	}
}

class NullContextImpl implements Context {
	@Override
	public Context getParent() {
		return this;
	}

	@Override
	public void register(String name, Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get(String name) {
		throw new NoSuchElementException();
	}
	
	@Override
	public <T> T get(Class<T> contract) {
		throw new NoSuchElementException();
	}
}

class ContextImpl implements Context {

	private final Context parent;

	private final Map<String, Object> objects;

	public ContextImpl(Context parent) {
		this.parent = parent;
		this.objects = new HashMap<>();
	}

	@Override
	public void register(String name, Object o) {
		objects.put(name, o);
	}

	@Override
	public Object get(String name) {
		if (objects.containsKey(name)) {
			return objects.get(name);
		}
		return parent.get(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> contract) {
		for (Object o : objects.values()) {
			if (contract.isInstance(o)) {
				return (T) o;
			}
		}
		return parent.get(contract);
	}

	@Override
	public Context getParent() {
		return parent;
	}
}