package bjc.data;

import java.util.*;
import java.util.function.*;

import bjc.functypes.ID;

/**
 * Represents a pair where only one side has a value.
 *
 * @author ben
 *
 * @param <LeftType>  The type that could be on the left.
 *
 * @param <RightType> The type that could be on the right.
 *
 */
public class Either<LeftType, RightType> {
	/**
	 * Create a new either with the left value occupied.
	 *
	 * @param <LeftType>  The type of the left value.
	 *
	 * @param <RightType> The type of the empty right value.
	 *
	 * @param left        The value to put on the left.
	 *
	 * @return An either with the left side occupied.
	 */
	public static <LeftType, RightType> Either<LeftType, RightType> left(final LeftType left) {
		return new Either<>(left, null);
	}

	/**
	 * Create a new either with the right value occupied.
	 *
	 * @param <LeftType>  The type of the empty left value.
	 *
	 * @param <RightType> The type of the right value.
	 *
	 * @param right       The value to put on the right.
	 *
	 * @return An either with the right side occupied.
	 */
	public static <LeftType, RightType> Either<LeftType, RightType> right(final RightType right) {
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

	/**
	 * Perform a mapping over this either.
	 * 
	 * @param <NewLeft>  The new left type.
	 * @param <NewRight> The new right type.
	 * 
	 * @param leftFunc   The function to apply if this is a left either.
	 * @param rightFunc  The function to apply if this is a right either.
	 * 
	 * @return A new either, containing a value transformed by the appropriate
	 *         function.
	 */
	public <NewLeft, NewRight> Either<NewLeft, NewRight> map(Function<LeftType, NewLeft> leftFunc,
			Function<RightType, NewRight> rightFunc) {
		return isLeft ? left(leftFunc.apply(leftVal)) : right(rightFunc.apply(rightVal));
	}

	/**
	 * Extract the value from this Either.
	 * 
	 * @param <Common>     The common type to extract.
	 * 
	 * @param leftHandler  The function to handle left-values.
	 * @param rightHandler The function to handle right-values.
	 * 
	 * @return The result of applying the proper function.
	 */
	public <Common> Common extract(Function<LeftType, Common> leftHandler, Function<RightType, Common> rightHandler) {
		return isLeft ? leftHandler.apply(leftVal) : rightHandler.apply(rightVal);
	}

	/**
	 * Perform an action on this either.
	 * 
	 * @param leftHandler  The handler of left values.
	 * @param rightHandler The handler of right values.
	 */
	public void pick(Consumer<LeftType> leftHandler, Consumer<RightType> rightHandler) {
		if (isLeft)
			leftHandler.accept(leftVal);
		else
			rightHandler.accept(rightVal);
	}

	/**
	 * Check if this either is left-aligned (has the left value filled, not the
	 * right value).
	 * 
	 * @return Whether this either is left-aligned.
	 */
	public boolean isLeft() {
		return isLeft;
	}

	/**
	 * Get the left value of this either if there is one.
	 * 
	 * @return An optional containing the left value, if there is one.
	 */
	public Optional<LeftType> getLeft() {
		return Optional.ofNullable(leftVal);
	}

	/**
	 * Get the left value of this either, or get a {@link NoSuchElementException} if
	 * there isn't one.
	 * 
	 * @return The left value of this either.
	 * 
	 * @throws NoSuchElementException If this either doesn't have a left value.
	 */
	public LeftType forceLeft() {
		if (isLeft) {
			return leftVal;
		}
		
		throw new NoSuchElementException("Either has no left value, is right value");
	}

	/**
	 * Get the right value of this either if there is one.
	 * 
	 * @return An optional containing the right value, if there is one.
	 */
	public Optional<RightType> getRight() {
		return Optional.ofNullable(rightVal);
	}

	/**
	 * Get the right value of this either, or get a {@link NoSuchElementException}
	 * if there isn't one.
	 * 
	 * @return The right value of this either.
	 * 
	 * @throws NoSuchElementException If this either doesn't have a right value.
	 */
	public RightType forceRight() {
		if (isLeft) {
			throw new NoSuchElementException("Either has no right value, has left value");
		}
		
		return rightVal;
	}

	@SuppressWarnings("unchecked")
	public <T> Either<LeftType, T> newRight() {
		if (isLeft) return (Either<LeftType, T>) this;
		
		throw new NoSuchElementException("Can't replace right type on right Either");
	}
	
	@SuppressWarnings("unchecked")
	public <T> Either<T, RightType> newLeft() {
		if (isLeft) 
			throw new NoSuchElementException("Can't replace left type on left Either");
		return (Either<T, RightType>) this;
	}
	
	public static <T> T collapse(Either<T, T> eth) {
		return eth.extract(ID.id(), ID.id());
	}
	// Misc. overrides

	@Override
	public int hashCode() {
		return Objects.hash(isLeft, leftVal, rightVal);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Either<?, ?> other = (Either<?, ?>) obj;

		return isLeft == other.isLeft && Objects.equals(leftVal, other.leftVal)
				&& Objects.equals(rightVal, other.rightVal);
	}

	@Override
	public String toString() {
		return String.format("Either [leftVal='%s', rightVal='%s', isLeft=%s]", leftVal, rightVal, isLeft);
	}
}
