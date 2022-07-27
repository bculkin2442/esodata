package bjc.esodata;

import java.util.*;

import bjc.data.Pair;
import bjc.data.TransformIterator;
import bjc.funcdata.FunctionalList;
import bjc.funcdata.ListEx;

/**
 * A labeled tree, where you can reference sub-nodes by their label as long as
 * the reference is unambiguous.
 *
 * Inspired by the way that you can reference COBOL members by their name, as
 * long as it is unambiguous. If it is ambiguous, you can instead use parent
 * nodes to disambiguate.
 *
 * Additional note: The base iterator will give you all of the child nodes, but
 * in no defined order.
 * 
 * @param <Label>     The label on each node
 * @param <Contained> The type of data contained in the nodes.
 */
public class AbbrevTree<Label, Contained> implements Iterable<Pair<Label, Contained>> {
	private Multimap<Label, AbbrevTree<Label, Contained>> labelledNodes;

	private Map<Label, AbbrevTree<Label, Contained>> children;
	private AbbrevTree<Label, Contained> parent;

	private Contained data;
	private Label label;

	/**
	 * Create a new empty root AbbrevTree.
	 */
	public AbbrevTree() {
		labelledNodes = new Multimap<>();
		children = new HashMap<>();
	}

	/**
	 * Create a new occupied root AbbrevTree
	 * 
	 * @param label The label for this tree
	 * @param data  The data for this tree
	 */
	public AbbrevTree(Label label, Contained data) {
		this();

		this.label = label;
		this.data = data;
	}

	/**
	 * Create a new empty child AbbrevTree.
	 * 
	 * @param parent The parent of this node
	 */
	public AbbrevTree(AbbrevTree<Label, Contained> parent) {
		labelledNodes = new Multimap<>();
		children = new HashMap<>();

		this.parent = parent;
	}

	/**
	 * Create a new occupied child AbbrevTree
	 * 
	 * @param parent The parent of this node
	 * @param label  The label for this tree
	 * @param data   The data for this tree
	 */
	public AbbrevTree(AbbrevTree<Label, Contained> parent, Label label, Contained data) {
		this();

		this.parent = parent;
		this.label = label;
		this.data = data;

		addFromChild(label, this);
	}

	private void addFromChild(Label lbl, AbbrevTree<Label, Contained> node) {
		labelledNodes.add(lbl, node);

		if (parent != null)
			parent.addFromChild(lbl, node);
	}

	/**
	 * Get the data contained in this node.
	 * 
	 * @return The contained data.
	 */
	public Contained getData() {
		return data;
	}

	/**
	 * Set the data contained in this node.
	 * 
	 * @param data The new data.
	 */
	public void setData(Contained data) {
		this.data = data;
	}

	/**
	 * Get the label for this node.
	 * 
	 * @return The label for this node.
	 */
	public Label getLabel() {
		return label;
	}

	/*
	 * Unsupported for now. This requires some additional scaffolding.
	 * 
	 * Set the label for this node.
	 * 
	 * @param label The new label for this node.
	 */
	// public void setLabel(Label label) {
	// this.label = label;
	// }

	/**
	 * Add a child to this node
	 * 
	 * @param key The label for the new node
	 * @param dat The data for the new node.
	 * 
	 * @return The new node
	 */
	public AbbrevTree<Label, Contained> add(Label key, Contained dat) {
		AbbrevTree<Label, Contained> node = new AbbrevTree<>(this, key, dat);

		children.put(key, node);

		return node;
	}

	/**
	 * Remove a direct child from this node.
	 * 
	 * @param key The label for this child.
	 * 
	 * @return The removed child.
	 */
	public Optional<AbbrevTree<Label, Contained>> removeChild(Label key) {
		Optional<AbbrevTree<Label, Contained>> res = Optional.ofNullable(children.remove(key));

		res.ifPresent((node) -> {
			node.parent = null;
			node.labelledNodes.iterator().forEachRemaining((par) -> {
				labelledNodes.remove(par.getLeft(), par.getRight());

				parent.labelledNodes.remove(par.getLeft(), par.getRight());
			});
		});

		return res;
	}

	/**
	 * Retrieve a number of subnodes from this tree which correspond to the given
	 * keys.
	 * 
	 * Note that the keys are passed in reverse order. Essentially, the first
	 * argument is the actual key, the remainder are just disambiguators
	 * 
	 * @param keys The keys to look up.
	 * 
	 * @return All of the nodes which match the given key pattern.
	 */
	public Set<AbbrevTree<Label, Contained>> nodes(@SuppressWarnings("unchecked") Label... keys) {
		// Need this; Java can't deduce the proper type for reduceAux otherwise
		Set<AbbrevTree<Label, Contained>> nodes = new HashSet<>();

		ListEx<Label> keyList = new FunctionalList<>(keys);

		// COBOL keylists are in reverse order
		keyList.reverse();

		Label last = keyList.popLast();

		List<AbbrevTree<Label, Contained>> focusList = List.of(this);
		for (Label key : keyList) {
			List<AbbrevTree<Label, Contained>> nextFocus = new ArrayList<>();

			for (AbbrevTree<Label, Contained> focus : focusList) {
				Set<AbbrevTree<Label, Contained>> focusSet = focus.labelledNodes.get(key);
				nextFocus.addAll(focusSet);
			}

			focusList = nextFocus;
		}

		focusList.forEach((focus) -> {
			nodes.addAll(focus.labelledNodes.get(last));
		});

		if (label.equals(last))
			nodes.add(this);

		return nodes;
	}

	/**
	 * Retrieve all of the values which correspond to a given key.
	 * 
	 * 
	 * Note that the keys are passed in reverse order. Essentially, the first
	 * argument is the actual key, the remainder are just disambiguators
	 * 
	 * @param keys The keys to look up
	 * 
	 * @return All of the values which correspond to the key
	 */
	public Set<Contained> values(@SuppressWarnings("unchecked") Label... keys) {
		Set<Contained> res = new HashSet<>();

		nodes(keys).forEach((node) -> res.add(node.data));

		return res;
	}

	/**
	 * Returns the singular value identified by the given keypath.
	 * 
	 * Note that unlike {@link AbbrevTree#nodes(Object...)} and
	 * {@link AbbrevTree#values(Object...)}, the keys to this method are passed in
	 * the proper order, not reverse.
	 * 
	 * @param keys The keypath to look up.
	 * 
	 * @return An optional containing the identified element if there is one;
	 *         otherwise, empty.
	 */
	public Optional<AbbrevTree<Label, Contained>> path(@SuppressWarnings("unchecked") Label... keys) {
		Optional<AbbrevTree<Label, Contained>> focus = Optional.of(this);

		for (Label key : keys) {
			focus.map((node) -> {
				return node.children.get(key);
			});
		}

		return focus;
	}

	@Override
	public Iterator<Pair<Label, Contained>> iterator() {
		return new TransformIterator<>(labelledNodes.iterator(),
				(node) -> node.mapRight(AbbrevTree<Label, Contained>::getData));
	}

	@Override
	public int hashCode() {
		return Objects.hash(children, data, label, parent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbbrevTree<?, ?> other = (AbbrevTree<?, ?>) obj;
		return Objects.equals(children, other.children) && Objects.equals(data, other.data)
				&& Objects.equals(label, other.label);
	}
}
