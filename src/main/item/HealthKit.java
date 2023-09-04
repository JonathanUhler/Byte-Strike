package item;


import interfaces.Item;


/**
 * Represents a health kit.
 *
 * @author Jonathan Uhler
 */
public class HealthKit extends Item {

	/**
	 * Constructs a new {@code HealthKit} object.
	 */
    public HealthKit() {
		super(250);
	}
	

	@Override
	public String getType() {
		return "HealthKit";
	}

}
