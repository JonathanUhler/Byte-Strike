package item;


import interfaces.Item;


/**
 * Represents body armor that can be worn by the player.
 *
 * @author Jonathan Uhler
 */
public class Armor extends Item {

	/**
	 * Constructs a new {@code Armor} object.
	 */
    public Armor() {
		super(400);
	}
	

	@Override
	public String getType() {
		return "Armor";
	}

}
