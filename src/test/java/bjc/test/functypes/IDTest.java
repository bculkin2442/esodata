package bjc.test.functypes;

import static org.junit.Assert.*;

import java.util.function.*;

import org.junit.*;

import bjc.functypes.ID;

@SuppressWarnings("javadoc")
public class IDTest {

	@Test
	public void testID() {
		UnaryOperator<String> idOp = ID.id();
		
		assertEquals("hello", idOp.apply("hello"));
		
		assertEquals(1, ID.id().apply(1));
	}

}
