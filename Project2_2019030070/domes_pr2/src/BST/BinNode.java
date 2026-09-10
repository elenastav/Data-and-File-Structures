package BST;

/*
 * ADT for binary tree nodes
 */
interface BinNode {

	/*
	 * Return the element value
	 */
	public Object element();

	/*
	 * Set the element value
	 */
	public Object setElement(Object v);

	/*
	 * Return left child
	 */
	public BinNode left();

	/*
	 * Set left child
	 */
	public BinNode setLeft(BinNode p);

	/*
	 * Return right child
	 */
	public BinNode right();

	/*
	 * Set right child
	 */
	public BinNode setRight(BinNode p);

	/*
	 * Return true if this is a leaf node
	 */
	public boolean isLeaf();
} // interface BinNode
