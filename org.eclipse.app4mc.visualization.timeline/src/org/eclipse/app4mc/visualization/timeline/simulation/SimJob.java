package org.eclipse.app4mc.visualization.timeline.simulation;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

public class SimJob extends SimProcess {
	private SimTask parentTask;
	private int activationTime;
	private int executionTime;
	private int absoluteDeadline;
	private SimModel model;
	private String name;

	public SimJob(SimTask parentTask, String name, int activationTime, int executionTime, int deadline, Model model) {
		super(model, name, true);
		this.parentTask = parentTask;
		this.activationTime = activationTime;
		this.name = name;
		this.executionTime = executionTime;
		this.absoluteDeadline = deadline;
		this.model = (SimModel) model;
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		model.jobQueue.addLast(this);

		if (model.jobQueue.size() > 1) {
			model.setNextSchedule(true);
		}

		if (!model.processorQueue.isEmpty()) {
			Processor assignedProcessor = model.processorQueue.first();
			model.processorQueue.remove(assignedProcessor);

			assignedProcessor.activate();
		} else {
			if (model.preemptiveness.equals("PREEMPTIVE") && model.processor.checkIsPriority(this)) {
				model.processor.interrupt(model.priorityJobCode);
			}
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

	public int getAbsoluteDeadline() {
		return absoluteDeadline;
	}

	public SimTask getParentTask() {
		return parentTask;
	}

	public int getActivationTime() {
		return activationTime;
	}

}