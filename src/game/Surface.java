package game;

import processing.core.PApplet;

public class Surface extends GraphicObject {
	private float x2, y2;
	private float length_;
	private float perpAngle;
	private boolean upToDate;
	
	public Surface (float x, float y, float length, float angle) {
		super(x, y, angle);
		length_ = length;
		upToDate = false;
		update();
	}
//	public Surface(float x1_, float y1_, float x2_, float y2_) {
//		x1 = x1_;
//		y1 = y1_;
//		x2 = x2_;
//		y2 = y2_;
//		float dx = x1 - x2, dy = y1 - y2;
//		perpAngle = PApplet.atan(dx/-dy);
//	}
	
	public void update() {
//		if (!upToDate) {
			perpAngle = getAngle() + PApplet.PI/2;
			x2 = getX() + length_ * PApplet.cos(getAngle());
			y2 = getY() + length_ * PApplet.sin(getAngle());
			upToDate = true;
//		}
	}
	
	public float getX2() {
		return x2;
	}
	
	public float getY2() {
		return y2;
	}
	
	public void setAngle(float newAngle) {
		super.setAngle(newAngle % (2* PApplet.PI));
		update();
	}
	
	public void draw() {
		if ((getAngle() < PApplet.PI/2 + PApplet.PI/64 && getAngle() > PApplet.PI/2 - PApplet.PI/32) || (getAngle() < 3*PApplet.PI/2 + PApplet.PI/64 && getAngle() > 3*PApplet.PI/2 - PApplet.PI/32)) {
			app_.stroke(0, 255, 0);
		}
		else {

			app_.stroke(255, 0, 0);
		}
		app_.line(getX(), getY(), x2, y2);
	}
	
	/**
	 * only colliding if a corner of the other hitbox is touching this hitbox somewhere,
	 * so if any corner of the other hitbox isInside this one, the boxes are colliding.
	 * @param other
	 * @return returns true when the other hitbox is touching this hitbox with any corner.
	 */
	public boolean isColliding(Hitbox other) {
		if(		isInside(other.getXMin(), other.getYMin()) ||
				isInside(other.getXMin(), other.getYMax()) ||
				isInside(other.getXMax(), other.getYMin()) ||
				isInside(other.getXMax(), other.getYMax())) {
			return true;
		}else return false;
	}
	
	public boolean isInside(float x, float y) {
		//which x, y coord of this line is bigger, which is smaller?
		float biggerX, biggerY, smallerX, smallerY;
		if(getX()>=getX2()) {
			biggerX = getX();
			smallerX = getX2();
		}else {
			biggerX = getX2();
			smallerX = getX();
		}
		
		if(getY()>=getY2()) {
			biggerY = getY();
			smallerY = getY2();
		}else {
			biggerY = getY2();
			smallerY = getY();
		}
		if(x > smallerX && x < biggerX && y > smallerY && y < biggerY) {
			return true;
		}else return false;
	}
}
