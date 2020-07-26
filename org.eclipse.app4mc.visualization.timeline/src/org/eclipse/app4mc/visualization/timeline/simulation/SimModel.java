package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import org.eclipse.app4mc.visualization.timeline.schedulers.EDFScheduler;
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
	LinkedHashMap<String, List<SimTaskParams>> processorToSimTaskMap;
	Scheduler scheduler;

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

		List<String> processorList = processorToSimTaskMap.keySet().stream().collect(Collectors.toList());
		List<SimTaskParams> taskParamsList = processorToSimTaskMap.values().stream().collect(Collectors.toList())
				.get(0);

		processor = new Processor(this, processorList.get(0), true, scheduler);
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

	public void setProcessorToSimTaskMap(LinkedHashMap<String, List<SimTaskParams>> processorToSimTaskMap) {
		this.processorToSimTaskMap = processorToSimTaskMap;
	}

	public Scheduler getScheduleStrategy() {
		return scheduler;
	}

	public void setScheduleStrategy(String strategy) {
		switch (strategy) {
		case "EDF":
			this.scheduler = new EDFScheduler();
			break;

		case "RM":
			this.scheduler = new RMScheduler();
			break;

		default:
			this.scheduler = new EDFScheduler();
			break;
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
