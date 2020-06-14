package org.eclipse.app4mc.visualization.timeline.simulation;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class Task extends SimProcess {
	String name;
	private int executionTime;
	private int deadline;
	private int period;
	private int offset;
	private SimModel model;

	public Task(String name, int executionTime, int deadline, int period, int offset, Model model) {
		super(model, name, false);
		this.name = name;
		this.executionTime = executionTime;
		this.deadline = deadline;
		this.period = period;
		this.offset = offset;
		this.model = (SimModel) model;
	}

	@Override
	public void lifeCycle() throws SuspendExecution {

		hold(new TimeSpan(offset));
		// Activation time for first job
		int activationTime = offset;
		// Deadline for first job
		deadline += offset;

		while (true) {
			Job job = new Job(this, name + "_job", activationTime, executionTime, deadline, model);
			job.activate();
			hold(new TimeSpan(period));
			activationTime += period;
			// Use absolute deadline for subsequent jobs
			deadline += period;
		}
	}

}
