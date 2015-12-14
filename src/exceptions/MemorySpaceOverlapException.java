package exceptions;

import models.MemorySpace;

public class MemorySpaceOverlapException extends Exception
{
	MemorySpace first;
	MemorySpace second;
	
	public MemorySpaceOverlapException(MemorySpace first, MemorySpace second)
	{
		this.first = first;
		this.second = second;
	}
	
	public String toString()
	{
		return "Two Memory spaces overlap: " + first.toString() + " and " + second.toString();
	}
}
