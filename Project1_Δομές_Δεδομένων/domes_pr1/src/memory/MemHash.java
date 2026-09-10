package memory;

import general.Search;

//An interface to represent the 2nd data structure (Hash in memory)

public interface MemHash extends Search {
	public MemList[] getTable();         //returns the table 
	public int findIndexAndInit(int x, int y);  //returns the index H(x,y) and initializes the list
}
