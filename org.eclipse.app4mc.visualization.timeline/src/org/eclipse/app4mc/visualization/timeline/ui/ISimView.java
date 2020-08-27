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

	public void disableSimulation();

	public void showMessage(String tiltle, String message);
}
