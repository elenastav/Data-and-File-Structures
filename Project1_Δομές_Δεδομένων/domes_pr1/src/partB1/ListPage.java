package partB1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import disk.DataPage;
import general.Point;

//An implementation of DataPage (it's used at DiskList class)

public class ListPage implements DataPage {

	public static final int DataPageSize = 256;  //Size of a page in the file (256 bytes)
	public static final int PointSize = 8;       //Size of an element (2 integers => 8 bytes)
	protected byte[] data;      //Serialized data of the page
	protected int pageNum;      //Number of page in the file (starting from 0)
	protected int numOfPoints;  //Number of elements in the page

	public ListPage(int pNum) {         //Initialize a page 
		data = new byte[DataPageSize];  //Byte buffer with page data
		numOfPoints = 0;                //Zero number of elements
		pageNum = pNum;                 //Specific position in the file
	}
	
	@Override
	public long getPosition() { 
		return pageNum * DataPageSize;
	}
	
	@Override
	public int getPageNum() {
		return pageNum;
	}

	@Override
	public void increasePageNum() {    //Increase page number (when a new page is added to the file)
		pageNum++;
	}
	
	@Override
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	@Override
	public int getNumOfPoints() {
		return numOfPoints;
	}
	
	@Override
	public void initNumOfPoints() {    //Initialize number of elements (when a new page is added to the file)
		numOfPoints = 0;
	}
	
	@Override
	public void increaseNumOfPoints() {   //Increase number of elements (when a new element is added to the page)
		numOfPoints++;
	}

	@Override
	public byte[] getData() {
		return data;
	}
	
	@Override
	public boolean isFull() {       //Check if the page is full of elements (maximum = 32)
		if (numOfPoints == DataPageSize / PointSize)
			return true;
		return false;
	}

	@Override
	public int[] convertDataToIntegers() {       //Deserialize         
		IntBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).asIntBuffer();  //Wrap the integers to a byte array
		int[] integers = new int[buffer.remaining()];      //Collect the integers
		buffer.get(integers);
		return integers;
	}
	
	@Override
	public byte[] convertPointToBytes(Point point) {     //Serialize     
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(PointSize);   //Allocate space for 1 element 
		buffer.putInt(point.getX());         
		buffer.putInt(point.getY());   //Place x and y of the element to the byte buffer
		return buffer.array();         //Return buffer
	}

	@Override
	public void addPoint(Point point) {        
		byte[] temp = new byte[PointSize];
		temp = convertPointToBytes(point);      //Convert element to byte array
		System.arraycopy(temp, 0, data, numOfPoints * PointSize, PointSize);    //Add it to the data buffer
		increaseNumOfPoints();    //Increase number of elements
	}
}
