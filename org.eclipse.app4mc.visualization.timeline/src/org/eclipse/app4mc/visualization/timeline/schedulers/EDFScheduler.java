package org.eclipse.app4mc.visualization.timeline.schedulers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.app4mc.visualization.timeline.models.TaskInstance;
import org.eclipse.app4mc.visualization.timeline.models.TaskJob;
import org.eclipse.app4mc.visualization.timeline.models.TaskSlice;
import org.eclipse.app4mc.visualization.timeline.utils.TaskUtil;

public class EDFScheduler {
	List<TaskJob> jobList;
	List<TaskInstance> instList;

	public EDFScheduler(List<TaskJob> jobList) {
		this.jobList = jobList;
	}

	public void schedule() {

		Comparator<TaskJob> deadlineComparator = Comparator.comparingInt(TaskJob::getDeadline);
		jobList.sort(deadlineComparator);
		int[] periodList = jobList.stream().mapToInt(TaskJob::getPeriod).toArray();
		int lcm = TaskUtil.calcLCM(periodList);

		for (int i = 0; i <= lcm; i++) {
			for (TaskJob job : jobList) {
				if (i - job.getReleaseTime() % job.getPeriod() == 0 && i >= job.getReleaseTime()) {
					int start = i;
					int end = start + job.getExecutionTime();
					int priority = start + job.getDeadline();
					instList.add(new TaskInstance(start, end, priority, job.getName()));
				}
			}

		}

		int progressStep = 1;
		TaskInstance lastTaskOnCPU = null;
		TaskSlice currentTaskSlice = null;
		List<TaskInstance> possible = new ArrayList<>();
		List<TaskSlice> scheduledTasks = new ArrayList<>();
		for (int i = 0; i <= lcm; i += progressStep) {
			for (TaskInstance instance : instList) {
				if (instance.getStart() <= i) {
					possible.add(instance);
				}
			}
			Comparator<TaskInstance> priorityComparator = Comparator.comparingInt(TaskInstance::getPriority);
			possible.sort(priorityComparator);

			if (possible.size() > 0) {
				TaskInstance taskOnCPU = possible.get(0);
				if (taskOnCPU != lastTaskOnCPU) {
					if (lastTaskOnCPU != null) {
						currentTaskSlice.setEnd(i - 1);
						scheduledTasks.add(currentTaskSlice);
					}
					currentTaskSlice = new TaskSlice(i, taskOnCPU);
					lastTaskOnCPU = taskOnCPU;
				}

				taskOnCPU.addUsage(progressStep);
				if (taskOnCPU.isCompleted()) {
					instList.remove(taskOnCPU);
					currentTaskSlice.setEnd(i);
					scheduledTasks.add(currentTaskSlice);
					// finished
				}
			}
		}
	}

	public float getUtilization() {
		float utilization = 0;
		for (TaskJob job : jobList) {
			utilization += (float) job.getExecutionTime() / job.getPeriod();
		}
		return utilization;
	}

}
