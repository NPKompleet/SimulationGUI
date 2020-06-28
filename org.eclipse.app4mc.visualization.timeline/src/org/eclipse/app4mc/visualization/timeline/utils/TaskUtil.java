package org.eclipse.app4mc.visualization.timeline.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.util.HardwareUtil;

public class TaskUtil {

	public static LinkedHashMap<String, BigInteger> generateTaskData(SWModel model) {
		Map<String, BigInteger> taskListMap = new LinkedHashMap<>();

		List<Task> taskList = model.getTasks();
		for (Task task : taskList) {
			Stimulus stimuli = task.getStimuli().get(0);

			if (stimuli instanceof PeriodicStimulus) {
				Time period = ((PeriodicStimulus) stimuli).getRecurrence();
//				period = alignPeriodToMS(period);
				System.out.println("Period " + period);
				taskListMap.put(task.getName(), period.getValue());
			}
		}
		return (LinkedHashMap<String, BigInteger>) taskListMap;
	}

	public List<ProcessingUnit> getProcessorsFromModel(Amalthea model) {
		return HardwareUtil.getAllProcessingUnitsForProcessingUnitDefinition(model, null);
	}

	public Map<String, Set<Process>> getAlmatheaProcessorToTaskMap(List<ProcessingUnit> processorList, Amalthea model) {
		Map<String, Set<Process>> processorToTaskMap = new HashMap<>();
		for (ProcessingUnit core : processorList) {
			Set<Process> set = DeploymentUtil.getProcessesMappedToCore(core, model);
			processorToTaskMap.put(core.getUniqueName(), set);
		}
		return processorToTaskMap;
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
