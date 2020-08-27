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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.app4mc.visualization.timeline.simulation.SimJobSlice;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
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

/**
 * <p>
 * This class creates the dialog for filtering down the cores and tasks to be
 * visualized after the simulation.
 * </p>
 * <p>
 * Displays a tree of cores and tasks mapped to them.
 * </p>
 * 
 * @author Philip Okonkwo
 *
 */
public class FilterDialog extends Dialog {
	private Controller controller;
	private Tree tree;

	protected FilterDialog(Controller controller, Shell parentShell) {
		super(parentShell);
		this.controller = controller;
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
		tree = new Tree(container, SWT.CHECK | SWT.BORDER);
		GridData treeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		treeData.heightHint = 80;
		tree.setLayoutData(treeData);

		// Make sure that if a tree item is checked
		// all its children are checked too
		tree.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				if (evt.detail == SWT.CHECK) {
					TreeItem item = (TreeItem) evt.item;
					boolean isChecked = item.getChecked();
					Arrays.asList(item.getItems()).stream().forEach(child -> child.setChecked(isChecked));

					// Ensure parent item is checked when a child item is checked
					// and ensure a parent item is unchecked when all child items
					// are unchecked.
					TreeItem parentItem = item.getParentItem();
					if (isChecked && parentItem != null) {
						parentItem.setChecked(isChecked);
					} else if (!isChecked && parentItem != null
							&& !Stream.of(parentItem.getItems()).anyMatch(i -> i.getChecked() == !isChecked)) {
						parentItem.setChecked(isChecked);
					}
				}
			}
		});

		Map<String, List<String>> dataMap = controller.getFilterData();
		for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
			TreeItem coreItem = new TreeItem(tree, SWT.NULL);
			coreItem.setText(entry.getKey());
			coreItem.setChecked(true);
			coreItem.setExpanded(true);

			InputStream is = getClass().getResourceAsStream("ProcessingUnit.gif");
			Image img = new Image(getShell().getDisplay(), is);
			coreItem.setImage(img);

			is = getClass().getResourceAsStream("Task.gif");
			img = new Image(null, is);
			for (String task : entry.getValue()) {
				TreeItem taskItem = new TreeItem(coreItem, SWT.NULL);
				taskItem.setText(task);
				taskItem.setChecked(true);
				taskItem.setImage(img);
			}

		}

		Composite buttonComp = new Composite(container, SWT.NONE);
		buttonComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		RowLayout rLayout = new RowLayout();
		rLayout.marginBottom = 10;
		buttonComp.setLayout(rLayout);

		Button selectAllButton = new Button(buttonComp, SWT.NONE);
		selectAllButton.setText("Select All");
		selectAllButton.addListener(SWT.Selection, l -> setCheckValueForAllItems(true));

		Button deSelAllButton = new Button(buttonComp, SWT.NONE);
		deSelAllButton.setText("Deselect All");
		deSelAllButton.addListener(SWT.Selection, l -> setCheckValueForAllItems(false));

		return container;
	}

	/**
	 * Checks or unchecks all the items in the tree.
	 * 
	 * @param value The vlue that determines whether to check the items. It is true
	 *              for checking and false otherwise.
	 */
	private void setCheckValueForAllItems(boolean value) {
		Arrays.asList(tree.getItems()).stream().forEach(item -> {
			item.setChecked(value);
			Arrays.asList(item.getItems()).stream().forEach(childItem -> childItem.setChecked(value));
		});
	}

	@Override
	protected void okPressed() {
		LinkedHashMap<String, List<SimJobSlice>> filteredProcessedJobMap = new LinkedHashMap<>();
		LinkedHashMap<String, List<SimJobSlice>> processedJobMap = controller.getProcessedJobMap();

		// Goes through the tree and checks only selected items and adds
		// only the cores and tasks which are selected to which are selected
		// to the filteredProcessedJobMap. This will be use create a filtered
		// visualization.
		for (TreeItem coreItem : tree.getItems()) {
			if (coreItem.getChecked()) {
				String coreName = coreItem.getText();

				Set<String> taskNameSet = new HashSet<>();
				for (TreeItem taskItem : coreItem.getItems()) {
					if (taskItem.getChecked()) {
						taskNameSet.add(taskItem.getText());
					}
				}

				List<SimJobSlice> filteredJobSlice = new ArrayList<>();
				for (SimJobSlice jSlice : processedJobMap.get(coreName)) {
					if (taskNameSet.contains(jSlice.getParentTask().getName())) {
						filteredJobSlice.add(jSlice);
					}
				}
				filteredProcessedJobMap.put(coreName, filteredJobSlice);
			}
		}

		super.okPressed();
		controller.filterVisualization(filteredProcessedJobMap);
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
