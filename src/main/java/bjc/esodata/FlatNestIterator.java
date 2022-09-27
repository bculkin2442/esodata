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

import java.util.*;

import bjc.data.*;

final class FlatNestIterator<Element> implements ListIterator<Element>
{
	private Deque<ListIterator<Either<Element, NestList<Element>>>> iterators;
	
	public FlatNestIterator(ListIterator<Either<Element, NestList<Element>>> iterator)
	{
		this.iterators = new ArrayDeque<>();
		this.iterators.add(iterator);
	}

	@Override
	public boolean hasNext() {
		boolean result = false;
		do {
			result = iterators.peek().hasNext();
			
			if (result == false) iterators.pop();
		} while (result == false && iterators.isEmpty() == false);
		
		return result;
	}

	@Override
	public Element next() {
		Holder<Element> element = Holder.of(null);
		do {
			element = iterators.peek().next().extract((ele) -> {
				return Holder.of(ele);
			}, (lst) -> {
				// Ignore empty sublists.
				if (lst.size() != 0) iterators.push(lst.listIterator());
				
				return null;
			});
		} while (element == null && hasNext());

		if (element == null) throw new NoSuchElementException();
		
		return element.getValue();
	}

	@Override
	public boolean hasPrevious() {
		boolean result = false;
		do {
			result = iterators.peek().hasPrevious();
			
			if (result == false) iterators.pop();
		} while (result == false && iterators.isEmpty() == false);
		
		return result;
	}

	@Override
	public Element previous() {
		Holder<Element> element = Holder.of(null);
		do {
			element = iterators.peek().previous().extract((ele) -> {
				return Holder.of(ele);
			}, (lst) -> {
				// Ignore empty sublists.
				if (lst.size() != 0) iterators.push(lst.listIterator());
				
				return null;
			});
		} while (element == null && hasPrevious());

		if (element == null) throw new NoSuchElementException();
		
		return element.getValue();
	}

	@Override
	public int nextIndex() {
		return iterators.peek().nextIndex();
	}

	@Override
	public int previousIndex() {
		return iterators.peek().previousIndex();
	}

	@Override
	public void remove() {
		iterators.peek().remove();
	}

	@Override
	public void set(Element e) {
		iterators.peek().set(Either.left(e));
	}

	@Override
	public void add(Element e) {
		iterators.peek().add(Either.left(e));
	}
}