package org.eclipse.app4mc.visualization.timeline.utils;

@SuppressWarnings("serial")
public class UnalignedPeriodException extends Exception {

	public UnalignedPeriodException() {
		super("The unit of the periods are not the same.");
	}

}
