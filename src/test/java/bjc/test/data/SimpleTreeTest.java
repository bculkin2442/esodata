package bjc.test.data;

import static org.junit.Assert.*;

import org.junit.Test;

import bjc.data.SimpleTree;

public class SimpleTreeTest {

	@Test
	public void test() {
		SimpleTree<String> tree1 = new SimpleTree<>("a");
		SimpleTree<String> tree2 = new SimpleTree<>("a");
		
		assertEquals(tree1, tree2);
		
		tree1.addChild("b");
		
		assertNotEquals(tree1, tree2);
		
		tree2.addChild("b");
	
		tree1.equals(tree2);
		
		assertEquals(tree1, tree2);
	}

}
