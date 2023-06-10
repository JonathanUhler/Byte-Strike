package item;


import interfaces.Weapon;


/**
 * Represents a shotgun.
 */
public class Shotgun extends Weapon {

	/**
	 * Constructs a new {@code Shotgun} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: 19 hp/bullet, 171 hp possible per shot
	 * <li> Velocity: 0.40 tiles/frame
	 * <li> Penetration: 50% base damage against armor
	 * <li> Firerate: 1 round/second
	 * <li> Range: 1.5 tiles
	 * <li> Recoil 0.100 radians
	 * <li> Mobility: 95% of normal walking speed
	 * <li> Capacity: 2 rounds
	 * <li> Bullets/Shot: 9
	 * <li> Reload Time: 4.6 seconds
	 * <li> Cost: $850
	 * <li> $/Kill: $225
	 * <li> Barrel: 0.94 tiles
	 * </ul>
	 */
	public Shotgun() {
	    super(19, 0.40, 1, 1.5, 0.100, 0.95, 2, 9, 4600, 850, 225, 0.94);
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
