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
package org.eclipse.app4mc.visualization.timeline.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.nebula.widgets.timeline.jface.DefaultTimelineStyleProvider;
import org.eclipse.swt.graphics.Color;

/**
 * This class is responsible for styling the visualization of the simulation.
 * 
 * @author Philip Okonkwo
 *
 */
public class StyleProvider extends DefaultTimelineStyleProvider {

	/**
	 * This overrides the {@link DefaultTimelineStyleProvider#LANE_COLORS} and adds
	 * more colors to the list. Helps to make the visualization more colorful.
	 */
	private static final Color[] LANE_COLORS = new Color[] { ColorConstants.lightBlue, ColorConstants.yellow,
			ColorConstants.red, ColorConstants.lightGreen, new Color(null, 255, 0, 255), ColorConstants.orange,
			ColorConstants.cyan, ColorConstants.darkGreen, new Color(null, 250, 128, 114), ColorConstants.lightGray,
			new Color(null, 255, 218, 185) };

	private static int fNextLaneColor = 0;

	public StyleProvider(ResourceManager resourceManager) {
		super(resourceManager);
	}

	@Override
	public Color getBackgroundColor() {
		return ColorConstants.black;
	}

	@Override
	public boolean showOverview() {
		return false;
	}

	@Override
	public Color getLaneColor() {
		final Color color = LANE_COLORS[fNextLaneColor];
		fNextLaneColor = (fNextLaneColor + 1) % LANE_COLORS.length;

		return color;
	}

	/**
	 * Sets the next color index to be used by the class to the first index.
	 */
	public void resetColorIndex() {
		fNextLaneColor = 0;
	}
}
