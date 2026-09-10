package partA1;

import general.MultiCounter;
import general.Point;
import memory.MemList;
import memory.MemListItem;

//An implementation of MemList interface

public class MemoryList implements MemList {
	
	private PosNode head, tail;
	private long length;

	public MemoryList() {    //Constructor to create an empty list
		head = null;
		tail = null;
		length = 0;
	}
	
	@Override
	public MemListItem getHead() {
		return head;
	}

	@Override
	public MemListItem getTail() {
		return tail;
	}

	@Override
	public long getLength() {
		return length;
	}

	@Override
	public void setLength(long length) {
		this.length = length;
	}

	@Override
	public boolean isEmpty() { 
		return (head == null); 
	}
	
	@Override
	public void insert(Point point) {        //Add a node to the list
		length++;                           
		if (isEmpty()) {
			head = tail = new PosNode((Position)point);    //If the list is empty, initialize it with a node
		}
		else {
			tail.setNext(new PosNode((Position)point));    //Else, add a node at the end of the list
			tail = (PosNode)tail.getNext();                //Move the tail (pointer at the end)
		}
	}

	@Override
	public boolean search(Point point) {       //Search for a specifis node
		MultiCounter.resetCounter(1);          //Reset counter
		MultiCounter.increaseCounter(1);       //Increase for the 1st assignment (tmp = head)
		MultiCounter.increaseCounter(1);       //Increase for 1st comparison to null
		
		for (PosNode tmp = head; tmp != null; tmp = (PosNode)tmp.getNext()) {       //Go through all the list 
			MultiCounter.increaseCounter(1);        //Increase counter for every assignment to the next node 
			MultiCounter.increaseCounter(1);        //Increase for every comparison to null
			
			if (point.equals(tmp.getValue())) 		//Compare the node to each node of the list and return the result		
				return true;
		}
		return false;
	}
}
