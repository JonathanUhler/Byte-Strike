package item;


import interfaces.Weapon;


/**
 * Represents a sniper.
 */
public class Sniper extends Weapon {

	/**
	 * Constructs a new {@code Sniper} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: _ hp
	 * <li> Velocity: _ tile/frame
	 * <li> Firerate: _ rounds/second
	 * <li> Range: 1000.0 tiles
	 * <li> Recoil _ radians (about _ degrees)
	 * <li> Mobility: _% of normal walking speed
	 * <li> Capacity: _ rounds
	 * <li> Reload Time: _ seconds
	 * </ul>
	 */
	public Sniper() {
	    super(100, 1, 1, 1000.0, 0.300, 0.65, 1, 3000);
	}


	/**
	 * Returns the type of this weapon. This method always returns {@code "Sniper"}.
	 *
	 * @return the string literal {@code "Sniper"}.
	 */
	@Override
	public String getType() {
		return "Sniper";
	}

}
