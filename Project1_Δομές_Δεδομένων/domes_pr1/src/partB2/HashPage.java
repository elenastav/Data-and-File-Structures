package partB2;

import general.Point;
import partB1.ListPage;

//An extension of list page (it's used at DiskHash class)

public class HashPage extends ListPage {

	public static final int InfoSize = 8;   //Size of information of each page: 4 bytes for the number of elements 
	private int nextPageNum;                                              //and 4 bytes for the number of next page 
	
	public HashPage(int pNum) {   //Initialize page
		super(pNum);	
		initNextPageNum();     
	}
	
	public int getNextPageNum() {    //Getter for the number of the next page
		return nextPageNum;
	}
	
	public void initNextPageNum() {    //Initialize next page number to -1
		nextPageNum = -1;
		byte[] temp = new byte[InfoSize/2];
		temp = convertIntToBytes(nextPageNum);     //Convert it to byte array
		System.arraycopy(temp, 0, data, InfoSize/2, InfoSize/2);     //Save it to data buffer (2nd number)
	}
	
	public void saveNextPageNum(int pCounter) {   //Save number of next page (used when the page gets full)
		nextPageNum = pCounter;
		byte[] temp = new byte[InfoSize/2];
		temp = convertIntToBytes(nextPageNum);   //Convert to byte array
		System.arraycopy(temp, 0, data, InfoSize/2, InfoSize/2);    //Save it to data buffer (2nd number)
	}
	
	public byte[] convertIntToBytes(int x) {      //Convert 1 integer to a byte array
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(InfoSize/2);
		buffer.putInt(x);
		return buffer.array();
	}
	
	public void setNumOfPoints() {     //Set number of elements (written 1st at the page)
		int[] ints = convertDataToIntegers();   //Deserialize the data buffer
		numOfPoints = ints[0];     //Take the 1st integer
	}
	
	@Override
	public boolean isFull() {      //Check if page is full of elements (maximum = 31)
		if (numOfPoints == (DataPageSize-InfoSize) / PointSize)
			return true;
		return false;
	}
	
	@Override
	public void addPoint(Point point) {
		byte[] temp1 = new byte[PointSize];  
		temp1 = convertPointToBytes(point);    //Convert element to byte array
		System.arraycopy(temp1, 0, data, numOfPoints*PointSize+InfoSize, PointSize);   //Add it to the data buffer
		increaseNumOfPoints();    //Increase number of elements
		byte[] temp2 = new byte[InfoSize/2];  
		temp2 = convertIntToBytes(numOfPoints);
		System.arraycopy(temp2, 0, data, 0, InfoSize/2);    //Inform the data buffer
	}
}
