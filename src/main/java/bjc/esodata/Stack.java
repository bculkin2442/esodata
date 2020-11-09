package bjc.esodata;

import java.util.*;

import java.util.function.*;

/*
 * @TODO 10/11/17 Ben Culkin :StackCombinators
 * 	Implement more combinators for the stack.
 */

/**
 * A stack, with support for forth/factor style stack combinators.
 *
 * <p>
 * <h2>Stack underflow</h2>
 * <p>
 * NOTE: In general, using any operation that attempts to remove more data from
 * the stack than exists will cause a {@link StackUnderflow} to be thrown. Check
 * the size of the stack if you want to avoid this.
 * <p>
 * </p>
 *
 * @param <T>
 *            The datatype stored in the stack.
 *
 * @author Ben Culkin
 */
public abstract class Stack<T> {
	/**
	 * The exception thrown when attempting to access an element from the stack that
	 * isn't there.
	 *
	 * @author EVE
	 */
	public static class StackUnderflow extends RuntimeException {
		/* The ID of the exception */
		private static final long serialVersionUID = 1423867176204571539L;
	}

	/**
	 * Push an element onto the stack.
	 *
	 * @param elm
	 *            The element to insert.
	 */
	public abstract void push(T elm);

	/**
	 * Pop an element off of the stack.
	 *
	 * @return The element on top of the stack.
	 */
	public abstract T pop();

	/**
	 * Retrieve the top element of this stack without removing it from the stack.
	 *
	 * @return The top element of this stack.
	 */
	public abstract T top();

	/**
	 * Get the number of elements in the stack.
	 *
	 * @return the number of elements in the stack.
	 */
	public abstract int size();

	/**
	 * Check if the stack is empty.
	 *
	 * @return Whether or not the stack is empty.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Create a spaghetti stack branching off of this one.
	 *
	 * @return A spaghetti stack with this stack as a parent.
	 */
	public Stack<T> spaghettify() {
		return new SpaghettiStack<>(this);
	}

	/*
	 * Multi-item add/remove.
	 */

	/**
	 * Push multiple elements onto the stack.
	 *
	 * @param elms
	 *             The elements to insert.
	 */
	public void pushAll(@SuppressWarnings("unchecked") T... elms) {
		for (T elm : elms) push(elm);
	}

	/**
	 * Push multiple elements onto the stack.
	 *
	 * @param elms
	 *             The elements to insert.
	 */
	public void pushAll(List<T> elms) {
		for (T elm : elms) push(elm);
	}

	/**
	 * Pop n items off of the stack and return them.
	 *
	 * @param n
	 *          The number of items to pop off of the stack.
	 *
	 * @return A list of the popped items, in the order they were popped.
	 */
	public List<T> multipop(int n) {
		List<T> lst = new LinkedList<>();

		for (int i = 0; i < n; i++) lst.add(pop());

		return lst;
	}

	/**
	 * Pop n items off of the stack and return them.
	 *
	 * @param n
	 *          The number of items to pop off of the stack.
	 *
	 * @return A list of the popped items, in the reverse order they were popped.
	 */
	public List<T> multipoprev(int n) {
		LinkedList<T> lst = new LinkedList<>();

		for (int i = 0; i < n; i++) lst.addFirst(pop());

		return lst;
	}

	/*
	 * Basic combinators
	 */

	/**
	 * Drop n items from the stack.
	 *
	 * @param n
	 *          The number of items to drop.
	 */
	public void drop(final int n) {
		for (int i = 0; i < n; i++) pop();
	}

	/** Drop one item from the stack. */
	public void drop() {
		drop(1);
	}

	/**
	 * Delete n items below the current one.
	 *
	 * @param n
	 *          The number of items below the top to delete.
	 */
	public void nip(final int n) {
		final T elm = pop();

		drop(n);

		push(elm);
	}

	/** Delete the second element in the stack. */
	public void nip() {
		nip(1);
	}

	/**
	 * Replicate the top n items of the stack m times.
	 *
	 * @param n
	 *          The number of items to duplicate.
	 *
	 * @param m
	 *          The number of times to duplicate items.
	 */
	public void multidup(final int n, final int m) {
		List<T> lst = multipoprev(n);

		for (int i = 0; i <= m; i++) pushAll(lst);
	}

	/**
	 * Duplicate the top n items of the stack.
	 *
	 * @param n
	 *          The number of items to duplicate.
	 */
	public void dup(final int n) {
		multidup(n, 1);
	}

	/** Duplicate the top item on the stack. */
	public void dup() {
		dup(1);
	}

	/**
	 * Replicate the n elements below the top one m times.
	 *
	 * @param n
	 *          The number of items to duplicate.
	 *
	 * @param m
	 *          The number of times to duplicate items.
	 */
	public void multiover(final int n, final int m) {
		T elm = pop();

		List<T> lst = multipoprev(n);

		for (final T nelm : lst) push(nelm);

		push(elm);

		for (int i = 0; i < m; i++) pushAll(lst);
	}

	/**
	 * Duplicate the n elements below the top one.
	 *
	 * @param n
	 *          The number of items to duplicate.
	 */
	public void over(final int n) {
		multiover(n, 1);
	}

	/** Duplicate the second item in the stack. */
	public void over() {
		over(1);
	}

	/** Duplicate the third item in the stack. */
	public void pick() {
		final T z = pop();
		final T y = pop();
		final T x = pop();

		push(x);
		push(y);
		push(z);
		push(x);
	}

	/**
	 * Rotate the n items m deep on the stack i positions.
	 *
	 * @param n
	 *          The number of items to rotate.
	 * @param m
	 *          The number of positions the item is down in the stack.
	 * @param i
	 *          The number of steps to rotate. Pass a negative number to rotate
	 *          things in the opposite direction.
	 */
	public void deepmultirot(int n, int m, int i) {
		List<T> kep = multipoprev(m);

		List<T> lst = multipoprev(n);

		Collections.rotate(lst, i);

		pushAll(lst);
		pushAll(kep);
	}

	/**
	 * Rotate the n items on top of the stack i positions.
	 *
	 * @param n
	 *          The number of items to rotate.
	 * @param i
	 *          The number of steps to rotate. Pass a negative number to rotate
	 *          things in the opposite direction.
	 */
	public void multirot(int n, int i) {
		deepmultirot(n, 0, i);
	}

	/** Swap the top two items on the stack. */
	public void swap() {
		multirot(2, 1);
	}

	/** Duplicate the second item below the first item. */
	public void deepdup() {
		final T y = pop();
		final T x = pop();

		push(x);
		push(x);
		push(y);
	}

	/** Swap the second and third items in the stack. */
	public void deepswap() {
		deepmultirot(2, 1, 1);
	}

	/**
	 * Rotate the top three items on the stack
	 */
	public void rot() {
		final T z = pop();
		final T y = pop();
		final T x = pop();

		push(y);
		push(z);
		push(x);
	}

	/** Inversely rotate the top three items on the stack */
	public void invrot() {
		final T z = pop();
		final T y = pop();
		final T x = pop();

		push(z);
		push(x);
		push(y);
	}

	/*
	 * Dataflow Combinators
	 */

	/**
	 * Hides the top n elements on the stack from an action.
	 *
	 * @param n
	 *               The number of elements to hide.
	 *
	 * @param action
	 *               The action to hide the elements from
	 */
	public void dip(final int n, final Consumer<Stack<T>> action) {
		List<T> elms = multipoprev(n);

		action.accept(this);

		pushAll(elms);
	}

	/**
	 * Hide the top element of the stack from an action.
	 *
	 * @param action
	 *               The action to hide the top from
	 */
	public void dip(final Consumer<Stack<T>> action) {
		dip(1, action);
	}

	/**
	 * Copy the top n elements on the stack, replacing them once an action is done.
	 *
	 * @param n
	 *               The number of elements to copy.
	 *
	 * @param action
	 *               The action to execute.
	 */
	public void keep(final int n, final Consumer<Stack<T>> action) {
		dup(n);

		dip(n, action);
	}

	/**
	 * Copy the first element on the stack, replacing them once an action is done.
	 *
	 * @param action
	 *               The action to execute.
	 */
	public void keep(final Consumer<Stack<T>> action) {
		keep(1, action);
	}

	/**
	 * Apply all the actions in a list to the top n elements of the stack.
	 *
	 * @param n
	 *                The number of elements to give to cons.
	 *
	 * @param actions
	 *                The actions to execute.
	 */
	public void multicleave(final int n, final List<Consumer<Stack<T>>> actions) {
		List<T> elms = multipoprev(n);

		for (final Consumer<Stack<T>> action : actions) {
			pushAll(elms);

			action.accept(this);
		}
	}

	/**
	 * Apply all the actions in a list to the top n elements of the stack.
	 *
	 * @param n
	 *                The number of elements to give to cons.
	 *
	 * @param actions
	 *                The actions to execute.
	 */
	public void multicleave(final int n, @SuppressWarnings("unchecked") final Consumer<Stack<T>>... actions) {
		List<T> elms = multipoprev(n);

		for (final Consumer<Stack<T>> action : actions) {
			pushAll(elms);

			action.accept(this);
		}
	}

	/**
	 * Apply all the actions in a list to the top element of the stack.
	 *
	 * @param actions
	 *                The actions to execute.
	 */
	public void cleave(final List<Consumer<Stack<T>>> actions) {
		multicleave(1, actions);
	}

	/**
	 * Apply all the actions in a list to the top element of the stack.
	 *
	 * @param actions
	 *                The actions to execute.
	 */
	public void cleave(@SuppressWarnings("unchecked") final Consumer<Stack<T>>... actions) {
		multicleave(1, actions);
	}

	/**
	 * Apply every action in a list of actions to n arguments.
	 *
	 * @param n
	 *                The number of parameters each action takes.
	 *
	 * @param actions
	 *                The actions to execute.
	 */
	public void multispread(final int n, final List<Consumer<Stack<T>>> actions) {
		List<List<T>> nelms = new LinkedList<>();

		for (int i = 0; i < actions.size(); i++) {
			List<T> elms = multipoprev(n);

			nelms.add(elms);
		}

		Iterator<Consumer<Stack<T>>> itr = actions.iterator();
		for (final List<T> elms : nelms) {
			pushAll(elms);

			itr.next().accept(this);
		}
	}

	/**
	 * Apply every action in a list of actions to n arguments.
	 *
	 * @param n
	 *                The number of parameters each action takes.
	 *
	 * @param actions
	 *                The actions to execute.
	 */
	public void multispread(final int n, @SuppressWarnings("unchecked") final Consumer<Stack<T>>... actions) {
		List<List<T>> nelms = new LinkedList<>();

		for (int i = 0; i < actions.length; i++) {
			List<T> elms = multipoprev(n);

			nelms.add(elms);
		}

		int i = 0;
		for (final List<T> elms : nelms) {
			pushAll(elms);

			actions[i++].accept(this);
		}
	}

	/**
	 * Apply the actions in a list of actions to corresponding elements from the
	 * stack.
	 *
	 * @param conses
	 *               The actions to execute.
	 */
	public void spread(final List<Consumer<Stack<T>>> conses) {
		multispread(1, conses);
	}

	/**
	 * Apply the actions in a list of actions to corresponding elements from the
	 * stack.
	 *
	 * @param conses
	 *               The actions to execute.
	 */
	public void spread(@SuppressWarnings("unchecked") final Consumer<Stack<T>>... conses) {
		multispread(1, conses);
	}

	/**
	 * Apply an action to the first m groups of n arguments.
	 *
	 * @param n
	 *               The number of arguments cons takes.
	 *
	 * @param m
	 *               The number of time to call cons.
	 *
	 * @param action
	 *               The action to execute.
	 */
	public void multiapply(final int n, final int m, final Consumer<Stack<T>> action) {
		final List<Consumer<Stack<T>>> actions = new ArrayList<>(m);

		for (int i = 0; i < m; i++) actions.add(action);

		multispread(n, actions);
	}

	/**
	 * Apply an action n times to the corresponding elements in the stack.
	 *
	 * @param n
	 *               The number of times to execute cons.
	 *
	 * @param action
	 *               The action to execute.
	 */
	public void apply(final int n, final Consumer<Stack<T>> action) {
		multiapply(1, n, action);
	}

	/*
	 * Misc. functions
	 */

	/**
	 * Get an array representing this stack.
	 *
	 * @return The stack as an array.
	 */
	public abstract T[] toArray();
}
