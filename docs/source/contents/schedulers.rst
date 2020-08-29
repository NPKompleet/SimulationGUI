**********
Schedulers
**********
All schedulers used in the project must inherit from the abstract class 
**Scheduler.java** and override its ``getComparator()`` method to implement its 
own specific form of scheduling priority. The ``Scheduler.java`` class has a
method ``schedule()`` that is used by the processor discussed in the :ref:`SimulatingCores` section
to schedule jobs (see :ref:`SimulatingJobs`) to be executed on the processor.

The classes discussed in this section are found under the ``org.eclipse.app4mc.visualization.timeline.schedulers``
package in the main project folder.

-----------------
Scheduler Example
-----------------
Here is a simple example of the Earliest Deadline First scheduler implemented 
in the project.

.. code-block:: java
    :linenos:

    public class EDFScheduler extends Scheduler {

        @Override
        public Comparator<SimJob> getComparator() {
            return (a, b) -> a.getAbsoluteDeadline() - b.getAbsoluteDeadline();
        }
    }