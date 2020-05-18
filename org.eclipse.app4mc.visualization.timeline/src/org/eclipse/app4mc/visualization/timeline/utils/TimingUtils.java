package org.eclipse.app4mc.visualization.timeline.utils;

import java.math.BigInteger;

import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;

public class TimingUtils {

	// Convert period to picoseconds
	public static Time convertPeriodToPS(Time period) {
		switch (period.getUnit().getName()) {
		case "s":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000000000000l)));
			period.setUnit(TimeUnit.PS);
			break;
		case "ms":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000000000)));
			period.setUnit(TimeUnit.PS);
			break;
		case "us":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000000)));
			period.setUnit(TimeUnit.PS);
			break;
		case "ns":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000)));
			period.setUnit(TimeUnit.PS);
			break;
		default:
			break;
		}
		return period;
	}

	// Convert period to nanoseconds
	public static Time convertPeriodToNS(Time period) {
		switch (period.getUnit().getName()) {
		case "s":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000000000)));
			period.setUnit(TimeUnit.NS);
			break;
		case "ms":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000000)));
			period.setUnit(TimeUnit.NS);
			break;
		case "us":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000)));
			period.setUnit(TimeUnit.NS);
			break;
		case "ps":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000)));
			period.setUnit(TimeUnit.NS);
			break;
		default:
			break;
		}
		return period;
	}

	// Convert period to microseconds
	public static Time convertPeriodToUS(Time period) {
		switch (period.getUnit().getName()) {
		case "s":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000000)));
			period.setUnit(TimeUnit.US);
			break;
		case "ms":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000)));
			period.setUnit(TimeUnit.US);
			break;
		case "ns":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000)));
			period.setUnit(TimeUnit.US);
			break;
		case "ps":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000000)));
			period.setUnit(TimeUnit.US);
			break;
		default:
			break;
		}
		return period;
	}

	// Convert period to milliseconds
	public static Time convertPeriodToMS(Time period) {
		switch (period.getUnit().getName()) {
		case "s":
			period.setValue(period.getValue().multiply(BigInteger.valueOf(1000)));
			period.setUnit(TimeUnit.MS);
			break;
		case "us":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000)));
			period.setUnit(TimeUnit.MS);
			break;
		case "ns":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000000)));
			period.setUnit(TimeUnit.MS);
			break;
		case "ps":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000000000)));
			period.setUnit(TimeUnit.MS);
			break;
		default:
			break;
		}
		return period;
	}

	// Convert period to seconds
	public static Time convertPeriodToS(Time period) {
		switch (period.getUnit().getName()) {
		case "ms":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000)));
			period.setUnit(TimeUnit.S);
			break;
		case "us":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000000)));
			period.setUnit(TimeUnit.S);
			break;
		case "ns":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000000000)));
			period.setUnit(TimeUnit.S);
			break;
		case "ps":
			period.setValue(period.getValue().divide(BigInteger.valueOf(1000000000000l)));
			period.setUnit(TimeUnit.S);
			break;
		default:
			break;
		}
		return period;
	}

}
