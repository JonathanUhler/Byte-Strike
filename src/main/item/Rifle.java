package item;


import interfaces.Weapon;


/**
 * Represents a classic rifle in any shooter game.
 */
public class Rifle extends Weapon {

	/**
	 * Constructs a new {@code Rifle} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: 31 hp
	 * <li> Velocity: 0.60 tiles/frame
	 * <li> Penetration: 70% base damage against armor
	 * <li> Firerate: 11 rounds/second
	 * <li> Range: 7 tiles
	 * <li> Recoil 0.075 radians
	 * <li> Mobility: 80% of normal walking speed
	 * <li> Capacity: 30 rounds
	 * <li> Reload Time: 3.1 seconds
	 * <li> Cost: $1300
	 * <li> $/Kill: $100
	 * <li> Barrel: 0.9 tiles
	 * </ul>
	 */
	public Rifle() {
	    super(25, 0.60, 11, 7.0, 0.075, 0.80, 30, 3100, 1300, 100, 0.9);
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
