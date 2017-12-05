package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class Limb extends PApplet implements ApplicationConstants{
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
		theta = jointAngles;
		numJoints = theta.size();
		x = x_;
		y = y_;
	}
	
	public void draw(PApplet sketch) {
		sketch.pushMatrix();
		//sketch.translate(x,  y);
		//base joint is first theta.
		for (int k=0; k<numJoints; k++) 
		{
			sketch.rotate(theta.get(k));
			sketch.noFill();
			sketch.stroke(LINK_COLOR);
			sketch.strokeWeight(LINK_THICKNESS);
			sketch.line(0, 0, 0, H);
			sketch.translate(0, H);
			sketch.noStroke();
			sketch.fill(JOINT_COLOR);
			sketch.ellipse(0, 0, JOINT_DIAMETER, JOINT_DIAMETER);
		}
		sketch.popMatrix();
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
		setTheta(whichJoint, (getTheta(whichJoint)+incThetaBy) %(2*PI));
	}
	
	public float getTheta(int index) {
		return theta.get(index);
	}
	
	
}
