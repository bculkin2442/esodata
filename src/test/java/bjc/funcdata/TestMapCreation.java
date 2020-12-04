package bjc.funcdata;

import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("javadoc")
public class TestMapCreation {
	@Test
	public void mapOfNothingCreatesEmptyMap() {
		MapEx<String, String> map = MapEx.of();
		
		assertEquals("Map is empty", 0, map.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void mapOfMismatchedCountErrors() {
		@SuppressWarnings("unused")
		MapEx<String, String> map = MapEx.of("thing1");
	}
	
	@Test(expected = ClassCastException.class) 
	public void mapOfMismatchedTypeErrors() {
		MapEx<String, String> map = MapEx.of(1, 1.0);
		
		map.forEach((key, val) -> {
			// An exception will be thrown here
		});
	}
	
	@Test
	public void mapOfCreatesWithGivenContents() {
		MapEx<String, String> map = MapEx.of("a", "A", "b", "B");
		
		assertTrue("Constructed map contains key 'a'", map.containsKey("a"));
		assertEquals("Constructed map has key 'a' mapped to value 'A'", "A", map.get("a"));
	}
}
