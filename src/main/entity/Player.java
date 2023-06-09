package entity;


import interfaces.Moveable;
import interfaces.Weapon;
import graphics.SoundManager;
import item.*;


/**
 * Represents a player.
 *
 * @author Jonathan Uhler
 */
public class Player extends Moveable {

	/** The last time, as a Unix epoch, that the player walked. Used for walking sounds. */
	private long lastWalked;

	/** The current health of the player. */
	private int health;
	/** The amount of money this player has. */
	private int money;
	/** The weapon this player has. */
	private Weapon weapon;
	

	/**
	 * Constructs a new {@code Player} object. The health of all players is initially set
	 * to {@code 100} and is in the range {@code (0, 100]}.
	 *
	 * @param x  the initial x position of this player.
	 * @param y  the initial y position of this player.
	 */
	public Player(double x, double y) {
		super(x, y, 0.85);

		this.lastWalked = System.currentTimeMillis();

		this.health = 100;
		this.money = 100;
		this.weapon = new Pistol();
	}


	/**
	 * Returns the health of this player.
	 *
	 * @return the health of this player.
	 */
	public int getHealth() {
		return this.health;
	}


	/**
	 * Determines if this player is dead.
	 *
	 * @return whether this player is dead. That is, whether {@code getHealth() <= 0}.
	 */
	public boolean isDead() {
		return this.health <= 0;
	}


	/**
	 * Returns the amount of money this player has.
	 *
	 * @return the amount of money this player has.
	 */
	public int getMoney() {
		return this.money;
	}


	/**
	 * Returns this player's weapon.
	 *
	 * @return this player's weapon.
	 */
	public Weapon getWeapon() {
		return this.weapon;
	}


	/**
	 * Reduces the player's health.
	 *
	 * @param damage  the amount to reduce from the player's health.
	 */
	public void damage(int damage) {
		this.health -= damage;
	}


	/**
	 * Attempts to buy a new weapon and returns the status of the purchase. If the
	 * weapon was successfully purchased, this method will automatically reduce
	 * the player's money and set their weapon. This method prevents buying the same
	 * weapon that the player already has.
	 *
	 * @param weapon  the weapon to buy.
	 *
	 * @return whether the weapon was successfully purchased.
	 */
	public boolean buy(Weapon weapon) {
		if (weapon.getType().equals(this.weapon.getType()))
			return false;
		
		int cost = weapon.cost();
		if (cost <= this.money) {
			this.money -= cost;
			this.weapon = weapon;
			return true;
		}
		return false;
	}


	/**
	 * Gives the player more money.
	 *
	 * @param money  the amount of money to give to the player.
	 */
	public void pay(int money) {
		this.money += money;
	}


	/**
	 * Gives this player a specified weapon.
	 *
	 * @param weapon  the weapon to give to this player.
	 */
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}


	/**
	 * Resets the player's information, excluding position. The reset consists of
	 * setting the player's health and {@code lastWalked} information, and zeroing the
	 * velocity.
	 */
	public void reset() {
		this.lastWalked = System.currentTimeMillis();

		this.health = 100;
		this.money = 250;
		this.weapon = new Pistol();
		super.setV(0, 0);
		super.setRad(0);
	}


	@Override
	public void move() {
		super.move();

		// Play footstep sound
		long currentTime = System.currentTimeMillis();
		long deltaWalkTime = currentTime - this.lastWalked;
		if (deltaWalkTime >= (int) (Math.random() * (600 - 400)) + 400) {
			SoundManager.playSound("walk");
			this.lastWalked = currentTime;
		}
	}


    /**
	 * Returns the type of the player. The type is defined by the following string:
	 * <p>
	 * {@code "Player" + weapon.getType()}
	 * <p>
	 * For example, {@code "PlayerRifle"} for a rifle.
	 *
	 * @return the type of the player.
	 */
	@Override
	public String getType() {
		return "Player" + this.weapon.getType();
	}

}
