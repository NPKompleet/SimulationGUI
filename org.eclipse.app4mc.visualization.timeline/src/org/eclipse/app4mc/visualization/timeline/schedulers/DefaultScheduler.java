package org.eclipse.app4mc.visualization.timeline.schedulers;

import java.util.Comparator;

import org.eclipse.app4mc.visualization.timeline.simulation.Scheduler;
import org.eclipse.app4mc.visualization.timeline.simulation.SimJob;

public class DefaultScheduler extends Scheduler {

	@Override
	public Comparator<SimJob> getComparator() {
		return (a, b) -> a.getActivationTime() - b.getActivationTime();
	}

}
