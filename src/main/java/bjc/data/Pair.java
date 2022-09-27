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

import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import bjc.funcdata.theory.Bifunctor;

/**
 * Represents a pair of values.
 *
 * @author ben
 *
 * @param <LeftType>
 *                    The type of the left side of the pair.
 *
 * @param <RightType>
 *                    The type of the right side of the pair.
 *
 */
public interface Pair<LeftType, RightType> extends Bifunctor<LeftType, RightType>, Formattable {
	/**
	 * Bind a function across the values in this pair.
	 *
	 * @param <BoundLeft>
	 *                     The type of the bound left.
	 *
	 * @param <BoundRight>
	 *                     The type of the bound right.
	 *
	 * @param binder
	 *                     The function to bind with.
	 *
	 * @return The bound pair.
	 */
	public <BoundLeft, BoundRight> Pair<BoundLeft, BoundRight>
			bind(BiFunction<LeftType, RightType, Pair<BoundLeft, BoundRight>> binder);

	/**
	 * Bind a function to the left value in this pair.
	 *
	 * @param <BoundLeft>
	 *                    The type of the bound value.
	 *
	 * @param leftBinder
	 *                    The function to use to bind.
	 *
	 * @return A pair with the left type bound.
	 */
	public <BoundLeft> Pair<BoundLeft, RightType>
			bindLeft(Function<LeftType, Pair<BoundLeft, RightType>> leftBinder);

	/**
	 * Bind a function to the right value in this pair.
	 *
	 * @param <BoundRight>
	 *                     The type of the bound value.
	 *
	 * @param rightBinder
	 *                     The function to use to bind.
	 *
	 * @return A pair with the right type bound.
	 */
	public <BoundRight> Pair<LeftType, BoundRight>
			bindRight(Function<RightType, Pair<LeftType, BoundRight>> rightBinder);

	/**
	 * Pairwise combine two pairs together.
	 *
	 * @param <OtherLeft>
	 *                     The left type of the other pair.
	 *
	 * @param <OtherRight>
	 *                     The right type of the other pair.
	 *
	 * @param otherPair
	 *                     The pair to combine with.
	 *
	 * @return The pairs, pairwise combined together.
	 */
	public default <OtherLeft, OtherRight>
			Pair<Pair<LeftType, OtherLeft>, Pair<RightType, OtherRight>>
			combine(final Pair<OtherLeft, OtherRight> otherPair) {
		return combine(otherPair,
				SimplePair<LeftType, OtherLeft>::new,
				SimplePair<RightType, OtherRight>::new);
	}

	/**
	 * Combine the contents of two pairs together.
	 *
	 * @param <OtherLeft>
	 *                        The type of the left value of the other pair.
	 *
	 * @param <OtherRight>
	 *                        The type of the right value of the other pair.
	 *
	 * @param <CombinedLeft>
	 *                        The type of the left value of the combined pair.
	 *
	 * @param <CombinedRight>
	 *                        The type of the right value of the combined pair.
	 *
	 * @param otherPair
	 *                        The other pair to combine with.
	 *
	 * @param leftCombiner
	 *                        The function to combine the left values with.
	 *
	 * @param rightCombiner
	 *                        The function to combine the right values with.
	 *
	 * @return A pair with its values combined.
	 */
	public <OtherLeft, OtherRight, CombinedLeft, CombinedRight>
			Pair<CombinedLeft, CombinedRight>
			combine(Pair<OtherLeft, OtherRight> otherPair,
					BiFunction<LeftType, OtherLeft, CombinedLeft> leftCombiner,
					BiFunction<RightType, OtherRight, CombinedRight> rightCombiner);

	/**
	 * Immediately perfom the specified action with the contents of this pair.
	 *
	 * @param consumer
	 *                 The action to perform on the pair.
	 */
	public default void doWith(final BiConsumer<LeftType, RightType> consumer) {
		merge((leftValue, rightValue) -> {
			consumer.accept(leftValue, rightValue);

			return null;
		});
	}

	@Override
	default <OldLeft, OldRight, NewLeft> LeftBifunctorMap<OldLeft, OldRight, NewLeft>
			fmapLeft(final Function<OldLeft, NewLeft> func) {
		return argumentPair -> {
			if (!(argumentPair instanceof Pair<?, ?>)) {
				final String msg
						= "This function can only be applied to instances of IPair";

				throw new IllegalArgumentException(msg);
			}

			final Pair<OldLeft, OldRight> argPair
					= (Pair<OldLeft, OldRight>) argumentPair;

			return argPair.mapLeft(func);
		};
	}

	@Override
	default <OldLeft, OldRight, NewRight> RightBifunctorMap<OldLeft, OldRight, NewRight>
			fmapRight(final Function<OldRight, NewRight> func) {
		return argumentPair -> {
			if (!(argumentPair instanceof Pair<?, ?>)) {
				final String msg
						= "This function can only be applied to instances of IPair";

				throw new IllegalArgumentException(msg);
			}

			final Pair<OldLeft, OldRight> argPair
					= (Pair<OldLeft, OldRight>) argumentPair;

			return argPair.mapRight(func);
		};
	}

	/**
	 * Get the value on the left side of the pair.
	 *
	 * @return The value on the left side of the pair.
	 */
	@Override
	public default LeftType getLeft() {
		return merge((leftValue, rightValue) -> leftValue);
	}

	/**
	 * Get the value on the right side of the pair.
	 *
	 * @return The value on the right side of the pair.
	 */
	@Override
	public default RightType getRight() {
		return merge((leftValue, rightValue) -> rightValue);
	}

	/**
	 * Transform the value on the left side of the pair.
	 *
	 * Doesn't modify the pair.
	 *
	 * @param <NewLeft>
	 *                  The new type of the left part of the pair.
	 *
	 * @param mapper
	 *                  The function to use to transform the left part of the pair.
	 *
	 * @return The pair, with its left part transformed.
	 */
	public <NewLeft> Pair<NewLeft, RightType>
			mapLeft(Function<LeftType, NewLeft> mapper);

	/**
	 * Transform the value on the right side of the pair.
	 *
	 * Doesn't modify the pair.
	 *
	 * @param <NewRight>
	 *                   The new type of the right part of the pair.
	 *
	 * @param mapper
	 *                   The function to use to transform the right part of the
	 *                   pair.
	 *
	 * @return The pair, with its right part transformed.
	 */
	public <NewRight> Pair<LeftType, NewRight>
			mapRight(Function<RightType, NewRight> mapper);

	/**
	 * Merge the two values in this pair into a single value.
	 *
	 * @param <MergedType>
	 *                     The type of the single value.
	 *
	 * @param merger
	 *                     The function to use for merging.
	 *
	 * @return The pair, merged into a single value.
	 */
	public <MergedType> MergedType
			merge(BiFunction<LeftType, RightType, MergedType> merger);

	/**
	 * Static pair constructor.
	 * 
	 * @param <Left> The type of the left side.
	 * @param <Right> The type of the right side.
	 * @param left The left side of the pair.
	 * @param right The right side of the pair.
	 * @return A pair, with the specified left/right side.
	 */
	public static <Left, Right> Pair<Left, Right> pair(Left left, Right right) {
		return new SimplePair<>(left, right);
	}
	
	@Override
	default void formatTo(Formatter formatter, int flags, int width, int precision) {
		if ((flags & FormattableFlags.ALTERNATE) != 0) {
			formatter.format("(%s, %s)", getLeft(), getRight());
		} else {
			formatter.format("Pair [l=%s, r=%s", getLeft(), getRight());
		}
	}
}
