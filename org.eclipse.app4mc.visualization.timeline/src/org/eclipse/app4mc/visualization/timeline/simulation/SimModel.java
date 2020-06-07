package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeInstant;

public class SimModel extends Model {
	protected static int NUM_OF_PROCESSORS = 1;
	protected ProcessQueue<Job> jobQueue;
	protected ProcessQueue<Processor> processorQueue;

	public SimModel(Model arg0, String arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	@Override
	public String description() {
		return "A simulation of tasks and schedulers";
	}

	@Override
	public void doInitialSchedules() {
		Processor processor = new Processor(this, "Processor", true);
		processor.activate();

		Task task1 = new Task("Task1", 2, 6, 10, 0, this);
		task1.activate();
		Task task2 = new Task("Task2", 3, 4, 5, 0, this);
		task2.activate();
	}

	@Override
	public void init() {
		jobQueue = new ProcessQueue<Job>(this, "Job Queue", false, true);
		processorQueue = new ProcessQueue<Processor>(this, "Processor Queue", false, true);
	}

	public static void main(String[] args) {
		SimModel model = new SimModel(null, "Simple Sim", true, true);
		Experiment experiment = new Experiment("SimExperiment");
		model.connectToExperiment(experiment);
		experiment.setShowProgressBar(true);
		experiment.stop(new TimeInstant(20, TimeUnit.MILLISECONDS));
		experiment.tracePeriod(new TimeInstant(0), new TimeInstant(20, TimeUnit.MILLISECONDS));
		experiment.start();
		experiment.report();
		experiment.finish();
	}

}
