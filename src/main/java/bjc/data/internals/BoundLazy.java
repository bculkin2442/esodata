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
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import bjc.data.Holder;
import bjc.data.Lazy;
import bjc.funcdata.FunctionalList;
import bjc.funcdata.ListEx;

/**
 * Implements a lazy holder that has been bound.
 *
 * @author Ben Culkin
 * @param <OldType>
 *                             The old type of the value.
 * @param <BoundContainedType>
 *                             The type of the new bound value.
 */
public class BoundLazy<OldType, BoundContainedType>
		implements Holder<BoundContainedType> {
	/* The old value. */
	private final Supplier<Holder<OldType>> oldSupplier;

	/* The function to use to transform the old value into a new value. */
	private final Function<OldType, Holder<BoundContainedType>> binder;

	/* The bound value being held. */
	private Holder<BoundContainedType> boundHolder;

	/* Whether the bound value has been actualized or not. */
	private boolean holderBound;

	/* Transformations currently pending on the bound value. */
	private final ListEx<UnaryOperator<BoundContainedType>> actions
			= new FunctionalList<>();

	/**
	 * Create a new bound lazy value.
	 *
	 * @param supp
	 *               The supplier of the old value.
	 *
	 * @param binder
	 *               The function to use to bind the old value to the new one.
	 */
	public BoundLazy(final Supplier<Holder<OldType>> supp,
			final Function<OldType, Holder<BoundContainedType>> binder) {
		oldSupplier = supp;
		this.binder = binder;
	}

	@Override
	public <BoundType> Holder<BoundType>
			bind(final Function<BoundContainedType, Holder<BoundType>> bindr) {
		if (bindr == null)
			throw new NullPointerException("Binder must not be null");

		/* Prepare a list of pending actions. */
		final ListEx<UnaryOperator<BoundContainedType>> pendingActions
				= new FunctionalList<>();
		
		for (UnaryOperator<BoundContainedType> pendAct : actions) {
			pendingActions.add(pendAct);
		}

		/* Create the new supplier of a value. */
		final Supplier<Holder<BoundContainedType>> typeSupplier = () -> {
			Holder<BoundContainedType> oldHolder = boundHolder;

			/* Bind the value if it hasn't been bound before. */
			if (!holderBound) {
				oldHolder = oldSupplier.get().unwrap(binder);
			}

			/* Apply all the pending actions. */
			return pendingActions.reduceAux(oldHolder,
					(action, state) -> state.transform(action), value -> value);
		};

		return new BoundLazy<>(typeSupplier, bindr);
	}

	@Override
	public <NewType> Function<BoundContainedType, Holder<NewType>>
			lift(final Function<BoundContainedType, NewType> func) {
		if (func == null)
			throw new NullPointerException("Function to lift must not be null");

		return val -> {
			return new Lazy<>(func.apply(val));
		};
	}

	@Override
	public <MappedType> Holder<MappedType>
			map(final Function<BoundContainedType, MappedType> mapper) {
		if (mapper == null)
			throw new NullPointerException("Mapper must not be null");

		/* Prepare a list of pending actions. */
		final ListEx<UnaryOperator<BoundContainedType>> pendingActions
				= new FunctionalList<>();
		
		for (UnaryOperator<BoundContainedType> pendAct : actions) {
			pendingActions.add(pendAct);
		}

		/* Prepare the new supplier. */
		final Supplier<MappedType> typeSupplier = () -> {
			Holder<BoundContainedType> oldHolder = boundHolder;

			/* Bound the value if it hasn't been bound. */
			if (!holderBound) {
				oldHolder = oldSupplier.get().unwrap(binder);
			}

			/* Apply pending actions. */
			return pendingActions.reduceAux(oldHolder.getValue(),
					(action, state) -> action.apply(state), value -> mapper.apply(value));
		};

		return new Lazy<>(typeSupplier);
	}

	@Override
	public String toString() {
		if (holderBound)
			return boundHolder.toString();

		return "(unmaterialized)";
	}

	@Override
	public Holder<BoundContainedType>
			transform(final UnaryOperator<BoundContainedType> transformer) {
		if (transformer == null)
			throw new NullPointerException("Transformer must not be null");

		actions.add(transformer);

		return this;
	}

	@Override
	public <UnwrappedType> UnwrappedType
			unwrap(final Function<BoundContainedType, UnwrappedType> unwrapper) {
		if (unwrapper == null)
			throw new NullPointerException("Unwrapper must not be null");

		if (!holderBound) {
			boundHolder = oldSupplier.get().unwrap(binder::apply);
		}

		return boundHolder.unwrap(unwrapper);
	}
}
