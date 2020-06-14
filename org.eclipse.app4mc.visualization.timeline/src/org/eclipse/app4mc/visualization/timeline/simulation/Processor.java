package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class Processor extends SimProcess {
	private SimModel model;
	private Scheduler scheduler;
	Job currentJob;
	protected boolean isBusy;

	public Processor(Model model, String name, boolean showInTrace) {
		super(model, name, showInTrace);
		this.model = (SimModel) model;
		this.scheduler = new Scheduler();
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		while (true) {
			if (model.jobQueue.isEmpty()) {
				model.processorQueue.insert(this);
				isBusy = false;
				passivate();
			} else {
				if (model.doSchedule) {
					model.jobQueue = scheduler.schedule(model.jobQueue);
					model.setNextSchedule(false);
					hold(new TimeSpan(model.schedulerOverhead));
					sendTraceNote("A Schedule!!!");
				}
				isBusy = true;
				currentJob = model.jobQueue.pollFirst();

				// Execute the Job
				hold(new TimeSpan(currentJob.getExecutionTime()));

				if (isInterrupted()) {
					sendTraceNote("interrupted!!");
					// Still need to implement calculating remaining execution time
					model.jobQueue.addFirst(currentJob);
					model.jobQueue = scheduler.schedule(model.jobQueue);
					this.clearInterruptCode();
				} else {
					currentJob.activate();
					isBusy = false;
				}
			}
		}
	}

	public boolean checkIsPriority(Job job) {
		if (currentJob != null && job == Stream.of(job, currentJob).min(scheduler.getComparator())
				.orElseThrow(NoSuchElementException::new))
			return true;
		return false;
	}

}
