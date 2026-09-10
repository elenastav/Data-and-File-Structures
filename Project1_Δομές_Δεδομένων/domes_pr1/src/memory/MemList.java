package memory;

import general.Search;

//An interface to represent the 1st data structure (List in memory)

public interface MemList extends Search {
	public MemListItem getHead();   //Return the head of the list
	public MemListItem getTail();   //Return the tail of the list
	public boolean isEmpty();       //Check if the list is empty
}
