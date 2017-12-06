package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class Main extends PApplet implements ApplicationConstants{
	
	Player player;
	
	private final float ANGLE_INCR = PI/16;
	private boolean animate = false;
	private float lastTime;
	private long frame = 0L;
	ArrayList<KeyFrame> keyframes;
	int currentEditingFrame = 1;
	
	public void settings() 
	{
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void setup() 
	{
		setupGraphicClasses_();
		lastTime = millis();
		player = new Player();
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
			stroke(0);
			line(WORLD_X_MIN, 0, WORLD_X_MAX, 0);
			
			player.draw();
			
			
			popMatrix();
		}
		
		float t = millis();
		
		if (animate)
		{
			
			//	time in seconds since last update: (t-lastTime_)*0.001f
			float dt = (t-lastTime)*0.001f;
			
			/*if(!body.isAnimDone()) {
				body.update(dt);
			}else {
				animate = false;
				println("Animation complete.");
			}*/
		}

		lastTime = t;
		
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
		PApplet.main("game.Main");
	}
}
