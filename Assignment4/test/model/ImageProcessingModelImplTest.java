package model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test class for testing the ImageProcessingModelImpl class.
 */
public class ImageProcessingModelImplTest {

  private Image image;

  @Before
  public void setUp() {
    int[][][] testImage = new int[][][]{{{255, 0, 0}, {0, 0, 255}, {255, 255, 0}},
        {{0, 255, 255}, {255, 0, 255}, {0, 0, 0}}};
    image = new Image(testImage);
  }

  @Test
  public void testLoad() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel imageProcessingModel = new ImageProcessingModelImpl(sb);
    Image loadedImage = imageProcessingModel.load("testImage", image);
    assertTrue(loadedImage.equals(image));
  }

  @Test
  public void testHorizontalFlip() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel imageProcessingModel = new ImageProcessingModelImpl(sb);
    imageProcessingModel.load("testImage", image);
    Image horizontalFlip = imageProcessingModel.horizontalFlip("testImage",
        "horizontalFlip_testImage");
    int[][][] testHorizontalFlipImage = {{{255, 255, 0}, {0, 0, 255}, {255, 0, 0}},
        {{0, 0, 0}, {255, 0, 255}, {0, 255, 255}}};
    assertTrue(horizontalFlip.equals(new Image(testHorizontalFlipImage)));
  }

  @Test
  public void testVerticalFlipOfImage() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel imageProcessingModel = new ImageProcessingModelImpl(sb);
    imageProcessingModel.load("testImage", image);
    Image verticalFlip = imageProcessingModel.verticalFlip("testImage",
        "verticalFlip_testImage");
    int[][][] testVerticalFlipImage = {{{0, 255, 255}, {255, 0, 255}, {0, 0, 0}},
        {{255, 0, 0}, {0, 0, 255}, {255, 255, 0}}};
    assertTrue(verticalFlip.equals(new Image(testVerticalFlipImage)));
  }

  @Test
  public void testSplitAndCombineOfRGBComponentsOfImage() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel imageProcessingModel = new ImageProcessingModelImpl(sb);

    imageProcessingModel.load("testImage", image);
    List<Image> splitImages = imageProcessingModel.rgbSplit("testImage", "image_red", "image_green",
        "image_blue");
    int[][][] redResult = {{{255, 255, 255}, {0, 0, 0}, {255, 255, 255}},
        {{0, 0, 0}, {255, 255, 255}, {0, 0, 0}}};
    assertTrue(splitImages.get(0).equals(new Image(redResult)));

    int[][][] greenResult = {{{0, 0, 0}, {0, 0, 0}, {255, 255, 255}},
        {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}};
    assertTrue(splitImages.get(1).equals(new Image(greenResult)));

    int[][][] blueResult = {{{0, 0, 0}, {255, 255, 255}, {0, 0, 0}},
        {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}}};
    assertTrue(splitImages.get(2).equals(new Image(blueResult)));

    Image combinedImage = imageProcessingModel.rgbCombine("image_combined", "image_red",
        "image_green", "image_blue");
    assertTrue(image.equals(combinedImage));
  }

  @Test
  public void testBrightenImage() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel imageProcessingModel = new ImageProcessingModelImpl(sb);
    imageProcessingModel.load("testImage", image);
    Image brightenedImage = imageProcessingModel
        .brighten(50, "testImage", "brighten_testImage");

    int[][][] brightenedResult = {{{255, 50, 50}, {50, 50, 255}, {255, 255, 50}},
        {{50, 255, 255}, {255, 50, 255}, {50, 50, 50}}};
    assertTrue(brightenedImage.equals(new Image(brightenedResult)));
  }

  @Test
  public void testBrightenImageByZeroFactor() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel imageProcessingModel = new ImageProcessingModelImpl(sb);
    Image loadedImage = imageProcessingModel.load("testImage", image);
    Image brightenedImage = imageProcessingModel
        .brighten(0, "testImage", "brighten_testImage");
    assertTrue(image.equals(brightenedImage));
  }

  @Test
  public void testGreyScalingImage() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel imageProcessingModel = new ImageProcessingModelImpl(sb);
    imageProcessingModel.load("testImage", image);

    Image redImage = imageProcessingModel.greyScale("red-component", "testImage", "redImage");
    int[][][] redResult = {{{255, 255, 255}, {0, 0, 0}, {255, 255, 255}},
        {{0, 0, 0}, {255, 255, 255}, {0, 0, 0}}};
    assertTrue(redImage.equals(new Image(redResult)));

    Image greenImage = imageProcessingModel.greyScale("green-component", "testImage", "greenImage");
    int[][][] greenResult = {{{0, 0, 0}, {0, 0, 0}, {255, 255, 255}},
        {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}};
    assertTrue(greenImage.equals(new Image(greenResult)));

    Image blueImage = imageProcessingModel.greyScale("blue-component", "testImage", "blueImage");
    int[][][] blueResult = {{{0, 0, 0}, {255, 255, 255}, {0, 0, 0}},
        {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}}};
    assertTrue(blueImage.equals(new Image(blueResult)));

    Image greyScaledWithIntensityImage = imageProcessingModel.greyScale("intensity-component",
        "testImage", "intensityImage");
    int[][][] greyScaledWithIntensityResult = {{{85, 85, 85}, {85, 85, 85}, {170, 170, 170}},
        {{170, 170, 170}, {170, 170, 170}, {0, 0, 0}}};
    assertTrue(greyScaledWithIntensityImage.equals(new Image(greyScaledWithIntensityResult)));

    Image greyScaledWithValueImage = imageProcessingModel.greyScale("value-component", "testImage",
        "valueImage");
    int[][][] greyScaledWithValueResult = {{{255, 255, 255}, {255, 255, 255}, {255, 255, 255}},
        {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}}};
    assertTrue(greyScaledWithValueImage.equals(new Image(greyScaledWithValueResult)));

    Image greyScaledWithLumaImage = imageProcessingModel.greyScale("luma-component", "testImage",
        "lumaImage");
    int[][][] greyScaledWithLumaResult = {{{54, 54, 54}, {18, 18, 18}, {236, 236, 236}},
        {{200, 200, 200}, {72, 72, 72}, {0, 0, 0}}};
    assertTrue(greyScaledWithLumaImage.equals(new Image(greyScaledWithLumaResult)));
  }

  @Test
  public void testIteratingCommands() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl(sb);
    Image loadedImage = model.load("testImage", image);
    model.brighten(50, "testImage", "testImageB");
    model.horizontalFlip("testImageB", "testImageBH");
    model.verticalFlip("testImageBH", "testImageBHV");
    model.rgbSplit("testImageBHV", "testImageBHVR", "testImageBHVG", "testImageBHVB");
    model.rgbCombine("testImageBHVC", "testImageBHVR", "testImageBHVG", "testImageBHVB");
    Image result = model.greyScale("value-component", "testImageBHVC", "testImageBHVCG");
    int[][][] expectedResult = {{{50, 50, 50}, {255, 255, 255}, {255, 255, 255}},
        {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}}};
    assertTrue(result.equals(new Image(expectedResult)));
  }

  @Test
  public void testSaveWhenFileIsNotPresentInTheProcessedMap() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl(sb);
    Image loadedImage = model.load("testImage", image);
    assertNull(model.save("test").getImageArray());
  }

  @Test
  public void testHorizontalFlipWhenFileIsNotLoadedOrNotProcessedInPreviousCommands()
      throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl(sb);
    model.load("image", image);

    assertNull(model.horizontalFlip("testImage", "horizontalImage"));

    model.brighten(50, "image", "brightenedImage");

    assertNull(model.horizontalFlip("brightenedTestImage", "brightenedHorizontalImage"));

    Image horizontalFlipFromLoad = model.horizontalFlip("image", "horizontalImage");
    int[][][] testHorizontalFlipImage = {{{255, 255, 0}, {0, 0, 255}, {255, 0, 0}},
        {{0, 0, 0}, {255, 0, 255}, {0, 255, 255}}};
    assertTrue(horizontalFlipFromLoad.equals(new Image(testHorizontalFlipImage)));

    Image horizontalFlipFromBrightenedImage = model.horizontalFlip("brightenedImage",
        "brightenedHorizontalImage");
    int[][][] brightenedResult = {{{255, 255, 50}, {50, 50, 255}, {255, 50, 50}},
        {{50, 50, 50}, {255, 50, 255}, {50, 255, 255}}};
    assertTrue(horizontalFlipFromBrightenedImage.equals(new Image(brightenedResult)));
  }

  @Test
  public void testVerticalFlipWhenFileIsNotLoadedOrNotProcessedInPreviousCommands()
      throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl(sb);
    model.load("image", image);

    assertNull(model.verticalFlip("testImage", "verticalImage"));

    model.brighten(50, "image", "brightenedImage");

    assertNull(model.verticalFlip("brightenedTestImage", "brightenedVerticalImage"));

    Image verticalFlipFromLoad = model.verticalFlip("image", "verticalImage");
    int[][][] testVerticalFlipImage = {{{0, 255, 255}, {255, 0, 255}, {0, 0, 0}},
        {{255, 0, 0}, {0, 0, 255}, {255, 255, 0}}};
    assertTrue(verticalFlipFromLoad.equals(new Image(testVerticalFlipImage)));

    Image verticalFlipFromBrightenedImage = model.verticalFlip("brightenedImage",
        "brightenedVerticalImage");
    int[][][] brightenedResult = {{{50, 255, 255}, {255, 50, 255}, {50, 50, 50}},
        {{255, 50, 50}, {50, 50, 255}, {255, 255, 50}}};
    assertTrue(verticalFlipFromBrightenedImage.equals(new Image(brightenedResult)));
  }

  @Test
  public void testRgbSplitWhenFileIsNotLoadedOrNotProcessedInPreviousCommands()
      throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl(sb);
    model.load("image", image);

    assertNull(model.rgbSplit("testImage", "redImage", "greenImage", "blueImage"));

    model.brighten(50, "image", "brightenedImage");

    assertNull(model.rgbSplit("brightenedTestImage", "redImage", "greenImage", "blueImage"));

    List<Image> splitImages = model.rgbSplit("image", "redImage", "greenImage", "blueImage");
    int[][][] redResult = {{{255, 255, 255}, {0, 0, 0}, {255, 255, 255}},
        {{0, 0, 0}, {255, 255, 255}, {0, 0, 0}}};
    assertTrue(splitImages.get(0).equals(new Image(redResult)));

    int[][][] greenResult = {{{0, 0, 0}, {0, 0, 0}, {255, 255, 255}},
        {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}};
    assertTrue(splitImages.get(1).equals(new Image(greenResult)));

    int[][][] blueResult = {{{0, 0, 0}, {255, 255, 255}, {0, 0, 0}},
        {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}}};
    assertTrue(splitImages.get(2).equals(new Image(blueResult)));
  }

  @Test
  public void testBrighteningAnImageByNegativeFactor() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel imageProcessingModel = new ImageProcessingModelImpl(sb);
    imageProcessingModel.load("testImage", image);
    Image brightenedImage = imageProcessingModel
        .brighten(-50, "testImage", "brighten_testImage");

    int[][][] brightenedResult = {{{205, 0, 0}, {0, 0, 205}, {205, 205, 0}},
        {{0, 205, 205}, {205, 0, 205}, {0, 0, 0}}};
    assertTrue(brightenedImage.equals(new Image(brightenedResult)));
  }

  @Test
  public void testLoadingMultipleTimesToTheModel() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel imageProcessingModel = new ImageProcessingModelImpl(sb);
    imageProcessingModel.load("testImage", image);
    Image brightenedImage = imageProcessingModel
        .brighten(-50, "testImage", "brighten_testImage");

    int[][][] brightenedResult = {{{205, 0, 0}, {0, 0, 205}, {205, 205, 0}},
        {{0, 205, 205}, {205, 0, 205}, {0, 0, 0}}};
    assertTrue(brightenedImage.equals(new Image(brightenedResult)));

    Image brightenedImageFromLoad = imageProcessingModel.load("brighten_testImage",
        new Image(brightenedResult));

    int[][][] newBrightenedResult = {{{255, 50, 50}, {50, 50, 255}, {255, 255, 50}},
        {{50, 255, 255}, {255, 50, 255}, {50, 50, 50}}};

    Image secondBrightenedImage = imageProcessingModel.brighten(50, "brighten_testImage",
        "new_brighten_testImage");
    assertTrue(secondBrightenedImage.equals(new Image(newBrightenedResult)));
  }

  @Test
  public void testModelWithCommandsAndFileAsInput() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl(sb);
    Image loadedImage = model.load("testImage", image);
    model.save("testImage");
    Image brightenImage = model.brighten(50, "testImage", "testImage-brighter");
    model.save("testImage-brighter");
    Image greyScaleValueImage = model.greyScale("value-component", "testImage",
        "testImage-greyscale-value");
    model.save("testImage-greyscale-value");
    Image greyScaleLumaImage = model.greyScale("luma-component", "testImage",
        "testImage-greyscale-luma");
    model.save("testImage-greyscale-luma");
    int[][][] loadResult = {{{255, 0, 0}, {0, 0, 255}, {255, 255, 0}},
        {{0, 255, 255}, {255, 0, 255}, {0, 0, 0}}};
    int[][][] brightenedResult = {{{255, 50, 50}, {50, 50, 255}, {255, 255, 50}},
        {{50, 255, 255}, {255, 50, 255}, {50, 50, 50}}};
    int[][][] greyScaledWithValueResult = {{{255, 255, 255}, {255, 255, 255}, {255, 255, 255}},
        {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}}};
    int[][][] greyScaledWithLumaResult = {{{54, 54, 54}, {18, 18, 18}, {236, 236, 236}},
        {{200, 200, 200}, {72, 72, 72}, {0, 0, 0}}};

    assertTrue(loadedImage.equals(new Image(loadResult)));
    assertTrue(brightenImage.equals(new Image(brightenedResult)));
    assertTrue(greyScaleValueImage.equals(new Image(greyScaledWithValueResult)));
    assertTrue(greyScaleLumaImage.equals(new Image(greyScaledWithLumaResult)));
  }

  @Test
  public void testModelForColorTransformationSepia() {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    model.load("testImage", image);
    Image sepiaImage = model.sepia("testImage", "testImageSepia");
    int[][][] result = {{{100, 88, 69}, {48, 42, 33}, {255, 255, 205}},
        {{244, 216, 169}, {148, 130, 102}, {0, 0, 0}}};
    assertEquals(sepiaImage, new Image(result));
  }

  @Test
  public void testModelForDither() {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    model.load("testImage", image);
    Image ditherImage = model.dither("testImage", "testImageSepia");
    int[][][] result = {{{0, 0, 0}, {0, 0, 0}, {255, 255, 255}},
        {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}};
    assertEquals(ditherImage, new Image(result));
  }

  @Test
  public void testSharpenSmallPPM() {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature imageProcessingModel = new ImageProcessingModelNewFeaturesImpl(
        sb);
    imageProcessingModel.load("testImage", image);
    Image sharpen = imageProcessingModel.sharpen("testImage", "sharpenSmallPPM");
    int[][][] sharpedImage = {{{255, 32, 189}, {189, 126, 255}, {255, 224, 95}},
        {{95, 224, 255}, {255, 126, 255}, {95, 32, 95}}};
    assertTrue(sharpen.equals(new Image(sharpedImage)));
  }

  @Test
  public void testBlurSmallPPM() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature imageProcessingModel = new ImageProcessingModelNewFeaturesImpl(
        sb);
    imageProcessingModel.load("testImage", image);
    Image blur = imageProcessingModel.blur("testImage", "blurredSmallPPM");
    int[][][] blurredImage = {{{78, 31, 77}, {93, 46, 109}, {78, 63, 46}},
        {{62, 63, 109}, {93, 46, 125}, {62, 31, 46}}};
    assertTrue(blur.equals(new Image(blurredImage)));
  }

}

