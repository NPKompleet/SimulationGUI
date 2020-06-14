package org.eclipse.app4mc.visualization.timeline.simulation;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class Processor extends SimProcess {
	private SimModel model;

	public Processor(Model arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
		this.model = (SimModel) arg0;
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		while (true) {
			if (model.jobQueue.isEmpty()) {
				model.processorQueue.insert(this);
				passivate();
			} else {
				Job nextJob = model.jobQueue.pollFirst();

				// Execute the Job
				hold(new TimeSpan(nextJob.getExecutionTime()));

				nextJob.activate();
			}
		}
	}

}
