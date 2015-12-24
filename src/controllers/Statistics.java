package controllers;

import models.Process;

public class Statistics
{
	private int finishedProcesses, cumulativeTimeInTheSystem;
	private int cumulativeWaitingTime;
	private int maxBufferSize;
	private int bufferSizeArea;
	private int cumulativeOccupiedSize;
	
	private double absoluteAverageMemoryUtilization;
	private double percentageAverageMemoryUtilization;
	private double averageTimeInTheSystem;
	private double averageWaitingTime;
	private double averageBufferSize;

	public Statistics()
	{
		finishedProcesses = 0;
		cumulativeTimeInTheSystem = 0;
		cumulativeWaitingTime = 0;
		maxBufferSize = 0;
		bufferSizeArea = 0;
		cumulativeOccupiedSize = 0;
		absoluteAverageMemoryUtilization = 0;
		percentageAverageMemoryUtilization = 0;
		averageTimeInTheSystem = 0;
		averageWaitingTime = 0;
		averageBufferSize = 0;
	}

	public void updateDepartureStatistics(Process process)
	{
		finishedProcesses++;
		int timeInTheSystem = process.getDepartureTime() - process.getArrivalTime();
		int waitingTime = process.getMemoryEntryTime() - process.getArrivalTime();
		cumulativeTimeInTheSystem += timeInTheSystem;
		cumulativeWaitingTime += waitingTime;
	}

	public void updateQueueLength(int queueLength, int currentSimulationTime, int previousEventTime)
	{
		maxBufferSize = Math.max(maxBufferSize, queueLength);
		bufferSizeArea += queueLength * (currentSimulationTime - previousEventTime);
	}

	public void updateOccupiedSize(int occupiedSize, int currentSimulationTime, int previousEventTime)
	{
		cumulativeOccupiedSize += occupiedSize * (currentSimulationTime - previousEventTime);
	}
	
	public void computeStatistics(int currentSimulationTime, int memorySize) {
		averageBufferSize = bufferSizeArea * 1.0 / currentSimulationTime;
		averageTimeInTheSystem = cumulativeTimeInTheSystem * 1.0 / finishedProcesses;
		averageWaitingTime = cumulativeWaitingTime * 1.0 / finishedProcesses;
		absoluteAverageMemoryUtilization = cumulativeOccupiedSize * 1.0 / currentSimulationTime;
		percentageAverageMemoryUtilization = cumulativeOccupiedSize * 100.0 / currentSimulationTime / memorySize;
	}
	
	
	/* get any statistics u want from these getters ya 7abeeby */
	
	public int getNumberFinishedProcesses() {
		return finishedProcesses;
	}
	
	public double getAverageBuferSize() {
		return averageBufferSize;
	}
	
	public double getAverageTimeInSystem()
	{
		return averageTimeInTheSystem;
	}

	public double getAverageWaitingTime()
	{
		return averageWaitingTime;
	}

	public double getAbsoluteAverageMemoryUtilization()
	{
		return absoluteAverageMemoryUtilization;
	}

	public double getPercentageAverageMemoryUtilization()
	{
		return percentageAverageMemoryUtilization;
	}


}
