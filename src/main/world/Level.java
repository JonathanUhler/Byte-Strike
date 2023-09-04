package world;


import interfaces.Moveable;
import java.awt.Point;


/**
 * Handles the tile arrangement of the world in which entities interact.
 *
 * @author Jonathan Uhler
 */
public class Level {

	/**
	 * Represents a tile on the level.
	 */
	public enum Tile {
		/** An empty tile on which entities can move. */
		NONE,
		/** A solid wall which stops entities, textured as stone. */
		WALL_STONE,
		/** A solid wall which stops entities, textured as sand bricks. */
		WALL_SAND,
		/** A solid block textured as a wooden crate. */
		CRATE
	}


	/** A map used for testing. */
    public static final String LEVEL_0 =
		"########\n" +
		"#      #\n" +
		"#      #\n" +
		"#      #\n" +
		"#      #\n" +
		"#      #\n" +
		"#      #\n" +
		"########";
	/** Map choice 1. */
	public static final String LEVEL_1 =
	    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%%%    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%%%    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%          %%%%\n" +
		"%%%%%    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%          %%%%\n" +
		"%%%%%    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%          %%%%\n" +
		"%%%%%           %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%          %%%%\n" +
		"%%%%%            %%%%%%%%%%%%%%%%%%%%%%                     %%%%\n" +
		"%%%%%                %%%%%%%%%%%%%%%%%%                     %%%%\n" +
		"%%%%%                %%%%%%%%%%%%%%%%%%%                    %%%%\n" +
		"%%%%%                           %%%%%                       %%%%\n" +
		"%%%%%                           %%%%                        %%%%\n" +
		"%%%%                            %%%%                        %%%%\n" +
		"%%%%                            %%%%                        %%%%\n" +
		"%%%%                                                          %%\n" +
		"%%%%                                                           %\n" +
		"%%%%                                                           %\n" +
		"%%%%                                                           %\n" +
		"%%%%                                                           %\n" +
		"%%%%             %%%%%%%%%%          %%%    %%%%%              %\n" +
		"%%%%             %%%%%%%%%%     %%%%%%%     %%%%%%%%%%         %\n" +
		"%%%%            %%%%%%%%%%%     %%%%%%%     %%%%%%%%%%         %\n" +
		"%%%%           %%%%%%%%%%%%      %%%%%%     %%%%%%%%%         %%\n" +
		"%%%%%%   %%   %%%%%%%%%%%%%      %%%%%%     %%%%%%%%%       %%%%\n" +
		"%%%%%%   %%   %%%%%%%%%%%%%     %%   %%     %%%%%%%%%       %%%%\n" +
		"%%%%%%   %%%%%%%%%                          %%%%%%%%%       %%%%\n" +
		"%%%%%%   %%%%%%%%%                          %%%%%%%%%%      %%%%\n" +
		"%%%%        %%%%%%                          %%%%%%%%%%      %%%%\n" +
		"%%%%         %%%%%                          %%%%%%%%%%      %%%%\n" +
		"%%%%                               %%%%%%%%%%%%%%%%%%%      %%%%\n" +
		"%%%%                  %%%%%%       %%%%%%%%%                %%%%\n" +
		"%%%%                  %%%%%%       %%%%%%%%                 %%%%\n" +
		"%%%%                 %%%%%%%       %%%%%%%%                 %%%%\n" +
		"%%%%                %%%%%%%%       %%%%%%%%                    %\n" +
		"%%%%%%%%%     %%%%%%%%%%%%%%       %%%%%%%%                    %\n" +
		"%%%%%%%%%%   %%%%%%%%%%%%%%%       %%%%%%%%                    %\n" +
		"%%%%%%%%%%   %%%%%%%%%%%%%%%       %%%%%%%%                    %\n" +
		"%%%%%%%%        %%%%%%%%%%         %%%%%%%%                    %\n" +
		"%%%%%%%%        %%%%%%%%%%         %%%%%%%%     %%             %\n" +
		"%%%%%%            %%%%%%%%         %%%%%%%%     %%             %\n" +
		"%%%%%%            %%%%%%%          %%%%%%%%     %%             %\n" +
		"%%%%%%            %%%%%%%                       %%             %\n" +
		"%%%%%%            %%%%%%%                       %%             %\n" +
		"%%%%%%            %%%%%%%                       %%             %\n" +
		"%%%%%%            %%%%%%%                       %%            %%\n" +
		"%%%%%%             %%%%%%%     %%%%%%           %%          %%%%\n" +
		"%%%%%%             %%%%%%%%%   %%%%%%           %%%%%%%%%%%%%%%%\n" +
		"%%%%%%             %%%%%%%%%   %%%%%%           %%%%%%%%%%%%%%%%\n" +
		"%%%%%%           %%%%%%%%%%%   %%%%%%           %%%%%%%%%%%%%%%%\n" +
		"%%%%%           %%%%%%%%%%%%   %%%%%%           %%%%%%%%%%%%%%%%\n" +
		"%%%%%           %%%    %%%%%   %%%%%%           %%%%%%%%%%%%%%%%\n" +
		"%%%%%                  %%%%%   %%%%%%           %%%%%%%%%%%%%%%%\n" +
		"%%%%%                  %%%%%   %%%%%%           %%%%%%%%%%%%%%%%\n" +
		"%%%%%                  %%%%%   %%%%%%           %%%%%%%%%%%%%%%%\n" +
		"%%%%%                  %%%%%              %%    %%%%%%%%%%%%%%%%\n" +
		"%%%%%                  %%%%%              %%%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%%                   %%%%%              %%%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%                                        %%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%                                        %%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%                                        %%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%                                        %%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%                                        %%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%                                        %%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%                                        %%%%%%%%%%%%%%%%%%%%%\n" +
		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%";
	/** Map choice 2. */
	public static final String LEVEL_2 =
		"################################################################\n" +
		"########                 ####             ###        ###########\n" +
		"########                 ####                        ###########\n" +
		"########                                               #########\n" +
		"########                                                ########\n" +
		"########                                               #########\n" +
		"########                                               #########\n" +
		"#########                                               ########\n" +
		"###########          ###                               #########\n" +
		"###########          ###                              ##########\n" +
		"###########          ###                              ##########\n" +
		"#####                ###                              ##########\n" +
		"#####                ###                              ##########\n" +
		"#####                ###                              ##########\n" +
		"#####                ###############     #######################\n" +
		"########             ###############     #######################\n" +
		"########             ###############     #######################\n" +
		"########             ###############     #######################\n" +
		"########             #########  ####     #######################\n" +
		"########                                 #######################\n" +
		"########                                 #######################\n" +
		"########                                 #######################\n" +
		"########                                 #######################\n" +
		"##############        #######    ###############################\n" +
		"##############      #########    ###############################\n" +
		"##############      #########    ###############################\n" +
		"##############      #########    ###############################\n" +
		"##############      #########    ###############################\n" +
		"#############         #####        #############################\n" +
		"#############                     #############      ###########\n" +
		"#############                      ############       ##########\n" +
		"#############                                                  #\n" +
		"#############         #####                                    #\n" +
		"#############         #####                                    #\n" +
		"#############         ################  #######                #\n" +
		"#############         ################  #######                #\n" +
		"#############                                                  #\n" +
		"#############                                                  #\n" +
		"#############                                                  #\n" +
		"#############                                                  #\n" +
		"#############                                                  #\n" +
		"#############                                                  #\n" +
		"##############                                                 #\n" +
		"################                                               #\n" +
		"################                                               #\n" +
		"################            #       #####################      #\n" +
		"################            #       ############################\n" +
		"###################                     ########################\n" +
		"###################                      #######################\n" +
		"###################                      #######################\n" +
		"###################                      #######################\n" +
		"###################                      #######################\n" +
		"###################     ####             #######################\n" +
		"###################     ####              ######################\n" +
		"###################     ####              ######################\n" +
		"###################                       ######################\n" +
		"###################                      #######################\n" +
		"###################                      #######################\n" +
		"###################       #              #######################\n" +
		"###################     ####             #######################\n" +
		"###################     ####             #######################\n" +
		"###################     ####             #######################\n" +
		"###################                      #######################\n" +
		"################################################################";
	

	/** A list of all loaded levels as 2d tile arrays. */
	private final Tile[][][] levels = {Level.fromString(Level.LEVEL_0),
									   Level.fromString(Level.LEVEL_1),
									   Level.fromString(Level.LEVEL_2)};


	/** The integer choice of this level. */
	private int l;
	/** The current 2d tile array representing this level. */
	private Tile[][] level;


	/**
	 * Constructs a new {@code Level} object from one of the pre-defined levels.
	 *
	 * @param l  the level number corresponding to one of the {@code LEVEL_*} strings in this class.
	 *
	 * @throws IllegalArgumentException  if {@code m} does not represent an existing level.
	 */
	public Level(int l) {
	    if (l < 0 || l > this.levels.length)
			throw new IllegalArgumentException(l + " out of bounds for length " +
											   this.levels.length);

		this.l = l;
		this.level = this.levels[l];
	}


	/**
	 * Returns the integer choice of this level. The returned integer is {@code l} such that
	 * {@code new Level(l).equals(this) == true}.
	 *
	 * @return the integer choice of this level.
	 */
	public int toInteger() {
		return this.l;
	}


	/**
	 * Loads a level as a 2d tile array from a string representation. The string form of a level
	 * is defined as:
	 * <ul>
	 * <li> Rows are separated by a newline character "\n"
	 * <li> A single character represents one tile
	 * <li> All rows are the same length
	 * <li> The pound sign "#" represents a WALL
	 * <li> The space " " represents NONE
	 * <li> The capital c "C" represents a CRATE
	 * </ul>
	 *
	 * @param str  the string to parse as a 2d tile array.
	 *
	 * @return the parsed 2d tile array. If any character is not recognized but the string
	 *         is otherwise valid, then the unknown char is assumed to be a NONE tile.
	 *
	 * @throws NullPointerException      if {@code str == null}.
	 * @throws IllegalArgumentException  if the given string is empty.
	 * @throws IllegalArgumentException  if any row is empty.
	 * @throws IllegalArgumentException  if any two rows have a different number of columns.
	 */
	public static Tile[][] fromString(String str) {
		if (str == null)
			throw new NullPointerException();

		// Get the list of rows
		String[] rows = str.split("\n");
		int numRows = rows.length;
		if (numRows == 0)
			throw new IllegalArgumentException("zero rows given: " + str);

		// Get the number of columns in each row from the first row, which is
		// assumed to be a constant
		int numCols = rows[0].length();
		if (numCols == 0)
			throw new IllegalArgumentException("zero cols given in row 0");

		// Generate the 2d tile array from the characters is each row. If any row is found to
		// have a different length, an error is thrown
		Tile[][] level = new Tile[numRows][numCols];
		for (int r = 0; r < rows.length; r++) {
			String strRow = rows[r];
			Tile[] tileRow = new Tile[numCols];

			if (strRow.length() != numCols)
				throw new IllegalArgumentException("improper row length in row " + r);
			for (int c = 0; c < numCols; c++) {
			    Tile tile;
				switch (strRow.charAt(c)) {
				case ' ' -> tile = Tile.NONE;
				case '#' -> tile = Tile.WALL_STONE;
				case '%' -> tile = Tile.WALL_SAND;
				case 'C' -> tile = Tile.CRATE;
				default -> tile = Tile.NONE;
				}
				tileRow[c] = tile;
			}

			level[r] = tileRow;
		}

		return level;
	}


	/**
	 * Gets the tile at a specific row and column.
	 *
	 * @param r  the row.
	 * @param c  the column.
	 *
	 * @return the {@code Tile} object on the level at the specified location.
	 *
	 * @throws IndexOutOfBoundsException  if {@code r} or {@code c} are not in the bounds
	 *                                    of the level size.
	 */
	public Tile get(int r, int c) {
		if (r < 0 || r >= this.level.length || c < 0 || c >= this.level[r].length)
			throw new IndexOutOfBoundsException("position out of bounds");
		
		return this.level[r][c];
	}


	/**
	 * Determines if the specified tile is filled by a non-passable block.
	 *
	 * @param r  the row.
	 * @param c  the column.
	 *
	 * @return whether the specified tile is filled by a non-passable block.
	 *
	 * @throws IndexOutOfBoundsException  if {@code r} or {@code c} are not in the bounds
	 *                                    of the level size.
	 */
	public boolean isFilled(int r, int c) {
		if (r < 0 || r >= this.level.length || c < 0 || c >= this.level[r].length)
			throw new IndexOutOfBoundsException("position out of bounds");
		
		return this.level[r][c] != Tile.NONE;
	}


	/**
	 * Returns the number of rows in this level.
	 *
	 * @return the number of rows in this level.
	 */
	public int rows() {
		return this.level.length;
	}


	/**
	 * Returns the number of columns in a given row.
	 *
	 * @param r  the row to get the number of columns in.
	 *
	 * @return the number of columns in the given row.
	 *
	 * @throws IndexOutOfBoundsException  if {@code r} is not in bounds of the size of the level.
	 */
	public int cols(int r) {
		if (r < 0 || r >= this.level.length)
			throw new IndexOutOfBoundsException(r + " out of bounds for len" + this.level.length);
		return this.level[r].length;
	}


	/**
	 * Determines if an object moving with some velocity will collide with any solid tile
	 * on the level in the next frame.
	 *
	 * @param moveable  the {@code Moveable} object to check collision with.
	 * @param vx        the x component of the velocity that the object will have.
	 * @param vy        the y component of the velocity that the object will have.
	 *
	 * @return {@code true} if the object will collide with any solid tile on the level with
	 *         its current position and given velocity.
	 *
	 * @throws IllegalArgumentException  if the size of the object as determined by
	 *                                   {@code Moveable::getSize()} is greater than 1 tile.
	 * @throws IllegalArgumentException  if the position of the object is not on the level.
	 */
	public boolean collides(Moveable moveable, double vx, double vy) {
		// Get the top-left position of the object
		double x1 = moveable.getX() + vx;
		double y1 = moveable.getY() + vy;
		double size = moveable.getSize();
		
		if (size > 1)
			throw new IllegalArgumentException("size " + size + " is greater than 1 tile");

		// Get bottom-right coordinate
	    double x2 = x1 + size;
		double y2 = y1 + size;

		// All possible tiles the object could be colliding with, depending on size
		Point currentTile = new Point((int) x1, (int) y1);
		Point rightTile = new Point((int) x2, (int) y1);
		Point downTile = new Point((int) x1, (int) y2);
		Point cornerTile = new Point((int) x2, (int) y2);

		// Check bounds
		if (currentTile.y < 0 || currentTile.y >= this.level.length ||
			currentTile.x < 0 || currentTile.x >= this.level[currentTile.y].length)
			throw new IllegalArgumentException("position " + currentTile + " out of bounds");

		// Check for a collision
		boolean collidesCurrent = this.level[currentTile.y][currentTile.x] != Tile.NONE;
	    boolean collidesRight = this.level[rightTile.y][rightTile.x] != Tile.NONE;
		boolean collidesDown = this.level[downTile.y][downTile.x] != Tile.NONE;
		boolean collidesCorner = this.level[cornerTile.y][cornerTile.x] != Tile.NONE;

		return collidesCurrent || collidesRight || collidesDown || collidesCorner;
	}

}
