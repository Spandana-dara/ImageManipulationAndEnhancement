package controller;

import java.io.File;
import java.io.StringReader;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.ImageProcessingModelNewFeature;
import view.ImageProcessingViewFrame;

/**
 * This class represents the controller implementation for the GUI. It sets the view for the GUI and
 * processes the requested operation by passing it to the model through the
 * ImageProcessingControllerImpl class.
 */
public class ImageProcessingGUIControllerImpl extends ImageProcessingControllerImpl implements
    ImageProcessingGUIController {

  private ImageProcessingViewFrame frame;
  private String command;
  private String fileName;
  private final String[] redFilePath;
  private final String[] greenFilePath;
  private final String[] blueFilePath;

  /**
   * Create a controller to work with the GUI of the Image Processing application.
   *
   * @param model ImageProcessingModelNewFeature object which performs all the operations
   */
  public ImageProcessingGUIControllerImpl(ImageProcessingModelNewFeature model) {
    super(model);
    redFilePath = new String[1];
    greenFilePath = new String[1];
    blueFilePath = new String[1];
  }

  @Override
  public void setView(ImageProcessingViewFrame frame) {
    this.frame = frame;
    this.frame.addController(this);
  }

  @Override
  public void simulate(Readable in) throws Exception {
    super.simulate(in);
    if (commandOutput == null) {
      frame.displayErrorDialogBox();
    } else {
      frame.refreshImageOnScreen(commandOutput);
    }
  }

  @Override
  public void executeTheInput(String buttonPressed, boolean loadFlag) {
    command = "";
    if (buttonPressed.equals("load")) {
      String filePathToLoad = chooseFilePathToOpen();
      if (filePathToLoad.isEmpty()) {
        return;
      }
      command = "load " + filePathToLoad + " loadimage";
      fileName = "loadimage";
    } else if (loadFlag || buttonPressed.equals("rgb-combine")) {
      switch (buttonPressed) {
        case "horizontal-flip":
          command = "horizontal-flip " + fileName + " horizontal-flip-image";
          fileName = "horizontal-flip-image";
          break;
        case "vertical-flip":
          command = "vertical-flip " + fileName + " vertical-flip-image";
          fileName = "vertical-flip-image";
          break;
        case "blur":
          command = "blur " + fileName + " blur-image";
          fileName = "blur-image";
          break;
        case "sepia":
          command = "sepia " + fileName + " sepia-image";
          fileName = "sepia-image";
          break;
        case "dither":
          command = "dither " + fileName + " dither-image";
          fileName = "dither-image";
          break;
        case "sharpen":
          command = "sharpen " + fileName + " sharpen-image";
          fileName = "sharpen-image";
          break;
        case "save":
          String filePathToSave = choosePathToSaveFile();
          if (filePathToSave.isEmpty()) {
            return;
          }
          command = "save " + filePathToSave + " " + fileName;
          break;
        case "brighten":
          createBrightenCommand();
          break;
        case "greyscale":
          createGreyScaleCommand();
          break;
        case "rgb-split":
          int resultForSplit = helperForChoosingPaths(true);
          createCommandForRGBSplit(resultForSplit);
          break;
        case "rgb-combine":
          int resultForCombine = helperForChoosingPaths(false);
          createCommandForRGBCombine(resultForCombine);
          break;
        default:
          throw new UnsupportedOperationException();
      }
    } else {
      frame.displayErrorLoadFirstDialogBox();
      return;
    }
    if (command.isEmpty()) {
      return;
    }
    try {
      simulate(new StringReader(command));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private void createBrightenCommand() {
    JFrame brightenFrame = new JFrame("Button Dialog Box");
    String brighterBy = JOptionPane.showInputDialog(brightenFrame, "Brighter image by:");
    if (brighterBy == null || brighterBy.isEmpty()) {
      return;
    }
    command = "brighten " + brighterBy + " " + fileName + " brighten-image";
    fileName = "brighten-image";
  }

  private void createGreyScaleCommand() {
    String greyScaleComponent = "";
    JFrame greyScaleFrame = new JFrame("GreyScale Dialog Box");
    String[] options = {"red-component", "green-component", "blue-component",
        "luma-component", "value-component", "intensity-component"};
    int choice = JOptionPane.showOptionDialog(greyScaleFrame, "Select an option", "Grey Scale",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
        options[options.length - 1]);
    if (choice == JOptionPane.CLOSED_OPTION) {
      return;
    }
    greyScaleComponent = options[choice];
    command = "greyscale " + greyScaleComponent + " " + fileName + " " + fileName + "-"
        + greyScaleComponent;
    fileName = fileName + "-" + greyScaleComponent;
  }

  private void createCommandForRGBSplit(int result) {
    StringBuilder commands = new StringBuilder();
    if (result == JOptionPane.OK_OPTION && redFilePath[0] != null && greenFilePath[0] != null
        && blueFilePath[0] != null) {
      commands.append(
          "rgb-split " + fileName + " loadimage-red" + " loadimage-green" + " loadimage-blue");
      commands.append(" save " + redFilePath[0] + " loadimage-red");
      commands.append(" save " + greenFilePath[0] + " loadimage-green");
      commands.append(" save " + blueFilePath[0] + " loadimage-blue");
      command = commands.toString();
    } else {
      frame.displayErrorDialogBox();
    }
  }

  private void createCommandForRGBCombine(int result) {
    StringBuilder rgbCombineCommands = new StringBuilder();
    if (result == JOptionPane.OK_OPTION && redFilePath[0] != null && greenFilePath[0] != null
        && blueFilePath[0] != null) {
      rgbCombineCommands.append("load " + redFilePath[0] + " loadimage-red");
      rgbCombineCommands.append(" load " + greenFilePath[0] + " loadimage-green");
      rgbCombineCommands.append(" load " + blueFilePath[0] + " loadimage-blue ");
      rgbCombineCommands.append(
          "rgb-combine " + "rgb-combine" + " loadimage-red" + " loadimage-green"
              + " loadimage-blue");
      command = rgbCombineCommands.toString();
      fileName = "rgb-combine";
    } else {
      frame.displayErrorDialogBox();
    }
  }

  private int helperForChoosingPaths(boolean isFromRGBSplit) {
    JButton redSplitSaveButton = new JButton("Path for red image");
    JButton greenSplitSaveButton = new JButton("Path for green image");
    JButton blueSplitSaveButton = new JButton("Path for blue image");
    redSplitSaveButton.addActionListener(a -> {
      if (isFromRGBSplit) {
        redFilePath[0] = choosePathToSaveFile();
      } else {
        redFilePath[0] = chooseFilePathToOpen();
      }
    });
    greenSplitSaveButton.addActionListener(a -> {
      if (isFromRGBSplit) {
        greenFilePath[0] = choosePathToSaveFile();
      } else {
        greenFilePath[0] = chooseFilePathToOpen();
      }
    });
    blueSplitSaveButton.addActionListener(a -> {
      if (isFromRGBSplit) {
        blueFilePath[0] = choosePathToSaveFile();
      } else {
        blueFilePath[0] = chooseFilePathToOpen();
      }
    });
    JComponent[] buttons = new JComponent[]{redSplitSaveButton, greenSplitSaveButton,
        blueSplitSaveButton};
    return JOptionPane.showConfirmDialog(null, buttons, "Select the File paths",
        JOptionPane.PLAIN_MESSAGE);
  }

  private String chooseFilePathToOpen() {
    final JFileChooser fileChooser = new JFileChooser(".");
    int resultAction = fileChooser.showOpenDialog(null);
    if (resultAction == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();
      return f.getAbsolutePath();
    } else {
      JOptionPane.showMessageDialog(null, "File selection failed.");
    }
    return "";
  }

  private String choosePathToSaveFile() {
    final JFileChooser fileChooser = new JFileChooser(".");
    int resultValue = fileChooser.showSaveDialog(null);
    if (resultValue == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();
      return f.getAbsolutePath();
    }
    return "";
  }
}
