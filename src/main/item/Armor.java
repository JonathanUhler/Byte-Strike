package item;


import interfaces.Item;


public class Armor extends Item {

    public Armor() {
		super(400);
	}
	

	@Override
	public String getType() {
		return "Armor";
	}

}
