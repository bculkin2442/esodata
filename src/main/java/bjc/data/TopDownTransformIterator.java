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

import static bjc.data.TopDownTransformResult.RTRANSFORM;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/*
 * @TODO 10/11/17 Ben Culkin :TopDownStep
 *
 * 	Figure out what is broken with this, and fix it so that step-wise
 * 	iteration works correctly.
 */

/**
 * An iterative top-down transform of a tree.
 *
 * @author EVE
 *
 * @param <ContainedType>
 *                        The type of the nodes in the tree.
 */
public class TopDownTransformIterator<ContainedType>
		implements Iterator<Tree<ContainedType>> {
	/**
	 * Alias type for a tree transformation.
	 *
	 * @author student
	 *
	 * @param <ContainedType>
	 *                        The type contained in the tree.
	 */
	public interface TreeTransform<ContainedType> extends BiFunction<Tree<ContainedType>,
			Consumer<Iterator<Tree<ContainedType>>>, Tree<ContainedType>> {
		// Alias type; no body is needed
	}

	/*
	 * The function that picks how to transform a given node
	 */
	private final Function<ContainedType, TopDownTransformResult> picker;
	/*
	 * The transform to apply to a given node.
	 */
	private final TreeTransform<ContainedType> transform;

	private Tree<ContainedType> preParent;
	private Tree<ContainedType> postParent;

	private final Deque<Tree<ContainedType>> preChildren;
	private final Deque<Tree<ContainedType>> postChildren;

	private TopDownTransformIterator<ContainedType> curChild;

	private boolean done;
	private boolean initial;

	private final Deque<Iterator<Tree<ContainedType>>> toYield;
	private Iterator<Tree<ContainedType>> currYield;

	/**
	 * Create a new tree iterator.
	 *
	 * @param pickr
	 *                 The function to use to pick how to process nodes.
	 * @param transfrm
	 *                 The transform to apply to the nodes.
	 * @param tree
	 *                 The tree to transform.
	 */
	public TopDownTransformIterator(
			final Function<ContainedType, TopDownTransformResult> pickr,
			final TreeTransform<ContainedType> transfrm,
			final Tree<ContainedType> tree) {
		preParent = tree;

		preChildren = new LinkedList<>();
		postChildren = new LinkedList<>();
		toYield = new LinkedList<>();

		picker = pickr;
		transform = transfrm;

		done = false;
		initial = true;
	}

	/**
	 * Add a set of nodes to yield.
	 *
	 * @param src
	 *            The nodes to yield.
	 */
	public void addYield(final Iterator<Tree<ContainedType>> src) {
		if (currYield != null) toYield.push(currYield);

		currYield = src;
	}

	@Override
	public boolean hasNext() {
		return !done;
	}

	/**
	 * Get the next yielded value.
	 *
	 * @param val
	 *            The sentinel value to yield.
	 *
	 * @return The next yielded value.
	 */
	public Tree<ContainedType> flushYields(final Tree<ContainedType> val) {
		if (currYield != null) {
			/*
			 * We have non-sentinel values to yield.
			 */

			/*
			 * Add the sentinel to yield later.
			 */
			toYield.add(new SingleIterator<>(val));

			if (currYield.hasNext()) return currYield.next();

			while (toYield.size() != 0 && !currYield.hasNext()) {
				currYield = toYield.pop();
			}

			if (toYield.size() == 0 && !currYield.hasNext()) {
				currYield = null;
				return val;
			}

			return currYield.next();
		}

		return val;
	}

	@Override
	public Tree<ContainedType> next() {
		if (done) throw new NoSuchElementException();

		/*
		 * Flush any values that need to be yielded.
		 */
		if (currYield != null) {
			Tree<ContainedType> yeld = flushYields(null);

			if (yeld != null) return yeld;
		}

		if (initial) {
			/*
			 * Get the way we are transforming.
			 */
			final TopDownTransformResult res = picker.apply(preParent.getHead());

			switch (res) {
			case PASSTHROUGH:
				postParent = new SimpleTree<>(preParent.getHead());

				if (preParent.getChildrenCount() != 0) {
					for (int i = 0; i < preParent.getChildrenCount(); i++) {
						preChildren.add(preParent.getChild(i));
					}

					// Return whatever the first child is
					break;
				}

				done = true;
				return flushYields(postParent);
			case SKIP:
				done = true;
				return flushYields(preParent);
			case TRANSFORM:
				done = true;
				return flushYields(transform.apply(preParent, this::addYield));
			case RTRANSFORM:
				preParent = transform.apply(preParent, this::addYield);
				return flushYields(preParent);
			case PUSHDOWN:
				if (preParent.getChildrenCount() != 0) {
					for (int i = 0; i < preParent.getChildrenCount(); i++) {
						preChildren.add(preParent.getChild(i));
					}

					// Return whatever the first child is
					break;
				}

				done = true;
				return flushYields(
						transform.apply(new SimpleTree<>(preParent.getHead()), this::addYield));
			case PULLUP:
				final Tree<ContainedType> intRes
						= transform.apply(preParent, this::addYield);

				postParent = new SimpleTree<>(intRes.getHead());

				if (intRes.getChildrenCount() != 0) {
					for (int i = 0; i < intRes.getChildrenCount(); i++) {
						preChildren.add(intRes.getChild(i));
					}

					// Return whatever the first child is
					break;
				}

				done = true;
				return flushYields(postParent);
			default:
				throw new IllegalArgumentException("Unknown result type " + res);
			}

			if (res != RTRANSFORM) initial = false;
		}

		if (curChild == null || !curChild.hasNext()) {
			if (preChildren.size() != 0) {
				curChild = new TopDownTransformIterator<>(picker, transform,
						preChildren.pop());

				final Tree<ContainedType> res = curChild.next();
				// System.out.println("\t\tTRACE: adding node " + res + " to children");
				postChildren.add(res);

				return flushYields(res);
			}

			Tree<ContainedType> res = null;

			if (postParent == null) {
				res = new SimpleTree<>(preParent.getHead());

				// System.out.println("\t\tTRACE: adding nodes " + postChildren + " to " +
				// res);

				for (final Tree<ContainedType> child : postChildren) {
					res.addChild(child);
				}

				// res = transform.apply(res,
				// this::addYield);
			} else {
				res = postParent;

				// System.out.println("\t\tTRACE: adding nodes " + postChildren + " to " +
				// res);
				for (final Tree<ContainedType> child : postChildren) {
					res.addChild(child);
				}
			}

			done = true;
			return flushYields(res);
		}

		final Tree<ContainedType> res = curChild.next();
		// System.out.println("\t\tTRACE: adding node " + res + " to children");
		postChildren.add(res);

		return flushYields(res);
	}
}
