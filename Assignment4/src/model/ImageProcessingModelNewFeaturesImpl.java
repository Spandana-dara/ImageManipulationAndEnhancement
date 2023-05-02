package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

  @Override
  public Image mosaic(int numOfSeeds, String imageName, String destImageName) {
    if (checkImageMapContainsImage(imageMap, imageName) == null) {
      return null;
    }
    int[][][] image = getArray(imageMap.get(imageName));
    int height = image.length;
    int width = image[0].length;

    List<int[]> seeds = getListOfSeeds(numOfSeeds, height, width);

    int[][] seedIndices = assignPixels(height, width, numOfSeeds, seeds);

    int[][] averages = calculateAverages(numOfSeeds, image, seedIndices);
    // Replace each pixel with the average color of its cluster
    int[][][] result = new int[height][width][3];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int index = seedIndices[y][x];
        result[y][x][0] = averages[index][0];
        result[y][x][1] = averages[index][1];
        result[y][x][2] = averages[index][2];
      }
    }
    imageMap.put(destImageName, result);
    return new Image(result);
  }

  private int[][] assignPixels(int height, int width, int numOfSeeds, List<int[]> seeds) {
    // Assign each pixel to its nearest seed
    int[][] seedIndices = new int[height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int minDistance = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < numOfSeeds; i++) {
          int[] seed = seeds.get(i);
          int dx = x - seed[0];
          int dy = y - seed[1];
          int distance = dx * dx + dy * dy; // squared Euclidean distance
          if (distance < minDistance) {
            minDistance = distance;
            minIndex = i;
          }
        }
        seedIndices[y][x] = minIndex;
      }
    }
    return seedIndices;
  }

  private List<int[]> getListOfSeeds(int numOfSeeds, int height, int width) {
    List<int[]> seeds = new ArrayList<>();
    // Generate random seeds
    Random rand = new Random();
    for (int i = 0; i < numOfSeeds; i++) {
      int x = rand.nextInt(width);
      int y = rand.nextInt(height);
      seeds.add(new int[]{x, y});
    }
    return seeds;
  }

  private int[][] calculateAverages(int numOfSeeds, int[][][] image, int[][] seedIndices) {
    // Compute average color for each seed's cluster
    int[][] sums = new int[numOfSeeds][3];
    int[] counts = new int[numOfSeeds];
    for (int y = 0; y < image.length; y++) {
      for (int x = 0; x < image[0].length; x++) {
        int index = seedIndices[y][x];
        int[] pixel = image[y][x];
        sums[index][0] += pixel[0];
        sums[index][1] += pixel[1];
        sums[index][2] += pixel[2];
        counts[index]++;
      }
    }
    int[][] averages = new int[numOfSeeds][3];
    for (int i = 0; i < numOfSeeds; i++) {
      int count = counts[i];
      averages[i][0] = count == 0 ? 0 : sums[i][0] / count;
      averages[i][1] = count == 0 ? 0 : sums[i][1] / count;
      averages[i][2] = count == 0 ? 0 : sums[i][2] / count;
    }
    return averages;
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
