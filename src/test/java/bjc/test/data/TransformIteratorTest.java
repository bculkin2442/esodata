package bjc.test.data;

import static bjc.test.TestUtils.assertIteratorEquals;
import java.util.Iterator;

import org.junit.Test;

import bjc.data.ArrayIterator;
import bjc.data.TransformIterator;

@SuppressWarnings("javadoc")
public class TransformIteratorTest {

	@Test
	public void test() {
		Iterator<String> itr1 = new ArrayIterator<>("a", "b", "c");
		Iterator<String> itr2 = new TransformIterator<>(itr1, str -> str + "X");
		
		assertIteratorEquals(itr2, "aX", "bX", "cX");
	}

}
