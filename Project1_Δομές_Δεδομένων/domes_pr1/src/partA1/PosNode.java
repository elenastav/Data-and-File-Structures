package partA1;

import general.Point;
import memory.MemListItem;

//An implementation of interface MemListItem

public class PosNode implements MemListItem {
	
	private Position pos;
	private PosNode next;
	
	public PosNode(Position pos) {
		this.pos = pos;
	}

	public PosNode(Position pos, PosNode next) {
		this.pos = pos;
		this.next = next;
	}

	@Override
	public Point getValue() {
		return pos;
	}

	@Override
	public void setValue(Point point) {
		this.pos = (Position) point;
	}

	@Override
	public MemListItem getNext() {
		return next;
	}

	public void setNext(MemListItem next) {
		this.next = (PosNode) next;
	}

}
