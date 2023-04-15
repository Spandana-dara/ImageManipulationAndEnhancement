package model;

import java.io.IOException;
import java.util.List;

/**
 * This interface represents all the operations that can be performed on an image.
 */
public interface ImageProcessingModel {

  /**
   * Insert the image which represents RGB components of an image in the imageMap.
   *
   * @param imageName the name of the image with which the loaded image is stored
   * @return an image object with integer array representing the RGB components of the loaded image
   */
  Image load(String imageName, Image image);

  /**
   * Create a greyscale image with the given component of the image with the given name, and refer
   * to it henceforth in the program by the given grey scaled image name.
   *
   * @param component       the component of the image with which the image is to be grey scaled
   * @param inputImageName  reference of the image that has to be grey scaled
   * @param greyScaledImage name of the grey scaled image with which it is further referred
   * @return an integer array representing the RGB components of the grey scaled image
   */

  Image greyScale(String component, String inputImageName, String greyScaledImage);

  /**
   * Flip an image horizontally to create a new image, referred to henceforth by the given
   * destination name.
   *
   * @param inputImageName   reference of the image that has to be horizontally flipped
   * @param flippedImageName name of the horizontally flipped image with which it is further
   *                         referred
   * @return an Image object containing integer array representing the RGB components of the
   *         horizontally flipped image
   * @throws IOException when the referenced image is not found
   */
  Image horizontalFlip(String inputImageName, String flippedImageName) throws IOException;

  /**
   * Flip an image vertically to create a new image, referred to henceforth by the given destination
   * name.
   *
   * @param inputImageName   reference of the image that has to be vertically flipped
   * @param flippedImageName name of the vertically flipped image with which it is further referred
   * @return an Image object containing integer array representing the RGB components of the
   *         vertically flipped image
   * @throws IOException when the referenced image is not found
   */
  Image verticalFlip(String inputImageName, String flippedImageName) throws IOException;

  /**
   * Brighten or Darken the image by the given increment to create a new image, referred to
   * henceforth by the given destination name.
   *
   * @param factor        value with which the image is to be brightened or darkened
   * @param imageName     reference of the image that has to be brightened or darkened
   * @param destImageName name of the brightened or darkened image with which it is further
   *                      referred
   * @return an Image object containing integer array representing the RGB components of the
   *         brightened image
   * @throws IOException when the referenced image is not found
   */
  Image brighten(int factor, String imageName, String destImageName) throws IOException;

  /**
   * Split the given image into three greyscale images containing its red, green and blue components
   * respectively and refer to them using the provided names.
   *
   * @param inputImageName name of the image whose RGB components that are to be separated
   * @param rgbSplitRed    name of the image with which the red component is further referred
   * @param rgbSplitGreen  name of the image with which the green component is further referred
   * @param rgbSplitBlue   name of the image with which the blue component is further referred
   * @return a List of Image objects containing integer array representing the individual RGB
   *         components of the given image
   */
  List<Image> rgbSplit(String inputImageName, String rgbSplitRed, String rgbSplitGreen,
      String rgbSplitBlue);

  /**
   * Combine the three greyscale images into a single image that gets its red, green and blue
   * components from the three images respectively and refer to them using the provided names.
   *
   * @param destinationImageName     the name with which the image is further referred
   * @param rgbCombineRedComponent   name of the image whose red component is used for combining
   * @param rgbCombineGreenComponent name of the image whose green component is used for combining
   * @param rgbCombineBlueComponent  name of the image whose blue component is used for combining
   * @return an Image object containing integer array consisting the RGB components of the combined
   *         image
   */
  Image rgbCombine(String destinationImageName, String rgbCombineRedComponent,
      String rgbCombineGreenComponent, String rgbCombineBlueComponent);

  /**
   * Returns the image object with the same name as imageName.
   *
   * @param imageName the name of the image which is to be saved
   */
  Image save(String imageName);

}
