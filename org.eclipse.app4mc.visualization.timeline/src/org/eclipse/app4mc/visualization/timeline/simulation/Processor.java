package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeOperations;
import desmoj.core.simulator.TimeSpan;

public class Processor extends SimProcess {
	private SimModel model;
	private Scheduler scheduler;
	SimJob currentJob;
//	protected boolean isBusy;
	protected List<SimJobSlice> processedJobList;
	protected SimJobSlice jobSlice;

	public Processor(Model model, String name, boolean showInTrace, Scheduler scheduler) {
		super(model, name, showInTrace);
		this.model = (SimModel) model;
		this.scheduler = scheduler;
		this.processedJobList = new ArrayList<>();
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		while (true) {
			if (model.jobQueue.isEmpty()) {
				model.processorQueue.insert(this);
//				isBusy = false;
				passivate();
			} else {
//				if (model.doSchedule) {
				hold(new TimeSpan(model.schedulerOverhead));
				model.jobQueue = scheduler.schedule(model.jobQueue);
//				model.setNextSchedule(false);

				sendTraceNote("A Schedule!!!");
//				}
//				isBusy = true;
				currentJob = model.jobQueue.pollFirst();
				TimeInstant startTime = presentTime();

				jobSlice = new SimJobSlice();
				jobSlice.setParentTask(currentJob.getParentTask());
				jobSlice.setName(currentJob.getQuotedName());
				jobSlice.setActivationTime((int) startTime.getTimeAsDouble());

				// Execute the Job
				TimeSpan executionTime = new TimeSpan(currentJob.getExecutionTime());
				hold(executionTime);

				// If processor is interrupted by job of higher priority,
				// set the execution time of the current running job to the difference
				// between the initial execution time and the amount of time the processor
				// has executed the job. Then put job in the head of job queue and
				// reschedule all jobs in the job queue.
				if (isInterrupted()) {
					sendTraceNote("interrupted!! " + currentJob.getQuotedName() + " " + startTime + " "
							+ model.presentTime());

					TimeSpan processingTime = TimeOperations.diff(presentTime(), startTime);
					int processingTimeValue = (int) processingTime.getTimeAsDouble();
					if (processingTimeValue > 0) {
						jobSlice.setExecutionTime(processingTimeValue);
						processedJobList.add(jobSlice);
					}
					jobSlice = null;

					executionTime = TimeOperations.diff(executionTime, TimeOperations.diff(presentTime(), startTime));
					sendTraceNote("interrupted!! exe " + executionTime);
					currentJob.setExecutionTime((int) (executionTime.getTimeAsDouble()));
					model.jobQueue.addFirst(currentJob);
//					currentJob = null;
					hold(new TimeSpan(0.005));
					model.jobQueue = scheduler.schedule(model.jobQueue);
					this.clearInterruptCode();
				} else {
					jobSlice.setExecutionTime((int) (executionTime.getTimeAsDouble()));
					processedJobList.add(jobSlice);
					jobSlice = null;

					currentJob.activate();
//					isBusy = false;
				}
			}
		}
	}

	public boolean checkIsPriority(SimJob job) {
		if (currentJob != null && job == Stream.of(job, currentJob)
				.min(scheduler.getComparator().thenComparing(SimJob::getActivationTime))
				.orElseThrow(NoSuchElementException::new))
			return true;
		return false;
	}

	public List<SimJobSlice> getProcessedJobList() {
		return processedJobList;
	}

}
