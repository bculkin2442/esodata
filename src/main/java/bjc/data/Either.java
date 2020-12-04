package bjc.data;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a pair where only one side has a value.
 *
 * @author ben
 *
 * @param <LeftType>
 *                    The type that could be on the left.
 *
 * @param <RightType>
 *                    The type that could be on the right.
 *
 */
public class Either<LeftType, RightType> implements Pair<LeftType, RightType> {
	/**
	 * Create a new either with the left value occupied.
	 *
	 * @param <LeftType>
	 *                    The type of the left value.
	 *
	 * @param <RightType>
	 *                    The type of the empty right value.
	 *
	 * @param left
	 *                    The value to put on the left.
	 *
	 * @return An either with the left side occupied.
	 */
	public static <LeftType, RightType> Either<LeftType, RightType>
			left(final LeftType left) {
		return new Either<>(left, null);
	}

	/**
	 * Create a new either with the right value occupied.
	 *
	 * @param <LeftType>
	 *                    The type of the empty left value.
	 *
	 * @param <RightType>
	 *                    The type of the right value.
	 *
	 * @param right
	 *                    The value to put on the right.
	 *
	 * @return An either with the right side occupied.
	 */
	public static <LeftType, RightType> Either<LeftType, RightType>
			right(final RightType right) {
		return new Either<>(null, right);
	}

	/* The left value of the either. */
	private LeftType leftVal;
	/* The right value of the either. */
	private RightType rightVal;
	/* Whether the left value is the one filled out. */
	private boolean isLeft;

	/* Create a new either with specifed values. */
	private Either(final LeftType left, final RightType right) {
		if (left == null) {
			rightVal = right;
		} else {
			leftVal = left;

			isLeft = true;
		}
	}

	@Override
	public <BoundLeft, BoundRight> Pair<BoundLeft, BoundRight> bind(
			final BiFunction<LeftType, RightType, Pair<BoundLeft, BoundRight>> binder) {
		if (binder == null) throw new NullPointerException("Binder must not be null");

		return binder.apply(leftVal, rightVal);
	}

	@Override
	public <BoundLeft> Pair<BoundLeft, RightType>
			bindLeft(final Function<LeftType, Pair<BoundLeft, RightType>> leftBinder) {
		if (leftBinder == null) throw new NullPointerException("Left binder must not be null");

		if (isLeft) return leftBinder.apply(leftVal);
		else        return new Either<>(null, rightVal);
	}

	@Override
	public <BoundRight> Pair<LeftType, BoundRight> bindRight(
			final Function<RightType, Pair<LeftType, BoundRight>> rightBinder) {
		if (rightBinder == null) throw new NullPointerException("Right binder must not be null");

		if (isLeft) return new Either<>(leftVal, null);
		else        return rightBinder.apply(rightVal);
	}

	@Override
	public <OtherLeft, OtherRight, CombinedLeft, CombinedRight>
			Pair<CombinedLeft, CombinedRight>
			combine(final Pair<OtherLeft, OtherRight> otherPair,
					final BiFunction<LeftType, OtherLeft, CombinedLeft> leftCombiner,
					final BiFunction<RightType, OtherRight,
							CombinedRight> rightCombiner) {
		if (otherPair == null) {
			throw new NullPointerException("Other pair must not be null");
		} else if (leftCombiner == null) {
			throw new NullPointerException("Left combiner must not be null");
		} else if (rightCombiner == null) {
			throw new NullPointerException("Right combiner must not be null");
		}

		if (isLeft) {
			return otherPair.bind((otherLeft, otherRight) -> {
				CombinedLeft cLeft = leftCombiner.apply(leftVal, otherLeft);

				return new Either<>(cLeft, null);
			});
		} else {	
			return otherPair.bind((otherLeft, otherRight) -> {
				CombinedRight cRight = rightCombiner.apply(rightVal, otherRight);
	
				return new Either<>(null, cRight);
			});
		}
	}

	@Override
	public <NewLeft> Pair<NewLeft, RightType>
			mapLeft(final Function<LeftType, NewLeft> mapper) {
		if (mapper == null) throw new NullPointerException("Mapper must not be null");

		if (isLeft) return new Either<>(mapper.apply(leftVal), null);
		else        return new Either<>(null, rightVal);
	}

	@Override
	public <NewRight> Pair<LeftType, NewRight>
			mapRight(final Function<RightType, NewRight> mapper) {
		if (mapper == null) throw new NullPointerException("Mapper must not be null");

		if (isLeft) return new Either<>(leftVal, null);
		else        return new Either<>(null, mapper.apply(rightVal));
	}

	@Override
	public <MergedType> MergedType
			merge(final BiFunction<LeftType, RightType, MergedType> merger) {
		if (merger == null) throw new NullPointerException("Merger must not be null");

		return merger.apply(leftVal, rightVal);
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 1;
		result = prime * result + (isLeft ? 1231 : 1237);
		result = prime * result + (leftVal == null ? 0 : leftVal.hashCode());
		result = prime * result + (rightVal == null ? 0 : rightVal.hashCode());

		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                    return true;
		if (obj == null)                    return false;
		if (!(obj instanceof Either<?, ?>)) return false;

		final Either<?, ?> other = (Either<?, ?>) obj;

		if (isLeft != other.isLeft) return false;

		if (leftVal == null) {
			if (other.leftVal != null) return false;
		} else if (!leftVal.equals(other.leftVal)) {
			return false;
		}

		if (rightVal == null) {
			if (other.rightVal != null) return false;
		} else if (!rightVal.equals(other.rightVal)) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return String.format("Either [leftVal='%s', rightVal='%s', isLeft=%s]", leftVal,
				rightVal, isLeft);
	}
}
