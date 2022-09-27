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

import bjc.data.internals.BoundListHolder;
import bjc.funcdata.FunctionalList;
import bjc.funcdata.ListEx;

/**
 * A holder that represents a set of non-deterministic computations.
 *
 * @author ben
 *
 * @param <ContainedType>
 *                        The type of contained value.
 */
public class ListHolder<ContainedType> implements Holder<ContainedType> {
	private ListEx<ContainedType> heldValues;

	/**
	 * Create a new list holder.
	 *
	 * @param values
	 *               The possible values for the computation.
	 */
	@SafeVarargs
	public ListHolder(final ContainedType... values) {
		heldValues = new FunctionalList<>();

		if (values != null) {
			for (final ContainedType containedValue : values) {
				heldValues.add(containedValue);
			}
		}
	}

	/* Create a new holder with values. */
	private ListHolder(final ListEx<ContainedType> toHold) {
		heldValues = toHold;
	}

	@Override
	public <BoundType> Holder<BoundType>
			bind(final Function<ContainedType, Holder<BoundType>> binder) {
		final ListEx<Holder<BoundType>> boundValues = heldValues.map(binder);

		return new BoundListHolder<>(boundValues);
	}

	@Override
	public <NewType> Function<ContainedType, Holder<NewType>>
			lift(final Function<ContainedType, NewType> func) {
		return val -> new ListHolder<>(new FunctionalList<>(func.apply(val)));
	}

	@Override
	public <MappedType> Holder<MappedType>
			map(final Function<ContainedType, MappedType> mapper) {
		final ListEx<MappedType> mappedValues = heldValues.map(mapper);

		return new ListHolder<>(mappedValues);
	}

	@Override
	public Holder<ContainedType>
			transform(final UnaryOperator<ContainedType> transformer) {
		heldValues = heldValues.map(transformer);

		return this;
	}

	@Override
	public <UnwrappedType> UnwrappedType
			unwrap(final Function<ContainedType, UnwrappedType> unwrapper) {
		return unwrapper.apply(heldValues.randItem());
	}

	@Override
	public String toString() {
		return String.format("ListHolder [heldValues=%s]", heldValues);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + (heldValues == null ? 0 : heldValues.hashCode());

		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                     return true;
		if (obj == null)                     return false;
		if (!(obj instanceof ListHolder<?>)) return false;

		final ListHolder<?> other = (ListHolder<?>) obj;

		if (heldValues == null) {
			if (other.heldValues != null) return false;
		} else if (!heldValues.equals(other.heldValues)) {
			return false;
		}

		return true;
	}
}
