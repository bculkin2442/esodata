package bjc.data.internals;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import bjc.data.IHolder;
import bjc.data.Option;

/**
 * A wrapped optional value.
 *
 * @author Ben Culkin.
 * @param <ContainedType>
 *        The wrapped type.
 */
public class WrappedOption<ContainedType> implements IHolder<ContainedType> {
	/* The held value. */
	private final IHolder<IHolder<ContainedType>> held;

	/**
	 * Create a new wrapped option.
	 *
	 * @param seedValue
	 *        The value to wrap.
	 */
	public WrappedOption(final IHolder<ContainedType> seedValue) {
		held = new Option<>(seedValue);
	}

	/*
	 * The dummy parameter is to ensure the compiler can pick the right
	 * method, because without this method erases to the same type as the
	 * public one.
	 */
	private WrappedOption(final IHolder<IHolder<ContainedType>> toHold,
			@SuppressWarnings("unused") final boolean dummy) {
		held = toHold;
	}

	@Override
	public <BoundType> IHolder<BoundType> bind(final Function<ContainedType, IHolder<BoundType>> binder) {
		final IHolder<IHolder<BoundType>> newHolder = held.map((containedHolder) -> {
			return containedHolder.bind((containedValue) -> {
				if(containedValue == null) return new Option<>(null);

				return binder.apply(containedValue);
			});
		});

		return new WrappedOption<>(newHolder, false);
	}

	@Override
	public <NewType> Function<ContainedType, IHolder<NewType>> lift(final Function<ContainedType, NewType> func) {
		return (val) -> {
			return new Option<>(func.apply(val));
		};
	}

	@Override
	public <MappedType> IHolder<MappedType> map(final Function<ContainedType, MappedType> mapper) {
		final IHolder<IHolder<MappedType>> newHolder = held.map((containedHolder) -> {
			return containedHolder.map((containedValue) -> {
				if(containedValue == null) return null;

				return mapper.apply(containedValue);
			});
		});

		return new WrappedOption<>(newHolder, false);
	}

	@Override
	public IHolder<ContainedType> transform(final UnaryOperator<ContainedType> transformer) {
		held.transform((containedHolder) -> {
			return containedHolder.transform((containedValue) -> {
				if(containedValue == null) return null;

				return transformer.apply(containedValue);
			});
		});

		return this;
	}

	@Override
	public <UnwrappedType> UnwrappedType unwrap(final Function<ContainedType, UnwrappedType> unwrapper) {
		return held.unwrap((containedHolder) -> {
			return containedHolder.unwrap((containedValue) -> {
				if(containedValue == null) return null;

				return unwrapper.apply(containedValue);
			});
		});
	}
}
