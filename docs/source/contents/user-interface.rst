Grachical User Interface
========================

This project was started as a Google Summmer of Code 2020 project.
All current code was written during the GSoC 2020 coding exercise.

********************
Software Development 
********************

The software development for this project includes:

    -   Extending the Nebula Timeline widget to include the possibility
        of adding annotations that indicate arrival time and deadlines
        of tasks in the timeline view.

    -   Creating the GUI for users to enter simulation parameters.
    
    -   Implementing the simulation backend by creating rudimentary
        schedulers like Earliest Deadline First or Rate Monotonic.

    -   Creating a visualization of the simulation of APP4MC models by
        connecting the model to the simulation back end and rendering
        the results on the GUI.

**************
User Interface
**************
The user interface is built on the visualization framework for Eclipse
APP4MC created by Dirk Fauth. It uses SWT and Jface for the UI components
and it also uses OSGi Declarative Services.

----------------
How It Works
----------------
The plug-in ``org.eclipse.app4mc.visualization.ui`` [1]_ provides a small framework 
to visualize model elements. It contains a view part ``VisualizationPart`` that 
can be opened via `Window – Show View – Other… – APP4MC – APP4MC Visualizations`, 
or via right click on an element and selecting `Open APP4MC Visualization`. 
Via the context menu it is also possible to open multiple instances of the 
VisualizationPart.

On selecting a model element, the ``VisualizationPart`` is updated to render a 
corresponding visualization. The visualization to render is searched in the 
``ModelVisualizationRegistry`` OSGi service. If multiple visualizations are 
registered, the first one will be selected by default, unless the user has 
selected another visualization before.

The visualization view has 3 buttons in the toolbar:

    1.  **Visualization dropdown** - The dropdown contains all available 
        visualizations for the current active selection. A click on the image 
        will reload the visualization.

    2.  **Pin visualization** - The selection handling will be disabled so the 
        visualization gets not updated on model selection changes.

    3.  **Select model element** - Selects the current visualized model element 
        in the model editor. Useful for example if a visualization is pinned and 
        the selection in the model editor changed.

.. image:: images/viz-framework.png
   :alt: arrows
   :align: center

The whole area within the red box in the picture above is what is 
used to render the visualization in Eclipse APP4MC.

----------------
Implementation
----------------

--------------------
Creating Annotations
--------------------
Annotations were created to indicate arrival time and deadlines of
tasks in the visualization. An up-pointing arrow and a down-pointing
arrows were used to represent arrival and deadline respectively.

.. image:: images/tmarrows.png
   :alt: arrows
   :align: center

Each annotation is designed to be constrained to one lane and to
extend and contract along with the height of the lane.

To create a new annotation, the ``AnnotationFigure`` class of the Timeline widget should
be extended. The class has been created specifically for that
purpose. Only on rare occasions should one extend the ``IAnnotationFigure``
interface directly.

.. [1] The need for this has been briefly highted in the :ref:`Dependencies` section.

