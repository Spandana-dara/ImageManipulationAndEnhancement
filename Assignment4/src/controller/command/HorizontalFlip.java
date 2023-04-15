package controller.command;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModel;

/**
 * This class represents the horizontal-flip command.
 */
public class HorizontalFlip<T> implements ImageProcessingCommand<T> {

  private final String[] horizontalFlipInputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param inputDetails inputs given to the command
   */
  public HorizontalFlip(String[] inputDetails) {
    this.horizontalFlipInputDetails = inputDetails;
  }

  @Override
  public Image execute(T image) throws Exception {
    return ((ImageProcessingModel) image).horizontalFlip(horizontalFlipInputDetails[0],
        horizontalFlipInputDetails[1]);
  }
}
