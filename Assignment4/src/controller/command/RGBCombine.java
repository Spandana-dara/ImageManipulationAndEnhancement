package controller.command;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModel;

/**
 * This class represents the rgb-combine command.
 */
public class RGBCombine<T> implements ImageProcessingCommand<T> {

  private final String[] rgbCombineInputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param inputDetails inputs given to the command
   */
  public RGBCombine(String[] inputDetails) {
    this.rgbCombineInputDetails = inputDetails;
  }

  @Override
  public Image execute(T image) {
    return ((ImageProcessingModel) image).rgbCombine(rgbCombineInputDetails[0],
        rgbCombineInputDetails[1], rgbCombineInputDetails[2], rgbCombineInputDetails[3]);
  }
}
