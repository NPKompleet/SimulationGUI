package org.eclipse.app4mc.visualization.timeline.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.nebula.widgets.timeline.jface.DefaultTimelineStyleProvider;
import org.eclipse.swt.graphics.Color;

public class StyleProvider extends DefaultTimelineStyleProvider {

	public StyleProvider(ResourceManager resourceManager) {
		super(resourceManager);
	}

	@Override
	public Color getBackgroundColor() {
		return ColorConstants.black;
	}

	@Override
	public boolean showOverview() {
		// TODO Auto-generated method stub
		return false;
	}

}
