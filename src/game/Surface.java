package game;

import java.awt.geom.Line2D;
import processing.core.PApplet;

/**	A line with which collisions can occur
 * 
 * @author Steph and Thomas
 *
 */
public class Surface extends GraphicObject {
	private float x2, y2;
	private float length_;
	private float perpAngle;
	
	/**	Creates a new surface, which extends from the specified point
	 * 	at the specified angle for the specified length. Specifically.
	 * 
	 * @param x			The x coordinate from which the surface starts
	 * @param y			The y coordinate from which the surface starts
	 * @param length	The length of the surface
	 * @param angle		The angle of rotation relative to the general reference frame
	 */
	public Surface (float x, float y, float length, float angle) {
		super(x, y, angle);
		length_ = length;
		update();
	}

	/**	
	 * 	Updates the perpendicular angle and the x and y coordinate of the
	 *  surface's endpoint. This is so that these operations do not have to
	 *  be performed every time these values are needed, such as in collision
	 *  calculations or when drawing.
	 */
	public void update() {
			perpAngle = getAngle() + PApplet.PI/2;
			x2 = getX() + length_ * PApplet.cos(getAngle());
			y2 = getY() + length_ * PApplet.sin(getAngle());
	}
	
	/**	Getter for the x coordinate of the endpoint of the surface
	 * 
	 * @return 	the x coordinate of the endpoint of the surface
	 */
	public float getX2() {
		return x2;
	}
	
	/**	Getter for the y coordinate of the endpoint of the surface
	 * 
	 * @return	the y coordinate of the endpoint of the surface
	 */
	public float getY2() {
		return y2;
	}
	
	/**	Sets the angle of the surface to the one provided,
	 * 	and then calls the update method
	 * 
	 * @param newAngle	the new angle for the surface
	 */
	public void setAngle(float newAngle) {
		super.setAngle(newAngle % (2* PApplet.PI));
		update();
	}
	
	/**
	 * 	Draws the surface. It is a line, and must be drawn relative to the original
	 * 	reference frame, not the reference frame of the platform.
	 * 
	 *  Walls are drawn to be green while floors and ceilings are red.
	 */
	public void draw() {
		
		// Draws walls as green rather than red
		if ((getAngle() < PApplet.PI/2 + PApplet.PI/64 && getAngle() > PApplet.PI/2 - PApplet.PI/64) || (getAngle() < 3*PApplet.PI/2 + PApplet.PI/64 && getAngle() > 3*PApplet.PI/2 - PApplet.PI/64)) {
			app_.stroke(0, 255, 0);
		}
		else {
			app_.stroke(255, 0, 0);
		}
		app_.line(getX(), getY(), x2, y2);
	}
	
	/**	Calculates the difference in x and y needed to push a point which has
	 * 	passed through the surface back out onto the given side of the line
	 * 
	 * @param badX	The x value of the point to be pushed
	 * @param badY	The y value of the point to be pushed
	 * @param l		The side of the line toward which the point should be pushed.
	 * 				If it is POS, it will be pushed in the direction of the perpendicular (the angle plus PI/2).
	 * 				If it is NEG, it will be pushed in the opposite direction.
	 * @return		The difference between the provided coordinates and the coordinates of the new location for that point.
	 * 				[0] is the x coordinate, and
	 * 				[1] is the y coordinate.
	 */
	public float[] pushPerpendicular(float badX, float badY, LineSide l) {
		float[] diff = {0, 0};
		float slope = 0;
		float perpSlope = 0;
		float additionalPop = 0;
		float multiplier = 0;
		float tempAngle = 0;
		boolean isVertical = false;
		// Check which side of the line to push the point toward
		switch(l) {
			case POS:
				// Calculate the slope
				slope = PApplet.tan(getAngle());
				
				// Keeps the perpendicular angle between 0 and 2*PI
				tempAngle = perpAngle % (2*PApplet.PI);
				
				// Don't calculate the slope of a vertical perpendicular.
				// Instead, set it to a weird, noticeable value and mark that 
				// The point needs to be pushed vertically
				if (((tempAngle < PApplet.PI/2 + PApplet.PI/80 && tempAngle > PApplet.PI/2 - PApplet.PI/80)) || ((tempAngle < 3*PApplet.PI/2 + PApplet.PI/80 && tempAngle > 3*PApplet.PI/2 - PApplet.PI/80))) {
					isVertical = true;
					perpSlope = -10101010;
				}
				else {
					perpSlope = PApplet.tan(tempAngle);
				}
				
				// Push a very small additional amount in the positive direction
				additionalPop = 0.05f;
				break;
				
			case NEG:
				// Calculate the slope
				slope = PApplet.tan(getAngle());
				
				// Keeps the perpendicular angle between 0 and 2*PI
				tempAngle = perpAngle % (2*PApplet.PI);
				
				// Don't calculate the slope of a vertical perpendicular.
				// Instead, set it to a weird, noticeable value and mark that 
				// The point needs to be pushed vertically
				if (((tempAngle < PApplet.PI/2 + PApplet.PI/80 && tempAngle > PApplet.PI/2 - PApplet.PI/80)) || ((tempAngle < 3*PApplet.PI/2 + PApplet.PI/80 && tempAngle > 3*PApplet.PI/2 - PApplet.PI/80))) {
					isVertical = true;
					perpSlope = -101010;
				}
				else {
					perpSlope = PApplet.tan(tempAngle);
				}
				
				// Push a very small additional amount in the negative direction 
				additionalPop = -0.05f;
				break;
				
			// If somehow the player was intersecting the surface before this check, print an error
			case ON:
				System.out.println("ERROR, SOMEWHERE THERE WAS A VALID POSITION ON A LINE");
				break;
		}
		
		// Calculates the amount which the point will get pushed out
		// When the perpendicular isn't vertical (non-flat floor)
		if (!isVertical) {
			// Finds the value of "b" for the lines in order to represent them
			// in the form "y = ax + b," where "a" is the slope of each line.
			// The formula is "b = y - ax"
			float b1 = getY() - slope*getX();
			float b2 = badY - perpSlope*badX;
			
			// Calculates the x coordinate of the intersection of the perpendicular and the surface
			diff[0] = -(b1-b2)/(slope-perpSlope);
			
			// Calculates the y coordinate of that intersection point
			diff[1] = perpSlope * diff[0] + b2;
			
			// Moves the additional amount in the direction of the perpendicular angle
			diff[0] += additionalPop * PApplet.cos(perpAngle) - badX;
			diff[1] += additionalPop * PApplet.sin(perpAngle) - badY;
		}
		
		// When the perpendicular is vertical (flat floor)
		else {
			// Ensure that the point is not pushed horizontally
			diff[0] = 0;
			
			// Calculates the vertical distance to the line, and then adds the
			// additional pop, being sure to account for the positive/negative value
			// of the sine of the perpendicular angle (positive if it is pointing up, 
			// negative if going down)
			diff[1] = getY() + additionalPop * PApplet.sin(perpAngle)  - badY;
		}
		
		// Return the array of the change in x and y
		return diff;
	}
	
	/**	Finds the side of the surface on which the given point lies.
	 * 	The positive side is the side where a ray pointing in the 
	 *  direction of perpAngle lies, while the negative side is the
	 *  opposite side.
	 * 
	 * @param testX		The x coordinate of the point to test
	 * @param testY		The y coordinate of the point to test
	 * @return			POS if positive, NEG if negative, ON if it lies on the line.
	 */
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
	
	/**	Checks if a line between the given points intersects this surface
	 * 
	 * @param testX1	The x coordinate of the starting point of the line being tested
	 * @param testY1	The y coordinate of the starting point of the line being tested
	 * @param testX2	The x coordinate of the endpoint of the line being tested
	 * @param testY2	The y coordinate of the endpoint of the line being tested
	 * @return			Boolean value, true if the lines intersect, false otherwise
	 */
	public boolean intersects(float testX1, float testY1, float testX2, float testY2) {
		// Use a java library function which handles line intersections
		return (Line2D.linesIntersect((double)getX(), (double)getY(),
				  (double)x2,     (double)y2,
				  (double)testX1, (double)testY1,
				  (double)testX2, (double)testY2));
	}
}
