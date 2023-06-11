package interfaces;


import entity.Bullet;
import graphics.Settings;
import graphics.SoundManager;
import java.awt.Point;


/**
 * Represents an abstract projectile weapon that can be used by the player.
 * <p>
 * This class includes information about the properties of the weapon. See the constructor for
 * more information on the meanings of these statistics.
 *
 * @author Jonathan Uhler
 */
public abstract class Weapon extends Item {

	/** 
	 * The delay, in milliseconds, until a recoil variation on the bullet's velocity will no 
	 * longer be applied. After this time, no random variation will be allied and the direction
	 * of the velocity resultant vector will point exactly at the mouse. Before this time is
	 * finished, a random variation angle will be applied (decreasing accuracy).
	 */
	public static final int RECOIL_COOLDOWN = 300; // In ms
	

	// Stats
	/** The amount of damage dealt by one hit by this weapon, out of 100. */
	private int damage;
	/** The velocity of projectiles fired by this weapon, in tiles per second. */
	private double velocity;
	/** The effectiveness of this weapon against a player with armor. */
	private double penetration;
	/** The rate of fire, in rounds per second. */
	private int firerate;
	/** The effective range of this weapon in tiles. */
	private double range;
	/** The maximum recoil angle in radians. */
	private double recoil;
	/**  The movement speed multiplier of the player while carrying this weapon from 0-1. */
	private double mobility;
	/** The capacity of this weapon before reloading is required. */
	private int capacity;
	/** The number of bullets created each time {@code fire} is called. */
	private int roundsPerShot;
	/** The time, in milliseconds, to reload. */
	private int reloadTime;
	/** The amount of money awarded to the attacking player using this weapon. .*/
	private int moneyPerKill;
	/** The length, in tiles, of the barrel. Used to determine bullet position. */
	private double barrelLength;

	// Usage information
	/** The unix epoch time when the weapon was last used. */
	private long lastFired;
	/** The number of projectiles left before reload is needed. */
	private int bulletsLeft;


	/**
	 * Constructs a new {@code Weapon} object with 1 round per shot.
	 *
	 * @param damage        the damage dealt by one projectiles from this weapon, in terms of HP.
	 * @param velocity      the velocity of a projectile, in tiles/sec.
	 * @param firerate      the rate of fire, in rounds per second.
	 * @param range         the effective range of this weapon in tiles.
	 * @param recoil        the maximum recoil angle in radians.
	 * @param mobility      the movement speed multiplier of the player while carrying this weapon 
	 *                      in the  interval (0, 1].
	 * @param capacity      the capacity of this weapon before reloading is required.
	 * @param reloadTime    the time, in milliseconds, to reload.
	 * @param cost          the cost to buy this weapon from the shop.
	 * @param moneyPerKill  the amount of money earned by the attacking player using this weapon.
	 * @param barrelLength  the length, in tiles, of the barrel. Used to determine bullet pos.
	 */
	public Weapon(int damage,
				  double velocity,
				  int firerate,
				  double range,
				  double recoil,
				  double mobility,
				  int capacity,
				  int reloadTime,
				  int cost,
				  int moneyPerKill,
				  double barrelLength)
	{
		this(damage,
			 velocity,
			 firerate,
			 range,
			 recoil,
			 mobility,
			 capacity,
			 1,
			 reloadTime,
			 cost,
			 moneyPerKill,
			 barrelLength);
	}


	/**
	 * Constructs a new {@code Weapon} object with a variable number of rounds per shot.
	 *
	 * @param damage         the damage dealt by one projectiles from this weapon, in terms of HP.
	 * @param velocity       the velocity of a projectile, in tiles/sec.
	 * @param firerate       the rate of fire, in rounds per second.
	 * @param range          the effective range of this weapon in tiles.
	 * @param recoil         the maximum recoil angle in radians.
	 * @param mobility       the movement speed multiplier of the player while carrying this weapon 
	 *                       in the  interval (0, 1].
	 * @param capacity       the capacity of this weapon before reloading is required.
	 * @param roundsPerShot  the number of bullets created each time {@code fire} is called.
	 * @param reloadTime     the time, in milliseconds, to reload.
	 * @param cost           the cost to buy this weapon from the shop.
	 * @param moneyPerKill   the amount of money earned by the attacking player using this weapon.
	 * @param barrelLength  the length, in tiles, of the barrel. Used to determine bullet pos.
	 */
	public Weapon(int damage,
				  double velocity,
				  int firerate,
				  double range,
				  double recoil,
				  double mobility,
				  int capacity,
				  int roundsPerShot,
				  int reloadTime,
				  int cost,
				  int moneyPerKill,
				  double barrelLength)
	{
		super(cost);
		
		this.damage = damage;
		this.velocity = velocity;
		this.penetration = velocity + 0.1;
		this.firerate = firerate;
		this.range = range;
		this.recoil = recoil;
		this.mobility = mobility;
		this.capacity = capacity;
		this.roundsPerShot = roundsPerShot;
		this.reloadTime = reloadTime;
		this.moneyPerKill = moneyPerKill;
		this.barrelLength = barrelLength;

		this.lastFired = 0;
		this.bulletsLeft = this.capacity;
	}


	/**
	 * Returns the damage of this weapon.
	 *
	 * @return the damage of this weapon.
	 */
	public int damage() {
		return this.damage;
	}


	/**
	 * Returns the velocity of this weapon.
	 *
	 * @return the velocity of this weapon.
	 */
	public double velocity() {
		return this.velocity;
	}


	/**
	 * Returns the effectiveness of this weapon against a player in armor. Penetration is
	 * a linear function of velocity in the interval {@code [0, 1]} where {@code 0} represents
	 * complete ineffectiveness against armor (no damage) and {@code 1} represents total
	 * effectiveness against armor (full damage).
	 *
	 * @return the penetration of bullets fired by this weapon.
	 */
	public double penetration() {
		return this.penetration;
	}


	/**
	 * Returns the firerate of this weapon.
	 *
	 * @return the firerate of this weapon.
	 */
	public int firerate() {
		return this.firerate;
	}


	/**
	 * Returns the range of this weapon.
	 *
	 * @return the range of this weapon.
	 */
	public double range() {
		return this.range;
	}


	/**
	 * Returns the recoil of this weapon.
	 *
	 * @return the recoil of this weapon.
	 */
	public double recoil() {
		return this.recoil;
	}


	/**
	 * Returns the mobility of this weapon.
	 *
	 * @return the mobility of this weapon.
	 */
	public double mobility() {
		return this.mobility;
	}


	/**
	 * Returns the capacity of this weapon.
	 *
	 * @return the capacity of this weapon.
	 */
	public int capacity() {
		return this.capacity;
	}


	/**
	 * Returns the money per kill of this weapon.
	 *
	 * @return the money per kill of this weapon.
	 */
	public int moneyPerKill() {
		return this.moneyPerKill;
	}


	/**
	 * Returns the number of bullets left before reload.
	 *
	 * @return the number of bullets left before reload.
	 */
	public int bulletsLeft() {
		return this.bulletsLeft;
	}


	/**
	 * Returns the number of bullets created each time {@code fire} is called.
	 *
	 * @return the number of bullets created each time {@code fire} is called
	 *
	 * @see fire
	 */
	public int roundsPerShot() {
		return this.roundsPerShot;
	}


	/**
	 * Returns a random recoil variation angle in the interval {@code [-recoil(), recoil()]}.
	 *
	 * @return a random recoil variation angle.
	 */
	private double generateRecoil() {
		long deltaTime = this.timeSinceLastFired();
		if (deltaTime > Weapon.RECOIL_COOLDOWN)
			return 0;
		return Math.random() * (this.recoil - (-this.recoil)) + (-this.recoil);
	}


	/**
	 * Returns a random offset for a bullet's direction to create spread for weapons
	 * that fire more than one round per shot. This offset is generated in the same
	 * range as {@code generateRecoil} is the recoil cooldown were not yet up.
	 *
	 * @return a random offset for a bullet's direction.
	 */
	private double generateSpread() {
		return Math.random() * (this.recoil - (-this.recoil)) + (-this.recoil);
	}


	/**
	 * Returns the change in time since this weapon was last used. This method does not
	 * modify the {@code lastFired} variable (this operation is done in the {@code fire}
	 * method).
	 *
	 * @return the change in time since this weapon was last used.
	 */
	private long timeSinceLastFired() {
		long currentTime = System.currentTimeMillis();
		long deltaTime = currentTime - this.lastFired;
		return deltaTime;
	}


	/**
	 * Returns the x offset that should be added to the initial position of a bullet
	 * to make it appear as if it came from the weapon muzzle.
	 * <p>
	 * See: https://www.desmos.com/calculator/it439pwq2n.
	 *
	 * @param rad  the rotation of the player.
	 *
	 * @return the x offset of the bullet.
	 */
	private double getMuzzleXOffset(double rad) {
		return (0.5 + this.barrelLength) * Math.sin(-rad + Math.PI / 2) + 0.35;
	}


    /**
	 * Returns the y offset that should be added to the initial position of a bullet
	 * to make it appear as if it came from the weapon muzzle.
	 * <p>
	 * See: https://www.desmos.com/calculator/it439pwq2n.
	 *
	 * @param rad  the rotation of the player.
	 *
	 * @return the y offset of the bullet.
	 */
	private double getMuzzleYOffset(double rad) {
		return (0.5 + this.barrelLength) * Math.sin(-rad + Math.PI) + 0.35;
	}


	/**
	 * Returns whether this weapon is currently reloading.
	 *
	 * @return whether this weapon is currently reloading.
	 */
	public boolean reloading() {
	    return this.bulletsLeft <= 0;
	}


	/**
	 * Handles reload sounds. This method plays a click/jam sound before waiting the reload
	 * period and playing the reloaded sound.
	 */
	private void reload() {
		SoundManager.playSound("reload_cue");
		Thread reloadTimer = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(Weapon.this.reloadTime);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
						return;
					}
					SoundManager.playSound("reload");
					Weapon.this.bulletsLeft = Weapon.this.capacity;
				}
			});
		reloadTimer.start();
	}


	/**
	 * Fires this weapon and returns the bullets fired.
	 *
	 * @param x    the x position, in tile space, where the projectile originates from.
	 * @param y    the y position, in tile space, where the projectile originates from.
	 * @param rad  the angle, in radians, to which the velocity vector of the projectile points.
	 *
	 * @return a list of {@code Bullet} objects fired by this weapon.
	 *
	 * @see entity.Bullet
	 */
	public Bullet[] fire(double x, double y, double rad) {
		// Check if there is ammunition available
		if (this.reloading())
		    return null;

		// Check if the firerate cooldown has passed
		long deltaTime = this.timeSinceLastFired();
		int framesPerRound = (int) ((1.0 / this.firerate) * Settings.FPS);
		int framesPassed = (int) ((deltaTime / 1000.0) * Settings.FPS);
		if (framesPassed < framesPerRound)
			return null;

		// Fire the bullets
		Bullet[] bullets = new Bullet[this.roundsPerShot()];
		for (int i = 0; i < bullets.length; i++) {
			double recoil = this.generateRecoil();
			double spread = i > 0 ? this.generateSpread() : 0; // Primarily for shotguns
			Bullet bullet = new Bullet(x + this.getMuzzleXOffset(rad),
									   y + this.getMuzzleYOffset(rad),
									   rad + recoil + spread, this);
			bullets[i] = bullet;
		}
		this.lastFired = System.currentTimeMillis();
		this.bulletsLeft--;
		if (this.reloading())
			this.reload();
		return bullets;
	}


	/**
	 * Completes the client-side routine of firing this weapon. This includes decrementing
	 * the number of bullets left, playing the weapon fired sound, and performing
	 * the reload routine if needed. This method does not create a bullet.
	 */
	public void fireBlank() {
		this.bulletsLeft--;
		SoundManager.playSound("shoot");
		if (this.reloading())
			this.reload();
	}

}
