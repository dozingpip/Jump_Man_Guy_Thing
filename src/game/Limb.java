package game;

import java.util.ArrayList;

import processing.core.PApplet;

/**
 * a collection of lines, in between the lines there are joints so you can rotate the angles the lines look to be draw at,
 * and it affects the angles of the lines later in the sequence.
 * @author Steph and Thomas
 *
 */
public class Limb extends GraphicObject{
	private final static int JOINT_COLOR = 0xFF000000;
	private final static int LINK_COLOR = 0xFF000000;
	private final static int LINK_SELECTED_COLOR = 0xFF07BA;
	private final static float JOINT_RADIUS = 0.3f;
	private final static float JOINT_DIAMETER = 2*JOINT_RADIUS;
	private final static float LINK_THICKNESS = 0.6f;
	private float linkColor = LINK_COLOR;
	
	private float linkLength;
	
	private int numJoints;
	private ArrayList<Float> theta;
	
	/**
	 * can get the number of joints just by looking at how long the theta array is.
	 * @param jointAngles list of the angles the joints should be at
	 * @param x_ start x of the limb
	 * @param y_ start y of the limb
	 * @param linkLength_ length of each line in the limb
	 */
	public Limb(ArrayList<Float> jointAngles, float x_, float y_, float linkLength_) {
		super(x_, y_, 0);
		theta = jointAngles;
		numJoints = theta.size();
		linkLength = linkLength_;
	}
	
	/**
	 * draws the limb, one joint and segment at a time.
	 */
	public void draw() {
		app_.pushMatrix();
		app_.translate(getX(), getY());
		for (int k=0; k<numJoints; k++) 
		{
			app_.noStroke();
			app_.fill(JOINT_COLOR);
			
			// draw the joint the link comes out of first
			app_.ellipse(0, 0, JOINT_DIAMETER, JOINT_DIAMETER);
			
			// then rotate to however that joint should be
			app_.rotate(theta.get(k));
			app_.noFill();
			app_.stroke(linkColor);
			app_.strokeWeight(LINK_THICKNESS);
			
			// draw the line from that joint out to length specified by link length
			app_.line(0, 0, 0, linkLength);
			
			// move the length of the line away so we can make the next joint and line
			app_.translate(0, linkLength);
		}
		app_.popMatrix();
	}
	
	/**
	 * Set the joint to the given value.
	 * @param whichJoint
	 * @param a_
	 */
	public void setTheta(int whichJoint, float a_) {
		theta.set(whichJoint, a_);
	}
	
	/**
	 * increment a joint's rotation by so much.
	 * @param whichJoint
	 * @param incThetaBy
	 */
	public void rotateJoint(int whichJoint, float incThetaBy) {
		setTheta(whichJoint, (getTheta(whichJoint)+incThetaBy) %(2*PApplet.PI));
	}
	
	/**
	 * get the theta/ angle of the joint at the specified index
	 * @param index the joint to look at
	 * @return the angle of the joint
	 */
	public float getTheta(int index) {
		return theta.get(index);
	}
	
	/**
	 * make the link look different if it's selected (in the animation editor).
	 */
	public void select() {
		linkColor = LINK_SELECTED_COLOR;
	}
	
	/**
	 * make the link go back to the normal color when it's not selected (only in the animation editor).
	 */
	public void deselect() {
		linkColor = LINK_COLOR;
	}
	
}
