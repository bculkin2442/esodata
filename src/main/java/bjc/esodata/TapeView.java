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

/**
 * An interface which allows you to view a given type as a tape.
 * 
 * @author Ben Culkin
 *
 * @param <ElementType> The type of element contained in the tape.
 */
public interface TapeView<ElementType> extends Tape<ElementType>
{
	/**
	 * Return a view of this object as a tape.
	 * 
	 * @return A view of this object as a tape.
	 */
	public Tape<ElementType> tapeView();
	
	@Override
	public default ElementType item()
	{
		return tapeView().item();
	}

	@Override
	public default void item(ElementType itm)
	{
		tapeView().item(itm);
	}

	@Override
	public default int size()
	{
		return tapeView().size();
	}

	@Override
	public default int position()
	{
		return tapeView().position();
	}

	@Override
	public default void insertBefore(ElementType itm)
	{
		tapeView().insertBefore(itm);
	}

	@Override
	public default void insertAfter(ElementType itm)
	{
		tapeView().insertAfter(itm);
	}

	@Override
	public default ElementType remove()
	{
		return tapeView().remove();
	}

	@Override
	public default void first()
	{
		tapeView().first();
	}

	@Override
	public default void last()
	{
		tapeView().last();
	}

	@Override
	public default boolean left()
	{
		return tapeView().left();
	}

	@Override
	public default boolean left(int amt)
	{
		return tapeView().left(amt);
	}

	@Override
	public default boolean right()
	{
		return tapeView().right();
	}

	@Override
	public default boolean right(int amt)
	{
		return tapeView().right(amt);
	}

	@Override
	public default boolean seekTo(int pos)
	{
		return tapeView().seekTo(pos);
	}
}
