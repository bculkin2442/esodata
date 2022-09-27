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

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * A holder that may or may not contain a value.
 *
 * @author ben
 *
 * @param <ContainedType>
 *                        The type of the value that may or may not be held.
 */
public class Option<ContainedType> implements Holder<ContainedType> {
	private ContainedType held;
	private boolean isHolding;
	
	/**
	 * Create a new empty optional.
	 */
	public Option() {
	    isHolding = false;
	}
	
	/**
	 * Create a new optional, using the given initial value.
	 *
	 * @param seed
	 *             The initial value for the optional.
	 */
	public Option(final ContainedType seed) {
		held = seed;
		isHolding = true;
	}

	@Override
	public <BoundType> Holder<BoundType>
			bind(final Function<ContainedType, Holder<BoundType>> binder) {
		if (isHolding) return new Option<>();

		return binder.apply(held);
	}

	@Override
	public <NewType> Function<ContainedType, Holder<NewType>>
			lift(final Function<ContainedType, NewType> func) {
		return val -> new Option<>(func.apply(val));
	}

	@Override
	public <MappedType> Holder<MappedType>
			map(final Function<ContainedType, MappedType> mapper) {
		if (isHolding) return new Option<>();

		return new Option<>(mapper.apply(held));
	}

	@Override
	public Holder<ContainedType>
			transform(final UnaryOperator<ContainedType> transformer) {
		if (isHolding) held = transformer.apply(held);

		return this;
	}

	@Override
	public <UnwrappedType> UnwrappedType
			unwrap(final Function<ContainedType, UnwrappedType> unwrapper) {
		if (isHolding) return null;

		return unwrapper.apply(held);
	}

	@Override
	public String toString() {
		return String.format("Option [held='%s']", held);
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 1;
		result = prime * result + (held == null ? 0 : held.hashCode());

		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                 return true;
		if (obj == null)                 return false;
		if (!(obj instanceof Option<?>)) return false;

		final Option<?> other = (Option<?>) obj;

		if (held == null) {
			if (other.held != null) return false;
		} else if (!held.equals(other.held)) {
			return false;
		}

		return true;
	}
}
