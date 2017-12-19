package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class Main extends PApplet implements ApplicationConstants{
	
	Player player;
	
	Platform testPlatform;
	float offsetMaxX, offsetMaxY, offsetMinX, offsetMinY;
	float camX, camY;
	
	private boolean animate = true;
	private float lastTime;
	private long frame = 0L;
	ArrayList<Character> keysPressed;
	ArrayList<Enemy> enemies;
	ArrayList<Level> levels;
	Level current;
	
	public void settings() 
	{
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void setup() 
	{
		frameRate(600);
		setupGraphicClasses_();
		lastTime = millis();
		
//		testPlatform = new Platform(0, 0, 0, new Surface[]{new Surface(-10, -5, 20, 0), new Surface(4, 5, 8, 7*PI/5)});
		testPlatform = new Platform(0, 0, PI/4, new Surface(-10, -2.5f, 20, 0), new float[] {5, 20, 5}, new float[] {PI/2, PI/2, PI/2});
		keysPressed = new ArrayList<Character>();
		enemies = new ArrayList<Enemy>();
		enemies.add(new Enemy("default_spider.txt", 2, 2, 4f, 2, 1, 0.05f));
		levels = new ArrayList<Level>();
		levels.add(new Level(30, new Platform[] {testPlatform, new Platform(0, 0, 0, new Surface(-5, -10, 80, 0), new float[] {20, 30, 14}, new float[] {PI/16, PI/8, PI/15} )}, 10, 2));
		setCurrentLevel(0);

		player = new Player(levels.get(0).getStartX(), levels.get(0).getStartY());
	}
	
	public void draw() 
	{
		if (camX > offsetMaxX)
		    camX = offsetMaxX;
		else if (camX < offsetMinX)
		    camX = offsetMinX;
		
		camX = player.getX();
		
		actuallyDrawing();
		
		animate();
		
	}
	
	public void animate() {
		float t = millis();
		
		if (animate)
		{
			//	time in seconds since last update: (t-lastTime_)*0.001f
			float dt = (t-lastTime)*0.001f;
			
			player.doGravity(dt);
			
			// Positional Movement
			if(!keysPressed.isEmpty()) {
				player.move(keysPressed, dt);
			}else {
				player.stop();
			}
			
			// Collisions
			for(Platform p: current.getPlatforms()) {
				p.doCollision(player);
			}
//			for(Platform p : current.getPlatforms()) {
//				p.doCollision(player);
//			}
			
			player.doMove();
			
			if(player.isAlive()) {
				player.update(dt);
			}else {
				animate = false;
			}
			
			for(Enemy e: enemies) {
				if(e.isAlive()) {
					e.update(dt);
				}
			}
		}

		lastTime = t;
	}
	
	public void actuallyDrawing() {
		frame++;
		if (frame % 5 == 0) {
			background(167);
			
			pushMatrix();
			
			translate(ORIGIN_X, ORIGIN_Y);
	 		
	 		scale(WORLD_TO_PIXELS_SCALE, -WORLD_TO_PIXELS_SCALE);	
			
	 		
			strokeWeight(0.2f);
			
			translate(-camX, 0);
			
			player.draw();
			
			for(Platform p: current.getPlatforms()) {
				p.draw();
			}
			
			for(Enemy e: enemies) {
				if(e.isAlive()) {
					e.draw();
				}
			}
			
			popMatrix();
		}
	}
	
	public void setCurrentLevel(int index) {
		current = levels.get(index);
		offsetMaxX = current.getWidth() - WORLD_WIDTH;
		offsetMinX = 0;
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