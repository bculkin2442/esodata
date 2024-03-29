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

/**
 * Interface for a double-sided object.
 *
 * @author bjculkin
 *
 */
public interface DoubleSided {
	/**
	 * Flips the object.
	 *
	 * The active side becomes inactive, and the inactive side becomes active.
	 */
	void flip();

	/**
	 * Check which side of the object is active;
	 *
	 * @return True if the front side is active, false otherwise.
	 */
	boolean currentSide();
}
