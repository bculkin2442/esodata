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
package bjc.data.internals;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import bjc.data.Holder;
import bjc.data.ListHolder;
import bjc.funcdata.ListEx;

/**
 * Holds a list, converted into a holder.
 *
 * @author Ben Culkin
 */
@SuppressWarnings("javadoc")
public class BoundListHolder<ContainedType> implements Holder<ContainedType> {
	/* The list of contained holders. */
	private final ListEx<Holder<ContainedType>> heldHolders;

	/**
	 * Create a new list of holders.
	 *
	 * @param toHold
	 *               The list of holders to, well, hold.
	 */
	public BoundListHolder(final ListEx<Holder<ContainedType>> toHold) {
		heldHolders = toHold;
	}

	@Override
	public <BoundType> Holder<BoundType>
			bind(final Function<ContainedType, Holder<BoundType>> binder) {
		if (binder == null)
			throw new NullPointerException("Binder must not be null");

		final ListEx<Holder<BoundType>> boundHolders
				= heldHolders.map(containedHolder -> {
					return containedHolder.bind(binder);
				});

		return new BoundListHolder<>(boundHolders);
	}

	@Override
	public <NewType> Function<ContainedType, Holder<NewType>>
			lift(final Function<ContainedType, NewType> func) {
		if (func == null)
			throw new NullPointerException("Function to lift must not be null");

		return val -> {
			return new ListHolder<>(func.apply(val));
		};
	}

	@Override
	public <MappedType> Holder<MappedType>
			map(final Function<ContainedType, MappedType> mapper) {
		if (mapper == null)
			throw new NullPointerException("Mapper must not be null");

		final ListEx<Holder<MappedType>> mappedHolders
				= heldHolders.map(containedHolder -> {
					return containedHolder.map(mapper);
				});

		return new BoundListHolder<>(mappedHolders);
	}

	@Override
	public Holder<ContainedType>
			transform(final UnaryOperator<ContainedType> transformer) {
		if (transformer == null)
			throw new NullPointerException("Transformer must not be null");

		heldHolders.forEach(containedHolder -> {
			containedHolder.transform(transformer);
		});

		return this;
	}

	@Override
	public <UnwrappedType> UnwrappedType
			unwrap(final Function<ContainedType, UnwrappedType> unwrapper) {
		if (unwrapper == null)
			throw new NullPointerException("Unwrapper must not be null");

		/*
		 * @NOTE Is there another way we could want to do this?
		 */
		return heldHolders.randItem().unwrap(unwrapper);
	}
}
