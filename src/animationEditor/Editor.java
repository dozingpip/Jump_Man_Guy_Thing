package animationEditor;

import java.io.IOException;
import java.util.ArrayList;

import processing.core.PApplet;
import game.Body;
import game.GraphicObject;
import animationEditor.KeyFrame;


public class Editor extends PApplet implements game.ApplicationConstants {
	
	private Body body;
	
	private Menu editAnimUI;
	private Menu playAnimUI;
	private Menu jumpToUI;
	private Menu timeSelectUI;
	private Menu startUI;
	private Menu limbsUI;
	
	private final float ANGLE_INCR = PI/16;
	private boolean animate = false;
	private float lastTime;
	private long frame = 0L;
	ArrayList<KeyFrame> keyframes;
	int currentEditingFrame = 1;
	KeyFrame start;
	int limbSelected;
	int jointSelected;
	float buttonY = WORLD_Y_MIN+6f;
	int jointsOnLimbs = 2;
	int limbsOnBody = 4;
	int timeSelected = 1;
	boolean editAnimation = false;
	boolean showJumpTo = false;
	boolean startScreen = false;
	float defaultFrameLength = 1f;
	float timeReset = 0;
	float frameSelectTime;
	
	
	public void settings() 
	{
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void setup() 
	{
		setupGraphicClasses_();
		initStartUI();
	}

	
	public void draw() 
	{	
		frame++;
		if (frame % 5 == 0) {
			background(167);
			
			pushMatrix();
			
			translate(ORIGIN_X, ORIGIN_Y);
	 		
	 		scale(WORLD_TO_PIXELS_SCALE, -WORLD_TO_PIXELS_SCALE);	
			
	 		// horizontal line for the "ground"
			stroke(255, 0, 0);
			strokeWeight(0.1f);
			line(WORLD_X_MIN, 0, WORLD_X_MAX, 0);
			line(0, WORLD_Y_MIN, 0, WORLD_Y_MAX);
			fill(0, 255, 255);
			ellipse(0, 0, 1, 1);
			
			if(startScreen)
				startUI.draw();
			else {
				body.draw();
				if(editAnimation) {
					editAnimUI.draw();
					limbsUI.draw();
				}else
					playAnimUI.draw();
	
				if(showJumpTo)
					jumpToUI.draw();
				
				timeSelectUI.draw();
			}
			popMatrix();
		}
		
		float t = millis()-timeReset-frameSelectTime;
		
		if (animate)
		{
			
			//	time in seconds since last update: (t-lastTime_)*0.001f
			float dt = (t-lastTime)*0.001f;
			
			if(!body.isAnimDone()) {
				// update the time keeper button
				updateTime(t/1000f);
				body.update(dt);
			}else {
				animate = false;
				println("Animation complete.");
			}
		}

		lastTime = t;
		
	}
	
	/**
	 * Wanted to add some UI for selecting specific joints in the case that there are more than
	 *  2 and having it rely on keyboard controls would get messy, but didn't finish doing 
	 *  that.
	 * @param index
	 * @param joint
	 */
	public void selectJoint(int index, int joint) {
		limbSelected = index;
		jointSelected = joint;
	}
	
	/**
	 * Selecting a limb for editing with keyboard controls
	 * @param index
	 */
	public void selectLimb(int index) {
		limbSelected = index;
		println("Limb "+index+ " selected!");
	}
	
	public void keyReleased() 
	{
		// make sure to only allow keyboard control in edit mode.
		if(editAnimation) {
			switch (key) {
			//--------------------------------------
			//	Forward Kinematics
			//--------------------------------------
			
			case 'b':
				FileInOutMachine.saveKeyFramesToFile(keyframes);
				break;
			case 'n':
				snapCurrent();
				break;
			case 'w':
				body.moveUp();
				break;
			case 'a':
				body.moveLeft();
				break;
			case 's':
				body.moveDown();
				break;
			case 'd':
				body.moveRight();
				break;
			case 'q':
				body.rotate(ANGLE_INCR);
				break;
			case 'e':
				body.rotate(-ANGLE_INCR);
				break;
			case 'z':
				body.getLimbs().get(limbSelected).rotateJoint(0, ANGLE_INCR);
				break;
			case 'x':
				body.getLimbs().get(limbSelected).rotateJoint(0, -ANGLE_INCR);
				break;
			case 'c':
				body.getLimbs().get(limbSelected).rotateJoint(1, ANGLE_INCR);
				break;
			case 'v':
				body.getLimbs().get(limbSelected).rotateJoint(1, -ANGLE_INCR);
				break;
			}
		}
	}

	public void mouseReleased() {
		float mouseXW = PIXELS_TO_WORLD_SCALE * (mouseX - ORIGIN_X);
		float mouseYW = -PIXELS_TO_WORLD_SCALE * (mouseY - ORIGIN_Y);
		
		if(startScreen)
			startUI.checkIsInside(mouseXW, mouseYW);
		else {
			if(editAnimation) {
				editAnimUI.checkIsInside(mouseXW, mouseYW);
				limbsUI.checkIsInside(mouseXW, mouseYW);
			}else
				playAnimUI.checkIsInside(mouseXW, mouseYW);
			if(showJumpTo)
				jumpToUI.checkIsInside(mouseXW, mouseYW);
		}
		
	}
	
	/**
	 * This initalizes the menu that shows when you first run the application.
	 */
	public void initStartUI() {
		startScreen = true;
		ArrayList<String> buttonNames = new ArrayList<String>();
		ArrayList<Runnable> r = new ArrayList<Runnable>(); 
		buttonNames.add(" New  ");
		buttonNames.add(" Open  ");
		r.add(new Runnable() { public void run() {newAnim();}});
		r.add(new Runnable() { public void run() {
			try {
				keyframes = FileInOutMachine.getKeyframesFromFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initializeEverything();
			startScreen = false;
			}});
		startUI = new Menu(r, buttonNames, buttonY-3f, WORLD_WIDTH/4, 2f, WORLD_X_MIN);
	}

	/**
	 * These buttons also have keyboard control counterparts, so they use the same methods
	 * as some keyboard controls.
	 */
	public void initAnimUI() {
		ArrayList<Runnable> r = new ArrayList<Runnable>();
		r.add(new Runnable() { public void run() {snapCurrent();}});
		r.add(new Runnable() { public void run() {FileInOutMachine.saveKeyFramesToFile(keyframes);}});
		//r.add(new Runnable() { public void run() {decreaseTimeVal();}});
		//r.add(new Runnable() { public void run() {increaseTimeVal();}});
		r.add(new Runnable() { public void run() {playAnim(); resetAnim();}});
		
		ArrayList<String> buttonNames = new ArrayList<String>();
		// There are extra spaces so that the text gets seen as wider and scales the text size
		//  appropriately.
		buttonNames.add("  Snapshot  ");
		buttonNames.add("  Save All  ");
		//buttonNames.add(" <  ");
		//buttonNames.add("  > ");
		buttonNames.add("   Play  ");

		editAnimUI = new Menu(r, buttonNames, buttonY, WORLD_WIDTH, 2f, WORLD_X_MIN);
	}
	
	/**
	 * The ui for selecting which limb to edit with the keyboard controls is made here.
	 * Also the second menu for the edit mode.
	 */
	public void initLimbsUI() {
		ArrayList<Runnable> r = new ArrayList<Runnable>();
		ArrayList<String> buttonNames = new ArrayList<String>();
		for(int i = 0; i< body.getLimbs().size(); i++) {
			buttonNames.add("Limb "+(i+1));
			int whichLimb = i;
			r.add(new Runnable() { public void run() { selectLimb(whichLimb);}});
		}
		limbsUI = new Menu(r, buttonNames, buttonY-3f, WORLD_WIDTH, 2f, WORLD_X_MIN);
	}
	
	/**
	 * The menu that shows in play mode has its functions and button names made here.
	 */
	public void initPlayUI(){
		ArrayList<Runnable> r = new ArrayList<Runnable>();
		r.add(new Runnable() { public void run() {playAnim();}});
		r.add(new Runnable() { public void run() {pauseAnim();}});
		r.add(new Runnable() { public void run() {resetAnim();}});
		r.add(new Runnable() { public void run() {newAnim();}});
		r.add(new Runnable() { public void run() {editAnim();}});
		
		ArrayList<String> buttonNames = new ArrayList<String>();
		buttonNames.add("Play");
		buttonNames.add("Pause");
		buttonNames.add("Restart");
		buttonNames.add("New");
		buttonNames.add("Edit");
		
		playAnimUI = new Menu(r, buttonNames, buttonY, WORLD_WIDTH/2, 2f, WORLD_X_MIN);
	}
	
	/**
	 * This menu only shows the time value of the animation, but it needs to be shown in
	 * multiple modes, so the button gets its own menu
	 */
	public void initTimeSelectUI() {
		ArrayList<Runnable> r = new ArrayList<Runnable>();
		r.add(new Runnable() { public void run() {blah();}});
		
		ArrayList<String> buttonNames = new ArrayList<String>();
		buttonNames.add("Time " + timeSelected);
		
		timeSelectUI = new Menu(r, buttonNames, WORLD_Y_MIN+10f, WORLD_WIDTH/8, 2f, WORLD_X_MAX-10f);
	}
	
	/**
	 * The UI for jumping to any given saved keyframe (not currently using, 
	 * because it doesn't get updated based on edits to the animation)
	 */
	public void initJumpToUI() {
		ArrayList<String> buttonNames = new ArrayList<String>();
		ArrayList<Runnable> r = new ArrayList<Runnable>();
		for(int i = 0; i<keyframes.size(); i++) {
			buttonNames.add("frame "+ i);
			int frame = i;
			r.add(new Runnable() { public void run() {jumpTo(frame);}});
		}
		jumpToUI = new Menu(r, buttonNames, buttonY-3f, WORLD_WIDTH, 2f, WORLD_X_MIN);
	}
	
	/**
	 * update the timekeeping button
	 * @param time
	 */
	public void updateTime(float time) {
		timeSelectUI.get(0).changeText("  "+time);
	}	
	
	/**
	 * jump forward or backward in the animation to the selected frame
	 * @param frame
	 */
	public void jumpTo(int frame) {
		body.jumpTo(frame);
		println("Jumping to frame "+ frame);
		updateTime(keyframes.get(frame).getT());
		frameSelectTime = keyframes.get(frame).getT();
	}
	
	//placeholder for what happens when you press the time-keeping button
	public void blah() {}
	
	/**
	 * If I want to add the ability to increment the time a keyframe gets made for (to make
	 * the change slower)
	 */
	public void increaseTimeVal() {
		if(timeSelected < 501)
			timeSelected++;
		updateTime(timeSelected);
	}
	
	/**
	 * If I want to add the ability to decrement the time a keyframe gets made for (to make
	 * the change faster)
	 */
	public void decreaseTimeVal() {
		if(timeSelected>keyframes.get((keyframes.size()-1)).getT())
			timeSelected--;
		updateTime(timeSelected);
	}
	
	/**
	 * Pressing play just enables animate and makes the editing menu disappear
	 */
	public void playAnim() {
		animate = true;
		editAnimation = false;
		println("playing");
	}
	
	/**
	 * restart the animation from the beginning
	 */
	public void resetAnim() {
		jumpTo(0);
		timeReset = millis();
		body.restartAnim();
		animate = true;
	}
	
	/**
	 * disable the animation playing.  Press play to begin it again from the point pause
	 * was pressed
	 */
	public void pauseAnim() {
		animate = false;
		println("paused");
	}
	
	/**
	 * Stop animating when editing and make the body go back to the first frame.
	 */
	public void editAnim() {
		//showJumpTo = true;
		editAnimation = true;
		animate = false;
		jumpTo(0);
	}
	
	/**
	 * Resets the saved keyframes and puts the body back at the starting point.
	 */
	public void newAnim() {
		animate = false;
		startScreen = false;
		editAnimation = true;
		keyframes = new ArrayList<KeyFrame>();
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
		keyframes.add(start);
		initializeEverything();
	}
	
	public void initializeEverything() {
		body = new Body("Animations/player.txt", limbsOnBody, jointsOnLimbs, 8);
		
		lastTime = millis();
		initAnimUI();
		initPlayUI();
		initJumpToUI();
		initTimeSelectUI();
		initLimbsUI();
	}
	
	/**
	 * Adds the current position and orientation of the body and its limbs to the keyframe
	 * array and increments the time display
	 */
	public void snapCurrent() {
		keyframes.add(new KeyFrame(timeSelected, body.getX(), body.getY(), 
				body.getA(), body.getLimbAngles() ));
		println("Saved keyframe at "+ timeSelected+ " (seconds).");
		timeSelected+=defaultFrameLength;
		updateTime(timeSelected);
	}
	
	public void setupGraphicClasses_()
	{
		if (GraphicObject.setup(this) != 1)
		{
			println("A graphic classe\'s setup() method was called illegally before this class");
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		PApplet.main("animationEditor.Editor");
	}
}
