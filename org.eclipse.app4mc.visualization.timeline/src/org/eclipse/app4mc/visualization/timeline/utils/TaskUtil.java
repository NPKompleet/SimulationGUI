package org.eclipse.app4mc.visualization.timeline.utils;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.util.HardwareUtil;

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
	public static LinkedHashMap<String, List<Task>> getAlmatheaProcessorToTaskMap(Amalthea model) {
		List<ProcessingUnit> processorList = getProcessorsFromModel(model);
		LinkedHashMap<String, List<Task>> processorToTaskMap = new LinkedHashMap<>();

		for (ProcessingUnit core : processorList) {
			Set<Process> set = DeploymentUtil.getProcessesMappedToCore(core, model);
			List<Task> taskProcessList = set.stream().filter(p -> p instanceof Task).map(t -> (Task) t)
					.filter(t -> t.getStimuli().get(0) instanceof PeriodicStimulus).collect(Collectors.toList());
			// Only add entry is list is not empty
			if (!taskProcessList.isEmpty())
				processorToTaskMap.put(core.getName(), taskProcessList);
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

	public static BigInteger calcLCM(List<BigInteger> periodList) {

		BigInteger lcm = periodList.get(0);
		for (boolean flag = true; flag;) {
			for (BigInteger x : periodList) {
				if (lcm.mod(x) != BigInteger.ZERO) {
					flag = true;
					break;
				}
				flag = false;
			}
			lcm = flag ? lcm.add(BigInteger.ONE) : lcm;
		}

		return lcm;
	}

	public static int calcLCM(int... periodList) {

		int lcm = periodList[0];
		for (boolean flag = true; flag;) {
			for (int x : periodList) {
				if (lcm % x != 0) {
					flag = true;
					break;
				}
				flag = false;
			}
			lcm = flag ? lcm += 1 : lcm;
		}

		return lcm;
	}
}
