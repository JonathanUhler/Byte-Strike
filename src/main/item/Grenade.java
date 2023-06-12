package item;


import interfaces.Item;


/**
 * Represents a hand grenade.
 *
 * @author Jonathan Uhler
 */
public class Grenade extends Item {

	/**
	 * Constructs a new {@code Grenade} object.
	 */
    public Grenade() {
		super(150);
	}
	

	@Override
	public String getType() {
		return "Grenade";
	}

}
