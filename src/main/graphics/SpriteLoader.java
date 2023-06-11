package graphics;


import world.Level.Tile;
import entity.Player;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.util.Map;
import java.util.HashMap;


/**
 * Handles the loading and drawing of sprite images.
 *
 * @author Jonathan Uhler
 */
public class SpriteLoader {

	/** The top-level path for all sprites. */
	public static final String SPRITE_PATH = "assets/sprites";
	/** The file extension for sprite images. */
	public static final String SPRITE_EXT = ".png";
	/** The full path to the missing sprite texture. */
	public static final String INVALID_SPRITE = SPRITE_PATH + "/missing" + SPRITE_EXT;


	/** A mapping of already loaded sprites and their names. */
	private static Map<String, BufferedImage> images = new HashMap<>();
	

	/**
	 * This class cannot be constructed.
	 */
	private SpriteLoader() { }


	/**
	 * Converts a generic {@code Image} object to a {@code BufferedImage}.
	 *
	 * @param img  the image to convert.
	 *
	 * @return the {@code BufferedImage} representation of the argument image.
	 */
	private static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage)
			return (BufferedImage) img;

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null),
												 img.getHeight(null),
												 BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}


	/**
	 * Scales a {@code BufferedImage} to a specified width and height.
	 *
	 * @param img  the img to scale.
	 * @param w    the final width of the image.
	 * @param h    the final height of the image.
	 *
	 * @return a scaled version of the argument {@code img}.
	 *
	 * @throws NullPointerException  if {@code img == null}.
	 */
    private static BufferedImage scale(BufferedImage img, int w, int h) {
		if (img == null)
			throw new NullPointerException("img was null");
		
        BufferedImage scaled = new BufferedImage(w, h, img.getType());
		Graphics2D graphics2D = scaled.createGraphics();
		graphics2D.drawImage(img, 0, 0, w, h, 0, 0, img.getWidth(), img.getHeight(), null);
		graphics2D.dispose();
        return scaled;
    }


	/**
	 * Loads a sprite image from a file.
	 *
	 * @param fileName  the name of the file to load. This string should not start with
	 *                  a slash and should not include the file extension. It must be located
	 *                  somewhere in the path structure specified by 
	 *                  {@code SpriteLoader.SPRITE_PATH}.
	 * @param size      the desired size (squared) of the image.
	 *
	 * @return the loaded image. If the specified file cannot be found or loaded, the
	 *         missing sprite texture of the specified size is returned.
	 */
	private static BufferedImage loadImage(String fileName, int size) {
		// Check if the image has already been stored in memory and has not since been resized.
		// If this is true, the saved pointer can be returned instead of re-loading the image.
		BufferedImage existing = SpriteLoader.images.get(fileName);
		if (existing != null && existing.getWidth() == size)
			return existing;

		// Load the image
		String path = SpriteLoader.SPRITE_PATH + "/" + fileName + SpriteLoader.SPRITE_EXT;
		ImageIcon imageIcon;
		try {
		    imageIcon = new ImageIcon(Thread.currentThread()
									  .getContextClassLoader()
									  .getResource(path));
		}
		catch (NullPointerException e) {
			// If the image cannot be loaded, load the missing sprite texture
			imageIcon = new ImageIcon(Thread.currentThread()
									  .getContextClassLoader()
									  .getResource(SpriteLoader.INVALID_SPRITE));
		}

		// Convert the image to a BufferedImage and scale it
		Image image = imageIcon.getImage();
		BufferedImage bImage = SpriteLoader.toBufferedImage(image);
		bImage = SpriteLoader.scale(bImage, size, size);
		// Save the image if it hasn't already been saved
		if (existing == null || existing.getWidth() != size)
			SpriteLoader.images.put(fileName, bImage);
		return bImage;
	}


	/**
	 * Draws an arbitrary sprite. If the sprite cannot be loaded, the missing sprite texture is 
	 * drawn. The specified type can be any sprite in the {@code assets/sprites} folder.
	 *
	 * @param g         the {@code Graphics} object to draw the tile on.
	 * @param type      the path, after {@code assets/sprites} to the sprite file. This path
	 *                  does <b>not</b> include a leading slash or a file extension.
	 * @param xPx       the x position of the top-left corner of the sprite.
	 * @param yPx       the y position of the top-left corner of the sprite.
	 * @param size      the size to draw the sprite.
	 *
	 * @throws NullPointerException  if {@code g == null}.
	 * @throws NullPointerException  if {@code type == null}.
	 */
	public static void draw(Graphics g, String type, int xPx, int yPx, int size) {
		if (g == null)
			throw new NullPointerException("g was null");
		if (type == null)
			throw new NullPointerException("type was null");
		g.drawImage(SpriteLoader.loadImage(type, size), xPx, yPx, null);
	}


	/**
	 * Draws a tile. If the sprite cannot be loaded, the missing sprite texture is drawn.
	 *
	 * @param g         the {@code Graphics} object to draw the tile on.
	 * @param type      the type of the tile, as defined by the {@code Map.Tile} enum.
	 * @param xPx       the x position of the top-left corner of the tile.
	 * @param yPx       the y position of the top-left corner of the tile.
	 * @param tileSize  the size of the tile.
	 *
	 * @throws NullPointerException  if {@code g == null}.
	 * @throws NullPointerException  if {@code type == null}.
	 */
	public static void drawTile(Graphics g,
								Tile type,
								int xPx,
								int yPx,
								int tileSize)
	{
		if (g == null)
			throw new NullPointerException("g was null");
		if (type == null)
			throw new NullPointerException("type was null");
		g.drawImage(SpriteLoader.loadImage("Tile/" + type.name(), tileSize), xPx, yPx, null);
	}


	/**
	 * Draws an entity (excluding the player). If the sprite cannot be loaded, the missing sprite 
	 * texture is drawn. The player and weapon textures can be drawn with this method, but not
	 * at once. For the player to be drawn with their weapon in the appropriate location, use
	 * the {@code drawPlayer} method.
	 *
	 * @param g         the {@code Graphics} object to draw the entity on.
	 * @param type      the type of entity to draw, as defined by the {@code getType} method of 
	 *                  the entity.
	 * @param xPx       the x position of the top-left corner of the entity.
	 * @param yPx       the y position of the top-left corner of the entity.
	 * @param size      the size of the entity's collision area.
	 * @param rad       the angle, in radians relative to the unit circle, to rotate the
	 *                  sprite before drawing.
	 *
	 * @throws NullPointerException  if {@code g == null}.
	 * @throws NullPointerException  if {@code type == null}.
	 */
	public static void drawEntity(Graphics g,
								  String type,
								  int xPx,
								  int yPx,
								  int size,
								  double rad)
	{
		if (g == null)
			throw new NullPointerException("g was null");
		if (type == null)
			throw new NullPointerException("type was null");

		rad += Math.PI / 2; // Because the rotate method does not know how the unit cricle works

		int centerX = xPx + size / 2;
		int centerY = yPx + size / 2;
		Graphics2D gg = (Graphics2D) g;
		gg.rotate(rad, centerX, centerY);
		gg.drawImage(SpriteLoader.loadImage("entity/" + type, size), xPx, yPx, null);
		gg.rotate(-rad, centerX, centerY);
	}


	private static Point weaponOffset(double rad, int tileSize) {
		double x = 0.5 * Math.sin(-rad + Math.PI);
		double y = -0.5 * Math.sin(-rad + Math.PI / 2);
	    return new Point((int) (x * tileSize), (int) (y * tileSize));
	}


	public static void drawPlayer(Graphics g,
								  Player player,
								  int xPx,
								  int yPx,
								  int tileSize,
								  double rad) {
		if (g == null)
			throw new NullPointerException("g was null");
		if (player == null)
			throw new NullPointerException("player was null");

		Graphics2D gg = (Graphics2D) g;
		int size = (int) (player.getSize() * tileSize);

		rad += Math.PI / 2; // Because the rotate method does not know how the unit cricle works

		int centerX = xPx + size / 2;
		int centerY = yPx + size / 2;
		String type = player.getType();
		gg.rotate(rad, centerX, centerY);
		gg.drawImage(SpriteLoader.loadImage("entity/" + type, size), xPx, yPx, null);
		if (player.isArmored())
			gg.drawImage(SpriteLoader.loadImage("entity/Armor", size), xPx, yPx, null);
		gg.rotate(-rad, centerX, centerY);

		
		String weaponType = player.getWeapon().getType();
		Point weaponOffset = SpriteLoader.weaponOffset(rad, tileSize);
		int weaponX = xPx + weaponOffset.x;
		int weaponY = yPx + weaponOffset.y;
		int weaponCenterX = weaponX + size / 2;
		int weaponCenterY = weaponY + size / 2;
		gg.rotate(rad, weaponCenterX, weaponCenterY);
		gg.drawImage(SpriteLoader.loadImage("entity/" + weaponType, size),
					 weaponX, weaponY, null);
		gg.rotate(-rad, weaponCenterX, weaponCenterY);
	}

}
