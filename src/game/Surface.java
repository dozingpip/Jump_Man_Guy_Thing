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
	
	public void draw() {
		app_.stroke(255, 0, 0);
		app_.line(getX(), getY(), x2, y2);
	}
}
