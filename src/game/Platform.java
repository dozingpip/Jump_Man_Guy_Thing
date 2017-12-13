package game;

import processing.core.PApplet;

public class Platform extends GraphicObject{
	
	private static final float DOT_RADIUS = 0.5f;
	
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
	
	public void updateRotation(float angleChange) {
		for(Surface s : surfaces) {
			System.out.println("New Rotation");
			System.out.println("Angle Change " + angleChange);
			System.out.println("New Angle for Surface " + (s.getAngle() + angleChange));
			s.setAngle(s.getAngle() + angleChange);
			float relativeX = s.getX() - x;
			float relativeY = s.getY() - y;
			System.out.println("Cosine of the angle " + PApplet.cos(angleChange));
			System.out.println("Sine of the angle " + PApplet.sin(angleChange));
			float newX = PApplet.cos(angleChange) * relativeX + (-PApplet.sin(angleChange) * relativeY);
			float newY = PApplet.sin(angleChange) * relativeX + PApplet.cos(angleChange) * relativeY;
			System.out.println("New Start X " + newX);
			System.out.println("New Start Y " + newY);
			s.setX(newX + x);
			s.setY(newY + y);
			s.update();
		}
	}
	
	public float getAngle() {
		return angle;
	}
	
	public void setAngle(float angle_) {
		updateRotation(angle_ - angle);
		angle = angle_;
	}
	
	public void draw() {
		app_.ellipse(x, y, DOT_RADIUS*2, DOT_RADIUS*2);
		for(Surface s : surfaces) {
			s.draw();
		}
	}
	
}
