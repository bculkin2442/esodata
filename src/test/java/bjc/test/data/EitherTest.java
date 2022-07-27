package bjc.test.data;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import bjc.data.Either;

public class EitherTest
{
	private Either<String, String> leftEither;
	private Either<String, String> rightEither;
	
	@Before
	public void setUp() throws Exception {
		leftEither  = Either.left("left");
		rightEither = Either.right("right");
	}

	@Test
	public void testIsLeft() {
		assertTrue("isLeft properly marks left eithers", leftEither.isLeft());
		assertFalse("isLeft properly marks right eithers", rightEither.isLeft());
	}
	
	@Test
	public void testGetLeft() {
		assertEquals("getLeft treats left eithers properly",
				Optional.of("left"), leftEither.getLeft());
		assertEquals("getLeft treats right eithers properly",
				Optional.empty(), rightEither.getLeft());
	}

}
