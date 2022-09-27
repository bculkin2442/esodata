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

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A pair of values, with nothing special about them.
 *
 * @author ben
 *
 * @param <LeftType>
 *                    The type of the left value.
 *
 * @param <RightType>
 *                    The type of the right value.
 */
public class SimplePair<LeftType, RightType> implements Pair<LeftType, RightType> {
	/* The left value. */
	private LeftType leftValue;
	/* The right value. */
	private RightType rightValue;

	/** Create a new pair with both sides set to null. */
	public SimplePair() {
		// Do nothing :)
	}

	/**
	 * Create a new pair with both sides set to the specified values.
	 *
	 * @param left
	 *              The value of the left side.
	 *
	 * @param right
	 *              The value of the right side.
	 */
	public SimplePair(final LeftType left, final RightType right) {
		leftValue = left;
		rightValue = right;
	}

	@Override
	public <BoundLeft, BoundRight> Pair<BoundLeft, BoundRight> bind(
			final BiFunction<LeftType, RightType, Pair<BoundLeft, BoundRight>> binder) {
		if (binder == null) throw new NullPointerException("Binder must not be null.");

		return binder.apply(leftValue, rightValue);
	}

	@Override
	public <BoundLeft> Pair<BoundLeft, RightType>
			bindLeft(final Function<LeftType, Pair<BoundLeft, RightType>> leftBinder) {
		if (leftBinder == null) throw new NullPointerException("Binder must not be null");

		return leftBinder.apply(leftValue);
	}

	@Override
	public <BoundRight> Pair<LeftType, BoundRight> bindRight(
			final Function<RightType, Pair<LeftType, BoundRight>> rightBinder) {
		if (rightBinder == null) throw new NullPointerException("Binder must not be null");

		return rightBinder.apply(rightValue);
	}

	@Override
	public <OtherLeft, OtherRight, CombinedLeft, CombinedRight>
			Pair<CombinedLeft, CombinedRight>
			combine(final Pair<OtherLeft, OtherRight> otherPair,
					final BiFunction<LeftType, OtherLeft, CombinedLeft> leftCombiner,
					final BiFunction<RightType, OtherRight,
							CombinedRight> rightCombiner) {
		return otherPair.bind((otherLeft, otherRight) -> {
			final CombinedLeft left = leftCombiner.apply(leftValue, otherLeft);
			final CombinedRight right = rightCombiner.apply(rightValue, otherRight);

			return new SimplePair<>(left, right);
		});
	}

	@Override
	public <NewLeft> Pair<NewLeft, RightType>
			mapLeft(final Function<LeftType, NewLeft> mapper) {
		if (mapper == null) throw new NullPointerException("Mapper must not be null");

		return new SimplePair<>(mapper.apply(leftValue), rightValue);
	}

	@Override
	public <NewRight> Pair<LeftType, NewRight>
			mapRight(final Function<RightType, NewRight> mapper) {
		if (mapper == null) throw new NullPointerException("Mapper must not be null");

		return new SimplePair<>(leftValue, mapper.apply(rightValue));
	}

	@Override
	public <MergedType> MergedType
			merge(final BiFunction<LeftType, RightType, MergedType> merger) {
		if (merger == null) throw new NullPointerException("Merger must not be null");

		return merger.apply(leftValue, rightValue);
	}

	@Override
	public String toString() {
		return String.format("Pair [leftValue='%s', rightValue='%s']", leftValue,
				rightValue);
	}

	@Override
	public LeftType getLeft() {
		return leftValue;
	}

	@Override
	public RightType getRight() {
		return rightValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + (leftValue == null ? 0 : leftValue.hashCode());
		result = prime * result + (rightValue == null ? 0 : rightValue.hashCode());

		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                  return true;
		if (obj == null)                  return false;
		if (!(obj instanceof SimplePair<?, ?>)) return false;

		final SimplePair<?, ?> other = (SimplePair<?, ?>) obj;

		if (leftValue == null) {
			if (other.leftValue != null) return false;
		} else if (!leftValue.equals(other.leftValue)) {
			return false;
		}

		if (rightValue == null) {
			if (other.rightValue != null) return false;
		} else if (!rightValue.equals(other.rightValue)) {
			return false;
		}

		return true;
	}
}
