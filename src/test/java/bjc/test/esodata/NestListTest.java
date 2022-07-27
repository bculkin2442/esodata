package bjc.test.esodata;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import bjc.*;
import bjc.esodata.NestList;

@SuppressWarnings("javadoc")
public class NestListTest
{

	@Test
	public void testAddItemElement() {
		NestList<String> nl = new NestList<>();
		
		assertEquals("NestLists are created empty", 0, nl.size());
		
		nl.addItems("hello", "there");
		
		assertEquals("Adding two items to an empty NestList makes it size 2",
				2, nl.size());
	}

	@Test
	public void testAddItemNestListOfElement() {
		NestList<String> nl = new NestList<>();
		
		NestList<String> nl2 = new NestList<>();
		
		nl2.addItem("friend");
		
		nl.addItems("hello", "there");
		nl.addItem(nl2);
		
		assertEquals("Adding a sublist increases the size of NestList by 1",
				3, nl.size());
	}

	@Test
	public void testAddSublist() {
		NestList<String> nl = new NestList<>();
		
		nl.addSublist("here", "is", "a");
		nl.addItem("thing");
		
		assertEquals("addSublist increases size of NestList by 1", 2, nl.size());
	}

	@Test
	public void testFlatIterator() {
		NestList<String> nl1 = new NestList<>();
		NestList<String> nl2 = new NestList<>();
		NestList<String> nl3 = new NestList<>();
		
		nl1.addItems("of", "means");
		
		nl2.addItem(nl1);
		
		nl3.addItem("Hello");
		nl3.addSublist("my", "unfortunate");
		nl3.addItem("friend");
		nl3.addItem(nl2);
		
		TestUtils.assertIteratorEquals(nl3.flatIterator(),
				"Hello", "my", "unfortunate", "friend", "of", "means");
	}

	@Test
	public void testFlatten() {	
		NestList<String> nl1A = new NestList<>();
		NestList<String> nl2A = new NestList<>();
		NestList<String> nl3A = new NestList<>();
		
		nl1A.addItems("of", "means");
		
		nl2A.addItem(nl1A);
		
		nl3A.addItem("Hello");
		nl3A.addSublist("my", "unfortunate");
		nl3A.addItem("friend");
		nl3A.addItem(nl2A);
		
		NestList<String> nl1B = new NestList<>();
		NestList<String> nl2B = new NestList<>();
		
		nl1B.addItems("of", "means");

		nl2B.addItems("Hello", "my", "unfortunate", "friend");
		nl2B.addItem(nl1B);
		
		assertEquals("Flatten removes one level of nesting",
				nl2B, nl3A.flatten());
	}

	@Test
	public void testDeepFlatten() {
		NestList<String> nl1 = new NestList<>();
		NestList<String> nl2 = new NestList<>();
		NestList<String> nl3 = new NestList<>();
		
		nl1.addItems("of", "means");
		
		nl2.addItem(nl1);
		
		nl3.addItem("Hello");
		nl3.addSublist("my", "unfortunate");
		nl3.addItem("friend");
		nl3.addItem(nl2);
		
		List<String> testList = Arrays.asList(
				"Hello", "my", "unfortunate", "friend", "of", "means");
		
		assertEquals("deepFlatten flattens out all sublists",
				testList, nl3.deepFlatten());
	}
}
