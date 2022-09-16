package bjc.funcdata.bst;

import java.io.*;
import java.util.*;

/**
 * Example showing how to use the binary search tree.
 *
 * @author ben
 *
 */
public class BinarySearchTreeExample {
	private static void display(final BinarySearchTree<Character> tree,
			final Scanner input, PrintStream output) {
		output.print(
				"What order would you like the tree to be printed in (m for options): ");
		char command;

		while (true) {
			command = input.nextLine().charAt(0);
			TreeLinearizationMethod method = null;

			switch (command) {
			case 'm':
				System.out.println("Possible tree printing methods: ");
				System.out.println(
						"\tp: Preorder printing (print parent first, then left & right).");
				System.out.println(
						"\ti: Inorder printing (print left first, then parent & right).");
				System.out.println(
						"\to: Postorder printing (print left first, then right & parent).");
				break;
			case 'p':
				method = TreeLinearizationMethod.PREORDER;
				break;
			case 'i':
				method = TreeLinearizationMethod.INORDER;
				break;
			case 'o':
				method = TreeLinearizationMethod.POSTORDER;
				break;
			default:
				System.out.println("ERROR: Unknown command.");
			}

			if (method != null) {
				tree.traverse(method, element -> {
					System.out.println("Node: " + element);
					return true;
				});

				return;
			}

			System.out.print(
					"What order would you like the tree to be printed in (m for options): ");
		}
	}

	/**
	 * Main method of class
	 *
	 * @param args
	 *             Unused CLI args
	 */
	public static void main(final String[] args) {
		try (Scanner input = new Scanner(System.in)) {
			runExample(input, System.out);
		}
	}

	private static void runExample(final Scanner input, OutputStream outpt) {
		System.out.println("Binary Tree Constructor/Searcher");
		final BinarySearchTree<Character> tree
				= new BinarySearchTree<>((o1, o2) -> o1 - o2);

		PrintStream output = new PrintStream(outpt);
		
		char command = ' ';
		while (command != 'e') {
			output.print("Enter a command (m for help): ");
			command = input.nextLine().charAt(0);

			switch (command) {
			case 'm':
				output.println("Valid commands: ");
				output.println("\tm: Display this help message.");
				output.println("\te: Exit this program.");
				output.println("\ta: Add a node to the binary tree.");
				output.println("\td: Display the binary tree.");
				output.println("\tr: Remove a node from the binary tree.");
				output.println("\tf: Check if a given node is in the binary tree.");
				output.println("\tt: Trim all deleted nodes from the tree.");
				output.println("\tb: Balance the tree (also trims dead nodes)");
				break;
			case 'a':
				output.print("Enter the letter to add to the binary tree: ");
				command = input.nextLine().charAt(0);

				tree.addNode(command);
				break;
			case 'r':
				output.print("Enter the letter to add to the binary tree: ");
				command = input.nextLine().charAt(0);

				tree.deleteNode(command);
				break;
			case 'd':
				display(tree, input, output);
				break;
			case 'f':
				output.print("Enter the letter to add to the binary tree: ");
				command = input.nextLine().charAt(0);

				final boolean inTree = tree.isInTree(command);
				if (inTree) {
					output.printf("Node %s was found\n", command);
				} else {
					output.printf("Node %s was not found\n", command);
				}
				break;
			case 't':
				tree.trim();
				break;
			case 'b':
				tree.balance();
				break;
			case 'e':
				break;
			default:
				output.println("ERROR: Unrecognized command.");
			}
		}
	}
}
