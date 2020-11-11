package bjc.data;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test Either
 * @author Ben Culkin
 *
 */
public class EitherTest {

	/**
	 * Do a test of Either.
	 */
	@Test
	public void test() {
		Either<String, String> left = Either.left("left");
		Either<String, String> right = Either.right("right");
		
		assertNotEquals(left, right);
	}

}
