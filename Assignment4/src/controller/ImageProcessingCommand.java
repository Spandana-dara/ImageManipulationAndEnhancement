package controller;

/**
 * This interface represents each command that can be executed by the model.
 */
public interface ImageProcessingCommand<T> {

  /**
   * Execute the respective command using the passed model object.
   *
   * @param image the model object that is any implementation of the interfaces ImageProcessingModel
   *              or ImageProcessingModelNewFeatures
   * @return an object that is the result of each command
   * @throws Exception when the provided command causes an exception in the method of a model
   */
  Object execute(T image) throws Exception;

}
