package world;


import client.ByteStrike;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.imageio.ImageIO;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Hashtable;


/**
 * Generates the bare outline of a level from the luminance values in an image. <b>This class is a 
 * work in progress and may contain issues parsing non-regular image shapes or sizes.</b>
 * <p>
 * <b> Usage </b>
 * <p>
 * This program is graphical and operates primarily using {@code JOptionPane}s. It can be run
 * through the main game menu by clicking on the "Level Builder" button. The file can also
 * be run directly through the public method, {@code create} which will graphically prompt
 * the user for necessary inputs.
 * <p>
 * If enough information is already known about the conditions being used to generate
 * the level, the {@code completeGeneration} method can be used. However, it is recommended
 * that the {@code create} method be invoked instead to minimize the risk of pre-condition
 * related exceptions.
 * <p>
 * <b>Algorithm</b>
 * <p>
 * The level builder determines the dimensions of one tile in terms of pixels from the width
 * and height of the source image (by definition this is 
 * {@code pxPerTileDim = imageDim / LEVEL_SIZE} for a given dimension: width or height).
 * Using this, the program loops through the image in sections of that many pixels. The average
 * color of all the pixels in a {@code pxPerTileWidth * pxPerTileHeight} area is taken and
 * compared against the {@code LUMINANCE_THRESHOLD} value specified in the command line
 * arguments. If the average luminance is strictly greater than the threshold (or strictly
 * less than the threshold if the reverse option is specified), a solid wall tile of the type
 * provided is added, otherwise an empty tile is added.
 */
public class LevelBuilder {

	/**
	 * This class is not intended to be constructed.
	 */
	private LevelBuilder() { }


	/**
	 * Wrapper function to create a new {@code JSeparator}. The returned separator is the
	 * correct width and height for the option pane displayed by the level builder. The
	 * color is also specified.
	 *
	 * @return a new {@code JSeparator}.
	 */
	private static JSeparator getSeparator() {
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		separator.setPreferredSize(new Dimension(600, 20));
		separator.setBackground(new Color(160, 160, 160));
		return separator;
	}


	/**
	 * Entry method to create a new level using a graphical interface.
	 */
	public static void create() {
		// Construct components
		JFileChooser imageFileChooser = new JFileChooser();
		JSpinner levelSizeSpinner = new JSpinner(new SpinnerNumberModel(64, 16, 256, 1));
		JTextField wallCharTextField = new JTextField(2);
		JTextField noneCharTextField = new JTextField(2);
		JSlider luminanceSlider = new JSlider(0, 255, 128);
		JCheckBox invertLuminanceCheckBox = new JCheckBox("Invert Luminance");

		// Set up component properties
		imageFileChooser.setFileFilter(new FileNameExtensionFilter("PNG images", "png", "PNG"));
		imageFileChooser.setAcceptAllFileFilterUsed(false);
		Hashtable<Integer, JLabel> luminanceLabelTable = new Hashtable<>();
		luminanceLabelTable.put(0, new JLabel("0"));
		luminanceLabelTable.put(128, new JLabel("Luminance Limit"));
		luminanceLabelTable.put(255, new JLabel("255"));
		luminanceSlider.setLabelTable(luminanceLabelTable);
		luminanceSlider.setPaintLabels(true);

		// Get user input
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(new JLabel("<html><h1>Byte Strike Level Builder<h1></html>"), gbc);
		gbc.gridy++;
		panel.add(LevelBuilder.getSeparator(), gbc);

		gbc.gridy++;
		panel.add(new JLabel("Level Image File"), gbc);
		gbc.gridy++;
		panel.add(imageFileChooser, gbc);
		gbc.gridy++;
		panel.add(LevelBuilder.getSeparator(), gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		panel.add(new JLabel("Level Size: "), gbc);
		gbc.gridx++;
		panel.add(levelSizeSpinner, gbc);
		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(LevelBuilder.getSeparator(), gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		panel.add(new JLabel("Wall Char: "), gbc);
		gbc.gridx++;
		panel.add(wallCharTextField, gbc);
		gbc.gridx++;
		panel.add(new JLabel("None Char: "), gbc);
		gbc.gridx++;
		panel.add(noneCharTextField, gbc);
		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(LevelBuilder.getSeparator(), gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		panel.add(luminanceSlider, gbc);
		gbc.gridx += 2;
		panel.add(invertLuminanceCheckBox, gbc);
		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(LevelBuilder.getSeparator(), gbc);

		// Create the JOptionPane
		int confirm = JOptionPane.showConfirmDialog(null, panel, "Level Settings",
													JOptionPane.OK_CANCEL_OPTION,
													JOptionPane.PLAIN_MESSAGE, null);
		if (confirm != JOptionPane.OK_OPTION)
			return;

		// Generate level
		LevelBuilder.completeGeneration(imageFileChooser.getSelectedFile(),
										(int) levelSizeSpinner.getValue(),
										wallCharTextField.getText(),
										noneCharTextField.getText(),
										luminanceSlider.getValue(),
										invertLuminanceCheckBox.isSelected());
	}


	/**
	 * Performs the programatical operations to generate a level.
	 *
	 * @param file                the file pointing to the source image that should be used to
	 *                            generate the level.
	 * @param levelSize           the size, square, of the level to generate.
	 * @param wall                the character that should be added to the level string if
	 *                            the luminance operation determines a given tile to be filled.
	 * @param none                the character that should be added to the level string if
	 *                            the luminance operation determines a given tile to be empty.
	 * @param luminanceThreshold  the minimum (or maximum if {@code invert == true}) average value
	 *                            that a group of pixels in the source image must have to be
	 *                            treated as a solid wall tile in the final image.
	 * @param invert              if {@code true}, pixel groups with an average luminance 
	 *                            <b>strictly less</b> than {@code luminanceThreshold} are walls,
	 *                            otherwise groups <b>strictly greater</b> than
	 *                            {@code luminanceThreshold} are walls. That is, if 
	 *                            {@code invert == false}, walls in the source image should be
	 *                            bright and if {@code invert == true}, walls in the source image
	 *                            should be dark.
	 *
	 * @throws IllegalArgumentException  if {@code level} is outside the interval {@code [16, 256}.
	 * @throws NullPointerException      if {@code wall == null}.
	 * @throws NullPointerException      if {@code none == null}.
	 * @throws IllegalArgumentException  if {@code luminanceThreshold} is outside the interval
	 *                                   {@code [0, 255]}.
	 */
	public static void completeGeneration(File file,
										  int levelSize,
										  String wall,
										  String none,
										  int luminanceThreshold,
										  boolean invert)
	{
		// Check preconditions
		if (levelSize < 16 || levelSize > 256)
			throw new IllegalArgumentException("invalid level size: " + levelSize);
		if (wall == null)
			throw new NullPointerException("wall char was null");
		if (none == null)
			throw new NullPointerException("none char was null");
		if (luminanceThreshold < 0 || luminanceThreshold > 255)
			throw new IllegalArgumentException("invalid luminance limit: " + luminanceThreshold);
		
		// Create buffered image
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		}
		catch (Exception e) {
			ByteStrike.displayMessage("File Error", "Cannot load file as BufferedImage:\n" + e);
		    return;
		}

		// Calculate size info
		int imageW = image.getWidth();
		int imageH = image.getHeight();
		int pxPerTileHoriz = imageW / levelSize;
		int pxPerTileVert = imageH / levelSize;
		int pxPerTile = pxPerTileHoriz * pxPerTileVert;

		// Load image data as 2d color array
		Color[][] pixels = new Color[imageH][imageW];
		for (int y = 0; y < imageH; y++) {
			for (int x = 0; x < imageW; x++) {
				int rgb = image.getRGB(x, y);
				Color color = new Color(rgb);
				pixels[y][x] = color;
			}
		}

		// Create level
		String level = "";
		for (int tx = 0; tx < levelSize; tx++) {
			level += "\"";
			for (int ty = 0; ty < levelSize; ty++) {
				int red = 0;
				int green = 0;
				int blue = 0;

				// Find the color of all pixels that contribute to this tile
				for (int px = tx * pxPerTileHoriz; px < (tx + 1) * pxPerTileHoriz; px++) {
					if (px >= pixels.length)
						continue;
					
					for (int py = ty * pxPerTileVert; py < (ty + 1) * pxPerTileVert; py++) {
						if (py >= pixels[px].length)
							continue;
						
						Color color = pixels[px][py];
						red += color.getRed();
						green += color.getGreen();
						blue += color.getBlue();
					}
				}

				red /= pxPerTile;
				green /= pxPerTile;
				blue /= pxPerTile;
				double luminance = 0.299 * red + 0.587 * green + 0.114 * blue;
				if (!invert)
					level += luminance > luminanceThreshold ? wall : none;
				else
					level += luminance < luminanceThreshold ? wall : none;
			}
			level += "\\n\" +\n";
		}
		if (level.endsWith(" +\n"))
			level = level.substring(0, level.length() - 3);

		// Return data to the user
		JPanel panel = new JPanel();
		JTextArea textArea = new JTextArea(level);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 512 / levelSize));
		textArea.setEditable(false);
		panel.add(new JScrollPane(textArea));
		JOptionPane.showConfirmDialog(null, panel, "Finished Level",
									  JOptionPane.OK_CANCEL_OPTION,
									  JOptionPane.PLAIN_MESSAGE, null);
	}

}
