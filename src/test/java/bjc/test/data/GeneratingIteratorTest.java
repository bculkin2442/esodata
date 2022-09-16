package bjc.test.data;

import static bjc.test.TestUtils.assertIteratorEquals;
import java.util.Iterator;

import org.junit.Test;

import bjc.data.GeneratingIterator;

@SuppressWarnings("javadoc")
public class GeneratingIteratorTest {

	@Test
	public void test() {
		Iterator<String> itr = new GeneratingIterator<>("", str -> str + "A", str -> str.equalsIgnoreCase("AAAA"));
		
		assertIteratorEquals(false, itr, "A", "AA", "AAA", "AAAA");
	}

}
