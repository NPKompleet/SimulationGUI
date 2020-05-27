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

package org.eclipse.app4mc.visualization.timeline.annotationfigure;

import java.util.concurrent.TimeUnit;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.AnnotationFigure;
import org.eclipse.nebula.widgets.timeline.jface.ITimelineStyleProvider;


public class DownArrowAntFigure extends AnnotationFigure{

	private static final int ANNOTATOR_WIDTH = 13;
	private static final int TRIANGLE_SIZE = 6;
	private final RectangleFigure fLineFigure = new RectangleFigure();


	public DownArrowAntFigure(long timeStamp, ITimelineStyleProvider styleProvider) {
		super(timeStamp, styleProvider);
		makeFigure();
	}
	
	public DownArrowAntFigure(long timeStamp, TimeUnit timeUnit, ITimelineStyleProvider styleProvider) {
		super(timeStamp, timeUnit, styleProvider);
		makeFigure();
	}

	private void makeFigure() {
		setLayoutManager(new AnnotationFigureLayout());
	
		final Triangle topTriangle = new Triangle();
		topTriangle.setSize(TRIANGLE_SIZE, TRIANGLE_SIZE);
		topTriangle.setOpaque(true);
		topTriangle.setDirection(PositionConstants.RIGHT);
		add(topTriangle, BorderLayout.BOTTOM);

		fLineFigure.setLineWidth(1);
		add(fLineFigure, BorderLayout.CENTER);
	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(ANNOTATOR_WIDTH, hHint);
	}
	
	
	@Override
	public double getAnnotatorWidth() {
		return ANNOTATOR_WIDTH;
	}

	private class AnnotationFigureLayout extends BorderLayout {

		@Override
		public void layout(IFigure container) {
			super.layout(container);

			final Rectangle bounds = fLineFigure.getBounds();
			bounds.performTranslate((bounds.width() / 2), 0);
			bounds.setWidth(1);
			bounds.setY(0);
			bounds.setHeight(container.getBounds().height() + 200);
		}
	}

}
