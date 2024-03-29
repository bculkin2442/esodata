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
import bjc.data.Option;

/**
 * A wrapped optional value.
 *
 * @author Ben Culkin.
 * @param <ContainedType>
 *                        The wrapped type.
 */
public class WrappedOption<ContainedType> implements Holder<ContainedType> {
	/* The held value. */
	private final Holder<Holder<ContainedType>> held;

	/**
	 * Create a new wrapped option.
	 *
	 * @param seedValue
	 *                  The value to wrap.
	 */
	public WrappedOption(final Holder<ContainedType> seedValue) {
		held = new Option<>(seedValue);
	}

	/*
	 * The dummy parameter is to ensure the compiler can pick the right method,
	 * because without this method erases to the same type as the public one.
	 */
	private WrappedOption(final Holder<Holder<ContainedType>> toHold,
			@SuppressWarnings("unused") final boolean dummy) {
		held = toHold;
	}

	@Override
	public <BoundType> Holder<BoundType>
			bind(final Function<ContainedType, Holder<BoundType>> binder) {
		final Holder<Holder<BoundType>> newHolder = held.map(containedHolder -> {
			return containedHolder.bind((containedValue) -> {
				if (containedValue == null)
					return new Option<>(null);

				return binder.apply(containedValue);
			});
		});

		return new WrappedOption<>(newHolder, false);
	}

	@Override
	public <NewType> Function<ContainedType, Holder<NewType>>
			lift(final Function<ContainedType, NewType> func) {
		return val -> {
			return new Option<>(func.apply(val));
		};
	}

	@Override
	public <MappedType> Holder<MappedType>
			map(final Function<ContainedType, MappedType> mapper) {
		final Holder<Holder<MappedType>> newHolder = held.map(containedHolder -> {
			return containedHolder.map((containedValue) -> {
				if (containedValue == null)
					return null;

				return mapper.apply(containedValue);
			});
		});

		return new WrappedOption<>(newHolder, false);
	}

	@Override
	public Holder<ContainedType>
			transform(final UnaryOperator<ContainedType> transformer) {
		held.transform(containedHolder -> {
			return containedHolder.transform((containedValue) -> {
				if (containedValue == null)
					return null;

				return transformer.apply(containedValue);
			});
		});

		return this;
	}

	@Override
	public <UnwrappedType> UnwrappedType
			unwrap(final Function<ContainedType, UnwrappedType> unwrapper) {
		return held.unwrap(containedHolder -> {
			return containedHolder.unwrap((containedValue) -> {
				if (containedValue == null)
					return null;

				return unwrapper.apply(containedValue);
			});
		});
	}
}
