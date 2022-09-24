package bjc.data;

import java.util.*;
import java.util.Map.Entry;

/**
 * Represents a Petri net, a device oft used for modeling concurrent processes.
 * 
 * @author bjcul
 *
 * @param <Label> The type labeling the nodes and transitions in this net.
 */
public class PetriNet<Label> {
	private Map<Label, Integer> nodes;
	private Map<Label, PetriTransition<Label>> transitions;

	public PetriNet() {
		nodes = new HashMap<>();
		transitions = new HashMap<>();
	}

	public void merge(PetriNet<Label> net) {
		for (Entry<Label, Integer> node : net.nodes.entrySet()) {
			int value = node.getValue();
			nodes.merge(node.getKey(), value, (k, v) -> v + value);
		}
		for (Entry<Label, PetriTransition<Label>> transition : net.transitions.entrySet()) {
			PetriTransition<Label> value = transition.getValue();
			transitions.merge(transition.getKey(), value, (k, v) -> {
				v.merge(value);
				return v;
			});
		}
	}
	
	public static <Label> PetriNet<Label> merged(PetriNet<Label>... nets) {
		PetriNet<Label> result = new PetriNet<>();

		for (PetriNet<Label> net : nets) {
			result.merge(net);
		}

		return result;
	}

	@Override
	public String toString() {
		return String.format("PetriNet [nodes=%s, transitions=%s]", nodes, transitions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nodes, transitions);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PetriNet<?> other = (PetriNet<?>) obj;
		return Objects.equals(nodes, other.nodes) && Objects.equals(transitions, other.transitions);
	}
}