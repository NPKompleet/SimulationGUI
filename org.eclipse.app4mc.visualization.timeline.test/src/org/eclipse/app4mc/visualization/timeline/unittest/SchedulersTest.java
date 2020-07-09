package org.eclipse.app4mc.visualization.timeline.unittest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.app4mc.visualization.timeline.schedulers.EDFScheduler;
import org.eclipse.app4mc.visualization.timeline.schedulers.RMScheduler;
import org.eclipse.app4mc.visualization.timeline.simulation.Scheduler;
import org.eclipse.app4mc.visualization.timeline.simulation.SimJob;
import org.eclipse.app4mc.visualization.timeline.simulation.SimTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchedulersTest {
	@Mock
	SimTask simTask1;
	@Mock
	SimTask simTask2;
	@Mock
	SimTask simTask3;

	@Mock
	SimJob job1;
	@Mock
	SimJob job2;
	@Mock
	SimJob job3;

	Scheduler scheduler;
	LinkedBlockingDeque<SimJob> jobQueue;

	@Before
	public void setUp() {

		when(simTask1.getPeriod()).thenReturn(7);
		when(simTask2.getPeriod()).thenReturn(10);
		when(simTask3.getPeriod()).thenReturn(4);

		when(job1.getParentTask()).thenReturn(simTask1);
		when(job1.getAbsoluteDeadline()).thenReturn(5);
		when(job2.getParentTask()).thenReturn(simTask2);
		when(job2.getAbsoluteDeadline()).thenReturn(3);
		when(job3.getParentTask()).thenReturn(simTask3);
		when(job3.getAbsoluteDeadline()).thenReturn(4);

		jobQueue = new LinkedBlockingDeque<>();
		jobQueue.add(job1);
		jobQueue.add(job2);
		jobQueue.add(job3);
	}

	@Test
	public void edfSchedulerShouldWork() {
		scheduler = new EDFScheduler();
		LinkedBlockingDeque<SimJob> result = scheduler.schedule(jobQueue);

		assertEquals(job2, result.getFirst());
		assertEquals(job1, result.getLast());
	}

	@Test
	public void rmSchedulerShouldWork() {
		scheduler = new RMScheduler();
		LinkedBlockingDeque<SimJob> result = scheduler.schedule(jobQueue);

		assertEquals(job3, result.getFirst());
		assertEquals(job2, result.getLast());
	}
}
