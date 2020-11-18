package bjc.funcdata;

import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("javadoc")
public class TestMapCreation {
	@Test
	public void mapOfNothingCreatesEmptyMap() {
		IMap<String, String> map = IMap.of();
		
		assertEquals("Map is empty", 0, map.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void mapOfMismatchedCountErrors() {
		@SuppressWarnings("unused")
		IMap<String, String> map = IMap.of("thing1");
	}
	
	@Test(expected = ClassCastException.class) 
	public void mapOfMismatchedTypeErrors() {
		IMap<String, String> map = IMap.of(1, 1.0);
		
		map.forEach((key, val) -> {
			// An exception will be thrown here
		});
	}
	
	@Test
	public void mapOfCreatesWithGivenContents() {
		IMap<String, String> map = IMap.of("a", "A", "b", "B");
		
		assertEquals("Constructed map contains key 'a'", "a", map.containsKey("a"));
		assertEquals("Constructed map has key 'a' mapped to value 'A'", "A", map.get("A"));
	}
}
