package item;


import interfaces.Weapon;


/**
 * Represents a classic rifle in any shooter game.
 */
public class Rifle extends Weapon {

	/**
	 * Constructs a new {@code Rifle} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: 25 hp
	 * <li> Velocity: 0.8 tile/frame (about 16 m/s in real life)
	 * <li> Firerate: 6 rounds/second
	 * <li> Range: 7 tiles
	 * <li> Recoil 0.075 radians (about 4.3 degrees)
	 * <li> Mobility: 85% of normal walking speed
	 * <li> Capacity: 24 rounds
	 * <li> Reload Time: 1.7 seconds
	 * <li> Cost: $750.
	 * </ul>
	 */
	public Rifle() {
	    super(25, 0.8, 6, 7.0, 0.075, 0.85, 24, 1700, 750);
	}


	/**
	 * Returns the type of this weapon. This method always returns {@code "Rifle"}.
	 *
	 * @return the string literal {@code "Rifle"}.
	 */
	@Override
	public String getType() {
		return "Rifle";
	}

}
