package controller.command;

import controller.ImageProcessingCommand;
import model.Image;
import model.ImageProcessingModel;

/**
 * This class represents the brighten command.
 */
public class Brighten<T> implements ImageProcessingCommand<T> {

  private final String[] brightenInputDetails;

  /**
   * Construct the class with given inputs from the controller.
   *
   * @param inputDetails inputs given to the command
   */
  public Brighten(String[] inputDetails) {
    this.brightenInputDetails = inputDetails;
  }

  @Override
  public Image execute(T image) throws Exception {
    return ((ImageProcessingModel) image).brighten(Integer.parseInt(brightenInputDetails[0]),
        brightenInputDetails[1], brightenInputDetails[2]);
  }
}
