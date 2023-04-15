package controller;

import view.ImageProcessingViewFrame;

/**
 * This interface represents the controller for the GUI of the application.
 */
public interface ImageProcessingGUIController extends ImageProcessingController {

  /**
   * This method sets the view and assigns ImageProcessingGUIController to the view.
   *
   * @param view GUI of the application.
   */
  void setView(ImageProcessingViewFrame view);

  /**
   * This method is used to create the command and execute the actions performed on the view.
   *
   * @param buttonPressed the action command string related to the button clicks
   * @param loadFlag      a boolean to check if the load operation is performed
   */
  void executeTheInput(String buttonPressed, boolean loadFlag);

}
