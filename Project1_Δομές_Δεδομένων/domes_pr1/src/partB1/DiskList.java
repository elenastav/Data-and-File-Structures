package partB1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import disk.FileHandling;
import general.MultiCounter;
import general.Point;
import general.Search;

//A class to represent the 3rd data structure (List in the disk) 

public class DiskList implements Search, FileHandling {

	public static final int DataPageSize = 256;
	protected RandomAccessFile file;    //File in the disk
	protected File helpFile;       //Help file to be able to delete the random access file 
	protected String fileName;     
	protected ListPage listPage;   //Current page of the file that we process

	public DiskList(String fileName) {   //List Initialization
		this.fileName = fileName;
		try {
			helpFile = new File(fileName);
			file = new RandomAccessFile(helpFile, "rw");   //Create the file
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		listPage = new ListPage(0);      //Create the first page of the file
	}
	
	@Override
	public void readPage(int pos) {
		try {
			file.seek(pos * DataPageSize);   //Seek to the specific position of the file 
			file.read(listPage.getData());   //Read the page (use the data buffer of listPage)
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writePage() {
		try {
			file.seek(listPage.getPosition());  //Seek to the position of the current page that has been processed
			file.write(listPage.getData());     //Write the current data buffer back to the file
		} catch (IOException e) {
			e.printStackTrace();
		}
		listPage.increasePageNum();   
		listPage.initNumOfPoints();   //Increase page number and initialize number of elements
	}
	
	@Override
	public void writeLastPage() {
		if(listPage.getNumOfPoints() != 0) {     //In case there are elements left to the current page (last page) 
			writePage();     //Write them back to the file
		}
	}

	@Override
	public void insert(Point point) {
		listPage.addPoint(point);   //Add the element to current data buffer
		if (listPage.isFull()) {   								
			writePage();   //If the data buffer is full of elements, write it to the file
		}
	}

	@Override
	public boolean search(Point point) {
		MultiCounter.resetCounter(1);    //Reset counter of disk accesses
		
		for (int i = 0; i < listPage.getPageNum(); i++) {   //Go through every page of the file
			readPage(i);   
			MultiCounter.increaseCounter(1);   //Increase counter for every read
			int[] integers = listPage.convertDataToIntegers();    //Deserialize data
			
			for (int j = 0; j < integers.length; j = j + 2) {     //Go through every pair of integers (every element)
				if (integers[j] == point.getX() && integers[j + 1] == point.getY())   //Check if the element exists 
					return true;				
			}
		}
		return false;
	}
	
	@Override
	public long getLength() {      //Return the length of the list in bytes
		return listPage.getPageNum()*DataPageSize;
	}

	@Override
	public void setLength(long length) {
		listPage.setPageNum((int) (length/DataPageSize));
	}
	
	@Override
	public void deleteFile() {   //Close and delete the file using the help file
		try {
			file.setLength(0);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		helpFile.delete();
	}
}
