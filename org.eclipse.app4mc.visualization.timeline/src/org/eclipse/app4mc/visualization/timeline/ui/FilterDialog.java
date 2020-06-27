package org.eclipse.app4mc.visualization.timeline.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class FilterDialog extends Dialog {

	protected FilterDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 8;
		layout.marginTop = 3;
		container.setLayout(layout);
		Label text = new Label(container, SWT.NONE);
		text.setText("Select the cores/tasks whose simulation you want to view:");
		Tree tree = new Tree(container, SWT.CHECK | SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TreeItem item1 = new TreeItem(tree, SWT.NULL);
		item1.setText("Item1");
		TreeItem item2 = new TreeItem(tree, SWT.NULL);
		item2.setText("Item2");
		TreeItem item3 = new TreeItem(tree, SWT.NULL);
		item3.setText("Item3");
		TreeItem item4 = new TreeItem(tree, SWT.NULL);
		item4.setText("Item4");
		TreeItem item5 = new TreeItem(tree, SWT.NULL);
		item5.setText("Item5");
		TreeItem item6 = new TreeItem(item5, SWT.NULL);
		item6.setText("Item6");

		Composite buttonComp = new Composite(container, SWT.NONE);
		buttonComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		RowLayout rLayout = new RowLayout();
		rLayout.marginBottom = 10;
		buttonComp.setLayout(rLayout);

		Button selectAllButton = new Button(buttonComp, SWT.PUSH);
		selectAllButton.setText("Select All");

		Button deSelAllButton = new Button(buttonComp, SWT.PUSH);
		deSelAllButton.setText("Deselect All");

		return container;
	}

	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		super.okPressed();
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Filter Tasks");
		super.configureShell(newShell);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 250);
	}

}
