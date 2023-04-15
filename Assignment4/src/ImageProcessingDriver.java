import java.io.InputStreamReader;
import java.io.StringReader;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import controller.ImageProcessingGUIController;
import controller.ImageProcessingGUIControllerImpl;
import model.ImageProcessingModelNewFeature;
import model.ImageProcessingModelNewFeaturesImpl;
import view.ImageProcessingView;
import view.ImageProcessingViewFrameImpl;
import view.ImageProcessingViewImpl;

/**
 * This class drives the Image Processing Application.
 */
public class ImageProcessingDriver {

  /**
   * Create model, view and controller objects and call controller.
   *
   * @param args command line arguments
   * @throws Exception if input provided is invalid
   */
  public static void main(String[] args) throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    Readable in;
    if (args.length == 0) {
      ImageProcessingViewFrameImpl frame = new ImageProcessingViewFrameImpl();
      ImageProcessingGUIController controller = new ImageProcessingGUIControllerImpl(model);
      controller.setView(frame);
    } else {
      ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
      if (args[0].equals("-file")) {
        in = new StringReader("run" + " " + args[1]);
      } else {
        in = new InputStreamReader(System.in);
      }
      ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
      controller.simulate(in);
    }
  }
}