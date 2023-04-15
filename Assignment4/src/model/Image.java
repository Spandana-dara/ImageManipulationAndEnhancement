package model;

import java.util.Arrays;

/**
 * This class represents an Image that is represented using an array consisting of the red, green
 * and blue components.
 */
public class Image {

  private final int[][][] imageArray;

  /**
   * Construct an Image object using an image array.
   *
   * @param imageArray an array consisting of the red, green and blue components of an image
   */
  public Image(int[][][] imageArray) {
    this.imageArray = imageArray;
  }

  /**
   * Returns the imageArray.
   *
   * @return the imageArray
   */
  public int[][][] getImageArray() {
    return imageArray;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Image)) {
      return false;
    }
    Image image = (Image) o;
    return Arrays.deepEquals(this.imageArray, image.imageArray);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(imageArray);
  }
}
