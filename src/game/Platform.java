package game;

import processing.core.PApplet;

/**	A collection of surfaces which can be rotated about a provided center.
 * 
 * @author Steph and Thomas
 *
 */
public class Platform extends GraphicObject{
	
	// For debug purposes, draw the point around which the platform rotates
	private static final float DOT_RADIUS = 0.5f;
	
	// The list of surfaces in this platform
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
	 * 	Ensure the arrays of lengths and angles are of equal size.
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
		// Iterate through the lists of lengths and surfaces, creating new surfaces 
		for(int i = 0; i < lengths.length; i++) {
			surfaces[i+1] = new Surface(surfaces[i].getX2(), surfaces[i].getY2(), lengths[i], surfaces[i].getAngle() + angles[i]);
		}
	}
	
	/**	Change the angle of rotation of every surface by the given amount
	 * 
	 * @param angleChange
	 */
	public void updateRotation(float angleChange) {
		// Iterates through all the surfaces
		for(Surface s : surfaces) {
			
			// Changes the angle by the amount given
			s.setAngle(s.getAngle() + angleChange);
			
			// Gets the difference between the starting point of the surface and the origin of the platform
			float relativeX = s.getX() - x;
			float relativeY = s.getY() - y;
			
			// Performs a rotation matrix multiplication operation on the vector of relativeX and relativeY
			//	[ Cos*X	-Sin*Y	] = [	newX	]
			//	[ Sin*X	 Cos*Y	] =	[	newY	]
			float newX = PApplet.cos(angleChange) * relativeX + (-PApplet.sin(angleChange) * relativeY);
			float newY = PApplet.sin(angleChange) * relativeX + PApplet.cos(angleChange) * relativeY;
			
			// Moves the surface to the new position relative to the platform
			s.setX(newX + x);
			s.setY(newY + y);
			
			// Updates the collisions and draw information in accordance with the changes
			s.update();
		}
	}
	
	/**	Gets the angle of rotation of the platform
	 * 
	 * @return The angle of rotation of the platform
	 */
	public float getAngle() {
		return angle;
	}
	
	/**	Changes the angle of rotation of the platform to the new value
	 * 
	 * @param 	The new angle of rotation for the platform
	 */
	public void setAngle(float angle_) {
		// Update all the surfaces by the difference
		updateRotation(angle_ - angle);
		// Replace the angle with the new one
		angle = angle_;
	}
	
	/**	Calls the draw method of all the surfaces
	 */
	public void draw() {
//		app_.noFill();
//		app_.stroke(0, 0, 0);
//		app_.ellipse(x, y, DOT_RADIUS*2, DOT_RADIUS*2);
		for(Surface s : surfaces) {
			s.draw();
		}
	}
	
	/**	Checks if a given entity is colliding with any of this platform's surfaces
	 * 
	 * @param e		The entity to test collision with
	 * @return		[0] Whether a collision occurred, and
	 * 				[1] Whether the entity is grounded on this platform 
	 */
	public boolean[] testCollision(Entity e) {
		boolean[] result = {false, false};
		Surface hit = null;
		int corner = -1;
		LineSide l = null;
		// Iterate through all the surfaces
		for(Surface s: surfaces) {
			// Check if the change in the coordinates of the hitbox's corners
			// Between the current position and the projected next position
			// form a line which is incident with the current surface
			if (s.intersects(e.getXMin(), e.getYMin(), e.nextXMin(), e.nextYMin())) {
				
				// Store which corner collided
				corner = 0;
				
				// store the surface with which an intersection was found
				hit = s;
				
				// get the side on which the corner was before the projected move
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
			// If there was a collision
			if (hit != null) {
				// Store in result that a collision occurred
				result[0] = true;

				float[] diff = null;
				// Check which corner crossed, and push it back perpendicular to the surface
				switch(corner) {
					case 0:
						diff = s.pushPerpendicular(e.nextXMin(), e.nextYMin(), l);
						break;
					case 1:
						diff = s.pushPerpendicular(e.nextXMin(), e.nextYMax(), l);
						break;
					case 2:
						diff = s.pushPerpendicular(e.nextXMax(), e.nextYMin(), l);
						break;
					case 3:
						diff = s.pushPerpendicular(e.nextXMax(), e.nextYMax(), l);
						break;
				}
				// If there was an error in collision checking, undo player movement
				// Catches most errors which get players caught on slopes
				if (diff[2] == -1) {
					diff[0] = e.getXMin() - e.nextXMin();
					diff[1] = e.getYMin() - e.nextYMin();
				}
				e.moveBy(diff);
				// Reset hit to null
				hit = null;
			}
		}
		
		// Determine if the entity is grounded after all of the pushes
		// Iterate through the surfaces
		for(Surface s: surfaces) {	
			// Tests some small lines below the bottom corners of the hitbox for intersections 
			// with the surface to determine if there is ground beneath the entity
			if (s.intersects(e.getXMin(), e.getYMin(), e.getXMin(), e.getYMin() - 0.1f) || s.intersects(e.getXMax(), e.getYMin(), e.getXMax(), e.getYMin() - 0.1f)) {
				result[1] = true;
			}
		}
		return result;
	}
}
