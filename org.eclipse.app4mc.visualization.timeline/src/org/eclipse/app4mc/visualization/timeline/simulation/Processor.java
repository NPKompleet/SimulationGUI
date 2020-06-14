package org.eclipse.app4mc.visualization.timeline.simulation;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class Processor extends SimProcess {
	private SimModel model;

	public Processor(Model model, String name, boolean showInTrace) {
		super(model, name, showInTrace);
		this.model = (SimModel) model;
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
