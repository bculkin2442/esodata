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
package bjc.esodata;

import static bjc.functypes.Combinators.*;

import java.util.*;
import java.util.function.*;

import bjc.data.*;

/**
 * A list which can contain sublists of itself.
 * 
 * N.B: Be careful if you form a recursive list, as there is no form of detection
 * in place for that. Some operations may work, but those that do a deep traversal
 * of the list will not.
 * 
 * @author Ben Culkin
 *
 * @param <Element> The type contained in the list.
 */
public class NestList<Element> extends AbstractList<Either<Element, NestList<Element>>>
{
	private final List<Either<Element, NestList<Element>>> backing;

	/**
	 * Create a new empty nesting list.
	 */
	public NestList() {
		backing = new ArrayList<>();
	}	
	
	/**
	 * Create a new empty nesting list with the given capacity.
	 * 
	 * @param cap The capacity for the nesting list.
	 */
	public NestList(int cap) {
		backing = new ArrayList<>(cap);
	}
	
	/**
	 * Add an element to this list.
	 * 
	 * @param element The element to add to the list.
	 * 
	 * @return Whether we could add the element.
	 */
	public boolean addItem(Element element) {
		return backing.add(Either.left(element));
	}
	
	/**
	 * Add a sublist to this list.
	 * 
	 * @param element The sublist to add to this list.
	 * 
	 * @return Whether we could add the sublist.
	 */
	public boolean addItem(NestList<Element> element) {
		return backing.add(Either.right(element));
	}
	/**
	 * Add elements to this list.
	 * 
	 * @param elements The elements to add to the list.
	 * 
	 * @return Whether we could add each element.
	 */
	public boolean[] addItems(@SuppressWarnings("unchecked") Element... elements) {
		boolean[] vals = new boolean[elements.length];
		
		for (int i = 0; i < vals.length; i++)
		{
			vals[i] = addItem(elements[i]);
		}
		
		return vals;
	}
	
	/**
	 * Add sublists to this list.
	 * 
	 * @param elements The sublists to add to this list.
	 * 
	 * @return Whether we could add each sublist.
	 */
	public boolean[] addItems(@SuppressWarnings("unchecked") NestList<Element>... elements) {
		boolean[] vals = new boolean[elements.length];
		
		for (int i = 0; i < vals.length; i++)
		{
			vals[i] = addItem(elements[i]);
		}
		
		return vals;
	}
	
	/**
	 * Add a sublist with the given elements to this list.
	 * 
	 * @param elements The elements of the sublist.
	 * 
	 * @return Whether or not we could add the sublist.
	 */
	public boolean addSublist(@SuppressWarnings("unchecked") Element... elements) {
		NestList<Element> container = new NestList<>(elements.length);
		
		for (Element ele : elements) {
			container.addItem(ele);
		}
		
		return addItem(container);
	}
	
	/**
	 * Return an iterator over a flattened version of this list.
	 * 
	 * N.B: In certain cases involving empty sublists, the hasNext() operation\
	 * may not be 100% accurate. Be warned.
	 * 
	 * @return An iterator over a flattened variant of this list.
	 */
	public ListIterator<Element> flatIterator() {
		return new FlatNestIterator<>(listIterator());
	}
	
	/**
	 * Flatten one level of nesting from this list.
	 * 
	 * @return The list with one level of nesting flattened.
	 */
	public NestList<Element> flatten() {
		NestList<Element> flatterList = new NestList<>(size());
		
		backing.forEach((element) -> 
			element.pick(flatterList::addItem,	flatterList::addAll)
		);
		
		return flatterList;
	}
	
	/**
	 * Flatten this list recursively.
	 * 
	 * @return A flattened form of this list.
	 */
	public List<Element> deepFlatten() {
		List<Element> flatList = new ArrayList<>();
		
		flatIterator().forEachRemaining(flatList::add);
		
		return flatList;
	}
	
	/**
	 * Get the total number of elements contained in this list and all sublists.
	 * 
	 * @return The total number of elements contained in this list.
	 */
	public int deepSize() {
		int size = 0;
		
		for (Either<Element, NestList<Element>> element : backing)
		{
			size += element.extract((ele) -> 1, (lst) -> lst.deepSize());
		}
		
		return size;
	}
	
	/**
	 * Replace all of the elements in this list in-place with transformed versions.
	 * 
	 * @param elementOperator The operator to apply to elements.
	 * @param listOperator The operator to apply to sublists.
	 */
	public void replace(
			UnaryOperator<Element> elementOperator,
			UnaryOperator<NestList<Element>> listOperator) {
		backing.replaceAll((ele) -> ele.map(elementOperator, listOperator));
	}
	
	/**
	 * Perform a shallow mapping over this list.
	 * 
	 * @param <NewElement> The new element type.
	 * 
	 * @param elementMapper The function to map elements.
	 * @param listMapper The function to map lists.
	 * 
	 * @return A new list containing the mapped elements.
	 */
	public <NewElement> NestList<NewElement> map(
			Function<Element, NewElement> elementMapper,
			Function<NestList<Element>, NestList<NewElement>> listMapper)
	{
		NestList<NewElement> nest = new NestList<>(backing.size());
		
		for (Either<Element, NestList<Element>> element : backing)
		{
			nest.add(element.map(elementMapper, listMapper));
		}
		
		return nest;
	}
	
	/**
	 * Perform a recursive mapping over this list.
	 * 
	 * @param <NewElement> The new element type.
	 * 
	 * @param mapper The element mapper.
	 * 
	 * @return A new list with the same structure, but transformed elements.
	 */
	public <NewElement> NestList<NewElement> deepMap(
			Function<Element, NewElement> mapper)
	{
		return map(mapper, (lst) -> lst.deepMap(mapper));
	}
	
	/**
	 * Perform a mapping on the list with controllable recursion.
	 * 
	 * Inspired by the function of the same name from Raku.
	 * 
	 * @param <NewElement> The new element type.
	 * 
	 * @param recurPredicate Determines whether to recur into a list or not.
	 * @param elementMapper The mapper on elements.
	 * @param listMapper The mapper on lists we aren't recursing into.
	 * 
	 * @return A new list with its elements mapped.
	 */
	public <NewElement> NestList<NewElement> duckMap(
			Predicate<NestList<Element>> recurPredicate,
			Function<Element, NewElement> elementMapper,
			Function<NestList<Element>, NestList<NewElement>> listMapper)
	{
		return map(
				elementMapper, 
				iftt(recurPredicate,
						(list) -> list.duckMap(
								recurPredicate, elementMapper, listMapper),
						listMapper
				)
		);
	}
	
	/**
	 * Perform a reduction over this list.
	 * 
	 * @param <Output> The type of the output value.
	 * 
	 * @param initial The initial state of the output value.
	 * @param elementFolder The function to fold elements with.
	 * @param listFolder The function to fold lists with.
	 * 
	 * @return The result of reducing the list.
	 */
	public <Output> Output reduce(
			Output initial,
			BiFunction<Output, Element, Output> elementFolder,
			BiFunction<Output, NestList<Element>, Output> listFolder)
	{
		Holder<Output> out = Holder.of(initial);
		for (Either<Element, NestList<Element>> item : backing)
		{
			out.transform((state) -> item.extract(
					(ele) -> elementFolder.apply(state, ele),
					(lst) -> listFolder.apply(state, lst))
			);
		}
		
		return out.getValue();
	}
	
	/**
	 * Perform a recursive reduction over this list.
	 * 
	 * @param <Output> The type of the output value.
	 * 
	 * @param initial The initial state of the output value.
	 * @param elementFolder The function to fold elements with.
	 * 
	 * @return The result of recursively reducing the list.
	 */
	public <Output> Output deepReduce(
			Output initial,
			BiFunction<Output, Element, Output> elementFolder)
	{
		return reduce(
				initial,
				elementFolder,
				(state, lst) -> lst.deepReduce(state, elementFolder));
	}
	
	/**
	 * Conditionally expand elements of this list into the provided list.
	 * 
	 * @param nest The list to expand elements into.
	 * @param expandPicker Picks whether or not to expand a list.
	 * @param recur Whether or not to recursively expand lists.
	 * 
	 * @return The provided list.
	 */
	public NestList<Element> expandInto(
			NestList<Element> nest,
			Predicate<NestList<Element>> expandPicker,
			boolean recur)
	{
		return reduce(nest,
				(state, item) -> with(state, (stat) -> stat.addItem(item)),
				(state, list) -> {	
					if (expandPicker.test(list)) {
						return list.reduce(nest,
								(substate, subitem) 
									-> with(substate,
											(subst) -> subst.addItem(subitem)),
								(substate, sublist) 
									-> with(substate, (subst) -> {												
										if (recur) {
											sublist.expandInto(
													subst,
													expandPicker,
													recur);
										} else {
											subst.addItem(sublist);
										}
								}));
					}
					
					state.addItem(list);
					return state;
		});
	}
	// List methods and other things.
	
	@Override
	public boolean add(Either<Element, NestList<Element>> e) {
		return backing.add(e);
	}

	@Override
	public void add(int index, Either<Element, NestList<Element>> element) {
		backing.add(index, element);
	}
	
	@Override
	public Either<Element, NestList<Element>> get(int index) {
		return backing.get(index);
	}

	@Override
	public boolean remove(Object o) {
		return backing.remove(o);
	}


	@Override
	public Either<Element, NestList<Element>> remove(int index) {
		return backing.remove(index);
	}

	@Override
	public int size() {
		return backing.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(backing);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)                  return true;
		if (!super.equals(obj))           return false;
		if (getClass() != obj.getClass()) return false;

		NestList<?> other = (NestList<?>) obj;
		
		return Objects.equals(backing, other.backing);
	}
}
