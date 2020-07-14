package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.app4mc.visualization.timeline.schedulers.RMScheduler;

import desmoj.core.simulator.InterruptCode;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeInstant;

public class SimModel extends Model {
	protected static int NUM_OF_PROCESSORS = 1;
	protected LinkedBlockingDeque<SimJob> jobQueue;
	protected ProcessQueue<Processor> processorQueue;
	protected int schedulerOverhead = 0;
	protected boolean doSchedule = false;
	protected Processor processor;
	protected InterruptCode priorityJobCode;
	protected String preemptiveness = "PREEMPTIVE";

	public SimModel(Model model, String name, boolean showInReport, boolean showInTrace) {
		super(model, name, showInReport, showInTrace);
	}

	@Override
	public String description() {
		return "A simulation of tasks and schedulers";
	}

	@Override
	public void doInitialSchedules() {
		processor = new Processor(this, "Processor", true, new RMScheduler());
		processor.activate();

		SimTask task1 = new SimTask("Task1", 6, 9, 10, 0, this);
		task1.activate();
		SimTask task2 = new SimTask("Task2", 2, 4, 5, 0, this);
		task2.activate();
		SimTask task3 = new SimTask("Task3", 3, 3, 6, 5, this);
		task3.activate();
	}

	@Override
	public void init() {
		jobQueue = new LinkedBlockingDeque<SimJob>();
		processorQueue = new ProcessQueue<Processor>(this, "Processor Queue", false, false);
		priorityJobCode = new InterruptCode("priority job arrived");
	}

	protected synchronized void setNextSchedule(boolean state) {
		doSchedule = state;
	}

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
