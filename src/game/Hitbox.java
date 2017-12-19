package game;

/**
 * The box used to determine what some object this is attached to may be colliding with (the floor, other objects with hitboxes, etc)
 * @author Steph and Thomas
 *
 */
public class Hitbox extends GraphicObject{
	//relative to object's center
	private float xMin, xMax, yMin, yMax;
	private float width, height;
	
	/**
	 * 
	 * @param xMin_ leftmost x coord
	 * @param yMin_ lowermost y coord
	 * @param xMax_ rightmost x coord
	 * @param yMax_ uppermost y coord
	 */
	public Hitbox(float xMin_, float yMin_, float xMax_, float yMax_) {
		xMin = xMin_;
		xMax = xMax_;
		yMin = yMin_;
		yMax = yMax_;
		width = xMax-xMin;
		height = yMax-yMin;
	}
	
	/**
	 * draw the hitbox
	 */
	public void draw() {
		app_.pushMatrix();
		app_.stroke(0);
		app_.strokeWeight(0.2f);
		app_.noFill();
		app_.translate(xMin, yMin);
		app_.rect(0, 0, width, height);
		app_.popMatrix();
	}
	
	/**
	 * only colliding if a corner of the other hitbox is touching this hitbox somewhere,
	 * so if any corner of the other hitbox isInside this one, the boxes are colliding.
	 * @param other
	 * @return returns true when the other hitbox is touching this hitbox with any corner.
	 */
	public boolean isColliding(Hitbox other) {
		if(		//is other hitbox's bottom left corner inside?
				isInside(other.getXMin(), other.getYMin()) ||
				//is other hitbox's bottom right corner inside?
				isInside(other.getXMin(), other.getYMax()) ||
				//is other hitbox's top left corner inside?
				isInside(other.getXMax(), other.getYMin()) ||
				//is other hitbox's top right corner inside?
				isInside(other.getXMax(), other.getYMax())) {
			return true;
		}else return false;
	}
	
	/**
	 * Is the other hitbox colliding from the top?
	 * @param other
	 * @return
	 */
	public boolean hitTop(Hitbox other) {
		//is other hitbox's bottom left corner inside? but not top left corner?
		//is other hitbox's bottom right corner inside? but not top right corner?
		if(		(isInside(other.getXMin(), other.getYMin()) &&
				!isInside(other.getXMax(), other.getYMin())) ||
				(isInside(other.getXMin(), other.getYMax()) &&
				!isInside(other.getXMax(), other.getYMax()))
			) {
			return true;
		}else return false;
	}
	
	/**
	 * 
	 * @return leftmost x coord
	 */
	public float getXMin() {
		return xMin;
	}
	
	/**
	 * 
	 * @return lowermost y coord
	 */
	public float getYMin() {
		return yMin;
	}
	
	/**
	 * 
	 * @return rightmost x coord
	 */
	public float getXMax() {
		return xMax;
	}
	
	/**
	 * 
	 * @return uppermost y coord
	 */
	public float getYMax() {
		return yMax;
	}
	
	/**
	 * 
	 * @return width of the hitbox
	 */
	public float getW() {
		return width;
	}
	
	/**
	 * 
	 * @return height of the hitbox
	 */
	public float getH() {
		return height;
	}

	/** Tells whether the point whose coordinates are passed is inside this object
	 * 
	 * @param x  x coordinate of the point (in world coordinates)
	 * @param y x coordinate of the point (in world coordinates)
	 * @return true if (x, y) is inside this object
	 */
	public boolean isInside(float x, float y)
	{		
		return ((x >= getX()) && (x <= getX() + getW()) && (y >= getY()) && (y <= getY() + getH()));
	}

}
