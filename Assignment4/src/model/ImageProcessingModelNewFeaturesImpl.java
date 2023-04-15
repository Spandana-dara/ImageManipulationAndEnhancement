package model;

/**
 * This class represents the model with the implementations of the new features.
 */

public class ImageProcessingModelNewFeaturesImpl extends ImageProcessingModelImpl implements
    ImageProcessingModelNewFeature {

  /**
   * Constructs the ImageProcessingModelNewFeaturesImpl with respective string builder to attach the
   * errors.
   *
   * @param out StringBuilder class to save the exceptions caught
   */
  public ImageProcessingModelNewFeaturesImpl(StringBuilder out) {
    super(out);
  }

  @Override
  public Image blur(String inputImageName, String blurredImageName) {
    if (checkImageMapContainsImage(imageMap, inputImageName) == null) {
      return null;
    }
    int[][][] image = getArray(imageMap.get(inputImageName));
    float[][] kernel = {
        {1 / 16f, 1 / 8f, 1 / 16f},
        {1 / 8f, 1 / 4f, 1 / 8f},
        {1 / 16f, 1 / 8f, 1 / 16f}
    };
    int[][][] blurredImage = filter(image, kernel);
    imageMap.put(blurredImageName, blurredImage);
    return new Image(blurredImage);
  }


  @Override
  public Image sharpen(String inputImageName, String shapedImageName) {
    if (checkImageMapContainsImage(imageMap, inputImageName) == null) {
      return null;
    }
    int[][][] image = getArray(imageMap.get(inputImageName));

    float[][] kernel = {
        {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f},
        {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
        {-1 / 8f, 1 / 4f, 1, 1 / 4f, -1 / 8f},
        {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
        {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f}
    };

    int[][][] sharpedImage = filter(image, kernel);
    imageMap.put(shapedImageName, sharpedImage);
    return new Image(sharpedImage);
  }

  @Override
  public Image sepia(String imageName, String destImageName) {
    if (checkImageMapContainsImage(imageMap, imageName) == null) {
      return null;
    }
    int[][][] imageArray = getArray(imageMap.get(imageName));
    double[][] factors = {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}};
    colorTransformation(imageArray, factors);
    imageMap.put(destImageName, imageArray);
    return new Image(imageArray);
  }

  @Override
  public Image dither(String imageName, String destImageName) {
    if (checkImageMapContainsImage(imageMap, imageName) == null) {
      return null;
    }
    newGreyscaleLuma(imageName, destImageName);
    int[][][] imageArray = getArray(imageMap.get(destImageName));
    for (int i = 0; i < imageArray.length; i++) {
      for (int j = 0; j < imageArray[0].length; j++) {
        int oldColor = imageArray[i][j][0];
        int newColor = 255 - oldColor < oldColor ? 255 : 0;
        int error = oldColor - newColor;
        for (int k = 0; k < 3; k++) {
          imageArray[i][j][k] = newColor;
          if (j + 1 < imageArray[0].length) {
            imageArray[i][j + 1][k] += (double) (7 / 16) * error;
            if (i + 1 < imageArray.length) {
              imageArray[i + 1][j + 1][k] += (double) (1 / 16) * error;
            }
          }
          if (i + 1 < imageArray.length) {
            imageArray[i + 1][j][k] += (double) (5 / 16) * error;
            if (j - 1 >= 0) {
              imageArray[i + 1][j - 1][k] += (double) (3 / 16) * error;
            }
          }
        }
      }
    }
    imageMap.put(destImageName, imageArray);
    return new Image(imageArray);
  }

  private int[][][] filter(int[][][] image, float[][] kernel) {
    int[][][] filteredImage = new int[image.length][image[0].length][3];
    for (int x = 0; x < image.length; x++) {
      for (int y = 0; y < image[0].length; y++) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int index = kernel.length / 2;
        for (int kx = (-1) * index; kx <= index; kx++) {
          for (int ky = (-1) * index; ky <= index; ky++) {
            int pixelX = x + kx;
            int pixelY = y + ky;
            if (pixelX >= 0 && pixelX < image.length && pixelY >= 0 && pixelY < image[0].length) {
              int[] pixel = image[pixelX][pixelY];
              double weight = kernel[kx + index][ky + index];
              redSum += (int) (pixel[0] * weight);
              greenSum += (int) (pixel[1] * weight);
              blueSum += (int) (pixel[2] * weight);
            }
          }
        }
        filteredImage[x][y][0] = Math.min(Math.max(redSum, 0), 255);
        filteredImage[x][y][1] = Math.min(Math.max(greenSum, 0), 255);
        filteredImage[x][y][2] = Math.min(Math.max(blueSum, 0), 255);
      }
    }
    return filteredImage;
  }
}
