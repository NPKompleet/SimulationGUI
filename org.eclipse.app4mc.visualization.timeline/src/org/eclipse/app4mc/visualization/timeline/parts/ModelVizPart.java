package org.eclipse.app4mc.visualization.timeline.parts;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.visualization.timeline.utils.Constants;
import org.eclipse.app4mc.visualization.timeline.utils.SWTResourceManager;
import org.eclipse.app4mc.visualization.ui.registry.Visualization;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.nebula.widgets.timeline.ILane;
import org.eclipse.nebula.widgets.timeline.ITimeline;
import org.eclipse.nebula.widgets.timeline.ITrack;
import org.eclipse.nebula.widgets.timeline.TimelineDataBinding;
import org.eclipse.nebula.widgets.timeline.jface.TimelineViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.component.annotations.Component;

@Component(property = {
				"id=ModelViz",
				"name=Model Visualization",
				"description=Some other Task visualization" })
public class ModelVizPart implements Visualization {
	int trackSize;
	private Text txtSTime;
	private Text txtStepsize;
	private Text txtOverhd;

	@PostConstruct
	public void createVisualization(Amalthea model, Composite parent) throws IOException {
		createSimulationControls(model, parent);
	}

	@PreDestroy
	public void dispose() {
		System.out.println("Destroy resources");
	}

	public Control createTimelineControl(Composite parent) {
		TimelineViewer timelineViewer = new TimelineViewer(parent, SWT.NULL);
		timelineViewer.setStyleProvider(new StyleProvider(JFaceResources.getResources()));
		final ITimeline model = (ITimeline) timelineViewer.getInput();

		new TimelineDataBinding(timelineViewer, model, 300);

		createTimelineContent(model);

		trackSize = model.getTracks().size();

		return timelineViewer.getControl();
	}

	private void createTimelineContent(ITimeline model) {
		// TimeLine contents go here
	}

	public void createSimulationControls(Amalthea model, Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1));
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);

		Control control = createTimelineControl(scrolledComposite);

		Group grpParameters = new Group(scrolledComposite_1, SWT.NONE);
		grpParameters.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpParameters.setText("Parameters");
		GridLayout gl_grpParameters = new GridLayout(3, false);
		grpParameters.setLayout(gl_grpParameters);

		Label lblAlgorithm = new Label(grpParameters, SWT.NONE);
		lblAlgorithm.setToolTipText("Scheduling Strategy");
		lblAlgorithm.setText("Strategy:");

		Button btnEdf = new Button(grpParameters, SWT.RADIO);
		btnEdf.setText("EDF");
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);

		Button btnRms = new Button(grpParameters, SWT.RADIO);
		btnRms.setText("RMS");
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);

		Button btnDefault = new Button(grpParameters, SWT.RADIO);
		btnDefault.setSelection(true);
		btnDefault.setText("DEFAULT");
		new Label(grpParameters, SWT.NONE);

		Label lblSimTime = new Label(grpParameters, SWT.NONE);
		lblSimTime.setToolTipText("Simulation Time");
		lblSimTime.setBounds(0, 0, 55, 15);
		lblSimTime.setText("Sim Time:");

		txtSTime = new Text(grpParameters, SWT.BORDER);
		txtSTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtSTime.setText("0");

		Combo cmbSTime = new Combo(grpParameters, SWT.READ_ONLY);
		cmbSTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbSTime.setItems(Constants.TIME_UNIT_OPTIONS);
		cmbSTime.select(2);

		Label lblStepSize = new Label(grpParameters, SWT.NONE);
		lblStepSize.setText("Stepsize:");

		txtStepsize = new Text(grpParameters, SWT.BORDER);
		txtStepsize.setText("5");
		txtStepsize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Combo cmbStepsize = new Combo(grpParameters, SWT.READ_ONLY);
		cmbStepsize.setItems(Constants.TIME_UNIT_OPTIONS);
		cmbStepsize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbStepsize.select(2);

		Label lblOverhead = new Label(grpParameters, SWT.NONE);
		lblOverhead.setToolTipText("Scheduling Overhead");
		lblOverhead.setText("Overhead:");

		txtOverhd = new Text(grpParameters, SWT.BORDER);
		txtOverhd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtOverhd.setText("5");

		Combo cmbOverhd = new Combo(grpParameters, SWT.READ_ONLY);
		cmbOverhd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbOverhd.setItems(Constants.TIME_UNIT_OPTIONS);
		cmbOverhd.select(2);

		Label lblETM = new Label(grpParameters, SWT.NONE);
		lblETM.setToolTipText("Execution Time Model");
		lblETM.setText("ETM:");

		Combo cmbETM = new Combo(grpParameters, SWT.READ_ONLY);
		cmbETM.setItems(Constants.ETM_OPTIONS);
		cmbETM.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		cmbETM.select(2);

		Label lblPreemption = new Label(grpParameters, SWT.NONE);
		lblPreemption.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPreemption.setText("Preemption:");

		Combo cmbPreemptn = new Combo(grpParameters, SWT.READ_ONLY);
		cmbPreemptn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		cmbPreemptn.setItems(Constants.PREEMPTION_OPTIONS);
		cmbPreemptn.select(2);

		Label lblCoretasks = new Label(grpParameters, SWT.NONE);
		lblCoretasks.setText("Core/Tasks:");

		ListViewer listViewer = new ListViewer(grpParameters, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		List list = listViewer.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		listViewer.setLabelProvider(new LabelProvider());
		listViewer.setContentProvider(ArrayContentProvider.getInstance());
		listViewer.setInput(model.getSwModel().getTasks().stream().map(x -> x.getName()).collect(Collectors.toList()));
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);

		Button btnLoad = new Button(grpParameters, SWT.NONE);
		btnLoad.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnLoad.setText("Load");

		Button btnSimulate = new Button(grpParameters, SWT.NONE);
		btnSimulate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSimulate.setText("Simulate");
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);
		
		Label lblMessage = new Label(grpParameters, SWT.NONE);
		lblMessage.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		lblMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblMessage.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblMessage.setText("Message");
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);
		
		scrolledComposite.setContent(control);
		scrolledComposite.setMinSize(-1, trackSize * 70);
		System.out.println("Size is> " + trackSize);
		scrolledComposite_1.setContent(grpParameters);
		scrolledComposite_1.setMinSize(grpParameters.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

}
