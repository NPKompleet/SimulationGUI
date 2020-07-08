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
import org.eclipse.app4mc.visualization.timeline.utils.TaskUtil;
import org.eclipse.app4mc.visualization.timeline.utils.TimingUtils;
import org.eclipse.app4mc.visualization.timeline.utils.UnalignedPeriodException;

public class Controller {
	Amalthea model;
	ISimView viewer;
	private LinkedHashMap<String, List<String>> filterData;

	public Controller(Amalthea model, ISimView viewer) {
		this.model = model;
		this.viewer = viewer;
	}

	public void populateView() {
		SWModel swModel = model.getSwModel();
		// To be moved to its own method or class
		LinkedHashMap<String, Time> taskPeriodMap = TimingUtils.getPeriodMap(swModel);
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
		int simTimeValue = Integer.valueOf(simParams.getSimTime());
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
			stepSizeValue = TimingUtils.convertTimeUnit(overheadValue, overheadUnit, simTimeUnit).intValue();

		String etmValue = simParams.getEtm();
		String preemption = simParams.getPreemption();

		LinkedHashMap<String, List<Task>> processorToTaskMap = TaskUtil.getAlmatheaProcessorToTaskMap(model);
		filterData = TaskUtil.getProcessNameToTaskNameMap(processorToTaskMap);
		System.out.println(filterData.size());
		viewer.enableFiltering();
	}

	public LinkedHashMap<String, List<String>> getFilterData() {
		return filterData;
	}

}
