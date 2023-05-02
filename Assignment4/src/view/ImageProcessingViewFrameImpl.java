package view;

import controller.ImageProcessingGUIController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.Image;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * This class represent the GUI of the application and implements the ImageProcessingViewFrame along
 * with the ActionListener.
 */
public class ImageProcessingViewFrameImpl extends JFrame implements ImageProcessingViewFrame,
    ActionListener {

  private ImageProcessingGUIController controller;
  private JLabel imageLabel;
  private JPanel chartPanel;
  private String command;
  private boolean loadFlag;

  /**
   * Create the JFrame and setup all the components of the GUI.
   */
  public ImageProcessingViewFrameImpl() {
    super();
    command = "";
    loadFlag = false;
    setTitle("Image Processing Application");
    setSize(1000, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(mainPanel);
    JButton loadImage = createButton("Load Image", "load");
    mainPanel.add(loadImage);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    mainPanel.add(addImageAndHistogramToView());
    mainPanel.add(addButtonsForAllTheOperations());
    JButton saveImage = createButton("Save Image", "save");
    mainPanel.add(saveImage);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    setVisible(true);
    pack();
  }

  private Component addButtonsForAllTheOperations() {
    JPanel buttonsPanel = new JPanel();
    JButton brighten = createButton("Brighten", "brighten");
    buttonsPanel.add(brighten);
    JButton horizontalFlip = createButton("Horizontal Flip", "horizontal-flip");
    buttonsPanel.add(horizontalFlip);
    JButton verticalFlip = createButton("Vertical Flip", "vertical-flip");
    buttonsPanel.add(verticalFlip);
    JButton greyScale = createButton("Greyscale", "greyscale");
    buttonsPanel.add(greyScale);
    JButton rgbSplit = createButton("RGB Split", "rgb-split");
    buttonsPanel.add(rgbSplit);
    JButton rgbCombine = createButton("RGB Combine", "rgb-combine");
    buttonsPanel.add(rgbCombine);
    JButton blur = createButton("Blur", "blur");
    buttonsPanel.add(blur);
    JButton sharpen = createButton("Sharpen", "sharpen");
    buttonsPanel.add(sharpen);
    JButton sepia = createButton("Sepia", "sepia");
    buttonsPanel.add(sepia);
    JButton dither = createButton("Dither", "dither");
    buttonsPanel.add(dither);
    JButton mosaic = createButton("Mosaic", "mosaic");
    buttonsPanel.add(mosaic);
    return buttonsPanel;
  }

  private Component addImageAndHistogramToView() {
    JPanel imagePanel = new JPanel();
    ImageIcon image = new ImageIcon();
    imageLabel = new JLabel(image);
    JScrollPane scrollPane = new JScrollPane(imageLabel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    chartPanel = new JPanel();
    imagePanel.setLayout(new GridLayout(1, 2));
    imagePanel.setPreferredSize(new Dimension(500, 500));
    imagePanel.add(scrollPane);
    imagePanel.add(chartPanel);
    return imagePanel;
  }

  private JButton createButton(String name, String actionCommand) {
    JButton button = new JButton(name);
    button.setAlignmentX(0);
    button.setActionCommand(actionCommand);
    button.addActionListener(this);
    return button;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    command = e.getActionCommand();
    controller.executeTheInput(command, loadFlag);
    if (command.equals("load")) {
      loadFlag = true;
    }
  }


  @Override
  public void addController(ImageProcessingGUIController controller) {
    this.controller = controller;
  }

  @Override
  public void displayErrorLoadFirstDialogBox() {
    JOptionPane.showMessageDialog(null, "Image should be loaded first", "Error",
        JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void displayErrorDialogBox() {
    JOptionPane.showMessageDialog(null, "Operation failed to execute!", "Error",
        JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void refreshImageOnScreen(Object image) {
    if (!command.contains("rgb-split")) {
      int[][][] imageArray = ((Image) image).getImageArray();
      int height = imageArray.length;
      int width = imageArray[0].length;
      BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      for (int x = 0; x < height; x++) {
        for (int y = 0; y < width; y++) {
          int rgb = (imageArray[x][y][0] << 16) | (imageArray[x][y][1] << 8) | imageArray[x][y][2];
          bufferedImage.setRGB(y, x, rgb);
        }
      }
      chartPanel.removeAll();
      JLabel jLabel = new JLabel();
      jLabel.setIcon(new ImageIcon(createLineChart(bufferedImage)));
      chartPanel.add(jLabel);
      imageLabel.setIcon(new ImageIcon(bufferedImage));
      validate();
    }
  }


  private BufferedImage createLineChart(BufferedImage image) {
    int[] redComponentsCount = new int[256];
    int[] greenComponentsCount = new int[256];
    int[] blueComponentsCount = new int[256];
    int[] intensityComponentsCount = new int[256];
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Color c = new Color(image.getRGB(x, y));
        redComponentsCount[c.getRed()]++;
        greenComponentsCount[c.getGreen()]++;
        blueComponentsCount[c.getBlue()]++;
        int intensity = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
        intensityComponentsCount[intensity]++;
      }
    }
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (int i = 0; i < 256; i++) {
      dataset.addValue(redComponentsCount[i], "Red", Integer.toString(i));
      dataset.addValue(greenComponentsCount[i], "Green", Integer.toString(i));
      dataset.addValue(blueComponentsCount[i], "Blue", Integer.toString(i));
      dataset.addValue(intensityComponentsCount[i], "Intensity", Integer.toString(i));
    }
    JFreeChart chart = ChartFactory.createLineChart("Histogram", "RGB values(0-255)", "Frequency",
        dataset,
        PlotOrientation.VERTICAL, true, true, false);
    CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
    categoryPlot.getRenderer().setSeriesPaint(0, Color.RED);
    categoryPlot.getRenderer().setSeriesPaint(1, Color.GREEN);
    categoryPlot.getRenderer().setSeriesPaint(2, Color.BLUE);
    categoryPlot.getRenderer().setSeriesPaint(3, Color.YELLOW);
    CategoryAxis domainAxis = categoryPlot.getDomainAxis();
    domainAxis.setCategoryLabelPositions(
        CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 2.0));
    return chart.createBufferedImage(450, 400);
  }
}
