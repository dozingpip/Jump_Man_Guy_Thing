package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class Main extends PApplet implements ApplicationConstants{
	
	Player player;
	
	Platform testPlatform;
	
	private boolean animate = true;
	private float lastTime;
	private long frame = 0L;
	ArrayList<Character> keysPressed;
	ArrayList<Enemy> enemies;
	
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
		keysPressed = new ArrayList<Character>();
		enemies = new ArrayList<Enemy>();
		//enemies.add(new Enemy("", 2, 2, 4f, 2, 1));
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
			
			//testPlatform.draw();
			
			for(Enemy e: enemies) {
				if(e.isAlive() && e.onScreen(WORLD_X_MIN, WORLD_Y_MIN, WORLD_X_MAX, WORLD_Y_MAX)) {
					e.draw();
				}
			}
						
			player.draw();
			
			
			popMatrix();
		}
		
		testPlatform.setAngle(testPlatform.getAngle() + PI/1024);
		//testPlatform.checkColliding(player);
		
		float t = millis();
		
		if (animate)
		{
			if(keysPressed.isEmpty()) {
				player.stop();
			}else {
				player.move(keysPressed);
			}
			//	time in seconds since last update: (t-lastTime_)*0.001f
			float dt = (t-lastTime)*0.001f;
			
			if(player.isAlive()) {
				player.update(dt);
			}else {
				animate = false;
			}
			
			for(Enemy e: enemies) {
				if(e.isAlive() && e.onScreen(WORLD_X_MIN, WORLD_Y_MIN, WORLD_X_MAX, WORLD_Y_MAX)) {
					e.update(dt);
				}
			}
		}

		lastTime = t;
		
	}
	
	public void keyPressed() {
		if(!keysPressed.contains(key))
			keysPressed.add(key);
	}
	
	public void keyReleased() {
		keysPressed.remove(keysPressed.indexOf(key));
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
