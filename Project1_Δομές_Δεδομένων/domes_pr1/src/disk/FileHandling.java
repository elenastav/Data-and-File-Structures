package disk;

//An interface to process files

public interface FileHandling {
	public void writePage();    //Write current page back to the file
	public void writeLastPage();   //Write the last page to the file
	public void readPage(int position);  //Read page from a specific position of the file
	public void deleteFile();   //Delete the file
}
