package bjc.functypes;

import java.util.function.*;

/**
 * Denotes that two types are essentially equivalent, by providing a means to
 * convert between them.
 * 
 * @author Ben Culkin
 *
 * @param <Source> The first type.
 * @param <Dest> The second type.
 */
public interface Isomorphism<Source, Dest>
{

	/**
	 * Apply the isomorphism forward.
	 *
	 * @param val
	 *            The source value.
	 *
	 * @return The destination value.
	 */
	Dest to(Source val);

	/**
	 * Apply the isomorphism backward.
	 *
	 * @param val
	 *            The destination value.
	 *
	 * @return The source value.
	 */
	Source from(Dest val);
	
	/**
	 * Create an isomorphism from a pair of functions.
	 * 
	 * @param <Src> The source type.
	 * @param <Dst> The destination type.
	 * 
	 * @param forward The function from source to destination.
	 * @param backward The function from destination to source.
	 * 
	 * @return An isomorphism between the two types.
	 */
	static <Src, Dst> Isomorphism<Src, Dst> from(
			Function<Src, Dst> forward,
			Function<Dst, Src> backward)
	{
		return new FunctionalIsomorphism<>(forward, backward);
	}
}