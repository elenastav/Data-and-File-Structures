package mainConsole;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import MemAVLTree.AVLTree;
import counter.MultiCounter;
import diskBPlusTree.BPlusConfiguration;
import diskBPlusTree.BPlusTree;
import diskBPlusTree.BPlusTreePerformanceCounter;
import diskBPlusTree.InvalidBTreeStateException;
import memBTree.BTree;

public class Console {

	public int[] convertToIntegers(byte bArray[]) { // convert a byte array to integer array
		IntBuffer buf = ByteBuffer.wrap(bArray).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		int[] intArray = new int[buf.remaining()];
		buf.get(intArray);
		return intArray;
	}

	public static void main(String[] args) throws IOException, InvalidBTreeStateException {
		Console console = new Console();
		int M = 100;
		int N = (int) Math.pow(10, 6);

		int[] keys = new int[N];
		int[] insertKeys = new int[M];
		int[] searchKeys = new int[M];
		int[] deleteKeys = new int[M];

		try { // read keys from files and convert them to integer arrays
			keys = console.convertToIntegers(Files.readAllBytes(Paths.get("keys_1000000_BE.bin")));
			insertKeys = console.convertToIntegers(Files.readAllBytes(Paths.get("keys_insert_100_BE.bin")));
			searchKeys = console.convertToIntegers(Files.readAllBytes(Paths.get("keys_search_100_BE.bin")));
			deleteKeys = console.convertToIntegers(Files.readAllBytes(Paths.get("keys_delete_100_BE.bin")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int pageSize[] = { 128, 256 };

		// ------------------------BPLUS 128-------------------------------
		BPlusConfiguration bconf = new BPlusConfiguration(pageSize[0], 8, 8);
		BPlusTreePerformanceCounter bcount = new BPlusTreePerformanceCounter(true);
		BPlusTree bpt = new BPlusTree(bconf, "rw+", bcount);
		int[] counters = new int[30];
		System.out.println("-----------------B+ tree(page size: 128)--------------------");

		for (int i = 1; i <= 10; i++) {
			int[] keyArray = Arrays.copyOf(keys, (N / 10) * i);

			for (int j = 0; j < keyArray.length; j++)     //initial insertions 
				bpt.insertKey(keyArray[j], null, false);
			bcount.resetAllMetrics();

			for (int j = 0; j < insertKeys.length; j++)   //100 insertions
				bpt.insertKey(insertKeys[j], null, true);
			counters[i - 1] = (bcount.getPageReads() + bcount.getPageWrites()) / insertKeys.length;
			bcount.resetAllMetrics();

			for (int j = 0; j < searchKeys.length; j++)   //100 searches
				bpt.searchKey(searchKeys[j], true);
			counters[i + 9] = (bcount.getPageReads() + bcount.getPageWrites()) / searchKeys.length;
			bcount.resetAllMetrics();

			for (int j = 0; j < deleteKeys.length; j++)   //100 deletions
				bpt.deleteKey(deleteKeys[j], false);
			counters[i + 19] = (bcount.getPageReads() + bcount.getPageWrites()) / deleteKeys.length;
			bcount.resetAllMetrics();

			System.out.println("Keys: " + (N / 10) * i + "	Insertion: " + counters[i - 1] + "	Search: "
					+ counters[i + 9] + "	Deletion: " + counters[i + 19]);
			bpt = new BPlusTree(bconf, "rw+", bcount);
		}

		// --------------------------BPLUS 256---------------------------
		bconf = new BPlusConfiguration(pageSize[1], 8, 8);
		bcount = new BPlusTreePerformanceCounter(true);
		counters = new int[30];
		System.out.println("-----------------B+ tree(page size: 256)--------------------");

		for (int i = 1; i <= 10; i++) {
			bpt = new BPlusTree(bconf, "rw+", bcount);
			int[] keyArray = Arrays.copyOf(keys, (N / 10) * i);

			for (int j = 0; j < keyArray.length; j++)     //initial insertions 
				bpt.insertKey(keyArray[j], null, false);
			bcount.resetAllMetrics();

			for (int j = 0; j < insertKeys.length; j++)     //100 insertions
				bpt.insertKey(insertKeys[j], null, true);
			counters[i - 1] = (bcount.getPageReads() + bcount.getPageWrites()) / insertKeys.length;
			bcount.resetAllMetrics();

			for (int j = 0; j < searchKeys.length; j++)    //100 searches
				bpt.searchKey(searchKeys[j], true);
			counters[i + 9] = (bcount.getPageReads() + bcount.getPageWrites()) / searchKeys.length;
			bcount.resetAllMetrics();

			for (int j = 0; j < deleteKeys.length; j++)    //100 deletions
				bpt.deleteKey(deleteKeys[j], false);
			counters[i + 19] = (bcount.getPageReads() + bcount.getPageWrites()) / deleteKeys.length;
			bcount.resetAllMetrics();

			System.out.println("Keys: " + (N / 10) * i + "	Insertion: " + counters[i - 1] + "	Search: "
					+ counters[i + 9] + "	Deletion: " + counters[i + 19]);
		}

		// ------------------B TREE---------------------------
		BTree bt = new BTree(33);
		counters = new int[30];
		System.out.println("-------------------------B tree------------------------------");
		for (int i = 1; i <= 10; i++) {
			int[] array = Arrays.copyOf(keys, (N / 10) * i);
			for (int j = 0; j < array.length; j++)       //initial insertions 
				bt.insert(array[j]);

			int totalCount = 0;
			for (int j = 0; j < insertKeys.length; j++) {      //100 insertions
				MultiCounter.resetCounter(1);
				bt.insert(insertKeys[j]);
				totalCount += MultiCounter.getCount(1);
			}
			counters[i - 1] = totalCount / insertKeys.length;

			totalCount = 0;
			for (int j = 0; j < searchKeys.length; j++) {    //100 searches
				MultiCounter.resetCounter(1);
				bt.search(searchKeys[j]);
				totalCount += MultiCounter.getCount(1);
			}
			counters[i + 9] = totalCount / searchKeys.length;

			totalCount = 0;
			for (int j = 0; j < deleteKeys.length; j++) {     //100 deletions
				MultiCounter.resetCounter(1);
				bt.remove(deleteKeys[j]);
				totalCount += MultiCounter.getCount(1);
			}
			counters[i + 19] = totalCount / deleteKeys.length;

			System.out.println("Keys: " + (N / 10) * i + "	Insertion: " + counters[i - 1] + "	Search: "
					+ counters[i + 9] + "	Deletion: " + counters[i + 19]);
		}

		// -------------------------AVL TREE-----------------------------
		AVLTree avlt = new AVLTree();
		counters = new int[30];
		System.out.println("-------------------------AVL tree---------------------------");
		for (int i = 1; i <= 10; i++) {
			int[] array = Arrays.copyOf(keys, (M / 10) * i);
			for (int j = 0; j < array.length; j++)        //initial insertions 
				avlt.setRoot(avlt.insert(avlt.getRoot(), array[j]));    

			int totalCount = 0;
			for (int j = 0; j < insertKeys.length; j++) {   //100 insertions
				MultiCounter.resetCounter(1);
				avlt.setRoot(avlt.insert(avlt.getRoot(), insertKeys[j]));
				totalCount += MultiCounter.getCount(1);
			}
			counters[i - 1] = totalCount / insertKeys.length;

			totalCount = 0;
			for (int j = 0; j < searchKeys.length; j++) {    //100 searches
				MultiCounter.resetCounter(1);
				avlt.searchNode(avlt.getRoot(), searchKeys[j]);
				totalCount += MultiCounter.getCount(1);
			}
			counters[i + 9] = totalCount / searchKeys.length;

			totalCount = 0;
			for (int j = 0; j < deleteKeys.length; j++) {     //100 deletions
				MultiCounter.resetCounter(1);
				avlt.setRoot(avlt.deleteNode(avlt.getRoot(), deleteKeys[j]));
				totalCount += MultiCounter.getCount(1);
			}
			counters[i + 19] = totalCount / deleteKeys.length;

			System.out.println("Keys: " + (N / 10) * i + "	Insertion: " + counters[i - 1] + "	Search: "
					+ counters[i + 9] + "	Deletion: " + counters[i + 19]);
		}
	}
}
