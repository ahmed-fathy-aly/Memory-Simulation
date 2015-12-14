package exceptions;

public class InvalidHoleSizeException extends Exception
{
	int holeSize;

	public InvalidHoleSizeException(int holeSize)
	{
		this.holeSize = holeSize;
	}

	public String toString()
	{
		return "Cant have a hole of size " + holeSize;
	}
}
