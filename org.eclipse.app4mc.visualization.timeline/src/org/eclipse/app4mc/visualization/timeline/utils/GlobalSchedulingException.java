package org.eclipse.app4mc.visualization.timeline.utils;

@SuppressWarnings("serial")
public class GlobalSchedulingException extends Exception {
	private static String message = "The tasks running on the processors are not partitioned. "
			+ "Global scheduling not suppported yet.";

	public GlobalSchedulingException() {
		super(message);
	}

	@Override
	public String toString() {
		return "Global Scheduling Unsupported";
	}

}
