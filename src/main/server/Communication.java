package server;


import entity.Player;
import entity.Bullet;
import interfaces.Item;
import java.util.Map;
import java.util.HashMap;


/**
 * Facilitates communication over the network with strings and maps.
 * <p>
 * All command-generation methods follow some general rules:
 * <ul>
 * <li> A {@code null} value is returned if any {@code Object} argument is {@code null}.
 * </ul>
 *
 * @author Jonathan Uhler
 */
public class Communication {

	/** Value indicating a player movement. */
	public static final String OPCODE_MOVE = "move";
	/** Value indicating the use of a player's weapon. */
	public static final String OPCODE_SHOOT = "shoot";
	/** Value indicating the new position of a player. */
	public static final String OPCODE_POS_PLAYER = "pos_player";
	/** Value indicating the creation of a new bullet. */
	public static final String OPCODE_NEW_BULLET = "pos_bullet";
	/** Value indicating the addition of a new player to the game. */
	public static final String OPCODE_JOIN = "join";
	/** Value indicating the removal of an existing player from the game. */
	public static final String OPCODE_LEAVE = "leave";
	/** Value indicating that a player was hurt. */
	public static final String OPCODE_DAMAGED = "damaged";
	/** Value indicating that a player's condition was reset. */
	public static final String OPCODE_RESET = "reset";
	/** Value indicating the purchase of a new weapon. */
	public static final String OPCODE_BUY = "buy";
	/** Value indicating the gifting of money to a player. */
	public static final String OPCODE_PAY = "pay";
	/** Value indicating the usage of an item. */
	public static final String OPCODE_USE = "use";
	/** Key indicating the type of command sent. */
	public static final String KEY_OPCODE = "opcode";
	/** Key indicating whether a player is attempting to move up. */
	public static final String KEY_UP = "up";
	/** Key indicating whether a player is attempting to move left. */
	public static final String KEY_LEFT = "left";
	/** Key indicating whether a player is attempting to move down. */
	public static final String KEY_DOWN = "down";
	/** Key indicating whether a player is attempting to move right. */
	public static final String KEY_RIGHT = "right";
	/** Key indicating the UID of a player. */
	public static final String KEY_ID = "id";
	/** Key indicating the bullet number in a sequence of bullets fired by the same weapon. */
	public static final String KEY_BULLET_NUM = "bullet_num";
	/** Key indicating the x position of an entity. */
	public static final String KEY_X = "x";
	/** Key indicating the y position of an entity. */
	public static final String KEY_Y = "y";
	/** Key indicating the rotation, in radians on the unit circle, of an entity. */
	public static final String KEY_RAD = "rad";
	/** Key indicating a type of item, as defined by {@code Item::getType()}. */
	public static final String KEY_ITEM = "item";
	/** Key indicating the number of the current level being played on. */
	public static final String KEY_LEVEL = "level";
	/** Key indicating a numerical amount of damage. */
	public static final String KEY_DMG = "dmg";
	/** Key indicating a numerical amount of money. */
	public static final String KEY_MONEY = "money";
	/** Key indicating the number of an item being used. */
	public static final String KEY_ITEM_NUM = "item_num";


	/**
	 * This class cannot be constructed.
	 */
	private Communication() { }
	

	/**
	 * Generates the payload for a movement command. 
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code move}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code up}
	 *   <td style="border: 1px solid black"> Whether the player is attempting to move up, as
	 *                                        a stringified boolean.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code left}
	 *   <td style="border: 1px solid black"> Whether the player is attempting to move left, as
	 *                                        a stringified boolean.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code down}
	 *   <td style="border: 1px solid black"> Whether the player is attempting to move down, as
	 *                                        a stringified boolean.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code left}
	 *   <td style="border: 1px solid black"> Whether the player is attempting to move right, as
	 *                                        a stringified boolean.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code rad}
	 *   <td style="border: 1px solid black"> The rotation, in radians on the unit circle, of the
	 *                                        player.
	 *  </tr>
	 * </table>
	 *
	 * @param up     whether the player is moving up.
	 * @param left   whether the player is moving left.
	 * @param down   whether the player is moving down.
	 * @param right  whether the player is moving right.
	 * @param rad    the rotation of the player sprite.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdMove(boolean up,
											  boolean left,
											  boolean down,
											  boolean right,
											  double rad)
	{
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_MOVE);
		map.put(Communication.KEY_UP, Boolean.toString(up));
		map.put(Communication.KEY_LEFT, Boolean.toString(left));
		map.put(Communication.KEY_DOWN, Boolean.toString(down));
		map.put(Communication.KEY_RIGHT, Boolean.toString(right));
		map.put(Communication.KEY_RAD, Double.toString(rad));
		return map;
	}



	/**
	 * Generates the payload for a shoot command. The information for bullet type and direction
	 * is taken from the latest {@code move} command recieved from the player and the weapon
	 * they have equipped.
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code shoot}.
	 *  </tr>
	 * </table>
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdShoot() {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_SHOOT);
		return map;

	}


	/**
	 * Generates the payload for a player position command. 
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code pos_player}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code id}
	 *   <td style="border: 1px solid black"> The UID of the player.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code x}
	 *   <td style="border: 1px solid black"> The x position of the player.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code y}
	 *   <td style="border: 1px solid black"> The y position of the player.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code rad}
	 *   <td style="border: 1px solid black"> The rotation, in radians on the unit circle, of the
	 *                                        player.
	 *  </tr>
	 * </table>
	 *
	 * @param player    the player object that has moved.
	 * @param playerId  the UID of the player.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdPosPlayer(Player player, int playerId) {
		if (player == null)
			return null;
		
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_POS_PLAYER);
		map.put(Communication.KEY_ID, Integer.toString(playerId));
	    map.put(Communication.KEY_X, Double.toString(player.getX()));
		map.put(Communication.KEY_Y, Double.toString(player.getY()));
		map.put(Communication.KEY_RAD, Double.toString(player.getRad()));
		return map;
	}


    /**
	 * Generates the payload for a new bullet command. The information in this command
	 * defines the initial conditions of the bullet. From that, it is up to the client
	 * to accurately simulate the path of the bullet, although the final decision for collisions
	 * is given by the server, not the clients.
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code pos_bullet}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code x}
	 *   <td style="border: 1px solid black"> The x position of the bullet.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code y}
	 *   <td style="border: 1px solid black"> The y position of the bullet.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code rad}
	 *   <td style="border: 1px solid black"> The rotation, in radians on the unit circle, of the
	 *                                        bullet.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code bullet_num}
	 *   <td style="border: 1px solid black"> The number of the bullet in a sequence of multiple
	 *                                        bullets fired by the same weapon, starting at 0.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code id}
	 *   <td style="border: 1px solid black"> The UID of the player that fired the bullet. This
	 *                                        can be used by the client to determine the weapon
	 *                                        type and other information.
	 *  </tr>
	 * </table>
	 *
	 * @param bullet    the bullet object that was created.
	 * @param bulletId  the number of the bullet in a sequence of multiple bullets fired by the
	 *                  same weapon.
	 * @param playerId  the UID of the player that fired the bullet.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdNewBullet(Bullet bullet, int bulletId, int playerId) {
		if (bullet == null)
			return null;
		
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_NEW_BULLET);
		map.put(Communication.KEY_X, Double.toString(bullet.getX()));
		map.put(Communication.KEY_Y, Double.toString(bullet.getY()));
		map.put(Communication.KEY_RAD, Double.toString(bullet.getRad()));
		map.put(Communication.KEY_BULLET_NUM, Integer.toString(bulletId));
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		return map;
	}


	/**
	 * Generates the payload for a player join command. Client should determine whether this
	 * command is intended for them to initialize a player state depending on if that client's
	 * current id is {@code -1}. Ultimately, all final information is determined by the server
	 * in case of a malfunctioning or malicious client.
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code join}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code id}
	 *   <td style="border: 1px solid black"> The UID of the new player.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code x}
	 *   <td style="border: 1px solid black"> The x position of the new player.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code y}
	 *   <td style="border: 1px solid black"> The y position of the new player.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code level}
	 *   <td style="border: 1px solid black"> The number of the level currently being played on.
	 *  </tr>
	 * </table>
	 *
	 * @param playerId  the UID of the new player.
	 * @param x         the x position of the new player.
	 * @param y         the y position of the new player.
	 * @param levelId   the integer representing the current level being played on.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdJoin(int playerId, double x, double y, int levelId) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_JOIN);
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		map.put(Communication.KEY_X, Double.toString(x));
		map.put(Communication.KEY_Y, Double.toString(y));
		map.put(Communication.KEY_LEVEL, Integer.toString(levelId));
		return map;
	}



	/**
	 * Generates the payload for a player leave command.
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code leave}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code id}
	 *   <td style="border: 1px solid black"> The UID of the new player.
	 *  </tr>
	 * </table>
	 *
	 * @param playerId  the UID of the departing player.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdLeave(int playerId) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_LEAVE);
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		return map;
	}



	/**
	 * Generates the payload for a player damaged command.
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code leave}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code id}
	 *   <td style="border: 1px solid black"> The UID of the hit player.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code dmg}
	 *   <td style="border: 1px solid black"> The integer amount of damage dealt.
	 *  </tr>
	 * </table>
	 *
	 * @param playerId  the UID of the hit player.
	 * @param dmg       the amount of damage dealt.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdDamaged(int playerId, int dmg) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_DAMAGED);
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		map.put(Communication.KEY_DMG, Integer.toString(dmg));
		return map;
	}




	/**
	 * Generates the payload for a player reset command.
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code reset}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code id}
	 *   <td style="border: 1px solid black"> The UID of the player that is being reset.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code x}
	 *   <td style="border: 1px solid black"> The new x position of the reset player.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code y}
	 *   <td style="border: 1px solid black"> The new y position of the reset player.
	 *  </tr>
	 * </table>
	 *
	 * @param playerId  the UID of the hit player.
	 * @param x         the new x position of the player.
	 * @param y         the new y position of the player.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdReset(int playerId, double x, double y) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_RESET);
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		map.put(Communication.KEY_X, Double.toString(x));
		map.put(Communication.KEY_Y, Double.toString(y));
		return map;
	}


	/**
	 * Generates the payload for a shop purchase command.
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code buy}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code item}
	 *   <td style="border: 1px solid black"> The string type of the item being purchased.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code id}
	 *   <td style="border: 1px solid black"> The UID of the player that bought the item. This
	 *                                        field is ignored for client to server communication,
	 *                                        but is used when the server broadcasts the new item
	 *                                        to all other clients.
	 *  </tr>
	 * </table>
	 *
	 * @param item      the item purchased.
	 * @param playerId  the UID of the player buying the item.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdBuy(Item item, int playerId) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_BUY);
		map.put(Communication.KEY_ITEM, item.getType());
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		return map;
	}


	/**
	 * Generates the payload for a player payment command.
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code pay}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code money}
	 *   <td style="border: 1px solid black"> The amount of money given to the player.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code id}
	 *   <td style="border: 1px solid black"> The UID of the player.
	 *  </tr>
	 * </table>
	 *
	 * @param money     the amount of money given to the player.
	 * @param playerId  the UID of the player.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdPay(int money, int playerId) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_PAY);
		map.put(Communication.KEY_MONEY, Integer.toString(money));
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		return map;
	}


	/**
	 * Generates the payload for an item usage command.
	 * <p>
	 * This command is comprised of the following components:
	 * <table style="border: 1px solid black">
	 *  <caption>{@code color} Command Payload</caption>
	 *  <tr style="border: 1px solid black">
	 *   <th style="border: 1px solid black"> Key
	 *   <th style="border: 1px solid black"> Commentary
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code opcode}
	 *   <td style="border: 1px solid black"> Identifies this command, always {@code use}.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code item_num}
	 *   <td style="border: 1px solid black"> The number of the item being used.
	 *  </tr>
	 *  <tr style="border: 1px solid black">
	 *   <td style="border: 1px solid black"> {@code id}
	 *   <td style="border: 1px solid black"> The UID of the player using the item.
	 *  </tr>
	 * </table>
	 *
	 * @param itemNum   the number key of the item being used.
	 * @param playerId  the UID of the player using the item.
	 *
	 * @return the command payload.
	 */
	public static Map<String, String> cmdUse(int itemNum, int playerId) {
	    Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_USE);
		map.put(Communication.KEY_ITEM_NUM, Integer.toString(itemNum));
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		return map;
	}
	

	/**
	 * Serializes a {@code Map} to a {@code String}. The keys and values of the map are
	 * permitted to contain any character that can be successfully passed over a network
	 * socket. Commas, colons, and double-quotes are allowed and will be managed accordingly
	 * by the {@code deserialize} method.
	 * <p>
	 * This method does not rely on the {@code Map.toString} method. The syntax of the returned
	 * string is as follows:
	 * <ul>
	 * <li> The string begins and ends with curly brackets
	 * <li> All keys and values, which must be strings, are enclosed within literal double-quotes
	 * <li> Any double-quotes already present in the keys and values are converted to the
	 *      literal string '\"' (that is, the value {@code \\\\\"} is inserted to replace any 
	 *      {@code \"})
	 * <li> Keys are separated from values with a single colon, and without any whitespace
	 * <li> Map entries are separated with a single comma, and without any whitespace
	 * </ul>
	 * <p>
	 * An example string for the map {@code {key=value, ke"y=value}}, when printed to
	 * the standard output,  would be:
	 * <p>
	 * {@code '{"key":"value","ke\"y"="value"}'}
	 *
	 * @param map  the {@code Map} to serialize.
	 *
	 * @return the serialized string.
	 *
	 * @see deserialize
	 */
	public static String serialize(Map<String, String> map) {
		String str = "{";

		// Add each key and value pair
		for (String key : map.keySet()) {
			String value = map.get(key);

			// Replace all quote literals in the strings with a literal \" to prevent
			// confusion when deserializing
			key = key.replaceAll("\"", "\\\\\"");
			value = value.replaceAll("\"", "\\\\\"");

			// Add the key and value between double quotes (since all elements are strings).
			str += "\"" + key + "\":\"" + value + "\",";
		}

		// Remove the last comma added
		if (str.endsWith(","))
			str = str.substring(0, str.length() - 1);

		// Close the map and return
		str += "}";
		return str;
	}
	

	/**
	 * Deserializes a {@code Map} from a {@code String}.
	 *
	 * @param str  the string to deserialize.
	 *
	 * @return the deserialized map.
	 *
	 * @see serialize
	 */
	public static Map<String, String> deserialize(String str) {
		Map<String, String> map = new HashMap<>();

		// Remove the leading and trailing curly brackets
		str = str.substring(1, str.length() - 1);

		// Add the map entrys, going through each character to ensure no mistakes in case
		// the elements of any key or value includes the " or , or : delimiters
		String key = "";
		String temp = "";
		char prev = 0x00;
		boolean inQuotes = false;
		for (char c : str.toCharArray()) {
			// Quotes begin or end if the character is a quote and the previous character was
			// not the \ to indicate this quote is a literal.
			if (c == '"' && prev != '\\')
				inQuotes = !inQuotes;
			else if (c == ':' && !inQuotes) {
				key = temp;
				temp = "";
			}
			else if (c == ',' && !inQuotes) {
				// Undo the quote \" literal operation from the serialize method
				key = key.replaceAll("\\\\\"", "\"");
				temp = temp.replaceAll("\\\\\"", "\"");
				
				map.put(key, temp);
				key = "";
				temp = "";
			}
			else
				temp += c;
			
			prev = c;
		}

		// Add the final entry created
		if (key.length() > 0) {
			key = key.replaceAll("\\\\\"", "\"");
			temp = temp.replaceAll("\\\\\"", "\"");
			map.put(key, temp);
		}

		// Return the completed map
		return map;
	}

}
