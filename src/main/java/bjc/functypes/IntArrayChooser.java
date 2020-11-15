package bjc.functypes;

/**
 * Int-specialized variant of ArrayChooser.
 * 
 * @author Ben Culkin
 *
 */
@FunctionalInterface
public interface IntArrayChooser {
	/**
	 * Choose a single int from an array of ints.
	 * 
	 * @param ints The array of ints to choose.
	 * 
	 * @return The chosen int.
	 */
	public int choose(int... ints);
}
