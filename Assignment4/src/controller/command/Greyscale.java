package controller.command;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModel;

/**
 * This class represents the greyscale command.
 */
public class Greyscale<T> implements ImageProcessingCommand<T> {

  private final String[] greyInputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param greyInputDetails inputs given to the command
   */
  public Greyscale(String[] greyInputDetails) {
    this.greyInputDetails = greyInputDetails;
  }

  @Override
  public Image execute(T image) {
    return ((ImageProcessingModel) image).greyScale(greyInputDetails[0], greyInputDetails[1],
        greyInputDetails[2]);
  }
}
