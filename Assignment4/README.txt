04/11/2023 - GUI implementation
===============================

Updates:
--------
In this update the application supports interaction with the help of GUI. All the basic operations
that are supported with the script or text input can be performed using the GUI.

Current Design:
---------------
 The project still follows MVC (Model-View-Controller) architecture with new components added to
 different parts of the project.

New components added to Controller:
-----------------------------------
A new interface named "ImageProcessingGUIController" is created extending the old inorder to add
the GUI controls to the application.
A new class named "ImageProcessingGUIControllerImpl" is created that implements the new interface
and extends the older controller class "ImageProcessingControllerImpl".

New components added to the view:
---------------------------------
A new interface named "ImageProcessingViewFrame" is created which implements all the supporting
methods for the GUI.
A new class named "ImageProcessingViewFrameImpl" is created that extends the JFrame and implements
the new interface and ActionListener. This class has all the GUI's UI implementation and the
action listeners that are performed after a certain action.

Major changes in the code and reasons for it:
---------------------------------------------
1) Added new Interface "ImageProcessingGUIController" that adds GUI to the application and extends
"ImageProcessingController" interface to support the previous operations.
Added a new class "ImageProcessingGUIControllerImpl" that implements the new interface and extends
the "ImageProcessingControllerImpl" class. This class communicates with the GUI.
Reason for new interface and class:
We have added a new interface and class so there will be no new additions or changes to the existing
code and the controller operations for GUI are separated form the other type of inputs possible. The
code for both the versions is different so, it is easy to debug.

2) Added new Interface "ImageProcessingViewFrame" that implements all supporting operations in the
GUI.
Added a new class "ImageProcessingViewFrameImpl" that implements the new interface and
ActionListener and extends the JFrame class. This class has all the GUI's implementation and the
action listeners that are performed after a user performs an action.
Reason for new interface and class:
We have added the GUI feature in a new interface and class so there will be no new changes to the
existing code. This also separates the GUI from other view features that can be set by passing to
the previous view. It is easy to debug.


====================================================================================================

03/29/2023 - Additions to the application
=========================================

Updates:
--------
In this update the application can now be used to blur, sharpen an image. It can also be converted
to an older image representations like sepia and dither.
Grey-scaling Luma operation uses a new method to convert to greyscale.
Different image formats (png, jpg and bpm etc.) can now be loaded and saved using the application in
addition to the ppm. An image can be loaded in a format and saved in another.
====================================================================================================

Current Design:
---------------
The project still follows MVC (Model-View-Controller) architecture with new components added to
different parts of the project.

New Components added to Model:
------------------------------
A new interface named "ImageProcessingModelNewFeature" is created extending the old inorder to add
the new functionalities that can be performed on an image.
A new class named "ImageProcessingModelNewFeaturesImpl" is created that implements the new interface
and extends the older model class ImageProcessingModelImpl.

New Components added to Controller:
-----------------------------------
Since the controller follows Command Design pattern to add the new functionalities new command
classes have been added in the command package.
Classes that are added:
Blur - Implementation of the blur command
Dither - Implementation of the dither command
Sepia - Implementation of the sepia command
Sharpen - Implementation of the sharpen command

Major changes in the code and reasons for it:
---------------------------------------------
1) Added new Interface "ImageProcessingModelNewFeature" which has all the new operations that can be
done on an image and extends the "ImageProcessingModel" interface to support the previous operations.
Added a new class "ImageProcessingModelNewFeatureImpl" that implements the new interface and extends
 the "ImageProcessingModelImpl" class. This class contains all execution of the new operations and
redirects the older operating to the parent class.
Reason for new interface and class:
We have added the new operations in a new interface and class so there will be no new additions or
changes to the existing code. The code for both the versions is different so, it is easy to debug.
Rolling back to the older version is also easy.

2) The load and save operation of an image i.e., the File I/O part is moved to the controller
inorder to send a generic image array to the model to work without worrying about the loaded or
saved image format.


====================================================================================================

03/15/2023 - Initial build
==========================
Design of the project:
----------------------
  The project is build using the MVC (Model-View-Controller) architecture where the controller takes
  inputs in command format where each command specifies what action should be done on the image and
  the model performs the operations on the image.
----------------------------------------------------------------------------------------------------

Model:
------
  The 'ImageProcessingModel' interface has all the operations that can be performed on the given image.
  The 'ImageProcessingModelImpl' implements the interface to implement functionality to the operations.
----------------------------------------------------------------------------------------------------

Controller:
-----------
  The 'ImageProcessingController' represents the controller of an image processing application.
  This interface is used to start controller of the project.
  'ImageProcessingControllerImpl' class implements the controller interface and is responsible for
  reading the command give as input.

  There are several commands that are implemented in this project and there is a wide scope for
  adding more, so we have used Command Design pattern in the controller. Therefore, each class in
  the ‘command’ package represents a command that is used to perform a specific action.
  Load - Implementation of the load command
  Save - Implementation of the save command
  Brighten - Implementation of the brighten command
  Greyscale - Implementation of the greyscale command
  HorizontalFlip - Implementation of the horizontal-flip command
  VerticalFlip - Implementation of the vertical-flip command
  RGBSplit - Implementation of the rgb-split command
  RGBCombine - Implementation of the rgb-combine command
  Run - Implementation of the run command

  ImageProcessingCommand is an interface that is implemented by all the above command classes.
  All the command classes override a single method which makes the call to the respective operation
  in the model.
----------------------------------------------------------------------------------------------------

View:
-----
  'ImageProcessingView' interface is used as a view for the application. Currently, the view is only
  text based, it only shows whether the command is executed correctly and if not why it failed. This
  can be extended based on future assignments.
  'ImageProcessingViewImpl' class has all the implementations of the methods in the
  ImageProcessingView interface.
----------------------------------------------------------------------------------------------------

Flow of the application:
------------------------
  The controller take the command as input, it then creates the command class with respect to the
  command that is inputted by passing the model object. The command class then executes the command
  using the model

====================================================================================================

Script file that has commands:
------------------------------
  'building.txt'
Process to run the script commands:
-----------------------------------
  Once the driver class is executed, you can either execute each command in the script file
  individually by pasting it in the terminal as command line argument or enter the command line
  argument "run building.txt" to rin all at once.

====================================================================================================

Image Used for testing:
-----------------------
  ppm Image: 'building.ppm'
  Citation: It is our own image clicked on a DSLR and resized to fit in the file size limit. We
  authorize this image to be used in the project.

  For testing the model we used a small and simple image which we created on our own, it consists
  of 6 pixels with width 3 and height 2.
  ppm Image name: 'testImage.ppm'

====================================================================================================

