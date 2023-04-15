package controller.command;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModelNewFeature;

/**
 * This class represents the sepia command.
 */
public class Sepia<T> implements ImageProcessingCommand<T> {

  private final String[] inputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param inputDetails inputs given to the command
   */
  public Sepia(String[] inputDetails) {
    this.inputDetails = inputDetails;
  }

  @Override
  public Image execute(T image) throws Exception {
    return ((ImageProcessingModelNewFeature) image).sepia(inputDetails[0], inputDetails[1]);
  }
}
