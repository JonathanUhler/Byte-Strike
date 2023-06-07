package item;


import interfaces.Weapon;


/**
 * Represents a sub-machine gun.
 */
public class SMG extends Weapon {

	/**
	 * Constructs a new {@code SMG} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: _ hp
	 * <li> Velocity: _ tile/frame
	 * <li> Firerate: _ rounds/second
	 * <li> Range: _ tiles
	 * <li> Recoil _ radians (about _ degrees)
	 * <li> Mobility: _% of normal walking speed
	 * <li> Capacity: _ rounds
	 * <li> Reload Time: _ seconds
	 * </ul>
	 */
	public SMG() {
	    super(0, 0, 0, 0, 0, 0, 0, 0, 0);
	}


	/**
	 * Returns the type of this weapon. This method always returns {@code "SMG"}.
	 *
	 * @return the string literal {@code "SMG"}.
	 */
	@Override
	public String getType() {
		return "SMG";
	}

}
