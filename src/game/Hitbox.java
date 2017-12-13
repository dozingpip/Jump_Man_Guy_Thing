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
	
	public boolean isHit(Hitbox other) {
		/*if((xMax > other.getXMin() && (yMin < other))) {
			return true;
		}*/
		return false;
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
