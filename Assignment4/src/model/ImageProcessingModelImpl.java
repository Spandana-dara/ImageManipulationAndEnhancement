package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * This class represents a ImageProcessingModel which can perform various operations on the given
 * image.
 */
public class ImageProcessingModelImpl implements ImageProcessingModel {

  protected final Map<String, int[][][]> imageMap;

  private final StringBuilder out;

  /**
   * Constructs the ImageProcessingModel with respective string builder to attach the errors.
   *
   * @param out StringBuilder class to save the exceptions caught
   */
  public ImageProcessingModelImpl(StringBuilder out) {
    this.out = out;
    this.imageMap = new HashMap<>();
  }

  @Override
  public Image load(String imageName, Image image) {
    imageMap.put(imageName, image.getImageArray());
    return image;
  }

  @Override
  public Image save(String imageName) {
    return new Image(checkImageMapContainsImage(imageMap, imageName));
  }

  @Override
  public Image greyScale(String component, String inputImageName, String greyScaledImage) {
    if (checkImageMapContainsImage(imageMap, inputImageName) == null) {
      return null;
    }
    switch (component) {
      case "red-component":
        setComponent(0, inputImageName, greyScaledImage);
        break;
      case "green-component":
        setComponent(1, inputImageName, greyScaledImage);
        break;
      case "blue-component":
        setComponent(2, inputImageName, greyScaledImage);
        break;
      case "value-component":
        valueComponent(inputImageName, greyScaledImage);
        break;
      case "luma-component":
        newGreyscaleLuma(inputImageName, greyScaledImage);
        break;
      case "intensity-component":
        intensityComponent(inputImageName, greyScaledImage);
        break;
      default:
        out.append("Select a valid component to greyscale the image.");
        return null;
    }
    return new Image(imageMap.get(greyScaledImage));
  }


  private void intensityComponent(String inputImageName, String greyScaledImage) {
    int[][][] imageArray = getArray(imageMap.get(inputImageName));
    int[][][] greyScaleImageArray = new int[imageArray.length][imageArray[0].length][3];
    for (int i = 0; i < greyScaleImageArray.length; i++) {
      for (int j = 0; j < greyScaleImageArray[0].length; j++) {
        int temp = (imageArray[i][j][0] + imageArray[i][j][1] + imageArray[i][j][2]) / 3;
        greyScaleImageArray[i][j][0] = temp;
        greyScaleImageArray[i][j][1] = temp;
        greyScaleImageArray[i][j][2] = temp;
      }
    }
    imageMap.put(greyScaledImage, greyScaleImageArray);
  }

  private void valueComponent(String inputImageName, String greyScaledImage) {
    int[][][] imageArray = getArray(imageMap.get(inputImageName));
    int[][][] greyScaleImageArray = new int[imageArray.length][imageArray[0].length][3];
    for (int i = 0; i < greyScaleImageArray.length; i++) {
      for (int j = 0; j < greyScaleImageArray[0].length; j++) {
        int temp = Math.max(Math.max(imageArray[i][j][0], imageArray[i][j][1]),
            imageArray[i][j][2]);
        greyScaleImageArray[i][j][0] = temp;
        greyScaleImageArray[i][j][1] = temp;
        greyScaleImageArray[i][j][2] = temp;
      }
    }
    imageMap.put(greyScaledImage, greyScaleImageArray);
  }

  private void setComponent(int component, String inputImageName, String greyScaledImage) {
    int[][][] imageArray = getArray(imageMap.get(inputImageName));
    int[][][] greyScaleImageArray = new int[imageArray.length][imageArray[0].length][3];
    for (int i = 0; i < greyScaleImageArray.length; i++) {
      for (int j = 0; j < greyScaleImageArray[0].length; j++) {
        greyScaleImageArray[i][j][0] = imageArray[i][j][component];
        greyScaleImageArray[i][j][1] = imageArray[i][j][component];
        greyScaleImageArray[i][j][2] = imageArray[i][j][component];
      }
    }
    imageMap.put(greyScaledImage, greyScaleImageArray);
  }

  @Override
  public Image verticalFlip(String inputImageName, String flippedImageName) {
    if (checkImageMapContainsImage(imageMap, inputImageName) == null) {
      return null;
    }
    int[][][] verticallyFlippedImageArray = getArray(imageMap.get(inputImageName));
    for (int i = 0; i < verticallyFlippedImageArray.length / 2; i++) {
      for (int j = 0; j < verticallyFlippedImageArray[0].length; j++) {

        int[] temp = verticallyFlippedImageArray[i][j];
        verticallyFlippedImageArray[i][j] = verticallyFlippedImageArray[
            verticallyFlippedImageArray.length - 1 - i][j];
        verticallyFlippedImageArray[verticallyFlippedImageArray.length - 1 - i][j] = temp;
      }
    }
    imageMap.put(flippedImageName, verticallyFlippedImageArray);
    return new Image(verticallyFlippedImageArray);
  }

  @Override
  public Image horizontalFlip(String inputImageName, String flippedImageName) {
    if (checkImageMapContainsImage(imageMap, inputImageName) == null) {
      return null;
    }
    int[][][] horizontallyFlippedImage = getArray(imageMap.get(inputImageName));
    for (int i = 0; i < horizontallyFlippedImage.length; i++) {
      for (int j = 0; j < horizontallyFlippedImage[0].length / 2; j++) {
        int[] temp = horizontallyFlippedImage[i][j];
        horizontallyFlippedImage[i][j] = horizontallyFlippedImage[i][
            horizontallyFlippedImage[0].length - 1 - j];
        horizontallyFlippedImage[i][horizontallyFlippedImage[0].length - 1 - j] = temp;
      }
    }
    imageMap.put(flippedImageName, horizontallyFlippedImage);
    return new Image(horizontallyFlippedImage);
  }

  @Override
  public Image brighten(int factor, String inputImageName, String destImageName) {
    if (checkImageMapContainsImage(imageMap, inputImageName) == null) {
      return null;
    }
    int[][][] brightenedImageArray = getArray(imageMap.get(inputImageName));
    IntStream.range(0, brightenedImageArray.length).parallel().forEach(i -> {
      for (int j = 0; j < brightenedImageArray[i].length; j++) {
        for (int k = 0; k < brightenedImageArray[i][j].length; k++) {
          brightenedImageArray[i][j][k] += factor;
          if (brightenedImageArray[i][j][k] > 255) {
            brightenedImageArray[i][j][k] = 255;
          } else if (brightenedImageArray[i][j][k] < 0) {
            brightenedImageArray[i][j][k] = 0;
          }
        }
      }
    });
    imageMap.put(destImageName, brightenedImageArray);
    return new Image(brightenedImageArray);
  }

  @Override
  public List<Image> rgbSplit(String inputImageName, String rgbSplitRed, String rgbSplitGreen,
      String rgbSplitBlue) {
    if (checkImageMapContainsImage(imageMap, inputImageName) == null) {
      return null;
    }
    int[][][] imageArray = getArray(imageMap.get(inputImageName));
    int[][][] redImageArray = new int[imageArray.length][imageArray[0].length][3];
    int[][][] greenImageArray = new int[imageArray.length][imageArray[0].length][3];
    int[][][] blueImageArray = new int[imageArray.length][imageArray[0].length][3];
    for (int i = 0; i < imageArray.length; i++) {
      for (int j = 0; j < imageArray[0].length; j++) {
        int r = imageArray[i][j][0];
        int g = imageArray[i][j][1];
        int b = imageArray[i][j][2];
        for (int k = 0; k < 3; k++) {
          redImageArray[i][j][k] = r;
        }
        for (int k = 0; k < 3; k++) {
          greenImageArray[i][j][k] = g;
        }
        for (int k = 0; k < 3; k++) {
          blueImageArray[i][j][k] = b;
        }
      }
    }
    imageMap.put(rgbSplitRed, redImageArray);
    imageMap.put(rgbSplitGreen, greenImageArray);
    imageMap.put(rgbSplitBlue, blueImageArray);
    List<Image> result = new ArrayList<>();
    result.add(new Image(redImageArray));
    result.add(new Image(greenImageArray));
    result.add(new Image(blueImageArray));
    return result;
  }

  @Override
  public Image rgbCombine(String destinationImageName, String rgbCombineRedComponent,
      String rgbCombineGreenComponent, String rgbCombineBlueComponent) {
    for (String s : Arrays.asList(rgbCombineRedComponent, rgbCombineGreenComponent,
        rgbCombineBlueComponent)) {
      if (checkImageMapContainsImage(imageMap, s) == null) {
        return null;
      }
    }
    int[][][] redImageArray = getArray(imageMap.get(rgbCombineRedComponent));
    int[][][] greenImageArray = getArray(imageMap.get(rgbCombineGreenComponent));
    int[][][] blueImageArray = getArray(imageMap.get(rgbCombineBlueComponent));
    int[][][] combinedImageArray = new int[redImageArray.length][redImageArray[0].length][3];
    for (int i = 0; i < combinedImageArray.length; i++) {
      for (int j = 0; j < combinedImageArray[0].length; j++) {
        combinedImageArray[i][j][0] = redImageArray[i][j][0];
        combinedImageArray[i][j][1] = greenImageArray[i][j][0];
        combinedImageArray[i][j][2] = blueImageArray[i][j][0];
      }
    }
    imageMap.put(destinationImageName, combinedImageArray);
    return new Image(combinedImageArray);
  }


  protected void newGreyscaleLuma(String imageName, String destImageName) {
    int[][][] imageArray = getArray(imageMap.get(imageName));
    double[][] factors = {{0.2126, 0.7152, 0.0722}, {0.2126, 0.7152, 0.0722},
        {0.2126, 0.7152, 0.0722}};
    colorTransformation(imageArray, factors);
    imageMap.put(destImageName, imageArray);
  }

  protected void colorTransformation(int[][][] imageArray, double[][] factors) {
    for (int i = 0; i < imageArray.length; i++) {
      for (int j = 0; j < imageArray[0].length; j++) {
        int[] pixelArray = new int[3];
        for (int b = 0; b < 3; b++) {
          pixelArray[b] = 0;
          for (int c = 0; c < 3; c++) {
            pixelArray[b] += imageArray[i][j][c] * factors[b][c];
          }
        }
        for (int k = 0; k < 3; k++) {
          imageArray[i][j][k] = (pixelArray[k] > 255) ? 255 : (Math.max(pixelArray[k], 0));
        }
      }
    }
  }

  protected int[][][] checkImageMapContainsImage(Map<String, int[][][]> imageMap,
      String imageName) {
    if (!imageMap.containsKey(imageName)) {
      out.append("Referred image is not found.");
      return null;
    }
    return imageMap.get(imageName);
  }

  protected int[][][] getArray(int[][][] oldArray) {
    int[][][] newArray = new int[oldArray.length][oldArray[0].length][oldArray[0][0].length];
    for (int i = 0; i < oldArray.length; i++) {
      for (int j = 0; j < oldArray[0].length; j++) {
        for (int k = 0; k < oldArray[0][0].length; k++) {
          newArray[i][j][k] = oldArray[i][j][k];
        }
      }
    }
    return newArray;
  }
}