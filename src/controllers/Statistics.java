package controllers;

import models.Process;

public class Statistics
{
	private int finishedProcesses, cumulativeTimeInTheSystem;
	private int cumulativeWaitingTime;
	private int maxBufferSize;
	private int bufferSizeArea;
	private int cumulativeOccupiedSize;

	public Statistics()
	{
		finishedProcesses = 0;
		cumulativeTimeInTheSystem = 0;
		cumulativeWaitingTime = 0;
		maxBufferSize = 0;
		bufferSizeArea = 0;
		cumulativeOccupiedSize = 0;
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

	public double getAverageBufferSize(int currentSimulationTime)
	{
		return bufferSizeArea * 1.0 / currentSimulationTime;
	}

	public int getMaxBufferSize()
	{
		return maxBufferSize;
	}

	public int getNumberFinishedProcesses()
	{
		return finishedProcesses;
	}

	public double getAverageTimeInSystem()
	{
		return cumulativeTimeInTheSystem * 1.0 / finishedProcesses;
	}

	public double getAverageWaitingTime()
	{
		return cumulativeWaitingTime * 1.0 / finishedProcesses;
	}

	public double getAbsoluteAverageMemoryUtilization(int currentSimulationTime)
	{
		return cumulativeOccupiedSize * 1.0 / currentSimulationTime;
	}

	

	/* etsef ya 7abiby we gebly el simulation time enta */
	public double getPercentageAverageMemoryUtilization()
	{
		return cumulativeOccupiedSize * 1.0 / 100 / 100;
	}


}
