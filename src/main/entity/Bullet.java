package entity;


import interfaces.Moveable;
import interfaces.Weapon;


/**
 * Represents a projectile fired from a weapon.
 *
 * @author Jonathan Uhler
 */
public class Bullet extends Moveable {

	/** The origin x position of this bullet. */
	private double startX;
	/** The origin y position of this bullet. */
	private double startY;
	/** The weapon which fired this bullet. */
	private Weapon origin;
	

	/**
	 * Constructs a new {@code Bullet} object.
	 *
	 * @param x       the x position of the origin of this projectile.
	 * @param y       the y position of the origin of this projectile.
	 * @param rad     the angle of the velocity vector of this projectile.
	 * @param origin  the weapon which fired this bullet. This parameter is used to determine
	 *                the velocity and other information about the projectile.
	 */
	public Bullet(double x, double y, double rad, Weapon origin) {
	    super(x, y, 0.1);

		double vx = origin.velocity() * Math.cos(rad);
		double vy = origin.velocity() * Math.sin(rad);
		if (origin.roundsPerShot() > 1) {
			double vVariance = 0.05;
			vx += Math.random() * (vVariance - (-vVariance)) + (-vVariance);
			vy += Math.random() * (vVariance - (-vVariance)) + (-vVariance);
		}
		super.setV(vx, vy);
		this.startX = x;
		this.startY = y;
		this.origin = origin;
	}


	/**
	 * Returns the weapon that fired this bullet.
	 *
	 * @return the weapon that fired this bullet.
	 */
	public Weapon getOriginWeapon() {
		return this.origin;
	}


	/**
	 * Returns the type of the weapon that fired this bullet.
	 *
	 * @return the type of the weapon that fired this bullet.
	 *
	 * @see interfaces.Weapon
	 */
	public String getWeaponType() {
		return this.origin.getType();
	}


	/**
	 * Gets the damage dealt by this bullet based on the weapon that fired it and the
	 * distance it has travelled.
	 *
	 * @return the scaled damage dealt by this bullet.
	 */
	public int getScaledDamage() {
		double distanceTravelled = Math.sqrt(Math.pow(this.startX - super.getX(), 2) +
											 Math.pow(this.startY - super.getY(), 2));
		double effectiveRange = this.origin.range();
		int baseDamage = this.origin.damage();
		if (distanceTravelled <= effectiveRange)
			return baseDamage;

		double rangesTravelled = distanceTravelled / effectiveRange;
		double damagePortion = (1.0) / (Math.pow(2.0, rangesTravelled - 1.0));
		return (int) (baseDamage * damagePortion);
	}


	/**
	 * Returns the type of this bullet. The type is defined by the following string:
	 * <p>
	 * {@code "Bullet" + getWeaponType()}
	 * <p>
	 * For example, {@code "BulletRifle"} for a rifle.
	 *
	 * @return the type of this bullet.
	 */
	@Override
	public String getType() {
		return "Bullet" + this.origin.getType();
	}

}
