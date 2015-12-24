package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import models.Hole;
import models.Process;
import random.GaussianRandomGenerator;
import random.ExponentialRandomGenerator;
import random.UniformRandomGenerator;
import exceptions.NoSpaceAvailableException;

public class SimulationController
{
	/* constants */
	
	
	/* fields */
	private List<Hole> holes;
	private List<Process> processes, queue;
	private Event headEvent;
	private ExponentialRandomGenerator interArrivalTimeGenerator;
	private UniformRandomGenerator serviceTimeGenerator;
	private GaussianRandomGenerator processSizeGenerator;
	private int currentSimulationTime, previousEventTime, numProcesses, occupiedSize;
	private Statistics statistics;
	private String eventsDescription;

	/* el fields elly 7abiby beya5odha mn el GUI */
	private String algorithm;
	private int memorySize, averageInterrival, meanProcessSize;
	private double varianceProcessSize;

	/* constructor */
	public SimulationController()
	{
		this.holes = new ArrayList<>();
		this.processes = new ArrayList<>();
		this.headEvent = null;
		this.queue = new ArrayList<>();
		
		this.interArrivalTimeGenerator = new ExponentialRandomGenerator(0.2);
		this.serviceTimeGenerator = new UniformRandomGenerator(1, 10);
		this.processSizeGenerator = new GaussianRandomGenerator(100, 1);
		
		this.currentSimulationTime = 0;
		this.previousEventTime = 0;
		this.occupiedSize = 0;
		this.numProcesses = 0;
		this.memorySize = 0;
		this.averageInterrival = 0;
		this.meanProcessSize = 0;
		this.varianceProcessSize = 0;
		this.statistics = new Statistics();
	}

	/* setters and getters */
	public List<Hole> getHoles()
	{
		return holes;
	}

	public List<Process> getProcesses()
	{
		return processes;
	}

	public void setAlgorithm(String algorithm)
	{
		this.algorithm = algorithm;
	}

	public void setMemorySize(int memorySize)
	{
		this.memorySize = memorySize;
	}

	public void setAverageInterrival(int averageInterrival)
	{
		this.averageInterrival = averageInterrival;
		this.interArrivalTimeGenerator = new ExponentialRandomGenerator(1.0 / this.averageInterrival);
	}

	public void setMeanProcessSize(int meanProcessSize)
	{
		this.meanProcessSize = meanProcessSize;
		this.processSizeGenerator = new GaussianRandomGenerator(this.meanProcessSize, this.varianceProcessSize);

	}

	public void setVarianceProcessSize(double varianceProcessSize)
	{
		this.varianceProcessSize = varianceProcessSize;
		this.processSizeGenerator = new GaussianRandomGenerator(this.meanProcessSize, this.varianceProcessSize);
	}

	public Statistics getStatistics()
	{
		statistics.computeStatistics(currentSimulationTime, memorySize);
		return statistics;
	}
	
	/* methods */

	private void firstFit(Process newProcess) throws NoSpaceAvailableException
	{
		// find the first hole that fits
		int size = newProcess.getSize();
		Hole firstHole = null;
		for (Hole hole : holes)
			if (hole.getSize() >= size)
			{
				firstHole = hole;
				break;
			}
		if (firstHole == null)
			throw new NoSpaceAvailableException();

		// add the process to that hole
		newProcess.setMemoryEntryTime(currentSimulationTime);
		scheduleDepartureEvent(newProcess);
		firstHole.addProcess(newProcess);
		occupiedSize += newProcess.getSize();
		processes.add(newProcess);
		eventsDescription += newProcess.getName() + " enters memory at t = " + currentSimulationTime + "\n";

		// remove the hole if its size became zero
		if (firstHole.getSize() == 0)
			holes.remove(firstHole);
	}

	private void bestFit(Process newProcess) throws NoSpaceAvailableException
	{
		// find the first hole that fits
		int size = newProcess.getSize();
		Hole bestHole = null;
		for (Hole hole : holes)
			if (hole.getSize() >= size)
				if (bestHole == null || (hole.getSize() < bestHole.getSize()))
					bestHole = hole;
		if (bestHole == null)
			throw new NoSpaceAvailableException();

		// add the process to that hole
		newProcess.setMemoryEntryTime(currentSimulationTime);
		scheduleDepartureEvent(newProcess);
		bestHole.addProcess(newProcess);
		occupiedSize += newProcess.getSize();
		processes.add(newProcess);
		eventsDescription += newProcess.getName() + " enters memory at t = " + currentSimulationTime + "\n";

		// remove the hole if its size became zero
		if (bestHole.getSize() == 0)
			holes.remove(bestHole);
	}

	private void worstFit(Process newProcess) throws NoSpaceAvailableException
	{
		// find the first hole that fits
		int size = newProcess.getSize();
		Hole worstHole = null;
		for (Hole hole : holes)
			if (hole.getSize() >= size)
				if (worstHole == null || (hole.getSize() > worstHole.getSize()))
					worstHole = hole;
		if (worstHole == null)
			throw new NoSpaceAvailableException();

		// add the process to that hole
		newProcess.setMemoryEntryTime(currentSimulationTime);
		scheduleDepartureEvent(newProcess);
		worstHole.addProcess(newProcess);
		occupiedSize += newProcess.getSize();
		processes.add(newProcess);
		eventsDescription += newProcess.getName() + " enters memory at t = " + currentSimulationTime + "\n";

		// remove the hole if its size became zero
		if (worstHole.getSize() == 0)
			holes.remove(worstHole);
	}

	public void initialize()
	{
		// holes and processes
		holes.clear();
		processes.clear();
		queue.clear();
		holes.add(new Hole(0, memorySize));
		
		// generators
		this.interArrivalTimeGenerator = new ExponentialRandomGenerator(averageInterrival);
		this.serviceTimeGenerator = new UniformRandomGenerator(2, 10);
		this.processSizeGenerator = new GaussianRandomGenerator(meanProcessSize, varianceProcessSize);
		
		// simulation details
		currentSimulationTime = 0;
		previousEventTime = 0;
		headEvent = null;
		statistics = new Statistics();
		numProcesses = 1;
		occupiedSize = 0;
		processSizeGenerator.nextRand();
		addEvent(new Event(Event.Type.PROCESS_ARRIVAL, 0,
				new Process((int) processSizeGenerator.nextRand(), "P0", 0)));
		
	}

	private void mergeHoles()
	{
		// sort holes according to their start
		Collections.sort(holes, new Comparator<Hole>()
		{
			@Override
			public int compare(Hole o1, Hole o2)
			{
				return Integer.compare(o1.getStart(), o2.getStart());
			}
		});
		
		
		// either append a hole or merge to the previous
		Hole prev = null;
		LinkedList<Hole> newHoles = new LinkedList<>();
		for (Hole hole : holes)
			if (prev != null && prev.isMergable(hole))
				prev.extendHole(hole);
			else
			{
				newHoles.add(hole);
				prev = hole;
			}
		holes = newHoles;		
	}

	public String singleStep()
	{
		Event event = pickHeadEvent();
		if (event == null)
			return "System is static\n";

		// initialize
		eventsDescription = "";
		previousEventTime = currentSimulationTime;
		currentSimulationTime = event.getTime();
		
		// update statistics
		updateSigmaStatistics();
		
		while (true)
		{

			// handle event
			if (event.getType() == Event.Type.PROCESS_ARRIVAL)
				processArrivalHandler(event);
			else if (event.getType() == Event.Type.PROCESS_DEPARTURE)
				processDepartureHandler(event);



			if (headEvent == null || headEvent.getTime() > currentSimulationTime)
				break;
			event = pickHeadEvent();
		}
		// return description of what happened
		return eventsDescription;

	}

	private void updateSigmaStatistics()
	{
		statistics.updateQueueLength(queue.size(), currentSimulationTime, previousEventTime);
		statistics.updateOccupiedSize(occupiedSize, currentSimulationTime, previousEventTime);
	}

	private void processDepartureHandler(Event event)
	{
		Process departuringProcess = event.getProcess();
		int start = departuringProcess.getStart();
		int size = departuringProcess.getSize();
		processes.remove(departuringProcess);
		occupiedSize -= size;
		holes.add(new Hole(start, size));
		mergeHoles();		
		eventsDescription += departuringProcess.getName() + " departure at t = " + currentSimulationTime + "\n";
		statistics.updateDepartureStatistics(departuringProcess);
		
		getFromQueue();
		departuringProcess.setDepartureTime(currentSimulationTime);
		
	}

	private void getFromQueue()
	{
		for (int i = 0; i < queue.size(); i++)
			if (algorithm == "First Fit")
				try
				{
					firstFit(queue.get(i));
					queue.remove(i);
					break;
				} catch (NoSpaceAvailableException e)
				{
					continue;
				}
			else if (algorithm == "Worst Fit")
				try
				{
					worstFit(queue.get(i));
					queue.remove(i);
					break;
				} catch (NoSpaceAvailableException e)
				{
					continue;
				}
			else if (algorithm == "Best Fit")
				try
				{
					bestFit(queue.get(i));
					queue.remove(i);
					break;
				} catch (NoSpaceAvailableException e)
				{
					continue;
				}

	}

	private void processArrivalHandler(Event event)
	{
		Process arrivingProcess = event.getProcess();
		eventsDescription += arrivingProcess.getName() + " arrives at t = " + currentSimulationTime + " with size = "
				+ arrivingProcess.getSize() + "\n";
		if (algorithm == "First Fit")
			try
			{
				firstFit(arrivingProcess);
			} catch (NoSpaceAvailableException e)
			{
				addProcessToQueue(event.getProcess());
			}
		else if (algorithm == "Worst Fit")
			try
			{
				worstFit(arrivingProcess);
			} catch (NoSpaceAvailableException e)
			{
				addProcessToQueue(event.getProcess());
			}
		else if (algorithm == "Best Fit")
			try
			{
				bestFit(arrivingProcess);
			} catch (NoSpaceAvailableException e)
			{
				addProcessToQueue(event.getProcess());
			}
		scheduleArrivalEvent();
	}

	private void addProcessToQueue(Process process)
	{
		queue.add(process);
		eventsDescription += process.getName() + " enters queue at t = " + currentSimulationTime + "\n";
	}

	private void scheduleDepartureEvent(Process process)
	{
		int serviceTime = (int) serviceTimeGenerator.nextRand();
		int departureTime = currentSimulationTime + serviceTime;
		process.setDepartureTime(departureTime);
		Event newEvent = new Event(Event.Type.PROCESS_DEPARTURE, departureTime, process);
		addEvent(newEvent);
	}

	private void scheduleArrivalEvent()
	{
		int interArrivaltime = (int) Math.ceil((interArrivalTimeGenerator.nextRand()));
		int arrivalTime = currentSimulationTime + interArrivaltime;
		int size = (int) processSizeGenerator.nextRand();
		Process newProcess = new Process(size, "P" + numProcesses, arrivalTime);
		numProcesses += 1;
		Event newEvent = new Event(Event.Type.PROCESS_ARRIVAL, arrivalTime, newProcess);
		addEvent(newEvent);
	}

	private Event pickHeadEvent()
	{
		if (headEvent == null)
			return null;
		Event retEvent = headEvent;
		headEvent = headEvent.getNextEvent();
		return retEvent;
	}

	private void addEvent(Event newEvent)
	{
		if (headEvent == null)
			headEvent = newEvent;
		else
		{
			Event curEvent = headEvent;
			if (newEvent.getTime() < curEvent.getTime())
			{
				newEvent.setNextEvent(curEvent.getNextEvent());
				curEvent.setNextEvent(newEvent);
				int time1, time2;
				Event.Type type1, type2;
				Process process1, process2;
				time1 = curEvent.getTime();
				type1 = curEvent.getType();
				process1 = curEvent.getProcess();

				time2 = newEvent.getTime();
				type2 = newEvent.getType();
				process2 = newEvent.getProcess();

				curEvent.setTime(time2);
				curEvent.setType(type2);
				curEvent.setProcess(process2);
				newEvent.setTime(time1);
				newEvent.setType(type1);
				newEvent.setProcess(process1);
			} else
			{
				while (curEvent.getNextEvent() != null && curEvent.getNextEvent().getTime() <= newEvent.getTime())
					curEvent = curEvent.getNextEvent();
				newEvent.setNextEvent(curEvent.getNextEvent());
				curEvent.setNextEvent(newEvent);
			}
		}

	}

	public static void main(String[] args)
	{
		// you can use this main for testing ya 7abiby
		SimulationController controller = new SimulationController();
		controller.initialize();
		controller.setAlgorithm("best fit");
		for (int i = 0; i < 10; i++)
			System.out.println(controller.singleStep());
	}

}
