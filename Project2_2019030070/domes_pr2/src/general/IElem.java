package general;

// Sample implementation for Elem interface.
// A record with just an int field.
public class IElem implements Elem {
	private int value;

	public IElem(int v) {
		value = v;
	}

	public IElem() {
		value = 0;
	}

	public int key() {
		return value;
	}

	public void setkey(int v) {
		value = v;
	}

	// Override Object.toString
	public String toString() {
		return Integer.toString(value);
	}

}
