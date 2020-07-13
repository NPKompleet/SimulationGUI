package org.eclipse.app4mc.visualization.timeline.ui;

import java.util.List;

import org.eclipse.app4mc.visualization.timeline.simulation.SimJobSlice;

public interface ISimView {
	public void setSimTimeValue(String value);

	public void setSimTimeUnitIndex(int index);

	public void setPeriodicTasks(List<String> taskList);

	public void enableFiltering();

	public void createVisualization(List<SimJobSlice> processedJobList);
}
