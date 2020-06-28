package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public abstract class Scheduler {

	/**
	 * Sorts the contents the jobQueue according to the implemented comparator
	 * 
	 * @param jobQueue A LinkedBlockingDeque that contains {@link SimJob} elements
	 * @see #getComparator()
	 */
	public final LinkedBlockingDeque<SimJob> schedule(LinkedBlockingDeque<SimJob> jobQueue) {
		List<SimJob> jobList = jobQueue.stream().sorted(getComparator().thenComparing(SimJob::getActivationTime))
				.collect(Collectors.toList());
		return new LinkedBlockingDeque<SimJob>(jobList);
	}

	/**
	 * Should be overridden by implemented Schedulers
	 * 
	 * @return A comparator that can be used to schedule jobs
	 */
	public abstract Comparator<SimJob> getComparator();
}
