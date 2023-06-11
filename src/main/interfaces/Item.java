package interfaces;


/**
 * A flag that identifies a class as a type of {@code Item}. This class also requires that children
 * implement some functionality to interface with the shop and sprite loading systems.
 *
 * @author Jonathan Uhler
 */
public abstract class Item {

	/** The price of this item in the shop. */
	private int cost;
	

	/**
	 * Constructs a new {@code Item} object with a specified price.
	 *
	 * @param cost  the price of this item in the shop.
	 */
	public Item(int cost) {
		this.cost = cost;
	}
	

	/**
	 * Returns the cost of this item.
	 *
	 * @return the cost of this item.
	 */
	public int getCost() {
		return this.cost;
	}
	
	
	/**
	 * Returns the type of this item as a string. This can be any string, but is recommended
	 * to be standardized by a specific {@code Item} child class.
	 * <p>
	 * This method is separate from the {@code toString}, which can be overriden as desired
	 * by children. The intent of the item "type" is to easily identify the name of the
	 * sprite image file when rending this item.
	 *
	 * @return the type of this item as a string.
	 */
	public abstract String getType();

}
