package game;

import processing.core.PApplet;

public class Main extends PApplet implements ApplicationConstants{
	
	Player player;
	
	Platform testPlatform;
	
	private boolean animate = true;
	private float lastTime;
	private float animStart =0;
	private long frame = 0L;
	int currentEditingFrame = 1;
	boolean grounded = false;
	
	public void settings() 
	{
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void setup() 
	{
		frameRate(600);
		setupGraphicClasses_();
		lastTime = millis();
		player = new Player();
		
//		testPlatform = new Platform(0, 0, 0, new Surface[]{new Surface(-10, -5, 20, 0), new Surface(4, 5, 8, 7*PI/5)});
		testPlatform = new Platform(0, 0, PI/4, new Surface(-10, -2.5f, 20, 0), new float[] {5, 20, 5}, new float[] {PI/2, PI/2, PI/2});
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
			strokeWeight(0.2f);
			line(WORLD_X_MIN, 0, WORLD_X_MAX, 0);
			
			testPlatform.draw();
						
			player.draw();
			
			
			popMatrix();
		}
		
		testPlatform.setAngle(testPlatform.getAngle() + PI/1024);
		if(testPlatform.checkColliding(player)) {
			grounded = true;
		}else {
			grounded = false;
		}
		
		float t = millis()-animStart;
		
		if (animate)
		{
			//	time in seconds since last update: (t-lastTime_)*0.001f
			float dt = (t-lastTime)*0.001f;
			
			if(player.isAlive()) {
				player.update(dt);
			}else {
				animate = false;
			}
		}

		lastTime = t;
		
	}
	
	public void keyPressed() {
		
		animStart = millis();
		player.move(key);
		println(key);
		/*
		switch(key) {
			case 'w': case 'd': case 'a': 
				animStart = millis();
				player.move(key);
				break;
		}*/
	}
	
	public void keyReleased() {
		switch(key) {
			//case 'w': case 'd': case 'a': 
	//		case '[':
	//			testPlatform.setAngle(testPlatform.getAngle() - PI/16);
	//			break;
	//		case ']':
	//			testPlatform.setAngle(testPlatform.getAngle() + PI/16);
	//			break;
			default:
				//animStart = millis();
				//player.stop(key);
				break;
		}
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
