package graphics;


/**
 * Contains various game-wide settings. This class follows the record style, with
 * {@code public static final} variables that can be accessed and a {@code private} constructor.
 *
 * @author Jonathan Uhler
 */
public class Settings {

	/** The frame rate of the game. */
	public static final int FPS = 60;
	/** 
	 * The field of view of the player. This value is the maximum number of tiles, end-to-end, that
	 * can be seen by the player at once. FOV should be an odd number such that 
	 * {@code (FOV - 1) / 2 % 2 == 0} so that an even number of tiles can be seen to both sides
	 * of the player with a single tile left for the player to occupy in the middle.
	 * This variable can be changed, but undefined behavior occurs if it is outside the range
	 * {@code [3, +inf)} or does not meet the criteria defined above.
	 */
	public static int FOV = 25;
	

	/**
	 * The {@code Settings} class cannot be constructed. Fields should be accessed as static
	 * variables.
	 */
	private Settings() { }

}
