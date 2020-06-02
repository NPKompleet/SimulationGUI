package org.eclipse.app4mc.visualization.timeline.utils;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;

public class TimingUtils {

	/**
	 * Gets the minimum {@link org.eclipse.app4mc.amalthea.model.TimeUnit} in a list
	 * of task periods.
	 * 
	 * @param periodList A list of the periods
	 * @return The minimum TimeUnit
	 * 
	 */
	public static TimeUnit getMinimumTimeUnit(List<Time> periodList) {
		return periodList.stream().map(x -> x.getUnit()).min(Comparator.comparing(TimeUnit::getValue))
				.orElseThrow(NoSuchElementException::new);
	}

	/**
	 * Converts a list of task periods to the desired
	 * {@link org.eclipse.app4mc.amalthea.model.TimeUnit}.
	 * 
	 * @param periodList A list of the periods
	 * @param baseUnit   The {@link org.eclipse.app4mc.amalthea.model.TimeUnit} to
	 *                   convert each period to.
	 * @return The list of periods with values converted to the baseUnit
	 */
	public static List<Time> getAlignedPeriods(List<Time> periodList, TimeUnit baseUnit) {
		return periodList.stream().map(x -> {
			return TimeUtil.convertToTimeUnit(x, baseUnit);
		}).collect(Collectors.toList());
	}

	/**
	 * Calculates the hyperperiod (LCM) of a list of task periods
	 * 
	 * @param periodList A list of the periods
	 * @return The hyperperiod
	 */
	public static BigInteger computeHyperPeriod(List<Time> periodList) {
		BigInteger lcm = periodList.get(0).getValue();
		for (boolean flag = true; flag;) {
			for (Time x : periodList) {
				if (lcm.mod(x.getValue()) != BigInteger.ZERO) {
					flag = true;
					break;
				}
				flag = false;
			}
			lcm = flag ? lcm.add(BigInteger.ONE) : lcm;
		}
		return lcm;
	}

	/**
	 * A map that relates an Amalthea
	 * {@link org.eclipse.app4mc.amalthea.model.TimeUnit} to its equivalent standard
	 * Java {@link java.util.concurrent.TimeUnit}
	 * 
	 * @return The map of Amalthea to Java time units
	 */
	public static HashMap<TimeUnit, java.util.concurrent.TimeUnit> amaltheaToJavaTimeMap() {
		HashMap<TimeUnit, java.util.concurrent.TimeUnit> map = new HashMap<>();
		map.put(TimeUnit.NS, java.util.concurrent.TimeUnit.NANOSECONDS);
		map.put(TimeUnit.US, java.util.concurrent.TimeUnit.MICROSECONDS);
		map.put(TimeUnit.MS, java.util.concurrent.TimeUnit.MILLISECONDS);
		map.put(TimeUnit.S, java.util.concurrent.TimeUnit.SECONDS);
		return map;
	}

	/**
	 * A map that relates a {@link TimeUnit} to assigned integer constants. These
	 * constants match the index of the corresponding string in
	 * {@value Constants#TIME_UNIT_OPTIONS}
	 * 
	 * @return The map of Java time units to constants
	 * @see Constants#TIME_UNIT_OPTIONS
	 */
	public static HashMap<TimeUnit, Integer> timeToConstantMap() {
		HashMap<TimeUnit, Integer> map = new HashMap<>();
		map.put(TimeUnit.NS, 3);
		map.put(TimeUnit.US, 2);
		map.put(TimeUnit.MS, 1);
		map.put(TimeUnit.S, 0);
		return map;
	}

	/**
	 * Gets a mapping of periodic tasks to their period
	 * 
	 * @param model An Amalthea {@link org.eclipse.app4mc.amalthea.model.SWModel} to
	 *              be read from
	 * @return A {@link LinkedHashMap} of each periodic task and its period
	 */
	public static LinkedHashMap<String, Time> getPeriodMap(SWModel model) {
		LinkedHashMap<String, Time> taskListMap = new LinkedHashMap<>();

		List<Task> taskList = model.getTasks();
		for (Task task : taskList) {
			Stimulus stimuli = task.getStimuli().get(0);

			if (stimuli instanceof PeriodicStimulus) {
				Time period = ((PeriodicStimulus) stimuli).getRecurrence();
				taskListMap.put(task.getName(), period);
			}
		}
		return taskListMap;
	}
}
