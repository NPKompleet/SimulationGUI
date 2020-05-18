package org.eclipse.app4mc.visualization.timeline.models;

public class TaskSlice {
	private int start;
	private int end;
	private int id;
	private String name;

	public TaskSlice(int start, int end, String name, int id) {
		this.start = start;
		this.end = end;
		this.id = id;
		this.name = name;
	}

	public TaskSlice(int progressStep, TaskInstance instance) {
		this.start = progressStep;
		this.id = instance.getId();
		this.name = instance.getName();
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

}
