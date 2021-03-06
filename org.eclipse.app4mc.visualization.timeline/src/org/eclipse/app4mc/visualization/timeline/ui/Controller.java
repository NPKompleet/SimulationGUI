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
package org.eclipse.app4mc.visualization.timeline.ui;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.visualization.timeline.simulation.SimJobSlice;
import org.eclipse.app4mc.visualization.timeline.simulation.SimModel;
import org.eclipse.app4mc.visualization.timeline.simulation.SimTaskParams;
import org.eclipse.app4mc.visualization.timeline.utils.GlobalSchedulingException;
import org.eclipse.app4mc.visualization.timeline.utils.TaskUtil;
import org.eclipse.app4mc.visualization.timeline.utils.TimingUtils;
import org.eclipse.app4mc.visualization.timeline.utils.UnalignedPeriodException;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;

public class Controller {
	Amalthea model;
	ISimView viewer;
	private LinkedHashMap<String, List<String>> filterData;
	private LinkedHashMap<String, List<SimJobSlice>> processedJobMap;
	private int simTimeValue;
	private java.util.concurrent.TimeUnit timeUnit;

	public Controller(Amalthea model, ISimView viewer) {
		this.model = model;
		this.viewer = viewer;
	}

	public void populateView() {
		SWModel swModel = model.getSwModel();
		// To be moved to its own method or class
		LinkedHashMap<String, Time> taskPeriodMap = null;
		try {
			taskPeriodMap = TimingUtils.getPeriodMap(swModel);
		} catch (Exception e) {
			viewer.disableSimulation();
			String message = "This model does not seem to have elements that can be used in the simulation. "
					+ "Please check the model again.";
			viewer.showMessage("Incomplete Model", message);
			return;
		}
		java.util.List<Time> periodList = taskPeriodMap.values().stream().collect(Collectors.toList());
		org.eclipse.app4mc.amalthea.model.TimeUnit unit = TimingUtils.getMaximumTimeUnit(periodList);

		// Align all periods to the same base time unit using the maximum time unit
		// If maximum unit in the model is in picoseconds, align everything to
		// nanoseconds instead and change the unit to nanoseconds.
		// Reason: Java and the Timeline widget uses the minimum time unit of
		// nanoseconds.
		periodList = unit == org.eclipse.app4mc.amalthea.model.TimeUnit.PS
				? TimingUtils.getAlignedPeriods(periodList, org.eclipse.app4mc.amalthea.model.TimeUnit.NS)
				: TimingUtils.getAlignedPeriods(periodList, unit);
		BigInteger hyperperiod = null;
		try {
			hyperperiod = TimingUtils.computeHyperPeriod(periodList);
		} catch (UnalignedPeriodException e) {
			e.printStackTrace();
		}
		unit = unit == org.eclipse.app4mc.amalthea.model.TimeUnit.PS ? org.eclipse.app4mc.amalthea.model.TimeUnit.NS
				: unit;
		int index = TimingUtils.TIME_TO_CONSTANT_MAP.get(unit);

		viewer.setSimTimeValue(hyperperiod.toString());
		viewer.setSimTimeUnitIndex(index);
		viewer.setPeriodicTasks(taskPeriodMap.keySet().stream().collect(Collectors.toList()));
	}

	private TimeUnit getTimeUnitFromString(String unitString) {
		return TimingUtils.TIME_UNIT_STRING_TO_TIME_MAP.get(unitString);
	}

	public void startSimulation(SimViewParameters simParams) {
		String strategy = simParams.getStrategy();
		simTimeValue = Integer.valueOf(simParams.getSimTime());
		TimeUnit simTimeUnit = getTimeUnitFromString(simParams.getSimTimeUnit());

		// The simulation time unit is the reference time unit
		// the time unit for all other values will be converted to that
		// if they are different
		int stepSizeValue = Integer.valueOf(simParams.getStepSize());
		TimeUnit stepSizeUnit = getTimeUnitFromString(simParams.getStepSizeUnit());
		if (stepSizeUnit != simTimeUnit)
			stepSizeValue = TimingUtils.convertTimeUnit(stepSizeValue, stepSizeUnit, simTimeUnit).intValue();
		int overheadValue = Integer.valueOf(simParams.getOverhead());
		TimeUnit overheadUnit = getTimeUnitFromString(simParams.getOverheadUnit());
		if (overheadUnit != simTimeUnit)
			overheadValue = TimingUtils.convertTimeUnit(overheadValue, overheadUnit, simTimeUnit).intValue();

		String etmValue = simParams.getEtm();
		String preemption = simParams.getPreemption();

		LinkedHashMap<String, List<Task>> processorToTaskMap = null;
		try {
			processorToTaskMap = TaskUtil.getAlmatheaProcessorToTaskMap(model);
		} catch (GlobalSchedulingException e) {
			viewer.disableSimulation();
			viewer.showMessage(e.toString(), e.getMessage());
			return;
		}

		LinkedHashMap<String, List<SimTaskParams>> processorToSimTaskMap = TaskUtil
				.getProcessorToSimTaskMap(processorToTaskMap, simTimeUnit, etmValue);

		processedJobMap = new LinkedHashMap<>();

		for (String key : processorToSimTaskMap.keySet()) {
			List<SimTaskParams> taskParamsList = processorToSimTaskMap.get(key);

			// Simulation
			SimModel simModel = new SimModel(null, "Simple Sim", true, true);
			simModel.setScheduleStrategy(strategy);
			simModel.setTaskParamsList(taskParamsList);
			simModel.setPreemptiveness(preemption);
			simModel.setSchedulerOverhead(overheadValue);
			simModel.setProcessorName("Processor");

			Experiment experiment = new Experiment("SimExperiment");
			simModel.connectToExperiment(experiment);
			experiment.setShowProgressBar(false);
			experiment.stop(new TimeInstant(simTimeValue));
//			experiment.tracePeriod(new TimeInstant(0), new TimeInstant(simTimeValue));
			experiment.start();
//			experiment.report();
			experiment.finish();
			simModel.finalize(simModel.presentTime());

			processedJobMap.put(key, simModel.getProcessor().getProcessedJobList());
		}

		timeUnit = TimingUtils.AMALTHEA_TO_JAVA_TIME_MAP.get(simTimeUnit);
		createVisualization(processedJobMap, simTimeValue, timeUnit);

		filterData = TaskUtil.getProcessNameToTaskNameMap(processorToTaskMap);
		viewer.enableFiltering();
	}

	private void createVisualization(LinkedHashMap<String, List<SimJobSlice>> processedJobMap, int simTime,
			java.util.concurrent.TimeUnit simTimeUnit) {
		viewer.createVisualization(processedJobMap, simTime, simTimeUnit);
	}

	public LinkedHashMap<String, List<String>> getFilterData() {
		return filterData;
	}

	public LinkedHashMap<String, List<SimJobSlice>> getProcessedJobMap() {
		return processedJobMap;
	}

	public void filterVisualization(LinkedHashMap<String, List<SimJobSlice>> filteredProcessedJobMap) {
		createVisualization(filteredProcessedJobMap, simTimeValue, timeUnit);
	}

}
