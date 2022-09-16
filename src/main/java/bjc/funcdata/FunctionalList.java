package bjc.funcdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import bjc.data.Holder;
import bjc.data.Pair;
import bjc.data.Identity;
import bjc.data.SimplePair;

/**
 * A wrapper over another list that provides eager functional operations over
 * it.
 *
 * Differs from a stream in every way except for the fact that they both provide
 * functional operations.
 *
 * @author ben
 *
 * @param <E>
 *            The type in this list
 */
public class FunctionalList<E> implements Cloneable, ListEx<E> {
	/* The list used as a backing store */
	private final List<E> wrapped;

	/** Create a new empty functional list. */
	public FunctionalList() {
		wrapped = new ArrayList<>();
	}

	/**
	 * Create a new functional list containing the specified items.
	 *
	 * Takes O(n) time, where n is the number of items specified
	 *
	 * @param items
	 *              The items to put into this functional list.
	 */
	@SafeVarargs
	public FunctionalList(final E... items) {
		wrapped = new ArrayList<>(items.length);

		for (final E item : items) {
			wrapped.add(item);
		}
	}

	/**
	 * Create a new functional list containing the specified items.
	 *
	 * Takes O(n) time, where n is the number of items specified
	 *
	 * @param <T> The type of items to put into the list.
	 * 
	 * @param items The items to put into this functional list.
	 * 
	 * @return The returned list.
	 */
	@SafeVarargs
	public static <T> ListEx<T> listOf(final T... items) {
		return new FunctionalList<>(items);
	}

	/**
	 * Create a new functional list with the specified size.
	 *
	 * @param size The size of the backing list .
	 */
	private FunctionalList(final int size) {
		wrapped = new ArrayList<>(size);
	}

	/**
	 * Create a new functional list as a wrapper of a existing list.
	 *
	 * Takes O(1) time, since it doesn't copy the list.
	 *
	 * @param backing The list to use as a backing list.
	 */
	public FunctionalList(final List<E> backing) {
		if (backing == null)
			throw new NullPointerException("Backing list must be non-null");

		wrapped = backing;
	}

	@Override
	public boolean add(final E item) {
		return wrapped.add(item);
	}

	@Override
	public boolean allMatch(final Predicate<E> predicate) {
		if (predicate == null)
			throw new NullPointerException("Predicate must be non-null");

		for (final E item : wrapped) {
			if (!predicate.test(item))
				/* We've found a non-matching item. */
				return false;
		}

		/* All of the items matched. */
		return true;
	}

	@Override
	public boolean anyMatch(final Predicate<E> predicate) {
		if (predicate == null)
			throw new NullPointerException("Predicate must be not null");

		for (final E item : wrapped) {
			if (predicate.test(item))
				/* We've found a matching item. */
				return true;
		}

		/* We didn't find a matching item. */
		return false;
	}

	/**
	 * Clone this list into a new one, and clone the backing list as well.
	 *
	 * Takes O(n) time, where n is the number of elements in the list.
	 *
	 * @return A copy of the list.
	 */
	@Override
	public ListEx<E> clone() {
		final ListEx<E> cloned = new FunctionalList<>();

		for (final E element : wrapped) {
			cloned.add(element);
		}

		return cloned;
	}

	@Override
	public <T, F> ListEx<F> combineWith(final ListEx<T> rightList,
			final BiFunction<E, T, F> itemCombiner) {
		if (rightList == null) {
			throw new NullPointerException("Target combine list must not be null");
		} else if (itemCombiner == null) {
			throw new NullPointerException("Combiner must not be null");
		}

		final ListEx<F> returned = new FunctionalList<>();

		/* Get the iterator for the other list. */
		final Iterator<T> rightIterator = rightList.toIterable().iterator();

		for (final Iterator<E> leftIterator = wrapped.iterator();
				leftIterator.hasNext() && rightIterator.hasNext();) {
			/* Add the transformed items to the result list. */
			final E leftVal = leftIterator.next();
			final T rightVal = rightIterator.next();

			returned.add(itemCombiner.apply(leftVal, rightVal));
		}

		return returned;
	}

	@Override
	public boolean contains(final E item) {
		/* Check if any items in the list match the provided item. */
		return this.anyMatch(item::equals);
	}

	@Override
	public E first() {
		if (wrapped.size() < 1)
			throw new NoSuchElementException(
					"Attempted to get first element of empty list");

		return wrapped.get(0);
	}

	@Override
	public E last() {
		if (wrapped.size() < 1)
			throw new NoSuchElementException(
					"Attempted to get last element of empty list");

		return wrapped.get(wrapped.size() - 1);
	}

	@Override
	public E popFirst() {
		if (wrapped.size() < 1)
			throw new NoSuchElementException(
					"Attempted to pop first element of empty list");

		E head = first();
		wrapped.remove(0);

		return head;
	}

	@Override
	public E popLast() {
		if (wrapped.size() < 1)
			throw new NoSuchElementException(
					"Attempted to pop last element of empty list");

		E tail = last();
		wrapped.remove(wrapped.size() - 1);

		return tail;
	}

	@Override
	public <T> ListEx<T> flatMap(final Function<E, ListEx<T>> expander) {
		if (expander == null)
			throw new NullPointerException("Expander must not be null");

		final ListEx<T> returned = new FunctionalList<>(this.wrapped.size());

		forEach(element -> {
			final ListEx<T> expandedElement = expander.apply(element);

			if (expandedElement == null)
				throw new NullPointerException("Expander returned null list");

			/* Add each element to the returned list. */
			expandedElement.forEach(returned::add);
		});

		return returned;
	}

	@Override
	public void forEach(final Consumer<? super E> action) {
		if (action == null)
			throw new NullPointerException("Action is null");

		wrapped.forEach(action);
	}

	@Override
	public void forEachIndexed(final BiConsumer<Integer, E> indexedAction) {
		if (indexedAction == null)
			throw new NullPointerException("Action must not be null");

		/*
		 * This is held b/c ref'd variables must be final/effectively final.
		 */
		final Holder<Integer> currentIndex = new Identity<>(0);

		wrapped.forEach(element -> {
			/* Call the action with the index and the value. */
			indexedAction.accept(currentIndex.unwrap(index -> index), element);

			/* Increment the value. */
			currentIndex.transform((index) -> index + 1);
		});
	}

	@Override
	public E getByIndex(final int index) {
		return wrapped.get(index);
	}

	/**
	 * Get the internal backing list.
	 *
	 * @return The backing list this list is based off of.
	 */
	public List<E> getInternal() {
		return wrapped;
	}

	@Override
	public ListEx<E> getMatching(final Predicate<E> predicate) {
		if (predicate == null)
			throw new NullPointerException("Predicate must not be null");

		final ListEx<E> returned = new FunctionalList<>();

		wrapped.forEach(element -> {
			if (predicate.test(element)) {
				/*
				 * The item matches, so add it to the returned list.
				 */
				returned.add(element);
			}
		});

		return returned;
	}

	@Override
	public int getSize() {
		return wrapped.size();
	}

	@Override
	public boolean isEmpty() {
		return wrapped.isEmpty();
	}

	/* Check if a partition has room for another item. */
	private Boolean isPartitionFull(final int numberPerPartition,
			final Holder<ListEx<E>> currentPartition) {
		return currentPartition
				.unwrap(partition -> partition.getSize() >= numberPerPartition);
	}

	@Override
	public <T> ListEx<T> map(final Function<E, T> elementTransformer) {
		if (elementTransformer == null)
			throw new NullPointerException("Transformer must be not null");

		final ListEx<T> returned = new FunctionalList<>(this.wrapped.size());

		forEach(element -> {
			// Add the transformed item to the result
			returned.add(elementTransformer.apply(element));
		});

		return returned;
	}

	@Override
	public <T> ListEx<Pair<E, T>> pairWith(final ListEx<T> rightList) {
		return combineWith(rightList, SimplePair<E, T>::new);
	}

	@Override
	public ListEx<ListEx<E>> partition(final int numberPerPartition) {
		if (numberPerPartition < 1 || numberPerPartition > wrapped.size()) {
			final String fmt
					= "%s is an invalid partition size. Must be between 1 and %d";
			final String msg = String.format(fmt, numberPerPartition, wrapped.size());

			throw new IllegalArgumentException(msg);
		}

		final ListEx<ListEx<E>> returned = new FunctionalList<>();

		/* The current partition being filled. */
		final Holder<ListEx<E>> currentPartition = new Identity<>(new FunctionalList<>());

		this.forEach(element -> {
			if (isPartitionFull(numberPerPartition, currentPartition)) {
				/* Add the partition to the list. */
				returned.add(currentPartition.unwrap(partition -> partition));

				/* Start a new partition. */
				currentPartition.transform(partition -> new FunctionalList<>());
			} else {
				/* Add the element to the current partition. */
				currentPartition.unwrap(partition -> partition.add(element));
			}
		});

		return returned;
	}

	@Override
	public void prepend(final E item) {
		wrapped.add(0, item);
	}

	@Override
	public E randItem(final Function<Integer, Integer> rnd) {
		if (rnd == null)
			throw new NullPointerException("Random source must not be null");

		final int randomIndex = rnd.apply(wrapped.size());

		return wrapped.get(randomIndex);
	}

	@Override
	public <T, F> F reduceAux(final T initialValue,
			final BiFunction<E, T, T> stateAccumulator,
			final Function<T, F> resultTransformer) {
		if (stateAccumulator == null) {
			throw new NullPointerException("Accumulator must not be null");
		} else if (resultTransformer == null) {
			throw new NullPointerException("Transformer must not be null");
		}

		/* The current collapsed list. */
		final Holder<T> currentState = new Identity<>(initialValue);

		wrapped.forEach(element -> {
			/* Accumulate a new value into the state. */
			currentState.transform(state -> stateAccumulator.apply(element, state));
		});

		/* Convert the state to its final value. */
		return currentState.unwrap(resultTransformer);
	}

	@Override
	public boolean removeIf(final Predicate<E> removePredicate) {
		if (removePredicate == null)
			throw new NullPointerException("Predicate must be non-null");

		return wrapped.removeIf(removePredicate);
	}

	@Override
	public void removeMatching(final E desiredElement) {
		removeIf(element -> element.equals(desiredElement));
	}

	@Override
	public void reverse() {
		Collections.reverse(wrapped);
	}

	@Override
	public E search(final E searchKey, final Comparator<E> comparator) {
		/* Search our internal list. */
		final int foundIndex = Collections.binarySearch(wrapped, searchKey, comparator);

		if (foundIndex >= 0) {
			/* We found a matching element. */
			return wrapped.get(foundIndex);
		}

		/* We didn't find an element. */
		return null;
	}

	@Override
	public void sort(final Comparator<E> comparator) {
		Collections.sort(wrapped, comparator);
	}

	@Override
	public ListEx<E> tail() {
		return new FunctionalList<>(wrapped.subList(1, getSize()));
	}

	@Override
	public E[] toArray(final E[] arrType) {
		return wrapped.toArray(arrType);
	}

	@Override
	public Iterable<E> toIterable() {
		return wrapped;
	}

	@Override
	public String toString() {
		final int lSize = getSize();

		if (lSize == 0)
			return "()";

		final StringBuilder sb = new StringBuilder("(");
		final Iterator<E> itr = toIterable().iterator();
		final E itm = itr.next();
		int i = 0;

		if (lSize == 1)
			return "(" + itm + ")";

		for (final E item : toIterable()) {
			sb.append(item.toString());

			if (i < lSize - 1) {
				sb.append(", ");
			}

			i += 1;
		}

		sb.append(")");

		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(wrapped);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionalList<?> other = (FunctionalList<?>) obj;
		return Objects.equals(wrapped, other.wrapped);
	}
}
