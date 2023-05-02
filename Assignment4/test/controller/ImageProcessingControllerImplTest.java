package controller;

import java.io.File;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import model.Image;
import model.ImageProcessingModelNewFeature;
import model.ImageProcessingModelNewFeaturesImpl;
import view.ImageProcessingView;
import view.ImageProcessingViewImpl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test class to test the ImageProcessingController class.
 */
public class ImageProcessingControllerImplTest {

  @Test
  public void testControllerCorrectnessOfPassingLoadCommand() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String input = "load res/building.ppm building";
    Readable in = new StringReader(input);
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(in);
    assertEquals("load building" + "\n", sb.toString());
  }

  @Test
  public void testForCommandsInFileWithCorrectCommands() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new FileReader("res/mockModelTest.txt"));
    StringBuilder result = new StringBuilder();
    result.append("load building\n");
    result.append("brighten 50 building building-brighter\n");
    result.append("save building-brighter\n");
    result.append("vertical-flip building building-vertical\n");
    result.append("save building-vertical\n");
    result.append("horizontal-flip building building-horizontal\n");
    result.append("save building-horizontal\n");
    result.append("horizontal-flip building-vertical building-vertical-horizontal\n");
    result.append("save building-vertical-horizontal\n");

    assertEquals(result.toString(), sb.toString());
  }

  @Test
  public void testForCommandsInStringBufferSimilarToCommandline() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load res/building.ppm building\n"
            + "vertical-flip building building-vertical\n"
            + "save building-vertical.ppm building-vertical\n";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
    String resultToModel = "load building\n"
            + "vertical-flip building building-vertical\n"
            + "save building-vertical\n";
    assertEquals(resultToModel, sb.toString());
  }

  @Test
  public void testChangingTheLoadedImageToAnotherImage() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load res/building.ppm building\n"
            + "vertical-flip building building-vertical\n"
            + "load res/testImage.ppm building\n"
            + "horizontal-flip building building-horizontal\n"
            + "save building-vertical.ppm building-vertical\n"
            + "save building-horizontal.ppm building-horizontal\n";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
    String resultantCommands = "load building\n"
            + "vertical-flip building building-vertical\n"
            + "load building\n"
            + "horizontal-flip building building-horizontal\n"
            + "save building-vertical\n"
            + "save building-horizontal\n";
    assertEquals(resultantCommands, sb.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForCommandsWithNoKeyword() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load building.ppm building "
            + "building building-vertical "
            + "save building-vertical.ppm building-vertical";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForCommandsWithWrongCommand() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load building.ppm building "
            + "vertical building building-vertical "
            + "save building-vertical.ppm building-vertical";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForWrongCommand() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load building.ppm "
            + "vertical-flip building building-vertical "
            + "save building-vertical.ppm building-vertical";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForWrongCommandForTheLastCommand() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load building.ppm building "
            + "vertical-flip building building-vertical "
            + "save";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(
            new StringReader(commands));
  }

  @Test
  public void testForCommandsWithComments() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load res/building.ppm building\n"
            + "#flip building vertically\n"
            + "vertical-flip building building-vertical\n"
            + "#save the flipped image\n"
            + "save building-vertical.ppm building-vertical\n";

    String commandsOutput = "load building\n"
            + "vertical-flip building building-vertical\n"
            + "save building-vertical\n";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
    assertEquals(commandsOutput, sb.toString());
  }

  @Test
  public void testForCommandsMosaic() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load res/building.ppm building\n"
            + "mosaic 1000 building building-mosaic\n"
            + "save building-mosaic.ppm building-mosaic\n";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
    String resultToModel = "load building\n"
            + "mosaic 1000 building building-mosaic\n"
            + "save building-mosaic\n";
    assertEquals(resultToModel, sb.toString());
  }

  @Test
  public void testControllerAndModelForAllValidCommands() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    StringBuilder commands = new StringBuilder();
    commands.append("load res/building.ppm building\n");
    commands.append("brighten 50 building building-brighter\n");
    commands.append("save res/building-brighter.ppm building-brighter\n");
    commands.append("vertical-flip building building-vertical\n");
    commands.append("save res/building-vertical.ppm building-vertical\n");
    commands.append("horizontal-flip building building-horizontal\n");
    commands.append("save res/building-horizontal.ppm building-horizontal\n");
    commands.append("horizontal-flip building-vertical building-vertical-horizontal\n");
    commands.append("save res/building-vertical-horizontal.ppm building-vertical-horizontal\n");
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands.toString()));
    assertEquals("", sb.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerAndModelWhenCommandIsIncorrectDueToMissingOrWrongFile()
          throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    StringBuilder commands = new StringBuilder();
    commands.append("load building.ppm building\n");
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands.toString()));
  }

  @Test
  public void testControllerAndModelWhenCommandIsIncorrectDueToWrongReference()
          throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    StringBuilder commands = new StringBuilder();
    commands.append("load res/building.ppm building\n");
    commands.append("brighten 50 building building-brighter\n");
    commands.append("save res/building-brighter.ppm building-brighter\n");
    commands.append("vertical-flip buildingImage building-vertical\n");
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    try {
      controller.simulate(new StringReader(commands.toString()));
    } catch (IllegalArgumentException e) {
      assertEquals("Referred image is not found.", sb.toString());
    }
  }

  @Test
  public void testControllerAndModelWhenCommandIsIncorrectDueToMissingPathWhileSaving()
          throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    StringBuilder commands = new StringBuilder();
    commands.append("load res/building.ppm building\n");
    commands.append("save res/building_save.ppm building\n");
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    try {
      controller.simulate(new StringReader(commands.toString()));
    } catch (IllegalArgumentException e) {
      assertEquals("File/Path not found", sb.toString());
    }
  }

  @Test
  public void testControllerAndModelWhenCommandIsIncorrectDueToMissingReferenceWhileSaving()
          throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    StringBuilder commands = new StringBuilder();
    commands.append("save res/building.ppm building\n");
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    try {
      controller.simulate(new StringReader(commands.toString()));
    } catch (IllegalArgumentException e) {
      assertEquals("Referred image is not found.", sb.toString());
    }
  }

  @Test
  public void testControllerAndModelWithMultipleLoads()
          throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    StringBuilder commands = new StringBuilder();
    commands.append("load res/building.ppm building\n");
    commands.append("brighten 50 building building-brighter\n");
    commands.append("save res/building-brighter.ppm building-brighter\n");
    commands.append("vertical-flip building building-vertical\n");
    commands.append("save res/building-vertical.ppm building-vertical\n");
    commands.append("load res/testImage.ppm building\n");
    commands.append("horizontal-flip building building-horizontal\n");
    commands.append("save res/building-horizontal.ppm building-horizontal\n");
    commands.append("horizontal-flip building-vertical building-vertical-horizontal\n");
    commands.append("save res/building-vertical-horizontal.ppm building-vertical-horizontal\n");
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands.toString()));
    assertEquals("", sb.toString());
  }

  @Test
  public void testController() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    StringBuilder commands = new StringBuilder();
    commands.append("load res/testImage.ppm testImage ");
    commands.append("brighten 50 testImage brighten_testImage ");
    commands.append("save res/brighten_testImage.ppm brighten_testImage ");
    commands.append("run res/testInput.txt ");
    commands.append("greyscale value-component testImage testImage-greyscale-value ");
    commands.append("save res/testImage-greyscale-value.ppm testImage-greyscale-value ");
    commands.append("greyscale luma-component testImage testImage-greyscale-luma ");
    commands.append("save res/testImage-greyscale-luma.ppm testImage-greyscale-luma ");
    commands.append("blur testImage testImage-blur ");
    commands.append("save res/testImage-blur.ppm testImage-blur ");
    commands.append("sharpen testImage testImage-sharpen ");
    commands.append("save res/testImage-sharpen.ppm testImage-sharpen ");
    commands.append("sepia testImage testImage-sepia ");
    commands.append("save res/testImage-sepia.ppm testImage-sepia ");
    commands.append("save res/testImage-sepia.ppm testImage-sepia ");
    commands.append("dither testImage testImage-dither ");
    commands.append("save res/testImage-dither.ppm testImage-dither ");

    int[][][] loadResult = {{{255, 0, 0}, {0, 0, 255}, {255, 255, 0}},
            {{0, 255, 255}, {255, 0, 255}, {0, 0, 0}}};
    int[][][] brightenedResult = {{{255, 50, 50}, {50, 50, 255}, {255, 255, 50}},
            {{50, 255, 255}, {255, 50, 255}, {50, 50, 50}}};
    int[][][] verticalFlipResult = {{{0, 255, 255}, {255, 0, 255}, {0, 0, 0}},
            {{255, 0, 0}, {0, 0, 255}, {255, 255, 0}}};
    int[][][] horizontalFlipResult = {{{255, 255, 0}, {0, 0, 255}, {255, 0, 0}},
            {{0, 0, 0}, {255, 0, 255}, {0, 255, 255}}};
    int[][][] redResult = {{{255, 255, 255}, {0, 0, 0}, {255, 255, 255}},
            {{0, 0, 0}, {255, 255, 255}, {0, 0, 0}}};
    int[][][] greenResult = {{{0, 0, 0}, {0, 0, 0}, {255, 255, 255}},
            {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}};
    int[][][] blueResult = {{{0, 0, 0}, {255, 255, 255}, {0, 0, 0}},
            {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}}};
    int[][][] greyScaledWithIntensityResult = {{{85, 85, 85}, {85, 85, 85}, {170, 170, 170}},
            {{170, 170, 170}, {170, 170, 170}, {0, 0, 0}}};
    int[][][] greyScaledWithValueResult = {{{255, 255, 255}, {255, 255, 255}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}}};
    int[][][] greyScaledWithLumaResult = {{{54, 54, 54}, {18, 18, 18}, {236, 236, 236}},
            {{200, 200, 200}, {72, 72, 72}, {0, 0, 0}}};
    int[][][] blurredResult = {{{78, 31, 77}, {93, 46, 109}, {78, 63, 46}},
            {{62, 63, 109}, {93, 46, 125}, {62, 31, 46}}};
    int[][][] sharpedResult = {{{255, 32, 189}, {189, 126, 255}, {255, 224, 95}},
            {{95, 224, 255}, {255, 126, 255}, {95, 32, 95}}};
    int[][][] ditheredResult = {{{0, 0, 0}, {0, 0, 0}, {255, 255, 255}},
            {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}};
    int[][][] sepiaResult = {{{100, 88, 69}, {48, 42, 33}, {255, 255, 205}},
            {{244, 216, 169}, {148, 130, 102}, {0, 0, 0}}};

    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands.toString()));

    assertEquals(ImageUtil.readPPM("res/brighten_testImage.ppm"), brightenedResult);
    assertEquals(ImageUtil.readPPM("res/testImage-vertical.ppm"), verticalFlipResult);
    assertEquals(ImageUtil.readPPM("res/testImage-horizontal.ppm"), horizontalFlipResult);
    assertEquals(ImageUtil.readPPM("res/testImage-red.ppm"), redResult);
    assertEquals(ImageUtil.readPPM("res/testImage-green.ppm"), greenResult);
    assertEquals(ImageUtil.readPPM("res/testImage-blue.ppm"), blueResult);
    assertEquals(ImageUtil.readPPM("res/testImage-combine.ppm"), loadResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-intensity.ppm"),
            greyScaledWithIntensityResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-value.ppm"), greyScaledWithValueResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-luma.ppm"), greyScaledWithLumaResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-red.ppm"), redResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-green.ppm"), greenResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-blue.ppm"), blueResult);
    assertEquals(ImageUtil.readPPM("res/testImage-blur.ppm"), blurredResult);
    assertEquals(ImageUtil.readPPM("res/testImage-sharpen.ppm"), sharpedResult);
    assertEquals(ImageUtil.readPPM("res/testImage-sepia.ppm"), sepiaResult);
    assertEquals(ImageUtil.readPPM("res/testImage-dither.ppm"), ditheredResult);
  }

  @Test
  public void testControllerWithRunCommandInputInFile() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    StringBuilder commands = new StringBuilder();
    commands.append("run res/testInput1.txt");
    int[][][] loadResult = {{{255, 0, 0}, {0, 0, 255}, {255, 255, 0}},
            {{0, 255, 255}, {255, 0, 255}, {0, 0, 0}}};
    int[][][] brightenedResult = {{{255, 50, 50}, {50, 50, 255}, {255, 255, 50}},
            {{50, 255, 255}, {255, 50, 255}, {50, 50, 50}}};
    int[][][] verticalFlipResult = {{{0, 255, 255}, {255, 0, 255}, {0, 0, 0}},
            {{255, 0, 0}, {0, 0, 255}, {255, 255, 0}}};
    int[][][] horizontalFlipResult = {{{255, 255, 0}, {0, 0, 255}, {255, 0, 0}},
            {{0, 0, 0}, {255, 0, 255}, {0, 255, 255}}};
    int[][][] redResult = {{{255, 255, 255}, {0, 0, 0}, {255, 255, 255}},
            {{0, 0, 0}, {255, 255, 255}, {0, 0, 0}}};
    int[][][] greenResult = {{{0, 0, 0}, {0, 0, 0}, {255, 255, 255}},
            {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}};
    int[][][] blueResult = {{{0, 0, 0}, {255, 255, 255}, {0, 0, 0}},
            {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}}};
    int[][][] greyScaledWithIntensityResult = {{{85, 85, 85}, {85, 85, 85}, {170, 170, 170}},
            {{170, 170, 170}, {170, 170, 170}, {0, 0, 0}}};
    int[][][] greyScaledWithValueResult = {{{255, 255, 255}, {255, 255, 255}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}}};
    int[][][] greyScaledWithLumaResult = {{{54, 54, 54}, {18, 18, 18}, {236, 236, 236}},
            {{200, 200, 200}, {72, 72, 72}, {0, 0, 0}}};
    int[][][] blurredResult = {{{78, 31, 77}, {93, 46, 109}, {78, 63, 46}},
            {{62, 63, 109}, {93, 46, 125}, {62, 31, 46}}};
    int[][][] sharpedResult = {{{255, 32, 189}, {189, 126, 255}, {255, 224, 95}},
            {{95, 224, 255}, {255, 126, 255}, {95, 32, 95}}};
    int[][][] ditheredResult = {{{0, 0, 0}, {0, 0, 0}, {255, 255, 255}},
            {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}};
    int[][][] sepiaResult = {{{100, 88, 69}, {48, 42, 33}, {255, 255, 205}},
            {{244, 216, 169}, {148, 130, 102}, {0, 0, 0}}};

    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands.toString()));

    assertEquals(ImageUtil.readPPM("res/testImage-brighter.ppm"), brightenedResult);
    assertEquals(ImageUtil.readPPM("res/testImage-vertical.ppm"), verticalFlipResult);
    assertEquals(ImageUtil.readPPM("res/testImage-horizontal.ppm"), horizontalFlipResult);
    assertEquals(ImageUtil.readPPM("res/testImage-red.ppm"), redResult);
    assertEquals(ImageUtil.readPPM("res/testImage-green.ppm"), greenResult);
    assertEquals(ImageUtil.readPPM("res/testImage-blue.ppm"), blueResult);
    assertEquals(ImageUtil.readPPM("res/testImage-combine.ppm"), loadResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-intensity.ppm"),
            greyScaledWithIntensityResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-value.ppm"), greyScaledWithValueResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-luma.ppm"), greyScaledWithLumaResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-red.ppm"), redResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-green.ppm"), greenResult);
    assertEquals(ImageUtil.readPPM("res/testImage-greyscale-blue.ppm"), blueResult);
    assertEquals(ImageUtil.readPPM("res/testImage-blur.ppm"), blurredResult);
    assertEquals(ImageUtil.readPPM("res/testImage-sharpen.ppm"), sharpedResult);
    assertEquals(ImageUtil.readPPM("res/testImage-sepia.ppm"), sepiaResult);
    assertEquals(ImageUtil.readPPM("res/testImage-dither.ppm"), ditheredResult);

  }

  @Test
  public void testControllerCorrectnessOfPassingTheNewCommands() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelModelMock(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "blur building building-blur\n"
            + "sharpen building building-sharpen\n"
            + "sepia building building-sepia\n"
            + "greyscale luma-component building building-luma\n";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
    assertEquals(commands, sb.toString());
  }

  @Test
  public void testControllerAndModelWithNewCommands() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load res/testImage.ppm testImage\n"
            + "sepia testImage testImage-sepia\n"
            + "save res/testSepia.png testImage-sepia\n"
            + "dither testImage testImage-dither\n"
            + "save res/testDither.png testImage-dither\n"
            + "greyscale luma-component testImage testImage-luma\n"
            + "save res/testLuma.bmp testImage-luma\n"
            + "blur testImage testImage-blur\n"
            + "save res/testBlur.png testImage-blur\n"
            + "sharpen testImage testImage-sharpen\n"
            + "save res/testSharpen.png testImage-sharpen\n";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));

    int[][][] sepiaImage = {{{100, 88, 69}, {48, 42, 33}, {255, 255, 205}},
            {{244, 216, 169}, {148, 130, 102}, {0, 0, 0}}};
    int[][][] ditheredImage = {{{0, 0, 0}, {0, 0, 0}, {255, 255, 255}},
            {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}};
    int[][][] greyScaledWithLumaResult = {{{54, 54, 54}, {18, 18, 18}, {236, 236, 236}},
            {{200, 200, 200}, {72, 72, 72}, {0, 0, 0}}};

    assertArrayEquals(ImageUtil.readOtherImageFormats("res/testSepia.png"), sepiaImage);
    assertArrayEquals(ImageUtil.readOtherImageFormats("res/testDither.png"), ditheredImage);
    assertArrayEquals(ImageUtil.readOtherImageFormats("res/testLuma.bmp"),
            greyScaledWithLumaResult);
  }

  @Test
  public void testControllerAndModelWithDifferentImageFormats() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load res/testImage_asPNG.png testImage\n"
            + "sepia testImage testImage-sepia\n"
            + "save res/testSepia.bmp testImage-sepia\n";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
    int[][][] sepiaImage = {{{100, 88, 69}, {48, 42, 33}, {255, 255, 205}},
            {{244, 216, 169}, {148, 130, 102}, {0, 0, 0}}};
    assertArrayEquals(ImageUtil.readOtherImageFormats("res/testSepia.bmp"), sepiaImage);
  }

  @Test
  public void testControllerAndModelWhenSavingAFile() throws Exception {
    StringBuilder sb = new StringBuilder();
    ImageProcessingModelNewFeature model = new ImageProcessingModelNewFeaturesImpl(sb);
    ImageProcessingView view = new ImageProcessingViewImpl(System.out, sb);
    String commands = "load res/testImage_asPNG.png testImage\n"
            + "save res/testImage.bmp testImage\n"
            + "save res/testImage.jpg testImage\n"
            + "save res/testImage.png testImage\n";
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view);
    controller.simulate(new StringReader(commands));
    assertTrue(new File("res/testImage.bmp").exists());
    assertTrue(new File("res/testImage.png").exists());
    assertTrue(new File("res/testImage.jpg").exists());
  }

  /**
   * Mock class to test the correctness of the commands passed by the Controller.
   */
  private class ImageProcessingModelModelMock implements ImageProcessingModelNewFeature {

    private final StringBuilder sb;

    /**
     * Create a mock model to test the controller output.
     *
     * @param sb the string builder to save the commands
     */
    private ImageProcessingModelModelMock(StringBuilder sb) {
      this.sb = sb;
    }

    @Override
    public Image load(String imageName, Image image) {
      sb.append("load" + " " + imageName + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image greyScale(String component, String inputImageName, String greyScaledImage) {
      sb.append("greyscale " + component + " " + inputImageName + " " + greyScaledImage + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image horizontalFlip(String inputImageName, String flippedImageName)
            throws IOException {
      sb.append("horizontal-flip " + inputImageName + " " + flippedImageName + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image verticalFlip(String inputImageName, String flippedImageName)
            throws IOException {
      sb.append("vertical-flip " + inputImageName + " " + flippedImageName + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image brighten(int factor, String imageName, String destImageName)
            throws IOException {
      sb.append("brighten " + factor + " " + imageName + " " + destImageName + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public List<Image> rgbSplit(String inputImageName, String rgbSplitRed, String rgbSplitGreen,
                                String rgbSplitBlue) {
      sb.append(
              "rgb-split " + inputImageName + " " + rgbSplitRed + " " + rgbSplitGreen + " "
                      + rgbSplitBlue + "\n");
      return new ArrayList<>();
    }

    @Override
    public Image rgbCombine(String destinationImageName, String rgbCombineRedComponent,
                            String rgbCombineGreenComponent, String rgbCombineBlueComponent) {
      sb.append("rgb-combine " + destinationImageName + " " + rgbCombineRedComponent + " "
              + rgbCombineGreenComponent + " " + rgbCombineGreenComponent + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image save(String imageName) {
      sb.append("save " + imageName + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image blur(String inputImageName, String blurredImageName) {
      sb.append("blur " + inputImageName + " " + blurredImageName + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image sharpen(String inputImageName, String sharpedImageName) {
      sb.append("sharpen " + inputImageName + " " + sharpedImageName + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image sepia(String imageName, String destImageName) {
      sb.append("sepia " + imageName + " " + destImageName + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image dither(String imageName, String destImageName) {
      sb.append("dither " + imageName + " " + destImageName + "\n");
      return new Image(new int[0][][]);
    }

    @Override
    public Image mosaic(int numOfSeeds, String imageName, String destImageName) {
      sb.append("mosaic" + " " + numOfSeeds + " " + imageName + " " + destImageName + "\n");
      return new Image(new int[0][][]);
    }
  }

}