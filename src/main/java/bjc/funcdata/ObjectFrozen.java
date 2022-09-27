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

/**
 * Exception that implementations of {@link Freezable} can throw if you attempt
 * to modify a frozen object.
 * 
 * @author Ben Culkin
 *
 */
public class ObjectFrozen extends RuntimeException {
	private static final long serialVersionUID = -1567447627139090728L;

	/**
	 * Create a new ObjectFrozen exception.
	 */
	public ObjectFrozen() {
		super();
	}

	/**
	 * Create a new ObjectFrozen exception.
	 * 
	 * @param message The message of the exception.
	 */
	public ObjectFrozen(String message) {
		super(message);
	}

	/**
	 * Create a new ObjectFrozen exception.
	 * 
	 * @param cause The root cause of this exception.
	 */
	public ObjectFrozen(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a new ObjectFrozen exception.
	 * 
	 * @param message The message of the exception.
	 * @param cause The root cause of this exception.
	 */
	public ObjectFrozen(String message, Throwable cause) {
		super(message, cause);
	}
}
