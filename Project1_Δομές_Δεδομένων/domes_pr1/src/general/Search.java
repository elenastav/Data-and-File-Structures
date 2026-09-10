package general;

//An interface to conduct the 2 basic operations of our data structure(insert, search)

public interface Search {
	public void insert(Point point);
	public boolean search(Point point);
	public long getLength();
	public void setLength(long length);
}
