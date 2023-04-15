package view;

import java.io.PrintStream;

/**
 * This class represent the implementation of the ImageProcessingView. Acts as the view for the
 * application when user performs any operation through command line or file input etc.(Not GUI).
 */
public class ImageProcessingViewImpl implements ImageProcessingView {

  private final PrintStream out;

  private final StringBuilder sb;

  /**
   * Constructs the view of the application using the respective output stream.
   *
   * @param out print stream object used to display the output
   * @param sb  the output given by the model, for this version it stores the error messages
   */
  public ImageProcessingViewImpl(PrintStream out, StringBuilder sb) {
    super();
    this.out = out;
    this.sb = sb;
  }

  @Override
  public void displaySuccess() {
    this.out.println("Command executed successfully.");
  }

  @Override
  public void displayRespectiveError() {
    this.out.println(sb.toString());
  }
}
