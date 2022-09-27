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

/**
 * Represents a library of possible tapes, with a single tape 'mounted' or active
 * at a time.
 * 
 * @author Ben Culkin
 *
 * @param <ElementType> The type stored on each tape.
 */
public class TapeLibrary<ElementType> implements TapeView<ElementType>
{
	private final Map<String, Tape<ElementType>> library;
	
	private String            currentLabel;
	private Tape<ElementType> currentTape;

	private boolean allowAutoCreation;
	
	/**
	 * Get a view of this tape library as a map.
	 * 
	 * Modifications to this map will apply to the library, but will not
	 * affect whether a given tape is mounted or not. Be forewarned.
	 * 
	 * @return A view onto this tape library as a map.
	 */
	public Map<String, Tape<ElementType>> asMap()
	{
		return library;
	}

	/**
	 * Create a new empty tape library, with no tape mounted.
	 */
	public TapeLibrary()
	{
		library = new HashMap<>();
	}

	/**
	 * Create a new tape, with a given tape mounted by default.
	 * 
	 * @param label The label of the tape.
	 * @param tape  The tape to mount.
	 */
	public TapeLibrary(String label, Tape<ElementType> tape)
	{
		this();
		library.put(label, tape);
		
		this.currentLabel = label;
		this.currentTape  = tape;
	}

	@Override
	public Tape<ElementType> tapeView()
	{
		return currentTape;
	}
	
	/**
	 * Insert a tape into this library.
	 * 
	 * @param label The label to use for the tape.
	 * @param tape The tape to add.
	 * 
	 * @return The tape which previously had that label, or null if there was not one.
	 */
	public Tape<ElementType> insertTape(String label, Tape<ElementType> tape)
	{
		return library.put(label, tape);
	}
	
	/**
	 * Remove a tape from this library.
	 * 
	 * @param label The label of the tape to remove.
	 * 
	 * @return The tape which had that label, or null if there was not one.
	 */
	public Tape<ElementType> removeTape(String label)
	{
		return library.remove(label);
	}
	
	/**
	 * Check if this library has a tape with a given label.
	 * 
	 * @param label The label of the tape to check for.
	 * 
	 * @return Whether or not the library contains a tape with that label.
	 */
	public boolean hasTape(String label) 
	{
		return allowAutoCreation ? true : library.containsKey(label);
	}
	
	/**
	 * Mount a different tape in the library.
	 * 
	 * @param label The label of the tape to mount.
	 * 
	 * @return True if the tape was successfully mounted, false otherwise.
	 */
	public boolean mountTape(String label)
	{
		if (library.containsKey(label) || allowAutoCreation)
		{
			currentLabel = label;
			currentTape  = library.computeIfAbsent(
						label,
						(ignored) -> new SingleTape<>());
			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the label of the current tape.
	 * 
	 * @return The label of the current tape, or null if no tape is
	 *         currently 'mounted'.
	 */
	public String currentLabel()
	{
		return currentLabel;
	}
	
	/**
	 * Unmount the currently mounted tape.
	 */
	public void ejectTape()
	{
		currentTape  = null;
		currentLabel = null;
	}
	
	/**
	 * Check if this tape library currently allows auto-creation of
	 * non-existing tapes.
	 * 
	 * @return Whether or not auto-creation of tapes is currently allowed.
	 */
	public boolean isAllowAutoCreation() {
		return allowAutoCreation;
	}
	
	/**
	 * Set whether or not this library allows auto-creation of non-existing
	 * tapes.
	 * 
	 * @param allowAutoCreation Whether tape auto-creation is allowed.
	 */
	public void setAllowAutoCreation(boolean allowAutoCreation)
	{
		this.allowAutoCreation = allowAutoCreation;
	}
}
