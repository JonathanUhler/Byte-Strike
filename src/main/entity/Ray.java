package entity;


import interfaces.Moveable;


/**
 * Represents an invisible object used to implement basic ray casting. This class is used by the
 * {@code isViewable} method of the {@code Screen} class to obscure tiles and entities behind
 * walls that the player cannot see.
 */
public class Ray extends Moveable {

	/**
	 * Constructs a new {@code Ray} object with an initial position and direction. The velocity
	 * is always 1.0 tiles/frame, which is the maximum distance required to avoid skipping
	 * tiles. The initial size of the ray is 0 tiles.
	 *
	 * @param x    the initial x position of the ray (e.g. {@code player.getX()}.
	 * @param y    the initial y position of the ray (e.g. {@code player.getY()}.
	 * @param rad  the direction, relative to the unit circle, that the ray is cast.
	 */
	public Ray(double x, double y, double rad) {
		super(x, y, 0);
		super.setV(Math.cos(rad), Math.sin(rad));
	}


	/**
	 * Determines whether this ray is in range of a specified target. Being "in range" is defined
	 * as having the position of the ray be strictly inside a box around the specified target
	 * position with a width and height of {@code 2 * margin}, where {@code margin = 1.0} tiles.
	 *
	 * @param targetX  the x position of the ray's target.
	 * @param targetY  the y position of the ray's target.
	 *
	 * @return {@code true} if this ray is in range of the specified target position.
	 */
	public boolean inRange(double targetX, double targetY) {
		double x = super.getX();
		double y = super.getY();
		double margin = 1.0;
		return x > (targetX - margin) && x < (targetX + margin) &&
			   y > (targetY - margin) && y < (targetY + margin);
	}


	/**
	 * Identifies this {@code Moveable} object as a {@code Ray}. This method always
	 * returns the string literal {@code "Ray"}.
	 *
	 * @return the string literal {@code "Ray"}.
	 */
	@Override
	public String getType() {
		return "Ray";
	}

}
