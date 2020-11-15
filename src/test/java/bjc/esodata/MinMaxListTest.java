package bjc.esodata;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

@SuppressWarnings("javadoc")
public class MinMaxListTest {
	private final static Comparator<Integer> intComparator = (lhs, rhs) -> lhs - rhs;;

	@Test
	public void minMaxListInitializesMinMax() {
		MinMaxList<Integer> list = new MinMaxList<>(intComparator,
				1, 2, 3, 4, 5);
		
		assertEquals("List contains 5 elements", 5, list.size());
		
		assertEquals("Minimum is 1", 1, (int)list.minimum());
		assertEquals("Maximum is 5", 5, (int)list.maximum());
	}
	
	@Test
	public void minMaxListAddUpdatesMinMax() {
		MinMaxList<Integer> list = new MinMaxList<>(intComparator,
				2, 3, 4);
		
		assertEquals("Minimum is 2", 2, (int)list.minimum());
		assertEquals("Maximum is 4", 4, (int)list.maximum());
		
		list.add(1);
		list.add(5);

		assertEquals("Minimum is 1", 1, (int)list.minimum());
		assertEquals("Maximum is 5", 5, (int)list.maximum());
	}
	
	public void minMaxListRemoveUpdatesMinMax() {
		MinMaxList<Integer> list = new MinMaxList<>(intComparator,
				1, 2, 3, 4, 5);

		assertEquals("Minimum is 1", 1, (int)list.minimum());
		assertEquals("Maximum is 5", 5, (int)list.maximum());
		
		list.remove((Integer)1);
		list.remove((Integer)5);
		
		assertEquals("Minimum is 2", 2, (int)list.minimum());
		assertEquals("Maximum is 4", 4, (int)list.maximum());
	}
}
