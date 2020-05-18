package org.eclipse.app4mc.visualization.timeline.models;

public class TaskInstance {
	private int start;
	private int end;
	private int usage = 0;
	private int priority;
	private int id;
	private String name;
	private static int UNIQUE_ID = 1;

	public TaskInstance(int start, int end, int priority, String name) {
		this.start = start;
		this.end = end;
		this.priority = priority;
		this.name = name;
		this.id = UNIQUE_ID++;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getUsage() {
		return usage;
	}

	public void setUsage(int usage) {
		this.usage = usage;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addUsage(int usage) {
		this.usage += usage;
	}

	public boolean isCompleted() {
		if (this.usage >= this.end - this.start)
			return true;
		return false;
	}

}
