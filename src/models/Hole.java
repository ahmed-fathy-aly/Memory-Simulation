package models;

public class Hole extends MemorySpace
{

	public Hole(int start, int size)
	{
		super(start, size);
	}

	public String toString()
	{
		return "Hole from: " + start + "->" + end;
	}

	/**
	 * extends the hole with another one directly after it
	 */
	public void extendHole(Hole hole)
	{
		size += hole.size;
		end = hole.end;
	}

	/**
	 * adds a process to the start of the hole
	 */
	public void addProcess(Process newProcess)
	{
		// put the process
		newProcess.start = start;
		newProcess.end = start + newProcess.size - 1;

		// remove a bit of the hole
		size -= newProcess.size;
		start += newProcess.size;
	}
}
