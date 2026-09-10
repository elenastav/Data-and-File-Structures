package heap;

import general.Elem;

// Max-heap class
public class MaxHeap implements Heap {
	private Elem[] Heap; // Pointer to the heap array
	private int size; // Maximum size of the heap
	private int n; // # of elements now in the heap
	private int insert_counter, remove_counter;

	public MaxHeap(Elem[] h, int num, int max) // Constructor
	{
		Heap = h;
		n = num;
		size = max;
	}

	public int heapsize() // Return current size
	{
		return n;
	}

	public boolean isLeaf(int pos) // TRUE if pos a leaf
	{
		remove_counter++;
		if((pos >= n / 2)) {
			remove_counter++;
			if((pos < n))
				return true;
		}
		return false;
	}

	public int leftchild(int pos) // Return leftchild pos
	{
		return 2 * pos + 1;
	}

	public int rightchild(int pos) // Return rightchild pos
	{
		return 2 * pos + 2;
	}

	public int parent(int pos) // Return parent position
	{
		return (pos - 1) / 2;
	}

	public void buildHeap() // Heapify contents
	{
		for (int i = n / 2 - 1; i >= 0; i--)
			siftdown(i);
	}
	
	// Swap two objects in an array
	private void swap(int p1, int p2) {
		insert_counter += 3;
		remove_counter += 3;
		Elem temp = Heap[p1];
		Heap[p1] = Heap[p2];
		Heap[p2] = temp;
	}
	
	private void siftdown(int pos) { // Put element in correct place
		remove_counter += 3;
		Assert.notFalse((pos >= 0) && (pos < n), "Illegal heap position");
		remove_counter++;
		while (!isLeaf(pos)) { // Stop if pos is a leaf
			remove_counter++;
			int j = leftchild(pos);
			remove_counter++;
			if (j < (n - 1)) {
				remove_counter++;
				if (Heap[j].key() < Heap[j + 1].key()) {
					remove_counter++;
					j++; // Set j to greater child's value
				}
			}
			remove_counter++;
			if (Heap[pos].key() >= Heap[j].key())
				return; // Done
			swap(pos, j);
			remove_counter++;
			pos = j; // Move down
		}
	}

	public int insert(Elem val) { // Insert value into heap
		insert_counter = 0;
		insert_counter += 4;
		Assert.notFalse(n < size, "Heap is full");
		int curr = n++;
		Heap[curr] = val; // Start at end of heap
		// Now sift up until curr’s parent > curr
		insert_counter++;
		if (curr != 0) {
			insert_counter++;
			while(Heap[curr].key() > Heap[parent(curr)].key()) {
				swap(curr, parent(curr));
				insert_counter++;
				curr = parent(curr);
			}
		}
		return insert_counter;
	}

	public int remove() { // Remove maximum value
		remove_counter = 0;
		remove_counter += 4;
		Assert.notFalse(n > 0, "Heap is empty");
		swap(0, --n); // Swap min with last value
		if (n != 0)
			siftdown(0); // Siftdown new root val
		return remove_counter; // Return deleted value
	}

} // maxheap class