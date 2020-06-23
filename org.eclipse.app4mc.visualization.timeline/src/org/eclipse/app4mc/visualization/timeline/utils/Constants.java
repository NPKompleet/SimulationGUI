package org.eclipse.app4mc.visualization.timeline.utils;

public class Constants {
	public static final String[] ETM_OPTIONS = new String[] { "BCET", "ACET", "WCET", "Randomized" };

	public static final String[] TIME_UNIT_OPTIONS = new String[] { "ns", "us", "ms", "s" };

	public static final String[] PREEMPTION_OPTIONS = new String[] { "Fully-Preemptive",
			"Preemptive On Runnable Bounds", "Non-Preemptive" };

	public static final String DEFAULT_SIM_TIME = "0";

	public static final String DEFAULT_STEP_SIZE = "1";

	public static final String DEFAULT_OVERHEAD = "0";

	public static final int DEFAULT_UNIT_INDEX = 2;

	public static final int DEFAULT_ETM_INDEX = 2;

	public static final int DEFAULT_PREEMPTN_INDEX = 2;
}
