package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.concurrent.LinkedBlockingDeque;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.InterruptCode;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeInstant;

public class SimModel extends Model {
	protected static int NUM_OF_PROCESSORS = 1;
	protected LinkedBlockingDeque<Job> jobQueue;
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
		processor = new Processor(this, "Processor", true);
		processor.activate();

		Task task1 = new Task("Task1", 9, 9, 10, 0, this);
		task1.activate();
		Task task2 = new Task("Task2", 3, 4, 5, 0, this);
		task2.activate();
		Task task3 = new Task("Task3", 3, 3, 4, 5, this);
		task3.activate();
	}

	@Override
	public void init() {
		jobQueue = new LinkedBlockingDeque<Job>();
		processorQueue = new ProcessQueue<Processor>(this, "Processor Queue", false, false);
		priorityJobCode = new InterruptCode("priority job arrived");
	}

	protected synchronized void setNextSchedule(boolean state) {
		doSchedule = state;
	}

	public static void main(String[] args) {
		SimModel model = new SimModel(null, "Simple Sim", true, true);
		Experiment experiment = new Experiment("SimExperiment");
		model.connectToExperiment(experiment);
		experiment.setShowProgressBar(true);
		experiment.stop(new TimeInstant(30));
		experiment.tracePeriod(new TimeInstant(0), new TimeInstant(30));
		experiment.start();
		experiment.report();
		experiment.finish();
	}

}
