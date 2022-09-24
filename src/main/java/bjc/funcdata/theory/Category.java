package bjc.funcdata.theory;

import java.util.Set;
import java.util.function.Function;

import bjc.data.Pair;
import bjc.esodata.PairMap;

// NOTE: haskell uses Category cat: ... where the parameter is the arrow type
public interface Category<Element, Arrow> {	
	Arrow identity(Element elem);
	Arrow compose(Arrow left, Arrow right);
}
// Java objects would form a category as Category<Class<?>, Function<?, ?>>

class FuncCategory implements Category<Class<?>, Function<?, ?>> {
	@Override
	public Function<?, ?> identity(Class<?> elem) {
		return (vl) -> vl;
	}

	@Override
	public Function<?, ?> compose(Function<?, ?> left, Function<?, ?> right) {
		// right.compose((Function<? super V, ?>) left);
		return null;
	}
	
}