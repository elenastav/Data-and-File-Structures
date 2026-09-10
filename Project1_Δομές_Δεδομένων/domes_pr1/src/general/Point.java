package general;

//An interface that represents 1 pair of integers(x,y)

public interface Point {
	public void setX(int x);
	public int getX();
	public void setY(int y);
	public int getY();                    //Getters and setters for x and y
	public boolean equals(Point p);       //Compare 2 Points
}
