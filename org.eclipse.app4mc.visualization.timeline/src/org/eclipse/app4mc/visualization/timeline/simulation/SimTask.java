package org.eclipse.app4mc.visualization.timeline.simulation;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class SimTask extends SimProcess {
	private String name;
	private int executionTime;
	private int deadline;
	private int period;
	private int offset;
	private SimModel model;

	public SimTask(Model arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	public SimTask(String name, int executionTime, int deadline, int period, int offset, Model model) {
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
		int jobDeadline = deadline;
		jobDeadline += offset;

		while (true) {
			SimJob job = new SimJob(this, name + "_job", activationTime, executionTime, jobDeadline, model);
			job.activate();
			hold(new TimeSpan(period));
			activationTime += period;
			// Use absolute deadline for subsequent jobs
			jobDeadline += period;
		}
	}

	public int getPeriod() {
		return period;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getOffset() {
		return offset;
	}

	public int getDeadline() {
		return deadline;
	}
}
