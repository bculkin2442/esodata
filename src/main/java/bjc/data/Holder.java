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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import bjc.data.internals.BoundListHolder;
import bjc.data.internals.WrappedLazy;
import bjc.data.internals.WrappedOption;
import bjc.funcdata.FunctionalList;
import bjc.funcdata.theory.Functor;

/**
 * A holder of a single value.
 *
 * @author ben
 *
 * @param <ContainedType>
 *                        The type of value held.
 */
public interface Holder<ContainedType> extends Functor<ContainedType> {
	/**
	 * Bind a function across the value in this container.
	 *
	 * @param <BoundType>
	 *                    The type of value in this container.
	 *
	 * @param binder
	 *                    The function to bind to the value.
	 *
	 * @return A holder from binding the value.
	 */
	public <BoundType> Holder<BoundType>
			bind(Function<ContainedType, Holder<BoundType>> binder);

	/**
	 * Apply an action to the value.
	 *
	 * @param action
	 *               The action to apply to the value.
	 */
	public default void doWith(final Consumer<? super ContainedType> action) {
		transform(value -> {
			action.accept(value);

			return value;
		});
	}

	@Override
	default <ArgType, ReturnType> Function<Functor<ArgType>, Functor<ReturnType>>
			fmap(final Function<ArgType, ReturnType> func) {
		return argumentFunctor -> {
			if (!(argumentFunctor instanceof Holder<?>)) {
				final String msg
						= "This functor only supports mapping over instances of IHolder";

				throw new IllegalArgumentException(msg);
			}

			final Holder<ArgType> holder = (Holder<ArgType>) argumentFunctor;

			return holder.map(func);
		};
	}

	@Override
	public default ContainedType getValue() {
		return unwrap(value -> value);
	}

	/**
	 * Lifts a function to bind over this holder.
	 *
	 * @param <NewType>
	 *                  The type of the functions return.
	 *
	 * @param func
	 *                  The function to lift over the holder.
	 *
	 * @return The function lifted over the holder.
	 */
	public <NewType> Function<ContainedType, Holder<NewType>>
			lift(Function<ContainedType, NewType> func);

	/**
	 * Make this holder lazy.
	 *
	 * @return A lazy version of this holder.
	 */
	public default Holder<ContainedType> makeLazy() {
		return new WrappedLazy<>(this);
	}

	/**
	 * Make this holder a list.
	 *
	 * @return A list version of this holder.
	 */
	public default Holder<ContainedType> makeList() {
		return new BoundListHolder<>(new FunctionalList<>(this));
	}

	/**
	 * Make this holder optional.
	 *
	 * @return An optional version of this holder.
	 */
	public default Holder<ContainedType> makeOptional() {
		return new WrappedOption<>(this);
	}

	/**
	 * Create a new holder with a mapped version of the value in this holder.
	 *
	 * Does not change the internal state of this holder.
	 *
	 * @param <MappedType>
	 *                     The type of the mapped value.
	 *
	 * @param mapper
	 *                     The function to do mapping with.
	 *
	 * @return A holder with the mapped value
	 */
	public <MappedType> Holder<MappedType>
			map(Function<ContainedType, MappedType> mapper);

	/**
	 * Replace the held value with a new one.
	 *
	 * @param newValue
	 *                 The value to hold instead.
	 *
	 * @return The holder itself.
	 */
	public default Holder<ContainedType> replace(final ContainedType newValue) {
		return transform(oldValue -> newValue);
	}

	/**
	 * Transform the value held in this holder.
	 *
	 * @param transformer
	 *                    The function to transform the value with.
	 *
	 * @return The holder itself, for easy chaining.
	 */
	public Holder<ContainedType> transform(UnaryOperator<ContainedType> transformer);

	/**
	 * Unwrap the value contained in this holder so that it is no longer held.
	 *
	 * @param <UnwrappedType>
	 *                        The type of the unwrapped value.
	 *
	 * @param unwrapper
	 *                        The function to use to unwrap the value.
	 *
	 * @return The unwrapped held value.
	 */
	public <UnwrappedType> UnwrappedType
			unwrap(Function<ContainedType, UnwrappedType> unwrapper);
	
	/**
	 * Create an instace of IHolder containing a single value.
	 * 
	 * @param <ElementType> The type of the value contained.
	 * 
	 * @param contained The value to contain.
	 * 
	 * @return An instance of IHolder containing that value.
	 */
	static <ElementType> Holder<ElementType> of(ElementType contained) {
		return new Identity<>(contained);
	}
}
