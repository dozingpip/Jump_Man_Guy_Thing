package game;

import java.io.IOException;
import java.util.ArrayList;

import animationEditor.KeyFrame;
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
	Animations anims;
	float torsoSize = 8f;
	Hitbox hitbox;
	float hitboxAngle = PApplet.PI/4;
	
	public Body(String animFile, int numLimbs_, int limbJoints_) {
		numLimbs = numLimbs_;
		limbJoints = limbJoints_;
		KeyFrame start;
		
		try {
			anims = new Animations(animFile);
			if(anims.getNumLimbs()!=numLimbs || anims.getNumJoints()!=limbJoints) {
				System.out.println("The limb/ joints numbers in the animation file and body set here differ./n"
						+ "body: limbs = "+ numLimbs+", anim file: limbs = "+ anims.getNumLimbs()+"/n"
						+ "body: joints = "+ limbJoints+", anim file: joints = "+ anims.getNumJoints());
			}
			setState(AnimState.IDLE);
			start = keys.get(0);
		} catch (IOException e) {
			System.out.println("Failed opening specified file, so using default file.");
			keys = new ArrayList<KeyFrame>();
			
			//default position of the body.
			ArrayList<ArrayList<Float>> limbAngles = new ArrayList<>();
			ArrayList<Float> startJointPos0 = new ArrayList<Float>();
			startJointPos0.add(-2.3561945f);
			startJointPos0.add(0f);
			limbAngles.add(startJointPos0);
			ArrayList<Float> startJointPos1 = new ArrayList<Float>();
			startJointPos1.add(-1.7671458f);
			startJointPos1.add(0f);
			limbAngles.add(startJointPos1);
			ArrayList<Float> startJointPos2 = new ArrayList<Float>();
			startJointPos2.add(3.3379426f);
			startJointPos2.add(0f);
			limbAngles.add(startJointPos2);
			ArrayList<Float> startJointPos3 = new ArrayList<Float>();
			startJointPos3.add(3.926991f);
			startJointPos3.add(0f);
			limbAngles.add(startJointPos3);
			start = new KeyFrame(0, 0, 0, -0.7853982f, limbAngles);
			keys.add(start);
			
		}
		t = 0;
		reachedLastFrame = false;

		limbs = new ArrayList<Limb>();
		for( int i = 0; i<numLimbs; i++) {
			limbs.add(new Limb(start.getLimbsJoints().get(i), (torsoSize/2)*PApplet.cos(i*2*PApplet.PI/numLimbs), (torsoSize/2)*PApplet.sin(i*2*PApplet.PI/numLimbs)));
		}
		
		jumpTo(0);
		float xMin = -(torsoSize/2);
		float xMax = (torsoSize/2);
		float yMin = -(torsoSize/2)-6.5f;
		float yMax = (torsoSize/2);
		hitbox = new Hitbox(xMin, yMin, xMax, yMax);
		torso = new Torso(torsoSize);
		
	}
	
	public void setState(AnimState state_) {
		state = state_;
		keys = getStateAnim();
	}
	
	public ArrayList<KeyFrame> getStateAnim(){
		switch(state) {
			case IDLE:
				return anims.getIdle();
			case WALK:
				return anims.getWalk();
			case JUMP:
				return anims.getJump();
			case HURT:
				return anims.getHurt();
			case DYING:
				return anims.getDying();
			default:
				return null;
		}
	}
	
	public void draw() {
		app_.pushMatrix();
		app_.translate(x, y);
		app_.rotate(a);

		app_.noStroke();
		torso.draw();
		app_.pushMatrix();
		app_.rotate(hitboxAngle);
		hitbox.draw();
		app_.popMatrix();
		for(int i = 0; i<numLimbs; i++)
			limbs.get(i).draw();
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
			//if the animation is not the idle one, stop playing when done.  If it is the idle animation, just restart it.
			if(state!=AnimState.IDLE)
				setState(AnimState.IDLE);
			else
				t = 0;
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
	
	public void idle() {
		setState(AnimState.IDLE);
	}

	public void jump() {
		//setState(AnimState.JUMP);
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
	
	public void walk() {
		setState(AnimState.WALK);
	}
	
	public void rotate(float angle) {
		a+=angle;
	}
	
	public int getNumLimbs () {
		return numLimbs;
	}
	
	public int getNumJoints () {
		return limbJoints;
	}
	
	public ArrayList<Limb> getLimbs(){
		return limbs;
	}
	
	public void hit() {
		
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
			for(int j = 0; j<limbJoints; j++) {
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
