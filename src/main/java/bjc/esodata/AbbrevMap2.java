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
 * A map that allows you to reference strings by unambiguous abbreviations to
 * them.
 *
 * One example is that adding the string 'abc' would allow you to get it back
 * with the following three keys
 * <ul>
 * <li>a</li>
 * <li>ab</li>
 * <li>abc</li>
 * </ul>
 *
 * @author Ben Culkin
 */
public class AbbrevMap2 {
	// Stores a mapping from strings, to strings that they could be abbreviations
	// for
	private Multimap<String, String> backing;

	/**
	 * Create a new abbreviation map.
	 */
	public AbbrevMap2() {
		backing = new TSetMultimap<>();
	}

	/**
	 * Add words to the map.
	 *
	 * @param words
	 *              The words to add to the map.
	 */
	public void add(String... words) {
		for (String word : words) {
			for (String substr : genAbbrevs(word)) backing.add(substr, word);
		}
	}

	// Generate all of the strings a given word could be abbreviated as
	private static List<String> genAbbrevs(String word) {
		List<String> retList = new ArrayList<>();

		int len = word.length();

		for (int i = 1; i <= len; i++) {
			String substr = word.substring(0, i);

			retList.add(substr);
		}

		return retList;
	}

	/**
	 * Remove words from the map.
	 *
	 * @param words
	 *              The words to remove from the map.
	 */
	public void removeWords(String... words) {
		for (String word : words) {
			for (String substr : genAbbrevs(word)) backing.remove(substr, word);
		}
	}

	/**
	 * Get all of the strings that a string could be an abbreviation for.
	 *
	 * @param word
	 *             The word to attempt to deabbreviate.
	 *
	 * @return All of the possible deabbreviations for that word.
	 */
	public Set<String> deabbrevAll(String word) {
		return backing.get(word);
	}

	/**
	 * Get the unambiguous thing the string is an abbreviation for.
	 *
	 * @param word
	 *             The word to attempt to deabbreviate.
	 *
	 * @return The unambiguous deabbreviation of the string, or null if there isn't
	 *         one.
	 */
	public String deabbrev(String word) {
		Set<String> st = backing.get(word);

		if (st.size() == 1) return st.iterator().next();
		else                return null;
	}
}
