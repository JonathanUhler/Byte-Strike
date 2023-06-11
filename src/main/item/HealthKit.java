package item;


import interfaces.Item;


public class HealthKit extends Item {

    public HealthKit() {
		super(250);
	}
	

	@Override
	public String getType() {
		return "HealthKit";
	}

}
