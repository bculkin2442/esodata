package bjc.funcdata;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

@SuppressWarnings("javadoc")
public class TestMapOperations {
	private MapEx<String, String> map;

	@Before
	public void setUp() throws Exception {
		map = MapEx.of("a", "A", "b", "B");
	}

	@Test
	public void sizeMatchesExpected() {
		assertEquals("Constructed map is of size 2", 2, map.size());
	}
	
	@Test
	public void containsExpectedKey() {
		assertTrue("Constructed map contains key 'a'", map.containsKey("a"));
	}
	
	@Test
	public void getYieldsExpectedValue() {
		assertEquals("Constructed map has key 'a' mapped to value 'A'", "A", map.get("a").get());
	}
	
	@Test
	public void doesNotContainNotAddedKey() {
		assertFalse("Constructed map doesn't contain key 'c'", map.containsKey("c"));
	}
	
	public void getOfNonexistentKeyThrows() {
		assertFalse("Getting a non-existant key yields an absent optional", map.get("c").isPresent());
	}
	
	@Test
	public void putOfNonExistingKeyAddsValue() {
		map.put("c", "C");
		
		assertEquals("Constructed map now has 3 items", 3, map.size());
		assertEquals("Constructed map now has 'c' mapped to 'C'", "C", map.get("c").get());
	}
	
	@Test
	public void putOfExistingKeyUpdatesValue() {
		String val = map.put("a", "D");
		
		assertEquals("Constructed map still contains 2 items", 2, map.size());
		assertEquals("Constructed map now has 'a' mapped to 'D'", "D", map.get("a").get());
		assertEquals("put method returned old value of 'A'", "A", val);
	}
	
	@Test
	public void forEachGetsExpectedElements() {
		List<String> result = new ArrayList<>();
		
		map.forEach((key, value) -> {
			result.add(key + " = " + value);
		});
		
		assertArrayEquals("For-each has the expected elements",
				new String[] {"a = A", "b = B"},
				result.toArray());
	}
	
	@Test
	public void forEachKeyGetsAddedKeys() {
		List<String> keys = new ArrayList<>();
		
		map.forEachKey((key) -> {
			keys.add(key);
		});
		
		assertArrayEquals("forEachKey gives the expected keys",
				new String[] {"a", "b"},
				keys.toArray());
	}
	
	@Test
	public void forEachValueGetsAddedValues() {
		List<String> keys = new ArrayList<>();
		
		map.forEachValue((key) -> {
			keys.add(key);
		});
		
		assertArrayEquals("forEachKey gives the expected values",
				new String[] {"A", "B"},
				keys.toArray());
	}
	
	@Test
	public void clearRemovesAllValues() {
		map.clear();
		
		assertEquals("A cleared map contains no items", 0, map.size());
	}
	
	@Test
	public void removeOfExistingKeyRemovesKey() {
		String removed = map.remove("a");
		
		assertEquals("Constructed map now has one less element", 1, map.size());
		assertFalse("Constructed map no longer contains a removed key", map.containsKey("a"));
		assertEquals("Remove returns the removed value", "A", removed);
	}
	
	@Test
	public void removeOfNonExistingKeyDoesntRemoveAnything() {
		String removed = map.remove("c");
		
		assertEquals("Constructed map still contains 2 elements", 2, map.size());
		assertNull("remove of a non-existing key returns null", removed);
	}
	
	@Test
	public void keyListReturnsListOfKeys() {
		assertArrayEquals("Constructed map key-list has the expected elements",
				new Object[] {"a", "b"},
				map.keyList().toArray(new String[0]));
	}
	
	@Test
	public void valueListReturnsListOfValues() {
		assertArrayEquals("Constructed map value-list has the expected elements",
				new Object[] {"A", "B"},
				map.valueList().toArray(new String[0]));
	}

	@Test
	public void mapIsThawedByDefault() {
		assertFalse("isFrozen is false by default for a map", map.isFrozen());
	}
	
	@Test
	public void canFreezeMap() {
		assertTrue("canFreeze is true for a map", map.canFreeze());
		assertTrue("freeze freezes a map", map.freeze());
		assertTrue("isFrozen indicates a map is frozen", map.isFrozen());
	}
	
	@Test(expected = ObjectFrozen.class)
	public void clearOfFrozenMapFails() {
		map.freeze();
		map.clear();
	}
}
