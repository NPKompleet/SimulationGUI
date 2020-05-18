package org.eclipse.app4mc.visualization.timeline.models;

public class TaskJob {
	private int releaseTime;
	private int executionTime;
	private int period;
	private int deadline;
	private String name;

	public TaskJob(int releaseTime, int executionTime, int period, int deadline, String name) {
		this.releaseTime = releaseTime;
		this.executionTime = executionTime;
		this.period = period;
		this.deadline = deadline;
		this.name = name;
	}

	public int getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(int releaseTime) {
		this.releaseTime = releaseTime;
	}

	public int getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
