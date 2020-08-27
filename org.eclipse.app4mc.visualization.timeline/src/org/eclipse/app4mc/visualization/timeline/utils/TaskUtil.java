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
package org.eclipse.app4mc.visualization.timeline.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.ConstraintsUtil;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.util.HardwareUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.TimeType;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;
import org.eclipse.app4mc.visualization.timeline.simulation.SimTaskParams;

public class TaskUtil {

	/**
	 * Gets all the processing unit in an Amalthea model.
	 * 
	 * @param model The model to get the information from.
	 * @return A list of all the processing units in the model.
	 */
	private static List<ProcessingUnit> getProcessorsFromModel(Amalthea model) {
		return HardwareUtil.getModulesFromHwModel(ProcessingUnit.class, model);
	}

	/**
	 * Gets a {@link LinkeHashMap} of processing unit names to Tasks assigned to it
	 * from a model.
	 * <p>
	 * <b>Note:</b> It only returns periodic tasks for now.
	 * </p>
	 *
	 * @param model The Amalthea model to get the task mapping from.
	 * @return A LinkedHashMap of processing unit names to periodic tasks.
	 */
	public static LinkedHashMap<String, List<Task>> getAlmatheaProcessorToTaskMap(Amalthea model)
			throws GlobalSchedulingException {
		List<ProcessingUnit> processorList = getProcessorsFromModel(model);
		LinkedHashMap<String, List<Task>> processorToTaskMap = new LinkedHashMap<>();

		for (ProcessingUnit core : processorList) {
			List<Task> taskProcessList = DeploymentUtil.getTasksMappedToCore(core, model).stream()
					.filter(t -> t.getStimuli().get(0) instanceof PeriodicStimulus).collect(Collectors.toList());

			// Only add entry is list is not empty
			if (!taskProcessList.isEmpty()) {
				// Throw an exception if the model uses a global scheduler
				if (taskProcessList.stream()
						.anyMatch(t -> DeploymentUtil.getAssignedCoreForProcess(t, model).size() > 1))
					throw new GlobalSchedulingException();

				processorToTaskMap.put(core.getName(), taskProcessList);
			}
		}
		return processorToTaskMap;
	}

	/**
	 * This method works just like {@link #getAlmatheaProcessorToTaskMap} except
	 * that it returns a list of task names instead.
	 * 
	 * @param map A LinkedHashMap that contains a mapping of processors to periodic
	 *            tasks assigned to them.
	 * @return A LinkedHashMap of processors names to the names of tasks mapped to
	 *         them in the model.
	 */
	public static LinkedHashMap<String, List<String>> getProcessNameToTaskNameMap(
			LinkedHashMap<String, List<Task>> map) {
		LinkedHashMap<String, List<String>> processorNameToTaskNameList = new LinkedHashMap<>();
		map.forEach((key, value) -> {
			List<String> taskNameList = value.stream().map(t -> t.getName()).collect(Collectors.toList());
			processorNameToTaskNameList.put(key, taskNameList);
		});
		return processorNameToTaskNameList;
	}

	/**
	 * Converts a list of {@link Task} to a list of {@link SimTaskParams}
	 * 
	 * @param taskList
	 * @param timeUnit
	 * @param timeType
	 * @return
	 */
	public static List<SimTaskParams> amaltheaTaskToSimTaskConverter(List<Task> taskList, TimeUnit timeUnit,
			TimeType timeType) {

		List<SimTaskParams> simTaskList = new ArrayList<>();

		for (Task task : taskList) {
			SimTaskParams sTask = new SimTaskParams();
			PeriodicStimulus stimulus = (PeriodicStimulus) task.getStimuli().get(0);
			int offset = TimeUtil.convertToTimeUnit(stimulus.getOffset(), timeUnit).getValue().intValue();
			int period = TimeUtil.convertToTimeUnit(stimulus.getRecurrence(), timeUnit).getValue().intValue();
			int executionTime = TimeUtil
					.convertToTimeUnit(RuntimeUtil.getExecutionTimeForProcess(task, null, timeType), timeUnit)
					.getValue().intValue();
			int deadline = TimeUtil.convertToTimeUnit(ConstraintsUtil.getDeadline(task), timeUnit).getValue()
					.intValue();

			sTask.setName(task.getName());
			sTask.setOffset(offset);
			sTask.setPeriod(period);
			sTask.setExecutionTime(executionTime);
			sTask.setDeadline(deadline);

			simTaskList.add(sTask);
		}

		return simTaskList;
	}

	/**
	 * Converts a map of {@link Task} to a map of {@link SimTaskParams}
	 * 
	 * @param map
	 * @param timeUnit
	 * @param etmValue
	 * @return
	 */
	public static LinkedHashMap<String, List<SimTaskParams>> getProcessorToSimTaskMap(
			LinkedHashMap<String, List<Task>> map, TimeUnit timeUnit, String etmValue) {

		LinkedHashMap<String, List<SimTaskParams>> simTaskMap = new LinkedHashMap<>();
		TimeType timeType = TimingUtils.ETM_TO_TIME_TYPE_MAP.get(etmValue);

		map.forEach((key, value) -> {
			simTaskMap.put(key, TaskUtil.amaltheaTaskToSimTaskConverter(value, timeUnit, timeType));
		});

		return simTaskMap;
	}

}
