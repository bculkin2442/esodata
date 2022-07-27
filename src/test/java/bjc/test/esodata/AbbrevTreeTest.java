package bjc.test.esodata;

import static org.junit.Assert.*;
import static bjc.test.TestUtils.*;

import org.junit.Test;

import bjc.esodata.AbbrevTree;

@SuppressWarnings("javadoc")
public class AbbrevTreeTest {
	private static class StringTree extends AbbrevTree<String, String> {
		// Alias type
		public StringTree(AbbrevTree<String, String> parent, String label, String data) {
			super(parent, label, data);
		}

		public StringTree(String label, String data) {
			super(label, data);
		}		
	}
	
	@Test
	public void testGet() {
		StringTree root = new StringTree("root", "a");
		
		StringTree leaf1 = new StringTree(root, "leaf", "b1");
		StringTree node1 = new StringTree(root, "node1", "b2");
		
		StringTree node2 = new StringTree(node1, "node2", "c1");
		StringTree leaf2 = new StringTree(node1, "leaf", "c2");
		
		var list1 = root.nodes("node2");
		assertEquals(1, list1.size());
		assertIteratorSet(false, list1.iterator(), node2);
		
		var list2 = root.nodes("leaf");
		assertEquals(2, list2.size());
		assertIteratorSet(false, list2.iterator(), leaf1, leaf2);
		
		var list3 = root.nodes("leaf", "node1");
		assertEquals(1, list3.size());
		assertIteratorSet(false, list3.iterator(), leaf2);
	}

}