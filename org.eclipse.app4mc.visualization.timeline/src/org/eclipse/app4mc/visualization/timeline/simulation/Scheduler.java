/*******************************************************************************
 * Copyright (c) 2020 Philip Okonkwo and others.
 * 
 * This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Philip Okonkwo - initial API and implementation
 *******************************************************************************/
package org.eclipse.app4mc.visualization.timeline.simulation;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import org.eclipse.app4mc.visualization.timeline.schedulers.DefaultScheduler;
import org.eclipse.app4mc.visualization.timeline.schedulers.EDFScheduler;
import org.eclipse.app4mc.visualization.timeline.schedulers.RMScheduler;

/**
 * <p>
 * This class is responsible for creating schedulers and the scheduling of tasks
 * in the simulation.
 * </p>
 * <p>
 * Any custom scheduler created must extend this class and override the
 * {@link #getComparator()} method.
 * </p>
 * 
 * @author Philip Okonkwo
 *
 */
public abstract class Scheduler {

	public enum SchedulerStrategy {

		EDF("EDF"), RM("RM"), DEFAULT("DEFAULT");

		private final String text;

		SchedulerStrategy(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}

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

	/**
	 * Gets the appropriate scheduler using the selected strategy.
	 * 
	 * @param strategy The scheduling strategy to be used in simulation.
	 * @return A type of Scheduler.
	 * @see {@link EDFScheduler}, {@link RMScheduler}.
	 */
	public static Scheduler getScheduler(String strategy) {
		SchedulerStrategy schedulerStrategy = SchedulerStrategy.valueOf(strategy);
		Scheduler scheduler = null;

		switch (schedulerStrategy) {
		case EDF:
			scheduler = new EDFScheduler();
			break;

		case RM:
			scheduler = new RMScheduler();
			break;

		case DEFAULT:
			scheduler = new DefaultScheduler();
			break;
		}
		return scheduler;
	}
}
