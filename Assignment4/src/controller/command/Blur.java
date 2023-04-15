package controller.command;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModelNewFeature;

/**
 * This class represents the Blur command.
 */
public class Blur<T> implements ImageProcessingCommand<T> {

  private final String[] blurInputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param inputDetails inputs given to the command
   */
  public Blur(String[] inputDetails) {
    this.blurInputDetails = inputDetails;
  }

  @Override
  public Image execute(T image) throws Exception {
    return ((ImageProcessingModelNewFeature) image).blur(blurInputDetails[0], blurInputDetails[1]);
  }
}
