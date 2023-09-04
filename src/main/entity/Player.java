package entity;


import interfaces.Moveable;
import interfaces.Weapon;
import interfaces.Item;
import graphics.SoundManager;
import item.*;
import java.util.List;
import java.util.ArrayList;


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
	/** The items carried by this player. */
    private List<Item> items;
	

	/**
	 * Constructs a new {@code Player} object. The health of all players is initially set
	 * to {@code 100} and is in the range {@code (0, 100]}. Players start with $100, a
	 * {@code Pistol}, and no armor or other usable items.
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
		this.items = new ArrayList<>();
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
	 * Returns whether this player is wearing armor.
	 *
	 * @return whether this player is wearing armor.
	 */
	public boolean isArmored() {
	    for (Item item : this.items)
			if (item instanceof Armor)
				return true;
		return false;
	}


	/**
	 * Gets the specified item from the player's list of items. The {@code itemNum} is <b>not</b>
	 * an index into the {@code items} list. Instead, it is the number of the usable item, which
	 * excludes any armor that the player may have.
	 * <p>
	 * For instace, if the player has the items {@code [HealthKit, Armor, Grenade]}, then
	 * {@code getItem(1)} will return the health kit and {@code getItem(2)} will return the
	 * grenade. Any other value of {@code itemNum} will yield {@code null}.
	 *
	 * @param itemNum  the item number to get.
	 *
	 * @return the item corresponding to the specified item number, or {@code null} if that
	 *         item number does not exist.
	 */
	public Item getItem(int itemNum) {
		int current = 1;
		for (Item item : this.items) {
			if (item instanceof Armor)
				continue;
			
			if (current == itemNum)
				return item;
			current++;
		}
		return null;
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
	 * Gives the player more money.
	 *
	 * @param money  the amount of money to give to the player.
	 */
	public void pay(int money) {
		this.money += money;
	}


	/**
	 * Attempts to buy a new item and returns the status of the purchase. If the
	 * item was successfully purchased, this method will automatically reduce
	 * the player's money and give them the item. This method prevents buying the same
	 * weapon that the player already has, and prevents over-buying on other items.
	 *
	 * @param item  the item to buy.
	 *
	 * @return whether the item was successfully purchased.
	 */
	public boolean buy(Item item) {
		// Prevent buying duplicate weapons
		if (item.getType().equals(this.weapon.getType()))
			return false;
		// Prevent over-buying on items
		if (!(item instanceof Weapon) && this.items.size() >= 3)
			return false;
		
		int cost = item.getCost();
		if (cost <= this.money) {
			this.money -= cost;
			this.give(item);
			return true;
		}
		return false;
	}


	/**
	 * Gives this player an item. If the specified item is a {@code Weapon}, the player's
	 * current weapon is replaced by the given weapon. Otherwise, the item is added to the
	 * player's list of items.
	 *
	 * @param item  the item to give the player.
	 *
	 * @throws NullPointerException  if {@code item == null}.
	 */
	public void give(Item item) {
		if (item == null)
			throw new NullPointerException("item was null");
		
		if (item instanceof Weapon)
		    this.weapon = (Weapon) item;
		else
			this.items.add(item);
	}


	/**
	 * Uses an item owned by this player. If the item is used successfully, the action
	 * of the item is automatically performed and the item is taken away from the player.
	 *
	 * @param itemNum  the item number to use.
	 *
	 * @return whether the specified item was used.
	 *
	 * @see getItem
	 */
	public boolean use(int itemNum) {
		Item item = this.getItem(itemNum);
		if (item == null)
			return false;

		if (item instanceof HealthKit)
			this.health = Math.min(this.health + 25, 100);
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
		
>>>>>>> a1a5105 (tmp)
>>>>>>> 809b721 (tmp)
		this.items.remove(item);
		return true;
	}


	/**
	 * Resets the player's information, excluding position. The reset consists of
	 * setting the player's health and {@code lastWalked} information, and zeroing the
	 * velocity.
	 */
	public void reset() {
		this.lastWalked = System.currentTimeMillis();

		this.health = 100;
		this.money = 100;
		this.weapon = new Pistol();
		this.items = new ArrayList<>();
		
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
	 * Returns the type of the player. The type is either {@code "PlayerShort"} or
	 * {@code "PlayerLong"} depending on the type of weapon being carried by the player.
	 *
	 * @return the type of the player.
	 */
	@Override
	public String getType() {
	    if (this.weapon instanceof Pistol ||
			this.weapon instanceof SMG)
			return "PlayerShort";
		else // Most weapons will have the player holding at two locations, so this is the default
			return "PlayerLong";
	}

}
