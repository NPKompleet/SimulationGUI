===========================
Introduction To The Project
===========================

.. image:: images/finalresult.gif
   :alt: Project Demo Gif

***************************
Scope of This Documentation
***************************

This application document is set up to explain how to use and extend the task scheduling simulation GUI 
created for `Eclipse APP4MC <https://www.eclipse.org/app4mc/>`_ as a contribution to the Google Summer of Code 2020.

********************
Scope of The Project
********************

The project involved creating an Eclipse plug-in for the simulation visualization of rudimentary
scheduling algorithms like `EDF <https://en.wikipedia.org/wiki/Earliest_deadline_first_scheduling>`_ 
and `RM <https://en.wikipedia.org/wiki/Rate-monotonic_scheduling>`_. The idea is to be able to select
an APP4MC model in the Eclipse APP4MC IDE and be able to simulate the tasks
running on each core stored in the model using any chosen scheduling
algorithm.

The project involved:

    -   Creating a user interface for visualization of the simulation.
    -   Creating a simulation backend using rudimentary schedulers like 
        Earliest Deadline First and Rate Montonic Scheduling.
    -   Connecting the simulation to the user interface for visualizing the results.
    -   Testing and documentation.


--------------------
Code Organisation
--------------------
The code is organised into 4 separate Eclipse plug-in projects. Each of the projects
hold different aspects of the application.

    -   ``org.eclipse.app4mc.visualization.timeline`` - The main project folder. **This documentation focuses 
        on the contents of this project folder**.
    -   ``org.eclipse.nebula.timeline`` - The modified Nebula Timeline widget used to visualize the simulation.
    -   ``plugin.des.desmoj`` - The DESMO-J discrete event simulator library.
    -   ``org.eclipse.app4mc.visualization.timeline.test`` - The project holding all the software testing done.


.. _Dependencies:

--------------------
Project Dependencies
--------------------

This project was built using the following libraries:

    - `Nebula Timeline widget. <https://www.eclipse.org/nebula/widgets/timeline/timeline.php>`_
    - `DESMO-J <http://desmoj.sourceforge.net/home.html>`_, a Java library for discrete event simulation.
    - `Jface <https://wiki.eclipse.org/JFace>`_. Used to create the UI elements.
    - `Draw2d <https://www.eclipse.org/gef/draw2d/index.php>`_. Used for creating custom annotations.

.. note:: The Nebula Timeline Widget does not currently support annotations 
            so modifications had to be made to it to be able to use it to 
            achieve the aims of this project.

The imported packages needed to get this project to work are:

    - ``org.eclipse.app4mc.amalthea.model``. To get access to the model element that should be rendered.
    - ``org.eclipse.app4mc.visualization.ui.registry``. Needed to import the necessary interface. 
    - ``org.osgi.service.component.annotations``. Needed to get access to the OSGi DS annotations.



