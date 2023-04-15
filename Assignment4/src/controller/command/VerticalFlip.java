package controller.command;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModel;

/**
 * This class represents the vertical-flip command.
 */
public class VerticalFlip<T> implements ImageProcessingCommand<T> {

  private final String[] verticalFlipInputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param inputDetails inputs given to the command
   */
  public VerticalFlip(String[] inputDetails) {
    this.verticalFlipInputDetails = inputDetails;
  }

  @Override
  public Image execute(T image) throws Exception {
    return ((ImageProcessingModel) image).verticalFlip(verticalFlipInputDetails[0],
        verticalFlipInputDetails[1]);
  }
}
