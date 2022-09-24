package bjc.funcdata;

public interface CListLike<T> {

	boolean isEmpty();

	T head();

	CListLike<T> tail();

	CListLike<T> prefix(T val);

}