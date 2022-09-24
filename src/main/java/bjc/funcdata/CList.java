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
