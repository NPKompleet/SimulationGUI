package org.eclipse.app4mc.visualization.timeline.ui;

import java.util.List;

public interface ISimView {
	public void setSimTimeValue(String value);

	public void setSimTimeUnitIndex(int index);

	public void setPeriodicTasks(List<String> taskList);
}
