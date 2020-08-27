/*******************************************************************************
 * Copyright (c) 2020 Philip Okonkwo and others.
 * 
 * This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Philip Okonkwo - initial API and implementation
 *******************************************************************************/
package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import desmoj.core.simulator.InterruptCode;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeInstant;

public class SimModel extends Model {
	protected static int NUM_OF_PROCESSORS = 1;
	protected LinkedBlockingDeque<SimJob> jobQueue;
	protected ProcessQueue<Processor> processorQueue;
	protected double schedulerOverhead = 0.0001;
//	protected boolean doSchedule = false;
	protected Processor processor;
	protected InterruptCode priorityJobCode;
	protected String preemptiveness;
	private List<SimTaskParams> taskParamsList;
	private String processorName;
	private Scheduler scheduler;

	public SimModel(Model model, String name, boolean showInReport, boolean showInTrace) {
		super(model, name, showInReport, showInTrace);
	}

	@Override
	public String description() {
		return "A simulation of tasks and schedulers";
	}

	@Override
	public void doInitialSchedules() {
//		processor = new Processor(this, "Processor", true, new RMScheduler());
//		processor.activate();
//
//		SimTask task1 = new SimTask("Task1", 6, 9, 10, 0, this);
//		task1.activate();
//		SimTask task2 = new SimTask("Task2", 2, 4, 5, 0, this);
//		task2.activate();
//		SimTask task3 = new SimTask("Task3", 3, 3, 6, 5, this);
//		task3.activate();

		processor = new Processor(this, processorName, true, scheduler);
		processor.activate();

		for (SimTaskParams tParams : taskParamsList) {
			System.out.println(tParams.getName());
			System.out.println(tParams.getExecutionTime());

			SimTask task = new SimTask(tParams, this);
			task.activate();
		}

	}

	@Override
	public void init() {
		jobQueue = new LinkedBlockingDeque<SimJob>();
		processorQueue = new ProcessQueue<Processor>(this, "Processor Queue", false, false);
		priorityJobCode = new InterruptCode("priority job arrived");
	}

//	protected synchronized void setNextSchedule(boolean state) {
//		doSchedule = state;
//	}

	public Processor getProcessor() {
		return processor;
	}

	public void finalize(TimeInstant instant) {
		if (processor.jobSlice != null) {
			processor.jobSlice
					.setExecutionTime((int) instant.getTimeAsDouble() - processor.jobSlice.getActivationTime());
			processor.processedJobList.add(processor.jobSlice);
			processor.jobSlice = null;
		}

		for (SimTaskParams tParams : taskParamsList) {

			SimTask task = new SimTask(tParams, this);
			SimJobSlice jobSlice = new SimJobSlice();
			jobSlice.setParentTask(task);
			jobSlice.setName(task.getName());
			jobSlice.setActivationTime((int) presentTime().getTimeAsDouble());
			jobSlice.setExecutionTime(0);
			processor.processedJobList.add(jobSlice);
		}

	}

	public void setTaskParamsList(List<SimTaskParams> taskParamsList) {
		this.taskParamsList = taskParamsList;
	}

	public Scheduler getScheduleStrategy() {
		return scheduler;
	}

	public void setScheduleStrategy(String strategy) {
		this.scheduler = Scheduler.getScheduler(strategy);
	}

	public String getPreemptiveness() {
		return preemptiveness;
	}

	public void setPreemptiveness(String preemptiveness) {
		this.preemptiveness = preemptiveness;
	}

	public String getProcessorName() {
		return processorName;
	}

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}

	public void setSchedulerOverhead(int schedulerOverhead) {
		if (schedulerOverhead != 0)
			this.schedulerOverhead = schedulerOverhead;
	}

//	public static void main(String[] args) {
//		SimModel model = new SimModel(null, "Simple Sim", true, true);
//		Experiment experiment = new Experiment("SimExperiment");
//		model.connectToExperiment(experiment);
//		experiment.setShowProgressBar(true);
//		experiment.stop(new TimeInstant(30));
//		experiment.tracePeriod(new TimeInstant(0), new TimeInstant(30));
//		experiment.start();
//		experiment.report();
//		experiment.finish();
//	}

}
