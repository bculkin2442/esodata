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
package bjc.funcdata;

import java.util.function.UnaryOperator;

import bjc.data.Either;
import bjc.data.Pair;
import bjc.functypes.Unit;

public class CList<T> implements CListLike<T> {
	private Either<Unit, Pair<T, CList<T>>> data;
	
	private CList() {
		data = Either.left(Unit.UNIT);
	}
	
	private CList(T data, CList<T> rest) {
		this.data = Either.right(Pair.pair(data, rest));
	}
	
	@Override
	public boolean isEmpty() {
		return data.isLeft();
	}
	
	@Override
	public T head() {
		return data.forceRight().getLeft();
	}
	
	@Override
	public CList<T> tail() {
		return data.forceRight().getRight();
	}
	
	@Override
	public CList<T> prefix(T val) {
		return new CList<>(val, this);
	}
	
	public static <T> UnaryOperator<CList<T>> prefixing(T val) {
		return (lst) -> lst.prefix(val);
	}
	
	public static <T> CList<T> empty() {
		return new CList<>();
	}
	public static <T> CList<T> of(T... vals) {
		CList<T> ret = empty();
		for (int i = vals.length - 1; i >= 0; i-- ) {
			ret = ret.prefix(vals[i]);
		}
		return ret;
	}
}
