/*******************************************************************************
 * Copyright (c) 2020 Philip Okonkwo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Phenomenon - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation;

import java.util.concurrent.TimeUnit;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.nebula.widgets.timeline.Timing;
import org.eclipse.nebula.widgets.timeline.jface.ITimelineStyleProvider;

/**
 * @author Philip Okonkwo
 *
 */
public class AnnotationFigure extends Figure implements IAnnotationFigure {

	private static final int ANNOTATOR_WIDTH = 13;

	Timing timing;

	public AnnotationFigure(long timeStamp, ITimelineStyleProvider styleProvider) {
		this.timing = new Timing(timeStamp);
		updateStyle(styleProvider);

	}

	public AnnotationFigure(long timeStamp, TimeUnit timeUnit, ITimelineStyleProvider styleProvider) {
		this(timeUnit.toNanos(timeStamp), styleProvider);
	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(ANNOTATOR_WIDTH, hHint);
	}

	@Override
	public void updateStyle(ITimelineStyleProvider styleProvider) {
		setForegroundColor(styleProvider.getCursorColor());
		setBackgroundColor(styleProvider.getCursorColor());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.IAnnotationFigure#getTimeStamp()
	 */
	@Override
	public double getTimeStamp() {
		return timing.getTimestamp();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.IAnnotationFigure#getAnnotatorWidth()
	 */
	@Override
	public double getAnnotatorWidth() {
		return ANNOTATOR_WIDTH;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.IAnnotationFigure#getTiming()
	 */
	@Override
	public Timing getTiming() {
		return timing;
	}

}
