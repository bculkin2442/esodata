package bjc.functypes;

import java.util.function.Function;

/**
 * A pair of functions to transform between a pair of types.
 *
 * @author bjculkin
 *
 * @param <Source>
 *                 The source type of the isomorphism.
 *
 * @param <Dest>
 *                 The destination type of isomorphism.
 */
public class FunctionalIsomorphism<Source, Dest> implements Isomorphism<Source, Dest>
{
	/* The function to the destination type. */
	private Function<Source, Dest> toFunc;
	/* The function to the source type. */
	private Function<Dest, Source> fromFunc;

	/**
	 * Create a new isomorphism.
	 *
	 * @param to
	 *             The 'forward' function, from the source to the definition.
	 *
	 * @param from
	 *             The 'backward' function, from the definition to the source.
	 */
	public FunctionalIsomorphism(Function<Source, Dest> to,
			Function<Dest, Source> from)
	{
		toFunc = to;
		fromFunc = from;
	}

	@Override
	public Dest to(Source val)
	{
		return toFunc.apply(val);
	}

	@Override
	public Source from(Dest val)
	{
		return fromFunc.apply(val);
	}
}
