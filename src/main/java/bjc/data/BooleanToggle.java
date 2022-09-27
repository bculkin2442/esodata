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
 * A simple {@link ValueToggle} that swaps between true and false.
 *
 * @author EVE
 *
 */
public class BooleanToggle implements Toggle<Boolean> {
	/* The contained value. */
	private boolean val;

	/**
	 * Create a new, initially false, flip-flop.
	 */
	public BooleanToggle() {
		this(false);
	}

	/**
	 * Create a flip-flop with the specified initial value.
	 *
	 * @param initial
	 *                The initial value of the flip-flop.
	 */
	public BooleanToggle(final boolean initial) {
		val = initial;
	}

	@Override
	public Boolean get() {
		final boolean res = val;

		val = !res;

		return res;
	}

	@Override
	public Boolean peek() {
		return val;
	}

	@Override
	public void set(final boolean vl) {
		val = vl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 1;

		result = prime * result + (val ? 1231 : 1237);

		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                     return true;
		if (obj == null)                     return false;
		if (!(obj instanceof BooleanToggle)) return false;

		final BooleanToggle other = (BooleanToggle) obj;

		if (val != other.val) return false;
		else                  return true;
	}

	@Override
	public String toString() {
		return String.format("%s", val);
	}
}
