package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class Limb extends GraphicObject{
	public final static int JOINT_COLOR = 0xF6784200;
	public final static int LINK_COLOR = 0x00000000;
	public final static float JOINT_RADIUS = 0.6f;
	public final static float JOINT_DIAMETER = 2*JOINT_RADIUS;
	public final static float LINK_THICKNESS = 0.6f;
	
	private final float H = 4;
	
	private int numJoints;
	float x, y;
	private ArrayList<Float> theta;
	
	
	/**
	 * can get the number of joints just by looking at how long the theta array is.
	 * @param jointAngles
	 */
	public Limb(ArrayList<Float> jointAngles, float x_, float y_) {
		super(x_, y_, 0);
		theta = jointAngles;
		numJoints = theta.size();
	}
	
	public void draw() {
		app_.pushMatrix();
		app_.translate(x, y);
		for (int k=0; k<numJoints; k++) 
		{
			app_.rotate(theta.get(k));
			app_.noFill();
			app_.stroke(LINK_COLOR);
			app_.strokeWeight(LINK_THICKNESS);
			app_.line(0, 0, 0, H);
			app_.translate(0, H);
			app_.noStroke();
			app_.fill(JOINT_COLOR);
			app_.ellipse(0, 0, JOINT_DIAMETER, JOINT_DIAMETER);
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
	
	public float getTheta(int index) {
		return theta.get(index);
	}
	
	
}
