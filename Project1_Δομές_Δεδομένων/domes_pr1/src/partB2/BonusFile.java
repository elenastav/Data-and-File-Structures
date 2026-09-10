package partB2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

//A class to represent the bonus file, where the table of pages is saved

public class BonusFile {

	private RandomAccessFile bFile; 
	
	public BonusFile(String bonusFileName, long M) {
		try {
			bFile = new RandomAccessFile(bonusFileName, "rw");    //Create the bonus file
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		initBonusFile(M);     //Bonus file initialization
	}
	
	public void initBonusFile(long M) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate((int) (M*8));   //Allocate space for M*8 bytes(M heads, M tails)
		for (int i = 0; i < 2*M; i++)  
			buffer.putInt(-1);	 //Initialize all the buffer to -1
		try {
			bFile.seek(0);
			bFile.write(buffer.array());    //Write the buffer at the beginning of the bonus file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int[] readFromBonusFile(int index) {   //Read head and tail from a specific position of the bonus file
		byte[] buffer = new byte[8];     
		try {
			bFile.seek(index*8);  //Seek to the position shown by the index
			bFile.read(buffer);   //Read the 8-byte buffer
		} catch (IOException e) {
			e.printStackTrace();
		}
		IntBuffer intBuffer = ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN).asIntBuffer(); //Convert to integer array
		int[] integers = new int[intBuffer.remaining()];
		intBuffer.get(integers);
		return integers;    //Return the 2 integers (head and tail)
	}
	
	public void writeToBonusFile(int head, int tail, int index) {   //Write head and tail to a specific position of the bonus file
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(8);
		buffer.putInt(head);
		buffer.putInt(tail);   //Convert to byte array
		try {
			bFile.seek(index*8);   //Seek to the position shown by the index
			bFile.write(buffer.array());   //Write the 8-byte buffer
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
