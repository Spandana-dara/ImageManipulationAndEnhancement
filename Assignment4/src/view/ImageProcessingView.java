package view;

/**
 * This interface represents the text view of an image processing application.
 */
public interface ImageProcessingView {

  /**
   * This method is used to display a success message when the command is successfully executed.
   */
  void displaySuccess();

  /**
   * This method is used to display respective error message when the command cannot be executed.
   */
  void displayRespectiveError();
}
