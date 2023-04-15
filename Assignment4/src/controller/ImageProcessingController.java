package controller;

/**
 * This interface represents the controller of an image processing application.
 */
public interface ImageProcessingController {

  /**
   * The main method that relinquishes control of the application to the controller.
   *
   * @param in a command or commands that are to be executed
   * @throws Exception when the command is wrong, ppm file is of incorrect format or file is not
   *                   found
   */
  void simulate(Readable in) throws Exception;
}
