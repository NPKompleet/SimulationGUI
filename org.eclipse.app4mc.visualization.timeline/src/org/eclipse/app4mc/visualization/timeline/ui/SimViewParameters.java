package org.eclipse.app4mc.visualization.timeline.ui;

import static org.eclipse.app4mc.visualization.timeline.utils.Constants.DEFAULT_ETM_INDEX;
import static org.eclipse.app4mc.visualization.timeline.utils.Constants.DEFAULT_OVERHEAD;
import static org.eclipse.app4mc.visualization.timeline.utils.Constants.DEFAULT_PREEMPTN_INDEX;
import static org.eclipse.app4mc.visualization.timeline.utils.Constants.DEFAULT_SIM_TIME;
import static org.eclipse.app4mc.visualization.timeline.utils.Constants.DEFAULT_STEP_SIZE;
import static org.eclipse.app4mc.visualization.timeline.utils.Constants.DEFAULT_STRATEGY;
import static org.eclipse.app4mc.visualization.timeline.utils.Constants.DEFAULT_UNIT_INDEX;
import static org.eclipse.app4mc.visualization.timeline.utils.Constants.ETM_OPTIONS;
import static org.eclipse.app4mc.visualization.timeline.utils.Constants.PREEMPTION_OPTIONS;
import static org.eclipse.app4mc.visualization.timeline.utils.Constants.TIME_UNIT_OPTIONS;

public class SimViewParameters {
	private String strategy = DEFAULT_STRATEGY;

	private String simTime = DEFAULT_SIM_TIME;

	private String simTimeUnit = TIME_UNIT_OPTIONS[DEFAULT_UNIT_INDEX];

	private String stepSize = DEFAULT_STEP_SIZE;

	private String stepSizeUnit = TIME_UNIT_OPTIONS[DEFAULT_UNIT_INDEX];

	private String overhead = DEFAULT_OVERHEAD;

	private String overheadUnit = TIME_UNIT_OPTIONS[DEFAULT_UNIT_INDEX];

	private String etm = ETM_OPTIONS[DEFAULT_ETM_INDEX];

	private String preemption = PREEMPTION_OPTIONS[DEFAULT_PREEMPTN_INDEX];

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getSimTime() {
		return simTime;
	}

	public void setSimTime(String simTime) {
		this.simTime = simTime;
	}

	public String getSimTimeUnit() {
		return simTimeUnit;
	}

	public void setSimTimeUnit(String simTimeUnit) {
		this.simTimeUnit = simTimeUnit;
	}

	public String getStepSize() {
		return stepSize;
	}

	public void setStepSize(String stepSize) {
		this.stepSize = stepSize;
	}

	public String getStepSizeUnit() {
		return stepSizeUnit;
	}

	public void setStepSizeUnit(String stepSizeUnit) {
		this.stepSizeUnit = stepSizeUnit;
	}

	public String getOverhead() {
		return overhead;
	}

	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	public String getOverheadUnit() {
		return overheadUnit;
	}

	public void setOverheadUnit(String overheadUnits) {
		this.overheadUnit = overheadUnits;
	}

	public String getEtm() {
		return etm;
	}

	public void setEtm(String etm) {
		this.etm = etm;
	}

	public String getPreemption() {
		return preemption;
	}

	public void setPreemption(String preemption) {
		this.preemption = preemption;
	}

}
