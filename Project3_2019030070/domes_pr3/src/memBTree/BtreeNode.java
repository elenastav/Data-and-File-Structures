package memBTree;

import counter.MultiCounter;

class BTreeNode {

	int[] keys; // An array of keys
	int degree; // Minimum degree (defines the range for number of keys)
	BTreeNode[] children; // An array of children nodes
	int numOfKeys; // Current number of keys
	boolean leaf; // Is true when node is leaf. Otherwise false

	// Constructor
	public BTreeNode(int degr, boolean lf) {
		degree = degr;
		leaf = lf;
		keys = new int[2 * degree - 1]; // maximum number of possible keys
		children = new BTreeNode[2 * degree];
		numOfKeys = 0; // Initialize the number of keys as 0
	}

	// A utility function that returns the index of the first key that is
	// greater than or equal to key
	public int findKey(int key) {
		int idx = 0;
		while (MultiCounter.increaseCounter(1) && idx < numOfKeys && MultiCounter.increaseCounter(1) && keys[idx] < key)
			++idx;
		return idx;
	}

	// A function to remove the key k from the sub-tree rooted with this node
	public void remove(int key) {
		int idx = findKey(key);

		// The key to be removed is present in this node		
		if (MultiCounter.increaseCounter(1) && idx < numOfKeys && MultiCounter.increaseCounter(1) && keys[idx] == key) {
			if (MultiCounter.increaseCounter(1) && leaf) // If the node is a leaf node - removeFromLeaf is called
				removeFromLeaf(idx);
			else // Otherwise, removeFromNonLeaf function is called
				removeFromNonLeaf(idx);
		} else {
			if (MultiCounter.increaseCounter(1) && leaf) { // If this node is a leaf node, then the key is not present in tree
				//System.out.printf("The key %d does not exist in the tree\n", key);
				return;
			}

			// The key to be removed is present in the sub-tree rooted with this node
			// The flag indicates whether the key is present in the sub-tree rooted
			// with the last child of this node
			boolean flag;
			if (MultiCounter.increaseCounter(1) && idx == numOfKeys)
				flag = true;
			else
				flag = false;

			// If the child where the key is supposed to exist has less than minimum degree
			// keys, we fill that child
			if (MultiCounter.increaseCounter(1) && children[idx].numOfKeys < degree)
				fill(idx);

			// If the last child has been merged, it must have merged with the previous
			// child and so we recurse on the (idx-1)th child.
			// Else, we recurse on the (idx)th child which now has atleast t keys
			if (MultiCounter.increaseCounter(1) && flag && MultiCounter.increaseCounter(1) && idx > numOfKeys)
				children[idx - 1].remove(key);
			else
				children[idx].remove(key);
		}
	}

	// A function to remove the idx-th key from this node - which is a leaf node
	public void removeFromLeaf(int idx) {
		// Move all the keys after the idx-th pos one place backward
		for (int i = idx + 1; MultiCounter.increaseCounter(1) && i < numOfKeys; ++i) 
			keys[i - 1] = keys[i];
		numOfKeys--; // Reduce the count of keys
	}

	// A function to remove the idx-th key from this node - which is a non-leaf node
	public void removeFromNonLeaf(int idx) {
		int k = keys[idx];

		// If the child that precedes k (children[idx]) has at least degree keys,
		// find the predecessor 'pred' of k in the subtree rooted at
		// children[idx]. Replace k by pred. Recursively delete pred in C[idx]
		if (MultiCounter.increaseCounter(1) && children[idx].numOfKeys >= degree) {
			int pred = getPred(idx);
			keys[idx] = pred;
			children[idx].remove(pred);
		}

		// If the child children[idx] has less that degree keys, examine children[idx+1].
		// If children[idx+1] has at least degree keys, find the successor 'succ' of k in
		// the subtree rooted at children[idx+1]. Replace k by succ and recursively delete succ in children[idx+1]
		else if (MultiCounter.increaseCounter(1) && children[idx + 1].numOfKeys >= degree) {
			int succ = getSucc(idx);
			keys[idx] = succ;
			children[idx + 1].remove(succ);
		}
		// If both children[idx] and children[idx+1] has less that degree keys, merge k
		// and all of children[idx+1]
		// into children[idx]. Now children[idx] contains 2degree-1 keys
		// Recursively delete k from children[idx]
		else {
			merge(idx);
			children[idx].remove(k);
		}
	}

	// A function to get predecessor of keys[idx]
	public int getPred(int idx) {
		BTreeNode cur = children[idx];
		while (MultiCounter.increaseCounter(1) && !cur.leaf) // Keep moving to the right most node until we reach a leaf
			cur = cur.children[cur.numOfKeys];
		return cur.keys[cur.numOfKeys - 1]; // Return the last key of the leaf
	}

	public int getSucc(int idx) {
		BTreeNode cur = children[idx + 1];
		while (MultiCounter.increaseCounter(1) && !cur.leaf) // Keep moving the left most node until we reach a leaf
			cur = cur.children[0];
		return cur.keys[0]; // Return the first key of the leaf
	}

	// A function to fill child C[idx] which has less than t-1 keys
	public void fill(int idx) {
		// If the previous child has more than degree-1 keys, borrow a key from that
		// child
		if (MultiCounter.increaseCounter(1) && idx != 0 && MultiCounter.increaseCounter(1) && children[idx - 1].numOfKeys >= degree)
			borrowFromPrev(idx);

		// If the next child has more than degree-1 keys, borrow a key from that child
		else if (MultiCounter.increaseCounter(1) && idx != numOfKeys && MultiCounter.increaseCounter(1) && children[idx + 1].numOfKeys >= degree)
			borrowFromNext(idx);

		// Merge children[idx] with its sibling
		// If children[idx] is the last child, merge it with its previous sibling
		// Otherwise merge it with its next sibling
		else {
			if (MultiCounter.increaseCounter(1) && idx != numOfKeys)
				merge(idx);
			else
				merge(idx - 1);
		}
	}

	// A function to borrow a key from children[idx-1] and insert it into
	// children[idx]
	public void borrowFromPrev(int idx) {
		BTreeNode child = children[idx];
		BTreeNode sibling = children[idx - 1];

		// The last key from children[idx-1] goes up to the parent and key[idx-1]
		// from parent is inserted as the first key in children[idx]. Thus, the loses
		// sibling one key and child gains one key		
		for (int i = child.numOfKeys - 1; MultiCounter.increaseCounter(1) && i >= 0; --i)  // Moving all keys one step ahead
			child.keys[i + 1] = child.keys[i];

		if (MultiCounter.increaseCounter(1) && !child.leaf) { /// If children[idx] is not a leaf, move all its child pointers one step ahead
			for (int i = child.numOfKeys; MultiCounter.increaseCounter(1) && i >= 0; --i) 
				child.children[i + 1] = child.children[i];
		}

		// Setting child's first key equal to keys[idx-1] from the current node
		child.keys[0] = keys[idx - 1];
		if (MultiCounter.increaseCounter(1) && !child.leaf) // Moving sibling's last child as children[idx]'s first child
			child.children[0] = sibling.children[sibling.numOfKeys];

		// Moving the key from the sibling to the parent
		// This reduces the number of keys in the sibling
		keys[idx - 1] = sibling.keys[sibling.numOfKeys - 1];
		child.numOfKeys += 1;
		sibling.numOfKeys -= 1;
	}

	// A function to borrow a key from the children[idx+1] and place
	// it in children[idx]
	public void borrowFromNext(int idx) {
		BTreeNode child = children[idx];
		BTreeNode sibling = children[idx + 1];
		child.keys[child.numOfKeys] = keys[idx]; // keys[idx] is inserted as the last key in chlidren[idx]

		// Sibling's first child is inserted as the last child into children[idx]
		if (MultiCounter.increaseCounter(1) && !child.leaf)
			child.children[child.numOfKeys + 1] = sibling.children[0];
		keys[idx] = sibling.keys[0]; // The first key from sibling is inserted into keys[idx]

		for (int i = 1; MultiCounter.increaseCounter(1) && i < sibling.numOfKeys; ++i) // Moving all keys in sibling one step behind
			sibling.keys[i - 1] = sibling.keys[i];

		if (MultiCounter.increaseCounter(1) && !sibling.leaf) { // Moving the child pointers one step behind
			for (int i = 1; MultiCounter.increaseCounter(1) && i <= sibling.numOfKeys; ++i)
				sibling.children[i - 1] = sibling.children[i];
		}
		// Increasing and decreasing the key count of children[idx] and children[idx+1]
		// respectively
		child.numOfKeys += 1;
		sibling.numOfKeys -= 1;
	}

	// A function to merge children[idx] with children[idx+1]
	public void merge(int idx) {
		BTreeNode child = children[idx];
		BTreeNode sibling = children[idx + 1];

		// Pulling a key from the current node and inserting it into (d-1)th position of
		// children[idx]
		child.keys[degree - 1] = keys[idx];

		// Copying the keys from children[idx+1] to children[idx] at the end
		for (int i = 0; MultiCounter.increaseCounter(1) && i < sibling.numOfKeys; ++i) 
			child.keys[i + degree] = sibling.keys[i];

		// Copying the child pointers from children[idx+1] to children[idx]
		if (MultiCounter.increaseCounter(1) && !child.leaf) {
			for (int i = 0; MultiCounter.increaseCounter(1) && i <= sibling.numOfKeys; ++i) 
				child.children[i + degree] = sibling.children[i];
		}

		// Moving all keys after idx in the current node one step before -
		// to fill the gap created by moving keys[idx] to children[idx]
		
		for (int i = idx + 1; MultiCounter.increaseCounter(1) && i < numOfKeys; ++i) 
			keys[i - 1] = keys[i];		

		// Moving the child pointers after (idx+1) in the current node one step before
		for (int i = idx + 2; MultiCounter.increaseCounter(1) && i <= numOfKeys; ++i)
			children[i - 1] = children[i];
		child.numOfKeys += sibling.numOfKeys + 1; // Updating the key count of child and the current node
		numOfKeys--;
	}

	// A utility function to insert a new key in this node
	// The assumption is, the node must be non-full when this function is called
	public void insertNoNFull(int key) {
		int i = numOfKeys - 1; // Initialize index as index of rightmost element

		if (MultiCounter.increaseCounter(1) && leaf) { // If this is a leaf node
			// Find the location of new key to be inserted
			// Move all greater keys to one place ahead
			while (MultiCounter.increaseCounter(1) && i >= 0 && MultiCounter.increaseCounter(1) &&  keys[i] > key) {
				keys[i + 1] = keys[i];
				i--;
			}
			keys[i + 1] = key; // Insert the new key at found location
			numOfKeys = numOfKeys + 1;
		} else { // If this node is not leaf
					// Find the child which is going to have the new key
			while (MultiCounter.increaseCounter(1) && i >= 0 && MultiCounter.increaseCounter(1) &&  keys[i] > key)
				i--;
			if (MultiCounter.increaseCounter(1) && children[i + 1].numOfKeys == 2 * degree - 1) { // See if the found child is full
				splitChild(i + 1, children[i + 1]); // If the child is full, then split it

				// After split, the middle key of children[i] goes up and children[i] is
				// splitted into two.
				// See which of the two is going to have the new key
				if (MultiCounter.increaseCounter(1) && keys[i + 1] < key)
					i++;
			}
			children[i + 1].insertNoNFull(key);
		}
	}

	// A utility function to split the child y of this node
	// Note that y must be full when this function is called
	public void splitChild(int i, BTreeNode y) {
		// Create a new node which is going to store (t-1) keys of y
		BTreeNode z = new BTreeNode(y.degree, y.leaf);
		z.numOfKeys = degree - 1;

		// Copy the last (degree-1) keys of y to z
		for (int j = 0; MultiCounter.increaseCounter(1) && j < degree-1; j++)
			z.keys[j] = y.keys[j+degree];
		
		// Copy the last degree children of y to z
		if (MultiCounter.increaseCounter(1) && !y.leaf) {
			for (int j = 0; MultiCounter.increaseCounter(1) && j < degree; j++)
				z.children[j] = y.children[j+degree];
		}
		y.numOfKeys = degree - 1;   // Reduce the number of keys in y

		// Since this node is going to have a new child, create space of new child
		for (int j = numOfKeys; MultiCounter.increaseCounter(1) && j >= i+1; j--)
			children[j + 1] = children[j];
		children[i + 1] = z;   // Link the new child to this node

		// A key of y will move to this node. Find location of
	    // new key and move all greater keys one space ahead
		for (int j = numOfKeys-1; MultiCounter.increaseCounter(1) && j >= i; j--)
			keys[j + 1] = keys[j];
		keys[i] = y.keys[degree - 1];   // Copy the middle key of y to this node

		numOfKeys = numOfKeys + 1;   // Increment count of keys in this node
	}

	// Function to traverse all nodes in a subtree rooted with this node
	public void traverse() {		
		//There are n keys and n+1 children, traverse through n keys and first n children
		int i;
		for (i = 0; i < numOfKeys; i++) {
			//If this is not leaf, then before printing key[i], traverse the subtree rooted with child children[i].
			if (!leaf)
				children[i].traverse();
			System.out.printf(" %d\n", keys[i]);
		}
		// Print the subtree rooted with last child
		if (!leaf) {
			children[i].traverse();
		}
	}

	// Function to search key k in subtree rooted with this node
	public BTreeNode search(int key) {
		// Find the first key greater than or equal to key
		int i = 0;
		while (MultiCounter.increaseCounter(1) && i < numOfKeys && MultiCounter.increaseCounter(1) && key > keys[i])
			i++;
		if (MultiCounter.increaseCounter(1) && keys[i] == key)  // If the found key is equal to key, return this node
			return this;		
		if (MultiCounter.increaseCounter(1) && leaf)   // If key is not found here and this is a leaf node
			return null;
		return children[i].search(key);  // Go to the appropriate child
	}
}