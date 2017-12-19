package game;

import java.util.ArrayList;

/**
 * The player class
 * @author Steph and Thomas
 *
 */
public class Player extends Entity{

	private static final float INITIAL_JUMP_SPD = 40;
	private static int numLimbs = 4;
	private static int numJoints = 2;

	private static String playerAnimFile = "player.txt";
	private static float torsoSize = 8f;
	private static int playerMaxHealth = 5;
	private static float moveSpeed = 30f;
	
	/**
	 * initialize the player at the default position of 0, 0.
	 */
	public Player() {
		super(playerAnimFile, numLimbs, numJoints, torsoSize, playerMaxHealth, moveSpeed);
	}
	
	/**
	 * initializes the player at the specified position
	 * @param x x coord of where to spawn the player
	 * @param y y coord of where to spawn the player
	 */
	public Player(float x, float y) {
		super(x, y, playerAnimFile, numLimbs, numJoints, torsoSize, playerMaxHealth, moveSpeed);
	}
	
	/**
	 * draw the player
	 */
	public void draw() {
		super.draw();
	}
	
	public void jump(float dt) {
		if(checkGrounded()) {
			setVY(INITIAL_JUMP_SPD);
			body.jump();
		}
	}
	
	/**
	 * move the player according to the key pressed and change in time
	 * @param k which key was pressed
	 * @param dt
	 */
	public void move(char k, float dt) {
		switch(k) {
			case 'w':
				jump(dt);
				break;
			case 'a':
				walkLeft(dt);
				break;
			case 'd':
				walkRight(dt);
				break;
		}
	}
	
	/**
	 * make the moves happen when more than one key is pressed at the same time
	 * @param keys
	 * @param dt
	 */
	public void move(ArrayList<Character> keys, float dt) {
		for(Character c: keys)
			move(c, dt);
	}
}
