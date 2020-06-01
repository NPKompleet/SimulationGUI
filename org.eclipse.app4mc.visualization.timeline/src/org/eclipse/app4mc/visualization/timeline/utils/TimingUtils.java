package org.eclipse.app4mc.visualization.timeline.utils;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;

public class TimingUtils {

	/*
	 * Gets the minimum {@link org.eclipse.app4mc.amalthea.model.TimeUnit} in a list of task periods.
	 * @param periodList a list of the periods 
	 * @return the minimum TimeUnit
	 * 
	 */
	public static TimeUnit getMinimumTimeUnit(List<Time> periodList) {
		return periodList.stream()
				.map(x -> x.getUnit())
				.min(Comparator.comparing(TimeUnit::getValue))
				.orElseThrow(NoSuchElementException::new);
	}
	
	/*
	 * Converts a list of task periods to the desired {@link org.eclipse.app4mc.amalthea.model.TimeUnit}.
	 * @param periodList a list of the periods
	 * @param baseUnit the {@link org.eclipse.app4mc.amalthea.model.TimeUnit} to convert each period to.
	 * @return the list of periods with values converted to the baseUnit
	 */
	public static List<Time> getAlignedPeriods(List<Time> periodList, TimeUnit baseUnit) {
		return periodList.stream()
				.map(x -> { return TimeUtil.convertToTimeUnit(x, baseUnit);})
				.collect(Collectors.toList());
	}
	
	/*
	 * Calculates the hyperperiod (LCM) of a list of task periods
	 * @param periodList a list of the periods
	 * @return the hyperperiod
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
	
	/*
	 * A maps that relates an Amalthea {@link org.eclipse.app4mc.amalthea.model.TimeUnit} to its equivalent
	 * standard Java {@link java.util.concurrent.TimeUnit}
	 */
	public static HashMap<TimeUnit, java.util.concurrent.TimeUnit> amaltheaToJavaTimeMap(){
		HashMap<TimeUnit, java.util.concurrent.TimeUnit> map = new HashMap<>();
		map.put(TimeUnit.NS, java.util.concurrent.TimeUnit.NANOSECONDS);
		map.put(TimeUnit.US, java.util.concurrent.TimeUnit.MICROSECONDS);
		map.put(TimeUnit.MS, java.util.concurrent.TimeUnit.MILLISECONDS);
		map.put(TimeUnit.S, java.util.concurrent.TimeUnit.SECONDS);
		return map;
	}
}
