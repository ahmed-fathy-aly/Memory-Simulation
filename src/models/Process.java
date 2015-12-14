package models;

public class Process extends MemorySpace
{
	/* fields */
	String name;

	/* Constructor */
	public Process(int start, int size, String name)
	{
		super(start, size);
		this.name = name;
	}

	public Process(int size, String name)
	{
		super(-1, size);
		this.name = name;
	}

	/* setters and getters */
	public String getName()
	{
		return name;
	}

	/* methods */
	public String toString()
	{
		return "Process " + name + " from: " + start + "->" + end;
	}

}