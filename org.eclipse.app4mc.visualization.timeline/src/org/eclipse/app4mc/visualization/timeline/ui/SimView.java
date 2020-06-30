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
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
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

@Component(property = { "id=SimView", "name=Model Visualization", "description=Model visualization" })
public class SimView implements Visualization, ISimView {
	int trackSize;
	private Button btnEdf;
	private Button btnRms;
	private Button btnDefault;
	private Text txtSimTime;
	private Text txtStepsize;
	private Text txtOverhd;
	private Combo cmbSTimeUnit;
	private Combo cmbStepsizeUnit;
	private Combo cmbOverhdUnit;
	private Combo cmbETM;
	private Combo cmbPreemptn;
	private ListViewer listViewer;
	private Controller controller;
	private SimViewParameters simViewParams;
	private Button btnFilter;

	private static Listener textListener = new Listener() {
		public void handleEvent(Event e) {
			String text = e.text;
			e.doit = text.chars().allMatch(Character::isDigit);
		}
	};

	@PostConstruct
	public void createVisualization(Amalthea model, Composite parent) throws IOException {
		controller = new Controller(model, SimView.this);
		simViewParams = new SimViewParameters();
		createSimulationControls(model, parent);
	}

	@PreDestroy
	public void dispose() {
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

		btnEdf = new Button(grpParameters, SWT.RADIO);
		btnEdf.setText("EDF");
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);

		btnRms = new Button(grpParameters, SWT.RADIO);
		btnRms.setText("RM");
		new Label(grpParameters, SWT.NONE);
		new Label(grpParameters, SWT.NONE);

		btnDefault = new Button(grpParameters, SWT.RADIO);
		btnDefault.setSelection(true);
		btnDefault.setText(Constants.DEFAULT_STRATEGY);
		new Label(grpParameters, SWT.NONE);

		Label lblSimTime = new Label(grpParameters, SWT.NONE);
		lblSimTime.setToolTipText("Simulation Time");
		lblSimTime.setBounds(0, 0, 55, 15);
		lblSimTime.setText("Sim Time:");

		txtSimTime = new Text(grpParameters, SWT.BORDER);
		txtSimTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtSimTime.setText(Constants.DEFAULT_SIM_TIME);
		txtSimTime.addListener(SWT.Verify, textListener);

		cmbSTimeUnit = new Combo(grpParameters, SWT.READ_ONLY);
		cmbSTimeUnit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbSTimeUnit.setItems(Constants.TIME_UNIT_OPTIONS);
		cmbSTimeUnit.select(Constants.DEFAULT_UNIT_INDEX);

		Label lblStepSize = new Label(grpParameters, SWT.NONE);
		lblStepSize.setText("Stepsize:");

		txtStepsize = new Text(grpParameters, SWT.BORDER);
		txtStepsize.setText(Constants.DEFAULT_STEP_SIZE);
		txtStepsize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtStepsize.addListener(SWT.Verify, textListener);

		cmbStepsizeUnit = new Combo(grpParameters, SWT.READ_ONLY);
		cmbStepsizeUnit.setItems(Constants.TIME_UNIT_OPTIONS);
		cmbStepsizeUnit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbStepsizeUnit.select(Constants.DEFAULT_UNIT_INDEX);

		Label lblOverhead = new Label(grpParameters, SWT.NONE);
		lblOverhead.setToolTipText("Scheduling Overhead");
		lblOverhead.setText("Overhead:");

		txtOverhd = new Text(grpParameters, SWT.BORDER);
		txtOverhd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtOverhd.setText(Constants.DEFAULT_OVERHEAD);
		txtOverhd.addListener(SWT.Verify, textListener);

		cmbOverhdUnit = new Combo(grpParameters, SWT.READ_ONLY);
		cmbOverhdUnit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbOverhdUnit.setItems(Constants.TIME_UNIT_OPTIONS);
		cmbOverhdUnit.select(2);

		Label lblETM = new Label(grpParameters, SWT.NONE);
		lblETM.setToolTipText("Execution Time Model");
		lblETM.setText("ETM:");

		cmbETM = new Combo(grpParameters, SWT.READ_ONLY);
		cmbETM.setItems(Constants.ETM_OPTIONS);
		cmbETM.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		cmbETM.select(Constants.DEFAULT_ETM_INDEX);

		Label lblPreemption = new Label(grpParameters, SWT.NONE);
		lblPreemption.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPreemption.setText("Preemption:");

		cmbPreemptn = new Combo(grpParameters, SWT.READ_ONLY);
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

		btnFilter = new Button(grpParameters, SWT.NONE);
		btnFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnFilter.setText("Filter");
		btnFilter.setEnabled(false);
		btnFilter.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				new FilterDialog(controller, parent.getShell()).open();
			}
		});

		Button btnSimulate = new Button(grpParameters, SWT.NONE);
		btnSimulate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSimulate.setText("Simulate");
		btnSimulate.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				startSimulation();
			}
		});
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

		createDataBinding();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createDataBinding() {
		DataBindingContext dBindCtx = new DataBindingContext();

		// Data binding for scheduling strategy
		SelectObservableValue selectedRadioButtonObservable = new SelectObservableValue();
		selectedRadioButtonObservable.addOption(btnEdf.getText(), WidgetProperties.buttonSelection().observe(btnEdf));
		selectedRadioButtonObservable.addOption(btnRms.getText(), WidgetProperties.buttonSelection().observe(btnRms));
		selectedRadioButtonObservable.addOption(btnDefault.getText(),
				WidgetProperties.buttonSelection().observe(btnDefault));
		IObservableValue modelObservableValue = PojoProperties.value(SimViewParameters.class, "strategy")
				.observe(simViewParams);
		dBindCtx.bindValue(selectedRadioButtonObservable, modelObservableValue);

		// Data binding for simulation time value and unit
		IObservableValue targetObservableValue = WidgetProperties.text(SWT.Modify).observe(txtSimTime);
		modelObservableValue = PojoProperties.value(SimViewParameters.class, "simTime").observe(simViewParams);
		dBindCtx.bindValue(targetObservableValue, modelObservableValue);

		targetObservableValue = WidgetProperties.comboSelection().observe(cmbSTimeUnit);
		modelObservableValue = PojoProperties.value(SimViewParameters.class, "simTimeUnit").observe(simViewParams);
		dBindCtx.bindValue(targetObservableValue, modelObservableValue);

		// Data binding for step size value and unit
		targetObservableValue = WidgetProperties.text(SWT.Modify).observe(txtStepsize);
		modelObservableValue = PojoProperties.value(SimViewParameters.class, "stepSize").observe(simViewParams);
		dBindCtx.bindValue(targetObservableValue, modelObservableValue);

		targetObservableValue = WidgetProperties.comboSelection().observe(cmbStepsizeUnit);
		modelObservableValue = PojoProperties.value(SimViewParameters.class, "stepSizeUnit").observe(simViewParams);
		dBindCtx.bindValue(targetObservableValue, modelObservableValue);

		// Data binding for scheduler overhead value and unit
		targetObservableValue = WidgetProperties.text(SWT.Modify).observe(txtOverhd);
		modelObservableValue = PojoProperties.value(SimViewParameters.class, "overhead").observe(simViewParams);
		dBindCtx.bindValue(targetObservableValue, modelObservableValue);

		targetObservableValue = WidgetProperties.comboSelection().observe(cmbOverhdUnit);
		modelObservableValue = PojoProperties.value(SimViewParameters.class, "overheadUnit").observe(simViewParams);
		dBindCtx.bindValue(targetObservableValue, modelObservableValue);

		// Data binding for ETM
		targetObservableValue = WidgetProperties.comboSelection().observe(cmbETM);
		modelObservableValue = PojoProperties.value(SimViewParameters.class, "etm").observe(simViewParams);
		dBindCtx.bindValue(targetObservableValue, modelObservableValue);

		// Data binding for preemption
		targetObservableValue = WidgetProperties.comboSelection().observe(cmbPreemptn);
		modelObservableValue = PojoProperties.value(SimViewParameters.class, "preemption").observe(simViewParams);
		dBindCtx.bindValue(targetObservableValue, modelObservableValue);
	}

	private void updateWidgetData(Amalthea model) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				controller.populateView();
			}
		});
	}

	private void startSimulation() {
		controller.startSimulation(simViewParams);
	}

	@Override
	public void setSimTimeValue(String value) {
		txtSimTime.setText(value);
	}

	@Override
	public void setSimTimeUnitIndex(int index) {
		cmbSTimeUnit.select(index);
	}

	@Override
	public void setPeriodicTasks(java.util.List<String> taskList) {
		listViewer.setInput(taskList);
	}

	@Override
	public void enableFiltering() {
		btnFilter.setEnabled(true);
	}

}
