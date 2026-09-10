package BST;

import general.Elem;

public class DynamicBST implements BinarySearchTree { // Binary Search Tree implementation

	private BinNode root; // The root of the tree
	private int insert_counter, remove_counter;

	public DynamicBST() {
		root = null;
		insert_counter = remove_counter = 0;
	} // Initialize root to null

	public void clear() {
		root = null;
	} // Throw the nodes away

	public int insert(Object val) {
		insert_counter = 0;
		root = inserthelp(root, (Elem) val);
		return insert_counter;
	}

	public int remove(int key) {
		remove_counter = 0;
		root = removehelp(root, key);
		return remove_counter;
	}

	public Elem find(int key) {
		return findhelp(root, key);
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void print() { // Print out the BST
		if (root == null)
			System.out.println("The BST is empty.");
		else {
			printhelp(root, 0);
			System.out.println();
		}
	}
	
	private Elem findhelp(BinNode rt, int key) {
		if (rt == null)
			return null;
		Elem it = (Elem) rt.element();
		if (it.key() > key)
			return findhelp(rt.left(), key);
		else if (it.key() == key)
			return it;
		else
			return findhelp(rt.right(), key);
	}

	private BinNode inserthelp(BinNode rt, Elem val) {
		insert_counter++;
		if (rt == null) {
			insert_counter += 3;
			return new BinNodePtr(val);
		}
		insert_counter += 2;
		Elem it = (Elem) rt.element();
		if (it.key() > val.key()) {
			insert_counter++;
			rt.setLeft(inserthelp(rt.left(), val));
		}
		else {
			insert_counter++;
			rt.setRight(inserthelp(rt.right(), val));
		}
		return rt;
	}

	private BinNode deletemin(BinNode rt) {
		remove_counter++;
		if (rt.left() == null)
			return rt.right();
		else {
			remove_counter++;
			rt.setLeft(deletemin(rt.left()));
			return rt;
		}
	}

	private Elem getmin(BinNode rt) {
		remove_counter++;
		if (rt.left() == null)
			return (Elem) rt.element();
		else
			return getmin(rt.left());
	}

	private BinNode removehelp(BinNode rt, int key) {
		remove_counter++;
		if (rt == null)
			return null;
		remove_counter += 3;
		Elem it = (Elem) rt.element();
		if (key < it.key()) {
			remove_counter++;
			rt.setLeft(removehelp(rt.left(), key));
		}
		else if (key > it.key()) {
			remove_counter++;
			rt.setRight(removehelp(rt.right(), key));
		}
		else { // Found it
			remove_counter += 2;
			if (rt.left() == null) {
				remove_counter++;
				rt = rt.right();
			}
			else if (rt.right() == null) {
				remove_counter++;
				rt = rt.left();
			}
			else { // Two children
				remove_counter += 3;
				Elem temp = getmin(rt.right());
				rt.setElement(temp);
				rt.setRight(deletemin(rt.right()));
			}
		}
		return rt;
	}

	private void printhelp(BinNode rt, int level) {
		if (rt == null)
			return;
		printhelp(rt.right(), level + 1);
		for (int i = 0; i < level; i++) // Indent based on level
			System.out.print("  ");
		System.out.println((Elem) rt.element()); // Print node value
		printhelp(rt.left(), level + 1);
	}

} // BST class
