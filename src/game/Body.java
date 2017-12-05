package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class Body extends GraphicObject{
	private ArrayList<Limb> limbs;
	private Torso torso;
	private float moveIncrement = 1f;
	private int numLimbs;
	int limbJoints;
	private float t, x, y, a;
	float startArm = PApplet.PI/4;
	private ArrayList<KeyFrame> keys;
	boolean reachedLastFrame;
	AnimState state;
	ArrayList<KeyFrame> idleAnim, walkAnim, jumpAnim, hurtAnim, dyingAnim;
	
	public Body(int numLimbs_, int limbJoints_) {
		numLimbs = numLimbs_;
		limbJoints = limbJoints_;
		t = 0;
		reachedLastFrame = false;
		// initial state: first currentFrame
		KeyFrame start = keys.get(0);
		x = start.getX();
		y = start.getY();
		a = start.getA();

		limbs = new ArrayList<Limb>();
		
		for( int i = 0; i<numLimbs; i++)
			limbs.add(new Limb(start.getLimbsJoints().get(i), PApplet.cos(i*2*PApplet.PI/numLimbs), PApplet.sin(i*2*PApplet.PI/numLimbs)));
		
		torso = new Torso(3f);
		
	}
	
	public void draw() {
		app_.pushMatrix();
		app_.translate(x, y);
		app_.rotate(a);
		for(int i = 0; i<numLimbs; i++)
			limbs.get(i).draw();
		torso.draw();
		app_.popMatrix();
	}
	
	public void update(float dt)
	{
		t += dt;
		int segIndex = -1;
		
		for (int i=0; i<keys.size(); i++)
		{
			if (t < keys.get(i).getT())
			{
				segIndex = i;
				break;
			}
		}
		
		if (segIndex > 0)
		{
			//	interpolate between frames segIndex-1 and segIndex
			// time since beginning of the currentFrame segment
			float s = (t- keys.get(segIndex-1).getT()) /
						//	total duration of the currentFrame segment
					(keys.get(segIndex).getT() - keys.get(segIndex-1).getT());
			
			//	x displacement over the currentFrame segment
			x = keys.get(segIndex-1).getX() + s * (keys.get(segIndex).getX() - keys.get(segIndex-1).getX());
			y = keys.get(segIndex-1).getY() + s * (keys.get(segIndex).getY() - keys.get(segIndex-1).getY());
			a = keys.get(segIndex-1).getA() + s * (keys.get(segIndex).getA() - keys.get(segIndex-1).getA());
			
			/*
			 * the keyframes for joints are organized like each limb has its own array list of 
			 * joints and each joint is represented by a float for its angle.
			 */
			
			for(int i = 0; i<numLimbs; i++)
				for(int j = 0; j<limbJoints; j++) {
					limbs.get(i).setTheta(j, 
						keys.get(segIndex-1).getLimbsJoints().get(i).get(j) + s * (
								keys.get(segIndex).getLimbsJoints().get(i).get(j) -
								keys.get(segIndex-1).getLimbsJoints().get(i).get(j)));
				}
		}
		else if (segIndex == -1)
		{
			x = keys.get(keys.size()-1).getX();
			y = keys.get(keys.size()-1).getY();
			a = keys.get(keys.size()-1).getA();
			for(int i = 0; i<numLimbs; i++)
				for(int j = 0; j<limbJoints; j++)
					limbs.get(i).setTheta(j, keys.get(keys.size()-
											 1).getLimbsJoints().get(i).get(j));
			reachedLastFrame = true;
		}
		
	}
	
	/**
	 * Sets the body to the position at the frame selected.
	 * @param frame Which frame to jump to.
	 */
	public void jumpTo(int frame) {
		x = keys.get(frame).getX();
		y = keys.get(frame).getY();
		a = keys.get(frame).getA();
		for(int i = 0; i<numLimbs; i++)
			for(int j = 0; j<limbJoints; j++)
				limbs.get(i).setTheta(j, keys.get(frame).getLimbsJoints().get(i).get(j));
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getA() {
		return a;
	}
	
	public void moveUp() {
		y+=moveIncrement;
	}
	public void moveDown() {
		y-=moveIncrement;
	}
	public void moveLeft() {
		x-=moveIncrement;
	}
	public void moveRight() {
		x+=moveIncrement;
	}
	
	public void rotate(float angle) {
		a+=angle;
	}
	
	public int getNumLimbs () {
		return numLimbs;
	}
	
	public ArrayList<Limb> getLimbs(){
		return limbs;
	}
	
	/**
	 * For easier access of all the limbs' joint angles all in one spot (mostly for packaging
	 * it up to output to a file)
	 * @return The list of limbs and the angles for their associated joints.
	 */
	public ArrayList<ArrayList<Float>> getLimbAngles() {
		ArrayList<ArrayList<Float>> limbAngles = new ArrayList<>();
		for(int i = 0; i<numLimbs; i++) {
			ArrayList<Float> jointAngles = new ArrayList<Float>();
			for(int j = 0; j<limbJoints+1; j++) {
				jointAngles.add(limbs.get(i).getTheta(j));
			}
			limbAngles.add(jointAngles);
		}
		return limbAngles;
	}
	
	public boolean isAnimDone() {
		return reachedLastFrame;
	}
	
	public void restartAnim() {
		KeyFrame start = keys.get(0);
		x = start.getX();
		y = start.getY();
		a = start.getA();
		t= 0;
		reachedLastFrame = false;
	}
	
	
}
