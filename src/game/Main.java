package game;

import java.util.ArrayList;

import processing.core.PApplet;

/**	The runtime application
 * 
 * @author Steph and Thomas
 *
 */
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
	
	/**
	 * Sets up the window with the right settings
	 */
	public void settings() 
	{
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	/**
	 * Sets the framerate, initializes objects, and performs other important processes at the start of the program
	 */
	public void setup() 
	{
		frameRate(600);
		setupGraphicClasses_();
		lastTime = millis();
		
//		testPlatform = new Platform(0, 0, 0, new Surface[]{new Surface(-10, -5, 20, 0), new Surface(4, 5, 8, 7*PI/5)});
		testPlatform = new Platform(60, 20, PI/4, new Surface(47, -2.5f, 20, 0), new float[] {5, 20, 5}, new float[] {PI/2, PI/2, PI/2});
		keysPressed = new ArrayList<Character>();
		enemies = new ArrayList<Enemy>();
//		enemies.add(new Enemy("", 2, 2, 4f, 2, 1));
		levels = new ArrayList<Level>();
		levels.add(new Level(30, new Platform[] {testPlatform, new Platform(0, 0, 0, new Surface(25, -15, 25, PI/2), new float[] {12}, new float[] {PI/2}), new Platform(0, 0, 0, new Surface(-5, -10, 80, 0), new float[] {20, 30, 14}, new float[] {PI/16, PI/9, PI/15} )}, 10, 2));
		setCurrentLevel(0);

		player = new Player(levels.get(0).getStartX(), levels.get(0).getStartY());
	}
	
	/**
	 * Overrides PApplet's draw. Runs every frame
	 */
	public void draw() 
	{
		// Supposed to prevent the camera from moving off of the level
		if (camX > offsetMaxX)
		    camX = offsetMaxX;
		else if (camX < offsetMinX)
		    camX = offsetMinX;
		
		// Centers the camera on the player
		camX = player.getX();
		
		// Calls the method which draws to the screen
		actuallyDraw();
		
		// Calls the method which handles movement and animation 
		animate();
		
	}
	
	/**
	 * Draws the game objects to the window
	 */
	public void actuallyDraw() {
		// increment the frame counter
		frame++;
		
		// draw only every 5 frames
		if (frame % 5 == 0) {
			background(167);
			
			pushMatrix();
			
			translate(ORIGIN_X, ORIGIN_Y);
	 		
	 		scale(WORLD_TO_PIXELS_SCALE, -WORLD_TO_PIXELS_SCALE);	
			
	 		
			strokeWeight(0.2f);
			
			// Moves all objects to the left as the player moves right, 
			// creating the appearance of the camera following the player.
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
	
	/**
	 * 	Where objects move and animate
	 */
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
			boolean isGrounded = false;
			for(int i = 0; i < current.getPlatforms().length; i++) {
				boolean[] collisionResult = current.getPlatforms()[i].testCollision(player);
				// If a collision occurred, reset the loop
				if (collisionResult[0]) {
					i = -1;
				}
				// Otherwise, check if the character is grounded
				else if (collisionResult[1]) {
					isGrounded = true;
				}
			}
			
//			println("NO SERIOUSLY REALLY GROUNDED " + isGrounded);
			player.setGrounded(isGrounded);

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
	
	/**	Sets the current level to the index provided
	 * 
	 * @param index		The level to use
	 */
	public void setCurrentLevel(int index) {
		current = levels.get(index);
		offsetMaxX = current.getWidth() - WORLD_WIDTH;
		offsetMinX = 0;
	}
	
	/**	
	 * Adds pressed keys to an ArrayList of keys
	 */
	public void keyPressed() {
		if(!keysPressed.contains(key))
			keysPressed.add(key);
	}
	
	/**
	 * Removes released keys from the ArrayList of keys
	 */
	public void keyReleased() {
		keysPressed.remove(keysPressed.indexOf(key));
	}
	
	/**
	 * Provides GraphicObject with a reference to the app
	 */
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