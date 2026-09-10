package partB2;

import java.io.IOException;

import general.MultiCounter;
import general.Point;
import partB1.DiskList;

//A class to represent the 4th data structure (Hash in the disk) 

public class DiskHash extends DiskList {      //Extension of DiskList (sharing methods and variables)

	private long M, N;
	private HashPage hashPage;   //Current page of the file that we process
	private int pageCounter;     //Number of pages in the file (start counting from 0, instead of 1)
	private BonusFile bonusFile;     //File where the table is saved         

	public DiskHash(long M, long N, String fileName, String bonusFileName) {    //Hash Initialization
		super(fileName);    //Constructor of DiskList
		pageCounter = -1;   //Empty file 
		this.M = M;
		this.N = N;
		bonusFile = new BonusFile(bonusFileName, M);
	}
	
	public int findIndex(Point point) {
		return (int) (((long) point.getX() * N + point.getY()) % M);
	}

	@Override
	public void readPage(int pos) {
		try {
			hashPage = new HashPage(pos);   //Initialize a new page
			file.seek(pos * DataPageSize);  //Seek to the specific position of the file 
			file.read(hashPage.getData());  //Read the page (use the data buffer of hashPage)
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writePage() {
		try {
			file.seek(hashPage.getPosition());  //Seek to the position of the current page that has been processed
			file.write(hashPage.getData());     //Write the current data buffer back to the file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insert(Point point) {
		int index = findIndex(point);    //Find the list of pages to insert the element
		int[] integers = bonusFile.readFromBonusFile(index); 
		int head = integers[0];
		int tail = integers[1];   //Get the head and the tail of the list from bonus file
		
		if (head == -1) {      //Check for empty list
			pageCounter++;     //Increase page counter
			hashPage = new HashPage(pageCounter);    //Create a new page
			head = tail = pageCounter;      //Set it at the beginning (or the end) of the list
			
		} else {
			readPage(tail);   //Read the last page of the list (tail)
			hashPage.setNumOfPoints();  //Take the first 4 bytes and deserialize them (number of elements) 
									
			if (hashPage.isFull()) {   //Check if page is full of elements
				pageCounter++;    //Increase page counter
				hashPage.saveNextPageNum(pageCounter);   //Save the number of the next page to be inserted to the list
				writePage();      //Write current page back to the file
				hashPage = new HashPage(pageCounter);    //Create a new page
				tail = pageCounter;    //Set it at the end of the list
			}
		}
		hashPage.addPoint(point);   //Add the element to current data buffer and inform number of elements
		writePage();     //Write page to the file     
		bonusFile.writeToBonusFile(head, tail, index);   //Inform the bonus file
	}

	@Override
	public boolean search(Point point) {
		MultiCounter.resetCounter(1);     //Reset counter of disk accesses
		int index = findIndex(point);     //Find the list of pages to search for the element
		int[] ints = bonusFile.readFromBonusFile(index);
		int head = ints[0];    //Get the head of the list from bonus file
		int[] integers;
		int next;
		
		if (head == -1)   //Empty list
			return false;		
		
		for (int i = head; i != -1; i = next) {   //Go through all the list 
			readPage(i);
			MultiCounter.increaseCounter(1);   //Increase the counter for every disk access (read)
			integers = hashPage.convertDataToIntegers();   //Deserialize data
			next = integers[1];   //Get the next page number 
			for (int j = 2; j < integers.length; j = j + 2) {    //Go through every element (starting from the 3rd integer)
				if (integers[j] == point.getX() && integers[j + 1] == point.getY())   //Check if the element exists
					return true;
			}
		}
		return false;
	}
}
