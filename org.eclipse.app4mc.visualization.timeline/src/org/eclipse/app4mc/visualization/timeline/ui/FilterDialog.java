package org.eclipse.app4mc.visualization.timeline.ui;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class FilterDialog extends Dialog {
	Map<String, List<String>> dataMap;

	protected FilterDialog(Controller controller, Shell parentShell) {
		super(parentShell);
		this.dataMap = controller.getFilterData();
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

		for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
			TreeItem coreItem = new TreeItem(tree, SWT.NULL);
			coreItem.setText(entry.getKey());
			coreItem.setChecked(true);
			for (String task : entry.getValue()) {
				TreeItem taskItem = new TreeItem(coreItem, SWT.NULL);
				taskItem.setText(task);
				taskItem.setChecked(true);
			}
		}

		Composite buttonComp = new Composite(container, SWT.NONE);
		buttonComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		RowLayout rLayout = new RowLayout();
		rLayout.marginBottom = 10;
		buttonComp.setLayout(rLayout);

		Button selectAllButton = new Button(buttonComp, SWT.NONE);
		selectAllButton.setText("Select All");
		selectAllButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				tree.selectAll();
			}
		});

		Button deSelAllButton = new Button(buttonComp, SWT.NONE);
		deSelAllButton.setText("Deselect All");
		deSelAllButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				tree.deselectAll();
			}
		});

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
