package item;


import interfaces.Weapon;


/**
 * Represents a sub-machine gun.
 */
public class SMG extends Weapon {

	/**
	 * Constructs a new {@code SMG} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: 25 hp
	 * <li> Velocity: 0.40 tiles/frame
	 * <li> Penetration: 50% base damage against armor
	 * <li> Firerate: 13 rounds/second
	 * <li> Range: 4 tiles
	 * <li> Recoil 0.070 radians
	 * <li> Mobility: 90% of normal walking speed
	 * <li> Capacity: 30 rounds
	 * <li> Reload Time: 2.6 seconds
	 * <li> Cost: $700
	 * <li> $/Kill: $150
	 * <li> Barrel: 0.65 tiles
	 * </ul>
	 */
	public SMG() {
	    super(25, 0.40, 13, 4, 0.070, 0.90, 30, 2600, 700, 150, 0.65);
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
