package org.eclipse.app4mc.visualization.timeline.unittest;

import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;
import org.eclipse.app4mc.visualization.timeline.ui.Controller;
import org.eclipse.app4mc.visualization.timeline.ui.ISimView;
import org.eclipse.app4mc.visualization.timeline.ui.SimViewParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {
	Amalthea model;
	@Mock
	ISimView viewer;
	@Spy
	SimViewParameters simParams;

	Controller controller;

	@Before
	public void setUp() {
		String filePath = "democar.amxmi";
		model = AmaltheaLoader.loadFromFileNamed(filePath);
		controller = new Controller(model, viewer);
	}

	@Test
	public void viewerMethodsShouldBeCalled() {
		controller.populateView();

		verify(viewer, atMost(1)).setSimTimeValue(Mockito.anyString());
		verify(viewer, atMost(1)).setSimTimeUnitIndex(Mockito.anyInt());
		verify(viewer, atMost(1)).setPeriodicTasks(Mockito.anyList());

	}

	@Test
	public void viewerFilteringShouldBeCalled() {
		controller.startSimulation(simParams);

		verify(viewer, atMost(1)).enableFiltering();
	}
}
