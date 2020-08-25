**********
Schedulers
**********
All schedulers used in the project must inherit from the abstract class 
**Scheduler.java** and override its ``getComparator()`` method to implement its 
own specific form of scheduling priority.

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