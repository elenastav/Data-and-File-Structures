package BST;

import java.io.*;

// Binary tree node with pointers to children
class BinNodePtr implements BinNode, Serializable {

	private Object element; // Object for this node
	private BinNode left; // Pointer to left child
	private BinNode right; // Pointer to right child

	public BinNodePtr() {
		left = right = null;
	} // Constructor 1

	public BinNodePtr(Object val) { // Constructor 2
		left = right = null;
		element = val;
	}

	public BinNodePtr(Object val, BinNode l, BinNode r) // Construct 3
	{
		left = l;
		right = r;
		element = val;
	}

	// Return and set the element value
	public Object element() {
		return element;
	}

	public Object setElement(Object v) {
		return element = v;
	}

	// Return and set the left child
	public BinNode left() {
		return left;
	}

	public BinNode setLeft(BinNode p) {
		return left = p;
	}

	// Return and set the right child
	public BinNode right() {
		return right;
	}

	public BinNode setRight(BinNode p) {
		return right = p;
	}

	public boolean isLeaf() // Return true if this is a leaf node
	{
		return (left == null) && (right == null);
	}
} // class BinNodePtr
