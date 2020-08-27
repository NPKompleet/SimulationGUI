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
package org.eclipse.app4mc.visualization.timeline.utils;

@SuppressWarnings("serial")
public class GlobalSchedulingException extends Exception {
	private static String message = "The tasks running on the processors are not partitioned. "
			+ "Global scheduling not suppported yet.";

	public GlobalSchedulingException() {
		super(message);
	}

	@Override
	public String toString() {
		return "Global Scheduling Unsupported";
	}

}
