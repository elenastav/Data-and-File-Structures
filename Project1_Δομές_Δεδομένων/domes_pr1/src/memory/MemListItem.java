package memory;

import general.Point;

//An interface to represent a node in memory

public interface MemListItem {
	public Point getValue();
	public void setValue(Point point);        //Getter and setter for the value(point)
	public MemListItem getNext();             
	public void setNext(MemListItem next);    //Getter and setter for the next node
}
