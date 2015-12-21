package models;

public class Process extends MemorySpace
{
	/* fields */
	private String name;
	private int arrivalTime, memoryEntryTime, departureTime;
	/* Constructor */
	public Process(int start, int size, String name)
	{
		super(start, size);
		this.name = name;
		this.setArrivalTime(-1);
		this.setMemoryEntryTime(-1);
		this.setDepartureTime(-1);
	}

	public Process(int size, String name)
	{
		super(-1, size);
		this.name = name;
		this.setArrivalTime(-1);
		this.setMemoryEntryTime(-1);
		this.setDepartureTime(-1);
	}

	public Process(int size, String name, int arrivalTime) {
		super(-1, size);
		this.name = name;
		this.setArrivalTime(arrivalTime);
		this.setMemoryEntryTime(-1);
		this.setDepartureTime(-1);
	}
	
	/* setters and getters */
	public String getName()
	{
		return name;
	}
	
//	public void setName(String name) {
//		this.name = name;
//	}
	
	/* methods */
	public String toString()
	{
		return "Process " + name + " from: " + start + "->" + end;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getMemoryEntryTime() {
		return memoryEntryTime;
	}

	public void setMemoryEntryTime(int memoryEntryTime) {
		this.memoryEntryTime = memoryEntryTime;
	}

	public int getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(int departureTime) {
		this.departureTime = departureTime;
	}

}