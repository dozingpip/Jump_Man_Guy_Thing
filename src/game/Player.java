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
	static float moveSpeed = 30f;
	
	public Player() {
		super(playerAnimFile, numLimbs, numJoints, torsoSize, playerMaxHealth, moveSpeed);
	}
	public Player(float x, float y) {
		super(x, y, playerAnimFile, numLimbs, numJoints, torsoSize, playerMaxHealth, moveSpeed);
	}
	
	public void draw() {
		super.draw();
	}
	
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
	
	public void move(ArrayList<Character> keys, float dt) {
		for(Character c: keys)
			move(c, dt);
	}
	
	public void jump(float dt) {
		moveUp(dt);
		body.jump();
	}
	
	public void die() {
		body.die();
	}
	
	public void walkLeft(float dt) {
		body.walk();
		moveLeft(dt);
	}
	
	public void walkRight(float dt) {
		body.walk();
		moveRight(dt);
	}

}
