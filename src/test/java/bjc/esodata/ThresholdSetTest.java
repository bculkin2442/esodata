package bjc.esodata;

import org.junit.Test;

import bjc.TestUtils;
import bjc.esodata.ThresholdSet;

import java.util.Iterator;

import static bjc.TestUtils.*;
import static org.junit.Assert.*;

/**
 * Tests for ThresholdSet
 *
 * @author Ben Culkin.
 */
public class ThresholdSetTest {
	@Test
	public void testAdd() {
		ThresholdSet<String> thst = new ThresholdSet<>();

		thst.addKeys("a", "b");

		assertIteratorEquals(false, thst.setView().iterator(), "a", "b");
	}

	@Test
	public void testAddMulti() {
		ThresholdSet<String> thst = new ThresholdSet<>();

		thst.addKeys("a", "b", "a");

		assertIteratorEquals(false, thst.setView().iterator(), "b");
	}

	@Test
	public void testRemoveMulti() {
		ThresholdSet<String> thst = new ThresholdSet<>();

		thst.addKeys("a", "a", "b");
		thst.removeKeys("a");

		assertIteratorEquals(false, thst.setView().iterator(), "a", "b");
	}
}
