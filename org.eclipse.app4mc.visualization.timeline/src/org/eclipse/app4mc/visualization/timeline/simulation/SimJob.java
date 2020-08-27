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

import org.eclipse.app4mc.visualization.timeline.utils.Constants;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

public class SimJob extends SimProcess {
	private SimTask parentTask;
	private int activationTime;
	private int executionTime;
	private int absoluteDeadline;
	private SimModel model;
	private String name;

	public SimJob(SimTask parentTask, String name, int activationTime, int executionTime, int deadline, Model model) {
		super(model, name, true);
		this.parentTask = parentTask;
		this.activationTime = activationTime;
		this.name = name;
		this.executionTime = executionTime;
		this.absoluteDeadline = deadline;
		this.model = (SimModel) model;
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		model.jobQueue.addLast(this);

//		if (model.jobQueue.size() > 1) {
//			model.setNextSchedule(true);
//		}

		if (!model.processorQueue.isEmpty()) {
			Processor assignedProcessor = model.processorQueue.first();
			model.processorQueue.remove(assignedProcessor);

			assignedProcessor.activate();
		} else {
			if (model.preemptiveness.equals(Constants.PREEMPTION_OPTIONS[0]) && model.processor.checkIsPriority(this)) {
				model.processor.interrupt(model.priorityJobCode);
			}
		}

		passivate();
	}

	public int getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}

	public String getName() {
		return name;
	}

	public int getAbsoluteDeadline() {
		return absoluteDeadline;
	}

	public SimTask getParentTask() {
		return parentTask;
	}

	public int getActivationTime() {
		return activationTime;
	}

}
