package partA2;

import general.Point;
import memory.MemHash;
import memory.MemList;
import partA1.MemoryList;

//An implementation of interface MemHash

public class MemoryHash implements MemHash {

	private long M, N;            //M: length of the table, N: width of x and y
	private MemoryList[] pLists;
	
	public MemoryHash(long M, long N) {      //Create a table of M lists
		this.M = M;
		this.N = N;
		this.pLists = new MemoryList[(int) M];
	}

	@Override
	public int findIndexAndInit(int x, int y) {    
		int index = (int)(((long)x*N + y) % M);     //Calculate index
		if (pLists[index] == null)
			pLists[index] = new MemoryList();       //Initialize the list at the specific index position
		return index;
	}
	
	@Override
	public void insert(Point point) {
		int index = findIndexAndInit(point.getX(), point.getY());   //Find the list to insert the node
		pLists[index].insert(point);       //Call insert from MemoryList class
	}

	@Override
	public boolean search(Point point) {
		int index = findIndexAndInit(point.getX(), point.getY());   //Find the list to search for the node
		return pLists[index].search(point);     //Call search from MemoryList class
	}

	@Override
	public long getLength() {
		return M;
	}
	
	@Override
	public void setLength(long M) {
		this.M = M;
	}

	@Override
	public MemList[] getTable() {	
		return pLists;
	}
}
