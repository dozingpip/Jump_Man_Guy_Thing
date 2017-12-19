package game;

import java.io.IOException;
import java.util.ArrayList;

import animationEditor.KeyFrame;
import processing.core.PApplet;

/**
 * Handles animation and drawing for any one entity in the game.
 * @author Fellamara
 *
 */
public class Body extends GraphicObject{
	private ArrayList<Limb> limbs;
	private Torso torso;
	
	// how much to move when moving in the animation editor
	private float moveIncrement = 1f;
	private int numLimbs;
	private int limbJoints;
	private float t, x, y, a;
	private ArrayList<KeyFrame> keys;
	
	//whether or not the last frame of current animation has been reached.
	private boolean reachedLastFrame;
	
	// current animation state of this body
	private AnimState state;
	
	// all the animations related to this body, like the idle, walk, and jump.
	private Animations anims;
	
	// diameter of the torso of this body
	private float torsoSize;
	
	/**
	 * 
	 * @param animFile animation file to load
	 * @param numLimbs_ number of limbs
	 * @param limbJoints_ number of joints per limb
	 * @param torsoSize_ diameter of torso
	 */
	public Body(String animFile, int numLimbs_, int limbJoints_, float torsoSize_) {
		numLimbs = numLimbs_;
		limbJoints = limbJoints_;
		KeyFrame start;
		torsoSize = torsoSize_;
		
		try {
			anims = new Animations(animFile);
			// the numbers for limbs and joints must be the same with the animation file and this body.
			if(anims.getNumLimbs()!=numLimbs || anims.getNumJoints()!=limbJoints) {
				System.out.println("The limb/ joints numbers in the animation file and body set here differ./n"
						+ "body: limbs = "+ numLimbs+", anim file: limbs = "+ anims.getNumLimbs()+"/n"
						+ "body: joints = "+ limbJoints+", anim file: joints = "+ anims.getNumJoints());
			}
			// set the playing animation to idle by default.
			setState(AnimState.IDLE);
		} catch (IOException e) {
			System.out.println("Failed opening specified file.");
		}
		
		// make sure everything is set up to start at the first frame of the animation.
		start = keys.get(0);
		t = 0;
		reachedLastFrame = false;
		
		// creating and placing all the limbs (their positions will probably eventually be changed by an update anyway)
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
	
	/**
	 * set the state, unless it's already the state you're trying to set it to.
	 * @param state_
	 */
	public void setState(AnimState state_) {
		if(state!= state_) {
			state = state_;
			keys = getStateAnim();
		}
	}
	
	/**
	 * get the set of keyframes based on the current state.
	 * @return
	 */
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
	
	/**
	 * set the currently playing animation to idle
	 */
	public void idle() {
		setState(AnimState.IDLE);
	}

	/**
	 * set the currently playing animation to walk
	 */
	public void walk() {
		setState(AnimState.WALK);
	}

	/**
	 * set the currently playing animation to jump
	 */
	public void jump() {
		setState(AnimState.JUMP);
	}
	
	/**
	 * set the currently playing animation to die
	 */
	public void die() {
		setState(AnimState.DYING);
	}
	
	/**
	 * set the currently playing animation to hurt
	 */
	public void hurt() {
		setState(AnimState.HURT);
	}
	
	/**
	 * draw the body.
	 */
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
	
	/**
	 * change from one keyframe to the next based on the time that has passed since the last update and the changes between each keyframe
	 * (so interpolate between one keyframe and the next)
	 * @param dt
	 */
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
			// if the animation is not jump, make it start again.  (We only want to play jump once and not play an animation again until
			//  you land again.
			if(state!=AnimState.JUMP)
				t = 0;
		}
		
	}
	
	/**
	 * is the animation set by state done?
	 * @return
	 */
	public boolean isAnimDone() {
		return reachedLastFrame;
	}
	
	/**
	 * Sets the body to the position at the frame selected.  Only to be used by the animation editor (or to set the frame to 0).
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
	
	/**
	 * get the x of the body (relevant to player position)
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * get the y of the body (relevant to player position)
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * get rotation of the body
	 * @return
	 */
	public float getA() {
		return a;
	}
	
	/**
	 *  number of limbs the body was set up to have.
	 * @return
	 */
	public int getNumLimbs () {
		return numLimbs;
	}
	
	/**
	 * number of joints the body was set up to have.
	 * @return
	 */
	public int getNumJoints () {
		return limbJoints;
	}
	
	/**
	 * the list of limbs the body has.
	 * @return
	 */
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
			for(int j = 0; j<limbJoints; j++) {
				jointAngles.add(limbs.get(i).getTheta(j));
			}
			limbAngles.add(jointAngles);
		}
		return limbAngles;
	}
	
	/**
	 * moves the body up (only to be used in the animation editor)
	 */
	public void moveUp() {
		y+=moveIncrement;
	}
	
	/**
	 * moves the body down (only to be used in the animation editor)
	 */
	public void moveDown() {
		y-=moveIncrement;
	}
	
	/**
	 * moves the body to the left (only to be used in the animation editor)
	 */
	public void moveLeft() {
		x-=moveIncrement;
	}
	
	/**
	 * moves the body to the right (only to be used in the animation editor)
	 */
	public void moveRight() {
		x+=moveIncrement;
	}
	
	/**
	 * adjusts the body's angle (only to be used in the animation editor)
	 */
	public void rotate(float angle) {
		a+=angle;
	}
	
	/**
	 * only to be used to replay an animation that's currently loaded in the editor.
	 */
	public void restartAnim() {
		KeyFrame start = keys.get(0);
		x = start.getX();
		y = start.getY();
		a = start.getA();
		t= 0;
		reachedLastFrame = false;
	}
}
