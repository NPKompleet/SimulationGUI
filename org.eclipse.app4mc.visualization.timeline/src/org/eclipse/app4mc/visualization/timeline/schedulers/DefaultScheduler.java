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
