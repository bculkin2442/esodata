package bjc.functypes;

import java.util.function.Function;

import bjc.funcdata.ListEx;

/**
 * A function that flattens a list.
 *
 * @author bjculkin
 *
 * @param <S>
 *            The type of value in the list.
 */
public interface ListFlattener<S> extends Function<ListEx<S>, S> {
	/*
	 * Alias
	 */
}
