package controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import controller.command.Blur;
import controller.command.Brighten;
import controller.command.Dither;
import controller.command.Greyscale;
import controller.command.HorizontalFlip;
import controller.command.RGBCombine;
import controller.command.RGBSplit;
import controller.command.Sepia;
import controller.command.Sharpen;
import controller.command.VerticalFlip;
import model.Image;
import model.ImageProcessingModelNewFeature;
import view.ImageProcessingView;

/**
 * This class implements the ImageProcessingController. It takes in the input command to perform
 * respective operation on the loaded image.
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {

  private final ImageProcessingModelNewFeature image;
  private ImageProcessingView view;
  private Scanner fetchInputs = null;
  protected Object commandOutput;

  /**
   * Create a controller to work with the ImageProcessingModel.
   *
   * @param model ImageProcessingModelNewFeature object which performs all the operations
   * @param view  ImageProcessingView object that is used to show the results
   */
  public ImageProcessingControllerImpl(ImageProcessingModelNewFeature model,
      ImageProcessingView view) {
    this.image = model;
    this.view = view;
  }

  /**
   * Create a controller to work with the ImageProcessingModel with respect to the GUI.
   *
   * @param model ImageProcessingModelNewFeature object which performs all the operations
   */
  public ImageProcessingControllerImpl(ImageProcessingModelNewFeature model) {
    this.image = model;
  }

  @Override
  public void simulate(Readable in) throws Exception {
    Scanner sc = new Scanner(in);
    Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands = new HashMap<>();
    knownCommands.put("greyscale", s -> new Greyscale(getInputDetails(3, fetchInputs)));
    knownCommands.put("horizontal-flip", s -> new HorizontalFlip(getInputDetails(2, fetchInputs)));
    knownCommands.put("vertical-flip", s -> new VerticalFlip(getInputDetails(2, fetchInputs)));
    knownCommands.put("brighten", s -> new Brighten(getInputDetails(3, fetchInputs)));
    knownCommands.put("rgb-split", s -> new RGBSplit(getInputDetails(4, fetchInputs)));
    knownCommands.put("rgb-combine", s -> new RGBCombine(getInputDetails(4, fetchInputs)));
    knownCommands.put("blur", s -> new Blur(getInputDetails(2, fetchInputs)));
    knownCommands.put("sharpen", s -> new Sharpen(getInputDetails(2, fetchInputs)));
    knownCommands.put("sepia", s -> new Sepia(getInputDetails(2, fetchInputs)));
    knownCommands.put("dither", s -> new Dither(getInputDetails(2, fetchInputs)));
    while (sc.hasNext()) {
      String input = sc.next();
      if (input.startsWith("#")) {
        sc.nextLine();
        continue;
      }
      forSwitch(input, knownCommands, sc);
      if (view != null) {
        view.displaySuccess();
      }
    }
  }

  private void forSwitch(String input,
      Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands, Scanner sc)
      throws Exception {
    switch (input) {
      case "run":
        String fileName = sc.next();
        executeCommandFile(fileName, knownCommands);
        break;
      case "load":
        load(getInputDetails(2, sc));
        break;
      case "save":
        String saveImagePath = sc.next();
        String saveImageName = sc.next();
        save(saveImagePath, saveImageName);
        break;
      default:
        fetchInputs = sc;
        executeCommand(knownCommands, input, fetchInputs);
    }
  }

  private void executeCommand(Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands,
      String input, Scanner sc)
      throws Exception {
    ImageProcessingCommand executableCommand;
    Function<Scanner, ImageProcessingCommand> cmd = knownCommands.getOrDefault(input, null);
    if (cmd == null) {
      throw new IllegalArgumentException(String.format("Unknown command %s", input));
    } else {
      executableCommand = cmd.apply(sc);
      commandOutput = executableCommand.execute(image);
      if (commandOutput == null) {
        if (view != null) {
          view.displayRespectiveError();
        }
        throw new IllegalArgumentException();
      }
    }
  }

  private void executeCommandFile(String fileName,
      Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands) throws Exception {
    Scanner scanner = null;
    try {
      File inputFile = new File(fileName);
      scanner = new Scanner(inputFile);
    } catch (Exception e) {
      throw new IllegalArgumentException("Provided file is not found.");
    }
    fetchInputs = scanner;
    while (scanner.hasNext()) {
      String input = scanner.next();
      if (input.startsWith("#")) {
        scanner.nextLine();
        continue;
      }
      forSwitch(input, knownCommands, scanner);
    }
  }

  private String[] getInputDetails(int size, Scanner sc) {
    String[] inputDetails = new String[size];
    for (int i = 0; i < size; i++) {
      if (!sc.hasNext()) {
        throw new IllegalArgumentException("Command is incomplete.");
      }
      inputDetails[i] = sc.next();
    }
    return inputDetails;
  }

  private void load(String[] inputs) {
    int[][][] imageArray = new int[0][][];
    String extension = checkImageExtension(inputs[0]);
    try {
      if (extension.equals("ppm")) {
        imageArray = ImageUtil.readPPM(inputs[0]);
      } else {
        imageArray = ImageUtil.readOtherImageFormats(inputs[0]);
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("The provided file is not found");
    }
    commandOutput = image.load(inputs[1], new Image(imageArray));
  }

  private String checkImageExtension(String fileName) {
    String extension = "";
    int i = fileName.lastIndexOf('.');
    if (i > 0) {
      extension = fileName.substring(i + 1);
    }
    return extension;
  }

  private void save(String imagePath, String imageName) {
    Image i = image.save(imageName);
    if (i.getImageArray() == null) {
      throw new IllegalArgumentException("The referred image is not found.");
    }
    String extension = checkImageExtension(imagePath);
    try {
      if (extension.equals("ppm")) {
        ImageUtil.generatePPMFile(i.getImageArray(), imagePath);
      } else {
        ImageUtil.saveImage(i.getImageArray(), extension, imagePath);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("The provided file path is not found");
    }
  }

}
