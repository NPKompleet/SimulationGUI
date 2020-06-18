package org.eclipse.app4mc.visualization.timeline.parts;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

@Creatable
@Singleton
public class Controller {

	public void doSomething() {
		System.out.println("Doing something....");
	}
}
