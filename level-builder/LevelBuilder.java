import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.Color;
import java.io.File;
import java.io.IOException;


public class LevelBuilder {

	public static void help() {
		System.out.println("level-builder\n" +
						   "A level building tool for Byte-Strike to generate maps from images.\n" +
						   "\n" +
						   "Usage: LevelBuilder IMAGE_FILE_PATH LEVEL_SIZE LUMINANCE_THRESHOLD");
	}
	

	public static void main(String[] args) {
		// Check for args
		if (args.length != 3) {
			System.out.println("level-builder: expected 2 args, found" + args.length);
			LevelBuilder.help();
			System.exit(1);
		}

		// Load file string
		String fileStr = args[0];
		if (!fileStr.toLowerCase().endsWith(".png")) {
			System.out.println("level-builder: please specify a png file, found " + fileStr);
			System.exit(1);
		}

		// Load file as object
		File file = new File(fileStr);
		if (!file.exists()) {
			System.out.println("level-builder: unable to open level image, file does not exist");
			System.exit(1);
		}

		// Get level size in tiles
		int levelSizeTiles = -1;
		try {
			levelSizeTiles = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e) {
			System.out.println("level-builder: cannot parse level size: " + args[1]);
			System.exit(1);
		}

		// Get luminance threshold for a solid tile
		double luminanceThreshold = -1.0;
		try {
			luminanceThreshold = Double.parseDouble(args[2]);
		}
		catch (NumberFormatException e) {
			System.out.println("level-builder: cannot parse luminance threshold: " + args[2]);
			System.exit(1);
		}

		// Create buffered image
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		}
		catch (IOException e) {
			System.out.println("level-builder: unable to load level image: " + e);
			System.exit(1);
		}

		// Calculate size info
		int imageWidthPx = image.getWidth();
		int imageHeightPx = image.getHeight();
		int pxPerTileHoriz = imageWidthPx / levelSizeTiles;
		int pxPerTileVert = imageHeightPx / levelSizeTiles;
		int pxPerTile = pxPerTileHoriz * pxPerTileVert;

		// Init image data
		Color[][] pixels = new Color[imageHeightPx][imageWidthPx];
		for (int y = 0; y < imageHeightPx; y++) {
			for (int x = 0; x < imageWidthPx; x++) {
				int rgb = image.getRGB(x, y);
				Color color = new Color(rgb);
				pixels[y][x] = color;
			}
		}

		// Create level
		String level = "";
		for (int tx = 0; tx < levelSizeTiles; tx++) {
			level += "\"";
			for (int ty = 0; ty < levelSizeTiles; ty++) {
				int red = 0;
				int green = 0;
				int blue = 0;

				// Find the color of all pixels that contribute to this tile
				for (int px = tx * pxPerTileHoriz; px < (tx + 1) * pxPerTileHoriz; px++) {
					for (int py = ty * pxPerTileVert; py < (ty + 1) * pxPerTileVert; py++) {
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
				if (luminance > luminanceThreshold)
					level += "#";
				else
					level += " ";
			}
			level += "\\n\" +\n";
		}
		if (level.endsWith(" +\n"))
			level = level.substring(0, level.length() - 3);

		// Return data to the user
		System.out.println("BEGIN LEVEL\n" +
						   "-----------\n" +
						   level + "\n" +
						   "-----------\n" +
						   "END LEVEL\n");
	}

}
