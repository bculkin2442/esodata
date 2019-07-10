package bjc.esodata;

import org.junit.Test;

import bjc.TestUtils;

import java.util.*;

import static bjc.TestUtils.*;

import static org.junit.Assert.*;

/**
 * Tests of Stack.
 *
 * @author Ben Culkin
 */
public class StackTest {
	@Test
	public void testBasic() {
		Stack<String> st = new SimpleStack<>();

		assertEquals(0, st.size());

		st.push("a");
		assertEquals(1, st.size());

		assertEquals("a", st.top());
		assertEquals(1, st.size());

		assertEquals("a", st.pop());
		assertEquals(0, st.size());
		assertTrue(st.isEmpty());
	}

	@Test
	public void testSpaghetti() {
		Stack<String> st = new SimpleStack<>();

		st.pushAll("a", "b", "c");

		Stack<String> spst1 = st.spaghettify();
		Stack<String> spst2 = st.spaghettify();

		// st should be [a, b, c]
		// spst1 should be [[a, b, c]]
		// spst2 should be [[a, b, c]]

		assertEquals("c", st.top());
		assertEquals("c", spst1.top());
		assertEquals("c", spst2.top());

		assertEquals(3, st.size());
		assertEquals(3, spst1.size());
		assertEquals(3, spst2.size());

		st.push("d");
		spst1.push("e");
		spst2.push("f");

		// st should be [a, b, c, d]
		// spst1 should be [[a, b, c, d], e]
		// spst2 should be [[a, b, c, d], f]

		assertEquals("d", st.top());
		assertEquals("e", spst1.top());
		assertEquals("f", spst2.top());

		assertEquals(4, st.size());
		assertEquals(5, spst1.size());
		assertEquals(5, spst2.size());

		spst1.pop();

		// st should be [a, b, c]
		// spst1 should be [[a, b, c]]
		// spst2 should be [[a, b, c], f]

		assertEquals("d", spst1.pop());
		assertEquals("c", st.top());

		assertEquals(3, st.size());
		assertEquals(3, spst1.size());
		assertEquals(4, spst2.size());
	}

	@Test
	public void testBasicComb() {
		Stack<String> st = new SimpleStack<>();

		st.pushAll("a", "b", "c", "d");

		st.drop();

		// stack should be [a, b, c]

		assertEquals("c", st.top());
		assertEquals(3, st.size());

		st.drop(2);

		// stack should be [a]

		assertEquals("a", st.top());
		assertEquals(1, st.size());

		st.pushAll("b", "c", "d");

		st.nip();
		st.nip();

		// stack should be [a, d]
		assertEquals("d", st.top());
		assertEquals(2, st.size());

		st.pop();

		assertEquals("a", st.top());
		assertEquals(1, st.size());

		st.multidup(1, 1);

		// stack should be [a, a]
		assertEquals("a", st.top());
		assertEquals(2, st.size());

		st.pushAll("b", "c");

		st.multidup(3, 1);

		// stack should be [a, a, b, c, a, b, c]
		assertEquals("c", st.top());
		assertEquals(7, st.size());

		st.pop();
		assertEquals("b", st.pop());
		assertEquals("a", st.pop());
		assertEquals("c", st.top());

		// stack should be [a, a, b, c]

		st.dup();
		assertEquals("c", st.top());
		assertEquals(5, st.size());

		// stack should be [a, a, b, c, c]
		assertEquals("c", st.pop());
		assertEquals(4, st.size());

		// stack should be [a, a, b, c]
	}
}
