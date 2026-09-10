package BST;


public class ArrayBST {

	private int data[][];
	private int root, avail;      //root and next available position
	private int insert_counter, remove_counter;    //counters

	public ArrayBST(int N) {
		root = -1;
		avail = 0;
		insert_counter = remove_counter = 0;

		data = new int[N][3];
		for (int i = 0; i < N; i++) {     //array initialization
			data[i][0] = data[i][1] = -1;
			data[i][2] = i + 1;
		}
	}

	public int insert(int key) {
		insert_counter = 0;
		root = inserthelp(root, key);
		return insert_counter;
	}

	private int inserthelp(int rt, int key) {   //when we reach an empty node, the new available node is filled with data                                             
		insert_counter++;                       //and the available position is updated
		if (rt == -1) {
			insert_counter += 5;
			int temp = avail;
			avail = data[avail][2];
			data[temp][0] = key;
			data[temp][1] = data[temp][2] = -1;
			return temp;
		}
		insert_counter++;
		if (key < data[rt][0]) {
			insert_counter++;
			data[rt][1] = inserthelp(data[rt][1], key);
		} else {
			insert_counter++;
			data[rt][2] = inserthelp(data[rt][2], key);
		}
		return rt;
	}

	public int find(int key) {
		return findhelp(root, key);
	}

	private int findhelp(int rt, int key) {      //go through the array to search for the node
		if (rt == -1)
			return -1;
		if (key < data[rt][0])
			return findhelp(data[rt][1], key);
		else if (key > data[rt][0])
			return findhelp(data[rt][2], key);
		else
			return rt;
	}

	public int remove(int key) {
		remove_counter = 0;
		root = removehelp(root, key);
		return remove_counter;
	}

	public int removehelp(int rt, int key) {    //search for the node to be deleted
		remove_counter++;
		if(rt == -1) 
			return -1;
		remove_counter += 2;
		if(key < data[rt][0]) {
			remove_counter++;
			data[rt][1] = removehelp(data[rt][1], key);
		} else if (key > data[rt][0]){
			remove_counter++;
			data[rt][2] = removehelp(data[rt][2], key);
		} else {   //found it   
			int temp;
			remove_counter += 2;
			if (data[rt][1] == -1) {      //if it has 1 or 0 children, exchange it with the child
				remove_counter += 2;
				temp = rt;
				rt = data[rt][2];
			}
			else if (data[rt][2] == -1) {
				remove_counter += 2;
				temp = rt;
				rt = data[rt][1];          
			}
			else {           //if it has 2 children, exchange it with the minimum of the right subtree
				remove_counter += 3;
				temp = getmin(data[rt][2]);
				data[rt][0] = data[temp][0];
				data[rt][2] = deletemin(data[rt][2]);				
			}
			remove_counter += 3;
			data[temp][0] = data[temp][1] = -1;      //delete the node
			data[temp][2] = avail;
			avail = temp;           //update avail
		}
		return rt;
	}

	public int getmin(int rt) {
		remove_counter++;
		if (data[rt][1] == -1)
			return rt;
		else
			return getmin(data[rt][1]);
	}
	
	private int deletemin(int rt) {
		remove_counter++;
		if (data[rt][1] == -1)
			return data[rt][0];
		else {
			remove_counter++;
			data[rt][1] = deletemin(data[rt][1]);
			return rt;
		}
	}
	
	 public void print(int n){
	        System.out.println("--------------");
	        for (int i = 0; i < n; i++){
	            System.out.println(data[i][0] + " " + data[i][1] +" " + data[i][2] );
	        }
	    }
}
