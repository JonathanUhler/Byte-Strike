package item;


import interfaces.Weapon;


/**
 * Represents a pistol.
 */
public class Pistol extends Weapon {

	/**
	 * Constructs a new {@code Pistol} object. This weapon has the following properties:
	 * <ul>
	 * <li> Damage: 10 hp
	 * <li> Velocity: 0.5 tile/frame
	 * <li> Firerate: 4 rounds/second
	 * <li> Range: 4 tiles
	 * <li> Recoil 0.060 radians (about 3.4 degrees)
	 * <li> Mobility: 95% of normal walking speed
	 * <li> Capacity: 12 rounds
	 * <li> Reload Time: 1.4 seconds
	 * </ul>
	 */
	public Pistol() {
	    super(10, 0.5, 4, 4.0, 0.060, 0.95, 12, 1400);
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
