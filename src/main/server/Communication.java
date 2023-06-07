package server;


import entity.Player;
import entity.Bullet;
import java.util.Map;
import java.util.HashMap;


/**
 * Facilitates communication over the network with strings and maps.
 *
 * @author Jonathan Uhler
 */
public class Communication {

	public static final String OPCODE_MOVE = "move";
	public static final String OPCODE_SHOOT = "shoot";
	public static final String OPCODE_POS_PLAYER = "pos_player";
	public static final String OPCODE_NEW_BULLET = "pos_bullet";
	public static final String OPCODE_JOIN = "join";
	public static final String OPCODE_LEAVE = "leave";
	public static final String OPCODE_DAMAGED = "damaged";
	public static final String OPCODE_RESET = "reset";
	public static final String KEY_OPCODE = "opcode";
	public static final String KEY_UP = "up";
	public static final String KEY_LEFT = "left";
	public static final String KEY_DOWN = "down";
	public static final String KEY_RIGHT = "right";
	public static final String KEY_ID = "id";
	public static final String KEY_X = "x";
	public static final String KEY_Y = "y";
	public static final String KEY_RAD = "rad";
	public static final String KEY_WEAPON = "weapon";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_DMG = "dmg";


	private Communication() { }
	

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


	public static Map<String, String> cmdShoot() {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_SHOOT);
		return map;

	}


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


	public static Map<String, String> cmdNewBullet(Bullet bullet, int playerId) {
		if (bullet == null)
			return null;
		
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_NEW_BULLET);
		map.put(Communication.KEY_X, Double.toString(bullet.getX()));
		map.put(Communication.KEY_Y, Double.toString(bullet.getY()));
		map.put(Communication.KEY_RAD, Double.toString(bullet.getRad()));
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		return map;
	}


	public static Map<String, String> cmdJoin(int playerId, double x, double y, int levelId) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_JOIN);
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		map.put(Communication.KEY_X, Double.toString(x));
		map.put(Communication.KEY_Y, Double.toString(y));
		map.put(Communication.KEY_LEVEL, Integer.toString(levelId));
		return map;
	}


	public static Map<String, String> cmdLeave(int playerId) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_LEAVE);
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		return map;
	}


	public static Map<String, String> cmdDamaged(int playerId, int dmg) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_DAMAGED);
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		map.put(Communication.KEY_DMG, Integer.toString(dmg));
		return map;
	}


	public static Map<String, String> cmdReset(int playerId, double x, double y) {
		Map<String, String> map = new HashMap<>();
		map.put(Communication.KEY_OPCODE, Communication.OPCODE_RESET);
		map.put(Communication.KEY_ID, Integer.toString(playerId));
		map.put(Communication.KEY_X, Double.toString(x));
		map.put(Communication.KEY_Y, Double.toString(y));
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
