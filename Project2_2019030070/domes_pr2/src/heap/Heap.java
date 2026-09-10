package heap;

import general.Elem;

public interface Heap {
	public int heapsize();

	public boolean isLeaf(int pos);

	public int leftchild(int pos);

	public int rightchild(int pos);

	public int parent(int pos);

	public void buildHeap();
	
	public int insert(Elem val);

	public int remove(); // max / min

}
