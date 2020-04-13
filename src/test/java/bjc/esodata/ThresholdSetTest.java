package bjc.esodata;

import org.junit.Test;

import static bjc.TestUtils.*;

import static bjc.esodata.ThresholdSet.*;

import static org.junit.Assert.*;

/**
 * Tests for ThresholdSet
 *
 * @author Ben Culkin.
 */
public class ThresholdSetTest {
	@Test
	public void testAdd() {
		ThresholdSet<String> thst = TS("a", "b");

		assertIteratorSet(false, thst.setView().iterator(), "a", "b");
		assertEquals(thst.setSize(), 2);
	}

	@Test
	public void testAddMulti() {
		ThresholdSet<String> thst = TS("a", "b", "a");

		assertIteratorSet(false, thst.setView().iterator(), "b");
		assertEquals(thst.setSize(), 1);
	}

	@Test
	public void testAddMulti2() {
		ThresholdSet<String> thst = TS("a", "b");
		thst.add("a");

		assertIteratorSet(false, thst.setView().iterator(), "b");
		assertEquals(thst.setSize(), 1);
	}

	@Test
	public void testContains() {
		ThresholdSet<String> thst = TS("1", "2", "2", "x", "z");

		int[] exps = new int[] {
				1, 2, -1
		};
		assertArrayEquals(exps, thst.containsKeys("1", "2", "y"));
	}

	@Test
	public void testRemoveMulti() {
		ThresholdSet<String> thst = TS("a", "a", "b");

		thst.remove("a");

		assertIteratorSet(false, thst.setView().iterator(), "a", "b");
		assertEquals(2, thst.setSize());

		thst.remove("a");

		assertIteratorSet(false, thst.setView().iterator(), "b");
		assertEquals(1, thst.setSize());
	}

	@Test
	public void testSetTransparency() {
		ThresholdSet<String> thst = TS("a", "b", "c");

		thst.setView().add("b");
		assertIteratorSet(false, thst.setView().iterator(), "a", "c");
		assertEquals(2, thst.setView().size());

		thst.setView().remove("c");
		assertIteratorSet(false, thst.setView().iterator(), "a");
	}
}
