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

/**
 * Represents a 'context' which is a hierarchical set of objects.
 * @author Ben Culkin
 *
 */
public interface Context {
	/**
	 * Register an object with this context.
	 * 
	 * @param name The name of the object.
	 * @param o The object to register.
	 */
	void register(String name, Object o);

	/**
	 * Get the parent of this context.
	 * 
	 * @return The parent of this context.
	 */
	Context getParent();

	/**
	 * Get an object from this context.
	 * 
	 * @param name The name of the object.
	 * 
	 * @return The object bound to that name.
	 */
	Object get(String name);
	
	/**
	 * Get an object which is an instance of the provided class or a subclass
	 * thereof.
	 * 
	 * @param <T> The type of the object.
	 * 
	 * @param contract The class of the object.
	 * 
	 * @return An instance of the provided class.
	 */
	<T> T get(Class<T> contract);
	
	/**
	 * Get a named object which is an instance of the provided class or a subclass
	 * thereof.
	 * 
	 * @param <T> The type of the object.
	 * 
	 * @param name The name of the object
	 * @param contract The class of the object.
	 * 
	 * @return An instance of the provided class, with the given name..
	 */
	default <T> T get(String name, Class<T> contract) {
		Object obj = get(name);
		return obj == null 
				? getParent().get(name, contract) 
						: contract.cast(obj);
	};
}
