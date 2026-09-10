package main;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import BST.ArrayBST;
import BST.DynamicBST;
import general.Elem;
import heap.MaxHeap;
import general.IElem;

public class MainConsole {
	
	public int[] convertToIntegers(byte bArray[]) {    //convert a byte array to integer array
		IntBuffer buf = ByteBuffer.wrap(bArray).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		int[] intArray = new int[buf.remaining()];
		buf.get(intArray);
		return intArray;
	}
	
	public static void main(String[] args) {
		MainConsole console = new MainConsole();
		int N = (int) Math.pow(10, 6);
		int M = 100;
		int [] counters = new int[6];
		double[] time = new double[7];	
		
		int [] insertKeys = new int[N];
		int [] deleteKeys = new int[M];
		try {      //read keys from files and convert them to integer arrays
			insertKeys = console.convertToIntegers(Files.readAllBytes(Paths.get("keys_1000000_BE.bin")));
			deleteKeys = console.convertToIntegers(Files.readAllBytes(Paths.get("keys_del_100_BE.bin")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Random rd = new Random();   //shuffle the keys to be inserted
		for (int i = insertKeys.length-1; i > 0; i--) {            
            //pick a random index from 0 to i
            int j = rd.nextInt(i+1);             
            //swap array[i] with the element at random index
            int temp = insertKeys[i];
            insertKeys[i] = insertKeys[j];
            insertKeys[j] = temp;
        }
		
		//Dynamic Binary Search Tree
		DynamicBST dtree = new DynamicBST();
		time[0] = System.nanoTime();
		for (int i = 0; i < N; i++) {
			counters[0] = counters[0] + dtree.insert(new IElem(insertKeys[i]));  //insertions
		}
		time[0] = System.nanoTime() - time[0];
		counters[0] = counters[0]/N;
		
		time[1] = System.nanoTime();
		for (int i = 0; i < M; i++) {
			counters[1] = counters[1] + dtree.remove(deleteKeys[i]);   //removals
		}
		time[1] = System.nanoTime() - time[1];
		counters[1] = counters[1]/M;
		
		//Array Binary Search Tree
		ArrayBST atree = new ArrayBST(N);
		time[2] = System.nanoTime();
		for (int i = 0; i < N; i++) {
			counters[2] = counters[2] +atree.insert(insertKeys[i]);
		}
		time[2] = System.nanoTime() - time[2];
		counters[2] = counters[2]/N;
		
		time[3] = System.nanoTime();
		for (int i = 0; i < M; i++) {
			counters[3] = counters[3] + atree.remove(deleteKeys[i]);
		}
		time[3] = System.nanoTime() - time[3];
		counters[3] = counters[3]/M;


		//Array Binary Heap
		Elem[] heap_array = new Elem[N];         
		for (int i = 0; i < N; i++) {
			heap_array[i] = new IElem(insertKeys[i]);
		}
		time[4] = System.nanoTime();
		MaxHeap heap = new MaxHeap(heap_array, heap_array.length, N); //insertions altogether
		heap.buildHeap();
		time[4] = System.nanoTime() - time[4];
		
		heap_array = new Elem[N];         
		time[5] = System.nanoTime();
		heap = new MaxHeap(heap_array, 0, N);   //insertions one by one	
		for (int i = 0; i < N; i++) {
			heap_array[i] = new IElem(insertKeys[i]);
			counters[4] = counters[4] + heap.insert(heap_array[i]);	
		}
		time[5] = System.nanoTime() - time[5];
		counters[4] = counters[4]/N;

		time[6] = System.nanoTime();
		for (int i = 0; i < M; i++) {
			counters[5] = counters[5] + heap.remove();
		}
		time[6] = System.nanoTime() - time[6];
		counters[5] = counters[5]/M;
		
		for(int i = 0; i < time.length; i++) {
			time[i] = (time[i] * (Math.pow(10, -6)));   //calculate time in ms
		}
		System.out.println("Method		Comparisons/Insertion  	 Time(10^6 one by one Insertions)          Comparisons/Deletion		Time(100 Deletions)	Time(10^6 all at once Insertions)");
		System.out.printf("Dynamic BST %15d %35.7f %30d %31.7f                 - \n", counters[0], time[0], counters[1], time[1]);
		System.out.printf("Array BST %17d %35.7f %30d %31.7f                 - \n", counters[2], time[2], counters[3], time[3]);
		System.out.printf("Array Heap %16d %35.7f %30d %31.7f %25.7f", counters[4], time[5], counters[5], time[6], time[4]);

	}
}
