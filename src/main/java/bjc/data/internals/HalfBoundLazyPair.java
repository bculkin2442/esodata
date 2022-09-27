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

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import bjc.data.Holder;
import bjc.data.Pair;
import bjc.data.Identity;
import bjc.data.LazyPair;

/*
 * @NOTE
 * 	I am not convinced that this code works correctly. Tests should be
 * 	written to make sure things only ever get instantiated once.
 *
 * 	Namely, my main concern is to whether the places that bind the pair
 * 	without setting pairBound are doing the right thing.
 */
/**
 * A lazy pair, with only one side bound.
 *
 * @author Ben Culkin
 */
@SuppressWarnings("javadoc")
public class HalfBoundLazyPair<OldType, NewLeft, NewRight>
		implements Pair<NewLeft, NewRight> {
	/* The supplier of the old value. */
	private final Supplier<OldType> oldSupplier;

	/* The function to transform the old value into a new pair. */
	private final Function<OldType, Pair<NewLeft, NewRight>> binder;

	/* The new bound pair. */
	private Pair<NewLeft, NewRight> boundPair;
	/* Has the pair been bound yet or not? */
	private boolean pairBound;

	/**
	 * Create a new half-bound lazy pair.
	 *
	 * @param oldSupp
	 *                The supplier of the old value.
	 *
	 * @param bindr
	 *                The function to use to create the pair from the old value.
	 */
	public HalfBoundLazyPair(final Supplier<OldType> oldSupp,
			final Function<OldType, Pair<NewLeft, NewRight>> bindr) {
		oldSupplier = oldSupp;
		binder = bindr;
	}

	@Override
	public <BoundLeft, BoundRight> Pair<BoundLeft, BoundRight> bind(
			final BiFunction<NewLeft, NewRight, Pair<BoundLeft, BoundRight>> bindr) {
		final Holder<Pair<NewLeft, NewRight>> newPair = new Identity<>(boundPair);
		final Holder<Boolean> newPairMade = new Identity<>(pairBound);

		final Supplier<NewLeft> leftSupp = () -> {
			if (!newPairMade.getValue()) {
				/* Bind the pair if it hasn't been bound yet. */
				newPair.replace(binder.apply(oldSupplier.get()));
				newPairMade.replace(true);
			}

			return newPair.unwrap(pair -> pair.getLeft());
		};

		final Supplier<NewRight> rightSupp = () -> {
			if (!newPairMade.getValue()) {
				/* Bind the pair if it hasn't been bound yet. */
				newPair.replace(binder.apply(oldSupplier.get()));
				newPairMade.replace(true);
			}

			return newPair.unwrap(pair -> pair.getRight());
		};

		return new BoundLazyPair<>(leftSupp, rightSupp, bindr);
	}

	@Override
	public <BoundLeft> Pair<BoundLeft, NewRight>
			bindLeft(final Function<NewLeft, Pair<BoundLeft, NewRight>> leftBinder) {
		final Supplier<NewLeft> leftSupp = () -> {
			Pair<NewLeft, NewRight> newPair = boundPair;

			if (!pairBound) {
				newPair = binder.apply(oldSupplier.get());
			}

			return newPair.getLeft();
		};

		return new HalfBoundLazyPair<>(leftSupp, leftBinder);
	}

	@Override
	public <BoundRight> Pair<NewLeft, BoundRight>
			bindRight(final Function<NewRight, Pair<NewLeft, BoundRight>> rightBinder) {
		final Supplier<NewRight> rightSupp = () -> {
			Pair<NewLeft, NewRight> newPair = boundPair;

			if (!pairBound) {
				newPair = binder.apply(oldSupplier.get());
			}

			return newPair.getRight();
		};

		return new HalfBoundLazyPair<>(rightSupp, rightBinder);
	}

	@Override
	public <OtherLeft, OtherRight, CombinedLeft, CombinedRight>
			Pair<CombinedLeft, CombinedRight>
			combine(final Pair<OtherLeft, OtherRight> otherPair,
					final BiFunction<NewLeft, OtherLeft, CombinedLeft> leftCombiner,
					final BiFunction<NewRight, OtherRight, CombinedRight> rightCombiner) {
		return otherPair.bind((otherLeft, otherRight) -> bind((leftVal, rightVal) -> {
			CombinedLeft cLeft = leftCombiner.apply(leftVal, otherLeft);
			CombinedRight cRight = rightCombiner.apply(rightVal, otherRight);

			return new LazyPair<>(cLeft, cRight);
		}));
	}

	@Override
	public <NewLeftType> Pair<NewLeftType, NewRight>
			mapLeft(final Function<NewLeft, NewLeftType> mapper) {
		final Supplier<NewLeftType> leftSupp = () -> {
			if (pairBound)
				return mapper.apply(boundPair.getLeft());

			final NewLeft leftVal = binder.apply(oldSupplier.get()).getLeft();

			return mapper.apply(leftVal);
		};

		final Supplier<NewRight> rightSupp = () -> {
			if (pairBound)
				return boundPair.getRight();

			return binder.apply(oldSupplier.get()).getRight();
		};

		return new LazyPair<>(leftSupp, rightSupp);
	}

	@Override
	public <NewRightType> Pair<NewLeft, NewRightType>
			mapRight(final Function<NewRight, NewRightType> mapper) {
		final Supplier<NewLeft> leftSupp = () -> {
			if (pairBound)
				return boundPair.getLeft();

			return binder.apply(oldSupplier.get()).getLeft();
		};

		final Supplier<NewRightType> rightSupp = () -> {
			if (pairBound)
				return mapper.apply(boundPair.getRight());

			final NewRight rightVal = binder.apply(oldSupplier.get()).getRight();

			return mapper.apply(rightVal);
		};

		return new LazyPair<>(leftSupp, rightSupp);
	}

	@Override
	public <MergedType> MergedType
			merge(final BiFunction<NewLeft, NewRight, MergedType> merger) {
		if (!pairBound) {
			boundPair = binder.apply(oldSupplier.get());

			pairBound = true;
		}

		return boundPair.merge(merger);
	}
}
