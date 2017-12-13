package game;

public class Hitbox extends GraphicObject{
	//relative to object's center
	float xMin, xMax, yMin, yMax;
	
	public Hitbox(float xMin_, float yMin_, float xMax_, float yMax_) {
		xMin = xMin_;
		xMax = xMax_;
		yMin = yMin_;
		yMax = yMax_;
	}
	
	public void draw() {
		app_.stroke(0);
		app_.noFill();
		app_.rect(xMin, yMin, xMax-xMin, yMax-yMin);
	}
	
	/**
	 * When using this, check the isHit on both objects and use an or.
	 * If there were more checks there wouldn't have to be an or statement, but you'd be doing twice the work for not much cause.
	 * @param other
	 * @return returns true when the other hitbox is touching this hitbox is on either the top or right side of the other.
	 */
	public boolean isColliding(Hitbox other) {
		//touching on right side of this hitbox, left side of other hitbox
		if( (getXMax() >= other.getXMin()) && ( (getYMin() <= other.getYMax()) && (getYMin()>= other.getYMin()) ) ) {
			return true;
		//touching on bottom side of this hitbox, top side of other hitbox
		}else if( (getYMin() >= other.getYMax()) && ( (getXMax() <= other.getXMin()) && (getXMin()>= other.getXMax())) ) {
			return true;
		//don't need to check other cases because this will be used on both objects with an or 
		}else {
			return false;
		}
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

}
