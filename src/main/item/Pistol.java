package item;


import interfaces.Weapon;


/**
 * Represents a pistol.
 */
public class Pistol extends Weapon {

	/**
	 * Constructs a new {@code Pistol} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: 24 hp
	 * <li> Velocity: 0.42 tiles/frame
	 * <li> Penetration: 52% base damage against armor
	 * <li> Firerate: 6 rounds/second
	 * <li> Range: 5 tiles
	 * <li> Recoil 0.060 radians
	 * <li> Mobility: 95% of normal walking speed
	 * <li> Capacity: 12 rounds
	 * <li> Reload Time: 2.3 seconds
	 * <li> Cost: $100
	 * <li> $/Kill: $75
	 * <li> Barrel: 0.36 tiles
	 * </ul>
	 */
	public Pistol() {
	    super(24, 0.42, 6, 5, 0.060, 0.95, 20, 2300, 100, 75, 0.36);
	}


	/**
	 * Returns the type of this weapon. This method always returns {@code "Pistol"}.
	 *
	 * @return the string literal {@code "Pistol"}.
	 */
	@Override
	public String getType() {
		return "Pistol";
	}

}
