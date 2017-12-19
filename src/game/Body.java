package game;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import animationEditor.FileInOutMachine;
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
	float torsoSize;
	
	public Body(String animFile, int numLimbs_, int limbJoints_, float torsoSize_) {
		numLimbs = numLimbs_;
		limbJoints = limbJoints_;
		KeyFrame start;
		torsoSize = torsoSize_;
		
		try {
			anims = new Animations(animFile);
			if(anims.getNumLimbs()!=numLimbs || anims.getNumJoints()!=limbJoints) {
				System.out.println("The limb/ joints numbers in the animation file and body set here differ./n"
						+ "body: limbs = "+ numLimbs+", anim file: limbs = "+ anims.getNumLimbs()+"/n"
						+ "body: joints = "+ limbJoints+", anim file: joints = "+ anims.getNumJoints());
			}
			setState(AnimState.IDLE);
		} catch (IOException e) {
			System.out.println("Failed opening specified file.");
		}
		start = keys.get(0);
		t = 0;
		reachedLastFrame = false;

		limbs = new ArrayList<Limb>();
		for( int i = 0; i<numLimbs; i++) {
			limbs.add(new Limb(start.getLimbsJoints().get(i), 
					(torsoSize/2)*PApplet.cos(i*2*PApplet.PI/numLimbs),
					(torsoSize/2)*PApplet.sin(i*2*PApplet.PI/numLimbs),
					torsoSize/2));
		}
		
		jumpTo(0);
		torso = new Torso(torsoSize);
	}
	
	public Body(int numLimbs_, int limbJoints_, float torsoSize_) {
		numLimbs = numLimbs_;
		limbJoints = limbJoints_;
		torsoSize = torsoSize_;
		limbs = new ArrayList<Limb>();
		ArrayList<Float> j = new ArrayList<Float>();
		for(int k = 0; k<limbJoints; k++) {
			j.add(0f);
		}
		
		for( int i = 0; i<numLimbs; i++) {
			limbs.add(new Limb(j, 
					(torsoSize/2)*PApplet.cos(i*2*PApplet.PI/numLimbs),
					(torsoSize/2)*PApplet.sin(i*2*PApplet.PI/numLimbs),
					torsoSize/2));
		}
		torso = new Torso(torsoSize);
	}
	
	public void setState(AnimState state_) {
		if(state!= state_) {
			state = state_;
			keys = getStateAnim();
		}
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
					limbs.get(i).setTheta(j, keys.get(keys.size()-1).getLimbsJoints().get(i).get(j));
			reachedLastFrame = true;
			//if the animation is not the idle one, stop playing when done.  If it is the idle animation, just restart it.
			if(state!=AnimState.JUMP)
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
		setState(AnimState.JUMP);
	}
	
	public void die() {
		setState(AnimState.DYING);
	}
	
	public void hurt() {
		setState(AnimState.HURT);
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
