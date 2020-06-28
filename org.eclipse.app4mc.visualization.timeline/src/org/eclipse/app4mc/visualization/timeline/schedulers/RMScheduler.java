package org.eclipse.app4mc.visualization.timeline.schedulers;

import java.util.Comparator;

import org.eclipse.app4mc.visualization.timeline.simulation.SimJob;
import org.eclipse.app4mc.visualization.timeline.simulation.Scheduler;

public class RMScheduler extends Scheduler {

	@Override
	public Comparator<SimJob> getComparator() {
		return (a, b) -> a.getParentTask().getPeriod() - b.getParentTask().getPeriod();
	}

}
