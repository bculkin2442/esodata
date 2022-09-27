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
import bjc.data.Lazy;

/**
 * A wrapped lazy value.
 *
 * @author Ben Culkin
 * @param <ContainedType>
 *                        The type of the wrapped value.
 */
public class WrappedLazy<ContainedType> implements Holder<ContainedType> {
	/* Held value. */
	private final Holder<Holder<ContainedType>> held;

	/**
	 * Create a new wrapped lazy value.
	 *
	 * @param wrappedHolder
	 *                      The holder to make lazy.
	 */
	public WrappedLazy(final Holder<ContainedType> wrappedHolder) {
		held = new Lazy<>(wrappedHolder);
	}

	/*
	 * This has an extra parameter, because otherwise it erases to the same as the
	 * public one.
	 *
	 * This is a case where reified generics would be useful, because then the
	 * compiler could know which one we meant without the dummy parameter.
	 */
	private WrappedLazy(final Holder<Holder<ContainedType>> wrappedHolder,
			@SuppressWarnings("unused") final boolean dummy) {
		held = wrappedHolder;
	}

	@Override
	public <BoundType> Holder<BoundType>
			bind(final Function<ContainedType, Holder<BoundType>> binder) {
		final Holder<Holder<BoundType>> newHolder = held.map(containedHolder -> {
			return containedHolder.bind(binder);
		});

		return new WrappedLazy<>(newHolder, false);
	}

	@Override
	public <NewType> Function<ContainedType, Holder<NewType>>
			lift(final Function<ContainedType, NewType> func) {
		return val -> {
			return new Lazy<>(func.apply(val));
		};
	}

	@Override
	public <MappedType> Holder<MappedType>
			map(final Function<ContainedType, MappedType> mapper) {
		final Holder<Holder<MappedType>> newHolder = held.map(containedHolder -> {
			return containedHolder.map(mapper);
		});

		return new WrappedLazy<>(newHolder, false);
	}

	@Override
	public Holder<ContainedType>
			transform(final UnaryOperator<ContainedType> transformer) {
		held.transform(containedHolder -> {
			return containedHolder.transform(transformer);
		});

		return this;
	}

	@Override
	public <UnwrappedType> UnwrappedType
			unwrap(final Function<ContainedType, UnwrappedType> unwrapper) {
		return held.unwrap(containedHolder -> {
			return containedHolder.unwrap(unwrapper);
		});
	}
}
