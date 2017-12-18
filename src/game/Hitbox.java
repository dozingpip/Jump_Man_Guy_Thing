package game;

import processing.core.PApplet;

public class Hitbox extends GraphicObject{
	//relative to object's center
	float xMin, xMax, yMin, yMax, angle;
	float width, height;
	
	public Hitbox(float xMin_, float yMin_, float xMax_, float yMax_, float angle_) {
		xMin = xMin_;
		xMax = xMax_;
		yMin = yMin_;
		yMax = yMax_;
		angle = angle_;
		width = xMax-xMin;
		height = yMax-yMin;
	}
	
	public void draw() {
		app_.pushMatrix();
		app_.stroke(0);
		app_.strokeWeight(0.2f);
		app_.noFill();
		app_.translate(xMin, yMin);
		app_.rotate(angle);
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
	
	public float getXMin() {
		return xMin;
	}
	
	public float getYMin() {
		return yMin;
	}
	
	public float getXMax() {
		return xMax;
	}
	
	public float getYMax() {
		return yMax;
	}
	
	public float getW() {
		return width;
	}
	
	public float getH() {
		return height;
	}
	
	public void incX(float x) {
		xMin+=x;
		xMax+=x;
	}
	
	public void incY(float y) {
		yMin+=y;
		yMax+=y;
	}

	/**
	 * Tells whether the point whose coordinates are passed is inside this object
	 * 
	 * @param x  x coordinate of the point (in world coordinates)
	 * @param y x coordinate of the point (in world coordinates)
	 * @return true if (x, y) is inside this object
	 */
	public boolean isInside(float x, float y)
	{	
		float dx = x - getX(), dy = y - getY();
		float ca = PApplet.cos(angle), sa = PApplet.sin(angle);
//		float ca = (float) Math.cos(angle_), sa = (float) Math.sin(angle_);
		
		float rdx = ca*dx + sa*dy;
		float rdy = -sa*dx + ca*dy;
		
		return ((rdx >= 0) && (rdx <= getW()) && (rdy >= 0) && (rdy <= getH()));
	}

}
