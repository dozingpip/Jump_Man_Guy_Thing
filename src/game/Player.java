package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class Player extends Entity{

	float hitboxAngle = PApplet.PI/4;
	static int numLimbs = 4;
	static int numJoints = 2;
	boolean grounded = false;

	static String playerAnimFile = "player.txt";
	static float torsoSize = 8f;
	static int playerMaxHealth = 5;
	static float moveSpeed = 0.05f;
	
	public Player() {
		super(playerAnimFile, numLimbs, numJoints, torsoSize, playerMaxHealth, moveSpeed);
	}
	public Player(float x, float y) {
		super(x, y, playerAnimFile, numLimbs, numJoints, torsoSize, playerMaxHealth, moveSpeed);
	}
	
	public void draw() {
		super.draw();
	}
	
	public void move(char k) {
		switch(k) {
			case 'w':
				jump();
				break;
			case 'a':
				walkLeft();
				break;
			case 'd':
				walkRight();
				break;
		}
	}
	
	public void move(ArrayList<Character> keys) {
		for(Character c: keys)
			move(c);
	}
	
	public void jump() {
		moveUp();
		body.jump();
	}
	
	public void die() {
		body.die();
	}
	
	public void walkLeft() {
		body.walk();
		moveBy(-getMoveSpd(), 0);
	}
	
	public void walkRight() {
		body.walk();
		moveBy(getMoveSpd(), 0);
	}

}
