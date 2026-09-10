package main;

import java.util.ArrayList;
import java.util.Collections;

import general.MultiCounter;
import partA1.MemoryList;
import partA1.Position;
import partA2.MemoryHash;
import partB1.DiskList;
import partB2.DiskHash;

//A class to test the functionality of our data structures

public class Console {

	public Console() {
	}

	public ArrayList<Integer> randomGenerator(long num1, long num2) {   //Generate random numbers in a range between num1 and num2
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (long i = num1; i <= num2; i++) {
			list.add((int) i);
		}
		Collections.shuffle(list);
		return list;
	}

	public static void main(String[] args) {

		Console console = new Console();
		long M = 100;
		long N = (long) Math.pow(2, 18);

		MemoryList mList;
		MemoryHash mHash;
		DiskList dList;
		DiskHash dHash;

		ArrayList<Integer> xList = console.randomGenerator(0, N);    
		ArrayList<Integer> yList = console.randomGenerator(0, N);

		int k[] = { 1000, 10000, 30000, 50000, 70000, 100000 };
		int[] counter = new int[48];     //Counters that store the sums of every type of search and every k
		for (int i : counter)
			counter[i] = 0;     //Counters initialized
		int count = 0;

		for (int i : k) {
			mList = new MemoryList();
			mHash = new MemoryHash(M, N);
			dList = new DiskList("File1");
			dHash = new DiskHash(M, N, "File2", "Bonus");    //Data structures initialized

			Position[] pos = new Position[i];
			for (int j = 0; j < i; j++) {
				pos[j] = new Position(xList.get(j), yList.get(j));
				mList.insert(pos[j]);
				mHash.insert(pos[j]);
				dList.insert(pos[j]);
				dHash.insert(pos[j]);     //Insert k elements in every data structure
			}
			dList.writeLastPage();   

			ArrayList<Integer> sList = console.randomGenerator(0, i - 1);   //Random numbers for successful searches
			ArrayList<Integer> u1List = console.randomGenerator(N + 1, N + 101);   //Random numbers for unsuccessful searches
			ArrayList<Integer> u2List = console.randomGenerator(N + 1, N + 101);

			for (int j = 0; j < 100; j++) {

				mList.search(pos[sList.get(j)]);
				counter[count] = counter[count] + (int) MultiCounter.getCount(1);

				mHash.search(pos[sList.get(j)]);
				counter[count + 1] = counter[count + 1] + (int) MultiCounter.getCount(1);

				dList.search(pos[sList.get(j)]);
				counter[count + 2] = counter[count + 2] + (int) MultiCounter.getCount(1);

				dHash.search(pos[sList.get(j)]);
				counter[count + 3] = counter[count + 3] + (int) MultiCounter.getCount(1);   //100 successful searches

				mList.search(new Position(u1List.get(j), u2List.get(j)));
				counter[count + 4] = counter[count + 4] + (int) MultiCounter.getCount(1);

				mHash.search(new Position(u1List.get(j), u2List.get(j)));
				counter[count + 5] = counter[count + 5] + (int) MultiCounter.getCount(1);

				dList.search(new Position(u1List.get(j), u2List.get(j)));
				counter[count + 6] = counter[count + 6] + (int) MultiCounter.getCount(1);

				dHash.search(new Position(u1List.get(j), u2List.get(j)));
				counter[count + 7] = counter[count + 7] + (int) MultiCounter.getCount(1);   //100 unsuccessful searches
			}
			count = count + 8;
			dList.deleteFile();
			dHash.deleteFile();
		}

		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("Successful searches:");
		System.out.println("Number of Data  |  Memory List  |  Memory Hash  |   Disk List   |   Disk Hash");

		int average1, average2, average3, average4;
		count = 0;
		for (int i : k) {    //Calculate and print the average value for the successful searches
			average1 = Math.round((float)(counter[count] / 100));
			average2 = Math.round((float)(counter[count + 1] / 100));
			average3 = Math.round((float)(counter[count + 2] / 100));
			average4 = Math.round((float)(counter[count + 3] / 100));
			count = count + 8;
			System.out.println("      " + i + "	|     " + average1 + "	|	" + average2 + "	|	" + average3
					+ "	|	" + average4);

		}

		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("Unsuccessful searches:");
		System.out.println("Number of Data  |  Memory List  |  Memory Hash  |   Disk List   |   Disk Hash");

		count = 0;
		for (int i : k) {    //Calculate and print the average value for the unsuccessful searches
			average1 = Math.round((float)(counter[count + 4] / 100));
			average2 = Math.round((float)(counter[count + 5] / 100));
			average3 = Math.round((float)(counter[count + 6] / 100));
			average4 = Math.round((float)(counter[count + 7] / 100));
			count = count + 8;
			System.out.println("      " + i + "	|     " + average1 + "	|	" + average2 + "	|	" + average3
					+ "	|	" + average4);
		}
		System.out.println("------------------------------------------------------------------------------------");
	}
}
