package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import exceptions.NoSpaceAvailableException;
import models.Hole;
import models.Process;

public class SimulationController
{
	/* constants */
	public static int INITIAL_HOLE_SIZE = 1000;

	/* fields */
	private List<Hole> holes;
	private List<Process> processes;

	/* constructor */
	public SimulationController()
	{
		this.holes = new ArrayList<>();
		this.processes = new ArrayList<>();
	}

	/* setters and getters */

	public List<Hole> getHoles()
	{
		return holes;
	}

	public void setHoles(List<Hole> holes)
	{
		this.holes = holes;
	}

	public List<Process> getProcesses()
	{
		return processes;
	}

	public void setProcesses(List<Process> processes)
	{
		this.processes = processes;
	}

	/* methods */

	/* TODO - Add your code hena ya 7abiby */

	public void initialize()
	{
		holes.clear();
		processes.clear();
		holes.add(new Hole(0, INITIAL_HOLE_SIZE));

		// TODO - initialize your stuff here brdo ya 7abiby
	}

	/**
	 * merges holes that are next to each other
	 */
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

	private void firstFit(int size) throws NoSpaceAvailableException
	{
		// find the first hole that fits
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
		Process newProcess = new Process(size, "P" + (processes.size() + 1));
		firstHole.addProcess(newProcess);
		processes.add(newProcess);

		// remove the hole if its size became zero
		if (firstHole.getSize() == 0)
			holes.remove(firstHole);
	}

	private void bestFit(int size) throws NoSpaceAvailableException
	{
		// find the first hole that fits
		Hole bestHole = null;
		for (Hole hole : holes)
			if (hole.getSize() >= size)
				if (bestHole == null || (hole.getSize() < bestHole.getSize()))
					bestHole = hole;
		if (bestHole == null)
			throw new NoSpaceAvailableException();

		// add the process to that hole
		Process newProcess = new Process(size, "P" + (processes.size() + 1));
		bestHole.addProcess(newProcess);
		processes.add(newProcess);

		// remove the hole if its size became zero
		if (bestHole.getSize() == 0)
			holes.remove(bestHole);
	}

	private void worstFit(int size) throws NoSpaceAvailableException
	{
		// find the first hole that fits
		Hole worstHole = null;
		for (Hole hole : holes)
			if (hole.getSize() >= size)
				if (worstHole == null || (hole.getSize() > worstHole.getSize()))
					worstHole = hole;
		if (worstHole == null)
			throw new NoSpaceAvailableException();

		// add the process to that hole
		Process newProcess = new Process(size, "P" + (processes.size() + 1));
		worstHole.addProcess(newProcess);
		processes.add(newProcess);

		// remove the hole if its size became zero
		if (worstHole.getSize() == 0)
			holes.remove(worstHole);
	}
	
	/**
	 * handles a single event in the simulation
	 * 
	 * @param algorithm
	 *            "First Fit", "Best Fit", "Worst Fit"
	 * @return readable description of the event e.g Arrival P1 t=2 size=100
	 */
	public String singleStep(String algorithm)
	{
		Random rand = new Random();
		int size = rand.nextInt(400) + 1;
		try
		{
			worstFit(100);
		} catch (NoSpaceAvailableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Arrival P1 t=2 size=100";

	}
	
	
	public static void main(String[] args)
	{
		// you can use this main for testing ya 7abiby
	}


}
