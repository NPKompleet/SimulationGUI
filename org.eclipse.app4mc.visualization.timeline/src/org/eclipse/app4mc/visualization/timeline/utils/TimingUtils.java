package org.eclipse.app4mc.visualization.timeline.utils;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;

public class TimingUtils {

	public static final Map<TimeUnit, java.util.concurrent.TimeUnit> AMALTHEA_TO_JAVA_TIME_MAP = getAmaltheaToJavaTimeMap();

	public static final Map<TimeUnit, Integer> TIME_TO_CONSTANT_MAP = getTimeToConstantMap();

	public static final Map<String, TimeUnit> TIME_UNIT_STRING_TO_TIME_MAP = getTimeUnitStringToTimeMap();

	private static Comparator<TimeUnit> timeUnitComparator = new Comparator<TimeUnit>() {
		@Override
		public int compare(TimeUnit a, TimeUnit b) {
			return TIME_TO_CONSTANT_MAP.get(a) - TIME_TO_CONSTANT_MAP.get(b);
		}
	};

	/**
	 * Gets the minimum {@link org.eclipse.app4mc.amalthea.model.TimeUnit} in a list
	 * of task periods.
	 * 
	 * @param periodList A list of the periods
	 * @return The minimum TimeUnit
	 * 
	 */
	public static TimeUnit getMinimumTimeUnit(List<Time> periodList) {
		return periodList.stream().map(x -> x.getUnit()).min(timeUnitComparator)
				.orElseThrow(NoSuchElementException::new);
	}

	/**
	 * Gets the maximum {@link org.eclipse.app4mc.amalthea.model.TimeUnit} in a list
	 * of task periods.
	 * 
	 * @param periodList A list of the periods
	 * @return The maximum TimeUnit
	 * 
	 */
	public static TimeUnit getMaximumTimeUnit(List<Time> periodList) {
		return periodList.stream().map(x -> x.getUnit()).max(timeUnitComparator)
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
	 * @param periodList A list of the periods.
	 * @return The hyperperiod.
	 * @throws UnalignedPeriodException
	 * @throws {@link                   UnalignedPeriodException} if the periods in
	 *                                  the list have different time units.
	 */
	public static BigInteger computeHyperPeriod(List<Time> periodList) throws UnalignedPeriodException {
		TimeUnit unit = periodList.get(0).getUnit();
		if (periodList.stream().anyMatch(x -> x.getUnit() != unit)) {
			throw new UnalignedPeriodException();
		}
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

	/**
	 * Converts time from one time unit to another.
	 * 
	 * @param value   The initial time value.
	 * @param oldUnit The initial unit of time to covert from.
	 * @param newUnit The desired unit of time to be converted to.
	 * @return the desired time value
	 */
	public static BigInteger convertTimeUnit(int value, TimeUnit oldUnit, TimeUnit newUnit) {
		Time time = AmaltheaFactory.eINSTANCE.createTime();
		time.setValue(BigInteger.valueOf(value));
		time.setUnit(oldUnit);
		Time newTime = TimeUtil.convertToTimeUnit(time, newUnit);
		return newTime.getValue();
	}

	/**
	 * A map that relates an Amalthea
	 * {@link org.eclipse.app4mc.amalthea.model.TimeUnit} to its equivalent standard
	 * Java {@link java.util.concurrent.TimeUnit}
	 * 
	 * @return The map of Amalthea to Java time units
	 */
	private static HashMap<TimeUnit, java.util.concurrent.TimeUnit> getAmaltheaToJavaTimeMap() {
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
	private static HashMap<TimeUnit, Integer> getTimeToConstantMap() {
		HashMap<TimeUnit, Integer> map = new HashMap<>();
		map.put(TimeUnit.NS, 0);
		map.put(TimeUnit.US, 1);
		map.put(TimeUnit.MS, 2);
		map.put(TimeUnit.S, 3);
		return map;
	}

	/**
	 * Create a map of String literals of time units to the equivalent
	 * {@link TimeUnit}. Relies on {@link Constants#TIME_UNIT_OPTIONS}.
	 * 
	 * @return The map
	 * @see Constants#TIME_UNIT_OPTIONS
	 */
	private static HashMap<String, TimeUnit> getTimeUnitStringToTimeMap() {
		HashMap<String, TimeUnit> map = new HashMap<>();
		map.put(Constants.TIME_UNIT_OPTIONS[0], TimeUnit.NS);
		map.put(Constants.TIME_UNIT_OPTIONS[1], TimeUnit.US);
		map.put(Constants.TIME_UNIT_OPTIONS[2], TimeUnit.MS);
		map.put(Constants.TIME_UNIT_OPTIONS[3], TimeUnit.S);
		return map;
	}

}
