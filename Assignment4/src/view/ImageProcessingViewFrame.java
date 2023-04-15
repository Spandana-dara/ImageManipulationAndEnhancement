package view;

import controller.ImageProcessingGUIController;

/**
 * This interface represents the GUI of an image processing application.
 */
public interface ImageProcessingViewFrame {

  /**
   * This method is used to pass the ImageProcessingGUIController object in order to send the inputs
   * to the controller.
   *
   * @param controller ImageProcessingGUIController object used to send the input to the controller
   */
  void addController(ImageProcessingGUIController controller);

  /**
   * This method displays an error dialog box when an operation has failed to execute.
   */
  void displayErrorDialogBox();

  /**
   * This method refreshes the GUI by setting the resultant image after a successful execution of an
   * operation.
   *
   * @param image represents the image to be displayed on the GUI.
   */
  void refreshImageOnScreen(Object image);

  /**
   * This method displays an error dialog box when an operation is performed before loading the
   * image.
   */
  void displayErrorLoadFirstDialogBox();

}
