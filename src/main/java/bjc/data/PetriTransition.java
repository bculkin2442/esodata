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

import java.util.*;
import java.util.Map.Entry;

/**
 * Represents a transition in a Petri net
 * @author bjcul
 *
 * @param <Label> The type of the label
 */
public class PetriTransition<Label> {
	private final Label name;

	private Map<Label, Integer> sources;
	private Map<Label, Integer> destinations;

	/**
	 * Create a new transition
	 * @param name The name for this transition
	 */
	public PetriTransition(Label name) {
		this.name = name;

		sources = new HashMap<>();
		destinations = new HashMap<>();
	}

	/**
	 * Add a source to this transition
	 * 
	 * @param lab The label to move from
	 * @param tokens The number of tokens to move
	 */
	public void addSource(Label lab, int tokens) {
		sources.merge(lab, tokens, (k, v) -> v + tokens);
	}

	/**
	 * Remove a source from this transition
	 * 
	 * @param lab The source to remove
	 * @return The number of tokens that would've been moved
	 */
	public int removeSource(Label lab) {
		return sources.remove(lab);
	}

	/**
	 * Add a destination to this transition.
	 * 
	 * @param lab The label for the destination
	 * @param tokens The number of tokens to add
	 */
	public void addDestination(Label lab, int tokens) {
		destinations.merge(lab, tokens, (k, v) -> v + tokens);
	}

	/**
	 * Remove a destination from this transition
	 * 
	 * @param lab The destination to remove
	 * 
	 * @return The number of tokens which would've moved
	 */
	public int removeDestination(Label lab) {
		return destinations.remove(lab);
	}

	/**
	 * Apply this transition to a map
	 * 
	 * @param nodes The map to apply the transition to
	 * 
	 * @return Whether the transition was successfully applied.
	 */
	public boolean apply(Map<Label, Integer> nodes) {
		Set<Entry<Label, Integer>> debts = new HashSet<>();
		boolean failed = false;

		for (Entry<Label, Integer> source : sources.entrySet()) {
			Label lab = source.getKey();
			int cost = source.getValue();

			if (nodes.containsKey(lab)) {
				int count = nodes.get(lab);
				if (count > cost) {
					nodes.put(lab, count - cost);
				} else {
					failed = true;
					break;
				}
			} else {
				throw new IllegalArgumentException("Node " + lab + "missing; needed for transition " + name);
			}
		}

		// If we failed, we may need to re-deposit some tokens
		if (failed) {
			for (Entry<Label, Integer> debt : debts) {
				nodes.computeIfPresent(debt.getKey(), (k, v) -> v + debt.getValue());
			}
			return false;
		}

		// NOTE: One possibility would be to use the same loop for both of these, and
		// just negate sources by default.
		for (Entry<Label, Integer> destination : destinations.entrySet()) {
			Label lab = destination.getKey();
			int profit = destination.getValue();

			if (nodes.containsKey(lab)) {
				int count = nodes.get(lab);
				if (count > profit) {
					nodes.put(lab, count + profit);
				} else {
					failed = true;
					break;
				}
			} else {
				throw new IllegalArgumentException("Node " + lab + "missing; needed for transition " + name);
			}
		}

		return true;
	}
	
	/**
	 * Merge this transition with another one
	 * 
	 * @param transition The transition to merge
	 */
	public void merge(PetriTransition<Label> transition) {
		for (Entry<Label, Integer> source : transition.sources.entrySet()) {
			int val = source.getValue();
			sources.merge(source.getKey(), val, (k, v) -> v + val);
		}
		for (Entry<Label, Integer> destination : transition.destinations.entrySet()) {
			int val = destination.getValue();
			sources.merge(destination.getKey(), val, (k, v) -> v + val);
		}
	}
	@Override
	public String toString() {
		return String.format("PetriTransition [name=%s, sources=%s, destinations=%s]", name, sources, destinations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(destinations, name, sources);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PetriTransition<?> other = (PetriTransition<?>) obj;
		return Objects.equals(destinations, other.destinations) && Objects.equals(name, other.name)
				&& Objects.equals(sources, other.sources);
	}

	/**
	 * Create a transition from merging a bunch
	 * 
	 * @param <Label> The type of the label
	 * 
	 * @param mergeName The label for the merged transition
	 * @param transitions The transitions to merge
	 * 
	 * @return A transition created by merging the given ones
	 */
	@SafeVarargs
	public static <Label> PetriTransition<Label> merged(Label mergeName, PetriTransition<Label>... transitions) {
		PetriTransition<Label> result = new PetriTransition<>(mergeName);
		for (PetriTransition<Label> transition : transitions) {
			result.merge(transition);
		}
		return result;
	}
}