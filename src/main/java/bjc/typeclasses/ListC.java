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
package bjc.typeclasses;

import java.util.List;

/**
 * A list wrapped in a container
 * @author bjcul
 *
 * @param <T> The type contained
 */
public class ListC<T> implements Container<T, ListC<?>> {
	private List<T> contained;
	
	/**
	 * Create a new contained list
	 * 
	 * @param contained The contained list
	 */
	public ListC(List<T> contained) {
		this.contained = contained;
	}
	
	/**
	 * Retrieve the contained list
	 * 
	 * @return The contained list
	 */
	public List<T> list() {
		return contained;
	}
}
