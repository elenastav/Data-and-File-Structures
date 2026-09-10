package memBTree;

import counter.MultiCounter;

public class BTree {
	BTreeNode root;
	int degree;

	// Constructor
	public BTree(int degr) {
		root = null;
		degree = degr;
	}

	public void traverse() {
		if (root != null) {
			root.traverse();
		}
		System.out.println();
	}

	// function to search a key in this tree
	public BTreeNode search(int key) {
		if (MultiCounter.increaseCounter(1) && root == null)
			return null;
		else
			return root.search(key);
	}

	// The main function that inserts a new key in this B-Tree
	public void insert(int key) {
		if (MultiCounter.increaseCounter(1) && root == null) { // If tree is empty
			root = new BTreeNode(degree, true);
			root.keys[0] = key; // Insert key
			root.numOfKeys = 1; // Update number of keys in root
		} else { // If tree is not empty

			// If root is full, then tree grows in height
			if (MultiCounter.increaseCounter(1) && root.numOfKeys == 2 * degree - 1) {
				BTreeNode s = new BTreeNode(degree, false); // Allocate memory for new root
				s.children[0] = root; // Make old root as child of new root
				s.splitChild(0, root); // Split the old root and move 1 key to the new root

				// New root has two children now. Decide which of the two children is going to
				// have new key
				int i = 0;
				if (MultiCounter.increaseCounter(1) && s.keys[0] < key)
					i++;
				s.children[i].insertNoNFull(key);
				root = s; // Change root
			} else // If root is not full, call insertNonFull for root
				root.insertNoNFull(key);
		}
	}

	public void remove(int key) {
		if (MultiCounter.increaseCounter(1) && root == null) {
			System.out.println("The tree is empty");
			return;
		}
		root.remove(key); // Call the remove function for root

		// If the root node has 0 keys, make its first child as the new root
		// if it has a child, otherwise set root as NULL
		if (MultiCounter.increaseCounter(1) && root.numOfKeys == 0) {
			if (MultiCounter.increaseCounter(1) && root.leaf)
				root = null;
			else
				root = root.children[0];
		}
	}
}