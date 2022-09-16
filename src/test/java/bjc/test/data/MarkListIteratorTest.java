package bjc.test.data;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import bjc.data.MarkListIterator;
import bjc.test.TestUtils;

/**
 * Tests for {@link MarkListIterator}
 * @author bjcul
 *
 */
@SuppressWarnings("javadoc")
public class MarkListIteratorTest {

	@Test
	public void test() {
		List<String> list1 = Arrays.asList("a", "b", "c");
		
		MarkListIterator<String> itr1 = new MarkListIterator<>(list1);
		itr1.mark();
		
		assertFalse(itr1.hasPrevious());
		assertTrue(itr1.hasNext());
		TestUtils.assertIteratorEquals(itr1, "a", "b", "c");
		
		assertTrue(itr1.hasPrevious());
		assertFalse(itr1.hasNext());
		
		itr1.rollback(false);
		
		assertFalse(itr1.hasPrevious());
		assertTrue(itr1.hasNext());
		TestUtils.assertIteratorEquals(itr1, "a", "b", "c");
	}

}
