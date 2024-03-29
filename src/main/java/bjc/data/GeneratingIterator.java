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

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * An iterator that generates a series of elements from a single element.
 *
 * @author bjculkin
 *
 * @param <E>
 *            The type of element generated.
 */
public class GeneratingIterator<E> implements Iterator<E> {
	/* Our current state. */
	private E state;
	/* The function to use to transition states. */
	private UnaryOperator<E> transtion;
	/* The predicate to indicate where to stop. */
	private Predicate<E> stpper;

	/**
	 * Create a new generative iterator.
	 *
	 * @param initial
	 *                   The initial state of the generator.
	 *
	 * @param transition
	 *                   The function to apply to the state.
	 *
	 * @param stopper
	 *                   The predicate applied to the current state to determine
	 *                   when to stop.
	 */
	public GeneratingIterator(E initial, UnaryOperator<E> transition,
			Predicate<E> stopper) {
		state = initial;
		transtion = transition;
		stpper = stopper;
	}

	@Override
	public boolean hasNext() {
		return !stpper.test(state);
	}

	/*
	 * @NOTE
	 *
	 * As this currently is, it only works correctly assuming that next() is only
	 * called when hasNext() is true. Should we safeguard against people who are not
	 * doing the right thing?
	 */
	@Override
	public E next() {
		state = transtion.apply(state);

		return state;
	}
	
	/**
	 * Sets the state of this iterator.
	 * 
	 * @param newState The new state value.
	 * 
	 * @return The old state value.
	 */
	public E setState(E newState) {
		E oldState = this.state;
		this.state = newState;
		return oldState;
	}
}
