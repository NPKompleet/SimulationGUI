/*******************************************************************************
 * Copyright (c) 2020 Phenomenon and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Phenomenon - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation;

import org.eclipse.nebula.widgets.timeline.Timing;
import org.eclipse.nebula.widgets.timeline.jface.ITimelineStyleProvider;

/**
 * @author Phenomenon
 *
 */
public interface IAnnotationFigure {

	public void updateStyle(ITimelineStyleProvider styleProvider);

	public double getTimeStamp();

	public double getAnnotatorWidth();

	public Timing getTiming();
}
