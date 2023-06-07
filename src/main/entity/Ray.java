package entity;


import interfaces.Moveable;


public class Ray extends Moveable {

	public Ray(double x, double y, double rad) {
		super(x, y, 0);
		super.setV(Math.cos(rad), Math.sin(rad));
	}


	public boolean inRange(double targetX, double targetY) {
		double x = super.getX();
		double y = super.getY();
		double margin = 1.0;
		return x > (targetX - margin) && x < (targetX + margin) &&
			   y > (targetY - margin) && y < (targetY + margin);
	}


	@Override
	public String getType() {
		return "Ray";
	}

}
