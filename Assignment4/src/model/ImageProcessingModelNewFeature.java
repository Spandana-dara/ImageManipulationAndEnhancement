package model;

/**
 * This interface represents all the new operations that can be performed on an image.
 */
public interface ImageProcessingModelNewFeature extends ImageProcessingModel {

  /**
   * Blur an image and create a new image, referred to henceforth by the given destination name.
   *
   * @param imageName     reference of the image that has to be blurred
   * @param destImageName name of the blurred image with which it is further referred
   * @return an Image object containing an array representing the blurred image
   */
  Image blur(String imageName, String destImageName);

  /**
   * Sharpen an image and create a new image, referred to henceforth by the given destination name.
   *
   * @param imageName     reference of the image that has to be sharpened
   * @param destImageName name of the sharpened image with which it is further referred
   * @return an Image object containing an array representing the sharpened image
   */
  Image sharpen(String imageName, String destImageName);

  /**
   * Create a new sepia version of the given image , referred to henceforth by the given destination
   * name.
   *
   * @param imageName     reference of the image that has to be converted to sepia
   * @param destImageName name of the resultant image with which it is further referred
   * @return an Image object containing an array representing the new sepia image
   */
  Image sepia(String imageName, String destImageName);

  /**
   * Dither an image and create a new image, referred to henceforth by the given destination name.
   *
   * @param imageName     reference of the image that has to be dithered
   * @param destImageName name of the dithered image with which it is further referred
   * @return an Image object containing an array representing the dithered image
   */
  Image dither(String imageName, String destImageName);
}
