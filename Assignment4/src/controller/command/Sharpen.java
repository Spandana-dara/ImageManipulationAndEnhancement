package controller.command;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModelNewFeature;

/**
 * This class represents the Sharpen command.
 */
public class Sharpen<T> implements ImageProcessingCommand<T> {

  private final String[] sharpInputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param inputDetails inputs given to the command
   */
  public Sharpen(String[] inputDetails) {
    this.sharpInputDetails = inputDetails;
  }

  @Override
  public Image execute(T image) throws Exception {
    return ((ImageProcessingModelNewFeature) image).sharpen(sharpInputDetails[0],
        sharpInputDetails[1]);
  }
}
