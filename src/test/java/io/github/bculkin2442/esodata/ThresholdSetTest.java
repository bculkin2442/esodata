package io.github.bculkin2442.esodata;

import org.junit.Test;

import bjc.esodata.ThresholdSet;
import io.github.bculkin2442.TestUtils;

import java.util.Iterator;

import static org.junit.Assert.*;
import static io.github.bculkin2442.TestUtils.*;

/**
 * Tests for ThresholdSet
 *
 * @author Ben Culkin.
 */
public class ThresholdSetTest {
	@Test
	public void testAdd() {
		ThresholdSet<String> thst = new ThresholdSet<>();

		thst.addAll("a", "b");

		assertIteratorEquals(false, thst.setView().iterator(), "a", "b");
	}

	@Test
	public void testAddMulti() {
		ThresholdSet<String> thst = new ThresholdSet<>();

		thst.addAll("a", "b", "a");

		assertIteratorEquals(false, thst.setView().iterator(), "b");
	}

	@Test
	public void testRemoveMulti() {
		ThresholdSet<String> thst = new ThresholdSet<>();

		thst.addAll("a", "a", "b");
		thst.removeAll("a");

		assertIteratorEquals(false, thst.setView().iterator(), "a", "b");
	}
}
