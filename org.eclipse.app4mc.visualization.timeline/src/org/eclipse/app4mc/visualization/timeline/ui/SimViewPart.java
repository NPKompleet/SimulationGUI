package org.eclipse.app4mc.visualization.timeline.ui;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.visualization.timeline.annotationfigure.DownArrowAntFigure;
import org.eclipse.app4mc.visualization.timeline.annotationfigure.UpArrowAntFigure;
import org.eclipse.app4mc.visualization.timeline.utils.Constants;
import org.eclipse.app4mc.visualization.timeline.utils.SWTResourceManager;
import org.eclipse.app4mc.visualization.ui.registry.Visualization;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.nebula.widgets.timeline.ITimelineEvent;
import org.eclipse.nebula.widgets.timeline.ITimelineFactory;
import org.eclipse.nebula.widgets.timeline.TimelineComposite;
import org.eclipse.nebula.widgets.timeline.figures.detail.track.TrackFigure;
import org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.LaneFigure;
import org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.IAnnotationFigure;
import org.eclipse.nebula.widgets.timeline.jface.TimelineViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.component.annotations.Component;

@Component(property = { "id=SimView", "name=Model Visualization", "description=Task visualization" })
public class SimViewPart implements Visualization, ISimView {
	int trackSize;
	private Text txtSTime;
	private Text txtStepsize;
	private Text txtOverhd;
	Combo cmbSTime;
	ListViewer listViewer;
	Controller controller;

	private static Listener textListener = new Listener() {
		public void handleEvent(Event e) {
			String text = e.text;
			e.doit = text.chars().allMatch(Character::isDigit);
		}
	};

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

		final TimelineComposite control = timelineViewer.getControl();
		final ITimelineEvent event = ITimelineFactory.eINSTANCE.createTimelineEvent();
		event.setStartTimestamp(100, TimeUnit.MILLISECONDS);
		event.setDuration(400, TimeUnit.MILLISECONDS);
		event.setMessage("The best event ever");

		final ITimelineEvent event2 = ITimelineFactory.eINSTANCE.createTimelineEvent();
		event2.setStartTimestamp(200, TimeUnit.MILLISECONDS);
		event2.setDuration(150, TimeUnit.MILLISECONDS);
		event2.setMessage("Evet 2");

		IAnnotationFigure antFigure = new UpArrowAntFigure(150, TimeUnit.MILLISECONDS,
				timelineViewer.getStyleProvider());
		IAnnotationFigure antFigure2 = new DownArrowAntFigure(550, TimeUnit.MILLISECONDS,
				timelineViewer.getStyleProvider());
		IAnnotationFigure antFigure3 = new DownArrowAntFigure(250, TimeUnit.MILLISECONDS,
				timelineViewer.getStyleProvider());
		IAnnotationFigure antFigure4 = new UpArrowAntFigure(350, TimeUnit.MILLISECONDS,
				timelineViewer.getStyleProvider());
		IAnnotationFigure antFigure5 = new DownArrowAntFigure(350, TimeUnit.MILLISECONDS,
				timelineViewer.getStyleProvider());

		final TrackFigure track1 = control.getRootFigure().createTrackFigure("Task 1");
		final LaneFigure lane1 = control.getRootFigure().createLaneFigure(track1);
		final TrackFigure track2 = control.getRootFigure().createTrackFigure("Task 2");
		final LaneFigure lane2 = control.getRootFigure().createLaneFigure(track2);
		control.getRootFigure().createEventFigure(lane1, event);
		control.getRootFigure().createEventFigure(lane2, event2);
		control.getRootFigure().addAnnotationFigure(lane1, antFigure);
		control.getRootFigure().addAnnotationFigure(lane1, antFigure2);
		control.getRootFigure().addAnnotationFigure(lane2, antFigure3);
		control.getRootFigure().addAnnotationFigure(lane2, antFigure4);
		control.getRootFigure().addAnnotationFigure(lane2, antFigure5);
		control.getRootFigure().zoom(0.000001, 0);

		trackSize = 2;

		return timelineViewer.getControl();
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
		btnRms.setText("RM");
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
		txtSTime.setText(Constants.DEFAULT_SIM_TIME);
		txtSTime.addListener(SWT.Verify, textListener);

		cmbSTime = new Combo(grpParameters, SWT.READ_ONLY);
		cmbSTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbSTime.setItems(Constants.TIME_UNIT_OPTIONS);
		cmbSTime.select(Constants.DEFAULT_UNIT_INDEX);

		Label lblStepSize = new Label(grpParameters, SWT.NONE);
		lblStepSize.setText("Stepsize:");

		txtStepsize = new Text(grpParameters, SWT.BORDER);
		txtStepsize.setText(Constants.DEFAULT_STEP_SIZE);
		txtStepsize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtStepsize.addListener(SWT.Verify, textListener);

		Combo cmbStepsize = new Combo(grpParameters, SWT.READ_ONLY);
		cmbStepsize.setItems(Constants.TIME_UNIT_OPTIONS);
		cmbStepsize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbStepsize.select(Constants.DEFAULT_UNIT_INDEX);

		Label lblOverhead = new Label(grpParameters, SWT.NONE);
		lblOverhead.setToolTipText("Scheduling Overhead");
		lblOverhead.setText("Overhead:");

		txtOverhd = new Text(grpParameters, SWT.BORDER);
		txtOverhd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtOverhd.setText(Constants.DEFAULT_OVERHEAD);
		txtOverhd.addListener(SWT.Verify, textListener);

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
		cmbETM.select(Constants.DEFAULT_ETM_INDEX);

		Label lblPreemption = new Label(grpParameters, SWT.NONE);
		lblPreemption.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPreemption.setText("Preemption:");

		Combo cmbPreemptn = new Combo(grpParameters, SWT.READ_ONLY);
		cmbPreemptn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		cmbPreemptn.setItems(Constants.PREEMPTION_OPTIONS);
		cmbPreemptn.select(Constants.DEFAULT_PREEMPTN_INDEX);

		Label lblCoretasks = new Label(grpParameters, SWT.NONE);
		lblCoretasks.setText("Tasks:");

		listViewer = new ListViewer(grpParameters, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		List list = listViewer.getList();
		GridData gd_list = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_list.heightHint = 80;
		list.setLayoutData(gd_list);
		listViewer.setLabelProvider(new LabelProvider());
		listViewer.setContentProvider(ArrayContentProvider.getInstance());
		new Label(grpParameters, SWT.NONE);

		updateWidgetData(model);

		Button btnLoad = new Button(grpParameters, SWT.NONE);
		btnLoad.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnLoad.setText("Filter");
		btnLoad.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				new FilterDialog(parent.getShell()).open();
			}
		});

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

	private void updateWidgetData(Amalthea model) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				controller = new Controller(model, SimViewPart.this);
				controller.populateView();
			}
		});
	}

	@Override
	public void setSimTimeValue(String value) {
		txtSTime.setText(value);
	}

	@Override
	public void setSimTimeUnitIndex(int index) {
		cmbSTime.select(index);
	}

	@Override
	public void setPeriodicTasks(java.util.List<String> taskList) {
		listViewer.setInput(taskList);
	}

}
