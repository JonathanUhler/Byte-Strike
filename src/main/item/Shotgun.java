package item;


import interfaces.Weapon;


/**
 * Represents a shotgun.
 */
public class Shotgun extends Weapon {

	/**
	 * Constructs a new {@code Shotgun} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: 100 hp (on close, but severe range restrictions)
	 * <li> Velocity: 0.65 tile/frame
	 * <li> Firerate: 1 round/second
	 * <li> Range: 1.5 tiles
	 * <li> Recoil 0.100 radians (about 5.7 degrees)
	 * <li> Mobility: 95% of normal walking speed
	 * <li> Capacity: 2 rounds
	 * <li> Reload Time: 2.5 seconds
	 * <li> Bullets Per Shot: 10 bullets
	 * <li> Cost: $1250.
	 * </ul>
	 */
	public Shotgun() {
	    super(100, 0.65, 1, 1.5, 0.100, 0.95, 2, 2500, 1250, 10);
	}


	/**
	 * Returns the type of this weapon. This method always returns {@code "Shotgun"}.
	 *
	 * @return the string literal {@code "Shotgun"}.
	 */
	@Override
	public String getType() {
		return "Shotgun";
	}

}
