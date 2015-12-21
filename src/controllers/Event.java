package controllers;

import models.Process;

public class Event {
	public static enum Type {
		PROCESS_ARRIVAL, PROCESS_DEPARTURE
	}
	private int time;
	private Type type;
	private Event nextEvent;
	private Process process;
	public Event(Type type, int time, Process process) {
		this.setType(type);
		this.setTime(time);
		this.setProcess(process);
		this.setNextEvent(null);
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public Event getNextEvent() {
		return nextEvent;
	}
	public void setNextEvent(Event nextEvent) {
		this.nextEvent = nextEvent;
	}
	public Process getProcess() {
		return process;
	}
	public void setProcess(Process process) {
		this.process = process;
	}
	public void print() {
		if (type == Type.PROCESS_ARRIVAL)
			System.out.print("ARRIVAL ");
		else
			System.out.print("DEPARTURE ");
		System.out.println(process.getName() + " " + process.getSize() + " " + time);
		
	}
}
