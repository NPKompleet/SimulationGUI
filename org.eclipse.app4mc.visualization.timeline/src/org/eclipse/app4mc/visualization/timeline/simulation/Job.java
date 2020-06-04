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
		super(model, name, false);
		this.task = task;
		this.name = name;
		this.executionTime = executionTime;
		this.deadline = deadline;
		this.model = (SimModel) model;
	}

	public Job(Model arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		// TODO Auto-generated method stub

	}

}
