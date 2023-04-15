package controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;


/**
 * This class contains utility method to read a PPM image from file and simply print its contents.
 * Feel free to change this method as required.
 */
public class ImageUtil {

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param filename the path of the file.
   */
  public static int[][][] readPPM(String filename) throws Exception {
    Scanner sc;
    sc = new Scanner(new FileInputStream(filename));
    StringBuilder builder = new StringBuilder();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }
    sc = new Scanner(builder.toString());
    String token = sc.next();
    if (!token.equals("P3")) {
      throw new Exception("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();
    int[][][] rgb = new int[height][width][3];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        rgb[i][j][0] = r;
        rgb[i][j][1] = g;
        rgb[i][j][2] = b;
      }
    }
    return rgb;
  }

  /**
   * Save the image in the ppm format.
   *
   * @param imageArray the image array that has to be saved as a ppm
   * @param imagePath  the path where the ppm file is to be saved
   * @throws IOException when the path is not found
   */
  public static void generatePPMFile(int[][][] imageArray, String imagePath) throws IOException {
    if (imageArray.length == 0) {
      return;
    }
    File myObj = new File(imagePath);
    myObj.createNewFile();
    FileWriter myWriter = new FileWriter(imagePath);
    myWriter.write("P3\n");
    myWriter.write(imageArray[0].length + " ");
    myWriter.write(imageArray.length + "\n");
    myWriter.write(255 + "\n");
    for (int i = 0; i < imageArray.length; i++) {
      for (int j = 0; j < imageArray[0].length; j++) {
        int r = imageArray[i][j][0];
        int g = imageArray[i][j][1];
        int b = imageArray[i][j][2];
        myWriter.write(r + " " + g + " " + b + " ");
      }
    }
    myWriter.close();
  }

  /**
   * Read the image file other than ppm format.
   *
   * @param fileName the path and image name that is to be loaded
   * @return an image array containing the RGB components of each pixel of the image
   * @throws Exception when the image is not present or the specified path is incorrect
   */
  public static int[][][] readOtherImageFormats(String fileName) throws Exception {
    BufferedImage image = ImageIO.read(new File(fileName));
    int[][][] img = new int[image.getHeight()][image.getWidth()][3];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        int pixel = image.getRGB(j, i);
        Color color = new Color(pixel, false);
        img[i][j][0] = color.getRed();
        img[i][j][1] = color.getGreen();
        img[i][j][2] = color.getBlue();
      }
    }
    return img;
  }

  /**
   * Save the image in specified format other than ppm.
   *
   * @param imageArray the image array representing the image that has to be saved
   * @param fileFormat the format of the image that it needs to be stored in
   * @param fileName   the path where the image has to be saved
   * @throws IOException when the provided path is incorrect
   */
  public static void saveImage(int[][][] imageArray, String fileFormat, String fileName)
      throws IOException {
    int height = imageArray.length;
    int width = imageArray[0].length;
    BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        int rgb = (imageArray[x][y][0] << 16) | (imageArray[x][y][1] << 8) | imageArray[x][y][2];
        flippedImage.setRGB(y, x, rgb);
      }
    }
    File outputFile = new File(fileName);
    ImageIO.write(flippedImage, fileFormat, outputFile);
  }
}



