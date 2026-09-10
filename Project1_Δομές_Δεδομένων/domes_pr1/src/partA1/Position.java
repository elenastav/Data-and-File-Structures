package partA1;

import general.MultiCounter;
import general.Point;

//An implementation of interface Point

public class Position implements Point{
	
	private int x, y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setX(int x) {
		this.x = x;	
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getY() {
		return y;
	}
	
	//Compare a point to another and increase the counter for each comparison of x or y
	
	@Override
	public boolean equals(Point p) {
		return (MultiCounter.increaseCounter(1) && p.getX() == x) && MultiCounter.increaseCounter(1) && (p.getY() == y);
	}

}
