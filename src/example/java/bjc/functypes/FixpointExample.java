package bjc.functypes;

import static bjc.functypes.Combinators.*;

import java.util.function.*;

/**
 * Examples about how fixpoints work.
 * 
 * @author Ben Culkin
 *
 */
public class FixpointExample
{
	/**
	 * Main method.
	 * 
	 * @param args Unused CLI args.
	 */
	public static void main(String[] args) {
		Function<Integer, Integer> factorial = Fixpoints.fix((input, self) -> {
			if (input <= 1) return 1;
			else            return input * self.apply(input - 1);
		});

		times(10, andThen(factorial::apply, System.out::println));
	}
}