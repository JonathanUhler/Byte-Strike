package interfaces;


/**
 * A representation of an entity-like object that can be moved.
 * <p>
 * In this context, "entity" refers to an object in the world that has complex properties beyond a 
 * regular tile. This definition includes objects that can be moved through some mechanism.
 * <p>
 * This abstract class tracks the 2-dimensional position and velocity of the object, as well
 * as its size. All these measurements are in <b>tile space</b>, <u><b>NOT</b></u> pixel space.
 * When displaying a {@code Moveable} object, the pixel position can be calculated using
 * the {@code getTileSize} method of the {@code Screen} class. See this method for more
 * information on displaying objects.
 *
 * @author Jonathan Uhler
 *
 * @see graphics.Screen
 */
public abstract class Moveable {

	// This information is in tile-space, not pixel-space. Convert in the Screen
	// class using the Screen::getTileSize method.
	/** X-position of the object. */
	private double x;
	/** Y-position of the object. */
	private double y;
	/** X component of the velocity of this object. */
	private double vx;
	/** Y component of the velocity of this object. */
	private double vy;
	/** The rotation, in radians, of this object. */
	private double rad;
	/** Size (square) of the collision area of this object. */
	private double s;


	/**
	 * Constructs a new {@code Moveable} object with a zero initial velocity.
	 *
	 * @param x  the x-position of the object.
	 * @param y  the y-position of the object.
	 * @param s  the square size of the object's collision area.
	 */
	public Moveable(double x, double y, double s) {
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
		this.rad = rad;
		this.s = s;
	}


	/**
	 * Returns the x-position of this object.
	 *
	 * @return the x-position of this object.
	 */
	public double getX() {
		return this.x;
	}


	/**
	 * Returns the y-position of this object.
	 *
	 * @return the y-position of this object.
	 */
	public double getY() {
		return this.y;
	}


	/**
	 * Returns the x component of this object's velocity.
	 *
	 * @return the x component of this object's velocity.
	 */
	public double getVx() {
		return this.vx;
	}


	/**
	 * Returns the y component of this object's velocity.
	 *
	 * @return the y component of this object's velocity.
	 */
	public double getVy() {
		return this.vy;
	}


	/**
	 * Returns the direction of the velocity component in radians, relative to the unit circle.
	 * (Thus, {@code 0} rad {@code ==} East, {@code PI/2} rad {@code ==} North, etc). The value 
	 * returned by this method is always positive and in the interval {@code [0, 2*PI)}.
	 *
	 * @return the direction of the velocity component in radians.
	 */
	public double getRad() {
		return this.rad;
	}


	/**
	 * Returns the size of this object's collision area.
	 *
	 * @return the size of this object's collision area.
	 */
	public double getSize() {
		return this.s;
	}


	/**
	 * Moves this object based on the velocity assigned to it. This is achieved by adding
	 * the x and y components of the velocity to the respective position components.
	 * This operation is strictly an addition, so velocity should be negative to move
	 * left or up.
	 */
	public void move() {
		this.x += this.vx;
		this.y += this.vy;
		this.calculateRad();
	}


	/**
	 * Sets the x position of this object.
	 *
	 * @param x  the x position of this object.
	 */
	public void setX(double x) {
		this.x = x;
	}


	/**
	 * Sets the y position of this object.
	 *
	 * @param y  the y position of this object.
	 */
	public void setY(double y) {
		this.y = y;
	}


	/**
	 * Sets the velocity components of this object.
	 *
	 * @param vx  the x component of this object's velocity.
	 * @param vy  the y component of this object's velocity.
	 */
	public void setV(double vx, double vy) {
	    this.vx = vx;
		this.vy = vy;
		this.calculateRad();
	}


	/**
	 * Calculates the rotation, in radians, of this object.
	 */
	private void calculateRad() {
		if (this.vx == 0)
			this.rad = 0;
		else
			this.rad = this.vx < 0 ?
				Math.atan(this.vy / this.vx) + Math.PI :
				Math.atan(this.vy / this.vx);
	}


	/**
	 * Sets the rotation of this object. Note that the rotation is also set in the
	 * {@code move} method.
	 *
	 * @param rad  the rotation of this object.
	 *
	 * @see move
	 */
	public void setRad(double rad) {
		this.rad = rad;
	}


	/**
	 * Returns the type of this object as a string. This can be any string, but is recommended
	 * to be standardized by a specific {@code Moveable} child class.
	 * <p>
	 * This method is separate from the {@code toString}, which can be overriden as desired
	 * by children.
	 *
	 * @return the type of this object as a string.
	 */
	public abstract String getType();

}
