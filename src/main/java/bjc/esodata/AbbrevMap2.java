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
		backing = new Multimap<>();
	}

	/**
	 * Add words to the map.
	 *
	 * @param words
	 *              The words to add to the map.
	 */
	public void add(String... words) {
		for (String word : words) {
			for (String substr : genAbbrevs(word)) {
				backing.add(substr, word);
			}
		}
	}

	// Generate all of the strings a given word could be abbreviated as
	private List<String> genAbbrevs(String word) {
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
			for (String substr : genAbbrevs(word)) {
				backing.remove(substr, word);
			}
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

		if (st.size() == 1) {
			return st.iterator().next();
		} else {
			return null;
		}
	}
}
