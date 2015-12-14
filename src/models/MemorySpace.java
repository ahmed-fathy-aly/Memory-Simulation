package models;

public class MemorySpace
{
	/* fields */
	protected int start;
	protected int size;
	protected int end;

	/* Constructor */
	public MemorySpace(int start, int size)
	{
		this.start = start;
		this.size = size;
		this.end = start + size - 1;
	}

	/* setters and getters */
	public int getStart()
	{
		return start;
	}

	public int getSize()
	{
		return size;
	}

	public int getEnd()
	{
		return end;
	}

	/* methods */
	public boolean isOverlLapping(MemorySpace x)
	{
		boolean xRight = x.start > end;
		boolean xLeft = start > x.end;
		return !(xRight || xLeft);
	}
	
	public boolean isMergable(MemorySpace x)
	{
		return x.start == end+1 || start == x.end + 1;
	}
}
