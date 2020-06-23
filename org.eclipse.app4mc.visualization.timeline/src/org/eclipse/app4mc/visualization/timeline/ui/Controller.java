package org.eclipse.app4mc.visualization.timeline.ui;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.visualization.timeline.utils.TimingUtils;

public class Controller {
	Amalthea model;
	ISimView viewer;

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
		BigInteger hyperperiod = TimingUtils.computeHyperPeriod(periodList);
		unit = unit == org.eclipse.app4mc.amalthea.model.TimeUnit.PS ? org.eclipse.app4mc.amalthea.model.TimeUnit.NS
				: unit;
		int index = TimingUtils.timeToConstantMap().get(unit);

		viewer.setSimTimeValue(hyperperiod.toString());
		viewer.setSimTimeUnitIndex(index);
		viewer.setPeriodicTasks(taskPeriodMap.keySet().stream().collect(Collectors.toList()));
	}

}
