package org.eclipse.app4mc.visualization.timeline.simulation;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

public class Job extends SimProcess {
	private Task task;
	private int executionTime;
	private int deadline;
	private SimModel model;
	String name;

	public Job(Task task, String name, int executionTime, int deadline, Model model) {
		super(model, name, true);
		this.task = task;
		this.name = name;
		this.executionTime = executionTime;
		this.deadline = deadline;
		this.model = (SimModel) model;
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		model.jobQueue.addLast(this);

		if (!model.processorQueue.isEmpty()) {
			Processor assignedProcessor = model.processorQueue.first();
			model.processorQueue.remove(assignedProcessor);

			assignedProcessor.activateAfter(this);
		}

		passivate();
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

	public int getDeadline() {
		return deadline;
	}

}
