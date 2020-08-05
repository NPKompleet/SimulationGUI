package org.eclipse.app4mc.visualization.timeline.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class SimProgressDialog extends Dialog {

	protected SimProgressDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 10;
		layout.marginHeight = 20;
		container.setLayout(layout);
		Label text = new Label(container, SWT.NONE);
		text.setText("Processing simulation...");
		ProgressBar pBar = new ProgressBar(container, SWT.INDETERMINATE);
		pBar.setData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return container;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}
}
