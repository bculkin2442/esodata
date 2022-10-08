/* 
 * esodata - data structures of varying utility
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

import java.util.*;
import java.util.function.Function;

/**
 * Various utilities for dealing w/ collections
 * @author bjcul
 *
 */
public class CollectionUtils {
	/**
	 * Create a function that converts a collection into a map.
	 * 
	 * @param <Val>   The type of the values in the map
	 * @param <Idx>   The type of the keys in the map
	 * @param <Ent>   The type of the values in the collection
	 * 
	 * @param keyFunc The function that determines keys
	 * @param valFunc The function that determines values
	 * 
	 * @return A function which uses the provided function to create a map from a
	 *         collection.
	 */
	public static <Val, Idx, Ent> Function<Collection<Ent>, Map<Idx, Val>> indexBy(Function<Ent, Idx> keyFunc,
			Function<Ent, Val> valFunc) {
		return (coll) -> {
			Map<Idx, Val> mep = new HashMap<>();

			for (Ent ent : coll) {
				mep.put(keyFunc.apply(ent), valFunc.apply(ent));
			}

			return mep;
		};
	}
}
