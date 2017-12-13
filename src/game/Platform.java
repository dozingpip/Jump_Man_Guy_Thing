package game;

import processing.core.PApplet;

public class Platform {
	
	private Surface[] surfaces;
	
	// Center about which this platform rotates
	private float x, y;
	
	// The angle of rotation of the platform
	private float angle;
	
	public Platform(float x_, float y_, float angle_, Surface[] surfaces_) {
		surfaces = surfaces_;
		x = x_;
		y = y_;
		angle = angle_;
	}
	
	public void updateRotation(float newAngle) {
		for(Surface s : surfaces) {
			s.setAngle(s.getAngle() + newAngle);
			float relativeX = x - s.getX();
			float relativeY = y - s.getY();
			float newX = PApplet.cos(newAngle) * relativeX - (PApplet.sin(newAngle) * relativeY);
			float newY = PApplet.sin(newAngle) * relativeX + PApplet.cos(newAngle) * relativeY;
		}
	}
	
	public void draw() {
		for(Surface s : surfaces) {
			s.draw();
		}
	}
	
}
