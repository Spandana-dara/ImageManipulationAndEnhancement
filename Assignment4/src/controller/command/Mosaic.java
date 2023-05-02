package controller.command;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModelNewFeature;

/**
 * This class represents the Mosaic command.
 */
public class Mosaic<T> implements ImageProcessingCommand<T> {
  private final String[] mosaicInputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param inputDetails inputs given to the command
   */
  public Mosaic(String[] inputDetails) {
    this.mosaicInputDetails = inputDetails;
  }

  @Override
  public Image execute(T image) throws Exception {
    return ((ImageProcessingModelNewFeature) image).mosaic(Integer.parseInt(mosaicInputDetails[0]),
            mosaicInputDetails[1], mosaicInputDetails[2]);
  }
}
