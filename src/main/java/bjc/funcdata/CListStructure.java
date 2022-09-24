package bjc.funcdata;

import bjc.data.Either;

// pretty sure we want to implement CListLike, but not obvious how to implement
public class CListStructure<T> implements CListLike<Either<T, CListLike<T>>> {
	private Either<T, CList<CListStructure<T>>> data;

	public boolean isAtomic() {
		return data.isLeft();
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Either<T, CListLike<T>> head() {
		if (isAtomic()) return data.newRight();
		// ... Beats me
		return null;
	}

	@Override
	public CListLike<Either<T, CListLike<T>>> tail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CListLike<Either<T, CListLike<T>>> prefix(Either<T, CListLike<T>> val) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// There are almost certainly other methods we want here, just not sure what
}
