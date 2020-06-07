package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.concurrent.TimeUnit;

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
		while (true) {
			Job job = new Job(this, name + "_job", executionTime, deadline, model);
			job.activateAfter(this);
			hold(new TimeSpan(period, TimeUnit.MILLISECONDS));
		}
	}

}
