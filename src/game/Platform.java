package game;

import processing.core.PApplet;

/**	A collection of surfaces which can be rotated about a provided center.
 * 
 * @author Thomas
 *
 */
public class Platform extends GraphicObject{
	
	// For debug purposes, draw the point around which the platform rotates
	private static final float DOT_RADIUS = 0.5f;
	
	
	private Surface[] surfaces;
	
	// Center about which this platform rotates
	private float x, y;
	
	// The angle of rotation of the platform
	private float angle;
	
	/**	A simple platform created from a list of arbitrarily placed surfaces
	 * 
	 * @param x_			The x coordinate of the platform's origin
	 * @param y_			The y coordinate of the platform's origin
	 * @param angle_		The angle of rotation of the platform about its origin
	 * @param surfaces_		The list of surfaces which are part of this platform
	 */
	public Platform(float x_, float y_, float angle_, Surface[] surfaces_) {
		surfaces = surfaces_;
		x = x_;
		y = y_;
		angle = angle_;
		updateRotation(angle);
	}
	
	/**	A platform created from a starting surface and a list of lengths and angles for consecutive surfaces.
	 * 	Each consecutive surface's starting point is at the endpoint of the previous one.
	 * 
	 * @param x_				The x coordinate of the platform's origin
	 * @param y_				The y coordinate of the platform's origin
	 * @param angle_			The angle of rotation of the platform about its origin
	 * @param firstSurface		The first surface object in the chain
	 * @param lengths			The lengths of the consecutive surfaces
	 * @param angles			The angles of rotation of the consecutive surfaces, relative to the previous one
	 */
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
		app_.noFill();
		app_.stroke(0, 0, 0);
		app_.ellipse(x, y, DOT_RADIUS*2, DOT_RADIUS*2);
		for(Surface s : surfaces) {
			s.draw();
		}
	}
	
	public boolean testCollision(Entity e) {
		boolean isHit = false;
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
				isHit = true;
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
				hit = null;
			}
		}
		boolean isGrounded = false;
		for(Surface s: surfaces) {
			System.out.println(s.intersects(e.getXMin(), e.getYMin(), e.getXMin(), e.getYMin() - 0.1f) || s.intersects(e.getXMax(), e.getYMin(), e.getXMax(), e.getYMin() - 0.1f));
			if (s.intersects(e.getXMin(), e.getYMin(), e.getXMin(), e.getYMin() - 0.1f) || s.intersects(e.getXMax(), e.getYMin(), e.getXMax(), e.getYMin() - 0.1f)) {
				isGrounded = true;
			}
		}
		System.out.println("IS GROUNDED " + isGrounded);
		return isGrounded;
	}
}
