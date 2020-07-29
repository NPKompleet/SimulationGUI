package org.eclipse.app4mc.visualization.timeline.ui;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.app4mc.visualization.timeline.simulation.SimJobSlice;

public interface ISimView {
	public void setSimTimeValue(String value);

	public void setSimTimeUnitIndex(int index);

	public void setPeriodicTasks(List<String> taskList);

	public void enableFiltering();

	public void createVisualization(LinkedHashMap<String, List<SimJobSlice>> processedJobMap, int simTime,
			TimeUnit simTimeUnit);
}
