package disk;

import general.Point;

//An interface to represent a file page: it can be deserialized, processed in memory and serialized again 

public interface DataPage {
	public long getPosition();     //Get the position of page in bytes (starting from the beginning of the file)
	public int getPageNum();       //Get the number of page in the file (starting from 0)
	public void increasePageNum(); //Increase page number(+1)
	public void setPageNum(int pageNum);  //Set page number 
	public int getNumOfPoints();   //Get the number of elements-nodes in the page
	public void initNumOfPoints(); //Set number of elements to 0
	public void increaseNumOfPoints();  //Increase number of elements(+1)
	public byte[] getData();       //Get data in bytes
	public boolean isFull();       //Check if the page is full of elements
	public int[] convertDataToIntegers();   //Convert the byte buffer to an integer array (deserialize)
	public byte[] convertPointToBytes(Point point);    //Convert an element to a byte array (serialize)
	public void addPoint(Point point);   //Add an element to the data buffer
}
