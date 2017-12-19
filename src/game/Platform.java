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
		updateRotation(angle);
	}
	
	public Platform(float x_, float y_, float angle_, Surface firstSurface, float[] lengths, float[] angles) {
		x = x_;
		y = y_;
		angle = angle_;
		if (lengths.length != angles.length) {
			System.out.println("PLATFORM AT X = " + x + " Y = " + y + " ANGLE = " + angle + " \n HAS UNEQUAL NUMBER OF LENGTHS AND ANGLES");
		}
		surfaces = new Surface[lengths.length + 1];
		surfaces[0] = firstSurface;
		surfaces[0].setAngle(surfaces[0].getAngle() + angle);
		for(int i = 0; i < lengths.length; i++) {
			surfaces[i+1] = new Surface(surfaces[i].getX2(), surfaces[i].getY2(), lengths[i], surfaces[i].getAngle() + angles[i]);
		}
	}
	
	public void updateRotation(float angleChange) {
		for(Surface s : surfaces) {
//			System.out.println("New Rotation");
//			System.out.println("Angle Change " + angleChange);
//			System.out.println("New Angle for Surface " + (s.getAngle() + angleChange));
			s.setAngle(s.getAngle() + angleChange);
			float relativeX = s.getX() - x;
			float relativeY = s.getY() - y;
//			System.out.println("Cosine of the angle " + PApplet.cos(angleChange));
//			System.out.println("Sine of the angle " + PApplet.sin(angleChange));
			float newX = PApplet.cos(angleChange) * relativeX + (-PApplet.sin(angleChange) * relativeY);
			float newY = PApplet.sin(angleChange) * relativeX + PApplet.cos(angleChange) * relativeY;
//			System.out.println("New Start X " + newX);
//			System.out.println("New Start Y " + newY);
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
	
	public void doCollision(Entity e) {
		Surface hit = null;
		int corner = -1;
		LineSide l = null;
		for(Surface s: surfaces) {
			if (s.intersects(e.getXMin(), e.getYMin(), e.nextXMin(), e.nextYMin())) {
				corner = 0;
				hit = s;
				l = s.findSide(e.getXMin(), e.getYMin());
			}
			else if (s.intersects(e.getXMin(), e.getYMax(), e.nextXMin(), e.nextYMax())) {
				corner = 1;
				hit = s;
				l = s.findSide(e.getXMin(), e.getYMax());
			}
			else if (s.intersects(e.getXMax(), e.getYMin(), e.nextXMax(), e.nextYMin())) {
				corner = 2;
				hit = s;
				l = s.findSide(e.getXMax(), e.getYMin());
			}
			else if (s.intersects(e.getXMax(), e.getYMax(), e.nextXMax(), e.nextYMax())) {
				corner = 3;
				hit = s;
				l = s.findSide(e.getXMax(), e.getYMax());
			}
			if (hit != null) {
				switch(corner) {
					case 0:
						e.moveBy(s.pushPerpendicular(e.nextXMin(), e.nextYMin(), l));
						break;
					case 1:
						e.moveBy(s.pushPerpendicular(e.nextXMin(), e.nextYMax(), l));
						break;
					case 2:
						e.moveBy(s.pushPerpendicular(e.nextXMax(), e.nextYMin(), l));
						break;
					case 3:
						e.moveBy(s.pushPerpendicular(e.nextXMax(), e.nextYMax(), l));
						break;
				}
			}
		}
	}
	
	public boolean checkColliding(Player player) {
		Surface hit = null;
		for(Surface s: surfaces) {
			if(s.isColliding(player.getHitbox())) {
				hit = s;
			}
		}
		if(hit!=null) {
//			System.out.println("hit!");
			return true;
		}else return false;
	}
	
}
