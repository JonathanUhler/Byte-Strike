package item;


import interfaces.Weapon;


/**
 * Represents a sniper.
 */
public class Sniper extends Weapon {

	/**
	 * Constructs a new {@code Sniper} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: 115 hp
	 * <li> Velocity: 0.90 tiles/frame
	 * <li> Penetration: 100% base damage against armor
	 * <li> Firerate: 1 round/second
	 * <li> Range: infinite tiles
	 * <li> Recoil 0.300 radians
	 * <li> Mobility: 70% of normal walking speed
	 * <li> Capacity: 1 round
	 * <li> Reload Time: 3.7 seconds
	 * <li> Cost: $2500
	 * <li> $/Kill: $25
	 * <li> Barrel: 0.89 tiles
	 * </ul>
	 */
	public Sniper() {
		super(115, 0.90, 1, 1000.0, 0.300, 0.70, 1, 3700, 2500, 25, 0.89);
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
