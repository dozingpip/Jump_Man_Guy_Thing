package game;

import java.awt.geom.Line2D;

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
	
	public static float[] lineIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (denom == 0.0) { // Lines are parallel.
			return null;
		}
		float ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denom;
		float ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denom;
		if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
			// Get the intersection point.
			return new float[] {(x1 + ua*(x2 - x1)), (y1 + ua*(y2 - y1))};
		}
		return null;
	}
	
	public float[] pushPerpendicular(float badX, float badY, LineSide l) {
		float[] diff = {0, 0};
		float slope = 0;
		float perpSlope = 0;
		float additionalPop = 0;
		float multiplier = 0;
		float tempAngle = 0;
		boolean isVertical = false;
		switch(l) {
			case POS:
				slope = PApplet.tan(getAngle());
				tempAngle = perpAngle;
				System.out.println("ANGLE " + tempAngle);
				if (((tempAngle < PApplet.PI/2 + PApplet.PI/2000 && tempAngle > PApplet.PI/2 - PApplet.PI/2000)) || ((tempAngle < 3*PApplet.PI/2 + PApplet.PI/2000 && tempAngle > 3*PApplet.PI/2 - PApplet.PI/2000))) {
					isVertical = true;
					perpSlope = -10101010;
				}
				else {
					perpSlope = PApplet.tan(tempAngle);
				}
				additionalPop = 0.05f;
				System.out.println("IS POSITIVE");
				break;
			case NEG:
				slope = PApplet.tan(getAngle());
				tempAngle = perpAngle % 2*PApplet.PI;
				if (((tempAngle < PApplet.PI/2 + PApplet.PI/2000 && tempAngle > PApplet.PI/2 - PApplet.PI/2000)) || ((tempAngle < 3*PApplet.PI/2 + PApplet.PI/2000 && tempAngle > 3*PApplet.PI/2 - PApplet.PI/2000))) {
					isVertical = true;
					perpSlope = -101010;
				}
				else {
					perpSlope = PApplet.tan(tempAngle);
				}
				additionalPop = -0.05f;
				System.out.println("IS NEGATIVE");
				break;
			default:
				break;
		}
		if (!isVertical) {
			System.out.println("PERP SLOPE " + perpSlope + " BADX = " + badX);
			float b1 = getY() - slope*getX();
			float b2 = badY - perpSlope*badX;
			diff[0] = -(b1-b2)/(slope-perpSlope);
			diff[1] = perpSlope * diff[0] + b2;
			diff[0] += additionalPop * PApplet.cos(perpAngle) - badX;
			diff[1] += additionalPop * PApplet.sin(perpAngle) - badY;
		}
		else {
			diff[0] = 0;
			diff[1] = getY() + additionalPop - badY;
		}
		
		return diff;
	}
	
	public LineSide findSide(float testX, float testY) {
		float det = (x2 - getX())*(testY - getY()) - ((y2 - getY())*(testX - getX()));
		if (det < 0) {
			return LineSide.NEG;
		}
		else if (det > 0) {
			return LineSide.POS;
		}
		else {
			return LineSide.ON;
		}
	}
	
	public boolean intersects(float testX1, float testY1, float testX2, float testY2) {
		return (Line2D.linesIntersect((double)getX(), (double)getY(),
									  (double)x2,     (double)y2,
									  (double)testX1, (double)testY1,
									  (double)testX2, (double)testY2));
//		boolean surfIsVertical = getX() == x2;
//		// The slope of the surface
//		float sA;
//		float sB;
//		boolean testIsVertical = testX1 == testX2;
//		// The slope of the tested line
//		float tA;
//		float tB;
//		
//		if (!surfIsVertical) {
//			sA = (y2 - getY())/(x2 - getX());
//		}
//		if (!testIsVertical) {
//			tA = (testY2 - testY1)/(testX2 - testX1);
//		}
//		if (!surfIsVertical && !testIsVertical) {
//			
//		}
	}
	
	public boolean isInside(float x, float y) {
		
		float dx = x - getX(), dy = y - getY();
		float ca = PApplet.cos(getAngle()), sa = PApplet.sin(getAngle());
//		float ca = (float) Math.cos(angle_), sa = (float) Math.sin(angle_);
		
		float rdx = ca*dx + sa*dy;
		float rdy = -sa*dx + ca*dy;
		
		return ((rdx >= 0) && (rdx <= length_) && (rdy >= 0) && (rdy <=1f));
	}
}
