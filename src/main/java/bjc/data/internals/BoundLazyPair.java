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

/**
 * Implements a lazy pair that has been bound.
 *
 * @author Ben Culkin
 */
@SuppressWarnings("javadoc")
public class BoundLazyPair<OldLeft, OldRight, NewLeft, NewRight>
		implements Pair<NewLeft, NewRight> {
	/* The supplier of the left value. */
	private final Supplier<OldLeft> leftSupplier;
	/* The supplier of the right value. */
	private final Supplier<OldRight> rightSupplier;

	/* The binder to transform values. */
	private final BiFunction<OldLeft, OldRight, Pair<NewLeft, NewRight>> binder;

	/* The bound pair. */
	private Pair<NewLeft, NewRight> boundPair;

	/* Whether the pair has been bound yet. */
	private boolean pairBound;

	/**
	 * Create a new bound lazy pair.
	 *
	 * @param leftSupp
	 *                  The supplier for the left value.
	 *
	 * @param rightSupp
	 *                  The supplier for the right value.
	 *
	 * @param bindr
	 *                  The function to use to bind the left and right into a new
	 *                  pair.
	 */
	public BoundLazyPair(final Supplier<OldLeft> leftSupp,
			final Supplier<OldRight> rightSupp,
			final BiFunction<OldLeft, OldRight, Pair<NewLeft, NewRight>> bindr) {
		leftSupplier = leftSupp;
		rightSupplier = rightSupp;
		binder = bindr;
	}

	@Override
	public <BoundLeft, BoundRight> Pair<BoundLeft, BoundRight> bind(
			final BiFunction<NewLeft, NewRight, Pair<BoundLeft, BoundRight>> bindr) {
		if (bindr == null)
			throw new NullPointerException("Binder must not be null");

		final Holder<Pair<NewLeft, NewRight>> newPair = new Identity<>(boundPair);
		final Holder<Boolean> newPairMade = new Identity<>(pairBound);

		final Supplier<NewLeft> leftSupp = () -> {
			if (!newPairMade.getValue()) {
				/*
				 * If the pair hasn't been bound before, bind it.
				 */
				newPair.replace(binder.apply(leftSupplier.get(), rightSupplier.get()));

				newPairMade.replace(true);
			}

			return newPair.unwrap(pair -> pair.getLeft());
		};

		final Supplier<NewRight> rightSupp = () -> {
			if (!newPairMade.getValue()) {
				/*
				 * If the pair hasn't been bound before, bind it.
				 */
				newPair.replace(binder.apply(leftSupplier.get(), rightSupplier.get()));

				newPairMade.replace(true);
			}

			return newPair.unwrap(pair -> pair.getRight());
		};

		return new BoundLazyPair<>(leftSupp, rightSupp, bindr);
	}

	@Override
	public <BoundLeft> Pair<BoundLeft, NewRight>
			bindLeft(final Function<NewLeft, Pair<BoundLeft, NewRight>> leftBinder) {
		if (leftBinder == null)
			throw new NullPointerException("Left binder must not be null");

		final Supplier<NewLeft> leftSupp = () -> {
			Pair<NewLeft, NewRight> newPair = boundPair;

			if (!pairBound) {
				/*
				 * If the pair hasn't been bound before, bind it.
				 */
				newPair = binder.apply(leftSupplier.get(), rightSupplier.get());
			}

			return newPair.getLeft();
		};

		return new HalfBoundLazyPair<>(leftSupp, leftBinder);
	}

	@Override
	public <BoundRight> Pair<NewLeft, BoundRight>
			bindRight(final Function<NewRight, Pair<NewLeft, BoundRight>> rightBinder) {
		if (rightBinder == null)
			throw new NullPointerException("Right binder must not be null");

		final Supplier<NewRight> rightSupp = () -> {
			Pair<NewLeft, NewRight> newPair = boundPair;

			if (!pairBound) {
				/*
				 * If the pair hasn't been bound before, bind it.
				 */
				newPair = binder.apply(leftSupplier.get(), rightSupplier.get());
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
		if (otherPair == null) {
			throw new NullPointerException("Other pair must not be null");
		} else if (leftCombiner == null) {
			throw new NullPointerException("Left combiner must not be null");
		} else if (rightCombiner == null) {
			throw new NullPointerException("Right combiner must not be null");
		}

		return otherPair.bind((otherLeft, otherRight) -> bind((leftVal, rightVal) -> {
			CombinedLeft cLeft = leftCombiner.apply(leftVal, otherLeft);
			CombinedRight cRight = rightCombiner.apply(rightVal, otherRight);

			return new LazyPair<>(cLeft, cRight);
		}));
	}

	@Override
	public <NewLeftType> Pair<NewLeftType, NewRight>
			mapLeft(final Function<NewLeft, NewLeftType> mapper) {
		if (mapper == null)
			throw new NullPointerException("Mapper must not be null");

		final Supplier<NewLeftType> leftSupp = () -> {
			if (!pairBound) {
				final NewLeft leftVal
						= binder.apply(leftSupplier.get(), rightSupplier.get()).getLeft();

				return mapper.apply(leftVal);
			}

			return mapper.apply(boundPair.getLeft());
		};

		final Supplier<NewRight> rightSupp = () -> {
			if (!pairBound)
				return binder.apply(leftSupplier.get(), rightSupplier.get()).getRight();

			return boundPair.getRight();
		};

		return new LazyPair<>(leftSupp, rightSupp);
	}

	@Override
	public <NewRightType> Pair<NewLeft, NewRightType>
			mapRight(final Function<NewRight, NewRightType> mapper) {
		if (mapper == null)
			throw new NullPointerException("Mapper must not be null");

		final Supplier<NewLeft> leftSupp = () -> {
			if (!pairBound)
				return binder.apply(leftSupplier.get(), rightSupplier.get()).getLeft();

			return boundPair.getLeft();
		};

		final Supplier<NewRightType> rightSupp = () -> {
			if (!pairBound) {
				final NewRight rightVal = binder
						.apply(leftSupplier.get(), rightSupplier.get()).getRight();

				return mapper.apply(rightVal);
			}

			return mapper.apply(boundPair.getRight());
		};

		return new LazyPair<>(leftSupp, rightSupp);
	}

	@Override
	public <MergedType> MergedType
			merge(final BiFunction<NewLeft, NewRight, MergedType> merger) {
		if (merger == null)
			throw new NullPointerException("Merger must not be null");

		if (!pairBound) {
			/*
			 * If the pair isn't bound yet, bind it.
			 */
			boundPair = binder.apply(leftSupplier.get(), rightSupplier.get());

			pairBound = true;
		}

		return boundPair.merge(merger);
	}

	@Override
	public String toString() {
		if (pairBound)
			return boundPair.toString();

		return "(un-materialized)";
	}
}
