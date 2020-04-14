package bjc.esodata;

import org.junit.Test;

import static bjc.TestUtils.*;

import static org.junit.Assert.*;

/**
 * Tests of Stack.
 *
 * @author Ben Culkin
 */
@SuppressWarnings("javadoc")
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

		// assertStackEquals goes from [top] -> [bottom]
		assertStackEquals(st, "c", "b", "a");

		st.drop(2);

		assertStackEquals(st, "a");

		st.pushAll("b", "c", "d");

		st.nip();
		st.nip();

		assertStackEquals(st, "d", "a");

		st.drop();

		st.dup();

		assertStackEquals(st, "a", "a");

		st.pushAll("b", "c");

		st.multidup(3, 1);

		assertStackEquals(st, "c", "b", "a", "c", "b", "a", "a");

		st.drop(3);

		assertStackEquals(st, "c", "b", "a", "a");

		st.over();

		assertStackEquals(st, "b", "c", "b", "a", "a");

		st.drop(2);

		assertStackEquals(st, "b", "a", "a");

		st.multiover(2, 2);

		assertStackEquals(st, "a", "a", "a", "a", "b", "a", "a");

		st.drop(6);

		assertStackEquals(st, "a");

		st.push("b");
		st.push("c");

		assertStackEquals(st, "c", "b", "a");

		st.pick();

		assertStackEquals(st, "a", "c", "b", "a");

		st.swap();

		assertStackEquals(st, "c", "a", "b", "a");

		st.deepdup();

		assertStackEquals(st, "c", "a", "a", "b", "a");

		st.swap();
		st.deepswap();

		assertStackEquals(st, "a", "a", "c", "b", "a");

		st.rot();

		assertStackEquals(st, "c", "a", "a", "b", "a");

		st.invrot();

		assertStackEquals(st, "a", "a", "c", "b", "a");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDataComb() {
		Stack<Integer> stk = new SimpleStack<>();

		stk.pushAll(2, 3, 4);

		assertStackEquals(stk, 4, 3, 2);

		stk.dip(st -> {
			int x = stk.pop();
			int y = stk.pop();

			stk.push(x + y);
		});

		assertStackEquals(stk, 4, 5);

		stk.dip(2, st -> {
			stk.push(6);
		});

		assertStackEquals(stk, 4, 5, 6);

		stk.keep(st -> {
			int v = st.pop();

			st.push(v + 1);
		});

		assertStackEquals(stk, 4, 5, 5, 6);

		stk.multicleave(2, st -> {
			int x = st.pop();
			int y = st.pop();

			st.push(x + y);
		}, st -> {
			int x = st.pop();
			int y = st.pop();

			st.push(y - x);
		});

		assertStackEquals(stk, 1, 9, 5, 6);

		stk.spread(st -> {
			int x = st.pop();
			int y = st.pop();

			st.push(x + y);
		}, st -> {
			int y = st.pop();

			st.push(y + 1);
		});

		assertStackEquals(stk, 10, 6, 6);

		stk.apply(2, st -> {
			int lhs = st.pop();
			st.push(lhs - st.pop());
		});

		assertStackEquals(stk, 2);
	}
}
