package controller.command;

import java.util.List;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModel;

/**
 * This class represents the rgb-split command.
 */
public class RGBSplit<T> implements ImageProcessingCommand<T> {

  private final String[] rgbSplitInputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param inputDetails inputs given to the command
   */
  public RGBSplit(String[] inputDetails) {
    this.rgbSplitInputDetails = inputDetails;
  }

  @Override
  public List<Image> execute(T image) {
    return ((ImageProcessingModel) image).rgbSplit(rgbSplitInputDetails[0], rgbSplitInputDetails[1],
        rgbSplitInputDetails[2], rgbSplitInputDetails[3]);
  }
}
