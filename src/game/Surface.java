package game;

import processing.core.PApplet;

public class Surface {
	
	protected static PApplet app_;
	private static int appSetCounter_ = 0;
	
	private float x, y;
	private float x2, y2;
	private float length, angle;
	private float perpAngle;
	private boolean upToDate;
	
	public Surface (float x_, float y_, float length_, float angle_) {
		x = x_;
		y = y_;
		length = length_;
		angle = angle_;
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
		if (!upToDate) {
			perpAngle = angle + PApplet.PI/2;
			x2 = length * PApplet.cos(angle);
			y2 = length * PApplet.sin(angle);
			upToDate = true;
		}
	}
	
	public void draw() {
		app_.stroke(255, 0, 0);
		app_.line(x, y, x2, y2);
	}
}
