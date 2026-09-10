package BST;

import general.Elem;

/*
 * Interface for binary search tree
 */
interface BinarySearchTree {

	public void clear();

	public int insert(Object val);

	public int remove(int key);

	public Elem find(int key);

	public boolean isEmpty();

	public void print();

}
