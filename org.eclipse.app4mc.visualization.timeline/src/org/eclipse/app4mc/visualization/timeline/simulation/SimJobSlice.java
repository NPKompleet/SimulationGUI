package org.eclipse.app4mc.visualization.timeline.simulation;

public class SimJobSlice {
	private SimTask parentTask;
	private int activationTime;
	private int executionTime;
	private String name;

	public SimJobSlice() {
	}

	public SimJobSlice(SimTask parentTask, String name, int activationTime, int executionTime) {
		this.parentTask = parentTask;
		this.activationTime = activationTime;
		this.name = name;
		this.executionTime = executionTime;
	}

	public SimTask getParentTask() {
		return parentTask;
	}

	public void setParentTask(SimTask parentTask) {
		this.parentTask = parentTask;
	}

	public int getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(int activationTime) {
		this.activationTime = activationTime;
	}

	public int getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
